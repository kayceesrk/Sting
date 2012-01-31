package thesis.benchmark;

public class ServerMessage extends Message 
{	
  public ServerMessage(int cid, String msg, int serverMessageSize) 
  {
    super(cid, msg, serverMessageSize);
  }
  
  public String toString()
  {
  	return "ServerMessage[sid=" + getSenderId() + ",msg=" + getMessage() + ",size=" + getMessageSize() + "]"; 
  }
}
