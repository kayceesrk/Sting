package sessionj.util;

import java.io.IOException;

import polyglot.types.SemanticException;
import polyglot.util.TypeEncoder;

import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.*;

import static sessionj.SJConstants.*;

public class SJTypeEncoder
{
	private TypeEncoder te;

	public SJTypeEncoder(SJTypeSystem sjts)
	{
		this.te = new TypeEncoder(sjts);
	}

	public String encode(SJSessionType st) throws SemanticException
	{
		try
		{
			return te.encode(st);
		}
		catch (IOException ioe)
		{
			throw new SemanticException(ioe);
		}
	}

	public SJSessionType decode(String encoded) throws SemanticException
	{
		try
		{
			return (SJSessionType) te.decode(encoded, POLYGLOT_TYPEDECODER_NAME_ARG);
		}
		catch (IOException ioe)
		{
			throw new SemanticException(ioe);
		}
	}
}
