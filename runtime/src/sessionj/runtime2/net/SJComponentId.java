/**
 *
 */
package sessionj.runtime2.net;

public class SJComponentId 
{
	private final String id; 
	
	public SJComponentId(String id) 
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}
	
	public String toString()
	{
		return getId().toString();
	}

	public final boolean equals(Object obj)
	{
		if (obj instanceof SJComponentId)
		{
			return getId().equals(((SJComponentId) obj).getId());
		}
		else
		{
			return false;
		}
	}

	public final int hashCode()
	{
		return getId().hashCode();
	}

	public final SJComponentId clone() throws CloneNotSupportedException
	{
		 throw new CloneNotSupportedException("[SJComponentId] SJComponentIds are not cloneable: " + getId());
	}
}
