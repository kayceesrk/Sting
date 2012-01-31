package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.QQ;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.UniqueID;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.basicops.SJPass;
import sessionj.ast.sessops.basicops.SJRecurse;
import sessionj.ast.sessops.basicops.SJRecursionExit;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sessvars.SJSocketVariable;
import sessionj.extension.sessops.SJSessionOperationExt;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;
import static sessionj.util.SJCompilerUtils.getSJSessionOperationExt;
import sessionj.util.SJLabel;
import sessionj.util.SJTypeEncoder;
import sessionj.types.SJTypeSystem;

import java.util.*;

/**
 * 
 * @author Raymond
 * 
 * This visitor was intended to do something to handle (i.e. integrate with runtime type monitoring) recursive sessions implemented through recursive method calls, possibly by inserting SJRuntime.Recurse calls where the recursive method calls are made. But this is not working at all naively; needs to be designed carefully.
 *
 */
public class SJRecursiveSessionCallTranslator extends SJSessionVisitor  
{
	private final SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private final SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

	public SJRecursiveSessionCallTranslator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected NodeVisitor sjEnterCall(Node parent, Node n) throws SemanticException
	{
		if (n instanceof Call)
		{
			if (!(n instanceof SJSessionOperation)) // The session targets for basic operations have been moved into the arguments. 
			{
				n = translateRecursiveSessionCall((Call) n); // For session recursions implemented using method calls rather than the recurse operation (we insert the recurse operation as a hook for runtime type monitoring).				
			}
		}	
		
		return this;
	}
	
	protected Node sjLeaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof Call)
		{
			if (!(n instanceof SJSessionOperation)) // The session targets for basic operations have been moved into the arguments. 
			{
				n = translateRecursiveSessionCall((Call) n); // For session recursions implemented using method calls rather than the recurse operation (we insert the recurse operation as a hook for runtime type monitoring).				
			}
		}	
		
		return n;
	}
	
	private Node translateRecursiveSessionCall(Call c) throws SemanticException
	{		
		List args = c.arguments();

		List<Stmt> rs = new LinkedList<Stmt>();	// Should be a list of Calls to SJRuntime.recurse, but not actually SJRecurse. So we can get the runtime hook, but without affecting future SJAbstractSessionVisitor passes. Could make an "internal operation" version of SJRecurse. 			
		
		for (Object arg : args)
		{
			if (arg instanceof SJSocketVariable)
			{
				SJSocketVariable s = (SJSocketVariable) arg;
				
				//SJRecurse r = sjnf.SJRecurse(s.position(), lab, targets);
				
				String sjname = s.sjname();
				
				sjcontext.sessionRemaining(sjname);
				
				System.out.println("a: " + c + ", " + sjcontext.sessionRemaining(sjname));
			}
		}
		
		if (rs.isEmpty())
		{
			return c;
		}
		
		rs.add(sjnf.Eval(c.position(), c));
		
		return buildAndCheckTypes(this, sjnf.Block(c.position(), rs));
	}	
}
