package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;
import polyglot.qq.QQ;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.UniqueID;
import polyglot.visit.*;
import sessionj.SJConstants;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessvars.SJVariable;
import sessionj.extension.SJExtFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.contexts.SJContextElement;
import sessionj.types.contexts.SJContextInterface;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.sesstypes.SJSetType;
import sessionj.util.SJCompilerUtils;
import sessionj.util.SJTypeEncoder;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;
import static sessionj.visit.SJSessionOperationTypeBuilder.getTargetNames;

import java.util.*;

public class SJTypecase_c extends Stmt_c implements SJTypecase {
    private final Receiver ambiguousSocket;
    private final SJVariable socket;
    private final List<SJWhen> whenStatements;

    public SJTypecase_c(Position pos, Receiver ambiguousSocket, List<SJWhen> whenStatements, SJVariable socket) {
        super(pos);
        assert ambiguousSocket != null;
        this.ambiguousSocket = ambiguousSocket;
        this.socket = socket;
        assert whenStatements != null;
        this.whenStatements = Collections.unmodifiableList(whenStatements);
    }

    public List<Receiver> ambiguousTargets() {
        return Collections.singletonList(ambiguousSocket);
    }

    public List<SJVariable> resolvedTargets() {
        return Collections.singletonList(socket);
    }

    public SJSessionOperation resolvedTargets(List<SJVariable> resolved) {
        return targets(resolved);
    }

    public List<Receiver> targets() {
        return Collections.singletonList(ambiguousSocket);
    }

    public SJSessionOperation targets(List target) {
        if (target.size() != 1) throw new IllegalArgumentException
            ("Typecase only supports one target, got: " + target);
        Object o = target.get(0);
        if (!(o instanceof SJVariable))
            throw new IllegalArgumentException("Tried to update target with a non-SJVariable:" + o);
        return new SJTypecase_c(position, ambiguousSocket, whenStatements, (SJVariable) o);
    }

    public Node buildType(ContextVisitor cv, SJTypeSystem sjts, SJExtFactory sjef) {
        List<String> sjnames = getTargetNames(resolvedTargets(), false);
        //SJSessionType st = sjts.SJSetType(Collections.singletonList((SJSessionType_c) sjts.SJUnknownType()));
        SJSessionType st = sjts.SJSetType(Collections.singletonList((SJSessionType) sjts.SJUnknownType()));
        return SJCompilerUtils.setSJSessionOperationExt(sjef, this, st, sjnames);
    }

    public void enterSJContext(SJContextInterface sjcontext) throws SemanticException {
        sjcontext.pushSJTypecase(this);
    }

    public SJContextElement leaveSJContext(SJContextInterface sjcontext) throws SemanticException {
        return sjcontext.pop();
    }

    public SJCompoundOperation sessionTypeCheck(SJSessionType typeForNode) throws SemanticException {
        if (!(typeForNode instanceof SJSetType))
            throw new SemanticException("Typecase should have a set type");
        SJSetType set = (SJSetType) typeForNode;
        if (!set.containsAllAndOnly(branchTypes()))
            throw new SemanticException("Typecase branches:\n" + whenStatements
                + "do not match with set type: " + set);
        return this;
    }

    public Node translate(QQ qq, ContextVisitor visitor, Collection<ClassMember> fieldsToAdd) throws SemanticException {
        StringBuilder statement = new StringBuilder();
        List<Object> parameters = new LinkedList<Object>();
        SJTypeEncoder sjte = new SJTypeEncoder((SJTypeSystem) visitor.typeSystem());

        String tmpVar = UniqueID.newID(SJConstants.SJ_TMP_LOCAL);
        statement.append("{ sessionj.types.sesstypes.SJSessionType ")
            .append(tmpVar).append(" = %s.remainingSessionType();");
        parameters.add(socket.sjname());

        for (SJWhen when : whenStatements) {
            String fieldName = UniqueID.newID(SJConstants.SJ_TMP_TYPECASE);
            
            statement.append("if (%s.typeEquals(%s)) { %LS; } else ");
            parameters.add(tmpVar);
            parameters.add(fieldName);
            // QQ tries to modify the list in some cases, hence the copy.
            parameters.add(copyOfStatements(when)); 
            
            FieldDecl f = (FieldDecl) qq.parseMember(
                "private static final sessionj.types.sesstypes.SJSessionType %s = SJRuntime.decodeType(%E);", 
                    fieldName, 
                    new StringLit_c(Position.compilerGenerated(), sjte.encode(when.type()))
            );
            f = (FieldDecl) buildAndCheckTypes(visitor, f);
            fieldsToAdd.add(f);
        }
        // There's always at least one when branch (guaranteed by typing).

        // statement.append(" { assert false : \"Typecase given an unexpected type:\"+ %s;}");
        statement.append("{ throw new sessionj.runtime.SJIOException(\"Typecase given an unexpected type: \" + %s); }"); // FIXME: factor out SJIOException constant.
        parameters.add(tmpVar);

        statement.append('}');

        return qq.parseStmt(statement.toString() , parameters);

    }

    private List<Stmt> copyOfStatements(SJWhen when) {
        return new LinkedList<Stmt>(when.statements());
    }

    private Collection<SJSessionType> branchTypes() {
        Collection<SJSessionType> res = new LinkedList<SJSessionType>();
        for (SJWhen when : whenStatements) res.add(when.type());
        return res;
    }

    // FIXME: The following are adapted from Switch_c. - duplicated in SJInbranch_c
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) // This is largely redundant (except for debugging) due to later translation.
    {
        w.write(SJConstants.SJ_KEYWORD_TYPECASE + '(' + ambiguousSocket + ')');
        w.write(" {");
        w.allowBreak(4, " ");
        w.begin(0);

        for (SJWhen when : whenStatements) {
            w.allowBreak(4, " ");
            print(when, w, tr);
        }

        w.end();
        w.allowBreak(0, " ");
        w.write("}");
    }

    public Context enterScope(Context c)
    {
        return c.pushBlock();
    }

    public Node visitChildren(NodeVisitor v)
    {
        List<SJWhen> newWhenStatements = visitList(whenStatements, v);
        return reconstruct(newWhenStatements);
    }

    private Node reconstruct(List<SJWhen> newWhenStatements) {
        Node newThis = new SJTypecase_c(position, ambiguousSocket, newWhenStatements, socket);
        if (ext() != null) {
            newThis = newThis.ext((Ext) ext.copy());
        }
        if (del != null) { // the del() method returns this if del is null, so use field to check
            newThis = newThis.del((JL) del().copy());
        }
        return newThis;
    }


    public Term firstChild()
    {
        return whenStatements.isEmpty() ? null : whenStatements.get(0);
    }

    public List acceptCFG(CFGBuilder v, List succs)
    {
        List<Term> cases = new LinkedList<Term>();
        List<Integer> entry = new LinkedList<Integer>();

        for (SJWhen when : whenStatements)
        {
            cases.add(when);
            entry.add(ENTRY);
        }

        cases.add(this);
        entry.add(EXIT);

        v.visitCFG(firstChild(), FlowGraph.EDGE_KEY_OTHER, cases, entry); // entry...

        v.push(this).visitCFGList(whenStatements, this, EXIT); // ...and exit points?

        return succs;
    }

}
