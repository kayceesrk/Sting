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
public class SJFieldProtocolInstance_c extends SJNamedFieldInstance_c implements
    SJFieldProtocolInstance
{
	/**
	 * 
	 */
	public SJFieldProtocolInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJFieldProtocolInstance_c(SJFieldInstance fi, SJSessionType st, String sjname)
	{
		super(fi, st, sjname);
	}
}
