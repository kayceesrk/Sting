package ecoop.bmarks2.micro.java.event.server;

import java.nio.*;

public class Attachment 
{
	public static final int GET_LABEL = 1; // REC or QUIT.
	public static final int GET_HEADER = 2; // Size of serialized ClientMessage.
	public static final int GET_MESSAGE = 3; // Serialized ClientMessage.
	
	public int tid;
	
  public ByteBuffer readBuffer;
  public ByteBuffer writeBuffer;

  public int state;
  public int numToRead;
  
  private static final int DEFAULT_SIZE = 1024;

  public Attachment(int tid) 
  {
  	this.tid = tid;
  	
    this.readBuffer = ByteBuffer.allocate(DEFAULT_SIZE);
    //this.writeBuffer = ByteBuffer.allocate(DEFAULT_SIZE);
    
    this.state = GET_LABEL;
  }
  
  public void resetReadState() 
  {
  	this.readBuffer.clear();
  	this.state = GET_LABEL;
  }
  
  public void setWriteBuffer(ByteBuffer bb)
  {
  	this.writeBuffer = bb;
  }
}
