//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/SJSmtpFormatter.sj -d tests/classes/ 

package smtp.sj;

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

// Message formatters are like a localised version of the protocol: the messages received by writeMessage should follow the dual protocol to the messages returned by readNextMessage. But the formatter is an object: need object-based session types to control this.
public class SJSmtpFormatter extends SJUtf8Formatter
{			
	private static final String LINE_FEED = "\n";
	
	// Rather than "manual" state management (enum, switch, update), would be nice to use a selector with typecase; then we'd get static typing (it's about controlling how states should progress, not just the overall set of states). Afterall, objects (actors?) are essentially event-driven entities. 
	private static final int GREETING = 1;
	private static final int HELO_ACK = 2;
	private static final int MAIL_ACK = 3;
	private static final int RCPT_OR_DATA_ACK = 4;
	//private static final int RCPT_ACK = 5;
	private static final int RCPT2_ACK = 5;
	private static final int RCPT5_ACK = 6;
	private static final int DATA_ACK = 7;
	private static final int MESSAGEBODY_ACK = 8;
	private static final int QUIT_ACK = 9;
	
	private int state = GREETING;

	//private int recipients = 0;	
	
	public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException // bb is read-only and already flipped (from SJCustomeMessageFormatter).
	{
		try
		{
			if (eof && state != QUIT_ACK) 
			{
				String m = decodeFromUtf8(bb);
				
				throw new SJIOException("[SJSmtpFormatter] Unexpected EOF: " + m);
			}				
	
			if (state == GREETING)
			{
				String greeting = decodeFromUtf8(bb);
								
				if (greeting.endsWith(LINE_FEED))
				{
					state = HELO_ACK;
					
					return new ServerGreeting(greeting.substring(0, greeting.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == HELO_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK;
					
					return new HeloAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MAIL_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = RCPT_OR_DATA_ACK;
					
					return new MailAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == RCPT_OR_DATA_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.equals("2"))
				{
					state = RCPT2_ACK;								
					
					return ack;
				}
				else if (ack.equals("5"))
				{
					state = RCPT5_ACK;
					
					return ack;
				}
				else
				{
					//throw new SJIOException("[SJSmtpFormatter] Shouldn't get in here: " + ack);

					state = DATA_ACK;
					
					return null; // Keep reading for DataAck. This assumes ack is not "\n" already.
				}
			}
			else if (state == RCPT2_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = RCPT_OR_DATA_ACK;

					return new Rcpt2Ack(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == RCPT5_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = QUIT_ACK;
					
					return new Rcpt5Ack(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == DATA_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MESSAGEBODY_ACK;
					
					return new DataAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MESSAGEBODY_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = QUIT_ACK;
					
					return new MessageBodyAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}	
			else if (state == QUIT_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED)) // Or is it just EOF directly?
				{
					return new QuitAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else 
			{
				throw new SJIOException("[SJSmtpFormatter] Shouldn't get in here.");
			}
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}
	
	/*public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException // bb is read-only and already flipped (from SJCustomeMessageFormatter).
	{
		try
		{
			if (eof && state != QUIT_ACK) 
			{
				String m = decodeFromUtf8(bb);
				//String m = decodeFromUtf8(bs);
				
				throw new SJIOException("[SJSmtpFormatter] Unexpected EOF: " + m);
			}				
	
			//byte[] bs = copyByteBufferContents(bb);	
			//String m = decodeFromUtf8(bs);
			
			if (state == GREETING)
			{
				String greeting = decodeFromUtf8(bb);
				//String greeting = decodeFromUtf8(bs);
								
				if (greeting.endsWith(LINE_FEED))
				{
					state = HELO_ACK;
					
					return new ServerGreeting(greeting.substring(0, greeting.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == HELO_ACK)
			{
				String ack = decodeFromUtf8(bb);
				//String ack = decodeFromUtf8(bs);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK;
					
					return new HeloAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MAIL_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = RCPT_BRANCH;
					
					return new MailAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == RCPT_BRANCH)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.equals("2"))
				{
					state = RCPT2_ACK;								
					
					return ack;
				}
				else if (ack.equals("5"))
				{
					state = RCPT5_ACK;
					
					return ack;
				}
				else
				{
					throw new SJIOException("[SJSmtpFormatter] Shouldn't get in here: " + ack);
				}
			}
			/*else if (state == RCPT_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					/*recipients++;
				
					if (recipients == 4)*
					{
						state = DATA_ACK;
					}
					
					return new RcptAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}*
			else if (state == RCPT2_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = DATA_ACK;

					return new Rcpt2Ack(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == RCPT5_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = QUIT_ACK;
					
					return new Rcpt5Ack(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == DATA_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MESSAGEBODY_ACK;
					
					return new DataAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MESSAGEBODY_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = QUIT_ACK;
					
					return new MessageBodyAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}	
			else if (state == QUIT_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED)) // Or is it just EOF directly?
				{
					return new QuitAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else 
			{
				throw new SJIOException("[SJSmtpFormatter] Shouldn't get in here.");
			}
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}*/		
}
