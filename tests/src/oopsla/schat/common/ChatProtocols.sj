//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/ChatProtocols.sj -d tests/classes/

package oopsla.schat.common;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.events.*;

public class ChatProtocols
{
	public static final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	public static final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	public static final noalias protocol p_ar_private_main { ?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } // From request "Acceptor's" perspective (the Acceptor is the "Client").  
	//public static final noalias protocol p_ar_private { cbegin.?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } 
	
	public static final noalias protocol p_event_out_stream 
	{ 
		![
		  !{ 
		  	USER_JOINED: 
		  		!<UserJoinedEvent>
		  , MESSAGE:
		  		!<MessageEvent>
		  , PRIVATE_CONVERSATION:
		  		!<PrivateConversationEvent>.
		  		?{
		  			ACCEPT: 
		  				!<cbegin.@(p_ar_private_main)>
		  		,	REJECT:
		  		}
		  ,	USER_LEFT:
		  		!<UserLeftEvent>
		  , QUIT:
		  		!<QuitEvent>
		  }
		 ]*
	}
	
	public static final noalias protocol p_event_in_stream { ^(p_event_out_stream) }
	
	public static final noalias protocol p_cs_main { !<String>.?(int).?(cbegin.@(p_event_in_stream)).@(p_event_out_stream) }
	public static final noalias protocol p_sc_main { ^(p_cs_main) }
	
	public static final noalias protocol p_cs_chat { cbegin.@(p_cs_main) }
	public static final noalias protocol p_sc_chat { sbegin.@(p_sc_main) }
	
	public static final noalias protocol p_s_out { sbegin.@(p_event_out_stream) }
}
