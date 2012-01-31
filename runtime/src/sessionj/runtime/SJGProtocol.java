//<By MQ> //Added
package sessionj.runtime;

import java.io.Serializable;

import sessionj.runtime.net.SJRuntime;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJSessionType;

public class SJGProtocol implements Serializable
{
	public static final long serialVersionUID = SJ_VERSION;

	private final String encoded;
    private final SJSessionType canonicalForm;

    public SJGProtocol(String encoded) {
		this.encoded = encoded;
        canonicalForm = SJRuntime.decodeType(encoded).getCanonicalForm();
    }

	public String encoded()
	{
		return encoded; // Maybe should return the decoded type. 
	}

    public synchronized SJSessionType type() throws SJIOException 
    {
        return canonicalForm;
    }
	
	public String toString()
	{
		try
		{
			return SJRuntime.decodeType(encoded).toString();
		}
		catch (SJRuntimeException ioe)
		{
			return ioe.getMessage();
		}
	}
}
//</By MQ>