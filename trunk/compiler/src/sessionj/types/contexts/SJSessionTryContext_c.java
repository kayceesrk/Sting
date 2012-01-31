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
public class SJSessionTryContext_c extends SJTryContext_c implements SJSessionTryContext
{
	private List<String> sjnames;
	
	public SJSessionTryContext_c()
	{
		
	}
	
	public SJSessionTryContext_c(SJContextElement ce)
	{
		super(ce);
	}
	public List<String> getSessions()
	{
		return sjnames;
	}
	
	public void setSessions(List<String> sjnames)
	{
		this.sjnames = sjnames;
	}
}
