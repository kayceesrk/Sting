package thesis.benchmark.bmark2;

import java.io.Serializable;

public class NoaliasMessage implements Serializable 
{
	private int senderId;
	private int messageId;
  private int messageSize; // Actually, the payload size 
  private noalias byte[] payload;
  
  public NoaliasMessage(int senderId, int messageId, int messageSize) 
  {
    this.senderId = senderId;
    this.messageId = messageId;
    this.messageSize = messageSize;
    this.payload = new byte[messageSize];
  }

  public int getSenderId() 
  {
  	return senderId;
  }
  
  public int getMessageId() 
  {
  	return messageId;
  }
  
  public void incrementMessageId()
  {
  	messageId++;
  }
  
  public int getMessageSize()
  {
  	return messageSize;
  }
  
  public byte[] getPayload() 
  {
  	return payload;
  }
  
  public String toString()
  {
  	return "NoaliasMessage[sid=" + getSenderId() + ",mid=" + getMessageId() + ",size=" + getMessageSize() + "]"; 
  }
}
