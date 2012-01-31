package thesis.benchmark;
  
public interface Killable  
{ 
	void run() throws Exception;  // Must be runnable to be killable
  void kill() throws Exception; // Wait for Clients to finish and then end
}
