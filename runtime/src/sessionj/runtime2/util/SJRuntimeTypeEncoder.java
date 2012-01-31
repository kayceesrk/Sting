package sessionj.runtime2.util;

import java.io.IOException;

import polyglot.util.TypeEncoder;

import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.*;

import sessionj.runtime2.*;

import static sessionj.SJConstants.*;

public class SJRuntimeTypeEncoder // Basically duplicated from SJTypeEncoder.
{
	private TypeEncoder te;

	public SJRuntimeTypeEncoder(SJTypeSystem sjts)
	{
		this.te = new TypeEncoder(sjts);
	}

	public String encode(SJSessionType st) throws SJIOException
	{
		try
		{
			return te.encode(st);
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public SJSessionType decode(String encoded) throws SJIOException
	{
		try
		{
			return (SJSessionType) te.decode(encoded, POLYGLOT_TYPEDECODER_NAME_ARG);
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
}
