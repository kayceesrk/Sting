package sessionj.visit;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sessops.basicops.SJPass;
import sessionj.ast.sessops.basicops.SJSend;
import sessionj.extension.SJExtFactory;
import sessionj.extension.noalias.SJNoAliasExprExt;
import sessionj.extension.sessops.SJSessionOperationExt;
import sessionj.extension.sesstypes.SJTypeableExt;
import static sessionj.util.SJCompilerUtils.*;
import sessionj.visit.noalias.SJNoAliasExprBuilder;

import java.util.LinkedList;

/**
 * 
 * @author Raymond
 *
 * Needs to come before noalias translation, so can tell if arguments are noalias.
 *
 */
public class SJSendTranslator extends ContextVisitor //ErrorHandlingVisitor
{
    private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory(); 

	public SJSendTranslator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof SJSend)
		{
			n = translateSJSend((SJSend) n);
		}

		return n;
	}

	private SJPass translateSJSend(SJSend s) throws SemanticException
	{	    
		Expr e = s.argument();

		SJPass p;
		
		if (isNoAlias(e) && !e.type().isPrimitive())
		{
			p = sjnf.SJPass(s.position(), asLinkedList(e), s.targets());

            // Need to grab the NewArray from the original SJSend,
            // as it has already been filled in with translated targets
            // in SJSessionOperationParser.
            // The NewArray is already in the arguments, so we grab them all.
            p = (SJPass) p.arguments(new LinkedList(s.arguments()));			
			
			SJNoAliasExprExt naee = getSJNoAliasExprExt(s);
			SJTypeableExt te = getSJTypeableExt(s);
			
			p = (SJPass) buildAndCheckTypes(this, p);
			p = (SJPass) SJNoAliasExprBuilder.setSJNoAliasExprExt(sjef, p, naee.isNoAlias(), naee.isFinal(), naee.fields(), naee.locals(), naee.arrayAccesses());
			p = (SJPass) setSJSessionOperationExt(sjef, p, te.sessionType(), ((SJSessionOperationExt) te).targetNames());
		}
		else
		{
			p = s;
		}
		
		return p;
	}

}
