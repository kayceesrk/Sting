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
public class SJLocalProtocolInstance_c extends SJNamedLocalInstance_c implements
    SJLocalProtocolInstance
{
	/**
	 * 
	 */
	public SJLocalProtocolInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalProtocolInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
