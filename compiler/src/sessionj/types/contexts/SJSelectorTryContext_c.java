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
public class SJSelectorTryContext_c extends SJTryContext_c implements SJSelectorTryContext
{
	private List<String> sjnames;
	
	public SJSelectorTryContext_c()
	{
		
	}
	
	public SJSelectorTryContext_c(SJContextElement ce)
	{
		super(ce);
	}
	public List<String> getSelectors()
	{
		return sjnames;
	}
	
	public void setSelectors(List<String> sjnames)
	{
		this.sjnames = sjnames;
	}
}
