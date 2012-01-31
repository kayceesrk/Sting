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
public class SJLocalServerInstance_c extends SJNamedLocalInstance_c implements
    SJLocalServerInstance
{
	/**
	 * 
	 */
	public SJLocalServerInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalServerInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
