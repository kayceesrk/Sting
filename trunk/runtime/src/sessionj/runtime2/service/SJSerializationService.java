package sessionj.runtime2.service;

import sessionj.runtime2.SJIOException;
import sessionj.runtime2.net.SJComponentId;
import sessionj.runtime2.net.SJSocket;

public interface SJSerializationService extends SJServiceComponent
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.SJSerializationService");
	
	public void writeObject(SJSocket s, Object o) throws SJIOException;
	public Object readObject(SJSocket s) throws SJIOException, ClassNotFoundException;
}
