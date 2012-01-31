import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;



public class TestFileReader {
	
	public TestFileReader() {

		File cts  = new File("/Users/fred/cts");
		File ctssignals  = new File("/Users/fred/ctssignals");
		
		
		
		System.out.println("Reader started");
		try{
			
			
			FileInputStream fis = new FileInputStream(cts);
			ObjectInputStream ois = new ObjectInputStream(fis);
			FileInputStream sis = new FileInputStream(ctssignals);
			
			int signal = 0;
			int arrlen = 0; //length byte array for incoming object
			byte[] lengthbuffer = new byte[4]; //arrlength bytes written here
			byte[] objectbuffer = null;
			
			while(signal != TestFileWriter.DONE){
				System.out.print("Waiting for signal: ");
				while(sis.available() < 1){} //wait for signal
				
				signal = sis.read();
				
				if(signal == TestFileWriter.READ){
					System.out.println("Before: " + cts.length());
					System.out.print("READ");
					System.out.println("Available: " + ois.available());
					sis.read(lengthbuffer);
					arrlen = bytesToInt(lengthbuffer);
					System.out.print(" " + arrlen + " ");
					
					objectbuffer = new byte[arrlen];
//					synchronized (this) {
//						try {
//							wait(5000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}

//					ois.readFully(objectbuffer);
					System.out.println(ois.read(objectbuffer));

//					List<String> test = (List<String>) fromByteArray(objectbuffer);
//					for(String message : test){
//						System.out.println(message);
//					}
//					TestClass test = (TestClass) fromByteArray(objectbuffer);
					String test = (String) fromByteArray(objectbuffer);
					System.out.println(test);

					System.out.println("After: " + cts.length());
				}else if(signal == TestFileWriter.DONE){
					System.out.println("DONE");
				}

			}
			
			ois.close();
			sis.close();
			
//			while(fis.available() < 4){}
//			
//			byte[] intbytes = new byte[4];
//			System.out.println(fis.read(intbytes));
//			
//			int length = bytesToInt(intbytes);
//			
//			//while(fis.available() < length){} 
//			
//			byte[] objbytes = new byte[length];
//			System.out.println(fis.read(objbytes));
//			String obj = (String) fromByteArray(objbytes);
//			System.out.println(obj);
			
//			System.out.println(ois.readInt() == TestFileWriter.WAIT);
			
//			String in = "a";
//			while(!in.equals("quit")){
//				
//				in = (String) ois.readObject();
//				System.err.println(in);
//				
//			}
			
			
		}catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}catch (ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		
	}

	private byte[] toByteArray(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}
	
	public Object fromByteArray(final byte[] bytes) throws IOException, ClassNotFoundException{
		System.out.println("Deserialising object of " + bytes.length + " bytes");
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(bis);
		return in.readObject();
	}

	private int bytesToInt(byte[] b) {
		return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | (b[3]&0xff);
	}
	
	private byte[] bytesFromInt(int i) {
		return new byte[] { (byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i };
	}
	
	public static void main(String[] args) {
		new TestFileReader();
	}
	
	
}
