/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.*;
import polyglot.util.Position;

import sessionj.types.sesstypes.*;

import static sessionj.SJConstants.*;

/**
 * @author Raymond
 * 
 */
public class SJNamedLocalInstance_c extends SJLocalInstance_c implements
    SJNamedLocalInstance
{
	private SJSessionType st; // All SJTypeableInstances duplicate this.
	private String sjname;
	
	/**
	 * 
	 */
	public SJNamedLocalInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJNamedLocalInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, li.isNoAlias(), li.isFinal());
		
		this.st = st;
		this.sjname = sjname;
	}

	public SJSessionType sessionType()
	{
		return st;
	}
	
	public String sjname()
	{
		return sjname;
	}
}
