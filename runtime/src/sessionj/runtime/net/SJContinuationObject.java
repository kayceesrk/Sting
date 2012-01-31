//<By MQ> Added
package sessionj.runtime.net;
import org.apache.commons.javaflow.Continuation;
import sessionj.runtime.session.SJControlSignal;

public class SJContinuationObject extends SJControlSignal
{
    public Continuation continuation; //The participant that will take care of the continuation;
    public String to;

    public SJContinuationObject(Continuation co, String to)
    {
	this.continuation = co;
	this.to = to;
    }
}