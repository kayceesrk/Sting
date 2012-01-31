//<By MQ> Added

package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import static sessionj.runtime.util.SJRuntimeUtils.*;

public class SJBatchedSerializer extends SJAbstractSerializer
{
    private static final byte FALSE = 0;
    private static final byte TRUE = 1;
    private final boolean BATCHING_ENABLED = true;
    private final boolean DEBUG_ENABLED = false;

    private SJSerializer ser;
    private byte[] buffer = null;

    public SJBatchedSerializer(SJSerializer ser)
    {
	super(ser.getConnection());
	this.ser = ser;
    }

    public void flush() throws SJIOException
    {	
	if(!BATCHING_ENABLED)
	    return;
	if(buffer != null)
	{
	    debug("flushing buffer. Bytes: " + buffer.length);
	    conn.writeBytes(buffer);
	    buffer = null;
	}
    }

    public byte[] getUnflushedBytes()
    {
	return buffer;
    }

    public void setUnflushedBytes(byte[] b)
    {
	buffer = b;
    }

    public boolean zeroCopySupported()
    {
	return ser.zeroCopySupported();
    }

    public void close()
    {
	ser.close();
    }

    public void writeObject(Object o) throws SJIOException
    {
	debug("writeObject()");
	if(o instanceof SJControlSignal)
	{
	    if(BATCHING_ENABLED)
	    {
		addToBatch(SJ_CONTROL, serializeObject2(o));		
	    }
	    else
	    {
		//System.out.println("BatchedSerilizer.writeObject(): wrote SJControlSignal(): " + o);
		writeControlSignal((SJControlSignal)o);
	    }
	    return;
	}
	if(!BATCHING_ENABLED)
	{
	    ser.writeObject(o);
	    return;
	}
	addToBatch(SJ_OBJECT, serializeObject2(o));
    }

    public void writeByte(byte b) throws SJIOException
    {
	debug("writeByte()");
	if(!BATCHING_ENABLED)
	    {
		ser.writeByte(b);
		return;
	    }
        addToBatch(SJ_BYTE, b);
    }

    public void writeInt(int i) throws SJIOException
    {
	debug("writeInt()");
	if(!BATCHING_ENABLED)
            {
                ser.writeInt(i);
                return;
            }
        addToBatch(SJ_INT, serializeInt(i));
    }

    public void writeBoolean(boolean b) throws SJIOException
    {
	debug("writeBoolean()");
	//printStackTrace();
	if(!BATCHING_ENABLED)
            {
                ser.writeBoolean(b);
                return;
            }
        addToBatch(SJ_BOOLEAN, serializeBoolean(b));
    }

    public void writeDouble(double d) throws SJIOException
    {
	debug("writeDouble()");
	if(!BATCHING_ENABLED)
            {
                ser.writeDouble(d);
                return;
            }
        addToBatch(SJ_DOUBLE);
	writeObject(new Double(d));
    }

    public Object readObject() throws SJIOException, ClassNotFoundException, SJControlSignal
    {
	return ser.readObject();
    }

    public byte readByte() throws SJIOException, SJControlSignal
    {
	return ser.readByte();
    }

    public int readInt() throws SJIOException, SJControlSignal
    {
	return ser.readInt();
    }

    public boolean readBoolean() throws SJIOException, SJControlSignal
    {
	return ser.readBoolean();
    }

    public double readDouble() throws SJIOException, SJControlSignal
    {
	return ser.readDouble();
    }

    public void writeReference(Object o) throws SJIOException
    {
	flush();
	ser.writeReference(o);
    }

    public Object readReference() throws SJIOException, SJControlSignal
    {
	return ser.readReference();
    }

    public void writeControlSignal(SJControlSignal cs) throws SJIOException
    {
	debug("writeControlSignal()");
	flush();
	ser.writeControlSignal(cs);
    }

    public SJControlSignal readControlSignal() throws SJIOException
    {
	return ser.readControlSignal();
    }

    public SJMessage nextMessage() throws SJIOException, ClassNotFoundException
    {
	return ser.nextMessage();
    }

    private static byte serializeBoolean(boolean b) 
    {
	return b ? TRUE : FALSE;
    }

    private byte[] serializeObject2(Object o) throws SJIOException
    {
	byte[] arr2 = serializeObject(o);
	byte[] arr1 = serializeInt(arr2.length);
	byte[] arr = new byte[arr1.length + arr2.length];
	for(int i=0; i<arr1.length; i++)
        {
	    arr[i] = arr1[i];
	}
	for(int i = arr1.length; i< arr.length; i++)
	{
	    arr[i] = arr2[i-arr1.length];
	}
	return arr;
    }

    private void addToBatch(byte flag, byte[] arr)
    {
	byte[] newArr = null;
	if(buffer != null)
	{
	    newArr = new byte[buffer.length + arr.length + 1];
	    for(int i=0; i<buffer.length; i++)
		newArr[i] = buffer[i];
	    newArr[buffer.length] = flag;
	    for(int i = buffer.length + 1; i< buffer.length + arr.length + 1; i++)
		newArr[i] = arr[i - 1 - buffer.length];
	}
	else
	{
	    newArr = new byte[arr.length + 1];
	    newArr[0] = flag;
	    for(int i=0; i< arr.length; i++)
		newArr[i+1] = arr[i];
	}
	buffer = newArr;	
    }

    private void addToBatch(byte flag, byte b)
    {
        byte[] newArr = null;
        if(buffer != null)
	    {		
		newArr = new byte[buffer.length + 2];
		for(int i=0; i<buffer.length; i++)
		    newArr[i] = buffer[i];

  		newArr[buffer.length] = flag;
		newArr[buffer.length + 1] = b;
	    }
        else
	    {
		newArr = new byte[2];
		newArr[0] = flag;
		newArr[1] = b;
	    }
        buffer = newArr;
    }

    private void addToBatch(byte b)
    {
        byte[] newArr = null;
        if(buffer != null)
            {
                newArr = new byte[buffer.length + 1];
                for(int i=0; i<buffer.length; i++)
                    newArr[i] = buffer[i];

                newArr[buffer.length] = b;
            }
        else
            {
                newArr = new byte[1];
                newArr[0] = b;
            }
        buffer = newArr;
    }

    private void debug(String str)
    {
	if(DEBUG_ENABLED)
	    System.out.println(str);
    }

    void printStackTrace()
    {
	StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for(int i=0; i<elements.length; i++) {
            debug(elements[i].toString());
        }
    }

    public Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal
    {
	Object o;
	byte flag;
	try
	{
	    flag = peekByte();
	}
	catch(SJIOException ex) //no data available
	{
	    //System.out.println("[SJBatchedSerializer]: " + ex);
	    return null;
	}

	if (flag == SJ_OBJECT || flag == SJ_CONTROL)
	    {
		o = peekObjectFromConn();

		if (flag == SJ_CONTROL)
		    {
			throw (SJControlSignal) o;
		    }
	    }
	else if (flag == SJ_REFERENCE)
	    {
		throw new SJIOException("SJBatchedSerializer: peekReference is not supported yet");
		//o = ((SJLocalConnection) conn).peekReference();
	    }
	else
	    {
		throw new SJRuntimeException("[SJBatchedSerializer] Unexpected flag: " + flag);
	    }

	return o;
    }

    private Object peekObjectFromConn() throws SJIOException 
    {	
	int size;
	try
	{
	    size = peekIntFromConn();
	}
	catch (SJIOException ex)
	{
	    return null;
	}

	byte[] bs1 = new byte[size + 1 + SJ_SERIALIZED_INT_LENGTH];
	byte[] bs = new byte[size];
	try
	{
	    conn.peekBytes(bs1);
	}
	catch (SJIOException ex)
	{
	    System.out.println("[SJBatchedSerializer]: " + ex);
	    return null;
	}
	
	for(int i = 1 + SJ_SERIALIZED_INT_LENGTH; i < bs1.length; i++)
	{
	    bs[i - 1 - SJ_SERIALIZED_INT_LENGTH] = bs1[i];
	}

	return deserializeObject(bs);
    }

    private int peekIntFromConn() throws SJIOException
    {
	byte[] bs1 = new byte[SJ_SERIALIZED_INT_LENGTH + 1]; // + 1 for the flag byte
	byte[] bs = new byte[SJ_SERIALIZED_INT_LENGTH];

	conn.peekBytes(bs1);

	for(int i = 1; i < bs1.length; i++)
	{
	    bs[i-1] = bs1[i];
	}

	return deserializeInt(bs);
    }

}