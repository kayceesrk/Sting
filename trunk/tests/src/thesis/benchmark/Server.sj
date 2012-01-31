package thesis.benchmark;

// Implementing this interface (or extending AbstractServer), the Server should perform a GC step with each iteration.  
public interface Server extends Killable
{
	void run() throws Exception; // Allow Clients to connect and run sessions; implementations should do a GC step (AbstractServer won't do it) 
  	
  boolean isDebug();
  int getPort();
}
