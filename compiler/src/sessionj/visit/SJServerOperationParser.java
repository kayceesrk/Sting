package sessionj.visit;

import polyglot.ast.Call;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Receiver;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.SJ_KEYWORD_ACCEPT;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.servops.SJAccept;
import sessionj.ast.sessvars.SJServerVariable;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Raymond
 *
 * Based on SJChannelOperationParser. 
 * 
 */
public class SJServerOperationParser extends ContextVisitor
{
	public static final Set<String> RUNTIME_SERVER_OPERATIONS = new HashSet<String>(); // Factor out as constants.
	
	{
		RUNTIME_SERVER_OPERATIONS.add("getProtocol");
		RUNTIME_SERVER_OPERATIONS.add("getLocalPort");
		RUNTIME_SERVER_OPERATIONS.add("getCloser");
		RUNTIME_SERVER_OPERATIONS.add("addParticipant");  //<By MQ>
		RUNTIME_SERVER_OPERATIONS.add("participantName");  //<By MQ>
		RUNTIME_SERVER_OPERATIONS.add("setCostsMap");  //<By MQ>
	}	
	
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	//private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();

	public SJServerOperationParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof Call)
		{
			n = parseCall((Call) n);
		}		
	
		return n;
	}
	
	private Call parseCall(Call c) throws SemanticException
	{
		Receiver target = c.target();

		if (target instanceof SJServerVariable) // SJServerCreate not permitted as target.
		{		
			if (c.name().equals(SJ_KEYWORD_ACCEPT)) 
			{							
				SJAccept a = sjnf.SJAccept(c.position(), c.target(), c.arguments()); 			
				a = (SJAccept) buildAndCheckTypes(this, a);
				
				c = a;
			}
			else if (RUNTIME_SERVER_OPERATIONS.contains(c.name()))
			{
				// Ignore from point of view of session type checking. // FIXME: shouldn't ignore, can statically disallow accept after a potential close? But seems quite tough (indirection of close operation via SJServerSocketCloser). 
			}
			else 
			{
				throw new SemanticException("[SJServerOperationParser] Unknown server operation: " + c);
			}
		}
		
		return c;
	}
}
