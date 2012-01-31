/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

/**
 * @author Raymond
 *
 */
public interface SJBoundedBufferConnection extends SJLocalConnection
{
	//public void initBoundedBuffers(int size) throws SJIOException; // Give both the same size for now.
	
	/*public void writeCell(Object o, int i) throws SJIOException;
	public Object readCell(int i) throws SJIOException;*/
	
	public void recurseBB(String lab) throws SJIOException;
}
