package sessionj.runtime2.net;

import java.util.*;

import sessionj.runtime2.*;
import sessionj.runtime2.service.*;

public interface SJSocket
{
	/* Service components. */
	
	//Map<SJComponentId, SJServiceComponent> services = new HashMap<SJComponentId, SJServiceComponent>(); 
	
	public SJServiceComponent getService(SJComponentId cid);
	
	/* Transport connections. */
	
	// ...
	
	/* Administration. */
	public SJProtocol getProtocol();

	public String getHostName();
	public int getPort();

	public String getLocalHostName();
	public int getLocalPort();

	public SJSessionParameters getSessionParameters();
	
	/* Dummy compilation targets. */
	// Sending.
	public void send(Object o) throws SJIOException;
	public void sendInt(int i) throws SJIOException;
	public void sendBoolean(boolean b) throws SJIOException;
	public void sendDouble(double d) throws SJIOException;

	public void pass(Object o) throws SJIOException;

	public void copy(Object o) throws SJIOException;
	/*public void copyInt(int i) throws SJIOException;
	public void copyDouble(double d) throws SJIOException;*/

	// Receiving.
	public Object receive() throws SJIOException, ClassNotFoundException;
	public int receiveInt() throws SJIOException;
	public boolean receiveBoolean() throws SJIOException;
	public double receiveDouble() throws SJIOException;

	/*public Object receive(int timeout) throws SJIOException, ClassNotFoundException;
	public int receiveInt(int timeout) throws SJIOException;
	public boolean receiveBoolean(int timeout) throws SJIOException;
	public double receiveDouble(int timeout) throws SJIOException;*/

	// Higher-order.
	public void sendChannel(SJService c, String encoded) throws SJIOException;
	public SJService receiveChannel(String encoded) throws SJIOException;

	public void sendSession(SJSocket s, String encoded) throws SJIOException;
	public SJSocket receiveSession(String encoded) throws SJIOException;
	public SJSocket receiveSession(String encoded, SJSessionParameters params) throws SJIOException;
}
