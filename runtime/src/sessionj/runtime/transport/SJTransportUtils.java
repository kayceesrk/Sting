package sessionj.runtime.transport;

import java.util.LinkedList;
import java.util.List;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.http.SJHTTP;
import sessionj.runtime.transport.https.SJHTTPS;
import sessionj.runtime.transport.sharedmem.SJBoundedFifoPair;
import sessionj.runtime.transport.sharedmem.SJFifoPair;
import sessionj.runtime.transport.tcp.SJAsyncManualTCP;
import sessionj.runtime.transport.tcp.SJManualTCP;
import sessionj.runtime.transport.tcp.SJStreamTCP;

public class SJTransportUtils 
{
	public static final char SJ_DEFAULT_TRANSPORTS = 'd';
	public static final char SJ_FIFO_PAIR = 'f';	    
	public static final char SJ_STREAM_TCP = 's';
	public static final char SJ_MANUAL_TCP = 'm';
	public static final char SJ_ASYNCHRONOUS_TCP = 'a';
	public static final char SJ_HTTP = 'h';
	public static final char SJ_BOUNDED_FIFO_PAIR = 'b';
	public static final char SJ_HTTPS = 'p'; // This is sessionj.runtime.transport.https.HTTPS, not sessionj.runtime.transport.http.HTTPS (package "https", not "http").  
	
	private SJTransportUtils() 
	{
	
	}

	// Purpose: for session-specific parameters. Don't want to change system-wide settings.
	public static SJSessionParameters createSJSessionParameters(SJCompatibilityMode mode, String setups, String transports, Class<? extends SJCustomMessageFormatter> cmf) throws SJSessionParametersException, SJIOException 
	{
		return new SJSessionParameters(mode, parseTransportFlags(setups), parseTransportFlags(transports), cmf);
	}
  
	// Purpose: for session-specific parameters. Don't want to change system-wide settings. 
	public static SJSessionParameters createSJSessionParameters(String setups, String transports) throws SJIOException, SJSessionParametersException 
	{
		return new SJSessionParameters(parseTransportFlags(setups), parseTransportFlags(transports));	
	}

	public static SJSessionParameters createSJSessionParameters(String setups, String transports, int boundedBufferSize) throws SJSessionParametersException, SJIOException 
	{		
		return new SJSessionParameters(parseTransportFlags(setups), parseTransportFlags(transports), boundedBufferSize);
	}

	// Purpose: system-wide settings.
	public static void configureTransports(String setups, String transports) throws SJIOException 
	{
		SJTransportManager sjtm = SJRuntime.getTransportManager();	
	
	  sjtm.loadNegotiationTransports(parseTransportFlags(setups));
	  sjtm.loadSessionTransports(parseTransportFlags(transports));
	}
	
	public static List<Class<? extends SJTransport>> parseTransportFlags(String flags) throws SJIOException
	{
		List<Class<? extends SJTransport>> cs = new LinkedList<Class<? extends SJTransport>>();
		
		for (char c : flags.toCharArray()) 
		{
			if (c == SJ_DEFAULT_TRANSPORTS)
			{
				//cs.addAll(parseTransportFlags("fs"));
				//cs.add(parseTransportFlag(SJ_FIFO_PAIR)); // FIXME: seems something is currently wrong with SJFifoPair (probably to due with using files to detect open/closed ports), so disable for now.  
				//cs.add(parseTransportFlag(SJ_STREAM_TCP));
				cs.add(parseTransportFlag(SJ_MANUAL_TCP));
			}
			else
			{
				cs.add(parseTransportFlag(c));
			}
		}
			
		return cs;
	}
	
  // The original intention is that these "letter codes" are not fundamental enough to be directly defined by the SJTransportManager; we define and process their use here. 
  private static Class<? extends SJTransport> parseTransportFlag(char code) throws SJIOException
  { 
    switch (code) 
    { 
	    case SJ_FIFO_PAIR:         return SJFifoPair.class;	    
	    case SJ_STREAM_TCP:        return SJStreamTCP.class;
	    case SJ_MANUAL_TCP:        return SJManualTCP.class;
	    case SJ_ASYNCHRONOUS_TCP:  return SJAsyncManualTCP.class;
	    case SJ_HTTP:              return SJHTTP.class;
	    case SJ_BOUNDED_FIFO_PAIR: return SJBoundedFifoPair.class;
	    case SJ_HTTPS:             return SJHTTPS.class;
    }

    throw new SJIOException("[SJTransportUtils] Unsupported transport flag: " + code);
  } 	
}
