//$ bin/sessionj -Dsessionj.transports.negotiation=a -Dsessionj.transports.session=a -cp tests/classes ecoop.bmarks.smtp.event.Server false 2525 a

package ecoop.bmarks.smtp.event;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;
import sessionj.runtime.session.*;

import ecoop.bmarks.*;
import ecoop.bmarks.smtp.messages.*;
import ecoop.bmarks.smtp.SmtpServerFormatter;

public class Server
{			
	public static int signal = MyObject.NO_SIGNAL;
	
	volatile public static boolean counting = false;
	volatile public static int count = 0;	
	
	static protocol smtp_server_mail
	{
		?(EmailAddress)
		.!{
			$250: 
				!<MailAckBody>
		}
	}
	
	static protocol smtp_server_rcpt
	{
		?(EmailAddress)
		.!{
			$250: 
				!<RcptAckBody>
		}
	}
	
	static protocol smtp_server_data	
	{
		!<DataAck>
		.?(MessageBody)
		.!<MessageBodyAck>
	}
	
	static protocol smtp_server_loop
	{
		rec LOOP
		[
			?{
				MAIL_FROM:
					@(smtp_server_mail)
					.#LOOP,						
				RCPT_TO: // This session type does not ensure RCPT comes after a valid MAIL, DATA after a valid RCPT, etc.
					@(smtp_server_rcpt)
					.#LOOP,
				DATA:
					@(smtp_server_data)
					.#LOOP,
				QUIT:
					!<QuitAck>
			}
		]		
	}
	
	static protocol smtp_event_ehlo
	{
		?(Ehlo)
		.!<EhloAck>		
		.@(smtp_server_loop)
	}

	static protocol smtp_event_mail
	{
		@(smtp_server_mail)
		.@(smtp_server_loop)
	}
	
	static protocol smtp_event_rcpt
	{
		@(smtp_server_rcpt)		
		.@(smtp_server_loop)
	}
	
	static protocol smtp_event_data
	{
		?(MessageBody)
		.!<MessageBodyAck>
		.@(smtp_server_loop)
	}
	
	static protocol smtp_event_loop
	{
		?{
			MAIL_FROM:
				@(smtp_server_mail)
				.@(smtp_server_loop),
			RCPT_TO: 
				@(smtp_server_rcpt)
				.@(smtp_server_loop),
			DATA:
				@(smtp_server_data)
				.@(smtp_server_loop),
			QUIT:
				!<QuitAck>
		}		
	}
	
	static protocol smtp_server_body
	{
		!<ServerGreeting>				
		.?(Ehlo)
		.!<EhloAck>		
		.@(smtp_server_loop)
	}
		
	static protocol p_select
	{
		@(smtp_server_body),
		@(smtp_event_ehlo),
		@(smtp_event_loop),
		@(smtp_event_mail),
		@(smtp_event_rcpt),
		@(smtp_event_data)
	}
	
	static protocol smtp_server
	{
		sbegin		
		.@(smtp_server_body)
	}		
	
	public void run(boolean debug, int port, String setups) throws Exception
	{
		// Although we currently need to give e.g. "s" and "a" as the negotiation and session transports for asynch. comm. support, non-SJ compatibility mode disables the session initiation handshake and so we can just use "a" directly (as the negotiation (and session) transport). 
		//SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, transports, SmtpServerFormatter.class);
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, setups, SmtpServerFormatter.class);
				
		final noalias SJSelector sel = SJRuntime.selectorFor(p_select);
		
		try (sel)
		{			
			noalias SJServerSocket ss;
			
			try (ss)
			{
				ss = SJServerSocket.create(smtp_server, port, params);
				
				sel.registerAccept(ss);
				
				while (true)
				{			
					noalias SJSocket s;			
					
					try (s)
					{
						s = sel.select();
						
						typecase (s)
						{
							when (@(smtp_server_body))
							{
								//220 smtp1.cc.ic.ac.uk ESMTP Exim 4.69 Sun, 22 Nov 2009 14:36:55 +0000
								ServerGreeting greeting = new ServerGreeting("localhost ESMTP blah blah blah");
								//System.out.print("Sending: " + greeting);			
								s.send(greeting);
								
								sel.registerInput(s);
							}
							when (@(smtp_event_ehlo))
							{
							    Ehlo ehlo = (Ehlo) s.receive();
								//System.out.print("Received: " + ehlo);
								
								/*250-smtp1.cc.ic.ac.uk Hello tui.doc.ic.ac.uk [146.169.2.83]
								250-SIZE 26214400
								250-PIPELINING
								250-STARTTLS
								250 HELP*/
								EhloAck ehloAck = new EhloAck("250 Hello foobar [1.2.3.4]");
								//System.out.print("Sending: " + ehloAck);			
								s.send(ehloAck);
								
								s.recursion(LOOP)
								{							
									sel.registerInput(s);
								}
							}
							when (@(smtp_event_loop))
							{								
								s.inbranch()
								{
									case MAIL_FROM:
									{
										sel.registerInput(s);													
									}
									case RCPT_TO:
									{
										sel.registerInput(s);
									}
									case DATA:
									{
										//354 Enter message, ending with "." on a line by itself
										DataAck dataAck = new DataAck("Enter message, ending with \".\" on a line by itself"); // Unlike the "ack bodies", already prefixes the reply code.
										//System.out.print("Sending: " + dataAck);			
										s.send(dataAck);											
										
										sel.registerInput(s);
									}
									case QUIT:
									{
										//221 smtp1.cc.ic.ac.uk closing connection
										QuitAck quitAck = new QuitAck("localhost closing connection"); // Unlike the "ack bodies", already prefixes the reply code.
										//System.out.print("Sending: " + quitAck);			
										s.send(quitAck);	
									}
								}								
							}
							when (@(smtp_event_mail))
							{
							    EmailAddress email = (EmailAddress) s.receive();
								//System.out.print("Received: " + email);
								
								//250 OK
								s.outbranch($250)
								{
									MailAckBody mailAckBody = new MailAckBody(SmtpMessage.SPACE_SEPARATOR + "OK"); // "Ack bodies" need the space/hyphen separator. 
									//System.out.print("Sending: " + mailAckBody);			
									s.send(mailAckBody);
								}
								
								s.recursion(LOOP)
								{
									sel.registerInput(s);
								}
							}
							when (@(smtp_event_rcpt))
							{
							    EmailAddress email = (EmailAddress) s.receive();
								//System.out.print("Received: " + email);
								
								//250 Accepted
								s.outbranch($250)
								{
									RcptAckBody rcptAckBody = new RcptAckBody(SmtpMessage.SPACE_SEPARATOR + "Accepted");
									//System.out.print("Sending: " + rcptAckBody);			
									s.send(rcptAckBody);
								}					
								
								s.recursion(LOOP)
								{
									sel.registerInput(s);
								}								
							}
							when (@(smtp_event_data))
							{
							    MessageBody data = (MessageBody) s.receive();
								//System.out.print("Received: " + data);
								
								//250 OK id=1NCDaj-0001P0-V7
								MessageBodyAck messageBodyAck = new MessageBodyAck("OK id=1ABCde-2345F6-G7");
								//System.out.print("Sending: " + messageBodyAck);			
								s.send(messageBodyAck);
								
								if (counting)
								{								
									Server.count++;
									
									if (debug)
									{
										System.out.println(count);
									}
								}								
								
								s.recursion(LOOP)
								{
									sel.registerInput(s);
								}								
							}
						}					
					}
					catch (Exception x)
					{
						x.printStackTrace();
					}
				}
			}
			finally
			{
				
			}
		}				
		finally 
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		
		String setups = args[2];
		
		new Server().run(debug, port, setups);
	}
}
