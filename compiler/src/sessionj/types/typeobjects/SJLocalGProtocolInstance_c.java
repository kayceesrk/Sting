//<By MQ> Added
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
public class SJLocalGProtocolInstance_c extends SJNamedLocalInstance_c implements
    SJLocalGProtocolInstance
{
	/**
	 * 
	 */
	public SJLocalGProtocolInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalGProtocolInstance_c(SJLocalInstance li, SJSessionType st, String sjname)
	{
		super(li, st, sjname);
	}
}
//</By MQ>