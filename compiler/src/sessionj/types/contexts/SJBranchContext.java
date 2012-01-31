/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 */
public interface SJBranchContext extends SJContextElement
{
	public void addBranch(SJContextElement ce);	
	public List<SJContextElement> branches();
	
	public boolean hasSessionImplementations(); // Sessions were modified within the branch cases. // Needs to be false by default. 
	public void setHasSessionImplementations(boolean hasSessionImplementations);
}
