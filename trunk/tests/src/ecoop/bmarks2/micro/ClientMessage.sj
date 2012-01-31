package ecoop.bmarks2.micro;

import java.io.*;

public class ClientMessage implements Serializable 
{
	private int cid;
	private String msg;
  private int serverMessageSize; // Use to request MyObjects of this size (it's not the size of this message). 

  public ClientMessage(int cid, String msg, int serverMessageSize) 
  {
    this.cid = cid;
    this.msg = msg;
    this.serverMessageSize= serverMessageSize;
  }

  public int getServerMessageSize()
  {
  	return serverMessageSize;
  }
  
  public String toString()
  {
  	return "ClientMessage[cid=" + cid + ",msg=" + msg + ",size=" + serverMessageSize + "]"; 
  }
}
