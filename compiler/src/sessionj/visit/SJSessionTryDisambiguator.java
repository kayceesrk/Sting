package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

import sessionj.ast.*;
import sessionj.ast.protocoldecls.*;
import sessionj.ast.sessops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sesstry.*;
import sessionj.ast.sessvars.*;
import sessionj.ast.typenodes.*;
import sessionj.extension.*;
import sessionj.types.*;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * 
 * @author Raymond
 *
 * Disambiguates all SJTry (i.e. also SJServerTry).
 * 
 */
public class SJSessionTryDisambiguator extends ContextVisitor
{
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();	
	private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();

	public SJSessionTryDisambiguator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof SJTry) // Should be SJAmbiguousTry.
		{
			n = disambiguateSJTry((SJTry) n);
		}
		
		return n;
	}
	
	private SJTry disambiguateSJTry(SJTry st) throws SemanticException
	{			
		List targets = st.targets();
		
		SJVariable v = (SJVariable) targets.get(0);
		
		if (v instanceof SJSocketVariable)
		{
			for (Iterator i = targets.iterator(); i.hasNext(); )				
			{
				Receiver r = (Receiver) i.next();
				
				if (!(r instanceof SJSocketVariable))
				{
					throw new SemanticException("[SJSessionTryDisambiguator] Expected session socket variable, not: " + r);
				}
			}
			
			st = sjnf.SJSessionTry(st.position(), st.tryBlock(), st.catchBlocks(), st.finallyBlock(), targets);
		}
		else if (v instanceof SJServerVariable)
		{
            /*
			if (targets.size() != 1) // Do this check elsewhere? Relax this restriction?
			{
				throw new SemanticException("[SJSessionTryDisambiguator] server-try may only specify a single target: " + targets);
			}
			*/
				 
			st = sjnf.SJServerTry(st.position(), st.tryBlock(), st.catchBlocks(), st.finallyBlock(), targets);
		}
		else if (v instanceof SJSelectorVariable)
		{
			if (targets.size() != 1) // Do this check elsewhere? Relax this restriction?
			{
				throw new SemanticException("[SJSessionTryDisambiguator] selector-try may only specify a single target: " + targets);
			}
				 
			st = sjnf.SJSelectorTry(st.position(), st.tryBlock(), st.catchBlocks(), st.finallyBlock(), targets);			
		}
		else
		{
			throw new RuntimeException("[SJSessionTryDisambiguator] Shouldn't get in here.");
		}
				
		st = (SJTry) buildAndCheckTypes(this, st);
		
		return st;
	}
	
}
