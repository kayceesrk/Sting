package sessionj.runtime.net;

import java.io.*;

import java.net.InetSocketAddress;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.transport.*;

import static sessionj.SJConstants.*;

public class SJService implements SJServiceInterface
{
	public final long serialVersionUID = SJ_VERSION;
    
        private SJProtocol protocol;  //<By MQ>
        //private SJServerIdentifier si;  //<By MQ> I have removed this and inserted each SJServerIdentifier in its respected SJParticipantInfo element in participantsList
        //<By MQ> Added 
        private SJSocketGroup participantsList;// = new SJSocketGroup();
        transient private Hashtable<String, SJRequestingSocket> participantsSockets = new Hashtable<String, SJRequestingSocket>();
        int portNumber;  //required only for listening to new connections only in the case of continuations
    int hostType;
        //</By MQ>

	protected SJService() { }
	
    //<By MQ>
    private SJService(SJProtocol protocol, SJServerIdentifier si, int hostType) throws SJIOException
    {
	this.protocol = protocol;
	participantsList = new SJSocketGroup(this);
	portNumber = si.getPort();
	this.hostType = hostType;
    }
    //</By MQ>
	private SJService(SJProtocol protocol, SJServerIdentifier si) throws SJIOException
	{
		this.protocol = protocol;
                participantsList = new SJSocketGroup(this);
		portNumber = si.getPort();
		//this.si = si; //<By MQ> No longer necessary
	}

    public static SJService create(SJProtocol protocol, String hostName, int port, int hostType) throws SJIOException
    {	
	return new SJService(protocol, new SJServerIdentifier(hostName, port), hostType);
    }
	public static SJService create(SJProtocol protocol, String hostName, int port) throws SJIOException
	{ 
		return new SJService(protocol, new SJServerIdentifier(hostName, port)); 
	}

	public static SJService create(SJProtocol protocol, String hostName, SJPort port) throws SJIOException // FIXME: record SJPort properly.
	{ 	    
		return create(protocol, hostName, port.getValue()); 
	}
	
	public SJRequestingSocket request(String participantName) throws SJIOException, SJIncompatibleSessionException
	{
	        return request(SJSessionParameters.DEFAULT_PARAMETERS, participantName); // Use default setups and transports.
	}
		
        public SJRequestingSocket request(SJSessionParameters params, String participantName) throws SJIOException, SJIncompatibleSessionException
	{
	    SJRequestingSocket s = new SJRequestingSocket(this, params); 
	    s.participantName = participantName;
	    SJRuntime.connectSocket(s);
	    SJRuntime.request(s);
	    participantsSockets.put(participantName, s);
	    return s;
	}

    public SJSocketGroup request() throws SJIOException, SJIncompatibleSessionException
	{
	    List<SJParticipantInfo> pil = participantsList.getParticipantsList();
	    for(SJParticipantInfo pi : pil)
	    {
		if(pi.participantName.equals(participantsList.participantName()))   //Don't connect yourself.
		    continue;
		SJRequestingSocket s = new SJRequestingSocket(this, SJSessionParameters.DEFAULT_PARAMETERS); 
		s.participantName = pi.participantName;
		SJRuntime.connectSocket(s);
		SJRuntime.request(s);
		participantsSockets.put(pi.participantName, s);
		participantsList.addSocket(pi.participantName, s);		
	    }
	    return participantsList;
	}

    /*public SJRequestingSocket request()
        {
	    return null;
	    }*/
	public SJProtocol getProtocol()
	{
		return protocol;
	}

        //<By MQ> Updated the following methods to supprt multi-party
	public SJServerIdentifier getServerIdentifier(String participantName)
	{
	        List<SJParticipantInfo> pil = participantsList.getParticipantsList();
		for(SJParticipantInfo pi : pil)
                {
		    if(pi.participantName.equals(participantName))
			return pi.si;
		}
		return null;
	}

	public String toString()
	{
	        String result = getProtocol().toString();
	        List<SJParticipantInfo> pil = participantsList.getParticipantsList();
		for(SJParticipantInfo pi : pil)
                {
		    result += "@" + pi + "   ";
		}
		return result;
	}

    public int getLocalPort()
    {
	return portNumber;
    }
        //</By MQ>
        //<By MQ> Added this API to support the new syntax and to specify to whom we are sending
    public void addParticipant(String participantName, String hostName, int portNumber, int hostType)
    {
	participantsList.addByHostName(participantName, hostName, portNumber, hostType);
    }

        public void addParticipant(String participantName, String hostName, int portNumber)
        {
	    participantsList.addByHostName(participantName, hostName, portNumber);
	}

        public SJRequestingSocket getSocketForParticipant(String participantName)
        {
	    return participantsSockets.get(participantName);
	}

        public void participantName(String p)
        {
	    participantsList.participantName(p, hostType);
	}

        public void setCostsMap(int[][] costsMap) throws SJRuntimeException
        {
	    participantsList.setCostsMap(costsMap);
	}

    public void setParticipantsInfo(List<SJParticipantInfo> pil)
    {
	participantsList.setParticipantsInfo(pil);
    }

        //</By MQ>
}
