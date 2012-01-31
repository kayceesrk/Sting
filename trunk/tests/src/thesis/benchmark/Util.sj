package thesis.benchmark;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Util  
{	
	public static final boolean TCP_NO_DELAY = true;
	
	public static final int DEBUG_DELAY = 1000; // Millis
	
  public static final void debugPrintln(boolean debug, String m)
  {
  	if (debug)
  	{
  		System.out.println(m);
  	}
  }
  
  public static final void closeInputStream(InputStream is)
  {
    try 
    {
			if (is != null)
			{
				is.close();
			}
  	} 
    catch (Exception e) 
    {
  		throw new RuntimeException(e);
  	}
  }
  
  public static final void closeOutputStream(OutputStream os)
  {
		try 
		{
			if (os != null)
			{
				os.flush();
				os.close();
			}
  	} 
		catch (Exception e) 
		{
  		throw new RuntimeException(e);
  	}
  }
  
  public static final void closeSocket(Socket s)
  {
    try 
    {
     	if (s != null)
			{
				s.close();
			}
    } 
    catch (Exception e) 
    {
  		throw new RuntimeException(e);
  	}
  }
  
  public static final void closeServerSocket(ServerSocket ss)
  {
    try 
    {
			if (ss != null)
			{
				ss.close();
			}
  	} 
    catch (Exception e) 
  	{
  		throw new RuntimeException(e);
  	}
  }

  /*
  public static final int REC = 1;
	public static final int QUIT = 2;
  
  public static final byte[] serializeInt(int value) 
  {
    return new byte[] {
			(byte)(value >>> 24),
			(byte)(value >>> 16),
			(byte)(value >>> 8),
			(byte)value
		};
  } 
  
	public static int deserializeInt(byte[] bs) 
	{
		int i = 0;
		
		i += (bs[0] & 255) << 24;
		i += (bs[1] & 255) << 16;
		i += (bs[2] & 255) << 8;
		i += bs[3] & 255;
		
		return i;		
	}	  	
  
	public static final byte[] serializeObject(Object o) throws Exception
	{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		
		try 
		{
			baos = new ByteArrayOutputStream(); // Could optimise by reusing the byte array.
			oos = new ObjectOutputStream(baos);
			
			oos.writeObject(o);
			oos.flush();
			
			return baos.toByteArray();
		} 
		finally 
		{
			if (oos != null)
			{
				oos.close();
			}
			
			if (baos != null)
			{
				baos.close();
			}
		}		
	}
	
	public static final Object deserializeObject(byte[] bs) throws Exception
	{
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		
		try
		{
			bais = new ByteArrayInputStream(bs);
			ois = new ObjectInputStream(bais);
			
			return ois.readObject();
		}
		finally
		{
			if (bais != null)
			{
				bais.close();
			}
			
			if (ois != null)
			{
				ois.close();
			}
		}
	}*/	  
}
