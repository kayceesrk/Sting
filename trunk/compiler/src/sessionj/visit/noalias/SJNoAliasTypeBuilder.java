/**
 * 
 */
package sessionj.visit.noalias;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.extension.SJExtFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.typeobjects.*;
import static sessionj.util.SJCompilerUtils.*;
import sessionj.util.noalias.SJNoAliasProcedureChecker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Raymond
 *
 */
public class SJNoAliasTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
    private static final boolean DEBUG = false;

    /**
	 * 
	 */
	public SJNoAliasTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassMember)
		{
			if (n instanceof ClassDecl)
			{
				n = buildClassDecl((ClassDecl) n);
			}
			else if (n instanceof FieldDecl)
			{
				n = buildFieldDecl((FieldDecl) n);
			}
			else if (n instanceof ProcedureDecl)
			{			
				if (n instanceof ConstructorDecl)
				{
					n = buildConstructorDecl((ConstructorDecl) n);					
				}
				else //if (n instanceof MethodDecl)
				{
					n = buildMethodDecl((MethodDecl) n);					
				}	
			}					
			else //if (!(n instanceof FieldDecl))
			{
				throw new SemanticException("[SJNoAliasTypeBuilder] ClassMember type not supported yet: " + n.getClass());
			}
		}	
		else if (n instanceof VarDecl)
		{
			n = buildVarDecl((VarDecl) n);
		}			
		else if (n instanceof New) // For anonymous classes.
		{
			n = buildNew((New) n);
		}		
		
		return n;
	}
	
	private New buildNew(New n)
	{
		SJParsedClassType pct = (SJParsedClassType) n.anonType();
		
		if (pct != null)
		{													
			boolean noAliasThroughThis = checkClassBodyAndSuper(n.body(), pct);
			
			if (noAliasThroughThis)
			{
				pct.setNoAliasThroughThis(true);					
				
				n = n.anonType(pct);
			}
		}
		
		return n;
	}
	
	private ClassDecl buildClassDecl(ClassDecl cd) {
		ClassBody cb = cd.body();

		SJParsedClassType pct = (SJParsedClassType) cd.type();		
		
		boolean noAliasThroughThis = checkClassBodyAndSuper(cb, pct);
					
		if (noAliasThroughThis)
		{					
			pct.setNoAliasThroughThis(true);					
			
			cd = cd.type(pct);
		}
		
		return cd;
	}
	
	// FIXME: doesn't detect recursive constructor calls yet.
	private Node buildConstructorDecl(ConstructorDecl cd) throws SemanticException
	{
		// Even if constructor has already been visited via SJNoAliasProcedureChecker forward-check, the AST (for the current job) has not been updated with the new type information.
		ConstructorInstance ci = cd.constructorInstance();		
		SJParsedClassType sjpct = (SJParsedClassType) ci.container();
		
		/*for (Iterator i = sjpct.constructors().iterator(); i.hasNext(); )
		{
			ConstructorInstance foo = (ConstructorInstance) i.next();
			
			if (foo.formalTypes().equals(ci.formalTypes()))
			{
				if (foo instanceof SJConstructorInstance)
				{
					ci = foo; // Type information for this constructor has already been built and recorded in the SJParsedClassType, but not yet recorded with the actual ConstructorDecl (so will be done now).
					
					break;
				}
			}
		}*/

		ci = sjts.findConstructor(sjpct, ci.formalTypes(), context().currentClass());
		
		SJConstructorInstance sjci;
		
		if (ci instanceof SJConstructorInstance)
		{					
			sjci = (SJConstructorInstance) ci;		
		}
		else
		{
			sjci = sjts.SJConstructorInstance(ci);		
				
			Boolean[] res = new Boolean[1]; // HACK.
			
			cd = (ConstructorDecl) cd.body((Block) cd.body().visit(new SJNoAliasProcedureChecker(this, res)));			
				
			if (res[0].equals(Boolean.TRUE))
			{
				sjci.setNoAliasThroughThis(true); // Default is false.
			}
														
			updateSJConstructorInstance(ci, sjci);
		}
		
		cd = cd.constructorInstance(sjci);
		cd = (ConstructorDecl) setNoAliasFormalTypes(cd);			
		
		return cd;
	}

	private Node buildFieldDecl(FieldDecl fd) {
		TypeNode tn = fd.type();
		
		boolean isNoAlias = false;
		boolean isFinal = false; // final noalias primitive variables are treated just as noalias, not na-final (since they are passed by value).
						
		if (tn.type().isPrimitive()) // Can't be void.
		{
			isNoAlias = true;
		}
		else
		{
			isNoAlias = isNoAlias(tn);
			isFinal = fd.flags().isFinal();
		}
		
		//boolean isNoAlias = fd.type().type() instanceof SJNoAliasReferenceType;
		//boolean isFinal = fd.flags().isFinal(); 
		
		FieldInstance fi = fd.fieldInstance();
		SJFieldInstance sjfi = sjts.SJFieldInstance(fd.fieldInstance(), isNoAlias, isFinal);						
		
		sjfi.setConstantValue(fi.constantValue()); // Currently, constant checker not run on custom nodes/type objects.
		
		fd = fd.fieldInstance(sjfi);
		fd = (FieldDecl) setSJNoAliasFinalExt(sjef, fd, isNoAlias, isFinal); // Needs to be done for all Expr.
		
		updateSJFieldInstance(fi, sjfi); // Needs to be done for all ClassMember except ClassDecl (latter links to shared, mutable ParsedClassType).								
		
		return fd;
	}	
	
	private Node buildMethodDecl(MethodDecl md) {
		MethodInstance mi = md.methodInstance();		
		SJMethodInstance sjmi = sjts.SJMethodInstance(mi);
									
		Boolean[] res = new Boolean[1]; // HACK.
		
		Block body = md.body();
		
		if (body == null) // Interfaces and abstract methods? 
		{
			sjmi.setNoAliasThroughThis(true); // Assume true for the interface. The "actual check" will take place when the subclass type is assigned to the (noalias) interface variable - assignment will be disallowed if the subclass method implementation is bad. 
		}
		else
		{
			Block b = (Block) md.body().visit(new SJNoAliasProcedureChecker(this, res));
			
			md = (MethodDecl) md.body(b); // FIXME: will cycle for mutually recursive method declarations.					
			
			if (res[0].equals(Boolean.TRUE))
			{
				sjmi.setNoAliasThroughThis(true); // Default is false.
			}
		}		
			
		TypeNode returnType = md.returnType();
		Type t = returnType.type();
		
		boolean isNoAlias;
		
		if (t.isPrimitive() && !t.isVoid())
		{
			isNoAlias = true;
			
			sjmi = sjmi.noAliasReturnType(t);			
		}
		else
		{
			isNoAlias = isNoAlias(returnType);
			
			if (isNoAlias)
			//if (returnType.type() instanceof SJNoAliasReferenceType)
			{		
				sjmi = sjmi.noAliasReturnType(sjts.SJNoAliasReferenceType((ReferenceType) returnType.type())); // FIXME: na-final return types currently not permitted: clashing usage of final keyword.  						
			}
			else
			{
				sjmi = sjmi.noAliasReturnType(returnType.type());
			}			
		}		
		
		md = md.methodInstance(sjmi);
		md = (MethodDecl) setSJNoAliasExt(sjef, md, isNoAlias);
		
		updateSJMethodInstance(mi, sjmi);		
					
		md = (MethodDecl) setNoAliasFormalTypes(md);		
		
		return md;
	}
	
	private Node buildVarDecl(VarDecl vd) {
		TypeNode tn = vd.type();
		
		boolean isNoAlias;
		boolean isFinal = false; // final noalias primitive variables are treated just as noalias, not na-final (since they are passed by value).  
		
		if (tn.type().isPrimitive()) // Can't be void.
		{
			isNoAlias = true;
		}
		else
		{
			isNoAlias = isNoAlias(tn);
			isFinal = vd.flags().isFinal();
		}		
			
		//boolean isNoAliasType = vd.type().type() instanceof SJNoAliasReferenceType;
		//boolean isFinal = vd.flags().isFinal();
		
		SJLocalInstance sjli = sjts.SJLocalInstance(vd.localInstance(), isNoAlias, isFinal);		
		
		if (vd instanceof LocalDecl) 
		{
			vd = ((LocalDecl) vd).localInstance(sjli);				
		}
		else //if (vd instanceof Formal)
		{
			vd = ((Formal) vd).localInstance(sjli);
		}		
		
		vd = (VarDecl) setSJNoAliasFinalExt(sjef, vd, isNoAlias, isFinal);		
		
		return vd;
	}
	
	private boolean checkClassBodyAndSuper(ClassBody cb, SJParsedClassType pct)
	{
		boolean noAliasThroughThis = true;

        for (Object o : cb.members()) {
            ClassMember cm = (ClassMember) o;

            if (cm instanceof FieldDecl) {
                if (!isNoAlias(cm)) // Includes primitive types. Also recursive dependency, e.g. field type is the parent class, OK?
                {
                    if (noAliasThroughThis) {
                        noAliasThroughThis = false;
                    }
                }
            } else if (cm instanceof ProcedureDecl) {
                ProcedureInstance pi = ((ProcedureDecl) cm).procedureInstance();

                if (pi instanceof SJMethodInstance) {
                    noAliasThroughThis = noAliasThroughThis && ((SJMethodInstance) pi).noAliasThroughThis(); // Constructors checked on use (although type built in this pass).
                } else if (!(pi instanceof SJConstructorInstance)) {
                    throw new RuntimeException("[SJNoAliasTypeBuilder] Shouldn't get here: " + cm); // If we're type building for this ClassDecl, then it must be a SJParsedClassType?
                }
            } else if (!(cm instanceof ClassDecl)) // ClassDecl members don't matter.
            {
                throw new RuntimeException("[SJNoAliasTypeBuilder] ClassMember type not supported yet: " + cm);
            }
        }
		
		// Check immediate superclass.
		if (noAliasThroughThis)
		{
			ParsedClassType superType = (ParsedClassType) pct.superType();
			
			if (superType instanceof SJParsedClassType)
			{			
				noAliasThroughThis = pct.noAliasThroughThis();
			}
			else
			{
				debug("[SJNoAliasTypeBuilder] Warning! Superclass not checked: " + superType);
			}
		}		
		
		return noAliasThroughThis;
	}

    private static void debug(String s) {
        if (DEBUG) System.out.println(s);
    }

    private ProcedureDecl setNoAliasFormalTypes(ProcedureDecl pd)
	{
		List formals = pd.formals();
		List<Type> naft = new LinkedList<Type>();

        for (Object formal : formals) {
            Formal f = (Formal) formal;
            Type t = f.type().type();

            if (isNoAlias(f))
            //if (t instanceof SJNoAliasReferenceType)
            {
                if (t.isPrimitive()) // Can't be void.
                {
                    naft.add(t);
                } else {
                    naft.add(sjts.SJNoAliasFinalReferenceType((ReferenceType) t, f.flags().isFinal())); // The SJNoAliasReferenceTypes are just wrappers that point to the concrete Polyglot type objects.
                }
            } else {
                naft.add(t);
            }
        }
		
		((SJProcedureInstance) pd.procedureInstance()).setNoAliasFormalTypes(naft);
		
		return pd;
	}

    public static void updateSJConstructorInstance(ConstructorInstance ci, SJConstructorInstance sjci)
    {
        SJParsedClassType pct = (SJParsedClassType) ci.container();
        List<ConstructorInstance> constructors = (List<ConstructorInstance>) copyProcedureInstanceList(pct.constructors());

        constructors.remove(ci);
        constructors.add(sjci);

        pct.setConstructors(constructors);
    }

    public static void updateSJMethodInstance(MethodInstance mi, SJMethodInstance sjmi)
    {
        SJParsedClassType pct = (SJParsedClassType) mi.container();
        List<MethodInstance> methods = (List<MethodInstance>) copyProcedureInstanceList(pct.methods());

        methods.remove(mi);
        methods.add(sjmi);

        pct.setMethods(methods);
    }

    private static List<? extends ProcedureInstance> copyProcedureInstanceList(List procedures)
    {
        List<ProcedureInstance> copy = new LinkedList<ProcedureInstance>();

        for (Object procedure : procedures) {
            copy.add((ProcedureInstance) procedure);
        }

        return copy;
    }
}
