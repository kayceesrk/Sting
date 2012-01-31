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
public class SJLocalSocketInstance_c extends SJNamedLocalInstance_c implements
    SJLocalSocketInstance
{
	/**
	 * 
	 */
	public SJLocalSocketInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalSocketInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
