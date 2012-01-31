package ecoop.bmarks.sj.common;

import java.io.*;

//@deprecated
public class ClientMessage implements Serializable 
{
	private int clientNum;
	private String msg;
  private int size; // Use to request MyObjects of this size (it's not the size of this message). 

  public ClientMessage(int clientNum, String msg, int size) 
  {
    this.clientNum = clientNum;
    this.msg = msg;
    this.size = size;
  }

  public int getSize()
  {
  	return size;
  }
  
  public String toString()
  {
  	return "ClientMessage[clientNum=" + clientNum + ",msg=" + msg + ",size=" + size + "]"; 
  }
}
