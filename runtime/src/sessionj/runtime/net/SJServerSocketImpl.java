package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.transport.SJAcceptorThreadGroup;
import sessionj.runtime.SJRuntimeException; //<By MQ>
import java.util.*; //<By MQ>
/**
 * @author Raymond
 *
 * Defines methods which an SJServerSocket needs, and handles parameters etc..
 *
 */
public class SJServerSocketImpl extends SJServerSocket 
{
	//private static final SJRuntime runtime = SJRuntime.getSJRuntime();
    //<By MQ> Added
    private SJSocketGroup participantsList;
    private Hashtable<String, SJSocket> participantsSockets = new Hashtable<String, SJSocket>();
    SJService service; //For requesting Sockets
    int portNumber;
    int hostType;
    //</By MQ>
	private SJAcceptorThreadGroup acceptors;
	
	/*protected SJServerSocketImpl(SJProtocol protocol, int port) throws SJIOException
	{
		super(protocol, port, SJSessionParameters.DEFAULT_PARAMETERS);
	}*/
	
    protected SJServerSocketImpl(SJProtocol protocol, int port, SJSessionParameters params, int hostType) throws SJIOException {
		super(protocol, port, params);
		service = SJService.create(protocol, "localhost", 1000); //the last two parameters are dummy
		participantsList = new SJSocketGroup(this);  //<By MQ>
		portNumber = port;  //<By MQ>
		this.hostType = hostType;
	}

	protected SJServerSocketImpl(SJProtocol protocol, int port, SJSessionParameters params) throws SJIOException {
		super(protocol, port, params);
		service = SJService.create(protocol, "localhost", 1000); //the last two parameters are dummy
		participantsList = new SJSocketGroup(this);  //<By MQ>
		portNumber = port;  //<By MQ>
	}
	
	public SJAcceptingSocket accept() throws SJIOException, SJIncompatibleSessionException
	{
	    //throw new SJIOException("This method should not be used anymore. use accept(String) instead");  //<By MQ>
	    if (isClosed())
		{
			throw new SJIOException("[SJServerSocketImpl] Server socket not open for accept.");
		}
		
		SJAcceptingSocket s = new SJAcceptingSocket(getProtocol(), getParameters());

        // nextConnection() blocks if there is no pending connection request.
		SJRuntime.bindSocket(s, nextConnection());			

		SJRuntime.accept("MQUnknown", s); //<By MQ>
		
		return s;
	}
    //<By MQ>
    public SJSocketGroup accept(String protocolInitiator) throws SJIOException, SJIncompatibleSessionException
    {
	//First, connect to the protocol initiator.
	SJAcceptingSocket s = new SJAcceptingSocket(getProtocol(), getParameters());
	s.participantName = protocolInitiator;
	SJRuntime.bindSocket(s, nextConnection());
	SJRuntime.accept(protocolInitiator, s);
	participantsSockets.put(protocolInitiator, s);
	participantsList.addSocket(protocolInitiator, s);
	//addParticipant(protocolInitiator, "", 0); // add an entry for the protocol initiator. We don't need hostname or port because we already have a connected socket

	//Next, connect to all other participants.
	//Sort the list, so that the connection order is known and no deadlocks are created
	List<SJParticipantInfo> pil = participantsList.getParticipantsList();
	Collections.sort(pil, new Comparator(){
 
		public int compare(Object o1, Object o2) {
		    SJParticipantInfo p1 = (SJParticipantInfo) o1;
		    SJParticipantInfo p2 = (SJParticipantInfo) o2;
		    return p1.participantName.compareTo(p2.participantName);
		}
	    });
	    
	
	for(SJParticipantInfo pi : pil)
	{
	    if(pi.participantName.equals(protocolInitiator) || pi.participantName.equals(participantsList.participantName()))  //We are already connected to the protocol initiator, so just ignore it. Also don't connect to yourself.
		continue;
	    //Whether I connect or wait for connection depends on port number
	    if(portNumber == pi.portNumber)
	       throw new SJIOException("SJServerSocketImpl: Please choose different port numbers for all participants even if they are on different hosts");  //MQTODO
	    else if(portNumber > pi.portNumber)
	    {
		//System.out.println(participantsList.participantName() + ": connecting to " + pi.participantName);
		//System.out.println("Connecting to " + pi.participantName);
		service.addParticipant(pi.participantName, pi.hostName, pi.portNumber);
		SJRequestingSocket s1 = new SJRequestingSocket(service, SJSessionParameters.DEFAULT_PARAMETERS);
		s1.participantName = pi.participantName;
		SJRuntime.connectSocket(s1);
		SJRuntime.request(s1);
		participantsSockets.put(pi.participantName, s1);
		participantsList.addSocket(pi.participantName, s1);
	    }
	    else
	    {
		//System.out.println("Waiting connection from " + pi.participantName);
		//System.out.println(participantsList.participantName() + ": connecting from " + pi.participantName);
		SJAcceptingSocket s1 = new SJAcceptingSocket(getProtocol(), getParameters());
		s1.participantName = pi.participantName;
		SJRuntime.bindSocket(s1, nextConnection());
		SJRuntime.accept(pi.participantName, s1);
		participantsSockets.put(pi.participantName, s1);
		participantsList.addSocket(pi.participantName, s1);
	    }
	}
	return participantsList;
    }

    public void addParticipant(String participantName, String hostName, int portNumber, int hostType)
    {
        participantsList.addByHostName(participantName, hostName, portNumber, hostType);
    }

    public void addParticipant(String participantName, String hostName, int portNumber)
    {
	participantsList.addByHostName(participantName, hostName, portNumber);
    }

    public void participantName(String p)
    {
	participantsList.participantName(p, hostType);
    }

    public void setCostsMap(int[][] costsMap) throws SJRuntimeException
    {
	participantsList.setCostsMap(costsMap);
    }
    //</By MQ>	
	public void close() // throws SJIOException // Due to SJServerSocketCloser (or failure), can be closed early.
	{
		if (isOpen)
		{
			isOpen = false;
					
			if (!acceptors.isClosed())
			{		
				acceptors.close(); //FIXME: doesn't seem to be working... Seems the main acceptor thread isn't finishing?
			}
			
			SJRuntime.closeServerSocket(this);
		}			
	}
	
	protected void init() throws SJIOException
	{
		SJRuntime.openServerSocket(this);		
		
		isOpen = true;
	}
	
	public SJAcceptorThreadGroup getAcceptorGroup()
	{
		return acceptors;
	}
	
	protected void setAcceptorGroup(SJAcceptorThreadGroup acceptors)
	{
		this.acceptors = acceptors;
	}	
}
