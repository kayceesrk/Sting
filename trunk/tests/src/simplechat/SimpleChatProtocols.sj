//$ bin/sessionjc tests/src/simplechat/SimpleChatProtocols.sj -d tests/classes/

package simplechat;

import sessionj.runtime.*;

public class SimpleChatProtocols
{		
	public static final noalias protocol p_client_send { ![!<String>]* }
	public static final noalias protocol p_client_receive { ![?(String)]* }
	
	public static final noalias protocol p_clientToServer { cbegin.!{WRITE: @(p_client_send), READ: @(p_client_receive)} }	
	public static final noalias protocol p_serverToClient { ^(p_clientToServer) }
}
