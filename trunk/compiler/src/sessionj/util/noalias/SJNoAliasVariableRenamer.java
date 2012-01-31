/**
 * 
 */
package sessionj.util.noalias;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.SJ_TMP_LOCAL;
import sessionj.ast.SJNodeFactory;
import sessionj.types.SJTypeSystem;
import static sessionj.util.SJCompilerUtils.isNoAlias;

import java.util.Set;

/**
 * @author Raymond
 * 
 * Also translates Field/ArrayAccessAssign to LocalAssign if the LHS has been renamed for translation to a Local.
 * 
 */
public class SJNoAliasVariableRenamer extends ErrorHandlingVisitor
{
	SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	
	private Set<Variable> vars;

    /**
	 */
	public SJNoAliasVariableRenamer(Job job, ErrorHandlingVisitor cv, Set<Variable> vars)
	{
		super(job, cv.typeSystem(), cv.nodeFactory());

        this.vars = vars;
	}

	protected ErrorHandlingVisitor enterCall(Node parent, Node n)  
	{
		if (n instanceof Field) 
		{
			return (ErrorHandlingVisitor) this.bypass(((Field) n).target()); // toString seems to get the (full) name of the field, so no need to visit the field target.			
		}
		
		return this;
	}
	
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof Assign && !(n instanceof LocalAssign)) // FieldAssign or ArrayAccessAssign.
		{
			Assign a = (Assign) n; 
			Expr left = a.left();
			
			if (left instanceof Local)
			{						
				LocalAssign la = sjnf.LocalAssign(a.position(), (Local) a.left(), Assign.ASSIGN, a.right());
				la = (LocalAssign) la.type(a.type());
				
				n = la;
			}
		}		
		else if (n instanceof Variable && isNoAlias(n))
		{						
			Variable var = (Variable) n;			

			boolean translate = false;
			
			for (Variable foo : vars)
			{
				if (foo.toString().equals(var.toString()))
				{
					translate = true;
					
					break;
				}				
			}
			
			if (translate)
			{
				if (var instanceof Field)
				{											
					String vname = renameNoAliasVariable(var);
					
					Local local = sjnf.Local(var.position(), sjnf.Id(var.position(), vname));
					//n = buildAndCheckTypes(job(), n, cv); // The existing context has not yet recorded the variables that we have just declared.								
					
					local = (Local) local.type(var.type());
					local = local.localInstance(sjts.SJLocalInstance(sjts.localInstance(var.position(), Flags.NONE, local.type(), vname), false, false)); 
					
					n = local;
				}
				else if (var instanceof Local)
				{
					String vname = renameNoAliasVariable(var);			
					
					n = ((Local) var).name(vname);
				}
				else //if (var instanceof ArrayAccess)
				{
					ArrayAccess aa = (ArrayAccess) var;
					String vname = renameNoAliasVariable(aa);			
					
					Local local = sjnf.Local(aa.position(), sjnf.Id(aa.position(), vname));								
					
					local = (Local) local.type(aa.array().type());
					local = local.localInstance(sjts.SJLocalInstance(sjts.localInstance(aa.position(), Flags.NONE, local.type(), vname), false, false));
					
					n = local;
				}
			}											
		}
		
		return n;
	}
	
	public static String renameNoAliasVariable(Variable v) {
		String vname = v.toString(); 
		
		if (v instanceof Field)
		{
			vname = SJ_TMP_LOCAL + "_" + vname;
		}
		else if (v instanceof Local)
		{
			vname = SJ_TMP_LOCAL + "_" + vname;
		}
		else //if (v instanceof ArrayAccess)
		{			
			vname = SJ_TMP_LOCAL + "_" + vname;
		}
		
		return vname.replace(".", "_").replace("[", "_").replace("]", "");
	}
}
	