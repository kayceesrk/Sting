/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.*;
import polyglot.util.Position;

import static sessionj.SJConstants.*;

/**
 * @author Raymond
 * 
 */
public class SJFieldInstance_c extends FieldInstance_c implements
    SJFieldInstance
{
	private boolean isNoAlias;
	private boolean isFinal;
	
	/**
	 * 
	 */
	public SJFieldInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJFieldInstance_c(FieldInstance fi, boolean isNoAlias, boolean isFinal)
	{
		super(fi.typeSystem(), fi.position(), fi.container(), fi.flags(), fi.type(), fi.name());
		
		this.isNoAlias = isNoAlias;
		this.isFinal = isFinal;
	}
	
	public boolean isNoAlias()
	{
		return isNoAlias;
	}
	
	public boolean isFinal()
	{
		return isFinal;
	}	
	
	public boolean equalsImpl(TypeObject o)
	{
		return o instanceof SJFieldInstance && super.equalsImpl(o);
	}
	
	public String toString()
	{
		String m = super.toString();
		
		if (isNoAlias())
		{
			m = SJ_KEYWORD_NOALIAS + " " + m; 
		}
		
		return m;
	}
}
