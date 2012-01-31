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
 */
public interface SJServerTryContext extends SJTryContext
{
	public List<String> getServers(); // Although currently restricted to a single session server.	
	public void setServers(List<String> sjnames);	
}
