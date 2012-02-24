//<By MQ> Added
package sessionj.runtime.net;
import java.util.List;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;
import sessionj.runtime.*;
import sessionj.runtime.session.SJStateManager;
import sessionj.runtime.transport.SJConnection;
import sessionj.types.sesstypes.SJSessionType;
import java.util.*; //<By MQ>
import sessionj.runtime.session.SJChainedObject; //<By MQ>
import sessionj.runtime.session.SJCloseObject; //<By MQ>
import sessionj.runtime.session.SJControlSignal; //<By MQ>
import org.apache.commons.javaflow.Continuation; //<By MQ>
import java.io.Serializable; //<By MQ>

class SJParticipantInfoComparator implements Comparator<SJParticipantInfo>
{
    @Override public int compare(SJParticipantInfo o1, SJParticipantInfo o2)
    {
	return o1.participantName.compareTo(o2.participantName);
    }
}

class PathElement
{
    public String participantName;
    public int cost;

    public PathElement(String p, int c)
    {
	participantName = p;
	cost = c;
    }

    @Override public String toString()
    {
	return participantName + "(" + cost + ")";
    }
}
public class SJSocketGroup implements SJSocket, Serializable
{
    protected List<SJParticipantInfo> participantsList =  new ArrayList<SJParticipantInfo>();
    public transient Hashtable<String, SJSocket> participantsSockets = new Hashtable<String, SJSocket>();
    private String participantName;
    private int closeRequests = 0;
    private int[][] costsMap;
    private transient Hashtable<String, String> exportedContinuations = new Hashtable<String, String>();  //The exported continuations (source, destination)
    public boolean isContinuationObject = false;
    private SJProtocol protocol;  //Will need protocol and portNumber in order to listen for sockets in case of continuations
    private int portNumber;
    SJServerSocket ss;
    private transient boolean isReconnecting = false;
    private transient byte[] tempUnflushedBuffer; //This buffer is used to save the unflushed batch when a continuation is made
    public boolean continuationEnabled = false;
    private static transient String exportTo = null;
    private static transient SJSocketGroup continuationSocket = null;
    
    SJSocketGroup(SJServerSocketImpl p) throws SJIOException
    {
	//super(null, null, null);
	participantsList = new LinkedList<SJParticipantInfo>();
	protocol = p.getProtocol();
	portNumber = p.getLocalPort();
	ss = p;
    }

    SJSocketGroup(SJService p) throws SJIOException
    {
        //super(null, null, null);
        protocol = p.getProtocol();
	portNumber = p.getLocalPort();
    }

    public void socketGroup(SJSocketGroup sg)
    {
    }

    public SJSocketObject getSocketObject()
    {
	return new SJSocketObject(this);
    }

    public static void executeExportable(Runnable r) throws SJIOException
    {
	Continuation c = Continuation.startWith(r);
	if(c != null)
	{
	    exportContinuation(c, exportTo);
	}
    }

    public static void exportContinuation(Continuation c, String p) throws SJIOException
    {
        //First, broadcast a notification for the intended continuation
	{
	    SJContinuationNotification cn = new SJContinuationNotification(continuationSocket.participantName, p);
	    Enumeration keys = continuationSocket.participantsSockets.keys();
	    while(keys.hasMoreElements())
	    {
		String key = (String)keys.nextElement();
		//if(key.equals(p) == false)  //Send the notification to everyone except for the executer, it will get a continuation object that holds the same information
		{
		    continuationSocket.pass(key, cn);
		}
	    }
	}	 
	//Then, send the continuation object to its destination
	SJContinuationObject co = new SJContinuationObject(c, p);
	debug("exporting to " + continuationSocket.participantsSockets.size());
	continuationSocket.pass(p, co);
	continuationSocket.flush();
    }
    
    public String getContinuationParticipant(String p)
    {
	return p;
	/*if(isContinuationObject)
	    return null;
	String p2 = exportedContinuations.get(p);
	if(p2 == null)
	    return p;
	    return p2;*/
    }

    public void setCostsMap(int[][] costsMap) throws SJRuntimeException
    {	
	//Next sort participantsList because we assume that the distances are assigned for participants in a sorted manner
	debug("participants:" + participantsList.size() + ", costsMap: " + costsMap.length);
	if(costsMap == null || costsMap[0] == null)
	    throw new SJRuntimeException("[SJSocketGroup]: costsMap can't be null");
	else if(costsMap.length != costsMap[0].length)
	    throw new SJRuntimeException("[SJSocketGroup]: A square array is expected.");
	else if(costsMap.length != participantsList.size())
	{
	    debug(participantName + participantsList);	    
	    throw new SJRuntimeException("[SJSocketGroup]: Array size must match the number of participants");
	}

         this.costsMap = costsMap;
	//order participantsList
	Collections.sort(participantsList, new SJParticipantInfoComparator());

	//MQTODO: TEMP TEMP TEMP
	/*participantsList.clear();
	participantsList.add(new SJParticipantInfo("client1", "", 0));
	participantsList.add(new SJParticipantInfo("client2", "", 0));
	participantsList.add(new SJParticipantInfo("client3", "", 0));
	participantsList.add(new SJParticipantInfo("client4", "", 0));

	int[][] temp =  { {0, 1, 3, 2},
			  {1, 0, 1, 2},
			  {3, 1, 0, 1},
			  {2, 2, 1, 0} };
	this.costsMap = temp;

	System.out.println(participantName + ": path to client1: " + listToString(getShortestPath(participantName, "client1", null)));
	System.out.println(participantName + ": path to client2: " + listToString(getShortestPath(participantName, "client2", null)));
	System.out.println(participantName + ": path to client3: " + listToString(getShortestPath(participantName, "client3", null)));
	System.out.println(participantName + ": path to client4: " + listToString(getShortestPath(participantName, "client4", null)));
	System.out.println(participantName + ": path to client1: " + listToString(getShortestPath("client2", "client1", null)));
	System.out.println(participantName + ": path to client2: " + listToString(getShortestPath("client2", "client2", null)));
	System.out.println(participantName + ": path to client3: " + listToString(getShortestPath("client2", "client3", null)));
	System.out.println(participantName + ": path to client4: " + listToString(getShortestPath("client2", "client4", null)));
	System.out.println(participantName + ": path to client1: " + listToString(getShortestPath("client3", "client1", null)));
	System.out.println(participantName + ": path to client2: " + listToString(getShortestPath("client3", "client2", null)));
	System.out.println(participantName + ": path to client3: " + listToString(getShortestPath("client3", "client3", null)));
	System.out.println(participantName + ": path to client4: " + listToString(getShortestPath("client3", "client4", null)));
	System.out.println(participantName + ": path to client1: " + listToString(getShortestPath("client4", "client1", null)));
	System.out.println(participantName + ": path to client2: " + listToString(getShortestPath("client4", "client2", null)));
	System.out.println(participantName + ": path to client3: " + listToString(getShortestPath("client4", "client3", null)));
	System.out.println(participantName + ": path to client4: " + listToString(getShortestPath("client4", "client4", null)));
	System.exit(0);*/
    }

    private List<PathElement> getShortestPath(String src, String dest, List<PathElement> path)
    {	
	//System.out.println("getShortestPath from " + src + " to " + dest);
	if(path == null) //path not built yet
	{
	    path = new LinkedList<PathElement>();
	    path.add(new PathElement(src, 0));
	}

	if(src.equals(dest))
	    return null;

	int minCost = -1;
	String minParticipant = null;
	int minPathLength = -1;  //if two paths have the same cost, we chose the shorter one (less hosts)
	List<PathElement> shortestSubPath = null;
	for(SJParticipantInfo pi : participantsList)
	{
	    if(pathContains(path, pi.participantName) == false && pi.participantName.equals(src) == false) //if this pi is not already included in the path, then include it (this prevents loops).
	    {
		List<PathElement> pathCopy = addToPath(path, pi.participantName, getCost(src, pi.participantName));
		List<PathElement> thisPath = getShortestPath(pi.participantName, dest, pathCopy);
		//System.out.println("path: " + path + ", pathCopy: " + pathCopy + ", thisPath: " + thisPath);
		int thisPathCost = totalCost(thisPath) + totalCost(pathCopy);
		//System.out.println("path cost from " + src + " to " + dest + ": " + thisPathCost);
		if((thisPathCost < minCost || minCost == -1) || (thisPath != null && thisPathCost == minCost && thisPath.size() < minPathLength))
		{
		    shortestSubPath = thisPath;
		    minCost = thisPathCost;
		    minParticipant = pi.participantName;
		}
	    }
	}
	if(minParticipant == null)
	    return path;
	//System.out.println("minParticipant: " + minParticipant + ", src: " + src);
	if(shortestSubPath == null)
	    shortestSubPath = new LinkedList<PathElement>();
	shortestSubPath.add(new PathElement(minParticipant, getCost(src, minParticipant)));
	return shortestSubPath;
    }
    
    private String listToString(List l)
    {
	if(l == null)
	    return "[]";
	String result = "size:" + l.size() + "[";
	for(Object o :  l)
	{
	    result += o + ", ";
	}
	return result + "]";	
    }

    private boolean pathContains(List<PathElement> path, String p)
    {
	for(PathElement pe : path)
	{
	    if(p.equals(pe.participantName))
		return true;
	}
	return false;
    }

    private List<PathElement> addToPath(List<PathElement> path, String p, int cost)
    {
	List<PathElement> result = new LinkedList<PathElement>();
	if(path != null)	//copy into the new list
	{
	    for(PathElement pe : path)
		result.add(pe);
	}
	result.add(new PathElement(p, cost));
	return result;
    }

    private int totalCost(List<PathElement> path)
    {
	if(path == null)
	    return 0;
	int cost = 0;
	for(PathElement pe : path)
	    cost += pe.cost;

	return cost;
    }

    private int getCost(String src, String dest)
    {
	int srcIndex = indexOfParticipant(src);
	int destIndex = indexOfParticipant(dest);
	//System.out.println(participantsList.get(0));
	//printList(costsMap);
	//System.out.println("costsMap:" + costsMap + ", src, dest: " + srcIndex + "   " + destIndex);
	return costsMap[srcIndex][destIndex];
    }

    private void setCost(String src, String dest, int cost)
    {
	int srcIndex = indexOfParticipant(src);
        int destIndex = indexOfParticipant(dest);
	costsMap[srcIndex][destIndex] = cost;
    }

    private int indexOfParticipant(String p)
    {
	for(int i = 0; i < participantsList.size(); i++)
	{
	    if(p.equals(participantsList.get(i).participantName))
		return i;
	}
	return -1;
    }
    
    public void addSocket(String p, SJSocket s)
    {
	s.socketGroup(this);
	participantsSockets.put(p, s);
    }

    public SJSocketGroup socketGroup()
    {
        return this;
    }

    public void participantName(String p, int hostType)
    {
	participantName = p;
	participantsList.add(new SJParticipantInfo(p, "localhost", 0, hostType)); //This addes THIS participant to the participants List
    }

    public String participantName()
    {
	return participantName;
    }

    public Object peekObject() throws SJIOException
    {
	throw new SJIOException("SJSocketGroup: This operation is not supported.");
    }

    private static void debug(String msg)
    {
	//System.out.println(msg);
    }

    private void printList(int[][] l)
    {
	String r = "[";
	for(int i=0; i< l.length; i++)
	{
	    for(int j=0; j<l[i].length; j++)
		r += l[i][j] + ", ";
	}
	r += "]";
	debug(r);
    }

    public void handleSJContinuationNotification(final SJContinuationNotification cn) throws SJIOException
    {
	debug(participantName + " received SJContinuationNotification");
	if(exportedContinuations.get(cn.from) != null)
	    throw new SJRuntimeException("[SJSocketGroup]: This participant has already exported a continuation.");
	
	if(protocol == null || portNumber == 0)
	{
	    throw new SJIOException("[SJSocketGroup]: Unable to create SJAcceptingSocket for a continuing participant");
	}
	isReconnecting = true;
	//tempUnflushedBuffer = getSocket(cn.from).getUnflushedBytes();
	try
	{
	    debug(participantName + ": waiting connection from continuing participant " + cn.from);
	    SJAcceptingSocket s1;
	    if(this.ss != null)
	    {
		if(cn.to.equals(participantName))//if this continuation is sent to me,then run accepting socket in a different thread and block until your receive continuation
		{		    
		    final SJServerSocketImpl ss2 = (SJServerSocketImpl)ss;
		    final SJSocketGroup sg = this;
		    final String pName = cn.from;
		    Thread acceptingThread = new Thread() {
                            public void run() {
				try
				{
				    sg.addContinuationSocket(pName, ss2.accept());
				}
				catch(Exception ex)
				{
				    System.out.println(ex);
				    ex.printStackTrace();
				}
				
                            }
                        };
		    SJSocket oldLocation = getSocket(cn.from);
		    acceptingThread.start();
		    debug("waiting for continuation object...");
		    SJContinuationObject co = oldLocation.expectContinuation();		    
                    handleSJContinuationObject(co);		    
		}
		else
		{
		    s1 = ((SJServerSocketImpl)ss).accept();
		    addContinuationSocket(cn.from, s1);
		}
	    }
	    else
	    {
		ss = SJServerSocketImpl.create(protocol, portNumber);
		s1 = new SJAcceptingSocket(ss.getProtocol(), ss.getParameters());
		s1.participantName = cn.from;
		SJRuntime.bindSocket(s1, ss.nextConnection());
		if(cn.to.equals(participantName)) //if this continuation is sent to me,then run accepting socket in a different thread and block until your receive continuation
		{
		    final SJAcceptingSocket s2 = s1;
		    final SJSocketGroup sg = this;
		    final String pName = cn.from;
		    Thread acceptingThread = new Thread() {
			    public void run() {
				try
				{
				    SJRuntime.accept(cn.from, s2);
				    sg.addContinuationSocket(pName, s2);
				}
                                catch(Exception ex)
				{
				    System.out.println(ex);
				    ex.printStackTrace();
				}
			    }
			};
                    SJSocket oldLocation = getSocket(cn.from);   
		    acceptingThread.start();
		    debug("waiting for continuation object...");
		    SJContinuationObject co = oldLocation.expectContinuation();
		    handleSJContinuationObject(co);
		}
		else
		{
		    SJRuntime.accept(cn.from, s1);
		    addContinuationSocket(cn.from, s1);
		}
	    }
	    //Now we modify the costsMap to reflect the new network configuration
	    if(costsMap != null)
	    {
		setCost(cn.from, participantName, getCost(cn.to, participantName));
		setCost(participantName, cn.from, getCost(participantName, cn.to));
	    }
	}
	catch(SJIncompatibleSessionException ex)
	{
	    throw new SJIOException(ex.getMessage());
	}
    }

    private void addContinuationSocket(String p, SJSocket s)
    {
	//s.setUnflushedBytes(tempUnflushedBuffer);
	addSocket(p, s);
    }

    public void setParticipantsInfo(List<SJParticipantInfo> pil)
    {
        participantsList = pil;
    }

    public void reconnectContinuation() throws SJIOException
    {
	debug(participantName + "reconnecting...");
	participantsSockets = new Hashtable<String, SJSocket>();
	protocol.rebuildProtocol();
	SJService service = SJService.create(protocol, "localhost", portNumber);
	service.setParticipantsInfo(participantsList);
	try
	{
	    for(SJParticipantInfo pi : participantsList)
	    {
		if(pi.participantName.equals(participantName))   //Don't connect yourself.
		    continue;

		debug(participantName + ": connecting to " + pi.participantName);
		SJRequestingSocket s = new SJRequestingSocket(service, SJSessionParameters.DEFAULT_PARAMETERS);
		s.participantName = pi.participantName;
		SJRuntime.connectSocket(s);
		SJRuntime.request(s);	    
		addSocket(pi.participantName, s);
		debug(participantName + ": successfully connected to " + pi.participantName);
	    }
	}
	catch(SJIncompatibleSessionException ex)
	{
	    throw new SJIOException(ex.getMessage());
	}
	//Now modify the costsMap to reflect the new network configuration
	/*if(costsMap != null)
	{
	    for(int i = 0; i < costsMap.length; i++)
	    {
		int srcIndex = indexOfParticipant(participantName);
		int destIndex = indexOfParticipant(co.to);
		costsMap[srcIndex][i] = costsMap[destIndex][i];
		costsMap[i][srcIndex] = costsMap[i][destIndex];
	    }
	    }*/
    }

    //if a receive() fails, maybe this is because the participant has moved to another location? so we might need to try receiving from the other location!
    private boolean isContinuationFailure(String p, SJSocket s)
    {
	if(isReconnecting) // if we are currently reconnecting a continuation object, then this is not a continuation failure
	{
	    isReconnecting = false;
	    return true;
	}
	else
	    return false;
    }

    public void handleSJContinuationObject(final SJContinuationObject co)
    {
	debug(participantName + ": received and executing continuation Object.");
	Thread continuationThread = new Thread() {
                public void run() {
		    Continuation.continueWith(co.continuation);
                }
            };	
	continuationThread.start();
    }

    private void handleChaining()
    {

	if(costsMap == null || isContinuationObject)
	{
	    debug(participantName + ": Exiting handleChaining()");
	    return;
	}
	try
	{
	Enumeration keys = participantsSockets.keys();
	while(keys.hasMoreElements())
	{
	    Object key = keys.nextElement();
	    SJSocket s = participantsSockets.get(key);
	    while(true)
	    {
		Object obj = null;
		try
		{
		    obj = s.peekObject();		    
		}
		catch(SJControlSignal cs)
		{
		    if(cs instanceof SJChainedObject)
		    {
			handleSJChainedObject((SJChainedObject) cs);
		    }
		    else if(cs instanceof SJCloseObject)
		    {
			handleSJCloseObject((SJCloseObject) cs);
		    }
		    else if(cs instanceof SJContinuationNotification)
		    {
			handleSJContinuationNotification((SJContinuationNotification)cs);
		    }
		    else if(cs instanceof SJContinuationObject)
		    {
			handleSJContinuationObject((SJContinuationObject) cs);
		    }
		    else
		    {
			//throw new SJIOException("[SJSocketGroup]: Unknown control signal was caught. " + cs);
			closeRequests = 99999;			
		    }
		}
		catch(Exception ex)
		{
		    debug("SJSocketGroup: " + ex);
		    break;
		}
		if(obj == null)
		    break;
		else
		    debug(participantName + ": peekObject(): " + obj + ", type: " + obj.getClass());

		if(obj instanceof SJChainedObject)
		{
		    //MQTODO: more reliable if we check that this belongs to ME instead of "not any other in the list"
		    obj = s.receive();
		    handleSJChainedObject((String)key, (SJChainedObject)obj);
		}
		else
		    break;
	    }
	}
	}
	catch(Exception e) 
	{
	    debug("SJSocketGroup: " + e);
	    e.printStackTrace();
	}
	debug(participantName + ": Exiting handleChaining()");
    }

    public void handleSJChainedObject(SJChainedObject obj) throws SJIOException
    {
	//MQTODO: more reliable if we check that this belongs to ME instead of "not any other in the list"
	//System.out.println(participantName + ": handleSJChainedObject()");
	SJSocket sock = participantsSockets.get(((SJChainedObject)obj).participantName());
	if(sock != null)
	{
	    debug("Chaining to " + ((SJChainedObject)obj).participantName());
	    sock.send(((SJChainedObject)obj).obj());
	}
    }

    public void handleSJCloseObject(SJCloseObject obj)
    {
	debug(participantName + ": received close request, Total: " + ++closeRequests);
    }

    private void handleSJChainedObject(String s, SJChainedObject obj) throws SJIOException
    {
	handleSJChainedObject(obj);
    }

    public void handleSJCloseObject(String s, SJCloseObject obj)
    {
	handleSJCloseObject(obj);
    }

    //Because of chaining, sockets are not permitted to close simply because they've finished their protocol operations. They must wait for all other
    //sockets to finish in order to close. This function receives notifications from participants that have finished.
    public boolean handleCloseRequests()
    {
	debug(participantName + ": entering handleCloseRequests()");
	try
	{
	    Enumeration keys = participantsSockets.keys();
	    while(keys.hasMoreElements())
	    {
		Object key = keys.nextElement();
		SJSocket s = participantsSockets.get(key);
		Object obj = null;
                try
		{
		    obj = s.peekObject();
		}
		catch(sessionj.runtime.session.SJFIN ex)
		{
		    return true;   //MQTODO: this should be the job of SJSessionProtocolsImpl
		}
                catch(Exception ex)
		{
		    debug("SJSocketGroup: " + ex);
		    break;
		}                
		if(obj instanceof SJCloseObject)
		{		    
		    obj = s.receive();
		    handleSJCloseObject((String)key, (SJCloseObject)obj);
		}
	    debug(participantName + ": exiting handleCloseRequests()");                
	    }
	}
        catch(Exception e)
	{
	    debug("[SJSocketGroup].handleCloseRequests(): " + e);
	    e.printStackTrace();
	}
	return false;
    }

    private String getShortestPathTo(String p)
    {
	p = getContinuationParticipant(p);
	if(costsMap == null)
	    return p;
	List<PathElement> shortestPath = getShortestPath(participantName, p, null);
	return shortestPath.get(shortestPath.size()-1).participantName;

	/*Enumeration keys = participantsSockets.keys();
	String[] strKeys = new String[participantsSockets.size()];
	int i = 0;
	while(keys.hasMoreElements())
	{
	    strKeys[i++] = (String) keys.nextElement();
	}
	Arrays.sort(strKeys);
	for(int j = 0; j < strKeys.length; j++)
	{
	    if(p.compareTo(strKeys[j]) > 0)
	    {
		System.out.println("shortest path from " + participantName + " to " + p + ": " + shortestPath.get(shortestPath.size()-1) + ". " + strKeys[j]);
		return strKeys[j];
	    }
	}
	return null;*/
    }

    private String getShortestPathFrom(String p)
    {
	p = getContinuationParticipant(p);
	if(costsMap == null)
	    return p;
	List<PathElement> shortestPath = getShortestPath(p, participantName, null);
	if(shortestPath.size() < 2)
	    return p;
	else
	    return shortestPath.get(shortestPath.size()-1).participantName;
	       
    /*System.out.println(participantName + ": shortest from " + p + " to " + participantName + "is " + shortestPath + ". element: " + shortestPath.get(shortestPath.size()-1).participantName);
        //System.out.println(participantName + "short: " + shortestPath.get(shortestPath.size()-1).participantName);
	if(participantName.equals("client1"))
        {
	    return null;
	}	   
	else if(participantName.equals("client2"))
	{
	    return null;
	}
	else
	{
	    if(p.equals("client1"))
		return "client2";
	    else
		return null;
	}
        /*Enumeration keys = participantsSockets.keys();
        String[] strKeys = new String[participantsSockets.size()];
        int i = 0;
        while(keys.hasMoreElements())
	{
	    strKeys[i++] = (String) keys.nextElement();
	}
        Arrays.sort(strKeys, Collections.reverseOrder());
        for(int j = 0; j < strKeys.length; j++)
	{
	    if(p.compareTo(strKeys[j]) < 0)
	    {
		debug(participantName + ": array: " + strKeys + ", chosen: " + strKeys[j]);
		return strKeys[j];
	    }
	}
        return null;*/
    }

    public SJSocket getSocket(String p)
    {
	return participantsSockets.get(p);
    }
    public void clear()
    {
	participantsList.clear();
    }
    public void addByHostName(String participantName, String hostName, int portNumber)
    {
	participantsList.add(new SJParticipantInfo(participantName, hostName, portNumber));
    }

    public void addByHostName(String participantName, String hostName, int portNumber, int hostType)
    {
	participantsList.add(new SJParticipantInfo(participantName, hostName, portNumber, hostType));
    }

    public List<SJParticipantInfo> getParticipantsList()
    {
	return participantsList;
    }
    public void send(String p, Object o) throws SJIOException
    {
	handleChaining();
	debug(participantName + ": Sending to " + p);
	SJChainedObject co = new SJChainedObject(p, o);
	String nextDestination = getShortestPathTo(p); //The next participant in the shortest path from this to p
	if(nextDestination == null)
	    nextDestination = p;
	else
	    debug(participantName + ": Send target chained from " + p + " to " + nextDestination);
	getSocket(nextDestination).send(o);
    }
    public Object receive(String p) throws SJIOException, ClassNotFoundException
    {
	//flush();
	//handleChaining();
	//handleCloseRequests();
	debug(participantName + ": receiving from " + p);
	String chainedSource = getShortestPathFrom(p);
	if(chainedSource == null)
	    chainedSource = p;
	else
	    debug(participantName + ": receive target chained from " + p + " to " + chainedSource);
	debug(participantName + ": receiving from " + p);
	Object obj = null;
	while(true)      //loop until you receive an object that is not SJChainedObject nor SJCloseObject
	{
	    obj = getSocket(chainedSource).receive();
	    if(obj instanceof SJChainedObject)
	    {
		//MQTODO: more reliable if we check that this msg belongs to me.
		if(getSocket(((SJChainedObject)obj).participantName()) != null)
		{
		    handleSJChainedObject(p, (SJChainedObject)obj);
		}
		else
		    obj = ((SJChainedObject)obj).obj();  //remove encapsulating class
	    }
	    else if(obj instanceof SJCloseObject)
	    {
		handleSJCloseObject(p, (SJCloseObject)obj);
	    }
	    else
		break;
	}
	debug(participantName + ": receieved from " + p + ", object: " + obj);
	return obj;
    }
    public Integer receiveInt(String p) throws SJIOException
    {
	return getSocket(p).receiveInt();
    }
    public void sendInt(String p, Integer i) throws SJIOException
    {
        getSocket(p).sendInt(i);
    }
    public void sendBoolean(String p, Boolean b) throws SJIOException
    {
        getSocket(p).sendBoolean(b);
    }
    public void sendDouble(String p, Double d) throws SJIOException
    {
        getSocket(p).sendDouble(d);
    }
    public void pass(String p, Object o) throws SJIOException
    {
	handleChaining();
	debug(participantName + ": Passing to " + p);
	String nextDestination = getShortestPathTo(p); //The next participant in the shortest path from this to p
	if(nextDestination == null || nextDestination.equals(p))
	{
	    getSocket(p).pass(o);
	}
        else
	{
	    getSocket(nextDestination).pass(new SJChainedObject(p, o));
	    debug(participantName + ": Pass target chained from " + p + " to " + nextDestination);
	}
    }
    public void copy(String p, Object o) throws SJIOException
    {
        getSocket(p).copy(o);
    }

    public void flush() throws SJIOException //MQTODO: flush should be as per socket independently
    {
	handleChaining();
	Enumeration keys = participantsSockets.keys();
        while(keys.hasMoreElements())
	{
	    SJSocket s = participantsSockets.get(keys.nextElement());
	    s.flush();
	}
    }

    public void flush(String p) throws SJIOException
    {
	SJSocket s = participantsSockets.get(p);
	if(s == null)
	    throw new SJIOException("Socket for participant " + p + " was not found.");
	s.flush();
    }
    
    void close(String p)
    {
    }

    public void close() throws RuntimeException 
    {
	if(costsMap == null) //no chaining, then closing connections immediately is safe
	{
	    try
	    {
		debug(participantName + ": closing connections...");
		flush();
		Enumeration keys = participantsSockets.keys();
		while(keys.hasMoreElements())
		    {
			Object key = keys.nextElement();
			SJSocket s = participantsSockets.get(key);
			s.close();
		    }
	    }
	    catch(Exception ex)
	    {
		System.out.println("[SJSocketGroup]: " + ex);
	    }
	    return;
	}

	Enumeration keys = participantsSockets.keys();
	SJCloseObject co = new SJCloseObject();
	debug(participantName + ": sending close requests.");
        while(keys.hasMoreElements())
	{
	    String key = (String)keys.nextElement();
	    if(key.equals(participantName))
		continue;
	    SJSocket s = participantsSockets.get(key);
	    try
	    {
		s.pass(co);
	    }
	    catch(SJIOException ex) 
	    {
		debug("[SJSocketGroup].close(): " + ex);
	    }
	}
	try
	{
	    flush();
	
	}
	catch(SJIOException ex){}
	int i = 0;

	while(true)
	{
	    handleChaining();
	    if(handleCloseRequests())
		break;
	    debug("Close Requests: " + closeRequests);
	    if(closeRequests >= participantsSockets.size())
	    {
		debug(participantName + ": Received close requests from all participants.");
		break;
	    }
	    if(i++ % 10 == 0)
	    {
		i = 0;
		try
		{
		    Thread.currentThread().sleep(1000);
		}
		catch(Exception e){}
	    }
	}
	debug(participantName + ": closing connection.");
	//closing all connections

	keys = participantsSockets.keys();
	while(keys.hasMoreElements())
	{
	    Object key = keys.nextElement();
	    SJSocket s = participantsSockets.get(key);
	    s.close();
	}

    }

    Boolean receiveBoolean(String p) throws SJIOException
    {
	return getSocket(p).receiveBoolean();
    }
    Double receiveDouble(String p) throws SJIOException
    {
	return getSocket(p).receiveDouble();
    }

    // Session handling.
    void outlabel(String p, String lab) throws SJIOException
    {
	getSocket(p).outlabel(lab); //MQTODO
    }
    String inlabel(String p) throws SJIOException
    {
	SJSocket s = getSocket(p);
	String result;
	try
	{
	    result = s.inlabel();
	}
	catch(Exception ex)
	{
	    if(isContinuationFailure(p, s))
	    {
		debug(participantName + ": Operation failure because participant has changed location, waiting for reconnection...");
		while(s == getSocket(p)); //wait until the socket object is replaced by a new one, so we know that we are connected to the new location.
		result = getSocket(p).inlabel(); //retry
	    }
	    else
		throw new SJIOException(ex.getMessage());
	}
	    
	return result;
    }
    public boolean outsync(boolean condition) throws SJIOException
    {
	debug(participantName + ": outsync()");
	Enumeration keys = participantsSockets.keys();
	boolean result = true;
        while(keys.hasMoreElements())
	{
	    SJSocket s = participantsSockets.get(keys.nextElement());
	    result &= s.outsync(condition);
	}
        return result;
    }
    boolean insync(String p) throws SJIOException
    {
	boolean result;
	debug(participantName + ": entering insync()");
	result = getSocket(p).insync();
	debug(participantName + ": exiting insync()");
	return result;
    }

    boolean isPeerInterruptibleOut(String p, boolean selfInterrupting) throws SJIOException
    {
	//flush(); //flush before entering inwhile()
	boolean result = false;
	int i = 1;
	if(i == 1)
	{
	    SJSocket s = getSocket(p);
	    try
		{
		    result = s.isPeerInterruptibleOut(selfInterrupting);
		}
	    catch(Exception ex)
		{
		    if(isContinuationFailure(p, s))
			{
			    //try {Thread.currentThread().sleep(500); System.out.println("peek: " + getSocket(p).peekObject());}
	   
			    //catch(Exception e){}
			    debug(participantName + ": Operation failure because participant has changed location, waiting for reconnection...");
			    while(s == getSocket(p)); //wait until the socket object is replaced by a new one, so we know that we are connected to the new location.
			    result = getSocket(p).isPeerInterruptibleOut(selfInterrupting); //retry
			}
		    else
			throw new SJIOException(ex.getMessage());
		}
	}
	tryExportContinuation();	
	return result;
    }

    private void tryExportContinuation() throws SJIOException
    {
	if(continuationEnabled)
	{
	    boolean meExportable = false;
	    for(int i = 0; i < participantsList.size(); i++)
	    {
		SJParticipantInfo pi = participantsList.get(i);
		if(pi.participantName.equals(participantName) && pi.hostType == SJParticipantInfo.TYPE_MOBILE)
		    meExportable = true;
		if(pi.participantName.equals(participantName) == false && pi.hostType == SJParticipantInfo.TYPE_SERVER)
		    exportTo = pi.participantName;
	    }
	    if(meExportable && exportTo != null)
	    {
		debug("exporting " + participantName + " to " + exportTo);
		continuationSocket = this;
		Continuation.suspend();
		reconnectContinuation();
	    }
	}

    }

    public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException
    {
	//flush();  //flush before entering outwhile()
	//Continuation Code
	tryExportContinuation();
	//////////////////////////
	Enumeration keys = participantsSockets.keys();
	boolean result = true;
        while(keys.hasMoreElements())
	{
	    SJSocket s = participantsSockets.get(keys.nextElement());
	    result &= s.isPeerInterruptingIn(selfInterruptible);
	}
        return result;
    }

    boolean interruptibleOutsync(String p, boolean condition) throws SJIOException
    {
        return getSocket(p).interruptibleOutsync(condition);
    }

    boolean interruptingInsync(String p, boolean condition, boolean peerInterruptible) throws SJIOException
    {
        return getSocket(p).interruptingInsync(condition, peerInterruptible);
    }

    boolean recursionEnter(String p, String lab) throws SJIOException
    {
        return getSocket(p).recursionEnter(lab);
    }

    boolean recursionExit(String p) throws SJIOException
    {
        return getSocket(p).recursionExit();
    }

    boolean recurse(String p, String lab) throws SJIOException
    {
        return getSocket(p).recurse(lab);
    }

    // Higher-order.
    void sendChannel(String p, SJService c, String encoded) throws SJIOException
    {
        getSocket(p).sendChannel(c, encoded);
    }

    SJService receiveChannel(String p, String encoded) throws SJIOException
    {
        return getSocket(p).receiveChannel(encoded);
    }

    void delegateSession(String p, SJAbstractSocket s, String encoded) throws SJIOException
    {
        getSocket(p).delegateSession(s, encoded);
    }

    SJAbstractSocket receiveSession(String p, String encoded, SJSessionParameters params) throws SJIOException
    {
        return getSocket(p).receiveSession(encoded, params);
    }
    
    public SJProtocol getProtocol(String p)
    {
	return getSocket(p).getProtocol();
    }

    public String getHostName(String p)
    {
        return getSocket(p).getHostName();
    }
    public int getPort(String p)
    {
        return getSocket(p).getPort();
    }

    public String getLocalHostName(String p)
    {
        return getSocket(p).getLocalHostName();
    }
    public int getLocalPort(String p)
    {
        return getSocket(p).getLocalPort();
    }
    public SJSessionParameters getParameters(String p)
    {
        return getSocket(p).getParameters();
    }

    public SJConnection getConnection(String p)
    {
        return getSocket(p).getConnection();
    }

    public void reconnect(String p, SJConnection connection) throws SJIOException
    {
        getSocket(p).reconnect(connection);
    }

    public void setHostName(String p, String hostAddress)
    {
        getSocket(p).setHostName(hostAddress);
    }
    public void setPort(String p, int port)
    {
        getSocket(p).setPort(port);
    }

    public SJStateManager getStateManager(String p)
    {
        return getSocket(p).getStateManager();
    }
    public void setStateManager(String p, SJStateManager sm)
    {
        getSocket(p).setStateManager(sm);
    }

    public SJSessionType currentSessionType(String p) // Session actions performed so far (modulo loop types).
    {
        return getSocket(p).currentSessionType();
    }
    public SJSessionType remainingSessionType(String p)
    {
        return getSocket(p).remainingSessionType();
    }

    public boolean typeStartsWithOutput(String p) throws SJIOException
    {
        return getSocket(p).typeStartsWithOutput();
    }

    public SJSessionType getInitialRuntimeType(String p) throws SJIOException
    {
        return getSocket(p).getInitialRuntimeType();
    }
    public boolean supportsBlocking(String p)
    {
        return getSocket(p).supportsBlocking();
    }

    public boolean arrived(String p)
    {
        return getSocket(p).arrived();
    }

    public TransportSelector transportSelector(String p)
    {
        return getSocket(p).transportSelector();
    }


        //**************** Overriden only to satisfy interface requirements, shouldn't be called! ***************************
    // Sending.
    public byte[] getUnflushedBytes() {return null;}
    public void setUnflushedBytes(byte[] b) {}
    public SJContinuationObject expectContinuation() throws SJIOException {throw new RuntimeException("[SJSocketGroup.expectContinuation] shouldn't get here.");}
    public void send(Object o) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.send] shouldn't get here.");}
    public void sendInt(int i) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.sendInt] shouldn't get here.");}
    public void sendBoolean(boolean b) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.sendBoolean] shouldn't get here.");}
    public void sendDouble(double d) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.sendDouble] shouldn't get here.");}
    public void pass(Object o) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.pass] shouldn't get here.");}
    public void copy(Object o) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.copy] shouldn't get here.");}
    // Receiving.
    public Object receive() throws SJIOException, ClassNotFoundException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receive] shouldn't get here.");}
    public int receiveInt() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receiveInt] shouldn't get here.");}
    public boolean receiveBoolean() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receiveBoolean] shouldn't get here.");}
    public double receiveDouble() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receiveDouble] shouldn't get here.");}
    // Session handling.
    public void outlabel(String lab) throws SJIOException, RuntimeException 
    {
	debug(participantName + ": outlabel()");
	Enumeration keys = participantsSockets.keys();
	while(keys.hasMoreElements())
	{
	    SJSocket s = participantsSockets.get(keys.nextElement());
	    s.outlabel(lab);
	}
    }
    public String inlabel() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.inlabel] shouldn't get here.");}
    /*    public boolean outsync(boolean condition) throws SJIOException, RuntimeException 
    {
	Enumeration keys = participantsSockets.keys();
	while(keys.hasMoreElements())
	{
	    SJSocket s = participantsSockets.get(keys.nextElement());
	    s.outsync(condition);
	}
	return true; //MQTODO: What to return here?
	}*/
    public boolean insync() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.insync] shouldn't get here.");}
    public boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.isPeerInterruptibleOut] shouldn't get here.");}
    /*    public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.isPeerInterruptibleIn] shouldn't get here.");}*/
    public boolean interruptibleOutsync(boolean condition) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.interruptibleoutsync] shouldn't get here.");}
    public boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.interruptibleInsync] shouldn't get here.");}

    public boolean recursionEnter(String lab) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.recursionEnter] shouldn't get here.");}
    public boolean recursionExit() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.recursionExit] shouldn't get here.");}
    public boolean recurse(String lab) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.recurse] shouldn't get here.");}

    // Higher-order.
    public void sendChannel(SJService c, String encoded) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.sendChannel] shouldn't get here.");}
    public SJService receiveChannel(String encoded) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receiveChannel] shouldn't get here.");}

    public void delegateSession(SJAbstractSocket s, String encoded) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.delegateSession] shouldn't get here.");}
    public SJAbstractSocket receiveSession(String encoded, SJSessionParameters params) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.receiveSession] shouldn't get here.");}

    public SJProtocol getProtocol() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getProtocol] shouldn't get here.");}

    public String getHostName()  throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getHostName] shouldn't get here.");}
    public int getPort() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getPort] shouldn't get here.");}

    public String getLocalHostName() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getLocalHostName] shouldn't get here.");}
    public int getLocalPort()  throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getLocalPort] shouldn't get here.");}

    public SJSessionParameters getParameters()  throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getParameters] shouldn't get here.");}

    public SJConnection getConnection()  throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getConnection] shouldn't get here.");}

    public void reconnect(SJConnection connection) throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.reconnect] shouldn't get here.");}

    public void setHostName(String hostAddress) throws RuntimeException {throw new RuntimeException("[SJSocketGroup.setHostName] shouldn't get here.");}

    public void setPort(int port) throws RuntimeException {throw new RuntimeException("[SJSocketGroup.setPort] shouldn't get here.");}

    public SJStateManager getStateManager() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.getStateManager] shouldn't get here.");}
    public void setStateManager(SJStateManager sm) throws RuntimeException {throw new RuntimeException("[SJSocketGroup.setStateManger] shouldn't get here.");}

    public SJSessionType currentSessionType() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.currentSessionType] shouldn't get here.");}
    public SJSessionType remainingSessionType() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.remainingSessionType] shouldn't get here.");}

    public boolean typeStartsWithOutput() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.typeStartWithOutput] shouldn't get here.");}

    public SJSessionType getInitialRuntimeType() throws SJIOException, RuntimeException {throw new RuntimeException("[SJSocketGroup.getInitialRuntimeType] shouldn't get here.");}

    public boolean supportsBlocking() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.supportBlocking] shouldn't get here.");}

    public boolean arrived() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.arrived] shouldn't get here.");}

    public TransportSelector transportSelector() throws RuntimeException {throw new RuntimeException("[SJSocketGroup.transportSelector] shouldn't get here.");}
}