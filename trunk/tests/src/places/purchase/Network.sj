//$ bin/sessionjc tests/src/places/purchase/Network.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.purchase.Network 9999 localhost 8888

package places.purchase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simulates the network that can read, modify or change the order of the messages
 * 
 * @author Nuno Alves
 *
 */
class Network {
	
	Network(int port_a, String addr_s, int port_s) throws Exception {
		
		final ServerSocket ss;
		
		try {
			ss = new ServerSocket(port_a);
			
			while (true) {
				
				final Socket s;
	
				try {
					s = ss.accept();

					final Socket s2;

					try {
						s2 = new Socket(addr_s, port_s);
						
						Thread t = new Thread() {
		                	
		                	public void run() {
		                		try {
			                		InputStream is = s2.getInputStream();
					                OutputStream os = s.getOutputStream();
					                
					            	dumpStream(is,os);
					                
		                		}
		                		catch (IOException e) {
		                			e.printStackTrace();
		                		}
		                	}
		                	
		                };
		                
		                t.start();
						
		                InputStream is = s.getInputStream();
		                OutputStream os = s2.getOutputStream();
		                
		                dumpStream(is,os);
						
					}		
					catch (Exception e) 
					{ 
						e.printStackTrace();
					}

				}		
				catch (Exception e) 
				{ 
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) 
		{ 
			e.printStackTrace();
		}		
	}


	public static void main(String[] args) throws Exception 
	{
		
		int port_a = Integer.parseInt(args[0]);
		String host_s = args[1];
		int port_s = Integer.parseInt(args[2]);
		
		new Network(port_a, host_s, port_s);
	}
	
	/**
	 * Copies the content from InputStream to OutputStream
	 * Notifies user if Credit Card information is detected.
	 */
	protected void dumpStream( InputStream in, OutputStream out)
			throws IOException {
		byte[] arr = new byte[1024];
		for(;;) {
			int n = in.read( arr);
			if( n == -1)
				break;
			
			String str = new String(arr,0,n);
			if (str.contains("CreditCard")) {
				System.out.println("CREDIT CARD INFORMATION FOUND!!!!\n" + str);
				/*try {
					ByteArrayInputStream bais = new ByteArrayInputStream(arr,0,n);
					ObjectInputStream ois = new ObjectInputStream(bais);
					CreditCard card = (CreditCard)ois.readObject();
					System.out.println(card.toString());
				} catch(ClassNotFoundException e) {
					
				}*/
			}
			
			out.write( arr, 0, n);
		}
	}
}
