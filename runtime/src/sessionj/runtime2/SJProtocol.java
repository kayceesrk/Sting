package sessionj.runtime2;

import java.io.Serializable;

import sessionj.types.sesstypes.SJSessionType;

import sessionj.runtime2.net.SJRuntime;

import static sessionj.runtime2.SJRuntimeConstants.*;
import static sessionj.runtime2.net.SJRuntime.*;

public class SJProtocol implements Serializable
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;

	private final String encoded;
	private SJSessionType decoded = null;

	public SJProtocol(String encoded)
	{
		this.encoded = encoded;
	}

	public String getEncodedSessionType()
	{
		return encoded; // Maybe should return the decoded type.
	}

	public SJSessionType getSessionType()
	{
		if (decoded == null)
		{
			try
			{
				decoded = SJ_RUNTIME.getRuntimeTypeEncoder().decode(encoded);
			}
			catch (SJIOException ioe)
			{
				throw new SJRuntimeException(ioe);
			}
		}

		return decoded;
	}

	public String toString()
	{
		return getSessionType().toString();
	}
}
