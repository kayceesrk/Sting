/**
 * 
 */
package sessionj.util.noalias;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.types.typeobjects.SJConstructorInstance;
import sessionj.types.typeobjects.SJFieldInstance;
import sessionj.types.typeobjects.SJParsedClassType;
import static sessionj.util.SJCompilerUtils.findClassDecl;
import static sessionj.util.SJCompilerUtils.getArgumentTypes;
import sessionj.visit.noalias.SJNoAliasTypeBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * @author Raymond
 *
 */
public class SJNoAliasProcedureChecker extends ErrorHandlingVisitor
{
    private static final boolean DEBUG = false;
	private SJNoAliasTypeBuilder natb;
	private Boolean[] res; // HACK.
	
	public SJNoAliasProcedureChecker(SJNoAliasTypeBuilder natb, Boolean[] res)
	{
		super(natb.job(), natb.typeSystem(), natb.nodeFactory());
		
		this.natb = natb;
		
		this.res = res;
		this.res[0] = Boolean.TRUE;
	}

	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof VarInit)
		{
			if (n instanceof FieldDecl)
			{
				n = checkFieldDecl((FieldDecl) n);
			}
			else //if (n instanceof LocalDecl)
			{
				n = checkLocalDecl((LocalDecl) n);			
			}
		}
		else if (n instanceof Assign)
		{
			n = checkAssign((Assign) n);			
		}
		else if (n instanceof ProcedureCall)
		{
			n = checkProcedureCall((ProcedureCall) n);
		}
		else if (n instanceof Return)
		{
			n = checkReturn((Return) n);
		}
		
		return n;
	}
	
	private FieldDecl checkFieldDecl(FieldDecl fd) throws SemanticException
	{		
		Expr init = fd.init();
		
		if (init != null)
		{		
			checkExprForBadThis(init);
		}
		
		return fd;
	}

	private LocalDecl checkLocalDecl(LocalDecl ld) throws SemanticException
	{
		Expr init = ld.init();
		
		if (init != null)
		{
			checkExprForBadThis(init);
		}
		
		return ld;
	}
	
	private Assign checkAssign(Assign a) throws SemanticException
	{
		Expr left = a.left();
		Expr right = a.right();
		
		checkExprForBadThis(right);
		
		/*if (!res[0].equals(Boolean.FALSE)) // Basically, aim was to stop bad use of fields. But difficult - would be very conservative? Only method parameters can be assigned to fields. And fields themselves can only be used as call targets. 
		{		
			boolean isNoAlias = true;
			
			// FIXME: for the following two checks, need to support fields nested in conditionals.
			
			if (left instanceof ArrayAccess)
			{
				ArrayAccess aa = (ArrayAccess) left;
				
				if (aa.array() instanceof Field)
				{
					left = (Field) aa.array();
				}
			}
			
			if (left instanceof Field) // OK for (temporary) alias to (non-noalias) locals.
			{
				Field f = (Field) left;
				
				Receiver target = f.target();								
				
				if (target instanceof Special && ((Special) target).kind().equals(Special.THIS))
				{
					if (right instanceof Field)
					{
						if (!isNoAliasField((Field) right)) // FIXME: conditionals, casts, etc.
						{
							isNoAlias = false;
						}
					}
				}
				
				if (right instanceof Field)
				{
					isNoAlias = false;
				}
				else if (!(right instanceof Local || ...))
				{
					...
				}
			}
			
			if (!isNoAlias)
			{
				res[0] = Boolean.FALSE;
			}
		}*/
		
		return a;
	}
	
	private ProcedureCall checkProcedureCall(ProcedureCall pc) throws SemanticException
	{		
		if (pc instanceof Call || pc instanceof New)
		{
			checkExprForBadThis((Expr) pc);
		}
		else //if (pc instanceof ConstructorCall) // Can only occur within a ConstructorDecl.
		{
			for (Iterator i = pc.arguments().iterator(); i.hasNext(); )
			{
				checkExprForBadThis((Expr) i.next());
			}
			
			if (!res[0].equals(Boolean.FALSE))
			{
				pc = checkConstructorCall((ConstructorCall) pc);
			}
		}
		
		return pc;
	}
		
	private ConstructorCall checkConstructorCall(ConstructorCall cc) throws SemanticException
	{
		List<Type> formalTypes = getArgumentTypes(cc.arguments());
		ParsedClassType pct = (ParsedClassType) cc.constructorInstance().container();
		
		if (cc.kind().equals(ConstructorCall.THIS))
		{			
			if (pct instanceof SJParsedClassType)
			{
				/*List constructors = pct.constructors();
				
				for (Iterator i = constructors.iterator(); i.hasNext(); )
				{
					ConstructorInstance ci = (ConstructorInstance) i.next();
					
					if (ci.formalTypes().equals(formalTypes))
					{
						SJConstructorInstance sjci = null; 
						
						if (ci instanceof SJConstructorInstance) 
						{
							sjci = (SJConstructorInstance) ci;
						}
						else // Calling a constructor that the type builder has not got to yet.
						{														
							sjci = (SJConstructorInstance) lookAheadForConstructorDecl((SJParsedClassType) pct, formalTypes).constructorInstance();					
						}
							
						if (!sjci.noAliasThroughThis()) // Enough to just check immediate super constructor here: the SJNoAliasTypeBuilder is used to recursively check the called constructor.
						{
							res[0] = Boolean.FALSE;
						}
						
						break;
					}
				}*/
				
				ConstructorInstance ci = typeSystem().findConstructor(pct, formalTypes, natb.context().currentClass());
				SJConstructorInstance sjci = null; 
				
				if (ci instanceof SJConstructorInstance) 
				{
					sjci = (SJConstructorInstance) ci;
				}
				else // Calling a constructor that the type builder has not got to yet.
				{														
					sjci = (SJConstructorInstance) lookAheadForConstructorDecl((SJParsedClassType) pct, formalTypes).constructorInstance();					
				}
					
				if (!sjci.noAliasThroughThis()) // Enough to just check immediate super constructor here: the SJNoAliasTypeBuilder is used to recursively check the called constructor.
				{
					res[0] = Boolean.FALSE;
				}				
			}
			/*else
			{
				// Shouldn't get in here? If we're checking a ConstructorDecl, then other constructors in the same class must also be available.
			}*/				
		}
		else //if (cc.kind().equals(ConstructorCall.SUPER))
		{											
			if (pct instanceof SJParsedClassType)
			{
				/*List constructors = pct.constructors();
				
				for (Iterator i = constructors.iterator(); i.hasNext(); )
				{
					SJConstructorInstance sjci = (SJConstructorInstance) i.next();
					
					if (sjci.formalTypes().equals(formalTypes))
					{					
						if (!sjci.noAliasThroughThis()) // Enough to just check immediate super constructor.
						{
							res[0] = Boolean.FALSE;
						}
					}
				}*/		
				
				ConstructorInstance ci = typeSystem().findConstructor(pct, formalTypes, natb.context().currentClass());
				
				if (!(ci instanceof SJConstructorInstance)) // FIXME: shouldn't get in here?
				{
					debug("[SJNoAliasProcedureChecker] Warning! Super constructor call (to " + pct + ") not checked [2]: " + cc);
				}
				else
				{
					SJConstructorInstance sjci = (SJConstructorInstance) ci;
					
					if (sjci.formalTypes().equals(formalTypes))
					{					
						if (!sjci.noAliasThroughThis()) // Enough to just check immediate super constructor.
						{
							res[0] = Boolean.FALSE;
						}
					}
					else
					{
						// Can't get in here?
					}
				}
			}
			else
			{
				debug("[SJNoAliasProcedureChecker] Warning! Super constructor call (to " + pct + ") not checked [1]: " + cc);
			}									
		}	
		
		return cc;
	}

    private static void debug(String s) {
        if (DEBUG) System.out.println(s);
    }

    private Return checkReturn(Return r) throws SemanticException
	{
		/*Expr e = r.expr(); // The aim was to detect leaking of fields through method return. But very difficult - can be indirection via locals or other method returns. (Maybe possible if done in conjunction with assignment-from-fields check.)
		
		/*if (!(e == null || e instanceof Local || e instanceof Call || isNoAlias(e))) // FIXME.
		{
			res[0] = Boolean.FALSE;
		}*
		
		if (e instanceof Field) // Not just fields of this instance, but also any other subobjects. So here being very conservative, not checking for this or subobjects, but simply just any fields. // FIXME: need to support conditionals, casts, etc.
		{				
			if (!isNoAliasField((Field) e))
			{
				res[0] = Boolean.FALSE;
			}
		}*/
		
		return r;
	}
	
	private ConstructorDecl lookAheadForConstructorDecl(SJParsedClassType pct, List<Type> formalTypes) throws SemanticException
	{		
		/*for (Iterator i = ((SourceFile) job().ast()).decls().iterator(); i.hasNext(); ) // If we are visiting this ConstructorDecl, must be from a SourceFile.
		{
			ClassDecl cd = (ClassDecl) i.next(); // ClassDecl is the only subtype of TopLevelDecl.
			
			if (cd.name().equals(pct.name())) // Need to explicitly qualify names?
			{
				... // Moved below.
			}
		}*/
		
		ClassDecl cd = findClassDecl((SourceFile) job().ast(), pct.name()); // Need to qualify type name?

		if (cd == null) // Shouldn't get here?
		{
			throw new SemanticException("[SJNoAliasProcedureChecker] Could not find class: " + pct);
		}
		
		for (Iterator j = cd.body().members().iterator(); j.hasNext(); )
		{
			ClassMember cm = (ClassMember) j.next();
			
			if (cm instanceof ConstructorDecl)
			{
				ConstructorDecl foo = (ConstructorDecl) cm; 
				
				if (foo.constructorInstance().formalTypes().equals(formalTypes))
				{
					foo = (ConstructorDecl) foo.visit(natb); // SJProtocolDeclTypeBuilder/SJCompilerUtils.dismabiguateSJTypeNode instead makes a fresh visitor, don't know which way is better.												
					
					//replaceASTNode(job(), cm, foo);	// Modifications to the job AST are not visible within the current pass: this ConstructorDecl will be revisited by SJNoAliasTypeBuilder.	
					
					return foo;
				}
			}
		}
		
		throw new SemanticException("[SJNoAliasProcedureChecker] Shouldn't get here: " + pct);
	}
	
	private Expr checkExprForBadThis(Expr e)
	{
		Boolean[] foo = new Boolean[1]; 
		
		e.visit(new SJNoAliasThisFinder(job(), typeSystem(), nodeFactory(), foo)); // No need to follow method calls transitively: all methods conservatively checked for each class (and class suitability checked at object creation).
		
		if (foo[0].equals(Boolean.TRUE))
		{
			res[0] = Boolean.FALSE;
		}
		
		return e;
	}
	
	// FIXME: following duplicated from SJNoAliasExprBuilder. Need to sort out recursive dependencies in the analysis, like for use of fields within methods of a class, and recursive method calls.
	private boolean isNoAliasField(Field f) throws SemanticException
	{
		Type t = f.target().type();
		
		boolean isNoAlias = false;	
		
		if (t instanceof ParsedClassType)
		{
			ParsedClassType pct = (ParsedClassType) t;
	
			if (pct instanceof SJParsedClassType)
			{				
				SJFieldInstance sjfi = (SJFieldInstance) typeSystem().findField(pct, f.name());							
				
				isNoAlias = sjfi.isNoAlias(); 
				
				f = f.fieldInstance(sjfi);							
			}
			else
			{																	
				System.out.println("[SJNoAliasExprBuilder] Warning! noalias type building skipped (defaulted to non-noalias) for field: " + f);
			}					
		}
		else if (t instanceof ArrayType) // Should be length property access, which is an int type, so leave as non-noalias like other primitives.
		{						
			isNoAlias = true; 
		}
		else if (t instanceof PrimitiveType) // Can't be void. // No need to look up the field instance (regardless of whether it is a SJFieldInstance set by SJNoAliasTypeBuilder). 
		{
			isNoAlias = true;
		}
		
		return isNoAlias;
	}
}	
