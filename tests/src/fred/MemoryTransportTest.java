import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sj.runtime.SJIOException;
import sj.runtime.SJProtocol;
import sj.runtime.net.SJNewServerSocket;
import sj.runtime.net.SJNewSocket;
import sj.runtime.net.transport.SJMemoryTransport;
import sj.runtime.net.transport.SJServerTransport;


public class MemoryTransportTest {
	
	Thread s;
	
	public MemoryTransportTest() throws IOException{

		s = new Server(8888);
		s.start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		while(!in.equals("q")){
			if(in.equals("n")){
				Thread c = new Client(8888);
				c.start();
			}
			in = "";
			System.out.print("n/q to go [q]");
			in = br.readLine();
		}

//		Thread c1 = new Client(8888);
//		c1.start();
		
	}


	public static void main(String[] args) throws IOException{
		
		new MemoryTransportTest();
		
	}

	private class Server extends Thread{
		
		private int port;

		Server(int port){
			this.port = port;
		}
		

		public void run() {
			System.out.println("Server started: " + this.getId());
			try{

				SJProtocol p = new SJProtocol("begin.!<int>.?(int)");
				SJServerTransport t = SJMemoryTransport.createServerTransport(port);
//				SJServerTransport tt= SJServerMemoryTransport.create(port+1);
//				SJServerTransportList ts = new SJServerTransportList(t);
//				ts.addTransport(tt);
				
				SJNewServerSocket ss = null;//SJNewServerSocket.create(p,);
				
				while(true){
					SJNewSocket s = null;
					
					s = ss.accept();
					s.sendInt(1234);
					
					System.out.println(s.receiveInt());
					
					s.send("asdf");
					
					System.out.println(s.receive());
				}
			}catch(SJIOException sjioe){
				System.out.println("I/O Error occured");
				sjioe.printStackTrace();
			}catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace();
			}
			
		}
		
		
	}
	
	private class Client extends Thread{
		
		private int port;

		Client(int port){
			this.port = port;
		}
		
		public void run(){
			System.out.println("Client started: " + this.getId());
			try{
				SJProtocol p = new SJProtocol("begin.?(int).!<int>");
				SJMemoryTransport t = SJMemoryTransport.createClientTransport("localhost", port);
				SJNewSocket s = null;//SJNewSocket.create(p, t);
				s.request();
				System.out.println("Connected...");
				System.out.println(s.receiveInt());
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				String input = "";
//				while(input.equals("")){
//					System.out.print("Anything to continue:");
//					input = br.readLine();
//				}
				s.sendInt(56789);
				
				System.out.println(s.receive());
				s.send("uiop");
				System.out.println("Done!");
			}catch (SJIOException sjioe) {
				sjioe.printStackTrace();
			}catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}/*catch(IOException ioe){
				ioe.printStackTrace();
			}*/

		}
		
	}
	
}
