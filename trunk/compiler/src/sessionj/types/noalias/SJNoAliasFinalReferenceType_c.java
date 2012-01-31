/**
 * 
 */
package sessionj.types.noalias;

import java.util.*;

import polyglot.types.*;
import polyglot.util.*;

import sessionj.types.*;

import static sessionj.SJConstants.*;

/**
 * 
 * @author Raymond
 * 
 */
public class SJNoAliasFinalReferenceType_c extends SJNoAliasReferenceType_c implements
    SJNoAliasFinalReferenceType
{
	private boolean isFinal;

	public SJNoAliasFinalReferenceType_c()
	{

	}
	
	public SJNoAliasFinalReferenceType_c(ReferenceType rt, boolean isFinal)
	{
		super(rt); 
		
		this.isFinal = isFinal;
	}
	
	public boolean isFinal()
	{
		return isFinal;
	}
	
	// Following are wrapper methods customised to support noalias.
	public boolean descendsFromImpl(Type t)
	{
		return (t instanceof SJNoAliasFinalReferenceType)
		    && type().descendsFrom(((SJNoAliasFinalReferenceType) t).type());
	}

	public boolean typeEqualsImpl(Type t)
	{
		return (t instanceof SJNoAliasFinalReferenceType)
		    && type().typeEquals(((SJNoAliasFinalReferenceType) t).type());
	}

	public Type superType()
	{
		return sjts.SJNoAliasFinalReferenceType((ReferenceType) type().superType(), isFinal());
	}

	public SJNoAliasReferenceType copy()
	{
		return sjts.SJNoAliasFinalReferenceType((ReferenceType) type().copy(), isFinal());
	}
	
	public boolean equalsImpl(TypeObject t)
	{
		return (t instanceof SJNoAliasFinalReferenceType) && typeEquals((SJNoAliasFinalReferenceType) t);
	}	
	
	public String toString()
	{
		String m = SJ_KEYWORD_NOALIAS + " ";
		
		if (isFinal())
		{
			m += "final ";
		}
		
		return m + type().toString();
	}	
}
