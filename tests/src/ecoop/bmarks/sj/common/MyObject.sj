package ecoop.bmarks.sj.common;

import java.io.*;

//@deprecated
public class MyObject implements Serializable 
{
	public static final int NO_SIGNAL = 0;	
  public static final int KILL = 1;
  public static final int BEGIN_TIMING = 2;
  public static final int BEGIN_COUNTING = 4;  
	
	private byte[] byteArray;
  	
  private int signal;
  private int size;

  public MyObject(int signal, int size) 
  {
    this.signal = signal;
    this.size = size;
    
    this.byteArray = new byte[size];
  }

  public byte[] getByteArray() 
  {
    return byteArray;
  }

  public boolean killSignal() 
  {
    return ((signal & KILL) != 0);
  }
  
  public boolean timeSignal() 
  {
    return ((signal & BEGIN_TIMING) != 0);
  }
  
  public String toString()
  {
  	return "MyObject[signal=" + signal + ",size=" + size + "]"; 
  }
}
