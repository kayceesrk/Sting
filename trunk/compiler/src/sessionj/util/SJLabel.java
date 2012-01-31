package sessionj.util;

import java.io.Serializable;

import polyglot.ast.Id;
import sessionj.SJConstants;

import static sessionj.SJConstants.*;

/*
 *  Immutable.
 *  
 *  FIXME: This class has been modified to be backwards-compatible with existing SJ (compiler) implementation, i.e. labels implicitly treated as Strings, but extended to support general objects as labels. But at some point, the latter should totally subsume the former.
 *  
 */
public final class SJLabel implements Cloneable, Serializable // Serializable hack - fix translation.
{
	public static final long serialVersionUID = SJ_VERSION;	
	
	//private final String lab;
	private final Object lab; // Can be any general object, but we make use of hashCode.
    private final int hashCode;

    //private final Id id;
	
	//public SJLabel(String lab)
	public SJLabel(Object lab)
	{
		if (lab instanceof String) // Hack for branch labels that start with a number. This would less hacky if it was built into the grammar. But eventually, "dependently-typed" branches will subsume this.
		{
			String m = (String) lab;
			
			if (m.startsWith(SJConstants.NUMERIC_LABEL_PREFIX_HACK)) 
			{
				lab = m.substring(SJConstants.NUMERIC_LABEL_PREFIX_HACK.length());
			}
		}
		
		this.lab = lab;		
        hashCode = lab.hashCode();
		//this.id = null;
	}

	/*public SJLabel(Id id)
	{
		this.lab = null;
		this.id = id;
	}*/
	
	//@Deprecated
	public final String labelValue() // A legacy method: SJ labels were implicitly treated as Strings throughout the compiler and runtime. 
	{
		return (String) dependentLabelValue();
	}
	
	public final Object dependentLabelValue()  
	{
		return lab;
	}

	public final String toString()
	{
		//return labelValue();
		return dependentLabelValue().toString(); // Backwards compatible with (implicit use of) Strings as labels.
	}

	public final boolean equals(Object obj)
	{
		if (obj instanceof SJLabel)
		{
			//return labelValue().equals(((SJLabel) obj).labelValue());
			return dependentLabelValue().equals(((SJLabel) obj).dependentLabelValue());
		}
		else
		{
			return false;
		}
	}

	public final int hashCode()
	{
		//return labelValue().hashCode();
		return hashCode;
	}

	public final SJLabel clone()
	{
		return new SJLabel(lab); // String is immutable, so shallow clone is enough.
	}
	
	/*public final Id getId()
	{
		return id;
	}*/
}
