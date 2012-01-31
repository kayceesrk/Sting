package ecoop.bmarks.smtp.client;

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

import ecoop.bmarks.smtp.client.messages.*;

/*
 * Message formatters are like a localised version of the protocol: the messages received by writeMessage should follow the dual protocol to the messages returned by readNextMessage. But the formatter is an object: need object-based session types to control this.
 * 
 * Might be convenient to extend the custom message formatter interface to maintain a sent message history (e.g. as a stack) - this contextual information can help (or may be needed) for parsing purposes.
 * 
 */
public class SmtpClientFormatter extends SJUtf8Formatter
{			
	public static final String _SPACE = "_SPACE"; // Hack to support ' ' as a branch label. We can parse the ' ' here, but need to return a String to represent it (branch labels must are currently coupled to String values). 
	public static final String _HYPHEN = "_HYPHEN";

	private static final String LINE_FEED = "\n";		
	
	/*// Rather than "manual" state management (enum, switch, update), would be nice to use a selector with typecase; then we'd get static typing (it's about controlling how states should progress, not just the overall set of states). Afterall, objects (actors?) are essentially event-driven entities. 
	private static final int GREETING = 1;
	
	private static final int HELO_ACK = 2;
	
	private static final int MAIL_ACK_FIRST = 3; // "2" or "5".
	private static final int MAIL_TWO_ACK_REST = 4; // The next two digits of the MAIL reply code.
	private static final int MAIL_TWO_ACK_CONTINUE = 5; // Final line of reply or not. If yes, then goto MAIL_TWO_ACK_MORE; else goto MAIL_TWO_ACK_END.   
	private static final int MAIL_TWO_ACK_MORE = 21; // Goto MAIL_ACK_FIRST.
	private static final int MAIL_TWO_ACK_END = 22; // Goto RCPT_OR_DATA_ACK. 
	
	private static final int MAIL_FIVE_ACK_REST = 34; // The next two digits of the MAIL reply code.
	private static final int MAIL_FIVE_ACK_CONTINUE = 30; // Final line of reply or not (hyphen or space).    
	private static final int MAIL_FIVE_ACK_MORE = 31; 
	private static final int MAIL_FIVE_ACK_END = 32; 	
	
	private static final int RCPT_OR_DATA_ACK = 6; 
	//private static final int RCPT_ACK = 5;
	private static final int RCPT2_ACK = 7;
	private static final int RCPT5_ACK = 8;
	private static final int DATA_ACK = 9;
	private static final int MESSAGEBODY_ACK = 10;
	private static final int QUIT_ACK = 11;
	
	private int state = GREETING;*/

	// Using token values of each expected message type to signify the current parser state. The parsing related routines are also provided by these dummy objects.
	private static final ServerGreeting GREETING = new ServerGreeting("");
	
	private static final String EHLO_ACK_FIRST_DIGIT = "2"; // A banch input.
	private static final ReplyCodeSecondAndThirdDigits EHLO_ACK_SECOND_AND_THIRD_DIGITS = new ReplyCodeSecondAndThirdDigits(""); 
	private static final String EHLO_ACK_HYPHEN_OR_SPACE = "EHLO_ACK - or \" \""; // A banch input.
	private static final EhloAckBody EHLO_ACK_HYPHEN_BODY = new EhloAckBody("");
	private static final EhloAckBody EHLO_ACK_SPACE_BODY = new EhloAckBody("");
	
	private static final String MAIL_ACK_FIRST_DIGIT = "2 or 5"; 
	private static final ReplyCodeSecondAndThirdDigits MAIL_ACK_2_SECOND_AND_THIRD_DIGITS = new ReplyCodeSecondAndThirdDigits(""); 
	private static final String MAIL_ACK_2_HYPHEN_OR_SPACE = "MAIL_ACK - or \" \"";
	private static final MailAckBody MAIL_ACK_2_HYPHEN_BODY = new MailAckBody("");
	private static final MailAckBody MAIL_ACK_2_SPACE_BODY = new MailAckBody("");
	private static final ReplyCodeSecondAndThirdDigits MAIL_ACK_5_SECOND_AND_THIRD_DIGITS = new ReplyCodeSecondAndThirdDigits(""); 
	private static final String MAIL_ACK_5_HYPHEN_OR_SPACE = "- or \" \""; 
	private static final MailAckBody MAIL_ACK_5_HYPHEN_BODY = new MailAckBody(""); 
	private static final MailAckBody MAIL_ACK_5_SPACE_BODY = new MailAckBody(""); // Don't really need to distinguish this from the above, we go to the same next state.
	
	private static final String RCPT_OR_DATA_ACK_FIRST_DIGIT = "2 or 5 or 3";
	
	private static final ReplyCodeSecondAndThirdDigits RCPT_ACK_2_SECOND_AND_THIRD_DIGITS = new ReplyCodeSecondAndThirdDigits(""); 
	private static final RcptAckBody RCPT_ACK_2_BODY = new RcptAckBody("");
	private static final ReplyCodeSecondAndThirdDigits RCPT_ACK_5_SECOND_AND_THIRD_DIGITS = new ReplyCodeSecondAndThirdDigits(""); 
	private static final RcptAckBody RCPT_ACK_5_BODY = new RcptAckBody("");
	 
	private static final DataAck DATA_ACK = new DataAck("");
	private static final MessageBodyAck MESSAGE_BODY_ACK = new MessageBodyAck("");
	private static final QuitAck QUIT_ACK = new QuitAck("");
	
	private /*SmtpMessage*/ Object state = GREETING;
	
	public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException // bb is read-only and already flipped (from SJCustomeMessageFormatter).
	{
		try
		{
			String m = decodeFromUtf8(bb);
			String mm = m;
			String consumed = "";
			
			Object o = null;
			
			if (eof)// && state != QUIT_ACK) 
			{
				throw new SJIOException("[SmtpClientFormatter] Unexpected EOF: " + m);
			}				
	
			if (state == GREETING)
			{
				if (GREETING.isParseable(m))
				{
					state = EHLO_ACK_FIRST_DIGIT;
					
					//return GREETING.parse(m);
					o = GREETING.parse(m);
					consumed = m;
				}
			}
			
			else if (state == EHLO_ACK_FIRST_DIGIT)
			{
				if (m.equals("2")) 
				{
					state = EHLO_ACK_SECOND_AND_THIRD_DIGITS;
					
					//return m;
					o = m;
					consumed = m;
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			else if (state == EHLO_ACK_SECOND_AND_THIRD_DIGITS)
			{
				if (EHLO_ACK_SECOND_AND_THIRD_DIGITS.isParseable(m))
				{
					state = EHLO_ACK_HYPHEN_OR_SPACE;
					
					//return EHLO_ACK_SECOND_AND_THIRD_DIGITS.parse(m);
					o = EHLO_ACK_SECOND_AND_THIRD_DIGITS.parse(m);
					consumed = m;
				}
			} 
			else if (state == EHLO_ACK_HYPHEN_OR_SPACE)
			{
				if (m.equals("-")) 
				{
					state = EHLO_ACK_HYPHEN_BODY;
					
					//return _HYPHEN;
					o = _HYPHEN;
					consumed = "-";
				}
				else if (m.equals(" ")) 
				{
					state = EHLO_ACK_SPACE_BODY;
					
					//return _SPACE;
					o = _SPACE;
					consumed = " ";
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			else if (state == EHLO_ACK_HYPHEN_BODY)
			{
				if (EHLO_ACK_HYPHEN_BODY.isParseable(m))
				{
					state = EHLO_ACK_FIRST_DIGIT;
					
					//return EHLO_ACK_HYPHEN_BODY.parse(m);
					o = EHLO_ACK_HYPHEN_BODY.parse(m);
					consumed = m;
				}
			}
			else if (state == EHLO_ACK_SPACE_BODY)
			{
				if (EHLO_ACK_SPACE_BODY.isParseable(m))
				{
					state = MAIL_ACK_FIRST_DIGIT;
					
					//return EHLO_ACK_SPACE_BODY.parse(m);
					o = EHLO_ACK_SPACE_BODY.parse(m);
					consumed = m;
				}
			}
			
			else if (state == MAIL_ACK_FIRST_DIGIT)
			{								
				if (m.equals("2")) 
				{
					state = MAIL_ACK_2_SECOND_AND_THIRD_DIGITS;
					
					//return m;
					o = m;
					consumed = "2";
				}
				else if (m.equals("5")) 
				{
					state = MAIL_ACK_5_SECOND_AND_THIRD_DIGITS;
					
					//return m;
					o = m;
					consumed = "5";
				}				
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			else if (state == MAIL_ACK_2_SECOND_AND_THIRD_DIGITS)
			{
				if (MAIL_ACK_2_SECOND_AND_THIRD_DIGITS.isParseable(m))
				{
					state = MAIL_ACK_2_HYPHEN_OR_SPACE;
					
					//return MAIL_ACK_2_SECOND_AND_THIRD_DIGITS.parse(m);
					o = MAIL_ACK_2_SECOND_AND_THIRD_DIGITS.parse(m);
					consumed = m;
				}
			} 
			else if (state == MAIL_ACK_2_HYPHEN_OR_SPACE)
			{
				if (m.equals("-")) 
				{
					state = MAIL_ACK_2_HYPHEN_BODY;
					
					//return _HYPHEN;
					o = _HYPHEN;
					consumed = "-";
				}
				else if (m.equals(" ")) 
				{
					state = MAIL_ACK_2_SPACE_BODY;
					
					//return _SPACE;
					o = _SPACE;
					consumed = " ";
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			else if (state == MAIL_ACK_2_HYPHEN_BODY)
			{
				if (MAIL_ACK_2_HYPHEN_BODY.isParseable(m))
				{
					state = MAIL_ACK_FIRST_DIGIT;
					
					//return MAIL_ACK_2_HYPHEN_BODY.parse(m);
					o = MAIL_ACK_2_HYPHEN_BODY.parse(m);
					consumed = m;
				}
			}
			else if (state == MAIL_ACK_2_SPACE_BODY)
			{
				if (MAIL_ACK_2_SPACE_BODY.isParseable(m))
				{
					state = RCPT_OR_DATA_ACK_FIRST_DIGIT;
					
					//return MAIL_ACK_2_SPACE_BODY.parse(m);
					o = MAIL_ACK_2_SPACE_BODY.parse(m);
					consumed = m;
				}
			}
			else if (state == MAIL_ACK_5_SECOND_AND_THIRD_DIGITS)
			{
				if (MAIL_ACK_5_SECOND_AND_THIRD_DIGITS.isParseable(m))
				{
					state = MAIL_ACK_5_HYPHEN_OR_SPACE;
					
					//return MAIL_ACK_5_SECOND_AND_THIRD_DIGITS.parse(m);
					o = MAIL_ACK_5_SECOND_AND_THIRD_DIGITS.parse(m);
					consumed = m;
				}
			} 
			else if (state == MAIL_ACK_5_HYPHEN_OR_SPACE)
			{
				if (m.equals("-")) 
				{
					state = MAIL_ACK_5_HYPHEN_BODY;
					
					//return _HYPHEN;
					o = _HYPHEN;
					consumed = "-";
				}
				else if (m.equals(" ")) 
				{
					state = MAIL_ACK_5_SPACE_BODY;
					
					//return _SPACE;
					o = _SPACE;
					consumed = " ";
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			else if (state == MAIL_ACK_5_HYPHEN_BODY)
			{
				if (MAIL_ACK_5_HYPHEN_BODY.isParseable(m))
				{
					state = MAIL_ACK_FIRST_DIGIT;
					
					//return MAIL_ACK_5_HYPHEN_BODY.parse(m);
					o = MAIL_ACK_5_HYPHEN_BODY.parse(m);
					consumed = m;
				}
			}
			else if (state == MAIL_ACK_5_SPACE_BODY)
			{
				if (MAIL_ACK_5_SPACE_BODY.isParseable(m))
				{
					state = MAIL_ACK_FIRST_DIGIT;
					
					//return MAIL_ACK_5_SPACE_BODY.parse(m);
					o = MAIL_ACK_5_SPACE_BODY.parse(m);
					consumed = m;
				}
			}
			
			else if (state == RCPT_OR_DATA_ACK_FIRST_DIGIT)
			{
				if (m.equals("2")) 
				{
					state = RCPT_ACK_2_SECOND_AND_THIRD_DIGITS;
					
					//return m;
					o = m;
					consumed = "2";
				}
				else if (m.equals("5")) 
				{
					state = RCPT_ACK_5_SECOND_AND_THIRD_DIGITS;
					
					//return m;
					o = m;
					consumed = "5";
				}				
				else if (m.equals("3")) 
				{
					state = DATA_ACK;
					
					//return null; // Go to next state, to keep reading this message (DataAck).
					o = null;
					consumed = "";
				}	
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + m);
				}
			}
			
			else if (state == RCPT_ACK_2_SECOND_AND_THIRD_DIGITS)
			{
				if (RCPT_ACK_2_SECOND_AND_THIRD_DIGITS.isParseable(m))
				{
					state = RCPT_ACK_2_BODY;
					
					//return RCPT_ACK_2_SECOND_AND_THIRD_DIGITS.parse(m);
					o = RCPT_ACK_2_SECOND_AND_THIRD_DIGITS.parse(m);
					consumed = m;
				}
			} 
			else if (state == RCPT_ACK_2_BODY)
			{
				if (RCPT_ACK_2_BODY.isParseable(m))
				{
					state = RCPT_OR_DATA_ACK_FIRST_DIGIT;
					
					//return RCPT_ACK_2_BODY.parse(m);
					o = RCPT_ACK_2_BODY.parse(m);
					consumed = m;
				}
			}
			else if (state == RCPT_ACK_5_SECOND_AND_THIRD_DIGITS)
			{
				if (RCPT_ACK_5_SECOND_AND_THIRD_DIGITS.isParseable(m))
				{
					state = RCPT_ACK_5_BODY;
					
					//return RCPT_ACK_5_SECOND_AND_THIRD_DIGITS.parse(m);
					o = RCPT_ACK_5_SECOND_AND_THIRD_DIGITS.parse(m);
					consumed = m;
				}
			} 
			else if (state == RCPT_ACK_5_BODY)
			{
				if (RCPT_ACK_5_BODY.isParseable(m))
				{
					state = RCPT_OR_DATA_ACK_FIRST_DIGIT;
					
					//return RCPT_ACK_5_BODY.parse(m);
					o = RCPT_ACK_5_BODY.parse(m);
					consumed = m;
				}
			}
			
			else if (state == DATA_ACK)
			{
				if (DATA_ACK.isParseable(m))
				{
					state = MESSAGE_BODY_ACK;
					
					//return DATA_ACK.parse(m);
					o = DATA_ACK.parse(m);
					consumed = m;
				}
			}
			
			else if (state == MESSAGE_BODY_ACK)
			{
				if (MESSAGE_BODY_ACK.isParseable(m))
				{
					state = MAIL_ACK_FIRST_DIGIT;
					o = MESSAGE_BODY_ACK.parse(m);
					consumed = m;
				}
			}

            // FIXME: never getting to this state now (infinite loop)
			else if (state == QUIT_ACK)
			{
				//System.out.println("1: " + m + ", " Arrays.toString(m.getBytes()));
				
				if (QUIT_ACK.isParseable(m))
				{
					o = QUIT_ACK.parse(m);
					consumed = m;
				}
			}
			
			else 
			{
				throw new SJIOException("[SJSmptFormatter] Invalid state: " + state);
			}
			
			if (o != null && consumed.length() > 0)
			{
				restoreReadButUnusedData(bb, consumed.getBytes().length); // FIXME: should specify the charset.
			}
			
			return o;
			
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}
	
}
