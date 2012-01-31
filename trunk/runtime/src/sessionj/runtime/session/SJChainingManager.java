//<By MQ> Added to manage type2 chaining between participants
package sessionj.runtime.session;
import java.util.*;
import sessionj.runtime.net.*;

public class SJChainingManager
{
    private List<SJParticipantInfo> participantsList;
    private Hashtable<String, SJSocket> participantsSockets;
    private String participantName;
    private int closeRequests = 0;

    public SJChainingManager(Hashtable<String, SJSocket> participantsSockets)
    {
	this.participantsSockets = participantsSockets;
    }
}