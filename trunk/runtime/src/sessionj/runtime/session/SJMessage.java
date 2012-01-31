/**
 * 
 */
package sessionj.runtime.session;

import java.io.Serializable;

/**
 * @author Raymond
 *
 */
public class SJMessage implements Serializable
{
	// These are independent from the flags used by the wire protocol.
	public static final byte SJ_CONTROL = 1;
    // Maybe SJMessage shouldn't include control signals. Then nextMessage serializer routine
    // should throw control signals instead of returning them.
	public static final byte SJ_OBJECT = 2; 
	public static final byte SJ_REFERENCE = 3;
	public static final byte SJ_BYTE = 4;
	public static final byte SJ_INT = 5;
	public static final byte SJ_BOOLEAN = 6;	
	public static final byte SJ_DOUBLE = 7;
	
	//FIXME: should factor our explicit label and "synchronization" types.
	
	private final byte type;
	private final Object content;
	
	public SJMessage(byte type, Object content)
	{
		this.type = type;
		this.content = content;
	}
	
	public byte getType()
	{
		return type;
	}
	
	public Object getContent()
	{
		return content;
	}
	
	public byte getByteValue()
	{
		return (Byte) content;
	}
	
	public int getIntValue()
	{
		return (Integer) content;
	}
	
	public Boolean getBooleanValue()
	{
		return (Boolean) content;
	}
	
	public String toString()
	{
		return content.toString();
	}
}
