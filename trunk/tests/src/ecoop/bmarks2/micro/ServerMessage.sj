package ecoop.bmarks2.micro;

import java.io.*;

public class ServerMessage implements Serializable 
{	
	/*private boolean isStartCounting = false;
	private boolean isStopCounting = false;*/
	private boolean isKill;
	
	private byte[] payload;
  	
  private int size;

  //public ServerMessage(int size, boolean isStartCounting, boolean isStopCounting, boolean isKill)
  public ServerMessage(int size, boolean isKill)
  {
    this.size = size;    
    this.payload = new byte[size];
    this.isKill = isKill;
  }

  /*public ServerMessage(int size, ServerMessage cm)
  {
    this(size);
    
    this.isStartCounting = cm.isStartCounting;
    this.isStopCounting = cm.isStopCounting;
    this.isKill = cm.isKill;
  }*/
  
  public byte[] getPayload() 
  {
    return payload;
  }

  /*public ServerMessage setStartCounting()
  {
  	this.isStartCounting = true;
  	this.isStopCounting = false;
  	
  	return this;
  }
  
  public ServerMessage setStopCounting()
  {
  	this.isStartCounting = false;
  	this.isStopCounting = true;
  	
  	return this;
  }
  
  public ServerMessage setKill()
  {
  	this.isStartCounting = false;
  	this.isKill = true;
  	
  	return this;
  }
  
  public boolean isStartCounting() 
  {
    return isStartCounting;
  }
  
  public boolean isStopCounting() 
  {
    return isStopCounting;
  }*/
  
  public boolean isKill() 
  {
    return isKill;
  }
    
  public String toString()
  {
  	//return "ServerMessage[size=" + size + ", start=" + isStartCounting + ", stop=" + isStopCounting + ", kill=" + isKill + "]";
  	return "ServerMessage[size=" + size + ",isKill=" + isKill + "]";
  }
}
