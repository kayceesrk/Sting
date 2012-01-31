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
public class SJLocalInstance_c extends LocalInstance_c implements
    SJLocalInstance
{
	private boolean isNoAlias;
	private boolean isFinal;
	
	/**
	 * 
	 */
	public SJLocalInstance_c()
	{
		super();
	}

	/**
	 * 
	 */
	public SJLocalInstance_c(LocalInstance li, boolean isNoAlias, boolean isFinal)
	{
		super(li.typeSystem(), li.position(), li.flags(), li.type(), li.name());
		
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
		return o instanceof SJLocalInstance && super.equalsImpl(o);
	}
	
	public String toString()
	{
		String m = super.toString();
		
		if (isNoAlias)
		{
			m = SJ_KEYWORD_NOALIAS + " " + m;
		}
		
		return m;
	}
}
