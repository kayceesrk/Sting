package sessionj.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.basicops.SJBasicOperation;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Raymond
 *
 * Currently must be run before SJCompoundOperationTranslator (see ExtensionInfo).
 *
 */
public class SJUnicastOptimiser extends ContextVisitor
{
    public SJUnicastOptimiser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof SJSessionOperation) 
		{
			if (n instanceof SJBasicOperation)
			{
				n = translateBasicOperation((SJBasicOperation) n);
			}		
		}		

		return n;
	}
	
	private SJBasicOperation translateBasicOperation(SJBasicOperation n)
	{
		List sockets = n.targets();
		
		if (sockets.size() == 1)
		{
			List newargs = new LinkedList();

            newargs.addAll(n.realArgs());
			newargs.add(sockets.get(0));
			
			n = (SJBasicOperation) n.arguments(newargs);
		}
		
		return n;
	}
}
