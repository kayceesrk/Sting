import java.io.*;

public class MyObject implements Serializable {

  private byte [] byteArray;
  private int signal;
  public static int DEFAULT_SIZE = 1024;
  public static final int KILL_LOAD = 0x00000001;
  public static final int BEGIN_TIMING = 0x00000002;
  public static final int BEGIN_COUNTING = 0x00000004;
  public static final int NO_SIGNAL = 0x00000000;

  public MyObject(int signal) {
    byteArray = new byte[DEFAULT_SIZE];
    this.signal = signal;
  }

  public byte [] getByteArray() {
    return byteArray;
  }

  public boolean killSignal() {
    return ((signal & KILL_LOAD) != 0);
  }
  
  public boolean timeSignal() {
    return ((signal & BEGIN_TIMING) != 0);
  }

}
