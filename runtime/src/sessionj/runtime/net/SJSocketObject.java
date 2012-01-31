//<By MQ> Added. 

package sessionj.runtime.net;
import sessionj.runtime.*;
import java.io.Serializable;
import org.apache.commons.javaflow.Continuation;

public class SJSocketObject implements Serializable
{
    SJSocketGroup s;
    public SJSocketObject(SJSocketGroup s)
    {
	this.s = s;
    }
    public void exportContinuation(Continuation c, String p) throws SJIOException
    {
	s.exportContinuation(c, p);
    }

    public void flush() throws SJIOException
    {
	s.flush();
    }

    public void executeExportable(Runnable r) throws SJIOException
    {
	s.executeExportable(r);
    }
}