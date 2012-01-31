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
 * FIXME: should be made a SJSessionContext. And maybe make a SJTryContext to share with SJServerTry.
 *
 */
public interface SJSessionTryContext extends SJTryContext
{
	public List<String> getSessions();	
	public void setSessions(List<String> sjnames);	
}
