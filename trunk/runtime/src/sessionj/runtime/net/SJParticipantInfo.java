//<By MQ> Added
package sessionj.runtime.net;
import java.io.Serializable;

public class SJParticipantInfo implements Serializable
{
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_PC = 2;
    public static final int TYPE_SERVER = 3;

    public String hostName;
    public int portNumber;
    public String participantName;
    public SJServerIdentifier si;
    public int hostType;

    public SJParticipantInfo(String participantName, String hostName, int portNumber)
    {
	this(participantName, hostName, portNumber, TYPE_UNKNOWN);
    }

    public SJParticipantInfo(String participantName, String hostName, int portNumber, int hostType)
    {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.participantName = participantName;
	this.si = new SJServerIdentifier(hostName, portNumber);
	this.hostType = hostType;
    }

    @Override public String toString()
    {
	return participantName;
    }
}