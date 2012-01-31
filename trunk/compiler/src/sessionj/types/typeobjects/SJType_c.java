/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.*;
import sessionj.types.SJTypeSystem;

/**
 * @author Raymond
 *
 */
public abstract class SJType_c extends Type_c implements SJType
{
	protected SJType_c()
	{		
	}
	
	protected SJType_c(TypeSystem ts)
	{
		super(ts);
	}	
	
	public SJTypeSystem typeSystem() 
	{
		return (SJTypeSystem) super.typeSystem();
	}	
}
