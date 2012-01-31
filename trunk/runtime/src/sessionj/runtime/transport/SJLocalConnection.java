/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

/**
 * @author Raymond
 *
 */
public interface SJLocalConnection extends SJConnection
{
	public void writeReference(Object o) throws SJIOException;
	public Object readReference() throws SJIOException;
}
