//$ bin/sessionjc -sourcepath tests/src/esmtp/sj/client/messages/';'tests/src/esmtp/sj/client/ tests/src/esmtp/sj/client/Client.sj -d tests/classes/
//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ esmtp.sj.client.Client false localhost 2525 sa 
//$ bin/sessionj -Djava.util.logging.config.file=logging.properties -cp tests/classes/ esmtp.sj.client.Client false localhost 2525 sa

package esmtp.sj.client;

import java.net.InetAddress;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;
import sessionj.runtime.session.*;

//import esmtp.sj.SJSmtpFormatter; // Doesn't work when specifying -sourcepath (without -cp).
//import esmtp.sj.*;
import esmtp.sj.client.messages.*;
//import esmtp.sj.server.Server;

/*
 * FIXME: redo this Client with three-digit branch labels instead of one-digit (where appropriate - but for "wild carded" digits, we won't be able to see their values), and use sent-message history in the parser to help parsing. Also factor the main code chunk into sub-chunks (like doQuit). 
 */
public class Client
{			
	private protocol p_client
	{
		//^(Server.p_server)
		cbegin
		.?(ServerGreeting)
				
		.!<Ehlo>
		.rec EHLO_ACK 
		[		
			?{
				$2: 
				  ?(ReplyCodeSecondAndThirdDigits) // SMTP says once the first reply code has been received, the following will all be the same. How to express this conveniently here? Maybe need the bounded polymorphism with dependent typing.
				  .?{
					  _HYPHEN: // Need full "dependently-typed" labels (in order to use a higher-level type representation for "-" here).
						  ?(EhloAckBody)
	    			  .#EHLO_ACK,
	    			_SPACE:
	    				?(EhloAckBody) // Can differentiate the different message structures at a finer-grained level, e.g. Ehlo2AckBody, Ehlo5AckBody (if this is possible).
				  }				
			}		
		]
		
		.rec MAIL // We should be able to repeatedly send emails.
		[
		  !<Mail>
		  .rec MAIL_ACK 
		  [
		    ?{
		    	$2:
		    		?(ReplyCodeSecondAndThirdDigits)
		    		.?{
		    		_HYPHEN: // Actually, is this needed for the "2 case"?
		    			?(MailAckBody)
		    			.#MAIL_ACK,  
		    		_SPACE: 
		    			?(MailAckBody)
		    		},
		    	$5:
		    		?(ReplyCodeSecondAndThirdDigits)
		    		.?{
		    			_HYPHEN: 
		    				?(MailAckBody)
		    				.#MAIL_ACK,  
		    			_SPACE: 
		    				?(MailAckBody)
		    				.#MAIL
		    		}
		    }
		  ]		  
		]
		
		.rec RCPT 
		[
			!{
				RCPT:
					!<RcptTo>
					.?{
						$2: 
							?(ReplyCodeSecondAndThirdDigits)
							.?(RcptAckBody) // Unlike MailAck, the " " part of the prefix is contained in here (we don't bother to check for "-").
							.#RCPT,
						$5:	
							?(ReplyCodeSecondAndThirdDigits)
							.?(RcptAckBody)
							.#RCPT
					},
					
			  DATA: // We shouldn't be allowed to do this if no valid recipients were specified.
					!<DataLineFeed>.?(DataAck) // Luckily, DataAck will start with a "3", which distinguishes it from the above RcptAcks.
					.!<MessageBody>.?(MessageBodyAck)
							
				/*QUIT: // We should have this here in case none of the recipients were accepted. But we'd need to differentiate the QuitAck reply code from RcptAck reply codes (and can't be bothered to that right now). (And then we don't want the final Quit at the end either.) Instead, it may be easier to check for an error code after DATA command (and maybe just quit after that). But the problem with this is that we need to distinguish the "5" prefixed error codes between RcptAck and DataAck, i.e. "550" vs. "503".
					!<Quit>.?(QuitAck)*/
			}
		]
		
		.!<Quit>.?(QuitAck)	// In principle, we should be able to QUIT at any time in the protocol, not just at the end.
	}
	
	public void run(boolean debug, String setups, String server, int port) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		
		final String fqdn = InetAddress.getLocalHost().getHostName().toString(); //getCanonicalHostName().toString();
		
		System.out.println("fqdn: " + fqdn);
		
		//SJSessionParameters sparams = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, transports, SmtpClientFormatter.class);
		SJSessionParameters sparams = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, setups, SmtpClientFormatter.class);
		
		final noalias SJSocket s;	
			
		try (s)
		{
			System.out.println("Requesting SMTP session with: " + server + ":" + port);
			
			s = SJService.create(p_client, server, port).request(sparams);	
			
			System.out.println((ServerGreeting) s.receive());
			
			Ehlo ehlo = new Ehlo(fqdn);
			System.out.print("Sending: " + ehlo);			
			s.send(ehlo);			
			
			s.recursion(EHLO_ACK)
			{
				s.inbranch()
				{						
					case $2:
					{
						String code = ((ReplyCodeSecondAndThirdDigits) s.receive()).toString();							
						
						s.inbranch()
						{
							case _HYPHEN:
							{
								System.out.println("Received: " + "2" + code + "-" + (EhloAckBody) s.receive());									
								
								s.recurse(EHLO_ACK);
							}
							case _SPACE:
							{
								System.out.println("Received: " + "2" + code + " " + (EhloAckBody) s.receive());
							}
						}
					}
				}
			}
			
			/*System.out.print("Sender's address? (e.g. sender@domain.com): ");			
			Mail mail = new Mail(readUserInput(sc));
			System.out.print("Sending: " + mail);
			s.send(mail);
			System.out.println("Received: " + (MailAck) s.receive());*/
									
			s.recursion(MAIL)
			{
				System.out.print("Sender's address? (e.g. sender@domain.com): ");			
				Mail mail = new Mail(readUserInput(sc));
				System.out.print("Sending: " + mail);
				s.send(mail);
				
				s.recursion(MAIL_ACK)
				{
					s.inbranch()
					{						
						case $2:
						{
							String code = ((ReplyCodeSecondAndThirdDigits) s.receive()).toString();							
							
							s.inbranch()
							{
								case _HYPHEN:
								{
									System.out.println("Received: " + "2" + code + "-" + (MailAckBody) s.receive());									
									
									s.recurse(MAIL_ACK);
								}
								case _SPACE:
								{
									System.out.println("Received: " + "2" + code + " " + (MailAckBody) s.receive());
								}
							}
						}
						case $5:
						{
							String code = ((ReplyCodeSecondAndThirdDigits) s.receive()).toString();							
							
							s.inbranch()
							{
								case _HYPHEN:
								{
									System.out.println("Received: " + "5" + code + "-" + (MailAckBody) s.receive());									
									
									s.recurse(MAIL_ACK);
								}
								case _SPACE:
								{
									System.out.println("Received: " + "5" + code + " " + (MailAckBody) s.receive());
									
									s.recurse(MAIL);
								}
							}
						}
					}
				}
			}
			
			boolean firstIteration = true;
			boolean anotherRecipient = true;
			
			int recipients = 0;			
			
			s.recursion(RCPT)
			{
				if (firstIteration)
				{
					firstIteration = false;
				}
				else
				{				
					System.out.print("Another recipient? [y/n]: ");
					
					String reply = readUserInput(sc);
					
					if (reply.equals("y"))
					{
						anotherRecipient = true;
					}
				}
							
				if (anotherRecipient)
				{
					s.outbranch(RCPT)
					{
						System.out.print("Recipient's address?: ");
						RcptTo rcptTo = new RcptTo(readUserInput(sc));
						System.out.print("Sending: RCPT" + rcptTo); // Need to re-add the "RCPT" that was sent by the outbranch.
						s.send(rcptTo);									
						
						s.inbranch()
						{
							case $2:
							{
								String code = ((ReplyCodeSecondAndThirdDigits) s.receive()).toString();
								
								System.out.println("Received: " + "2" + code + (RcptAckBody) s.receive()); // Need to re-add the "2" that was already consumed by the inbranch.
								
								recipients++;
								
								s.recurse(RCPT);								
							}
							case $5:
							{
								String code = ((ReplyCodeSecondAndThirdDigits) s.receive()).toString();
								
								System.out.println("Received: " + "5" + code + (RcptAckBody) s.receive());
								
								//doQuit(s); // Currently, delegation within loop contexts completely forbidden, even though this branch does not loop.
								
								s.recurse(RCPT);
							}
						}					
					}
					
					anotherRecipient = false;
				}
				else //if (recipients > 0)
				{
					if (recipients == 0) // HACK: we should send a QUIT here (would need to modify session type and parser to differentiate QuitAck from RctAck).
					{
						throw new RuntimeException("No valid recipients given.");
					}
					
					s.outbranch(DATA)
					{
						DataLineFeed dataLF = new DataLineFeed();
						System.out.print("Sending: DATA" + dataLF); 
						s.send(dataLF);												
						System.out.print("Received: " + (DataAck) s.receive()); // SmtpAcks already include a terminal line feed.
						
						System.out.print("Message subject?: ");
						String subject = readUserInput(sc);
						
						System.out.print("Message body? (Enter ends the message.): ");
						String text = readUserInput(sc);
						
						MessageBody body = new MessageBody("SUBJECT:" + subject + "\n\n" + text);				
						System.out.print("Sending: " + body);
						s.send(body);
						System.out.print("Received: " + (MessageBodyAck) s.receive());
						
						//doQuit(s);					
					}
				}
				/*else
				{
					throw new RuntimeException("foo");
				}*/
			}
			
			doQuit(s);
		}
		finally
		{
			
		}
	}
	
	private static String readUserInput(Scanner sc)
	{
		String m = sc.nextLine();
		
		//return m.substring(0, m.length() - "\n".length()); // FIXME: "\n" not supported as a call target.
		//return m.substring(0, m.length() - 1);
		
		return m;
	}
	
	private static void doQuit(final noalias !<Quit>.?(QuitAck) s) throws SJIOException, ClassNotFoundException
	{
		Quit quit = new Quit();
		System.out.print("Sending: " + quit);
		s.send(quit);
		System.out.print("Received: " + (QuitAck) s.receive());		
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		
		String setups = args[3];
		//String transports = args[2];		
		
		new Client().run(debug, setups, server, port);
	}
}
