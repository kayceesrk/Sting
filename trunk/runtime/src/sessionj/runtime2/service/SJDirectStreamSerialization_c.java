package sessionj.runtime2.service;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

public class SJDirectStreamSerialization_c extends SJServiceComponent_c implements SJDirectStreamSerialization
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.service.SJDirectStreamSerialization_c");
	
	public SJDirectStreamSerialization_c()
	{
		super(COMPONENT_ID);
	}
	
	public void writeObject(SJSocket s, Object o) throws SJIOException
	{
		
	}
	
	public Object readObject(SJSocket s) throws SJIOException
	{
		return null;
	}
}
