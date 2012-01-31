/**
 * 
 */
package sessionj.visit;

import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.SJSpawn;
import sessionj.ast.SessionTypedNode;
import sessionj.ast.sesscasts.SJSessionTypeCast;
import sessionj.ast.sessops.SJInternalOperation;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sessvars.SJChannelVariable;
import sessionj.ast.sessvars.SJLocalChannel;
import sessionj.ast.sessvars.SJSocketVariable;
import sessionj.ast.sessvars.SJVariable;
import sessionj.ast.typenodes.SJProtocolNode;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.extension.SJExtFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJCBeginType;
import sessionj.types.sesstypes.SJInbranchType;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.SJNamedInstance;
import static sessionj.util.SJCompilerUtils.getSJSessionOperationExt;
import static sessionj.util.SJCompilerUtils.setSJSessionOperationExt;

import java.util.LinkedList;
import java.util.List;

import polyglot.ast.*; //<By MQ>
import polyglot.util.Position; //<By MQ>
/**
 * 
 * @author Raymond
 *
 * Also does casts for receive.
 *
 * Should make a "SJSessionReturn" type for session socket create, session-receive (and casts) and session return type method calls. Similarly for channels and servers.
 *
 */
public class SJSessionOperationTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	public SJSessionOperationTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		/*if (n instanceof SJSessionTry)
		{
			// Could give something like SJSessionOperationExt (i.e. list of targetNames and maybe SJUnknownTypes to session-try).
		}
		else */
        if (n instanceof SessionTypedNode) {
            n = ((SessionTypedNode) n).buildType(this, sjts, sjef);
        }
		else if (n instanceof SJSessionOperation)
		{
			if (n instanceof SJBasicOperation)
			{
				if (n instanceof SJPass) // Includes SJSend.
				{
					if (n instanceof SJCopy)
					{
						n = buildSJCopy((SJCopy) n);						
					}
					else
					{
						n = buildSJPass((SJPass) n);
					}					
				}
				else if (n instanceof SJReceive)
				{				    
					n = buildSJReceive((SJReceive) n);
				}
				else if (n instanceof SJRecurse)
				{
					n = buildSJRecurse((SJRecurse) n);
				}
				else if(!(n instanceof SJInternalOperation))
				{
					throw new SemanticException("[SJSessionOperationTypeBuilder] Session operation not yet supported: " + n);
				}	
			}
			else if (n instanceof SJCompoundOperation)
			{
				if (n instanceof SJBranchOperation)
				{
					n = buildSJBranchOperation((SJBranchOperation) n);
				}
				else if (n instanceof SJLoopOperation)
				{
					if (n instanceof SJWhile) // SJLoopOperation interface not working for javac...
					{
						n = buildSJWhile((SJWhile) n);				
					}
					else //if (n instanceof SJRecursion)
					{
						n = buildSJRecursion((SJRecursion) n);
					}
				}
                else
				{
					throw new SemanticException("[SJSessionOperationTypeBuilder] Session operation not yet supported: " + n);
				}
			}
			else
			{
				throw new SemanticException("[SJSessionOperationTypeBuilder] Session operation not yet supported: " + n);
			}
		}	
		else if (n instanceof SJSpawn)
		{
			n = buildSJSpawn((SJSpawn) n);
		}
		else if (n instanceof Cast)
		{
			n = buildCast((Cast) n);
		}
		
		return n;
	}
	
	private Node buildSJCopy(SJCopy c) throws SemanticException
	{	
		Expr arg = (Expr) c.arguments().get(0); // Factor out constant.
		if (arg instanceof SJChannelVariable /*|| arg instanceof SJSocketVariable*/) // Only SJCopy can accept na-final args.
		{
			SJNamedInstance ni = (SJNamedInstance) context().findLocal(((SJVariable) arg).sjname());			
			SJSessionType st = sjts.SJSendType(c.arguments().get(c.arguments().size()-2).toString().replace("\"", "")/*null*/, ni.sessionType()); //<By MQ>// For session sockets, should always be SJUnknownType. 
			c = (SJCopy) buildSJPassAux(c, st);
		}
		else
		{
			c = (SJCopy) buildSJPass(c);
		}
		
		return c;
	}
	
	private Node buildSJPass(SJPass p) throws SemanticException
	{		
		Expr arg = p.argument();
		
		Type messageType;
		
		if (arg instanceof SJSocketVariable) 
		{
			SJNamedInstance ni = (SJNamedInstance) context().findLocal(((SJVariable) arg).sjname());
			
			messageType = ni.sessionType(); // For session sockets, should always be SJUnknownType. 
		}
		else
		{
			messageType = arg.type();
		}

		SJSessionType st = sjts.SJSendType(p.arguments().get(p.arguments().size()-2).toString().replace("\"", ""), messageType); //<By MQ> Added the target name
		
		p = buildSJPassAux(p, st);
		
		return p;
	}
	
	private SJPass buildSJPassAux(SJPass p, SJSessionType st)
	{
		List<String> sjnames = getTargetNames(p.targets(), false);
		
		p = (SJPass) setSJSessionOperationExt(sjef, p, st, sjnames);		
		
		return p;
	}
	
	private Node buildSJReceive(SJReceive r) throws SemanticException
	{	
		String name = r.name();
		
		SJSessionType st;

		if (name.equals(SJ_SOCKET_RECEIVE)) // FIXME: when SJNodeFactory is fixed to use the "runtime" constants rather than the "socket" constants, this routine should be fixed as well. // Includes the higher-order operations. 
		{
		    st = sjts.SJReceiveType(r.arguments().get(r.arguments().size()-2).toString().replace("\"", ""), sjts.SJUnknownType()); //<By MQ>
		}
		else if (name.equals(SJ_SOCKET_RECEIVEINT))
		{
		    st = sjts.SJReceiveType(r.arguments().get(r.arguments().size()-2).toString().replace("\"", ""), sjts.Int()); //<By MQ>
		}
		else if (name.equals(SJ_SOCKET_RECEIVEBOOLEAN))
		{
		    st = sjts.SJReceiveType(r.arguments().get(r.arguments().size()-2).toString().replace("\"", ""), sjts.Boolean()); //<By MQ>
		}
		else if (name.equals(SJ_SOCKET_RECEIVEDOUBLE))
		{
		    st = sjts.SJReceiveType(r.arguments().get(r.arguments().size()-2).toString().replace("\"", ""), sjts.Double()); //<By MQ>
		}
		else
		{
			throw new SemanticException("[SJSessionOperationTypeBuilder] Shouldn't get in here: " + name);
		}
		
		List<String> sjnames = getTargetNames(r.targets(), false); 
		
		r = (SJReceive) setSJSessionOperationExt(sjef, r, st, sjnames);
		return r;
	}
	
	private Node buildSJRecurse(SJRecurse r)
	{
		SJSessionType st = sjts.SJRecurseType(r.label());  		
		List<String> sjnames = getTargetNames(r.targets(), false);
		
		r = (SJRecurse) setSJSessionOperationExt(sjef, r, st, sjnames);
		
		return r;
	}
	
	private Node buildCast(Cast c) throws SemanticException
	{
		Expr e = c.expr();
		if (e instanceof SJReceive)
		{
			SJReceive r = (SJReceive) e;
			List<String> sjnames = getSJSessionOperationExt(r).targetNames();
			Type t;
			if (c instanceof SJSessionTypeCast)
			{
				SJTypeNode tn = ((SJSessionTypeCast) c).sessionType();
				SJSessionType st = tn.type();
				
				if (tn instanceof SJProtocolNode && st instanceof SJCBeginType) // Duplicated in SJSessionMethodTypeBuilder.
				{
					throw new SemanticException("[SJSessionOperationTypeBuilder] Protocol reference for channel type casts not yet supported: " + c);
				}
				
				t = st;
			}
			else
			{
				t = c.type();
			}

			SJSessionType st = sjts.SJReceiveType(r.arguments().get(r.arguments().size()-2).toString().replace("\"", ""), t);//<By MQ> MQTODO
			
			c = c.expr((SJReceive) setSJSessionOperationExt(sjef, r, st, sjnames));
		}
		
		return c;
	}
	
	private Node buildSJBranchOperation(SJBranchOperation bo)
	{
		List sjnames = getTargetNames(bo.targets(), false);
		SJSessionType st = sjts.SJUnknownType();
		
		if (bo instanceof SJOutbranch)
		{
			sjnames = getTargetNames(bo.targets(), false);
				 
			st = sjts.SJOutbranchType().branchCase(((SJOutbranch) bo).label(), st);
		}
		else //if (bo instanceof SJInbranch)
		{
		        //<By MQ>
		        List args = ((SJBasicOperation_c)((SJInbranch)bo).inlabel()).realArgs();
			SJInbranchType ibt = sjts.SJInbranchType(args.get(0).toString().replace("\"", ""));
			//</By MQ>
			for (SJInbranchCase ibc : ((SJInbranch) bo).branchCases())
			{
				ibt.branchCase(ibc.label(), sjts.SJUnknownType()); // Could just alias st?
			}
			
			st = ibt;
		}				
		
		bo = (SJBranchOperation) setSJSessionOperationExt(sjef, bo, st, sjnames);		
		
		return bo;
	}
	
	private Node buildSJWhile(SJWhile w)
	{
		List sjnames = getTargetNames(w.targets(), false);
		SJSessionType st = sjts.SJUnknownType();
		
		if (w instanceof SJOutwhile || w instanceof SJOutInwhile) // FIXME: hacky, inwhile element not explicitly typed.
		{
			sjnames = getTargetNames(w.targets(), false);
				 
			st = sjts.SJOutwhileType(st);
		}
		else //if (w instanceof SJInwhile)
		{		    
		    st = sjts.SJInwhileType(st, ((SJInwhile_c)w).arguments().get(0).toString().replace("\"", "")); //<By MQ> Added target
		}				
		
		w = (SJWhile) setSJSessionOperationExt(sjef, w, st, sjnames);

		return w;
	}
	
	private Node buildSJRecursion(SJRecursion r)
	{
		List sjnames = getTargetNames(r.targets(), false);
		SJSessionType st = sjts.SJRecursionType(r.label()).body(sjts.SJUnknownType());
		
		r = (SJRecursion) setSJSessionOperationExt(sjef, r, st, sjnames);
		
		return r;
	}
	
	private Node buildSJSpawn(SJSpawn s) throws SemanticException
	{
		List<String> sjnames = getTargetNames(s.targets(), true);
		List<SJSessionType> sts = new LinkedList<SJSessionType>();
		
		for (String sjname : sjnames)
		{
		    sts.add(sjts.SJSendType(s.arguments().get(s.arguments().size()-2).toString().replace("\"", ""), sjts.SJUnknownType()));//<By MQ>
		}
		
		s = (SJSpawn) setSJSessionOperationExt(s, sjnames, sts);
		
		return s;
	}
	
	public static List<String> getTargetNames(List targets, boolean channelsAllowed)
	{
		List<String> sjnames = new LinkedList<String>();

        for (Object target : targets) {
            SJVariable v = (SJVariable) target;

            if (v instanceof SJLocalChannel) {
                if (channelsAllowed) sjnames.add(v.sjname());
            } else {
                sjnames.add(v.sjname());
            }
        }
		
		return sjnames;
	}
}
