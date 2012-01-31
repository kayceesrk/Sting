
import java.nio.*;

public class ReadObject {
  public ByteBuffer b;
  public int numRead;
  private static final int defaultSize = 1024;

  public ReadObject() {
    b = ByteBuffer.allocate(defaultSize);
    numRead = 0;
  }
  
  public ReadObject(int size) {
    b = ByteBuffer.allocate(size);
    numRead = 0;
  }

}
