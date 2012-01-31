package ecoop.bmarks2.macro.smtp.sj;

import java.io.IOException;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;

import ecoop.bmarks2.macro.smtp.sj.messages.*;
  
public class SmtpServerFormatter extends SJUtf8Formatter
{			
	// Maybe these should be in the Server class.
	protected static final String MAIL_FROM_LABEL = "MAIL_FROM"; 
	protected static final String RCPT_TO_LABEL = "RCPT_TO";
	protected static final String DATA_LABEL = "DATA";
	protected static final String QUIT_LABEL = "QUIT";	
	
	private static final Ehlo EHLO = new Ehlo("");
	private static final Object MAIL_OR_RCPT_OR_DATA_OR_QUIT = "MAIL_OR_RCPT_OR_DATA_OR_QUIT";
	private static final EmailAddress EMAIL_ADDRESS = new EmailAddress("");
	private static final MessageBody MESSAGE_BODY = new MessageBody("");
	
	private Object state = EHLO;	
	private Object prevState = null; // Not currently needed.
	
	public SmtpServerFormatter()
	{
		//System.out.println("FOO!");
		
		/*try
		{
			throw new Exception("foo");
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}*/
	}
	
	public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException // bb is read-only and already flipped (from SJCustomeMessageFormatter).
	{
		//bb.position(0); // Don't remember why this was here.
		
		try
		{			
			String m = decodeFromUtf8(bb); // FIXME: won't work if we've received enough for the next message last time - CMF readNextMessage may not get past conn.readByte(). Could fix for now by always calling here first, then trying conn.readByte.
			
			String parsedFrom = null;
			Object parsed = null;
			
			if (eof) 
			{
				throw new SJIOException("[SmtpServerFormatter] Unexpected EOF: " + m + "; " + Arrays.toString(m.getBytes()));
			}
			
			if (state == EHLO)
			{
				if (EHLO.isParseableFrom(m))
				{
					prevState = state;
					state = MAIL_OR_RCPT_OR_DATA_OR_QUIT;
					
					parsed = EHLO.parse(m);
					parsedFrom = m;
				}
			}
			else if (state == MAIL_OR_RCPT_OR_DATA_OR_QUIT)
			{
				//String orig = m;
				
				m = m.toUpperCase(); 						
				
				if (m.startsWith("MAIL FROM:"))
				{
					prevState = state;
					state = EMAIL_ADDRESS;
					
					parsed = MAIL_FROM_LABEL;
					parsedFrom = "MAIL FROM:";
				}
				else if (m.startsWith("RCPT TO:"))
				{
					prevState = state;
					state = EMAIL_ADDRESS;	
					
					parsed = RCPT_TO_LABEL;
					parsedFrom = "RCPT TO:";
				}
				//else if (deleteSpaces(m).startsWith("DATA\r\n")) // FIXME: incompatible with using orig.
				else if (m.startsWith("DATA\r\n")) 
				{
					prevState = state;
					state = MESSAGE_BODY;	
					
					parsed = DATA_LABEL;
					parsedFrom = "DATA\r\n"; 
				}
				//else if (deleteSpaces(m).startsWith("QUIT\r\n"))
				else if (m.startsWith("QUIT\r\n"))
				{
					prevState = null;
					state = EHLO;
					
					parsed = QUIT_LABEL;
					parsedFrom = "QUIT\r\n"; 
				}
			}
			else if (state == EMAIL_ADDRESS)
			{
				String orig = m;
				
				//m = m.trim(); // Bad: also removes the final "\r\n".
				m = deleteSpaces(orig);
				
				if (EMAIL_ADDRESS.isParseableFrom(m))
				{
					prevState = state;
					state = MAIL_OR_RCPT_OR_DATA_OR_QUIT;
						
					parsed = EMAIL_ADDRESS.parse(m);
					parsedFrom = orig;
				}				
			}
			else if (state == MESSAGE_BODY)
			{
				if (MESSAGE_BODY.isParseableFrom(m))
				{
					prevState = state;
					state = MAIL_OR_RCPT_OR_DATA_OR_QUIT;
						
					parsed = MESSAGE_BODY.parse(m);
					parsedFrom = m;
				}						
			}
			else 
			{
				throw new SJIOException("[SmtpServerFormatter] Shouldn't get in here.");
			}
			
			if (parsed != null && parsedFrom.length() > 0)
			{
				restoreReadButUnusedData(bb, parsedFrom.getBytes().length); // FIXME: should specify the charset.
			}			
						
			return parsed; // Should be null if no message was successfully parsed from the received byte buffer.
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}
	
	private static final String deleteSpaces(String m) // Just spaces, not white space.
	{
		return m.replaceAll(" ", "");
	}
}
