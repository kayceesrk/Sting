package thesis.benchmark;

import thesis.benchmark.Util;

public abstract class AbstractServer implements Server
{
	private boolean debug;
	private int port;
  	
  public AbstractServer(boolean debug, int port) 
  {
  	this.debug = debug;
  	this.port = port;
  }
  
	public boolean isDebug()
  {
  	return debug;
  }  
  
	public int getPort()
	{
		return port;
	}
  
  protected void debugPrintln(String m)
  {
  	Util.debugPrintln(debug, m);
  }  
}
