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
public class SJLocalChannelInstance_c extends SJNamedLocalInstance_c implements
    SJLocalChannelInstance
{
	/**
	 * 
	 */
	public SJLocalChannelInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalChannelInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
