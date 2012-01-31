/**
 * 
 */
package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.SJSpawn;
import sessionj.ast.chanops.SJChannelOperation;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.createops.SJChannelCreate;
import sessionj.ast.createops.SJServerCreate;
import sessionj.ast.selectorops.SJSelect;
import sessionj.ast.servops.SJAccept;
import sessionj.ast.servops.SJServerOperation;
import sessionj.ast.sesscasts.SJChannelCast;
import sessionj.ast.sesscasts.SJSessionCast;
import sessionj.ast.sessops.SJInternalOperation;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.TraverseTypeBuildingContext;
import sessionj.ast.sessops.basicops.SJPass;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.SJSelectorTry;
import sessionj.ast.sesstry.SJServerTry;
import sessionj.ast.sesstry.SJSessionTry;
import sessionj.ast.sesstry.SJTry;
import sessionj.ast.sessvars.*;
import sessionj.extension.SJExtFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.contexts.SJContext;
import sessionj.types.contexts.SJContextElement;
import sessionj.types.contexts.SJContext_c;
import sessionj.types.sesstypes.SJDelegatedType;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.*;
import static sessionj.util.SJCompilerUtils.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author Raymond
 *
 * Post session type checking visitor which reads the session type information built and recorded by the preceding passes.
 *
 * N.B. visitors descending from this one must use sjEnter/LeaveCall instead of the regular enter/leaveCalls.
 *
 */
abstract public class SJAbstractSessionVisitor extends ContextVisitor  
{	
	protected SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	protected SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	protected SJExtFactory sjef = sjnf.extFactory();
	
	protected SJContext sjcontext = new SJContext_c(this, sjts);
	
	/**
	 * 
	 */
	public SJAbstractSessionVisitor(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	abstract protected NodeVisitor sjEnterCall(Node parent, Node n) throws SemanticException;
	abstract protected Node sjLeaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException;
	
	// This will be called on the visitor cloned by the parent ContextVisitor enter routine. 
	protected final NodeVisitor enterCall(Node parent, Node n) throws SemanticException
	{
		enterSJContext(parent, n);
		
		return sjEnterCall(parent, n);
		
		//return this; // Otherwise need to hand over the session context object and update the cv field to the new visitor.
	}
	
	protected final Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		n = sjLeaveCall(parent, old, n, v); // Want the Visitor to do stuff whilst in the current context, i.e. before it is popped. 
		
		// We don't need the popped context for anything (just needed to pop it).
        // But we need to pop and process compound contexts before performing type building etc.
        // for those operations.
        leaveSJContext(n);
        		
		if (n instanceof SJSessionOperation) // Must come before Expr, SJBasicOperations are Exprs.
		{
			if (!(n instanceof SJInternalOperation))
			{
				n = recordSJSessionOperation((SJSessionOperation) n);
			}
		}
		else if (n instanceof SJSpawn)
		{
			n = recordSJSpawn((SJSpawn) n);
		}
		else if (n instanceof Expr)
		{
			if (n instanceof Assign)
			{
				n = recordAssign((Assign) n);
			}
			else if (n instanceof SJChannelOperation) 
			{
				n = recordSJChannelOperation((SJChannelOperation) n);
			}
			else if (n instanceof SJServerOperation)
			{
				n = recordSJServerOperation((SJServerOperation) n); 
			}
			else if (n instanceof Cast)
			{
				if (n instanceof SJChannelCast)
				{
					//n = recordSJChannelCast(parent, (SJChannelCast) n); //FIXME.
				}
				/*else if (n instanceof SJSessionCast) // Already taken care of by recordAssign.
				{
					n = recordSJSessionCast(parent, (SJSessionCast) n);
				}*/
			}
		}				
		else if (n instanceof ProcedureCall)
		{
			n = recordProcedureCall((ProcedureCall) n); 
		}
		
		return n;
	}
	
	private Node recordSJSessionOperation(SJSessionOperation so) throws SemanticException
	{
		List<String> sjnames = getSJSessionOperationExt(so).targetNames();
		SJSessionType st = getSessionType(so);
		
		for (String sjname : sjnames)
		{
			if (so instanceof SJPass) //FIXME: also do channel passing.
			{
                doDelegationForSjSocketArgments(so);
			}
			
			sjcontext.advanceSession(sjname, st);
		}
		
		return so;
	}

    private void doDelegationForSjSocketArgments(SJSessionOperation so) throws SemanticException {
        //Expr arg = (Expr) ((SJPass) so).arguments().get(1); // Factor out constants.
    		Expr arg = (Expr) ((SJPass) so).arguments().get(0); // Ray: I believe that the message argument is now in position 0? 

        if (arg instanceof SJLocalSocket) // A bit lucky that this still works after SJHigherOrderTranslator? Need to be careful about translation is allowed? Or about which passes are allowed to be SJSessionVisitors.
        {
            String s = ((SJVariable) arg).sjname();

            if (s.startsWith(SJ_TMP_LOCAL))
            {
                s = s.substring(SJ_TMP_LOCAL.length() + "_".length());
            }

            sjcontext.delegateSession(s); // Maybe should instead just record the type from the extension object rather than recalculating the delegated (remaining) type here.
        }
    }

    private SJSpawn recordSJSpawn(SJSpawn s) throws SemanticException
	{
		Iterator<SJSessionType> i = s.sessionTypes().iterator();
		
		for (String sjname : s.sjnames())
		{
			SJSessionType st = i.next();
			
			if (st instanceof SJDelegatedType) // For sessions.
			{
				sjcontext.delegateSession(sjname); 
			}
			else
			{
				// Channels.
			}
		}
		
		return s;
	}
	
	private Assign recordAssign(Assign a) throws SemanticException
	{
		if (a.type().isSubtype(SJ_CHANNEL_TYPE))
		{
			Expr right = a.right();
			
			if (right instanceof SJChannelCreate)
			{
				Expr left = a.left();						
				SJChannelVariable cv = (SJChannelVariable) left; 			
				String sjname = cv.sjname();
				SJSessionType st = getSJTypeableExt(right).sessionType();										
				SJLocalChannelInstance lci = (SJLocalChannelInstance) sjcontext.getChannel(sjname); 	
				
				sjcontext.addChannel(sjts.SJLocalChannelInstance(lci, st, sjname)); 
			}
		}
		else if (a.type().isSubtype(SJ_SERVER_INTERFACE_TYPE))
		{
			Expr right = a.right();
			
			if (right instanceof SJServerCreate)
			{
				Expr left = a.left();						
				SJServerVariable sv = (SJServerVariable) left; 				
				String sjname = sv.sjname();
				SJSessionType st = getSJTypeableExt(right).sessionType();											
				SJLocalServerInstance lsi = (SJLocalServerInstance) sjcontext.getServer(sjname); 
				
				sjcontext.addServer(sjts.SJLocalServerInstance(lsi, st, sjname)); // Replaces the SJUnknownType added by the LocalDecl (for the server socket). 
				sjcontext.openService(sjname, st); // Replaces the SJUnknownType added at server-try enter.
			}
		}
		else if (a.type().isSubtype(SJ_SOCKET_INTERFACE_TYPE))
		{
			Expr right = a.right();
			
			if (right instanceof SJRequest 
					|| right instanceof SJAccept 
					|| right instanceof SJSessionCast
					|| right instanceof SJSelect) // FIXME: use SessionSocketCreator.   				
			{							
				Expr left = a.left();				
				SJSocketVariable sv = (SJSocketVariable) left;				
				String sjname = sv.sjname();				
				SJLocalSocketInstance lsi = (SJLocalSocketInstance) sjcontext.getSocket(sjname); 				
				SJSessionType st = getSJTypeableExt(right).sessionType();
				
				sjcontext.addSocket(sjts.SJLocalSocketInstance(lsi, st, sjname)); // Not really necessary.										   				
				sjcontext.openSession(sjname, st);
				
				if (right instanceof SJRequest)
				{
					sjcontext.advanceSession(sjname, sjts.SJCBeginType());
				}
				else if (right instanceof SJAccept)
				{
				    String target = ((SJAccept)right).arguments().get(0).toString().replace("\"", ""); //<By MQ> MQTODO: UGLY WAY of extracting target, needs revising
				    sjcontext.advanceSession(sjname, sjts.SJSBeginType(target)); //<By MQ>
				}
			}
		}
		
		return a;
	}

	private SJChannelOperation recordSJChannelOperation(SJChannelOperation co)
	{
		// Nothing needed.
		
		return co;
	}
	
	private SJServerOperation recordSJServerOperation(SJServerOperation so)
	{
		// Nothing needed.
		
		return so;
	}
	
	/*private SJSessionCast recordSJSessionCast(Node parent, SJSessionCast sc)
	{
		if (parent instanceof LocalAssign)
		{
			
		}
		else
		{
			throw new SJRuntimeException("[SJAbstractVisitor] Shouldn't get here: " + parent);
		}
		
		return sc;
	}*/
	
	private ProcedureCall recordProcedureCall(ProcedureCall pc) throws SemanticException // Based on SJSessionTypeChecker counterpart.
	{
		ProcedureInstance pi = pc.procedureInstance();
		
		if (pi instanceof SJProcedureInstance)
		{		
			List<Type> sft = ((SJProcedureInstance) pi).sessionFormalTypes();
			
			if (sft != null) // null means no arguments? Or Polyglot bug (should be empty list)?
			{			
				Iterator i = pc.arguments().iterator();
				
				for (Type theirs : sft)
				{	
					Expr arg = (Expr) i.next();					
					
					if (theirs instanceof SJSessionType)
					{
						if (arg instanceof SJLocalSocket) //FIXME: need to support channel arguments.
						{	
							String sjname = ((SJLocalSocket) arg).sjname();
							
							SJSessionType ours = sjcontext.sessionRemaining(sjname); 
							
							if (((SJVariable) arg).isFinal())
							{					
								for (SJSessionType st = (SJSessionType) theirs; st != null; st = st.child())
								{
									sjcontext.advanceSession(sjname, ours.nodeClone());
									
									ours = ours.child();
								}
							}
							else
							{
								sjcontext.delegateSession(sjname); 
							}
						}																
					}					
				}
			}
		}
		
		return pc;
	}
	
	private void enterSJContext(Node parent, Node n) throws SemanticException // Basically duplicated from SJTypeChecker.
	{

        if (n instanceof TraverseTypeBuildingContext)
        {
            ((TraverseTypeBuildingContext) n).enterSJContext(sjcontext);
        }
        else if (n instanceof LocalDecl) 
		{
			LocalDecl ld = (LocalDecl) n;
			LocalInstance li = ld.localInstance(); 
			
			if (li instanceof SJNamedInstance)
			{
				Type dt = ld.declType(); 
				
				if (dt.isSubtype(SJ_CHANNEL_TYPE))
				{				
					sjcontext.addChannel((SJNamedInstance) li);
				}
				else if (dt.isSubtype(SJ_SOCKET_INTERFACE_TYPE))
				{				
					sjcontext.addSocket((SJNamedInstance) li); // Should be SJUnknownType.
				}
				else if (dt.isSubtype(SJ_SERVER_INTERFACE_TYPE))
				{				
					sjcontext.addServer((SJNamedInstance) li);					
				}
				else if (dt.isSubtype(SJ_SELECTOR_INTERFACE_TYPE))  
				{
					sjcontext.addSelector((SJLocalSelectorInstance) li);
				}
			}
		}
		else if (n instanceof Try)
		{
			if (n instanceof SJTry)
			{
				if (n instanceof SJSessionTry)		
				{
					sjcontext.pushSJSessionTry((SJSessionTry) n);
				}
				else if (n instanceof SJServerTry)		
				{
					sjcontext.pushSJServerTry((SJServerTry) n);
				} 
				else if (n instanceof SJSelectorTry) 
				{
					sjcontext.pushSJSelectorTry((SJSelectorTry) n);
				}
			}
			else
			{
				sjcontext.pushTry();
			}			
		}
		else if (n instanceof SJCompoundOperation) // Must come before the base class cases for Loop/Block.
		{
			if (n instanceof SJBranchOperation)
			{
				if (n instanceof SJOutbranch)
				{
					sjcontext.pushSJBranchCase((SJBranchCase) n);
				}
				else //if (n instanceof SJInbranch)
				{
					sjcontext.pushSJBranchOperation((SJBranchOperation) n);
				}
			}
			else if (n instanceof SJLoopOperation)
			{
				if (n instanceof SJWhile)
				{
					sjcontext.pushSJWhile((SJWhile) n); // FIXME: should differentiate the contexts (including outinwhile).
				}
				else //if (n instanceof SJRecursion)
				{
					sjcontext.pushSJRecursion((SJRecursion) n);
				}
			}
		}
		else if (n instanceof If)
		{			
			sjcontext.pushBranch();
		}				
		else if (n instanceof Loop)
		{
			sjcontext.pushLoop();
		}
		else if (n instanceof Block)
		{
			if (n instanceof SJBranchCase)
			{
				sjcontext.pushSJBranchCase((SJBranchCase) n);
			}			
			else
			{
				sjcontext.pushBlock();
				
				if (parent instanceof MethodDecl)
				{			
					sjcontext.pushMethodBody((MethodDecl) parent);
				}
			}
		}
		else if (n instanceof MethodDecl)
		{
			sjcontext.pushBlock();
		}
		
		sjcontext.setVisitor(this); // ContextVisitor returns a new visitor for each scope.
	}
	
	private SJContextElement leaveSJContext(Node n) throws SemanticException // Duplicated from SJTypeChecker.
	{
		SJContextElement ce = null;
		
		if (n instanceof Try) // Includes SJTry (SJSessionTry, SJServerTry, etc.).
		{
			ce = sjcontext.pop();
		}
		else if (n instanceof SJInbranch || n instanceof SJTypecase)
		{
			ce = sjcontext.pop();
		}
		else if (n instanceof If)
		{
			ce = sjcontext.pop();
		}		
		else if (n instanceof Loop) // Includes SJLoopOperation.
		{
			ce = sjcontext.pop();
		}
		else if (n instanceof Block) // Includes SJRecursion, SJBranchCase and method bodies.
		{
			ce = sjcontext.pop();
		}		
		else if (n instanceof MethodDecl)
		{
			ce = sjcontext.pop();
		}
		
		sjcontext.setVisitor(this); // Is this needed?
		
		return ce;
	}
}
