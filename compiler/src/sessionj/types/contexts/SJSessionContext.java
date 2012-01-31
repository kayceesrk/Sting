/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.ast.sessops.compoundops.*;

/**
 * @author Raymond
 *
 */
public interface SJSessionContext extends SJContextElement
{
	public SJCompoundOperation node(); // The node (SJCompoundOperation) that caused this context to be entered.
	
	public List<String> targets();
}
