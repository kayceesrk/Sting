package sessionj.compiler.visit;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.frontend.*;
import polyglot.main.Options;
import sessionj.ExtensionInfo;
import sessionj.SJConstants;
import sessionj.types.SJTypeSystem;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sessvars.SJVariable;
import sessionj.ast.sessvars.SJLocalSocket_c;
import sessionj.visit.SJCompoundOperationTranslator;

import java.util.*;
import java.util.regex.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
 
public class SJWhileTranslationTest {
    private Position dummyPos;
    private Expr trueLit;
    private ExtensionInfo extInfo;
    private SJCompoundOperationTranslator visitor;
    private Stmt emptyBlock;
    private List targets;
    private List sources;

    @Test
    public void translateOutwhile() throws SemanticException, IOException {
        verifyBlock(
                visitor.leaveCall(
                    null, null,
                    new SJOutwhile_c(dummyPos, trueLit, emptyBlock, targets, false),
                    null),
                Pattern.quote(
                "{\n" +
                "    sessionj.runtime.net.LoopCondition ") +
                        javaIdentifier() +
                        Pattern.quote(" =\n" +
                "      sessionj.runtime.net.SJRuntime.negotiateOutsync(\n" +
                "        false, new sessionj.runtime.net.SJSocket[] { tgtSock });\n" +
                "    while (") +
                                "\\1" +
                        Pattern.quote(".call(true)) {  }\n" +
                "}")
        );
    }

    @Test
    public void translateInterruptibleOutwhile() throws SemanticException, IOException {
        verifyBlock(
                visitor.leaveCall(
                        null, null,
                        new SJOutwhile_c(dummyPos, trueLit, emptyBlock, targets, true),
                        null),
                Pattern.quote(
                        "{\n" +
                                "    sessionj.runtime.net.LoopCondition ") +
                        javaIdentifier() +
                        Pattern.quote(" =\n" +
                                "      sessionj.runtime.net.SJRuntime.negotiateOutsync(\n" +
                                "        true, new sessionj.runtime.net.SJSocket[] { tgtSock });\n" +
                                "    while (") +
                        "\\1" +
                        Pattern.quote(".call(true)) {  }\n" +
                                "}")
        );
    }
    private void verifyBlock(Node n, String expectedCode) throws IOException {
        assert n instanceof Block;
        assert n.isTypeChecked();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        n.prettyPrint(os);
        // Handy for debugging.
        //n.prettyPrint(System.out);
        //System.out.flush();
        assert os.toString().matches(expectedCode);
        os.close();
    }

    @Test
    public void translateInwhile() throws SemanticException, IOException {
        verifyBlock(
            visitor.leaveCall(null, null,
                    new SJInwhile_c(dummyPos, emptyBlock, sources),
                    null),
            Pattern.quote(
                "{\n" +
                "    sessionj.runtime.net.SJRuntime.negotiateNormalInwhile(\n" +
                "      new sessionj.runtime.net.SJSocket[] { srcSock });\n" +
                "    while (sessionj.runtime.net.SJRuntime.insync(\n" +
                "             new sessionj.runtime.net.SJSocket[] { srcSock })) {\n" +
                "        \n" +
                "    }\n" +
                "}"
            )
        );
    }

    @Test
    public void translateOutinwhile() throws SemanticException, IOException {
        verifyBlock(
                visitor.leaveCall(null, null,
                        new SJOutInwhile_c(dummyPos, trueLit, emptyBlock, targets, sources),
                        null),
                Pattern.quote(
                "{\n" +
                "    sessionj.runtime.net.LoopCondition ") +
                        javaIdentifier() +
                        Pattern.quote(" =\n" +
                "      sessionj.runtime.net.SJRuntime.negotiateOutsync(\n" +
                "        false, new sessionj.runtime.net.SJSocket[] { tgtSock });\n" +
                "    boolean ") +
                        javaIdentifier() +
                        Pattern.quote(" =\n" +
                "      sessionj.runtime.net.SJRuntime.negotiateInterruptingInwhile(\n" +
                "        new sessionj.runtime.net.SJSocket[] { srcSock });\n" +
                "    while (") +
                                "\\1" +
                        Pattern.quote(".call(\n" +
                "             sessionj.runtime.net.SJRuntime.interruptingInsync(\n" +
                "               true, ") +
                                "\\2" +
                        Pattern.quote(",\n" +
                "               new sessionj.runtime.net.SJSocket[] { srcSock }))) {\n" +
                "        \n" +
                "    }\n" +
                "}")
        );
    }

    @Test
    public void translateOutinwhileNoCond() throws SemanticException, IOException {
        verifyBlock(
                visitor.leaveCall(null, null,
                        new SJOutInwhile_c(dummyPos, null, emptyBlock, targets, sources),
                        null),
                Pattern.quote(
                "{\n" +
                "    sessionj.runtime.net.LoopCondition ") +
                        javaIdentifier() +
                        Pattern.quote(" =\n" +
                "      sessionj.runtime.net.SJRuntime.negotiateOutsync(\n" +
                "        false, new sessionj.runtime.net.SJSocket[] { tgtSock });\n" +
                "    sessionj.runtime.net.SJRuntime.negotiateNormalInwhile(\n" +
                "      new sessionj.runtime.net.SJSocket[] { srcSock });\n" +
                "    while (") +
                                "\\1" +
                        Pattern.quote(".call(\n" +
                "             sessionj.runtime.net.SJRuntime.insync(\n" +               
                "               new sessionj.runtime.net.SJSocket[] { srcSock }))) {\n" +
                "        \n" +
                "    }\n" +
                "}")
        );
            
    }

    private String javaIdentifier() {
        return "([\\$\\w]+)";
    }

    @BeforeMethod
    protected void setUp() throws SemanticException {
        dummyPos = new Position("", "");
        trueLit = new BooleanLit_c(dummyPos, true);

        emptyBlock = new Block_c(dummyPos, new LinkedList());
        SJVariable sockVar = sockVariable("tgtSock");
        targets = new LinkedList();
        targets.add(sockVar);
        sources = new LinkedList();
        SJVariable sockVar2 = sockVariable("srcSock");
        sources.add(sockVar2);

        initCompilerAndVisitor();
    }

    private void initCompilerAndVisitor() throws SemanticException {
        extInfo = new TestExtensionInfo();

        Options.global = new Options(extInfo);
        SJTypeSystem ts = (SJTypeSystem) extInfo.typeSystem();
        SJConstants.SJ_SOCKET_INTERFACE_TYPE = ts
                .typeForName(SJConstants.SJ_SOCKET_INTERFACE);
        Job job = new Job(extInfo, extInfo.jobExt(),
                          new Source("FakeFile.sj", "", new Date()), null);
        visitor = new SJCompoundOperationTranslator
                (job, ts, extInfo.nodeFactory());
        visitor.begin();
        ParsedClassType scope = ts.createClassType();
        scope.name("TestClass");
        scope.kind(ClassType.TOP_LEVEL);

        Context context = visitor.context();
        context = context.pushSource(new ImportTable(ts, new Package_c(ts, "")))
                         .pushClass(scope, scope);
        visitor = (SJCompoundOperationTranslator) visitor.context(context);
    }

    private SJVariable sockVariable(String id) {
        return new SJLocalSocket_c(dummyPos, new Id_c(dummyPos, id));
    }

}
