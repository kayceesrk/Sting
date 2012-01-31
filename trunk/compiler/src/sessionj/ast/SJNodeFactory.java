package sessionj.ast;

import static sessionj.SJConstants.SJ_SELECTOR_REGISTERACCEPT;
import static sessionj.SJConstants.SJ_SELECTOR_REGISTERINPUT;
import polyglot.ast.*;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.Flags;
import polyglot.util.Position;
import polyglot.parse.Name;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.createops.SJChannelCreate;
import sessionj.ast.createops.SJSelectorCreate;
import sessionj.ast.createops.SJServerCreate;
import sessionj.ast.createops.SJSocketCreate;
import sessionj.ast.noalias.SJAmbNoAliasTypeNode;
import sessionj.ast.noalias.SJNoAliasArrayTypeNode;
import sessionj.ast.noalias.SJNoAliasCanonicalTypeNode;
import sessionj.ast.protocoldecls.SJFieldProtocolDecl;
import sessionj.ast.protocoldecls.SJLocalProtocolDecl;
//<By MQ> For supporting gprotocol declarations
import sessionj.ast.protocoldecls.SJFieldGProtocolDecl;
import sessionj.ast.protocoldecls.SJLocalGProtocolDecl;
//</by MQ>
import sessionj.ast.selectorops.*;
import sessionj.ast.servops.SJAccept;
import sessionj.ast.sesscasts.SJAmbiguousCast;
import sessionj.ast.sesscasts.SJChannelCast;
import sessionj.ast.sesscasts.SJSessionCast;
import sessionj.ast.sessformals.SJFormal;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.*;
import sessionj.ast.sessvars.*;
import sessionj.ast.typenodes.*;
import sessionj.extension.SJExtFactory;
import sessionj.util.SJLabel;

import java.util.List;

/**
 * NodeFactory for sessionj extension.
 */
public interface SJNodeFactory extends NodeFactory 
{
    // TODO: Declare any factory methods for new AST nodes.
	
	void setExtensionInfo(ExtensionInfo extInfo); // For QQ.
	
	SJExtFactory extFactory();
	//public SJDelFactory delFactory();

	SJAmbNoAliasTypeNode SJAmbNoAliasTypeNode(Position pos, AmbTypeNode atn);
	SJNoAliasArrayTypeNode SJNoAliasArrayTypeNode(Position pos, ArrayTypeNode atn);
	SJNoAliasCanonicalTypeNode SJNoAliasCanonicalTypeNode(Position pos, CanonicalTypeNode ctn);
	
	SJFieldProtocolDecl SJFieldProtocolDecl(Position pos, Flags flags, Id name, SJTypeNode tn);
	SJLocalProtocolDecl SJLocalProtocolDecl(Position pos, Id name, SJTypeNode tn);
    //<By MQ> For supporting gprotocol declaration
        SJFieldProtocolDecl SJFieldGProtocolDecl(Position pos, Flags flags, Id name, SJTypeNode tn);
	SJLocalProtocolDecl SJLocalGProtocolDecl(Position pos, Id name, SJTypeNode tn);
	SJGProtocolRefNode SJGProtocolRefNode(Position pos, Receiver target);
	SJGProtocolDualNode SJGProtocolDualNode(Position pos, Receiver target);
        SJParticipantsNode SJParticipantsNode(Position pos, List<Id> IDs);  //For supporting the participants keyword
        SJMessageCommunicationNode SJGMsgNode(Position pos, Id sender, Id receiver, TypeNode/*List*/ messageType);
	SJBranchNode SJGBranchNode(Position pos, Id sender, List<SJBranchCaseNode> branchCases);
        SJLoopNode SJGLoopNode(Position pos, Id sender, SJTypeNode body);
        SJBeginNode SJGBeginNode(Position pos, Id sender);
        SJTypeNode SJParticipant(Position pos, Id p);
        SJTypeNode optimizeProtocol(SJTypeNode tn); //For optimizations
    //</By MQ>
	SJCBeginNode SJCBeginNode(Position pos);
        SJSBeginNode SJSBeginNode(Position pos, Id target); //<By MQ> Added Id
    //<By MQ> Change TypeNode to List for the second argument of both SJSendNode and SJReceiveNode.
        SJSendNode SJSendNode(Position pos, Id target, TypeNode/*List*/ messageType);
        SJReceiveNode SJReceiveNode(Position pos, Id target, TypeNode/*List*/ messageType);
    //</By MQ>
	SJOutbranchNode SJOutbranchNode(Position pos, List<SJBranchCaseNode> branchCases);
        SJInbranchNode SJInbranchNode(Position pos, Id target, List<SJBranchCaseNode> branchCases);  //<By MQ>
	SJBranchCaseNode SJBranchCaseNode(Position pos, SJLabel lab, SJTypeNode body);
	SJOutwhileNode SJOutwhileNode(Position pos, SJTypeNode body);
        SJInwhileNode SJInwhileNode(Position pos, Id target, SJTypeNode body); //<By MQ>
	SJRecursionNode SJRecursionNode(Position pos, SJLabel lab, SJTypeNode body);
	SJRecurseNode SJRecurseNode(Position pos, SJLabel lab);
	SJProtocolRefNode SJProtocolRefNode(Position pos, Receiver target);
	SJProtocolDualNode SJProtocolDualNode(Position pos, Receiver target);
	
	/*SJOutbranchNode SJOutbranchNode(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped);
	SJInbranchNode SJInbranchNode(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped);*/
	
	SJChannelCreate SJChannelCreate(Position pos, List arguments);
	SJSocketCreate SJSocketCreate(Position pos, List arguments);
	SJServerCreate SJServerCreate(Position pos, List arguments);
	SJSelectorCreate SJSelectorCreate(Position pos, List arguments);
	
	SJLocalChannel SJLocalChannel(Position pos, Id name);
	SJLocalSocket SJLocalSocket(Position pos, Id name);
	SJLocalServer SJLocalServer(Position pos, Id name);
	SJLocalSelector SJLocalSelector(Position pos, Id name);
	
	SJAmbiguousTry SJAmbiguousTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets);
	SJSessionTry SJSessionTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets);
	SJServerTry SJServerTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets);
	SJSelectorTry SJSelectorTry(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets);
	
	SJRequest SJRequest(Position pos, Receiver target, List arguments);
	SJSend SJSend(Position pos, List arguments, List targets);
	SJPass SJPass(Position pos, List arguments, List targets);
	SJCopy SJCopy(Position pos, List arguments, List targets);
	SJReceive SJReceive(Position pos, List<Expr> arguments, List targets);
	SJReceive SJReceiveInt(Position pos, List<Expr> arguments, List targets);
	SJReceive SJReceiveBoolean(Position pos, List<Expr> arguments, List targets);
	SJReceive SJReceiveDouble(Position pos, List<Expr> arguments, List targets);
	SJRecurse SJRecurse(Position pos, SJLabel lab, List targets);
	
	SJSpawn SJSpawn(Position pos, New w, List targets);
	
	SJOutlabel SJOutlabel(Position pos, SJLabel lab, List targets);
	SJInlabel SJInlabel(Position pos, List arguments, List targets);
	//SJRecursionEnter SJRecursionEnter(Position pos, List targets);
	SJRecursionEnter SJRecursionEnter(Position pos, List args, List targets);
	SJRecursionExit SJRecursionExit(Position pos, List targets);
	
	SJOutbranch SJOutbranch(Position pos, List<Stmt> stmts, SJLabel lab, List<Receiver> targets);	
	SJInbranch SJInbranch(Position pos, List arguments, List<SJInbranchCase> branchCases, List targets);	
	SJInbranchCase SJInbranchCase(Position pos, List stmts, SJLabel lab);
	SJOutwhile SJOutwhile(Position pos, Expr condition, Stmt body, List targets);
    SJOutwhile SJNewOutwhile(Position pos, Expr condition, Stmt body, List targets);
	SJOutInwhile SJOutInwhile(Position pos, Stmt body, List<Receiver> sources, List<Receiver> targets, Expr condition);
        SJInwhile SJInwhile(Position pos, Stmt body, List targets, List arguments);  //<By MQ> Added arguments
	//SJRecursion SJRecursion(SJTypeSystem ts, Position pos, List stmts, SJLabel lab, List targets);
	SJRecursion SJRecursion(Position pos, Block body, SJLabel lab, List targets);

	/*SJOutbranch SJOutbranch(Position pos, List<Stmt> stmts, SJLabel lab, List<Receiver> targets, boolean isDependentlyTyped);
	SJInbranch SJInbranch(Position pos, List arguments, List<SJInbranchCase> branchCases, List targets, boolean isDependentlyTyped);*/
	
    SJTypecase SJTypecase(Position pos, Name socket, List<SJWhen> cases);
    SJWhen SJWhen(Position pos, SJTypeNode type, Stmt body);
	
	SJAccept SJAccept(Position pos, Receiver target, List arguments);
	
  SJRegisterAccept SJRegisterAccept(Position pos, Receiver target, List arguments);
  SJRegisterOutput SJRegisterOutput(Position pos, Receiver target, List arguments);
  SJRegisterInput SJRegisterInput(Position pos, Receiver target, List arguments);     
  SJSelectSession SJSelectSession(Position pos, Receiver target, List arguments);
	
	SJChannelCast SJChannelCast(Position pos, Expr expr, SJTypeNode tn);
	SJSessionCast SJSessionCast(Position pos, Expr expr, SJTypeNode tn);
	SJAmbiguousCast SJAmbiguousCast(Position pos, Expr expr, SJTypeNode tn);
	
	SJFormal SJChannelFormal(Position pos, Flags flags, Id name, SJTypeNode tn);
	SJFormal SJSessionFormal(Position pos, Flags flags, Id name, SJTypeNode tn);
    SJFormal SJServerFormal(Position pos, Flags flags, Id name, SJTypeNode tn);

    NewArray makeSocketsArray(Position pos, int size);
}
