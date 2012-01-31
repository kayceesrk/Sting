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
import static sessionj.SJConstants.SJ_CHANNEL_TYPE;
import static sessionj.SJConstants.SJ_KEYWORD_REQUEST;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.createops.SJChannelCreate;
import sessionj.ast.sessvars.SJChannelVariable;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Raymond
 *
 * Mostly the same as SJSessionOperationParser. 
 * 
 */
public class SJChannelOperationParser extends ContextVisitor
{
	public static final Set<String> RUNTIME_CHANNEL_OPERATIONS = new HashSet<String>(); // Factor out as constants.
	
	{
		RUNTIME_CHANNEL_OPERATIONS.add("toString");
		RUNTIME_CHANNEL_OPERATIONS.add("getServerIdentifier");
		//<By MQ> Added operations that were added to SJService
		RUNTIME_CHANNEL_OPERATIONS.add("addParticipant");
		RUNTIME_CHANNEL_OPERATIONS.add("getSocketForParticipant");
		RUNTIME_CHANNEL_OPERATIONS.add("participantName");
		RUNTIME_CHANNEL_OPERATIONS.add("setCostsMap");
		//</By MQ>
	}
	
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	//private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();

	public SJChannelOperationParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof Call)
		{
			/*if (n instanceof SJChannelOperation) // SJRequest.
			{
				n = parseSJChannelOperation((SJChannelOperation) n);
			}	
			else*/
			{
				n = parseCall((Call) n);
			}
		}		
	
		return n;
	}

	/*private Call parseSJChannelOperation(SJChannelOperation co) throws SemanticException
	{
		co = fixSJChannelOperationArguments(co);
		
		return co;
	}*/
	
	// Similar to that in SJSessionOperationParser.
	private Call parseCall(Call c) throws SemanticException
	{
		Receiver target = c.target();
		
		if (target.type().isSubtype(SJ_CHANNEL_TYPE))
		{	
			if (c instanceof SJChannelCreate)
			{
				
			}
			else if (c.name().equals(SJ_KEYWORD_REQUEST)) 
			{						
				if (target instanceof SJChannelVariable || target instanceof SJChannelCreate) // FIXME: could also support "inline" channel-receive as a request target. For this purpose, could make a e.g. SJChannelReturn operation.
				{
					SJRequest r = sjnf.SJRequest(c.position(), c.target(), c.arguments());			
					r = (SJRequest) buildAndCheckTypes(this, r);
					
					c = r;
				}
				else 
				{
					throw new SemanticException("[SJChannelOperationParser] Unsupported channel operation target: " + target);
				}
			}
			else if (!RUNTIME_CHANNEL_OPERATIONS.contains(c.name())) 
			{
				throw new SemanticException("[SJChannelOperationParser] Unknown channel operation: " + c);
			}
		}
		
		return c;
	}
}
