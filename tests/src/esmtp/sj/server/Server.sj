//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ esmtp.sj.server.Server false 8888 

package esmtp.sj.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;
import sessionj.runtime.session.*;

//import esmtp.sj.SJSmtpFormatter;
import esmtp.sj.messages.*;

public class Server
{			
	static protocol smtp_server_rcptOrData
	{
		rec RCPT_OR_DATA
		[
		 	?{
		 		RCPT_TO:
		 			?(EmailAddress)
		 			.!{
		 				$250:
		 					!<RcptAckBody>
		 					.#RCPT_OR_DATA,
		 				$550:		 				
		 					!<RcptAckBody>
		 					.#RCPT_OR_DATA
		 			},
		 		DATA:
		 			!<DataAck>
		 			.?(MessageBody)
		 			.!<MessageBodyAck>
		 	}
		]
	}	
	
	static protocol smtp_server_rcpt
	{
		rec RCPT
		[
		 	?{
		 		RCPT_TO:
		 			?(EmailAddress)
		 			.!{
		 				$250:
		 					!<RcptAckBody>
		 					.@(smtp_server_rcptOrData),
						 	/*.rec RCPT_OR_DATA
							[
							 	?{
							 		RCPT_TO:
							 			?(EmailAddress)
							 			.!{
							 				$250:
							 					!<RcptAckBody>
							 					.#RCPT_OR_DATA,
							 				$550:		 				
							 					!<RcptAckBody>
							 					.#RCPT_OR_DATA
							 			},
							 		DATA:
							 			!<DataAck>
							 			.?(MessageBody)
							 			.!<MessageBodyAck>
							 	}
							],*/
		 				$550:		 				
		 					!<RcptAckBody>
		 					.#RCPT
		 			}/*,
		 		QUIT: // Still do not support "quitting at any time". Would need to change the whole protocol to do so (specifically starting with the outermost recursion).
		 			!<QuitAck>*/
		 	}
		]
	}	
	
	static protocol smtp_server_mail
	{
		rec MAIL
		[
		 	?{
		 		MAIL_FROM:
		 			?(EmailAddress)
		 			.!{
		 				$250:
		 					!<MailAckBody>
		 					.@(smtp_server_rcpt)
		 					.#MAIL,
		 				$550: // For unrouteable. Also 501 (for no domain).
		 					!<MailAckBody>
		 					.#MAIL
		 			},
		 		QUIT:
		 			!<QuitAck>
		 	}
		]
	}	
	
	static protocol smtp_server
	{
		sbegin
		.!<ServerGreeting>				
		.?(Ehlo)
		.!<EhloAck>		
		.@(smtp_server_mail)
	}
		
	public void run(boolean debug, int port) throws Exception
	{
		//final noalias SJServerSocket ss = 
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		
		new Server().run(debug, port);
	}
}
