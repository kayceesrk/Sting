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
public class SJServerTryContext_c extends SJTryContext_c implements SJServerTryContext
{
	private List<String> sjnames;
	
	public SJServerTryContext_c()
	{
		
	}
	
	public SJServerTryContext_c(SJContextElement ce)
	{
		super(ce);
	}
	public List<String> getServers()
	{
		return sjnames;
	}
	
	public void setServers(List<String> sjnames)
	{
		this.sjnames = sjnames;
	}
}
