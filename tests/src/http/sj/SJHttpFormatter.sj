//$ bin/sessionjc -cp tests/classes/ tests/src/http/sj/SJHttpFormatter.sj -d tests/classes/ 

package http.sj;

import java.io.IOException;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

import http.sj.messages.*;

// Message formatters are like a localised version of the protocol: the messages received by writeMessage should follow the dual protocol to the messages returned by readNextMessage. But the formatter is an object: need object-based session types to control this.
public class SJHttpFormatter extends SJUtf8Formatter
{			
	public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException // bb is read-only and already flipped (from SJCustomeMessageFormatter).
	{		
		try
		{
			if (eof)
			{
				return new SJHttpReply(decodeFromUtf8(bb));
			}
			else
			{
				return null;
			}						
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}
}
