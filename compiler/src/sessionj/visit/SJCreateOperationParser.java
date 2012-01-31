package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

import sessionj.ast.*;
import sessionj.ast.createops.*;
import sessionj.extension.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

public class SJCreateOperationParser extends ContextVisitor
{
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	//private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();

	public SJCreateOperationParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof Call)
		{
			n = parseSJCreateOperation((Call) n);			
		}

		return n;
	}

	private Call parseSJCreateOperation(Call c) throws SemanticException
	{
		Receiver r = c.target();

		if (r instanceof CanonicalTypeNode)
		{					
			CanonicalTypeNode ctn = (CanonicalTypeNode) r;
			
			SJCreateOperation co = null;
			
			if (ctn.type().typeEquals(SJ_CHANNEL_TYPE) && c.name().equals(SJ_KEYWORD_CHANNELCREATE))
			{
				co = sjnf.SJChannelCreate(c.position(), c.arguments());
			}
			/*else if (ctn.type().typeEquals(SJ_SOCKET_INTERFACE_TYPE) && c.name().equals(SJ_KEYWORD_SOCKETCREATE))
			{
				co = sjnf.SJSocketCreate(c.position(), c.arguments());
			}*/
			else if (ctn.type().isSubtype(SJ_SERVER_INTERFACE_TYPE) && c.name().equals(SJ_KEYWORD_SERVERCREATE))
			{
				co = sjnf.SJServerCreate(c.position(), c.arguments());
			}
			else if (ctn.type().isSubtype(SJ_RUNTIME_TYPE) && c.name().equals(SJ_KEYWORD_SELECTORCREATE)) // Doesn't discriminate between instance/static methods (no need to do so).
			{
				co = sjnf.SJSelectorCreate(c.position(), c.arguments());
			}
			
			if (co != null)
			{
				c = (Call) buildAndCheckTypes(this, co); 
			}
		}		

		return c;
	}
}
