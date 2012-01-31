//<By MQ> Added
package sessionj.runtime.net;
import sessionj.runtime.session.SJControlSignal;

public class SJContinuationNotification extends SJControlSignal
{
    public String from; 
    public String to; //The participant that will take care of the continuation;

    public SJContinuationNotification(String from, String to)
    {
	this.from = from;
	this.to = to;
    }
}