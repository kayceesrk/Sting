/**
 * 
 */
package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.*;
import sessionj.ast.servops.*;
import sessionj.ast.chanops.*;
import sessionj.ast.sessops.*;
import sessionj.types.sesstypes.*;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 * Post session type checking visitor which reads the session type information built and recorded by the preceding passes.
 *
 */
public class SJSessionVisitor extends SJAbstractSessionVisitor  
{	
	//private static boolean debug = true;
	private static boolean debug = false;
		
	/**
	 * 
	 */
	public SJSessionVisitor(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected NodeVisitor sjEnterCall(Node parent, Node n) throws SemanticException // FIXME: make abstract.
	{
		return this;
	}
	
	protected Node sjLeaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof SJSessionOperation && !(n instanceof SJInternalOperation))
		{
			SJSessionOperation so = (SJSessionOperation) n;			
			List<String> sjnames = getSJSessionOperationExt(so).targetNames();
			SJSessionType st = getSessionType(so);
			
			if (debug)
			{
				System.out.println("\n" + sjnames + ": " + st + " (" + so + ")\n");
			}
		}
		else if (n instanceof SJSpawn)
		{
			SJSpawn s = (SJSpawn) n;
			
			if (debug)
			{
				System.out.println("\n" + s.sjnames() + ": " + s.sessionTypes() + " (" + s + ")\n");
			}
		}
		else if (n instanceof SJChannelOperation)
		{
			SJChannelOperation co = (SJChannelOperation) n;			
			String sjname = getSJNamedExt(co).sjname();
			SJSessionType st = getSessionType(co);
			
			if (debug)
			{
				System.out.println("\n" + sjname + ": " + st + " (" + co + ")\n");
			}
		}
		else if (n instanceof SJServerOperation)
		{
			SJServerOperation so = (SJServerOperation) n;			
			SJSessionType st = getSessionType(so);
			
			if (debug)
			{
				System.out.println("\n" + so + ": " + st + "\n");
			}
		}
			
		
		return n;
	}	
}
