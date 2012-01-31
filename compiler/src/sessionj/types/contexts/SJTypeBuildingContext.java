/**
 * 
 */
package sessionj.types.contexts;

import polyglot.types.SemanticException;

import java.util.List;

/**
 * @author Raymond
 *
 */
public interface SJTypeBuildingContext extends SJContextInterface 
{		
	void recurseSessions(List<String> sjnames) throws SemanticException;
	
	void pushContextElement(SJContextElement ce);
	SJContextElement popContextElement() throws SemanticException;
	
	void checkSessionsCompleted() throws SemanticException;
	
	boolean inSJBranchCaseContext();
	boolean inSJSessionLoopContext();
}
