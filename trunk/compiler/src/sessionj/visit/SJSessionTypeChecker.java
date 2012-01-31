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
import sessionj.ast.SessionSocketCreator;
import sessionj.ast.chanops.SJChannelOperation;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.createops.SJChannelCreate;
import sessionj.ast.createops.SJServerCreate;
import sessionj.ast.selectorops.*;
import sessionj.ast.servops.SJAccept;
import sessionj.ast.servops.SJServerOperation;
import sessionj.ast.sesscasts.SJChannelCast;
import sessionj.ast.sesscasts.SJSessionCast;
import sessionj.ast.sesscasts.SJSessionTypeCast;
import sessionj.ast.sessops.SJInternalOperation;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.TraverseTypeBuildingContext;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.*;
import sessionj.ast.sessvars.*;
import sessionj.extension.SJExtFactory;
import sessionj.extension.sessops.SJSessionOperationExt;
import sessionj.types.SJTypeSystem;
import sessionj.types.contexts.SJBranchContext;
import sessionj.types.contexts.SJContextElement;
import sessionj.types.contexts.SJTypeBuildingContext;
import sessionj.types.contexts.SJTypeBuildingContext_c;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import static sessionj.util.SJCompilerUtils.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import polyglot.util.Position; //<By MQ>
import java.util.Collection;  //<By MQ>
/**
 * @author Raymond
 *
 * This visitor performs the core session type checking and completes session type building. 
 * This means the session type information is recorded onto the AST nodes, so that the subsequent visitors based on SJSessionVisitor can just read this information from the AST. 
 * So new session AST nodes need to be checked and type built by this visitor, and then SJ(Abstract)SessionVisitor needs to look up the recorded information.
 */
public class SJSessionTypeChecker extends ContextVisitor // Maybe factor out an abstract SJContextVisitor parent class. 
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	private SJTypeBuildingContext sjcontext = new SJTypeBuildingContext_c(this, sjts);
  
	//private static final boolean DEBUG = true;
	private static final boolean DEBUG = false;

	public SJSessionTypeChecker(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	// This will be called on the visitor cloned by the parent ContextVisitor enter routine. 
	protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException
	{
		enterSJContext(parent, n);
		
		return this; // Otherwise need to hand over the session context object and update the cv field to the new visitor.
	}
	
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{				
		SJContextElement ce = leaveSJContext(n);
        // Does type checking for compound operations and session-try, etc.
        // Basically, doesn't do anything (no context is popped) for basic operations and so on (the stuff that follows this).
        // So does it matter if we do context pops or the following stuff first?
        // (Shouldn't do, there is no Node n that will trigger both a context pop and the following routines.)
        // Not true: need to pop and process compound contexts before performing type building for those operations.
		
		/*if (n instanceof LocalDecl) // Channel fields currently not permitted.
		// Could make SJChannel/SocketDecl nodes.
		{
			n = checkLocalDecl((LocalDecl) n);						
		}
		else if (n instanceof SJSessionTry)
		{
			n = checkSJSessionTry(ce, (SJSessionTry) n);
		}
		else */if (n instanceof SJSessionOperation) // Must come before Expr, SJBasicOperations are Exprs.
		{
			n = checkSJSessionOperation(parent, (SJSessionOperation) n, ce); // For compound operations, type checked already, but still need to build types.
		}		
		else if (n instanceof SJSpawn) // Must come before Expr (SJSpawn is a Call).
		{
			n = checkSJSpawn((SJSpawn) n);
		}
		else if (n instanceof Expr)
		{
			if (n instanceof Assign)
			{
				n = checkAssign((Assign) n);
			}
			else if (n instanceof NewArray)
			{
				n = checkNewArray((NewArray) n);
			}
			else if (n instanceof SJChannelOperation) 
			{
				n = checkSJChannelOperation(parent, (SJChannelOperation) n); // More of a type building routine.
			}
			else if (n instanceof SJServerOperation)
			{
				n = checkSJServerOperation(parent, (SJServerOperation) n); // More of a type building routine.
			}
			else if (n instanceof SJSelectorOperation) // Must come before ProcedureCall (e.g. for register operations).
			{
				n = checkSJSelectorOperation(parent, (SJSelectorOperation) n); // Does register operations. But select operations are done in assignToSocket (like other SessionSocketCreators).
			}
			else if (n instanceof Cast)
			{
				if (n instanceof SJSessionTypeCast)			
				{
					if (n instanceof SJChannelCast)
					{
						n = checkSJChannelCast((SJChannelCast) n);
					}
					else //if (n instanceof SJSessionCast)
					{
						n = checkSJSessionCast(parent, (SJSessionCast) n);
					}
				}
				else 
				{
					n = checkCast((Cast) n);
				}
			}
			else if (n instanceof ProcedureCall) // Call or New.
			{
				n = checkProcedureCall((ProcedureCall) n, false); // FIXME: session method return types not supported yet.
			}
		}		
		else if (n instanceof ConstructorCall)
		{
			n = checkProcedureCall((ConstructorCall) n, false); 
		}
		else if (n instanceof Branch)
		{
			n = checkBranch((Branch) n);
		}
		else if (n instanceof Return)
		{
			n = checkReturn((Return) n);
		}
		
		return n;
	}
	
	private Node checkSJChannelOperation(Node parent, SJChannelOperation co) throws SemanticException
	{
		if (co instanceof SJRequest) // Not really checking anything, rather finishing the type building process.
		{
			if (!(parent instanceof Assign))
			{
				throw new SemanticException(getVisitorName() + " Illegal session request: " + co);
			}
			
			Receiver target = co.target();
			
			if (target instanceof SJChannelVariable) 
			{
				if (getSessionType(co) instanceof SJUnknownType)
				{
					co = (SJRequest) setSJTypeableExt(sjef, co, sjcontext.findChannel(((SJChannelVariable) target).sjname())); // Actually overwrites the SJNamedExt set by SJSocketDeclTypeBuilder.
				}
				else
				{
					// Type already built by SJSocketDeclTypeBuilder.
				}
			}
			else //if (target instanceof SJChannelCreate) // SJSocketDeclTypeBuilder doesn't allow any other initialiser.
			{
				// Type already built by SJSocketDeclTypeBuilder.
			}
		}
		else
		{
			throw new RuntimeException(getVisitorName() + " Shouldn't get in here.");
		}
		
		return co;
	}
	
	private SJServerOperation checkSJServerOperation(Node parent, SJServerOperation so) throws SemanticException
	{ // Probably better to check for in-scope rules here rather than e.g. in checkAssignToServer.
		if (so instanceof SJAccept) // Not really checking anything, rather finishing the type building process.
		{
			if (!(parent instanceof Assign))
			{
				throw new SemanticException(getVisitorName() + " Illegal session accept: " + so);
			}
			
			Receiver target = so.target();
			
			if (target instanceof SJServerVariable) 
			{
				if (getSessionType(so) instanceof SJUnknownType)
				{
					so = (SJAccept) setSJTypeableExt(sjef, so, sjcontext.findServer(((SJServerVariable) target).sjname())); // Actually overwrites the SJNamedExt set by SJSocketDeclTypeBuilder.
				}
				else
				{
					//throw new RuntimeException(getVisitorName() + " Shouldn't get in here.");
					
					// Can get here for bad programs (i.e. didn't obey server-try scope rules, but that is not checked yet).
				}
			}
			else //if (target instanceof SJServerCreate) // SJSocketDeclTypeBuilder doesn't allow any other initialiser.
			{
				throw new RuntimeException(getVisitorName()
                    + " Shouldn't get in here (Anonymous (inline) server creation not permitted.)");
			}
		}
		else
		{
			throw new RuntimeException(getVisitorName() + " Shouldn't get in here.");
		}
		
		return so;
	}
	
	// FIXME: haven't taken into account the selector filters yet, e.g. SJSelector.ACCEPT.
	private SJSelectorOperation checkSJSelectorOperation(Node parent, SJSelectorOperation so) throws SemanticException // Duplicated from checkSJServerOperation.
	{
		Receiver target = so.target();					
		
		if (!(target instanceof SJSelectorVariable))  
		{			
			throw new RuntimeException(getVisitorName()
                  + " Shouldn't get in here (Anonymous (inline) server creation not permitted).");
		}	
		
		SJSelectorVariable sv = (SJSelectorVariable) target;
		String selectorName = sv.sjname();
		
		if (!sjcontext.selectorInScope(selectorName)) // Should probably do something like this for servers, etc.
		{
			throw new SemanticException(getVisitorName() + " Selector not in scope: " + selectorName);
		}
		
		SJSessionType selectorType;		
		SJSessionType st; // The type associated with the operation (so).
		
		if (so instanceof SJSelect) // Not really checking anything, rather finishing the type building process.
		{
			if (!(parent instanceof Assign))
			{
				throw new SemanticException(getVisitorName() + " Illegal session select: " + so);
			}
			
			selectorType = getSessionType(so); // Built by SJSocketDeclTypeBuilder.
			
			if (selectorType instanceof SJUnknownType) // Selectors must be initialised on declaration.
			{
				throw new RuntimeException(getVisitorName() + " Shouldn't get in here: " + selectorType);
			}
			else
			{
				// SJNamedExt already set by SJSocketDeclTypeBuilder with the associated session type.
			}
			
			st = selectorType.copy();
		}
		else if (so instanceof SJRegister)
		{			
			selectorType = sjcontext.findSelector(selectorName);
			
			String argname;					
			
			if (so instanceof SJRegisterAccept)
			{
				// FIXME: need to remove the registered service (and the server) from scope (and outer scopes?).
				
				SJServerVariable arg = (SJServerVariable) so.arguments().get(0); // Factor out constant.
				
				argname = arg.sjname();
								
				st = sjcontext.currentContext().getService(argname).getCanonicalForm().child(); // Guaranteed to start with sbegin by server typing.				
			}
			else if (so instanceof SJRegisterOutput || so instanceof SJRegisterInput) // Treat like delegation in SJPass.
			{
				SJSocketVariable arg = (SJSocketVariable) so.arguments().get(0); // Factor out constant.
				
				argname = arg.sjname();
				
				st = sjcontext.delegateSession(argname);
			}
			else 
			{
				throw new RuntimeException(getVisitorName() + " Shouldn't get in here: " + so);
			}
			
			if (selectorType instanceof SJSetType)
			{
				if (!((SJSetType) selectorType).contains(st)) // FIXME: seems contains is not working as expected, e.g. selector type is just { ?(T) } but registered type is just ?(T).!<T>, and vice versa.
				{
					throw new SemanticException(getVisitorName() + " Selector type " + selectorType + " incompatible with registered session/service: " + st);
				}
			}
			else // Not sure we're ever coming into here, even for singleton selector types? 
			{
				if (!selectorType.typeEquals(st)) // FIXME: following current set type handling, no subtyping supported for set type members.
				{
					throw new SemanticException(getVisitorName() + " Selector type " + selectorType + " incompatible with registered session/service: " + st);
				}
			}
			
			sv = (SJSelectorVariable) setSJNamedExt(sjef, target, selectorType, selectorName); // Unlike select operations, this hasn't been done yet.			
			so = (SJRegister) so.target(sv);			
		}
		else
		{
			throw new RuntimeException(getVisitorName() + " Shouldn't get in here.");
		}
				
		so = (SJSelectorOperation) setSJTypeableExt(sjef, so, st);
		
		//debug(getVisitorName() + " " + selectorName + ": " + st);
		
		return so;
	}
	
	
	private Node checkAssign(Assign a) throws SemanticException
	{
		if (a.type().isSubtype(SJ_CHANNEL_TYPE))
		{
            checkAssignToChannel(a);
		}
		else if (a.type().isSubtype(SJ_SERVER_INTERFACE_TYPE)) // FIXME: factor out with above.
		{
            checkAssignToServer(a); // CHECKME: now that servers do not have to be final, do we need to do some extra work about assignment of services? 
		}
		else if (a.type().isSubtype(SJ_SELECTOR_INTERFACE_TYPE))
		{
			checkAssignToSelector(a);
		}		
		else if (a.type().isSubtype(SJ_SOCKET_INTERFACE_TYPE)) // Only need to check assign, session socket declaration with initialisation not possible - the session must be in a session-try, and the session-try needs the uninitialised socket to be declared first. 
		{
            a = checkAssignToSocket(a);
		}
		
		return a;
	}

    private Assign checkAssignToSocket(Assign a) throws SemanticException {
        Expr right = a.right();

        // Should make a "SessionSocketCreator" type for session socket create, session-receive
        // (and casts) and session return type method calls. Similarly for channels and servers.
        // Now done for the first 3, not for session return type method calls, nor channels or servers.
        if (right instanceof SessionSocketCreator)
        {
            Expr left = a.left();

            if (!(left instanceof SJSocketVariable))
            {
                throw new SemanticException(getVisitorName() + " Cannot assign session socket to: " + left);
            }

            SJVariable sv = (SJVariable) left;

            String sjname = sv.sjname();

            SJLocalSocketInstance lsi = (SJLocalSocketInstance) sjcontext.getSocket(sjname);

            // Actually, na-final would also take care of this (although it seems the order of pass completion is not very deterministic).
            // For na-final sockets, this is equivalent to the final aspect, in terms of regular Java.
            checkUnusedLocalInstance(lsi);

            SJSessionType st = getSessionType(right);

            sjcontext.addSocket(sjts.SJLocalSocketInstance(lsi, st, sjname)); // Not really necessary.

            if (right instanceof SJRequest) // SJTypeableExt for these are set in checkSJChannelOperation, ... 
            {
                a = a.right(checkSJRequest((SJRequest) right, sjname, st));
            }
            else if (right instanceof SJAccept) // ... checkSJServerOperation, ...
            {
                a = a.right(checkSJAccept((SJAccept) right, sjname, st));
            }
            else if (right instanceof SJSelect) // ... checkSJSelectorOperation, etc.
            {
            	a = a.right(checkSJSelect((SJSelect) right, sjname, st));
            }
            else if (!(right instanceof SJSessionCast)) // SJSessionCast already checked.
            {
            	throw new SemanticException(getVisitorName() + " Shouldn't get in here: " + right);
            }

            // No point to set individual instance type objects for each reference to a (session) variable (also see SJRequest building in SJSocketDeclTypeBuilder). Instance type objects only useful for storing static delcaration-related information, and actually want all instance objects to be the same for all references to a variable.
            a = a.left((Expr) setSJNamedExt(sjef, sv, st, sjname)); // Is this actually useful?
            
            //sjcontext.openSession(sjname, st);
            sjcontext.openSession(sjname, st.getCanonicalForm()); // Set types are popping up in unexpected places: this modification was in response to the session-receive in the Service party of Travel Agency.
            
            if (right instanceof SJRequest)
            {
                sjcontext.advanceSession(sjname, sjts.SJCBeginType());
            }
            else if (right instanceof SJAccept)
            {
		String target = ((SJAccept)right).arguments().get(0).toString().replace("\"", ""); //<By MQ> MQTODO: UGLY WAY of extracting target, needs revising
                sjcontext.advanceSession(sjname, sjts.SJSBeginType(target));  //<By MQ>
            }
        }
        else
        {
            throw new SemanticException(getVisitorName() + " Assign of session socket type not yet supported: " + right);
        }
        
        return a;
    }

    private void checkAssignToServer(Assign a) throws SemanticException {
        Expr right = a.right();

        if (right instanceof SJServerCreate)
        {
            Expr left = a.left();

            if (!(left instanceof SJServerVariable))
            {
                throw new SemanticException(getVisitorName() + " Cannot assign session server to: " + left);
            }

            SJVariable sv = (SJVariable) left;

            String sjname = sv.sjname();
            SJSessionType st = getSessionType(right);

            SJNamedLocalInstance lsi = (SJNamedLocalInstance) sjcontext.getServer(sjname);
            checkUnusedLocalInstance(lsi);

            if (!sjcontext.serviceInScope(sjname))
            {
                throw new SemanticException(getVisitorName() + " Service not in scope: " + sjname);
            }

            if (sjcontext.serviceOpen(sjname)) // Actually, na-final would also take care of this (although it seems the order of pass completion is not very deterministic).
            {
                throw new SemanticException(getVisitorName() + " Service already open: " + sjname);
            }

            if (!st.startsWith(SJSBeginType.class)) // Also checked (and isWellFormed) on session accept.
            {
                throw new SemanticException(getVisitorName() + " Bad session service type: " + st);
            }

            sjcontext.addServer(sjts.SJLocalServerInstance(lsi, st, sjname)); // Replaces the SJUnknownType added by the LocalDecl (for the server socket).
            sjcontext.openService(sjname, st); // Replaces the SJUnknownType added at server-try enter.
        }
        else
        {
            throw new SemanticException(getVisitorName() + " Assign of session server type not yet supported: " + a.right());
        }
    }
        
    private void checkAssignToSelector(Assign a) throws SemanticException
    {
    	throw new SemanticException(getVisitorName() + " assignment of selectors not yet supported: " + a);
    }
    
    private void checkUnusedLocalInstance(SJNamedInstance lsi) throws SemanticException {
        if (!(lsi.sessionType() instanceof SJUnknownType))
        // Cannot count on compiler pass completion order (i.e. noalias type checking before this pass), so cannot just throw RuntimeException.
        {
            throw new SemanticException(getVisitorName() + " Session local variable already used: " + lsi.sjname());
            // Because channels must be na-final, the instance object for the server must have SJUnknownType.
        }
    }

    private void checkAssignToChannel(Assign a) throws SemanticException {
        Expr right = a.right();

        if (right instanceof SJChannelCreate)
        {
            Expr left = a.left();

            if (!(left instanceof SJChannelVariable))
            {
                throw new SemanticException(getVisitorName() + " Cannot assign session channel to: " + left);
            }

            SJChannelVariable cv = (SJChannelVariable) left;

            String sjname = cv.sjname();
            SJSessionType st = getSessionType(right);

            SJLocalChannelInstance lci = (SJLocalChannelInstance) sjcontext.getChannel(sjname);

            if (!(lci.sessionType() instanceof SJUnknownType))
            {
                //throw new RuntimeException(getVisitorName() + " Shouldn't get in here."); // Because channels must be na-final, the instance object for the channel must have SJUnknownType.
                throw new SemanticException(getVisitorName() + " channel type already registered: " + sjname); // Because channels must be na-final, the instance object for the channel must have SJUnknownType.
            }

            if (!st.startsWith(SJCBeginType.class)) // Also checked (and isWellFormed) on session request.
            {
                throw new SemanticException(getVisitorName() + " Bad session channel type: " + st);
            }

            sjcontext.addChannel(sjts.SJLocalChannelInstance(lci, st, sjname));
        }
        else
        {
            throw new SemanticException(getVisitorName() + " Assign of session channel type not yet supported: " + a.right());
        }
    }

    private SJSessionType getSessionType(Node node) {
        return getSJTypeableExt(node).sessionType();
    }

    private NewArray checkNewArray(NewArray na) throws SemanticException
	{
		Type t = na.type();
		
		if (t.isSubtype(SJ_CHANNEL_TYPE) || t.isSubtype(SJ_SOCKET_INTERFACE_TYPE))
		{
			throw new SemanticException(getVisitorName() + " Session channel/socket arrays not permitted: " + na);
		} 
		
		return na;
	}
	
	private SJSessionOperation checkSJSessionOperation(Node parent, SJSessionOperation so, SJContextElement ce) throws SemanticException
	{
        if (so instanceof SJInternalOperation)
		{
			return so;
		}
		
		SJSessionOperationExt soe = getSJSessionOperationExt(so);
				
		for (String targetName : soe.targetNames())
		{
			if (!sjcontext.sessionInScope(targetName))
			{
				throw new SemanticException(getVisitorName() + " Session not in scope: " + targetName);
			}				
			
			if (!sjcontext.sessionActive(targetName))
			{
				throw new SemanticException(getVisitorName() + " Session not active: " + targetName);
			}		
																					
			if (so instanceof SJBasicOperation) 
			{
				SJSessionType expected = sjcontext.expectedSessionOperation(targetName);
				
				if (expected == null)
				{
					throw new SemanticException(getVisitorName() + " Unexpected session operation: " + so);
				}
				
				expected = expected.nodeClone();
				
				SJSessionType st = soe.sessionType(); // The implemented type, already built by SJSessionOperationTypeBuilder.
				
				so = checkSJBasicOperation(parent, (SJBasicOperation) so, targetName, expected, st);
			}
			else if (so instanceof SJCompoundOperation) // The compound operation context has already been popped (and checked) so no need to (re)check expected, and `st' will still have SJUnknownType body (we're going to build it now).
			{
			    assert ce != null;
				so = checkSJCompoundOperation((SJCompoundOperation) so, targetName, ce);
			}	
			else
			{
				throw new SemanticException(getVisitorName() + " Session operation not yet supported: " + so);
			}
		}
		
		return so;
	}
	
	private SJBasicOperation checkSJBasicOperation(Node parent, SJBasicOperation bo, String sjname, SJSessionType expected, SJSessionType st) throws SemanticException
	{							
		if (bo instanceof SJPass) // Includes SJSend.
		{	
			if (bo instanceof SJCopy)
			{
			    bo = checkSJCopy((SJCopy) bo, expected, st);
			}
			else
			{
			    bo = checkSJPass((SJPass) bo, expected, st);
			}
		}
		else if (bo instanceof SJReceive)
		{
			bo = checkSJReceive(parent, (SJReceive) bo, expected, st);
		}
		else if (bo instanceof SJRecurse)
		{												
			bo = checkSJRecurse((SJRecurse) bo, expected, st);
		}
		else //if (!(bo instanceof SJInternalOperation))
		{
			throw new SemanticException(getVisitorName() + " Session operation not yet supported: " + bo);
		}		
		
		st = getSJSessionOperationExt(bo).sessionType(); // Type building is done for inferred receive and higher-order operations.
		
		sjcontext.advanceSession(sjname, st);
		
		debug(getVisitorName() + " " + sjname + ": " + st);			
		
		return bo;
	}

    private void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
        }
    }

    private Expr checkSJRequest(SJRequest r, String sjname, SJSessionType st) throws SemanticException
	{
		if (!sjcontext.sessionInScope(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session not in scope, cannot be opened: " + sjname);
		}
		
		if (sjcontext.sessionActive(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session already open: " + sjname);
		}
		
		if (!st.startsWith(SJCBeginType.class))
		{
			throw new SemanticException(getVisitorName() + " Expected SJCBeginType, not: " + st);
		}		
		
		if (!st.isWellFormed()) // Session type grammar does not guarantee well-placed begins - they can be inserted via bad protocol references. But this is the only place where this is checked? So can have e.g. method declarations with bad channel parameters, although this check prevents them from being used.
		{
			throw new SemanticException(getVisitorName() + " Session type not well-formed: " + st);					
		}		
		
		return r;
	}	
	
	private SJAccept checkSJAccept(SJAccept a, String sjname, SJSessionType st) throws SemanticException // FIXME: factor out with above checkSJRequest.
	{	    
		if (!sjcontext.sessionInScope(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session not in scope, cannot be opened: " + sjname);
		}
		
		if (sjcontext.sessionActive(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session already open: " + sjname);
		}
		
		if (!st.startsWith(SJSBeginType.class))
		{
			throw new SemanticException(getVisitorName() + " Expected SJSBeginType, not: " + st);
		}		
		
		if (!st.isWellFormed())
		{
			throw new SemanticException(getVisitorName() + " Session type not well-formed: " + st);					
		}
		//<By MQ>
		if(!(st instanceof SJSetType_c))
		{
		    throw new SemanticException (getVisitorName() + " Unable to check operation target.");
		}
		else
	        {
		    //MQTODO: VERY UGLY way of doing it, however, couldn't find any other way!! need to recheck this
		    if(a.arguments().size() < 1)
		    {
			throw new SemanticException (getVisitorName() + " Target for operation was not specified!");
		    }
    		    String target = a.arguments().get(0).toString().replace("\"", "");
		    Collection<SJSessionType> stc = ((SJSetType_c)st).getFlattenedForm().getMembers();
		    String expectedTarget = st.toString().replace(st.child().toString(), "");
		    expectedTarget = expectedTarget.substring(0, expectedTarget.indexOf(":"));
		    expectedTarget = expectedTarget.replace("{", "");
		    expectedTarget = expectedTarget.trim();
		    if(expectedTarget == null || expectedTarget == "")
		    {
			throw new SemanticException (getVisitorName() + " Unable to check operation target.");
		    }
		    if(!target.equals(expectedTarget))
		    {
			throw new SemanticException(getVisitorName() + ": Unexpected operation target: Expected: " + expectedTarget + ", not: " + target);
		    }
		}
		//</By MQ>
		return a;
	}		
	
	private SJSelect checkSJSelect(SJSelect s, String sjname, SJSessionType st) throws SemanticException // Duplicated from checkSJAccept.
	{
		/*if (!sjcontext.sessionInScope(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session not in scope, cannot be selected: " + sjname);
		}
		
		if (sjcontext.sessionActive(sjname))
		{
			throw new SemanticException(getVisitorName() + " Session already open: " + sjname);
		}
		
		if (!st.isWellFormed())
		{
			throw new SemanticException(getVisitorName() + " Session type not well-formed: " + st);					
		}*/
		
		return s;
	}
	
    private SJCopy checkSJCopy(SJCopy c, SJSessionType expected, SJSessionType st) throws SemanticException // FIXME: this is actually redundant now (pass is the delegation operation now).
	{				
		Expr arg = (Expr) c.arguments().get(0); // Factor out constant.
		
		if (arg instanceof SJChannelVariable)
		{
			// Nothing special to do.
		}
		/*else if (arg instanceof SJSocketVariable) // Session-send now a pass operation (the argument must be noalias).
		{			
			/*SJSessionOperationExt soe = getSJSessionOperationExt(c);
			
			if (soe.targetNames().size() != 1)
			{
				throw new SemanticException(getVisitorName() + " Cannot multicast session-send: " + arg);
			}			
			
			st = sjts.SJSendType(sjcontext.delegateSession(((SJSocketVariable) arg).sjname()));
									
			c = (SJCopy) setSJSessionOperationExt(sjef, c, st, soe.targetNames());*
			
			throw new SemanticException(getVisitorName() + " Cannot copy session sockets: " + arg);
		}*/

		c = (SJCopy) checkSJPass(c, expected, st);
		
		return c;
	}
		
        private SJBasicOperation checkSJPass(SJPass p, SJSessionType expected, SJSessionType st) throws SemanticException //Includes SJSend (and SJCopy).
	{						
	        //<By MQ>
    		//String stTarget = st.toString().substring(0, st.toString().indexOf(":")).replace("\"", "");
    	        if(p.realArgs().size() < 2)
		{
		    throw new SemanticException(getVisitorName() + ": operation target was not specified!");
		}
	        String stTarget = p.realArgs().get(1).toString().replace("\"", "");
		Expr arg = (Expr) p.arguments().get(0); // Factor out constant.
		if (arg instanceof SJChannelVariable) // Shared channel passing.
		{
			// Nothing special to do (multicast allowed). Channel variables will be checked (by SJNoAliasTypeChecker) to be na-final, and so can only be sent using copy. 
		}
		else if (arg instanceof SJSocketVariable) // Session delegation. 
		{
			if (/*p instanceof SJSend || */p instanceof SJCopy) // Pass takes a noalias argument; send takes an ordinary argument (and hence also noalias); copy takes a na-final argument (and hence both noalias and ordinary arguments). 
			{			
				throw new SemanticException(getVisitorName() + " Cannot copy session sockets: " + arg);
			}
			else 
			{			
				SJSessionOperationExt soe = getSJSessionOperationExt(p);
				
				if (soe.targetNames().size() != 1)
				{
					throw new SemanticException(getVisitorName() + " Cannot multicast session-send: " + arg);
				}			
				
				st = sjts.SJSendType(stTarget, sjcontext.delegateSession(((SJVariable) arg).sjname())); //<By MQ> Added target name
														
				p = (SJPass) setSJSessionOperationExt(sjef, p, st, soe.targetNames());
			}
		}
		
		if (!expected.isSubtype(st))
		{
			throw new SemanticException(getVisitorName() + " (1) Expected " + expected + ", not: " + st);
		}
		String expectedTarget = expected.toString().substring(0, expected.toString().indexOf(":")).replace("\"", "");
		//</By MQ>
		//<By MQ> Checking if the send is to the correct target
		if(stTarget.equals(expectedTarget) == false)
		{
		    throw new SemanticException(getVisitorName() + ": Unexpected operation target: Expected: " + expected + ", not: " + st);
		}
		//</By MQ>

		return p;
	}
	
	private SJReceive checkSJReceive(Node parent, SJReceive r, SJSessionType expected, SJSessionType st) throws SemanticException
	{
	    //<By MQ>Checking if the receive is from the correct source
    	        if(r.realArgs().size() < 1)
		{
		    throw new SemanticException(getVisitorName() + ": operation target was not specified!");
		}

	        String stTarget = r.realArgs().get(0).toString().replace("\"", "");
	        String expectedTarget = expected.toString().substring(0, expected.toString().indexOf(":")).replace("\"", "");//<By MQ>
		if(stTarget.equals(expectedTarget) == false)
		{
		    throw new SemanticException(getVisitorName() + ": Unexpected operation target: Expected: " + expected + ", not: " + st);
		}
		//</By MQ>

		if (!expected.startsWith(SJReceiveType.class))
		{
			throw new SemanticException(getVisitorName() + " (2) Expected " + expected + ", not: " + st);
		}
		
		if (((SJMessageCommunicationType) st).messageType() instanceof SJUnknownType) 
        // FIXME: need to override base type checking for uncasted receive.
        // // Cast to be inserted in later translation pass.
		{							
			if (((SJMessageCommunicationType) expected).messageType() instanceof SJSessionType)
			{
				r = (SJReceive) checkSessionReceiveParent(parent, r);
			}
			
			r = (SJReceive) setSJSessionOperationExt(sjef, r, expected, getSJSessionOperationExt(r).targetNames());
		}
		else if (!expected.isSubtype(st))
		{
			throw new SemanticException(getVisitorName() + " (3) Expected " + expected + ", not: " + st); // Includes higher-order session-receive. // And channel-receive.
		}		
		
		return r;
	}
	
	private SJRecurse checkSJRecurse(SJRecurse r, SJSessionType expected, SJSessionType st) throws SemanticException
	{
		sjcontext.recurseSessions(getSJSessionOperationExt(r).targetNames());
        // Will be unnecessarily repeated for each target.
		
		if (!expected.typeEquals(st))
		{
			throw new SemanticException(getVisitorName() + " Expected " + expected + ", not: " + st);
		}
		
		return r;
	}
	
	private SJSessionOperation checkSJCompoundOperation(SJCompoundOperation co, String sjname, SJContextElement ce) throws SemanticException
	{
		SJSessionType typeForNode = ce.getImplemented(sjname);
        // Type already built by context pop, just need to attach it to the node.
		
		if (co instanceof SJBranchOperation)
		{
		    //<By MQ>
		    if(co instanceof SJInbranch)
		    {
			List args = ((SJInbranch)co).inlabel().realArgs();
			if(args.size() < 1)
			{
			    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Target for inbranch was not specified!");
			}
			if(!(typeForNode instanceof SJInbranchType))
			{
			    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Shouldn't get here.");
			}
			String expectedTarget = ((SJInbranchType_c)typeForNode).target();
			String target = args.get(0).toString().replace("\"", "");
			if(target.equals(expectedTarget) == false)
			{
			    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Unexpected operation target: Expected: " + expectedTarget + ", not: " + target);
			}
		    }
		   
            // Nothing to check: correct compound type constructor checked on context push, and
            // body type checked inductively.
		}
		else if(co instanceof SJLoopOperation)
		{
		    if(co instanceof SJWhile)
		    {
			if(co instanceof SJInwhile)
			{
			    List args = ((SJInwhile_c)co).arguments();
			    if(args.size() < 1)
				{
				    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Target for inwhile was not specified!");
				}
			    if(!(typeForNode instanceof SJInwhileType))
				{
				    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Shouldn't get here.");
				}
			    String expectedTarget = ((SJInwhileType_c)typeForNode).target();
			    String target = args.get(0).toString().replace("\"", "");
			    if(target.equals(expectedTarget) == false)
				{
				    throw new SemanticException("[SJSessionTypeChecker.checkSJCompoundOperation]: Unexpected operation target: Expected: " + expectedTarget + ", not: " + target);
				}
			}
		    }
		}
		 //</By MQ>
        else if (co instanceof SJTypecase)
        {
            co = ((SJTypecase) co).sessionTypeCheck(typeForNode);
        }
		else
		{
			throw new SemanticException(getVisitorName() + " Session operation not yet supported: " + co);
		}

        
        co = (SJCompoundOperation) updateSessionType(co, typeForNode, sjef);

		debug(getVisitorName() + " " + sjname + ": " + getSessionType(co));
		
		return co;
	}

    public static SJSessionOperation updateSessionType(SJSessionOperation bo, SJSessionType st, SJExtFactory sjef) {
        SJSessionOperationExt soe = getSJSessionOperationExt(bo);
        return (SJSessionOperation) setSJSessionOperationExt(sjef, bo, st, soe.targetNames());
    }
	
	private Node checkSJSpawn(SJSpawn s) throws SemanticException
	{		
		s = (SJSpawn) checkProcedureCall(s, true);
		
		List<SJSessionType> sts = new LinkedList<SJSessionType>();

         // Maybe, in the current implementation, this routine belongs better in SJNoAliasTypeChecker - but in general, belongs better here?
        for (Object o : s.arguments()) {
            SJVariable v = (SJVariable) o;
            SJSessionType st;

            if (v instanceof SJLocalChannel) {
                if (!((Local) v).localInstance().flags().isFinal()) {
                    throw new SemanticException(getVisitorName() + " SJThread-spawn channel arguments must be na-final: " + v);
                }

                st = sjcontext.findChannel(v.sjname()); // Actually a bit pointless, or maybe just doesn't make sense?
            } else {
                if (((Local) v).localInstance().flags().isFinal()) {
                    throw new SemanticException(getVisitorName() + " Cannot spawn session threads for final session sockets: " + v);
                }

                //st = sjts.SJSendType(sjcontext.delegateSession(v.sjname())); // Why a send type?
                st = sjts.SJDelegatedType(sjcontext.delegateSession(v.sjname()));
            }

            sts.add(st);
        }
		
		/*for (String sjname : s.targetNames())
		{								
			sts.add(sjts.SJSendType(sjcontext.delegateSession(sjname)));
		}*/
		
		s = (SJSpawn) setSJSessionOperationExt(s, s.sjnames(), sts);
		
		return s;
	}
	
	private SJChannelCast checkSJChannelCast(SJChannelCast sc) throws SemanticException
	{
		SJSessionType st = sc.sessionType().type();
		
		if (!st.startsWith(SJBeginType.class))
		{
			throw new SemanticException(getVisitorName() + " Bad channel cast: " + st);
		}
		
		return sc;
	}
	
	private Node checkSJSessionCast(Node parent, SJSessionCast sc) throws SemanticException
	{
		SJSessionType st = sc.sessionType().type();
		
		if (st.startsWith(SJBeginType.class))
		{
			throw new SemanticException(getVisitorName() + " Bad session cast: " + st);
		}
		
		sc = (SJSessionCast) checkSessionReceiveParent(parent, sc);
        // FIXME: non-casted session-receives not yet supported? (Apart from receive type inference
        //  - planning to insert casts after this pass?)
		
		return sc;
	}
	
	private Node checkCast(Cast c) throws SemanticException
	{
		Expr e = c.expr();
		
		if (e instanceof SJSocketVariable)
		{
			throw new SemanticException(getVisitorName() + " Cannot cast session sockets: " + e);
		}
		
		return c;
	}
	
	private Node checkProcedureCall(ProcedureCall pc, boolean forSJSpawn) throws SemanticException
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
						String sjname = ((SJVariable) arg).sjname();
						
						if (arg instanceof SJLocalChannel)
						{
							// Session type information is not actually recorded for SJVariables (no extension object for it).
							//SJSessionType ours = sjcontext.findChannel(sjname);
							SJSessionType ours = sjcontext.findChannel(sjname).getCanonicalForm();  // FIXME: find best place to do the standardisation

                            checkMethodTakesSameSessionType(theirs, ours);
                        }
						else if (arg instanceof SJLocalSocket) // Guaranteed noalias by parser.
						{							
                            checkMethodSessionType(theirs, arg, sjname, sjcontext.sessionRemaining(sjname));
                            if (!((SJVariable)arg).isFinal() && !forSJSpawn) // checkSJSpawn will do this itself.
                            {
                                sjcontext.delegateSession(sjname); // Will redundantly redo the sessionRemaining routine.
                            }
						} else if (arg instanceof SJLocalServer) {
                            checkMethodSessionType(theirs, arg, sjname, sjcontext.findServer(sjname));
                        } else {  // Never happens ?
							throw new SemanticException(getVisitorName() + " Expected " + theirs + ", not: " + arg);
						}										
					}
					else if (arg instanceof SJVariable)
					{
						throw new SemanticException(getVisitorName() + " Expected " + theirs + ", not: " + arg);
					}
				}
			}
		}
		
		return pc;
	}

    private void checkMethodSessionType(Type theirs, Expr arg, String sjname, SJSessionType ours) throws SemanticException {
        if (((SJVariable) arg).isFinal()) { // Cannot happen for SJSpawn.
            checkMethodTakesPrefixSessionType(theirs, sjname, ours);
            // ours doesn't need to be null here - na-final parameters can be a prefix of the "delegated" session.
        }
        else checkMethodTakesSameSessionType(theirs, ours);
    }

    private void checkMethodTakesSameSessionType(Type theirs, SJSessionType ours) throws SemanticException {    	
        if (ours == null || !ours.isSubtype(theirs))
        {
            throw new SemanticException(getVisitorName() + " Expected " + theirs + ", not: " + ours);
        }
    }

    private void checkMethodTakesPrefixSessionType(Type theirs, String sjname, SJSessionType ours) throws SemanticException {
        for (SJSessionType st = (SJSessionType) theirs; st != null; st = st.child())
        {
            if (ours == null || !ours.nodeSubtype(st))
            {
                throw new SemanticException(getVisitorName() + " Expected " + theirs + ", not: " + ours);
            }

            sjcontext.advanceSession(sjname, ours.nodeClone());

            ours = ours.child();
        }
    }

    private Node checkBranch(Branch b) throws SemanticException
	{
		//sjcontext.checkSessionsCompleted(); // FIXME: needs additional translation support for in/outwhile (need to send the final false flag). Also, may not work properly - maybe we just need to check the sessions in scope (the ones we can do operations on), not all currently active sessions (which includes all open sessions from outer scopes).
		if (sjcontext.inSJBranchCaseContext() || sjcontext.inSJSessionLoopContext())
		{
			throw new SemanticException(getVisitorName() + " Illegal branch within session branch/loop context: " + b);
		}
		
		return b;
	}
	
	private Node checkReturn(Return r) throws SemanticException
	{
		//sjcontext.checkSessionsCompleted(); // FIXME: as for Branch, needs additional translation support for in/outwhile (need to send the final false flag). Also, may not work properly - maybe we just need to check the sessions in scope (the ones we can do operations on), not all currently active sessions (which includes all open sessions from outer scopes). 
		
		if (sjcontext.inSJBranchCaseContext() || sjcontext.inSJSessionLoopContext())
		{
			throw new SemanticException(getVisitorName() + " Illegal return within session branch/loop context: " + r);
		}
		
		return r;
	}
	
	private void enterSJContext(Node parent, Node n) throws SemanticException
    // Could be factored out into an SJContextVisitor. Olivier: Ongoing, see TraverseTypeBuildingContext
	{
    if (n instanceof TraverseTypeBuildingContext) // Should rename: it's not a context itself, but an AST node that should have enter/leave context actions.
    {
      ((TraverseTypeBuildingContext) n).enterSJContext(sjcontext);
    }
    else if (n instanceof LocalDecl) // Parsing (of initialisation expression) and type building done by SJChannel/SocketDeclTypeBuilder.
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
					sjcontext.addServer((SJLocalServerInstance) li); // Server fields not currently supported.					
				}
				else if (dt.isSubtype(SJ_SELECTOR_INTERFACE_TYPE)) // Unlike servers, selectors are initialised out-of-scope at declaration. We can get the type directly from here. 
				{
					sjcontext.addSelector((SJLocalSelectorInstance) li);
				}
				else if (!(dt.isSubtype(SJ_PROTOCOL_TYPE))) // No need to manually record protocol instances.
				{
					throw new SemanticException(getVisitorName() + " Shouldn't get in here: " + dt);
				}
				//<By MQ> Added for supporting gprotocol
				else if (!(dt.isSubtype(SJ_GPROTOCOL_TYPE))) // No need to manually record protocol instances.
				{
					throw new SemanticException(getVisitorName() + " Shouldn't get in here: " + dt);
				}
				//</By MQ>
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
	
	private SJContextElement leaveSJContext(Node n) throws SemanticException
	{
		SJContextElement ce = null;
		
		if (n instanceof Try) // Includes SJTry (SJSessionTry, SJServerTry, etc.).
		{
			ce = sjcontext.pop();				
		}
		else if (n instanceof SJInbranch)
		{
			ce = sjcontext.pop();
		}
        else if (n instanceof TraverseTypeBuildingContext)
        {
            // Tested before instanceof Block so SJWhen is handled here 
            ce = ((TraverseTypeBuildingContext) n).leaveSJContext(sjcontext);
        }
		else if (n instanceof If)
		{
			ce = sjcontext.pop();
			
			if (((SJBranchContext) ce).hasSessionImplementations() && ((If) n).alternative() == null)
			{
				throw new SemanticException(getVisitorName() + " if missing an alternative: " + n);
			}
		}		
		else if (n instanceof Loop) // Includes SJLoopOperation.
		{
			ce = sjcontext.pop();
		}
		else if (n instanceof Block)
        // Includes SJRecursion, SJBranchCase and method bodies. 
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
	
	private Expr checkSessionReceiveParent(Node parent, Expr e) throws SemanticException
	{
		if (!(parent instanceof LocalAssign))
		{
			throw new SemanticException(getVisitorName() + " Received session incorrectly handled: " + e);
		}
		
		return e;
	}
	
	private static String getVisitorName()
	{
		return "[SJSessionTypeChecker]";
	}
}
