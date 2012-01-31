/**
 * 
 */
package sessionj.util;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.visit.*;
import sessionj.SJConstants;
import sessionj.ast.SJSpawn;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.typenodes.*;
import sessionj.extension.SJExtFactory;
import sessionj.extension.noalias.SJNoAliasExprExt;
import sessionj.extension.noalias.SJNoAliasExt;
import sessionj.extension.noalias.SJNoAliasFinalExt;
import sessionj.extension.noalias.SJNoAliasVariablesExt;
import sessionj.extension.sessops.SJSessionOperationExt;
import sessionj.extension.sesstypes.SJNamedExt;
import sessionj.extension.sesstypes.SJTypeableExt;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/**
 * @author Raymond 
 *
 */
public class SJCompilerUtils
{
	private SJCompilerUtils() { }
	
	public static void updateSJFieldInstance(FieldInstance fi, SJFieldInstance sjfi)
	{
		ParsedClassType pct = (ParsedClassType) fi.container();
		List<FieldInstance> fields = new LinkedList<FieldInstance>();

        for (Object o : pct.fields()) {
            fields.add((FieldInstance) o);
        }
		
		fields.remove(fi); // Done like this, just to be uniform with ConstructorDecl and MethodDecl.
		fields.add(sjfi);
		
		pct.setFields(fields); // Works because ParsedClassType is mutable (no need to reassign defensive copy).
	}

	// Generally, the "first" extension object should be for noalias typing, and the "second" for session typing... 
  public static SJTypeableExt getSJTypeableExt(Node n)
	{
		if (n.ext(2) == null) // SJCompoundOperation.
		{
			return (SJTypeableExt) n.ext(1); // ...but there can be some exceptions? // Yes: compound operations.
		}
		else 
		{
			return (SJTypeableExt) n.ext(2); // Factor out constant.
		}
	}
	
	public static Node setSJTypeableExt(SJExtFactory sjef, Node n, SJSessionType st)
	{
		return n.ext(2, sjef.SJTypeableExt(st)); // Factor out constant. // Won't be a SJCompoundOperation (so should have a SJNoAliasExt.
	}
	
	public static SJNamedExt getSJNamedExt(Node n)
	{
		return (SJNamedExt) getSJTypeableExt(n); 
	}
	
	public static Node setSJNamedExt(SJExtFactory sjef, Node n, SJSessionType st, String sjname)
	{
		return n.ext(2, sjef.SJNamedExt(st, sjname)); // Factor out constant. // Won't be a SJCompoundOperation.			
	}
	
	public static SJSessionOperationExt getSJSessionOperationExt(SJSessionOperation so)
	{
		return (SJSessionOperationExt) getSJTypeableExt(so);		
	}
	
	public static Node setSJSessionOperationExt(SJExtFactory sjef, SJSessionOperation so, SJSessionType st, List<String> sjnames)
	{
		if (so.ext(1) == null) // SJCompoundOperation.
		{
			return so.ext(1, sjef.SJSessionOperationExt(st, sjnames)); // Factor out constant.
		}
		else 
		{
			return so.ext(2, sjef.SJSessionOperationExt(st, sjnames)); // Factor out constant. 
		}
	}
	
	public static Node setSJSessionOperationExt(/*SJExtFactory sjef, */SJSpawn s, List<String> sjnames, List<SJSessionType> sts) // FIXME: SJSpawn is an example of multiply typed session operations.
	{
		/*if (s.ext(1) == null) // Not possible for SJSpawn. 
		{
			return so.ext(1, sjef.SJSessionOperationExt(st, targetNames)); // Factor out constant.
		}
		else*/ 
		{
			s = s.sjnames(sjnames);
			s = s.sessionTypes(sts); // FIXME: create an extension object for this information.
			
			return s; 
		}
	}
	
	public static SJSessionType getSessionType(Node n) // Should be SJTypeable parameter?
	{
		return getSJTypeableExt(n).sessionType();
	}

    public static SJNoAliasExt getSJNoAliasExt(Node n) // First ext link is for noalias.
	{
		return (SJNoAliasExt) n.ext(1); // Factor out constant.
	}
	
	public static Node setSJNoAliasExt(SJExtFactory sjef, Node n, boolean isNoAlias)
	{
		return n.ext(1, sjef.SJNoAliasExt(isNoAlias)); // Factor out constant.
	}

	public static SJNoAliasFinalExt getSJNoAliasFinalExt(Node n)
	{
		return (SJNoAliasFinalExt) getSJNoAliasExt(n);
	}
	
	public static Node setSJNoAliasFinalExt(SJExtFactory sjef, Node n, boolean isNoAlias, boolean isFinal)
	{
		SJNoAliasFinalExt nafe = sjef.SJNoAliasFinalExt(isNoAlias, isFinal);

		return n.ext(1, nafe);
	}
	
	public static SJNoAliasVariablesExt getSJNoAliasVariablesExt(Node n) // First ext link is for noalias.
	{
		return (SJNoAliasVariablesExt) n.ext(1);
	}

    public static SJNoAliasExprExt getSJNoAliasExprExt(Node n) // First ext link is for noalias.
	{
		return (SJNoAliasExprExt) getSJNoAliasExt(n);
	}

    //Only works post disambiguation pass. Prior to that, represented by SJAmbNoAliasTypeNode.
	public static boolean isNoAlias(Node n)
	{
		if (n instanceof TypeNode) // Ext object can be null for regular TypeNodes.*/
		{
			SJNoAliasExt nae = getSJNoAliasExt(n);		
			
			return nae != null && nae.isNoAlias();
		}
		else
		{
			return getSJNoAliasExt(n).isNoAlias();
			//return e.type() instanceof SJNoAliasReferenceType;
		}			
	}

	public static boolean isFinal(Node n)
	{		
		return getSJNoAliasFinalExt(n).isFinal();
		//return e.type() instanceof SJNoAliasReferenceType;
	}
	
	/*public static final void replaceASTNode(Job job, Node old, Node n)
	{
		job.ast(job.ast().visit(new SJNodeReplacer(old, n)));
	}*/

    public static Type subsumeMessageTypes(Type t1, Type t2, boolean forSend) throws SemanticException // false forSend means for receive.
	{
		if (t1 instanceof SJSessionType)
		{
			if (!(t2 instanceof SJSessionType))
			{
				throw new SemanticException("[SJCompilerUtils] Not subsumable: " + t1 + ", " + t2);
			}
	
			return ((SJSessionType) t1).subsume((SJSessionType) t2);
		}
		else if (t1 instanceof ReferenceType)
		{
			if (!(t2 instanceof ReferenceType)) // FIXME: could do some autoboxing for primitive types. 
			{
				throw new SemanticException("[SJCompilerUtils] Not subsumable: " + t1 + ", " + t2);
			}
	
			if (t1.isSubtype(t2)) // Could be checked earlier.
			{
				return forSend ? t2 : t1;
			}
			else if (t2.isSubtype(t1))
			{
				return (forSend) ? t1 : t2;
			}
			else
			{
				throw new SemanticException("[SJCompilerUtils] Full send type subsumption not yet supported: " + t1 + ", " + t2); // FIXME: e.g. String + Integer -> Object
			}
		}
		else if (t1.typeEquals(t2))
		{
			return t1;
		}
	
		throw new SemanticException("[SJCompilerUtils] Not subsumable: " + t1 + ", " + t2); // FIXME: some primitive types subsumable (e.g. char->int, int->dobule).
	}
	
	public static Node buildAndCheckTypes(ContextVisitor cv, Node n) throws SemanticException
	{
		TypeSystem ts = cv.typeSystem();
		NodeFactory nf = cv.nodeFactory();
			
		AmbiguityRemover ar = (AmbiguityRemover) new AmbiguityRemover(cv.job(), ts, nf, true, true).context(cv.context());
		
		n = disambiguateNode(ar, n); 
		
		TypeChecker tc = (TypeChecker) new TypeChecker(cv.job(), ts, nf).context(ar.context());
		
		n = n.visit(tc);
		
		ConstantChecker cc = (ConstantChecker) new ConstantChecker(cv.job(), ts, nf).context(tc.context());
		
		n = n.visit(cc); // FIXME: breaks SJProtocolDeclTypeBuilding pass.

		return n;		
	}
	
	public static Node disambiguateNode(AmbiguityRemover ar, Node n) throws SemanticException
	{
		TypeSystem ts = ar.typeSystem();
		NodeFactory nf = ar.nodeFactory();
		
		TypeBuilder tb = new TypeBuilder(ar.job(), ts, nf).pushContext(ar.context());
		
		n = n.visit(tb);
		n = n.visit(ar);
		
		return n;
	}
	
	public static SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeNode tn) throws SemanticException
	{
        SJTypeSystem sjts = (SJTypeSystem) cv.typeSystem();

        tn = tn.disambiguateSJTypeNode(cv, sjts);

        SJTypeNode child = tn.child();

        if (child != null) {
            tn = tn.child(disambiguateSJTypeNode(cv, child));
        }

        return tn;
	}

    public static SJSessionType dualSessionType(SJSessionType st) throws SemanticException
	{
		SJSessionType dual = null;

        while (st != null) {
            SJSessionType next = st.nodeDual();
            dual = dual == null ? next : dual.append(next);
            st = st.child();
        }

        return dual;
	}
	
	public static ClassDecl findClassDecl(SourceFile sf, String name)
	{
        for (Object o : sf.decls()) {
            ClassDecl cd = findClassDecl((ClassDecl) o, name); // ClassDecl is the only subtype of TopLevelDecl.

            if (cd != null) {
                return cd;
            }
        }
		
		return null;
	}
	
	private static ClassDecl findClassDecl(ClassDecl cd, String name)
	{
		if (cd.name().equals(name))
		{
			return cd;
		}

        for (Object o : cd.body().members()) {
            ClassMember cm = (ClassMember) o;

            if (cm instanceof ClassDecl) {
                ClassDecl res = findClassDecl((ClassDecl) cm, name);

                if (res != null) {
                    return res;
                }
            }
        }
		
		return null;
	}	
	
	// Expects a list of Expr.
	public static List<Type> getArgumentTypes(List arguments)
	{
		List<Type> argumentTypes = new LinkedList<Type>();

        for (Object argument : arguments) {
            argumentTypes.add(((Expr) argument).type());
        }
		
		return argumentTypes;
	}
	
	public static void debugPrint(String m)
	{
		if (SJConstants.SJ_DEBUG_PRINT)
		{
			System.out.print(m);
		}
	}
	
	public static void debugPrintln(String m)
	{
		debugPrint(m + System.getProperty("line.separator"));
	}

    public static <T> List<T> asLinkedList(T... elem) {
        List<T> l = new LinkedList<T>();
        Collections.addAll(l, elem);
        return l;
    }
}
