/**
 * 
 */
package sessionj.types.typeobjects;

import sessionj.types.sesstypes.*;

/**
 * @author Raymond
 * 
 */
public class SJLocalSelectorInstance_c extends SJNamedLocalInstance_c implements
    SJLocalSelectorInstance
{
	/**
	 * 
	 */
	public SJLocalSelectorInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalSelectorInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
