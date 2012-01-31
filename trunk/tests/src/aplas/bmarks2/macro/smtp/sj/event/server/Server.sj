//$ bin/sessionj -Dsessionj.transports.negotiation=a -Dsessionj.transports.session=a -cp tests/classes ecoop.bmarks2.macro.smtp.sj.event.server.Server false 8888 a

package ecoop.bmarks2.macro.smtp.sj.event.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;

import ecoop.bmarks2.macro.smtp.sj.*;
import ecoop.bmarks2.macro.smtp.sj.messages.*;

public class Server extends ecoop.bmarks2.macro.smtp.sj.Server
{			
	static protocol smtp_event_ehlo
	{
		?(Ehlo)
		.!<EhloAck>		
		.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop)
	}

	static protocol smtp_event_mail
	{
		@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_mail)
		.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop)
	}
	
	static protocol smtp_event_rcpt
	{
		@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_rcpt)		
		.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop)
	}
	
	static protocol smtp_event_data
	{
		?(MessageBody)
		.!<MessageBodyAck>
		.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop)
	}
	
	static protocol smtp_event_loop
	{
		?{
			MAIL_FROM:
				@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_mail)
				.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop),
			RCPT_TO: 
				@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_rcpt)
				.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop),
			DATA:
				@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_data)
				.@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_loop),
			QUIT:
				!<QuitAck>
		}		
	}
	
	static protocol p_select
	{
		@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_body),
		@(smtp_event_ehlo),
		@(smtp_event_loop),
		@(smtp_event_mail),
		@(smtp_event_rcpt),
		@(smtp_event_data)
	}
	
	volatile protected boolean run = true;
	volatile protected boolean kill = false;
	volatile private boolean finished = false;
	  
	private String setups;
	
	public Server(boolean debug, int port, String setups)
	{
		super(debug, port);
		
		this.setups = setups;
	}
	
	public void run() throws Exception
	{
		// Although we currently need to give e.g. "s" and "a" as the negotiation and session transports for asynch. comm. support, non-SJ compatibility mode disables the session initiation handshake and so we can just use "a" directly (as the negotiation (and session) transport). 
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, setups, SmtpServerFormatter.class);
				
		final noalias SJSelector sel = SJRuntime.selectorFor(p_select);
		
		try (sel)
		{			
			noalias SJServerSocket ss;
			
			try (ss)
			{
				ss = SJServerSocket.create(smtp_server, getPort(), params);
				
				debugPrintln("[Server] Listening on: " + (getPort() + SJAsyncManualTCP.TCP_PORT_MAP_ADJUST));
				
				sel.registerAccept(ss);				
			}
			finally
			{
			
			}
			
			boolean debug = isDebug();
			
			while (this.run)
			{			
				noalias SJSocket s;			
				
				try (s)
				{
					s = sel.select();
					
					typecase (s)
					{
						when (@(ecoop.bmarks2.macro.smtp.sj.Server.smtp_server_body))
						{
							addClient();
							
							//220 smtp1.cc.ic.ac.uk ESMTP Exim 4.69 Sun, 22 Nov 2009 14:36:55 +0000
							ServerGreeting greeting = new ServerGreeting("localhost ESMTP blah blah blah");
							debugPrintln("Sending: " + greeting);			
							s.send(greeting);
							
							sel.registerInput(s);
						}
						when (@(smtp_event_ehlo))
						{
							Ehlo ehlo = (Ehlo) s.receive();
							debugPrintln("Received: " + ehlo);
							
							/*250-smtp1.cc.ic.ac.uk Hello tui.doc.ic.ac.uk [146.169.2.83]
							250-SIZE 26214400
							250-PIPELINING
							250-STARTTLS
							250 HELP*/
							EhloAck ehloAck = new EhloAck("250 Hello foobar [1.2.3.4]");
							debugPrintln("Sending: " + ehloAck);			
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
									debugPrintln("Sending: " + dataAck);			
									s.send(dataAck);											
									
									sel.registerInput(s);
								}
								case QUIT:
								{
									//221 smtp1.cc.ic.ac.uk closing connection
									QuitAck quitAck = new QuitAck("localhost closing connection"); // Unlike the "ack bodies", already prefixes the reply code.
									debugPrintln("Sending: " + quitAck);			
									s.send(quitAck);	
									
									removeClient();
	                
	                int numClients = getNumClients();
	                
	                debugPrintln("[Server] Clients remaning: " + numClients);
	                
	                if (numClients == 0) // HACK: because the selector closer isn't working yet.
	                {
	                	this.run = false;
	                }
								}
							}								
						}
						when (@(smtp_event_mail))
						{
						    EmailAddress email = (EmailAddress) s.receive();
							debugPrintln("Received: " + email);
							
							//250 OK
							s.outbranch($250)
							{
								MailAckBody mailAckBody;
								
								if (this.kill)
								{
									mailAckBody = new MailAckBody(" QUIT"); // HACK.
								}
								else
								{
									mailAckBody = new MailAckBody(SmtpMessage.SPACE_SEPARATOR + "OK"); // "Ack bodies" need the space/hyphen separator.
								}

								debugPrintln("Sending: " + mailAckBody);			
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
							debugPrintln("Received: " + email);
							
							//250 Accepted
							s.outbranch($250)
							{
								RcptAckBody rcptAckBody = new RcptAckBody(SmtpMessage.SPACE_SEPARATOR + "Accepted");
								debugPrintln("Sending: " + rcptAckBody);			
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
							debugPrintln("Received: " + data);
							
							//250 OK id=1NCDaj-0001P0-V7
							MessageBodyAck messageBodyAck = new MessageBodyAck("OK id=1ABCde-2345F6-G7");
							debugPrintln("Sending: " + messageBodyAck);			
							s.send(messageBodyAck);
							
							if (isCounting()) 
					    {
					      incrementCount(0); // HACK: using a single counter (safe to do so for this single-threaded Server). Could store the "tids" in a map (using local ports as a key), but could be a non-neglible overhead.
					      
					      debugPrintln("[ServerThread] Current count:" + getCountTotal());
					    }								
							
							s.recursion(LOOP)
							{
								sel.registerInput(s);
							}								
						}
					}					
				}
				finally
				{
					
				}
			}						
		}			
		catch (Exception x)
		{
			x.printStackTrace();
		}
		finally 
		{
			this.finished = true;
		}
	}
	
  public void kill() throws Exception
  {
  	int numClients = getNumClients(); 
  	
  	this.kill = true;
  	
  	while (getNumClients() > 0);

  	this.run = false; // Can stop the selector loop after all LoadClients have quit.
  	  	
		while (!this.finished);
  	
		System.out.println("[Server] Finished running (" + numClients + " Clients joined).");
  }	
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		int port = Integer.parseInt(args[1]);		
		String setups = args[2];
		
		new Server(debug, port, setups).run();
	}
}
