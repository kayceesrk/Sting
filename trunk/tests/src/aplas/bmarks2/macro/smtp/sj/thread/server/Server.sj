//$ bin/sessionjc -cp tests/classes tests/src/ecoop/bmarks2/macro/smtp/sj/thread/server/Server.sj -d tests/classes
//$ bin/sessionj -cp tests/classes ecoop.bmarks2.macro.smtp.sj.thread.server.Server false 8888 s

package ecoop.bmarks2.macro.smtp.sj.thread.server;

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
	volatile protected boolean kill = false;
	volatile private boolean finished = false;
	  
	private SJServerSocketCloser ssc;	
	
	private String setups;
	
	public Server(boolean debug, int port, String setups)
	{
		super(debug, port);
		
		this.setups = setups;
	}
	
	public void run() throws Exception
	{
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, setups, setups, SmtpServerFormatter.class);
		
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocket.create(smtp_server, getPort(), params);

			this.ssc = ss.getCloser();
			
			debugPrintln("[Server] Listening on: " + getPort());			
			
			boolean debug = isDebug();
			
			for (int i = 0; true; i++)
			{			
				noalias SJSocket s;			
				
				try (s)
				{
					s = ss.accept();
					
					addClient(); // OK to "register" the Client before the thread actually started since we're not supposed to expect any failure for the purposes of this benchmark. 
					
					s.spawn(new ServerThread(debug, this, i));					
				}
				finally
				{
					
				}
			}
		}	
		catch (SJIOException ioe) // Server socket was closed (hopefully by us).
		{
			//ioe.printStackTrace();
		}		
		finally 
		{
			this.finished = true;
		}
	}

	public void kill() throws Exception
	{
		int numClients = getNumClients(); // Unlike java.thread.Server, this will only tell us the number of LoadClients currently connected, not the number of TimerClients that had also previously connected. That's alright, the TimerClient (an individual) has already finished by now.
  	
  	this.kill = true; // It's important that no more clients are trying to connect after this point.
  	
  	while (getNumClients() > 0); 	
		
  	this.ssc.close(); // Break the accepting loop.
		
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

class ServerThread extends SJThread
{
	private boolean debug;
	
	private Server server;
	private int tid;
	
	public ServerThread(boolean debug, Server server, int tid)
	{
		this.debug = debug;
		this.server = server;
		this.tid = tid;
	}
	
	public void srun(noalias @(Server.smtp_server_body) s)
	{
		try (s)
		{
			//220 smtp1.cc.ic.ac.uk ESMTP Exim 4.69 Sun, 22 Nov 2009 14:36:55 +0000
			ServerGreeting greeting = new ServerGreeting("localhost ESMTP blah blah blah");
			debugPrintln("Sending: " + greeting);			
			s.send(greeting);			
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
			
			doMainLoop(s);			
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	private final void doMainLoop(final noalias @(Server.smtp_server_loop) s) throws SJIOException, ClassNotFoundException
	{
		s.recursion(LOOP)
		{
			s.inbranch()
			{
				case MAIL_FROM:
				{
					doMailFrom(s);								
					s.recurse(LOOP);													
				}
				case RCPT_TO:
				{
					doRcptTo(s);
					s.recurse(LOOP);													
				}
				case DATA:
				{
					doData(s);	
					s.recurse(LOOP);													
				}
				case QUIT:
				{
					//221 smtp1.cc.ic.ac.uk closing connection
					QuitAck quitAck = new QuitAck("localhost closing connection"); // Unlike the "ack bodies", already prefixes the reply code.
					debugPrintln("Sending: " + quitAck);			
					s.send(quitAck);	
					
					server.removeClient(); // Maybe better to move to an outer finally block. But this benchmark is *not* supposed to be tolerant to failure.
  				
 					debugPrintln("[ServerThread] Clients remaning: " + server.getNumClients());
				}
			}
		}
	}
	
	private final void doMailFrom(final noalias @(Server.smtp_server_mail) s) throws SJIOException, ClassNotFoundException
	{
    EmailAddress email = (EmailAddress) s.receive();
		debugPrintln("Received: " + email);
		
		//250 OK
		s.outbranch($250)
		{
		
			MailAckBody mailAckBody;
			
			if (server.kill)
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
	}
	
	private final void doRcptTo(final noalias @(Server.smtp_server_rcpt) s) throws SJIOException, ClassNotFoundException
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
	}
	
	private final void doData(final noalias @(Server.smtp_server_data) s) throws SJIOException, ClassNotFoundException
	{	
		//354 Enter message, ending with "." on a line by itself
		DataAck dataAck = new DataAck("Enter message, ending with \".\" on a line by itself"); // Unlike the "ack bodies", already prefixes the reply code.
		debugPrintln("Sending: " + dataAck);			
		s.send(dataAck);	
				
		MessageBody body = (MessageBody) s.receive();
		debugPrintln("Received: " + body);
		
		//250 OK id=1NCDaj-0001P0-V7
		MessageBodyAck messageBodyAck = new MessageBodyAck("OK id=1ABCde-2345F6-G7");
		debugPrintln("Sending: " + messageBodyAck);			
		s.send(messageBodyAck);
		
		if (server.isCounting()) 
    {
      server.incrementCount(tid);
      
      debugPrintln("[ServerThread] Current count:" + server.getCountTotal());
    }
	}
	
	private void debugPrintln(String m)
	{
		server.debugPrintln(m);
	}
}
