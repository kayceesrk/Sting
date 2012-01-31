/**
 * 
 */
package sessionj.visit.noalias;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.SJSpawn;
import sessionj.ast.createops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.protocoldecls.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.typeobjects.*;
import sessionj.types.noalias.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 * Currently has the explicit hooks to control noalias typing for session entities, e.g. channels, selectors, etc.
 *
 */
public class SJNoAliasTypeChecker extends ContextVisitor
{
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	
	/**
	 * @param job
	 * @param ts
	 * @param nf
	 */
	public SJNoAliasTypeChecker(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}
	
	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{	
		if (n instanceof MethodDecl)
		{
			n = checkMethodDecl((MethodDecl) n);
		}
		else if (n instanceof VarInit) // Includes field/local arrays. 
		{
			if (n instanceof SJProtocolDecl)
			{
				n = checkSJProtocolDecl((SJProtocolDecl) n);
			}
			//<By MQ>
			else if (n instanceof SJGProtocolDecl)
			{
				n = checkSJGProtocolDecl((SJGProtocolDecl) n);
			}
			//</By MQ>
			else if (n instanceof FieldDecl)
			{
				n = checkFieldDecl((FieldDecl) n);
			}
			else //if (n instanceof LocalDecl)
			{
				n = checkLocalDecl((LocalDecl) n);
			}
		}
		else if (n instanceof Expr)
		{
			if (n instanceof Assign)
			{
				n = checkAssign((Assign) n);
			}
			else if (n instanceof ProcedureCall)
			{
				if (n instanceof Call)
				{
					if (n instanceof SJCreateOperation)
					{
						// Don't need to do any noalias checking, the protocol/channel arguments are checked to be na-final and we know the target methods are well-behaved.
					}
					else if (n instanceof SJBasicOperation)
					{
						if (n instanceof SJPass)
						{
							if (n instanceof SJSend) // Accepts regular and noalias.
							{								
								 n = checkSJSend((SJSend) n);
							}
							else if (n instanceof SJCopy) // Accepts na-final and noalias.
							{
								n = checkSJCopy((SJCopy) n);
							}
							else // Accepts only noalias.
							{
								n = checkSJPass((SJPass) n);
							}
						} // Other basic operations cannot have noalias arguments. // Implicit noalias return type for receive operations set by SJNoAliasExprBuilder and checked "on use" at e.g. assign and argument passing. 
					}
					else
					{
						n = checkCall((Call) n);
					}
				}
				else // if (n instanceof New)
				{
					n = checkNew((New) n);
				}
			}			
		}
		else if (n instanceof ConstructorCall)
		{
			n = checkConstructorCall((ConstructorCall) n);
		}
		else if (n instanceof Return)
		{
			n = checkReturn((Return) n);
		}
		
		return n;
	}

	private MethodDecl checkMethodDecl(MethodDecl md) throws SemanticException
	{
		SJMethodInstance sjmi = (SJMethodInstance) md.methodInstance();
		
		// Adapted from MethodDecl typeCheck. 
        for (Object o : sjmi.implemented()) {
            MethodInstance mi = (MethodInstance) o;

            if (sjts.isAccessible(mi, context())) {
                sjts.checkOverride(sjmi, mi); // Would it be enough to just check the closest (most recently overridden) parent method?
            }
        }
				
		return md;
	}
	
	private Node checkSJProtocolDecl(SJProtocolDecl pd) throws SemanticException
	{
		Flags flags;
		
		if (pd instanceof SJFieldProtocolDecl)
		{
			flags = ((SJFieldProtocolDecl) pd).flags();
		}
		else //if (pd instanceof SJLocalProtocolDecl)
		{
			flags = ((SJLocalProtocolDecl) pd).flags();
		}
		
		if (!(flags.isFinal() && isNoAlias(pd)))
		{
			throw new SemanticException("[SJNoAliasTypeChecker] Protocol declarations should be final noalias: " + pd);
		}
		
		return pd;
	}

    //<By MQ> Added
	private Node checkSJGProtocolDecl(SJGProtocolDecl pd) throws SemanticException
	{
		Flags flags;
		
		if (pd instanceof SJFieldGProtocolDecl)
		{
			flags = ((SJFieldGProtocolDecl) pd).flags();
		}
		else //if (pd instanceof SJLocalGProtocolDecl)
		{
			flags = ((SJLocalGProtocolDecl) pd).flags();
		}
		
		if (!(flags.isFinal() && isNoAlias(pd)))
		{
			throw new SemanticException("[SJNoAliasTypeChecker] GProtocol declarations should be final noalias: " + pd);
		}
		
		return pd;
	}

    //</By MQ>	
	private Node checkFieldDecl(FieldDecl fd) throws SemanticException
	{
		Expr init = fd.init();
				
		if (init != null) // Protocol declarations already checked and channel/socket/server/selector fields not permitted (by SJChannelDeclTypeBuilder/etc.).
		{
			if (isNoAlias(fd))				
			{
				if (!(init instanceof NullLit || init instanceof Lit || fd.declType().isPrimitive())) // Checking if init is isNoAlias is almost there, except for method arguments.
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot initialise a noalias field by: " + init);
				}
				
				/*if (!isNoAlias(init))
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign non-noalias type to noalias type: " + fd);
				}
				
				if (isFinal(init))
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign final noalias type: " + fd);
				}*/				 
			}
		}	
		
		return fd;
	}
	
	private Node checkLocalDecl(LocalDecl ld) throws SemanticException
	{
		Type t = ld.declType();
		Expr init = ld.init();
				
		if (t.isSubtype(SJ_CHANNEL_TYPE)) // Would belong better in SJChannel/ServerDeclTypeBuilder?
		{
			if (!(ld.flags().isFinal() && isNoAlias(ld)))
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Shared channels must be final noalias: " + ld);
			}
		}
				
		if (t.isSubtype(SJ_SELECTOR_INTERFACE_TYPE))
		{
			if (!(ld.flags().isFinal() && isNoAlias(ld)))
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Selector variables must be final noalias: " + ld);
			}
		}
		
		if (t.isSubtype(SJ_SOCKET_INTERFACE_TYPE) || t.isSubtype(SJ_SERVER_INTERFACE_TYPE)) // FIXME: the session entity checks should be moved into a session-related pass.
		{
			if (!isNoAlias(ld))
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Session sockets and server sockets must be noalias: " + ld);
			}
		}		
		else if (init != null)
		{			
			if (isNoAlias(ld)) // Includes na-final declarations: only requirement is that init is noalias.
			{
				/*if (init instanceof Assign || init instanceof Conditional)
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot initialise a noalias local by: " + init);
				}*/
				
				if (!isNoAlias(init))
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign non-noalias type to noalias type: " + ld);
				}							
			}
			
			if (isNoAlias(init) && isFinal(init))
			{
				if (!t.isPrimitive())
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign final noalias type: " + init);
				}
			}		
		}	
		
		return ld;
	}
	
	private Assign checkAssign(Assign a) throws SemanticException
	{
		Expr left = a.left();
		Expr right = a.right();			
		
		if (isNoAlias(left))
		{
			/*if (right instanceof Assign || right instanceof Conditional)
			{
				throw new SemanticException("Cannot assign to a noalias type: " + right);
			}*/			
			
			if (!(isNoAlias(right)))
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign non-noalias type to noalias type: " + a);
			}
		}

		if (isNoAlias(right) && isFinal(right))
		{
			if (!a.type().isPrimitive())
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Cannot assign final noalias type: " + a);
			}
		}
		
		return a;
	}
	
	private SJSend checkSJSend(SJSend s) throws SemanticException
	{				
		s = (SJSend) checkProcedureCallArgumentsRegularTarget(s, false); // Treat send as non-noalias target to allow non-noalias message types. 
		
		return s;
	}
	
	private SJCopy checkSJCopy(SJCopy c) throws SemanticException
	{	
		c = (SJCopy) checkProcedureCallArgumentsNoAliasTarget(c, true); // Hacky? To support SJ libraries implemented in regular Java.
		
		return c;
	}
	
	private SJPass checkSJPass(SJPass p) throws SemanticException
	{				
		p = (SJPass) checkProcedureCallArgumentsNoAliasTarget(p, false); 
		
		return p;
	}
	
	private Call checkCall(Call c) throws SemanticException
	{				
		if (isNoAlias(c.target()))
		{
			c = (Call) checkProcedureCallArgumentsNoAliasTarget(c, false);
		}
		else
		{
			c = (Call) checkProcedureCallArgumentsRegularTarget(c, false);
		}
		
		return c;
	}		
	
	private New checkNew(New n) throws SemanticException
	{
		n = (New) checkProcedureCallArgumentsRegularTarget(n, false);
		
		return n;		
	}
	
	private ConstructorCall checkConstructorCall(ConstructorCall cc) throws SemanticException
	{
		cc = (ConstructorCall) checkProcedureCallArgumentsRegularTarget(cc, true);
		
		return cc;		
	}
	
	private Return checkReturn(Return r) throws SemanticException
	{
		Expr e = r.expr();
		
		if (e != null)
		{
			SJMethodInstance sjmi = (SJMethodInstance) context().currentCode(); // Return only possible from a method (from a class that we are compiling, so will be SJMethodInstance).			
			
			if (sjmi.noAliasReturnType() instanceof SJNoAliasReferenceType && !isNoAlias(e))
			{
				throw new SemanticException("[SJNoAliasTypeChecker] noalias return type required: " + r);
			}			
			
			if (isFinal(e) && isNoAlias(e)) // e.g. returning a na-final parameter can create an alias to the original argument in the calling context.
			{
				throw new SemanticException("[SJNoAliasTypeChecker] Cannot return final noalias types: " + e);
			}
		}
		
		return r; 
	}
	
	// SJCopy should be treated as a noalias target call implicitly requiring final noalias parameters. But current translation removes the noalias target. 
	private ProcedureCall checkProcedureCallArgumentsNoAliasTarget(ProcedureCall pc, boolean ignoreFinal) throws SemanticException
	{
		/*for (Iterator i = pc.arguments().iterator(); i.hasNext(); ) //Not needed given all fields must now be noalias. 
		{
			Expr arg = (Expr) i.next();
			
			if (!isNoAlias(arg)) // For noalias targets, all arguments must also be noalias.
			{
				throw new SemanticException("[SJNoAliasTypeChecker] noalias type arguments required: " + pc);
			}
		}*/
		
		ProcedureInstance pi = pc.procedureInstance();
		
		if (pi instanceof SJProcedureInstance)
		{
			List<Type> naft = ((SJProcedureInstance) pi).noAliasFormalTypes();
			
			if (naft != null)
			{
				//if (!ignoreFinal) // If the SJ libraries were implemented in SJ and compiled using sessionjc, wouldn't need to "ignore final" for SJCopy. 
				{
					Iterator i = pc.arguments().iterator();
					
					for (Type t : naft)
					{
						Expr arg = (Expr) i.next();				
						
						if (isFinal(arg))
						{
							if (!(t instanceof SJNoAliasFinalReferenceType && ((SJNoAliasFinalReferenceType) t).isFinal()))
							{
								throw new SemanticException("[SJNoAliasTypeChecker] Cannot pass final noalias type to non- final noalias type [1]: " + arg);	
							}
						}
					}
				}
				
				pc = checkNoAliasArguments(pc);
			}
		}
		else // Simply treat all parameters as non-noalias.  
		{
			pc = defaultProcedureCallCheck(pc, ignoreFinal); // Hacky? For SJCopy argument checking for SJ libraries implemented in regular Java.
		}
		
		return pc;
	}
	
	private ProcedureCall checkProcedureCallArgumentsRegularTarget(ProcedureCall pc, boolean noFields) throws SemanticException
	{
		ProcedureInstance pi = pc.procedureInstance();
		
		if (pi instanceof SJProcedureInstance)
		{			
			List<Type> naft = ((SJProcedureInstance) pi).noAliasFormalTypes();
			
			if (naft != null) // null means no arguments? Or Polyglot bug (should be empty list)?
			{			
				Iterator i = pc.arguments().iterator();
				
				for (Type t : naft)
				{
					Expr arg = (Expr) i.next();										
					
					if (noFields)
					{
						SJNoAliasExprExt naee = getSJNoAliasExprExt(arg);
						
						if (!naee.fields().isEmpty())
						{
							throw new SemanticException("[SJNoAliasTypeChecker] Bad field access in argument expression: " + arg);
						}
						
						for (ArrayAccess aa : naee.arrayAccesses())
						{
							if (aa.array() instanceof Field)
							{
								throw new SemanticException
                                    ("[SJNoAliasTypeChecker] Bad field access in argument expression: " + arg);
							}
						}
					}
					
					if (t instanceof SJNoAliasReferenceType)
					{
						if (!isNoAlias(arg))
						{
							throw new SemanticException
                                ("[SJNoAliasTypeChecker] Cannot pass non-noalias type as a noalias parameter: " + pc);
						}
						
						if (isFinal(arg))
						{
							if (!((SJNoAliasFinalReferenceType) t).isFinal())
							{
								if (pi instanceof SJMethodInstance) // The run call from the generated SJThread spawn is an exception to the rule.
								{
									if(!(((SJMemberInstance) pi).container().isSubtype(SJ_THREAD_TYPE) && ((Call) pc).name().equals(SJ_THREAD_RUN)))
									{
										throw new SemanticException
                                            ("[SJNoAliasTypeChecker] Cannot pass final noalias type to non- final noalias type [2]: " + arg);
									}
								}
								else
								{
									throw new SemanticException
                                        ("[SJNoAliasTypeChecker] Cannot pass final noalias type to non- final noalias type [3]: " + arg);
								}
							}
						}						
					}		
					else if (isNoAlias(arg) && isFinal(arg))
					{
						throw new SemanticException
                            ("[SJNoAliasTypeChecker] Cannot pass final noalias type to non- final noalias type [3]: " + arg);
					}					
				}
			}
			
			pc = checkNoAliasArguments(pc);
		}
		else // Simply treat all parameters as non-noalias.  
		{
			pc = defaultProcedureCallCheck(pc, false);
		}		 
		
		return pc;
	}
	
	private ProcedureCall defaultProcedureCallCheck(ProcedureCall pc, boolean ignoreFinal) throws SemanticException
	{
		List ft = pc.procedureInstance().formalTypes();
		
		if (ft != null)
		{
			if (!ignoreFinal)
			{
                for (Object o : pc.arguments()) {
                    Expr arg = (Expr) o;
                  
                    if (isNoAlias(arg) && isFinal(arg) && !arg.type().isPrimitive()) {
                        throw new SemanticException
                            ("[SJNoAliasTypeChecker] Cannot pass final noalias type to non- final noalias type [4]: " + arg);
                    }
                }
			}
			
			pc = checkNoAliasArguments(pc);
		}
		
		return pc;
	}
	
	private ProcedureCall checkNoAliasArguments(ProcedureCall pc) throws SemanticException
	{
		if (pc instanceof SJSend)
        // HACK: translation introduces repeated argument for e.g. s.send(s.receive()) =>
        // SJRuntime.send(..s.., s.receive());
		{
			return pc;
		}
		
		List<Variable> vars = new LinkedList<Variable>();
		//SJNoAliasVariablesExt nave = getSJNoAliasVariablesExt(pc); // Includes ConstructorCalls.

        for (Object o : pc.arguments()) {
            Expr e = (Expr) o;

            SJNoAliasVariablesExt nave = getSJNoAliasVariablesExt(e);

            vars.addAll(nave.fields());
            vars.addAll(nave.locals());
            vars.addAll(nave.arrayAccesses());
        }

		Set <String> seen = new HashSet<String>(); // Should instead use VarInstance type objects?		
		
		for (Variable v: vars) // This includes self-delegation via arguments (target and message) to SJAbstractSocket.copy.
		{			
			String vname = v.toString();
			
			if (seen.contains(vname))
			{
				if (!v.type().isPrimitive())
                // Hacky, but should work because only want to skip things like call targets
                // (already handled by SJNoAliasExprBuilder), and Binary operands
                // (e.g. x < y) - the operands can only be primitive types or String, unless
                // the operator is == or instanceof (also handled SJNoAliasExprBuilder).
				{
					throw new SemanticException("[SJNoAliasTypeChecker] Repeated noalias argument: " + v);
				}
			}
			
			seen.add(vname);
		}
		
		return pc;
	}
}
