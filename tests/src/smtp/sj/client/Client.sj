//$ bin/sessionjc -sourcepath tests/src/smtp/sj/';'tests/src/smtp/sj/messages/';'tests/src/smtp/sj/client/ tests/src/smtp/sj/client/Client.sj -d tests/classes/
//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ smtp.sj.client.Client false smtp.cc.ic.ac.uk 25 

/*openssl s_client -starttls smtp -connect smtp.gmail.com:587
ehlo vm-shell1.doc.ic.ac.uk
AUTH LOGIN...*/

package smtp.sj.client;

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

//import smtp.sj.SJSmtpFormatter; // Doesn't work when specifying -sourcepath (without -cp).
import smtp.sj.*;
import smtp.sj.messages.*;
//import smtp.sj.server.Server;

public class Client
{			
	/*private protocol p_client
	{
		//^(Server.p_server)
		cbegin
		.?(ServerGreeting)
		.!<Helo>.?(HeloAck)
		.!<Mail>.?(MailAck)
		.!<Rcpt>.?{
			$2: 
				?(Rcpt2Ack)
				.!<Data>.?(DataAck)
				.!<MessageBody>.?(MessageBodyAck)
				.!<Quit>.?(QuitAck),
			$5:	
				?(Rcpt5Ack)	
				.!<Quit>.?(QuitAck)		
		}
	}*/
	
	private protocol p_client
	{
		//^(Server.p_server)
		cbegin
		.?(ServerGreeting)
		.!<Helo>.?(HeloAck)
		.!<Mail>.?(MailAck)
		.rec RCPT [
			!{
				RCPT:
					!<RcptTo>
					.?{
						$2: 
							?(Rcpt2Ack)
							.#RCPT,
						$5:	
							?(Rcpt5Ack)	
					},
			  DATA: 
					!<DataLineFeed>.?(DataAck)
					.!<MessageBody>.?(MessageBodyAck)	// Subject would have a "SUBJECT: " prefix, but how about no subject?		
			}
		]
		.!<Quit>.?(QuitAck)		
	}
	
	public void run(boolean debug, String server, int port) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		
		final String fqdn = InetAddress.getLocalHost().getHostName().toString(); //getCanonicalHostName().toString();
		
		System.out.println("fqdn: " + fqdn);
		
		SJSessionParameters sparams = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, new SJSmtpFormatter());
		
		final noalias SJSocket s;	
			
		try (s)
		{
			System.out.println("Requesting SMTP session with: " + server + ":" + port);
			
			s = SJService.create(p_client, server, port).request(sparams);	
			
			System.out.println((ServerGreeting) s.receive());
			
			Helo helo = new Helo(fqdn);
			System.out.print("Sending: " + helo);			
			s.send(helo);			
			System.out.println("Received: " + (HeloAck) s.receive());
			
			System.out.print("Sender's address? (e.g. sender@domain.com): ");			
			Mail mail = new Mail(readUserInput(sc));
			System.out.print("Sending: " + mail);
			s.send(mail);
			System.out.println("Received: " + (MailAck) s.receive());
			
			boolean firstIteration = true;
			boolean anotherRecipient = true;
						
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
								System.out.println("Received: " + "2" + (Rcpt2Ack) s.receive()); // Need to re-add the "2" that was already consumed by the inbranch.
								
								s.recurse(RCPT);								
							}
							case $5:
							{
								System.out.println("Received: " + "5" + (Rcpt5Ack) s.receive());
								
								//doQuit(s); // Currently, delegation within loop contexts completely forbidden, even though this branch does not loop.					
							}
						}					
					}
					
					anotherRecipient = false;
				}
				else
				{
					s.outbranch(DATA)
					{
						DataLineFeed dataLF = new DataLineFeed();
						System.out.print("Sending: DATA" + dataLF); 
						s.send(dataLF);												
						System.out.println("Received: " + (DataAck) s.receive());
						
						System.out.print("Message subject?: ");
						String subject = readUserInput(sc);
						
						System.out.print("Message body? (Enter ends the message.): ");
						String text = readUserInput(sc);
						
						MessageBody body = new MessageBody("SUBJECT:" + subject + "\n\n" + text);				
						System.out.print("Sending: " + body);
						s.send(body);
						System.out.println("Received: " + (MessageBodyAck) s.receive());
						
						//doQuit(s);					
					}
				}
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
		System.out.println("Received: " + (QuitAck) s.receive());		
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		 
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		
		new Client().run(debug, server, port);
	}
}
