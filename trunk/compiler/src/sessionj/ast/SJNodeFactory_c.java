 package sessionj.ast;

import polyglot.ast.*;
import polyglot.frontend.ExtensionInfo;
import polyglot.qq.QQ;
import polyglot.types.Flags;
import polyglot.util.Position;
import polyglot.parse.Name;
import static sessionj.SJConstants.*;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.chanops.SJRequest_c;
import sessionj.ast.createops.*;
import sessionj.ast.noalias.*;
import sessionj.ast.protocoldecls.*;
import sessionj.ast.servops.*;
import sessionj.ast.selectorops.*;
import sessionj.ast.sesscasts.*;
import sessionj.ast.sessformals.SJFormal_c;
import sessionj.ast.sessformals.SJFormal;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.*;
import sessionj.ast.sessvars.*;
import sessionj.ast.typenodes.*;
import sessionj.extension.SJExtFactory;
import sessionj.extension.SJExtFactory_c;
import static sessionj.util.SJCompilerUtils.setSJNoAliasFinalExt;
import sessionj.util.SJLabel;
import sessionj.util.SJCompilerUtils;

import java.util.LinkedList;
import java.util.List;

import sessionj.types.sesstypes.*; //<By MQ>
import java.util.*; //<By MQ>
/**
 * NodeFactory for sessionj extension.
 */
public class SJNodeFactory_c extends NodeFactory_c implements SJNodeFactory 
{
    // TODO:  Implement factory methods for new AST nodes.
    // TODO:  Override factory methods for overriden AST nodes.
    // TODO:  Override factory methods for AST nodes with new extension nodes.
	
	private ExtensionInfo extInfo; 
	
	private final SJExtFactory sjef = (SJExtFactory) super.extFactory();
	//private SJDelFactory sjdf = (SJDelFactory) super.delFactory();
        private List<Id> participantsList; //<By MQ>
        private Id participant; //<By MQ>
    public SJTypeNode unoptimizedProtocol;  //<By MQ>
    public SJTypeNode optimizedProtocol;  //<By MQ>
	public SJNodeFactory_c()
	{
		super(new SJExtFactory_c());
		//super(new SJExtFactory_c(), new SJDelFactory_c());		
	}

	public SJExtFactory extFactory()
	{
		return sjef;
	}
	
	/*public SJDelFactory delFactory()
	{
		return sjdf;
	}*/
	
	/*
	 * public SJDelFactory delFactory() { return (SJDelFactory)
	 * super.delFactory(); }
	 */

	public void setExtensionInfo(ExtensionInfo extInfo)
	{
		this.extInfo = extInfo;
	}
	
	public SJAmbNoAliasTypeNode SJAmbNoAliasTypeNode(Position pos, AmbTypeNode atn)
	{
        // n = (SJAmbNoAliasTypeNode) n.ext(extFactory().SJNoAliasExt()); // No
		// point: is discarded by disambiguation pass, and is not actually needed
		// before then. So is now made and attached to the (new unambiguous) node by
		// the disambiguation pass.

		return new SJAmbNoAliasTypeNode_c(pos, atn);
	}

	public SJNoAliasArrayTypeNode SJNoAliasArrayTypeNode(Position pos, ArrayTypeNode atn)
	{
        return new SJNoAliasArrayTypeNode_c(pos, atn);
	}

	public SJNoAliasCanonicalTypeNode SJNoAliasCanonicalTypeNode(Position pos, CanonicalTypeNode ctn)
	{
        return new SJNoAliasCanonicalTypeNode_c(pos, ctn);
	}
	
    //<By MQ> Added for supporting gprotocol declaration
    
    private SJTypeNode cleanupProtocol(SJTypeNode tn)  //removes any GProtocol nodes from the lprotocol
    {
	if(tn == null)
	    return null;
	if(tn instanceof SJGMsgNode)
	{
	    if(tn.child() != null)
		tn = (SJTypeNode)tn.child().position(tn.child().position());
	    else
		tn = null;
	    return cleanupProtocol(tn);
	}
	else if(tn instanceof SJBranchNode)
	{
	    for(int i = 0; i < ((SJBranchNode_c)tn).branchCases().size(); i++) //(SJBranchCaseNode bc : ((SJBranchNode_c)tn).branchCases())
	    {		
		SJBranchCaseNode bc = ((SJBranchNode_c)tn).branchCases().remove(0);
		//if(bc.body() != null)
		{
		    //SJTypeNode tnBody = (SJTypeNode)bc.body().position(bc.body().position());		    
		    SJTypeNode bcbody = cleanupProtocol(bc.body());
		    ((SJBranchNode_c)tn).branchCases().add(new SJBranchCaseNode_c(bc.position(), bc.label(), bcbody));
		}
	    }
	    return tn;	    
	}
	else if(tn instanceof SJLoopNode)
	{
	    ((SJLoopNode)tn).body(cleanupProtocol(((SJLoopNode)tn).body()));
	    return tn;
	}
	else
	    return tn.child(cleanupProtocol(tn.child()));
    }

    public SJTypeNode convertToGProtocol(SJTypeNode tn)  //converts the lprotocol to gprotocol
    {
	if(tn == null)
            return null;
	tn = (SJTypeNode)tn.position(tn.position());
	if(tn instanceof SJBeginNode)
	{
	    if(tn instanceof SJCBeginNode)
		return new SJGBeginNode_c(tn.position(), thisParticipant().id()).child(convertToGProtocol(tn.child()));
	    else if(tn instanceof SJSBeginNode)
		return new SJGBeginNode_c(tn.position(), ((SJSBeginNode_c)tn).target()).child(convertToGProtocol(tn.child()));
	}	   
        if(tn instanceof SJMessageCommunicationNode)
	{
	    if(tn instanceof SJGMsgNode)
	    {
		return tn.child(convertToGProtocol(tn.child()));
	    }
	    else if(tn instanceof SJSendNode)
	    {
		SJTypeNode t = new SJGMsgNode_c(tn.position(), thisParticipant().id(), ((SJSendNode_c)tn).target(), ((SJSendNode_c)tn).messageType());
		if(tn.child() == null)
		    return t;
		else
		    return t.child(convertToGProtocol((SJTypeNode)tn.child().position(tn.child().position())));
	    }
	    else if (tn instanceof SJReceiveNode)
	    {
		SJTypeNode t = new SJGMsgNode_c(tn.position(), ((SJReceiveNode_c)tn).target(), thisParticipant().id(), ((SJReceiveNode_c)tn).messageType());
		if(tn.child() == null)
		    return t;
		else
		    return t.child(convertToGProtocol((SJTypeNode)tn.child().position(tn.child().position())));
	    }
	}
        else if(tn instanceof SJBranchNode)
	    {
		List<SJBranchCaseNode> bcs = new ArrayList<SJBranchCaseNode>();
		for(SJBranchCaseNode bc : ((SJBranchNode_c)tn).branchCases())
	        {
		    SJTypeNode bcnBody = null;
		    if((SJTypeNode)bc.body() != null)
			bcnBody = (SJTypeNode)bc.body().position(bc.body().position());
		    SJBranchCaseNode bcn = SJBranchCaseNode(bc.position(), bc.label(), convertToGProtocol(bcnBody));
		    bcs.add(bcn);
		}
		if(tn instanceof SJOutbranchNode)
		    return new SJGBranchNode_c(tn.position(), thisParticipant().id(), bcs);
		else if(tn instanceof SJInbranchNode)
		    return new SJGBranchNode_c(tn.position(), ((SJInbranchNode_c)tn).target(), bcs);
	    }
        else if(tn instanceof SJLoopNode)
	    {
		SJTypeNode loopBody = null;
		if((SJTypeNode) ((SJLoopNode)tn).body() != null)
		    loopBody = (SJTypeNode) ((SJLoopNode)tn).body().position(tn.position());
		if(tn instanceof SJOutwhileNode)
		    return new SJGLoopNode_c(tn.position(), thisParticipant().id(), convertToGProtocol(loopBody));
		else if(tn instanceof SJInwhileNode)
		    return new SJGLoopNode_c(tn.position(), ((SJInwhileNode_c)tn).target(), convertToGProtocol(loopBody));
	    }
        else
            return tn.child(convertToGProtocol((SJTypeNode)tn.child().position(tn.child().position())));
	return null;
    }

    public SJTypeNode optimizeProtocol(SJTypeNode tn)
    {	
	unoptimizedProtocol = convertToGProtocol((SJTypeNode)tn.position(tn.position()));
	SJTypeNode otn = cleanupProtocol((SJTypeNode)tn.position(tn.position()));
	//System.out.println("\n\nbefore cleanup: " + tn);
	//System.out.println("\n\ngprotocol: " + unoptimizedProtocol);
	//System.out.println("\n\nafter cleanup: " + otn);

	return otn;
    }

    public SJTypeNode SJParticipant(Position pos, Id p)
    {
	participant = p;
	return null;
    }

	public SJFieldProtocolDecl SJFieldGProtocolDecl(Position pos, Flags flags, Id name, SJTypeNode tn)
        {
	    SJFieldProtocolDecl protocol = SJFieldProtocolDecl(pos, flags, name, tn);
	    return protocol; //SJFieldProtocolDecl(pos, flags, name, tn);
	}

    public SJLocalProtocolDecl SJLocalGProtocolDecl(Position pos, Id name, SJTypeNode tn)
	{
	    return SJLocalProtocolDecl(pos, name, tn);
	}

	public SJGProtocolRefNode SJGProtocolRefNode(Position pos, Receiver target)
	{
		return new SJGProtocolRefNode_c(pos, target);
	}
	
	public SJGProtocolDualNode SJGProtocolDualNode(Position pos, Receiver target)
	{
		return new SJGProtocolDualNode_c(pos, target);
	}
        public SJParticipantsNode SJParticipantsNode(Position pos, List<Id> IDs)
	{
	    //Possibly a bad-style hack, but it will dramatically simplify GProtocol to LProtocol conversion. Just save the first participant's name to identify "me".
	    this.participantsList = IDs;
	    assertExists(thisParticipant());
            return new SJParticipantsNode_c(pos, IDs);
	}
    private void assertExists(Id pid)
    {
	Boolean found = false;
	for(Id id : participantsList)
	    if(id.toString().equals(pid.toString()))
		found = true;
	if(!found)
        {
	    System.out.println("Undeclared participant: " + pid);
	    System.exit(0);
	}
    }
    private Id thisParticipant()
    {
	if(participant == null)
	{
	    System.out.println("This participant's Id was not declared, make sure to declare it before protocol declaration. eg: participant Client1;");
	    System.exit(0);
	}
	return participant;
    }
        public SJMessageCommunicationNode SJGMsgNode(Position pos, Id sender, Id receiver, TypeNode/*List*/ messageType)
	{
	    SJMessageCommunicationNode result = null;
	    assertExists(receiver);
	    assertExists(sender);
	    if(thisParticipant().id().equals(sender.id()))
		result = SJSendNode(pos, receiver, messageType);
	    else if(thisParticipant().id().equals(receiver.id()))
		result = SJReceiveNode(pos, sender, messageType);
	    //else
	    //result = new SJGMsgNode_c(pos, sender.toString(), receiver.toString(), messageType);
	    //System.out.println("sender: " + sender + ", receiver: " + receiver + ", result: "+ result);
	    return result;
	}
        public SJBranchNode SJGBranchNode(Position pos, Id sender, List<SJBranchCaseNode> branchCases)
	{
	    SJBranchNode result = null;
    	    assertExists(sender);
	    if(thisParticipant().id().equals(sender.id()))
		result = SJOutbranchNode(pos, branchCases);
	    else //if(participantInBranch(thisParticipant(), branchCases))  //generate inbranch
	    {
		result = SJInbranchNode(pos, sender, branchCases);
	    }
	    //else
	    return result;				       
	}
	    private Boolean participantInBranch(Id pid, List<SJBranchCaseNode> branchCases)
	    {
		for(SJBranchCaseNode bcNode : branchCases)
		{
		    if(bcNode.body() != null)
		       return true;
		}	   
		return false;
	    }
    /*private Boolean participantInNode(Id pid, SJTypeNode tn) //Not required any more!
	    {
		if(tn == null)
		    return false;
		if(tn instanceof SJMessageCommunicationNode)
		{
		    return true;
		    if(tn instanceof SJSendNode)
		    {
			if(((SJSendNode_c)tn).target().equals(pid.id()))
			    return true;
			else
			    return false;
			
		    }
		    else if(tn instanceof SJReceiveNode)
		    {
		    if(((SJReceiveNode_c)tn).target().equals(pid.id()))
			return true;
		    else
			return false;
			}
		}
		else if(tn instanceof SJInbranchNode)
		{
		    if(((SJInbranchNode_c)tn).target().equals(pid.id()))
			return true;
		    else
			return false;
		}
		else if(tn instanceof SJOutbranch)
		{
		    return true;
		}
		else if(tn instanceof SJInwhileNode)
		{
		    if(((SJInwhileNode_c)tn).target().equals(pid.id()))
			return true;
		    else
			return false;
		}
		else if(tn instanceof SJOutwhile)
		{
		    return true;
		}
		else if(tn.child() != null)
		    return participantInNode(pid, tn.child());
		return false;
		}*/
        public SJLoopNode SJGLoopNode(Position pos, Id sender, SJTypeNode body)
	{
	    assertExists(sender);
	    if(thisParticipant().id().equals(sender.id()))
		return SJOutwhileNode(pos, body);
	    else //if(body != null)
		return SJInwhileNode(pos, sender, body);
	    /*else
	      return null;*/
	}
        public SJBeginNode SJGBeginNode(Position pos, Id sender)
	{
	    SJBeginNode result = null;
	    assertExists(sender);
	    if(sender.id().equals(thisParticipant().id()))
		result = SJCBeginNode(pos);
	    else
		result = SJSBeginNode(pos, sender);
	    return result;
	}

    //</By MQ>
	public SJFieldProtocolDecl SJFieldProtocolDecl(Position pos, Flags flags, Id name, SJTypeNode tn)
        {
		assert pos != null;
		SJFieldProtocolDecl n = new SJFieldProtocolDecl_c
            (pos, flags.Final(), CanonicalTypeNode(pos, SJ_PROTOCOL_TYPE), name, NullLit(pos), tn);
        // Null initialization overwritten by protocol declaration translation pass (dummy init.
        // needed to satisfy base type checking).		
		n = (SJFieldProtocolDecl) n.type(convertToNoAliasTypeNode(n.type(), true));
        // Makes the object type: noalias SJProtocol. Also adds the extension objects for
        // the session type (later disambiguated, etc. by SJProtocolDeclTypeBuilder).
		return n;
	}

    public SJLocalProtocolDecl SJLocalProtocolDecl(Position pos, Id name, SJTypeNode tn)
	{
		SJLocalProtocolDecl n = new SJLocalProtocolDecl_c
            (pos, Flags.FINAL, CanonicalTypeNode(pos, SJ_PROTOCOL_TYPE), name, NullLit(pos), tn);
		
		n = (SJLocalProtocolDecl) n.type(convertToNoAliasTypeNode(n.type(), true));
        return n;
	}
	
	public SJCBeginNode SJCBeginNode(Position pos)
	{
		return new SJCBeginNode_c(pos);
	}
	
        public SJSBeginNode SJSBeginNode(Position pos, Id target)  //<By MQ> Added Id
	{
	        return new SJSBeginNode_c(pos, target.id()); //<By MQ> Added Id
	}
        //<By MQ> Changed TypeNode to List in the 2nd argument of SJSendNode
        public SJSendNode SJSendNode(Position pos, Id target, TypeNode/*List*/ messageType)
	{
	    return new SJSendNode_c(pos, target.id(), messageType);
	}
	//</By MQ>
	public SJOutbranchNode SJOutbranchNode(Position pos, List<SJBranchCaseNode> branchCases)
	{
		return new SJOutbranchNode_c(pos, branchCases);
	}
	
	/*public SJOutbranchNode SJOutbranchNode(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped)
	{
		return new SJOutbranchNode_c(pos, branchCases, isDependentlyTyped);
	}*/
	
	public SJInbranchNode SJInbranchNode(Position pos, Id target, List<SJBranchCaseNode> branchCases) //<By MQ> Added Id target
	{
	    return new SJInbranchNode_c(pos, target.id(), branchCases); //<By MQ> Added Id target
	}
	
	/*public SJInbranchNode SJInbranchNode(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped)
	{
		return new SJInbranchNode_c(pos, branchCases, isDependentlyTyped);
	}*/
	
	public SJBranchCaseNode SJBranchCaseNode(Position pos, SJLabel lab, SJTypeNode body)
	{
		return new SJBranchCaseNode_c(pos, lab, body);
	}
	//<By MQ> Changed TypeNode to List in the 2nd argument of SJReceiveNode
    public SJReceiveNode SJReceiveNode(Position pos, Id target, TypeNode /*List*/ messageType) //<By MQ> Added Id target
	//</By MQ>
	{
	    return new SJReceiveNode_c(pos, target.id(),  messageType); //<By MQ> Added Id target
	}

	public SJOutwhileNode SJOutwhileNode(Position pos, SJTypeNode body)
	{
		return new SJOutwhileNode_c(pos, body);
	}
	
	public SJInwhileNode SJInwhileNode(Position pos, Id target, SJTypeNode body) //<By MQ> Added Id target
	{
	        return new SJInwhileNode_c(pos, target.id(), body); //<By MQ> Added Id target
	}

	public SJRecursionNode SJRecursionNode(Position pos, SJLabel lab, SJTypeNode body)
	{
		return new SJRecursionNode_c(pos, lab, body);
	}
	
	public SJRecurseNode SJRecurseNode(Position pos, SJLabel lab)
	{
		return new SJRecurseNode_c(pos, lab);
	}
	
	public SJProtocolRefNode SJProtocolRefNode(Position pos, Receiver target)
	{
		return new SJProtocolRefNode_c(pos, target);
	}
	
	public SJProtocolDualNode SJProtocolDualNode(Position pos, Receiver target)
	{
		return new SJProtocolDualNode_c(pos, target);
	}
	
	public SJChannelCreate SJChannelCreate(Position pos, List arguments)
	{
		CanonicalTypeNode target = CanonicalTypeNode(pos, SJ_CHANNEL_TYPE);
		Id name = Id(pos, SJ_CHANNEL_CREATE);

        //return (SJChannelCreate) n.ext(extFactory().SJCreateOperationExt());
		
		return new SJChannelCreate_c(pos, target, name, arguments);
	}

	public SJSocketCreate SJSocketCreate(Position pos, List arguments)
	{
		CanonicalTypeNode target = CanonicalTypeNode(pos, SJ_SOCKET_INTERFACE_TYPE);
		Id name = Id(pos, SJ_SOCKET_CREATE);

        //return (SJSocketCreate) n.ext(extFactory().SJCreateOperationExt());
		
		return new SJSocketCreate_c(pos, target, name, arguments);
	}	
	
	public SJServerCreate SJServerCreate(Position pos, List arguments)
	{
		CanonicalTypeNode target = CanonicalTypeNode(pos, SJ_SERVER_TYPE);
		Id name = Id(pos, SJ_SERVER_CREATE);

        //return (SJServerCreate) n.ext(extFactory().SJCreateOperationExt());
		
		return new SJServerCreate_c(pos, target, name, arguments);
	}
	
	public SJSelectorCreate SJSelectorCreate(Position pos, List arguments)
	{
		CanonicalTypeNode target = CanonicalTypeNode(pos, SJ_RUNTIME_TYPE);
		Id name = Id(pos, SJ_SELECTOR_SELECTORFOR);
		
		return new SJSelectorCreate_c(pos, target, name, arguments);
	}	
	
	public SJLocalChannel SJLocalChannel(Position pos, Id name)
	{
        return new SJLocalChannel_c(pos, name);
	}
	
	public SJLocalSocket SJLocalSocket(Position pos, Id name)
	{
        return new SJLocalSocket_c(pos, name);
	}

	public SJLocalServer SJLocalServer(Position pos, Id name)
	{
        return new SJLocalServer_c(pos, name);
	}

	public SJLocalSelector SJLocalSelector(Position pos, Id name)
	{
		return new SJLocalSelector_c(pos, name);
	}	
	
	public SJAmbiguousTry SJAmbiguousTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
        return new SJAmbiguousTry_c(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}
	
	public SJSessionTry SJSessionTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
        return new SJSessionTry_c(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}
	
	public SJServerTry SJServerTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
        return new SJServerTry_c(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}

	public SJSelectorTry SJSelectorTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		return new SJSelectorTry_c(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}
	
	public SJRequest SJRequest(Position pos, Receiver target, List arguments)
	{	
		Id name = Id(pos, SJ_CHANNEL_REQUEST);

        return new SJRequest_c(pos, target, name, arguments);
	}
	
	public SJSend SJSend(Position pos, List arguments, List targets)
	{	 
        // FIXME: the name shouldn't associated with the socket but rather the runtime.
		return new SJSend_c(pos, this, arguments, targets);
	}
	
	public SJPass SJPass(Position pos, List arguments, List targets)
	{	
		return new SJPass_c(pos, this, SJ_SOCKET_PASS, arguments, targets);
	}
	
	public SJCopy SJCopy(Position pos, List arguments, List targets)
	{	
		return new SJCopy_c(pos, this, arguments, targets);
	}
	
	// This is called by SJSessionOperationParser, not the parser, so the target has already been disambiguated and the ArrayInit could be created directly here.
	public SJReceive SJReceive(Position pos, List<Expr> arguments, List targets)
	{	
		return new SJReceive_c(pos, this, SJ_SOCKET_RECEIVE, arguments, targets);
	}	

	public SJReceive SJReceiveInt(Position pos, List<Expr> arguments, List targets)
    // FIXME: a bit hacky when it comes to type building - need to explicitly distinguish primitive
    // and object type usage of the single SJReceive node class. 
	{	
		return new SJReceive_c(pos, this, SJ_SOCKET_RECEIVEINT, arguments, targets);
	}	
	
	public SJReceive SJReceiveBoolean(Position pos, List<Expr> arguments, List targets)
	{	
		return new SJReceive_c(pos, this, SJ_SOCKET_RECEIVEBOOLEAN, arguments, targets);
	}
	
	public SJReceive SJReceiveDouble(Position pos, List<Expr> arguments, List targets)
	{	
		return new SJReceive_c(pos, this, SJ_SOCKET_RECEIVEDOUBLE, arguments, targets);
	}
	
	public SJRecurse SJRecurse(Position pos, SJLabel lab, List targets)
	{
		return new SJRecurse_c(pos, this, SJCompilerUtils.asLinkedList(StringLit(pos, lab.labelValue())), targets, lab);
	}

	public SJSpawn SJSpawn(Position pos, New w, List targets)
	{	
		Id name = Id(pos, SJ_THREAD_SPAWN);						
				
		List arguments = new LinkedList();
		
		/*for (Iterator i = targets.iterator(); i.hasNext(); )
		{
			arguments.add(i.next());
		}*/

        //n = (SJSpawn) n.del(sjdf.SJSpawnDel()); // Had some type checking problems (because new arguments are inserted) due to a previously missing but needed barrier between SJThreadParsing (generate the target spawn method in the SJThread and build types for the class) and SJSessionOperationParsing (translate the spawn call and type check against the target method).
		
		return new SJSpawn_c(pos, w, name, arguments, targets);
	}

	// Can generalise this operation to support arbitrary objects as labels.
  public SJOutlabel SJOutlabel(Position pos, SJLabel lab, List targets)
	{	
		return new SJOutlabel_c(pos, this, SJCompilerUtils.asLinkedList(StringLit(pos, lab.labelValue())), targets);
	}
	
	public SJInlabel SJInlabel(Position pos, List arguments, List targets)
	{	
		return new SJInlabel_c(pos, this, arguments, targets);
	}
	
	public SJOutsync SJOutsync(Position pos, Expr condition, List targets)
	{	
		return new SJOutsync_c(this, pos, condition, targets);
	}

  /*public SJRecursionEnter SJRecursionEnter(Position pos, List targets)
	{	
		return new SJRecursionEnter_c(pos, this, SJ_SOCKET_RECURSIONENTER, targets);
	}*/
	
	public SJRecursionEnter SJRecursionEnter(Position pos, List args, List targets)
	{	
		return new SJRecursionEnter_c(pos, this, SJ_SOCKET_RECURSIONENTER, args, targets);
	}
	
	public SJRecursionExit SJRecursionExit(Position pos, List targets)
	{	
		return new SJRecursionExit_c(pos, this, SJ_SOCKET_RECURSIONEXIT, targets);
	}
		
	public SJOutbranch SJOutbranch(final Position pos, final List<Stmt> stmts, SJLabel lab, List<Receiver> targets)
	{
		final SJOutlabel os = SJOutlabel(pos, lab, targets);
		
		List<Stmt> stmtList = new LinkedList<Stmt>() {{
            add(Eval(pos, os));
            addAll(stmts);                    
        }};
		
		return new SJOutbranch_c(pos, stmtList, lab, targets);
	}
	
	/*public SJOutbranch SJOutbranch(final Position pos, final List<Stmt> stmts, SJLabel lab, List<Receiver> targets, boolean isDependentlyTyped)
	{
		//final SJOutlabel os = SJOutlabel(pos, lab, targets); // SJOutlabel is implicitly based on Strings as labels. For the dependently-typed branches, we need to do extra work in type-building, checking and translation.
		
		List<Stmt> stmtList = new LinkedList<Stmt>() {{
            //add(Eval(pos, os));
            addAll(stmts);                    
        }};
		
		return new SJOutbranch_c(pos, stmtList, lab, targets, isDependentlyTyped);
	}*/
	
	public SJInbranch SJInbranch(Position pos, List arguments, List<SJInbranchCase> branchCases, List targets)
	{
		SJInlabel il = SJInlabel(pos, arguments, targets);

        return new SJInbranch_c(pos, branchCases, il);
	}
	
	/*public SJInbranch SJInbranch(Position pos, List arguments, List<SJInbranchCase> branchCases, List targets, boolean isDependentlyTyped)
	{
		SJInlabel il = SJInlabel(pos, arguments, targets);

        return new SJInbranch_c(pos, branchCases, il, isDependentlyTyped);
	}*/
	
	public SJInbranchCase SJInbranchCase(Position pos, List stmts, SJLabel lab)
	{
        return new SJInbranchCase_c(pos, stmts, lab);
	}
	
	public SJOutwhile SJOutwhile(Position pos, Expr condition, Stmt body, List targets)
	{
		return new SJOutwhile_c(pos, condition, body, targets, false);
	}

    public SJOutwhile SJNewOutwhile(Position pos, Expr condition, Stmt body, List targets) {
        return new SJOutwhile_c(pos, condition, body, targets, true);
    }

        public SJOutInwhile SJOutInwhile(Position pos, Stmt body, List<Receiver> sources, List<Receiver> targets, Expr condition)
	{
        return new SJOutInwhile_c(pos, condition, body, targets, sources);
	}
	
    //<By MQ> added argument list to support target
        public SJInwhile SJInwhile(Position pos, Stmt body, List targets, List arguments)
	{
	    return new SJInwhile_c(pos, body, targets, arguments);
	}
    //</By MQ>

	public SJRecursion SJRecursion(final Position pos, Block body, final SJLabel lab, final List targets) // Inconvenient to ...
	{
		QQ qq = new QQ(extInfo, pos);

        List<Object> mapping = new LinkedList<Object>();

        String translation = "for ( ; new Boolean(false).booleanValue(); ) { }";
        // Dummy condition later replaced by SJCompoundOperationTranslator.
        // Used because we cannot give the intended loop-variable the correct name yet (targets are ambiguous).
		
		For f = (For) qq.parseStmt(translation, mapping.toArray());
		
		List args = new LinkedList()
		{{
			add(StringLit(pos, lab.labelValue()));
		}};
		
		//SJRecursionEnter re = SJRecursionEnter(pos, targets);
		SJRecursionEnter re = SJRecursionEnter(pos, args, targets); // Extended recursion-enter to take the recursion label as an argument. We could have done this in the translation phase, but unlike recursion-exit, recursion-enter seems to be handled here.
		
		translation = "%E;";
		mapping.add(re);
		
		Eval e = (Eval) qq.parseStmt(translation, mapping.toArray());
		
		body = body.prepend(e);

        return new SJRecursion_c(pos, f.inits(), f.cond(), body, lab, targets);
	}

    public SJTypecase SJTypecase(Position pos, Name socket, List<SJWhen> cases) {
        return new SJTypecase_c(pos, socket.toReceiver(), cases, null);
    }

    public SJWhen SJWhen(Position pos, SJTypeNode type, Stmt body) {
        List<Stmt> statements = new LinkedList<Stmt>();
        if (body instanceof Block) {
            statements.addAll(((Block) body).statements());
        } else {
            statements.add(body);
        }
        return new SJWhen_c(pos, type, statements);
    }

    public SJAccept SJAccept(Position pos, Receiver target, List arguments)
	{	
		Id name = Id(pos, SJ_SERVER_ACCEPT);

        return new SJAccept_c(pos, target, name, arguments);
	}	
    
  public SJRegisterAccept SJRegisterAccept(Position pos, Receiver target, List arguments)
  {
  	Id name = Id(pos, SJ_SELECTOR_REGISTERACCEPT);
  	
  	return new SJRegisterAccept_c(pos, target, name, arguments);
  }
  
  public SJRegisterOutput SJRegisterOutput(Position pos, Receiver target, List arguments)
  {
  	Id name = Id(pos, SJ_SELECTOR_REGISTEROUTPUT);
  	
  	return new SJRegisterOutput_c(pos, target, name, arguments);
  }
  
  public SJRegisterInput SJRegisterInput(Position pos, Receiver target, List arguments)
  {
  	Id name = Id(pos, SJ_SELECTOR_REGISTERINPUT);
  	
  	return new SJRegisterInput_c(pos, target, name, arguments);
  }  

  public SJSelectSession SJSelectSession(Position pos, Receiver target, List arguments)
  {
  	Id name = Id(pos, SJ_SELECTOR_SELECTSESSION);
  	
  	return new SJSelectSession_c(pos, target, name, arguments);
  }  
  
	public SJChannelCast SJChannelCast(Position pos, Expr expr, SJTypeNode tn)
	{

        return new SJChannelCast_c(pos, CanonicalTypeNode(pos, SJ_CHANNEL_TYPE), expr, tn);
	}
	
	public SJSessionCast SJSessionCast(Position pos, Expr expr, SJTypeNode tn)
	{

        return new SJSessionCast_c(pos, CanonicalTypeNode(pos, SJ_SOCKET_INTERFACE_TYPE), expr, tn);
	}
	
	public SJAmbiguousCast SJAmbiguousCast(Position pos, Expr expr, SJTypeNode tn)
	{

        return new SJAmbiguousCast_c(pos, CanonicalTypeNode(pos, SJ_CHANNEL_SOCKET_HACK_TYPE), expr, tn);
	}
	
	public SJFormal SJChannelFormal(Position pos, Flags flags, Id name, SJTypeNode tn) // Based on SJSessionFormal.
	{
		SJFormal n = new SJFormal_c(this, SJ_CHANNEL_TYPE, pos, flags, name, tn);

		n = (SJFormal) n.type(convertToNoAliasTypeNode(n.type(), flags.isFinal()));
		return n;
	}
	
	public SJFormal SJSessionFormal(Position pos, Flags flags, Id name, SJTypeNode tn)
    // Based on SJProtocolDecl.
    // // The choice is between modifying the base types to signal noalias, or make a separate
    // (sub)class for noalias session formals. Going with the former, as for SJProtocolDecls.
	{
		SJFormal n = new SJFormal_c(this, SJ_SOCKET_INTERFACE_TYPE, pos, flags, name, tn);
		
		n = (SJFormal) n.type(convertToNoAliasTypeNode(n.type(), flags.isFinal()));
		
		return n;
	}

    public SJFormal SJServerFormal(Position pos, Flags flags, Id name, SJTypeNode tn)
       // Based on SJProtocolDecl.
       // // The choice is between modifying the base types to signal noalias, or make a separate
       // (sub)class for noalias session formals. Going with the former, as for SJProtocolDecls.
       {
           SJFormal n = new SJFormal_c(this, SJ_SERVER_INTERFACE_TYPE, pos, flags, name, tn);

           n = (SJFormal) n.type(convertToNoAliasTypeNode(n.type(), flags.isFinal()));

           return n;
	   }

	private TypeNode convertToNoAliasTypeNode(TypeNode tn, boolean isFinal)
	{		
		if (tn instanceof AmbTypeNode)
		{
			tn = SJAmbNoAliasTypeNode(tn.position(), (AmbTypeNode) tn);
		}
		else if (tn instanceof ArrayTypeNode) 
		{
			tn = SJNoAliasArrayTypeNode(tn.position(), (ArrayTypeNode) tn);
		}
		/*else
		{
			// CanonicalTypeNode won't be replaced by disambigation pass (so extension object won't be lost).
		}*/
		
		return (TypeNode) setSJNoAliasFinalExt(sjef, tn, true, isFinal); // Protocols are na-final.
	}
	
	/*private Flags makeFinal(Flags flags)
	{
		return (!flags.isFinal()) ? flags.Final() : flags;
	}*/
    public NewArray makeSocketsArray(Position pos, int size)
    {
        CanonicalTypeNode base = CanonicalTypeNode(pos, SJ_SOCKET_INTERFACE_TYPE);

        List<Expr> dims = new LinkedList<Expr>();
        dims.add(IntLit(pos, IntLit.INT, size));

        return NewArray(pos, base, dims, 0, null);
    }
}
