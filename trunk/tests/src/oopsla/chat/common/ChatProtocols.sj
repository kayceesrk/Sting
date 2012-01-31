//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/common/ChatProtocols.sj -d tests/classes/

package oopsla.chat.common;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.events.*;

public class ChatProtocols
{
	public static final noalias protocol p_event_out_stream { ![!<ChatEvent>]* }
	public static final noalias protocol p_event_in_stream { ^(p_event_out_stream) }
	
	public static final noalias protocol p_cs_main { !<String>.?(int).?(cbegin.@(p_event_in_stream)).@(p_event_out_stream) }
	public static final noalias protocol p_sc_main { ^(p_cs_main) }
	
	public static final noalias protocol p_cs { cbegin.@(p_cs_main) }
	public static final noalias protocol p_sc { sbegin.@(p_sc_main) }
	
	public static final noalias protocol p_s_out { sbegin.@(p_event_out_stream) }
}
