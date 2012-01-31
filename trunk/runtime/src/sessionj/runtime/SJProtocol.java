package sessionj.runtime;

import java.io.Serializable;

import sessionj.runtime.net.SJRuntime;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJSessionType;

public class SJProtocol implements Serializable
{
	public static final long serialVersionUID = SJ_VERSION;

    private final String encoded; //<By MQ>
    private transient SJSessionType canonicalForm; //<By MQ>

    public SJProtocol(String encoded) {
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

    //<By MQ> Because transferring canonical form causes errors in the serialization process, we need to serialize the string and then rebuild it on the target machine, in case of continuation exporting
    public void rebuildProtocol()
    {
	canonicalForm = SJRuntime.decodeType(encoded).getCanonicalForm();
    }
    //</By MQ>
}
