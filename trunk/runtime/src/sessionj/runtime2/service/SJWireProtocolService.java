package sessionj.runtime2.service;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

public interface SJWireProtocolService extends SJServiceComponent
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.SJWireProtocolService");
	
	public void writeObject(SJSocket s, Object o) throws SJIOException;
	public void writeByte(SJSocket s, byte b) throws SJIOException;
	public void writeInt(SJSocket s, int v) throws SJIOException;
	public void writeBoolean(SJSocket s, boolean b) throws SJIOException;	
	
	public Object readObject(SJSocket s) throws SJIOException, ClassNotFoundException;
	public byte readByte(SJSocket s) throws SJIOException;
	public int readInt(SJSocket s) throws SJIOException;
	public boolean readBoolean(SJSocket s) throws SJIOException;
	
	public void sendChannel() throws SJIOException;
	public SJService receiveChannel() throws SJIOException;
	
	public void delegateSession() throws SJIOException;
	public SJSocket receiveSession() throws SJIOException;
	
	public void writeControlSignal() throws SJIOException;
	public void handleControlSignal() throws SJIOException;
}
