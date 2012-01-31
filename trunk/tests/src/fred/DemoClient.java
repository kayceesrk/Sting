import java.io.BufferedReader;
import java.io.InputStreamReader;

import sj.runtime.*;
import sj.runtime.net.*;

class DemoClient {
	
	public DemoClient(String server, int port) throws Exception
	{	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		
		SJProtocol p = new SJProtocol("H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
			       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
			       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
			       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
			       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
			       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
			       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
			       "U1KKuBq6qSsqq+AekRcXhSAgAA");
		
		SJServerAddress c = SJServerAddress.create(p, server, port);
		SJNewSocket s = SJNewSocket.create(c, SJParameters.test());
		
		System.out.print("Any key to start: ");
		br.readLine();
		
		s.request();

		while(!input.equalsIgnoreCase("quit")){
			System.out.print("Message: ");
			input = br.readLine();
			s.send(input);
			System.out.println(s.receive());
		}
		
		s.close();
		System.out.println("Done!");
	}
	
	public static void main(String[] args) throws Exception
	{
		new DemoClient(args[0], Integer.parseInt(args[1]));
	}	
}
