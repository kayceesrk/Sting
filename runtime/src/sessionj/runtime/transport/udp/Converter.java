/* Just put together various conversions necessary for UDP */

package sessionj.runtime.transport.udp;

import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.util.SJRuntimeUtils;

public class Converter {

    public static final int byteArrayToInt(byte[] ba, int offset) throws SJIOException
    {
       return 
         (ba[offset]<<24 | 
          (ba[offset+1]&0xff)<<16 | 
          (ba[offset+2]&0xff)<<8 | 
          (ba[offset+3]&0xff));
    	
    	/*byte[] bs = new byte[4];
    	
    	System.arraycopy(ba, offset, bs, 0, 4);
    	
    	return SJRuntimeUtils.deserializeInt(bs);*/
    }

    public static final void intToByteArray(int i, byte[] ba, int offset) throws SJIOException 
    {
       ba[offset] = (byte)(i>>24); 
       ba[offset+1] = (byte)(i>>16); 
       ba[offset+2] = (byte)(i>>8); 
       ba[offset+3] = (byte)i;
    	
    	/*byte[] bs = SJRuntimeUtils.serializeInt(i);
    	
    	System.arraycopy(bs, 0, ba, offset, 4);*/
    }

    public static int inetAddressToInt(InetAddress ia) throws SJIOException{
	// unless one uses IPV6 this should be 4 bytes
	// should check this but not doing
	byte[] bs = ia.getAddress();
	return byteArrayToInt(bs, 0);
    }

    public static InetAddress intToInetAddress(int i) {
	InetAddress ia = null;
	byte[] bs = new byte[4];
	try {
	    // unless one uses IPV6 this should be 4 bytes
	    // should check this but not doing
	    intToByteArray(i, bs, 0);
	    ia = InetAddress.getByAddress(bs);
	}
	catch (Exception e) {; }
	return ia;
    }

}


