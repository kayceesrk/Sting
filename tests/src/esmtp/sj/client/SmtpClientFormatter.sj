//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/SmtpClientFormatter.sj -d tests/classes/ 

package esmtp.sj.client;

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

import esmtp.sj.client.messages.*;

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
		/*if (bb.limit() == 0)
		{
			return null;
		}*/
		
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
					state = QUIT_ACK;
					
					//return MESSAGE_BODY_ACK.parse(m);
					o = MESSAGE_BODY_ACK.parse(m);
					consumed = m;
				}
			}

			else if (state == QUIT_ACK)
			{
				//System.out.println("1: " + m + ", " Arrays.toString(m.getBytes()));
				
				if (QUIT_ACK.isParseable(m))
				{
					//return QUIT_ACK.parse(m);
					o = QUIT_ACK.parse(m);
					consumed = m;
				}
			}
			
			else 
			{
				throw new SJIOException("[SJSmptFormatter] Invalid state: " + state);
			}
			
			//putBackRemainderReadyForReading(bb, "");
			//bb.clear();
			
			//System.out.println("p " + bb.position() + ", l " + bb.limit() + ", c" + bb.capacity());
			
			//foo(o, bb, consumed);
			if (o != null && consumed.length() > 0)
			{
				restoreReadButUnusedData(bb, consumed.getBytes().length); // FIXME: should specify the charset.
			}
			
			//bb.compact();
			
			//return null;
			return o;
			
			/*if (state == GREETING)
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
					state = MAIL_ACK_FIRST;
					
					return new HeloAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			/*else if (state == MAIL_ACK)
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
			}*
			else if (state == MAIL_ACK_FIRST)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.equals("2"))
				{
					state = MAIL_TWO_ACK_REST;								
					
					return ack;
				}
				else if (ack.equals("5"))
				{
					state = MAIL_FIVE_ACK_REST;
					
					return ack;
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + ack);
				}
			}
			else if (state == MAIL_TWO_ACK_REST)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.length() == 2)
				{
					state = MAIL_TWO_ACK_CONTINUE;
					
					return new MailAckTwoDigits(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}			
			
			else if (state == MAIL_TWO_ACK_CONTINUE)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.equals("-"))
				{
					state = MAIL_TWO_ACK_MORE;
					
					return HYPHEN;
				}
				else if (ack.equals(" "))
				{
					state = MAIL_TWO_ACK_END;
					
					return SPACE;
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + ack);
				}
			}		
			else if (state == MAIL_TWO_ACK_MORE)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK_FIRST;
					
					return new MailTwoAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MAIL_TWO_ACK_END)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = RCPT_OR_DATA_ACK;
					
					return new MailTwoAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			/*else if (state == MAIL_FIVE_ACK)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK_FIRST;
					
					return new MailFiveAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}*
			else if (state == MAIL_FIVE_ACK_REST)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.length() == 2)
				{
					state = MAIL_FIVE_ACK_CONTINUE;
					
					return new MailAckTwoDigits(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}			
			else if (state == MAIL_FIVE_ACK_CONTINUE)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.equals("-"))
				{
					state = MAIL_FIVE_ACK_MORE;
					
					return HYPHEN;
				}
				else if (ack.equals(" "))
				{
					state = MAIL_FIVE_ACK_END;
					
					return SPACE;
				}
				else
				{
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + ack);
				}
			}		
			else if (state == MAIL_FIVE_ACK_MORE)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK_FIRST;
					
					return new MailFiveAck(ack.substring(0, ack.length() - LINE_FEED.length()));
				}
				else
				{
					return null;
				}
			}
			else if (state == MAIL_FIVE_ACK_END)
			{
				String ack = decodeFromUtf8(bb);
				
				if (ack.endsWith(LINE_FEED))
				{
					state = MAIL_ACK_FIRST;
					
					return new MailFiveAck(ack.substring(0, ack.length() - LINE_FEED.length()));
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
					//throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + ack);

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

					return new RcptTwoAck(ack.substring(0, ack.length() - LINE_FEED.length()));
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
					
					return new RcptFiveAck(ack.substring(0, ack.length() - LINE_FEED.length()));
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
				throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here.");
			}*/
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
				
				throw new SJIOException("[SmtpClientFormatter] Unexpected EOF: " + m);
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
					throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here: " + ack);
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

					return new RcptTwoAck(ack.substring(0, ack.length() - LINE_FEED.length()));
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
				throw new SJIOException("[SmtpClientFormatter] Shouldn't get in here.");
			}
		}
		catch (CharacterCodingException cce)
		{
			throw new SJIOException(cce);
		}
	}*/		
}
