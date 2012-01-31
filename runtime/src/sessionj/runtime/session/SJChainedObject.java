//<By MQ>
package sessionj.runtime.session;

public class SJChainedObject extends SJControlSignal
{
    private String participantName;
    private Object obj;

    public SJChainedObject(String participantName, Object obj)
    {
	this.participantName = participantName;
	this.obj = obj;
    }

    public String participantName()
    {
	return participantName;
    }
    
    public Object obj()
    {
	return obj;
    }
}