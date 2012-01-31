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
public class SJNamedFieldInstance_c extends SJFieldInstance_c implements
    SJNamedFieldInstance
{
	private SJSessionType st;
	private String sjname;
	
	/**
	 * 
	 */
	public SJNamedFieldInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJNamedFieldInstance_c(SJFieldInstance fi, SJSessionType st, String sjname)
	{
		super(fi, fi.isNoAlias(), fi.isFinal());
		
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
