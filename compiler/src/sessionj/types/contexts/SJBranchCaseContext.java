/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import sessionj.util.SJLabel;

/**
 * 
 * @author Raymond
 *
 * Maybe should be renamed SJSessionBranchCaseContext. // But is it really a session context? Doesn't inherit from SJSessionContext.
 *
 */
public interface SJBranchCaseContext extends SJContextElement
{
	public SJLabel label();
	
	public void addTerminal(String sjname); // Bit of a work around to handle delegation within branches (currently only allowed within terminal branches).
	public boolean isTerminal(String sjname);
}
