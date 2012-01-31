/**
 * 
 */
package sessionj.util.noalias;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.visit.*;

/**
 * @author Raymond
 *
 * Checks if `this' occurs in the given AST, excluding the target expressions of method calls.
 *
 */
public class SJNoAliasThisFinder extends ErrorHandlingVisitor
{
	private Boolean[] res; // HACK.
	
	/**
	 * @param job
	 * @param ts
	 * @param nf
	 */
	public SJNoAliasThisFinder(Job job, TypeSystem ts, NodeFactory nf, Boolean[] res)
	{
		super(job, ts, nf);
		
		this.res = res;
		this.res[0] = Boolean.FALSE;
	}

	protected ErrorHandlingVisitor enterCall(Node parent, Node n)  
	{
		if (n instanceof Field)
		{
			return (ErrorHandlingVisitor) this.bypass(((Field) n).target());
		}
		else if (n instanceof ProcedureCall)
		{
			if(n instanceof Call)		
			{		
				return (ErrorHandlingVisitor) this.bypass(((Call) n).target());
			}
			else if (n instanceof New)
			{		
				return (ErrorHandlingVisitor) this.bypass(((New) n).qualifier()); // Is this correct?
			}		
		}
		
		return this;
	}	
	
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof Special && ((Special) n).kind().equals(Special.THIS))
		{
			this.res[0] = Boolean.TRUE;
		}
		
		return n;
	}
}
	