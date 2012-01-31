/**
 * 
 */
package sessionj.visit.noalias;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.SJNodeFactory;
import sessionj.extension.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.typeobjects.*;
import sessionj.types.noalias.*;
import sessionj.util.noalias.*;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 * @deprecated
 *
 */
public class SJNoAliasTypeBuilder2 extends ContextVisitor
{	
	/*private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();*/
	
	/**
	 * 
	 */
	public SJNoAliasTypeBuilder2(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	/*protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassMember)
		{
			if (n instanceof ClassDecl)
			{
				n = buildClassDecl((ClassDecl) n);
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
			else if (!(n instanceof FieldDecl))
			{
				throw new SemanticException("[SJNoAliasTypeBuilder] ClassMember type not supported yet: " + n.getClass());
			}
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
	
	private ClassDecl buildClassDecl(ClassDecl cd) throws SemanticException
	{
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
	private ConstructorDecl buildConstructorDecl(ConstructorDecl cd) throws SemanticException
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
		}*

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

	private MethodDecl buildMethodDecl(MethodDecl md) throws SemanticException
	{		
		MethodInstance mi = md.methodInstance();		
		SJMethodInstance sjmi = sjts.SJMethodInstance(mi);
									
		Boolean[] res = new Boolean[1]; // HACK.
		
		md = (MethodDecl) md.body((Block) md.body().visit(new SJNoAliasProcedureChecker(this, res))); // FIXME: will cycle for mutually recursive method declarations.					
		
		if (res[0].equals(Boolean.TRUE))
		{
			sjmi.setNoAliasThroughThis(true); // Default is false.
		}
			
		TypeNode returnType = md.returnType();
		Type t = returnType.type();
		
		boolean isNoAlias = false;
		
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
	
	private boolean checkClassBodyAndSuper(ClassBody cb, SJParsedClassType pct)
	{
		boolean noAliasThroughThis = true;
		
		for (Iterator i = cb.members().iterator(); i.hasNext(); )
		{
			ClassMember cm = (ClassMember) i.next();
			
			if (cm instanceof FieldDecl)
			{
				if (!isNoAlias(cm)) // Includes primitive types. Also recursive dependency, e.g. field type is the parent class, OK?
				{
					if (noAliasThroughThis)
					{
						noAliasThroughThis = false;
					}
				}
			}
			else if (cm instanceof ProcedureDecl)
			{
				ProcedureInstance pi = ((ProcedureDecl) cm).procedureInstance();			
				
				if (pi instanceof SJMethodInstance)
				{
					noAliasThroughThis = noAliasThroughThis ? ((SJMethodInstance) pi).noAliasThroughThis() : false; // Constructors checked on use (although type built in this pass).
				}
				else if (!(pi instanceof SJConstructorInstance))
				{
					throw new RuntimeException("[SJNoAliasTypeBuilder] Shouldn't get here: " + cm); // If we're type building for this ClassDecl, then it must be a SJParsedClassType?
				}
			}
			else if (!(cm instanceof ClassDecl)) // ClassDecl members don't matter. 
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
				System.out.println("[SJNoAliasTypeBuilder] Warning! Superclass not checked: " + superType);
			}
		}		
		
		return noAliasThroughThis;
	}	
	
	private ProcedureDecl setNoAliasFormalTypes(ProcedureDecl pd)
	{
		List formals = pd.formals();
		List<Type> naft = new LinkedList<Type>();
			
		for (Iterator i = formals.iterator(); i.hasNext(); )
		{
			Formal f = (Formal) i.next();
			Type t = f.type().type();
			
			if (isNoAlias(f))
			//if (t instanceof SJNoAliasReferenceType)
			{
				if (t.isPrimitive()) // Can't be void.
				{
					naft.add(t);
				}
				else
				{
					naft.add(sjts.SJNoAliasFinalReferenceType((ReferenceType) t, f.flags().isFinal())); // The SJNoAliasReferenceTypes are just wrappers that point to the concrete Polyglot type objects.
				}
			}
			else
			{
				naft.add(t);
			}
		}
		
		((SJProcedureInstance) pd.procedureInstance()).setNoAliasFormalTypes(naft);
		
		return pd;
	}*/	
}
