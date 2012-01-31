//$ bin/sessionjc -cp tests/classes/ tests/src/custom/MyFormatter.sj -d tests/classes/ 

package custom;

import java.io.IOException;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

import smtp.sj.messages.*;

public class MyFormatter extends SJUtf8Formatter
{			
	public static final String LINE_FEED = "\n";
	
	public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException 
	{
		try
		{
			/*if (eof) 
			{
				String m = decodeFromUtf8(bb);
				
				throw new SJIOException("[MyFormatter] Unexpected EOF: " + m);
			}*/			
	
			//if (state == GREETING)
			{
				String m = decodeFromUtf8(bb);
					
				if (m.equals("L1") || m.equals("L2"))
				{
					return m;
				}
				else if (m.endsWith(LINE_FEED))
				{
					//state = HELO_ACK;
					
					return m.substring(0, m.length() - LINE_FEED.length());
				}
				else
				{
					return null;
				}
			}
			/*else 
			{
				throw new SJIOException("[MyFormatter] Shouldn't get in here.");
			}*/
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}	
}
