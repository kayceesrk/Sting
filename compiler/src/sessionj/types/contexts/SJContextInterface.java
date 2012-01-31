/**
 * 
 */
package sessionj.types.contexts;

import polyglot.ast.MethodDecl;
import polyglot.types.SemanticException;
import polyglot.visit.ContextVisitor;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.SJSelectorTry;
import sessionj.ast.sesstry.SJServerTry;
import sessionj.ast.sesstry.SJSessionTry;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.SJNamedInstance;

/**
 * @author Raymond
 *
 */
public interface SJContextInterface // For SJContext (not SJContextElement).
{	
	void setVisitor(ContextVisitor cv);
	
	SJSessionType findProtocol(String sjname) throws SemanticException;
	SJSessionType findChannel(String sjname) throws SemanticException;
	SJSessionType findSocket(String sjname) throws SemanticException;
	SJSessionType findServer(String sjname) throws SemanticException;
	SJSessionType findSelector(String sjname) throws SemanticException;
	
	void addChannel(SJNamedInstance ni); // Can specify more the specific instance objects.
	void addSocket(SJNamedInstance ni);
	void addServer(SJNamedInstance ni); 
	void addSession(SJNamedInstance ni);
	void addSelector(SJNamedInstance ni);
	
	void openService(String sjname, SJSessionType st) throws SemanticException; // Services are open (activated/listening/...) servers. Not to be confused with SJServices (shared channels).
	void openSession(String sjname, SJSessionType st) throws SemanticException;
	//void openSelector(String sjname, SJSessionType st) throws SemanticException; // Selectors are "open" from creation (at variable initialisation) but cannot be used until inside a try. Unlike servers which cannot be opened until inside a try. 
	
	void advanceSession(String sjname, SJSessionType st) throws SemanticException;
	SJSessionType delegateSession(String sjname) throws SemanticException; // Maybe this operation should take the type as an argument instead of calculating it itself (which should be done by the equivalent in SJTypeBuildingContext).
	
	SJSessionType expectedSessionOperation(String sjname); // Gives the active type for the current context (not just the next operation, but not the full remaining type either).
	SJSessionType sessionImplemented(String sjname);
	SJSessionType sessionRemaining(String sjname) throws SemanticException;
	
	boolean serviceInScope(String sjname);    
	boolean serviceOpen(String sjname);
	boolean sessionInScope(String sjname);
	boolean sessionActive(String sjname);	
	boolean selectorInScope(String sjname);
	
	//void pushCode();
    void pushBlock();
	void pushTry();
	void pushBranch();
	void pushLoop();
	//void pushTry();
    void pushMethodBody(MethodDecl md) throws SemanticException;
	
	void pushSJSessionTry(SJSessionTry st) throws SemanticException;
	void pushSJServerTry(SJServerTry st) throws SemanticException;
	void pushSJSelectorTry(SJSelectorTry st) throws SemanticException;
	
	void pushSJBranchOperation(SJBranchOperation b) throws SemanticException;
	void pushSJBranchCase(SJBranchCase bc) throws SemanticException;
	void pushSJWhile(SJWhile w) throws SemanticException;
	void pushSJRecursion(SJRecursion r) throws SemanticException;

    void pushSJTypecase(SJTypecase typecase) throws SemanticException;
    void pushSJWhen(SJWhen when) throws SemanticException;
	
	SJContextElement pop() throws SemanticException;
	
	void setSessionRequested(String sjname, SJSessionType st);
	void setSessionActive(String sjname, SJSessionType st); // public so that noalias session method parameters can be initialised.
	void setSessionImplemented(String sjname, SJSessionType st);
	
	SJNamedInstance getChannel(String sjname) throws SemanticException;
	SJNamedInstance getSocket(String sjname) throws SemanticException;
	SJNamedInstance getServer(String sjname) throws SemanticException;
	SJNamedInstance getSelector(String sjname) throws SemanticException;
	
    SJContextElement currentContext();
}
