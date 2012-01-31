import java.io.BufferedReader;
import java.io.InputStreamReader;

import sj.runtime.SJProtocol;
import sj.runtime.net.SJNewSocket;
import sj.runtime.net.transport.SJOldFileTransport;
import sj.runtime.net.transport.SJTransport;


public class FileClient {

	public FileClient(String server, int port) throws Exception{
		SJProtocol p = new SJProtocol("begin.?(int).!<int>");
		SJTransport t = SJOldFileTransport.createClientTransport(server, port);
		SJNewSocket s = null;//SJNewSocket.create(p, t);
		
//		try(s)
//		{	

			s.request();
			System.out.println("Connected...");
			System.out.println(s.receiveInt());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			while(input.equals("")){
				System.out.print("Anything to continue:");
				input = br.readLine();
			}
			s.sendInt(56789);
			
			System.out.println(s.receive());
			s.send("uiop");
			System.out.println("Done!");
			s.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new FileClient(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
