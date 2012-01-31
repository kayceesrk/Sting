package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.selectorops.*;
import sessionj.ast.sessvars.SJSelectorVariable;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;

import java.util.*;

/**
 * 
 * @author Raymond
 *
 * Based on SJServerOperationParser.
 *
 */
public class SJSelectorOperationParser extends ContextVisitor
{
	public static final Set<String> UNTYPED_SELECTOR_OPERATIONS = new HashSet<String>(); // Factor out as constants.
	
	{
		//UNTYPED_SELECTOR_OPERATIONS.add("...");
	}	
	
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

	public SJSelectorOperationParser(Job job, TypeSystem ts, NodeFactory nf)
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

		if (target instanceof SJSelectorVariable) 
		{		
			if (c.name().equals(SJ_KEYWORD_REGISTERACCEPT)) 
			{							
				SJRegisterAccept ra = sjnf.SJRegisterAccept(c.position(), c.target(), c.arguments());
				
				c = (SJRegisterAccept) buildAndCheckTypes(this, ra);
			}
			else if (c.name().equals(SJ_KEYWORD_REGISTEROUTPUT)) 
			{							
				SJRegisterOutput ro = sjnf.SJRegisterOutput(c.position(), c.target(), c.arguments());
				
				c = (SJRegisterOutput) buildAndCheckTypes(this, ro);
			}
			else if (c.name().equals(SJ_KEYWORD_REGISTERINPUT)) 
			{							
				SJRegisterInput ri = sjnf.SJRegisterInput(c.position(), c.target(), c.arguments());
				
				c = (SJRegisterInput) buildAndCheckTypes(this, ri);
			}
			else if (c.name().equals(SJ_KEYWORD_SELECTSESSION)) 
			{											
				SJSelectSession ss = sjnf.SJSelectSession(c.position(), c.target(), c.arguments());
				
				c = (SJSelectSession) buildAndCheckTypes(this, ss);
			}
			else if (!UNTYPED_SELECTOR_OPERATIONS.contains(c.name()))
			{
				throw new SemanticException("[SJSelectorOperationParser] Unknown selector operation: " + c);
			}
		}
		
		return c;
	}
}
