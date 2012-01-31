package sessionj.runtime.net;

public class SJServerSocketCloser
{
	private SJServerSocket ss; // Would be better to avoid session typed fields, currently can not be compiled by the SJ compiler itself.  
	
	protected SJServerSocketCloser(SJServerSocket ss)
	{
		this.ss = ss;
	}
	
	public void close()
	{
		ss.close();
	}
}
