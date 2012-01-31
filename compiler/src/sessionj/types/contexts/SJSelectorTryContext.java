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
 * FIXME: should be made a SJSessionContext.
 * 
 * Duplicated from SJServerTryContext.
 *
 */
public interface SJSelectorTryContext extends SJTryContext
{
	public List<String> getSelectors(); // Although currently restricted to a single session selector.	
	public void setSelectors(List<String> sjnames);	
}
