// To test, run all servers before running this program
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/multiparty/Multi1.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.multiparty.Multi1
package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi1
{
	participant Multi1;
	 private final noalias protocol invitation {
	        participants: Multi1, Multi2, Multi3, Multi4
	        .Multi1: begin
		.Multi4->Multi3:<String>
                .Multi4->Multi2:<String>
		.Multi1->Multi2:<String>
		.Multi1->Multi3:<String>
		.Multi2->Multi1:<String>
		.Multi3->Multi1:<String>	
		.Multi2->Multi3:<String>
		.Multi3->Multi2:<String>
	}

	public void run() throws Exception 
	{
		final noalias SJService c = SJService.create(invitation, "localhost", 1000);
                c.addParticipant("Multi2", "localhost", 7102);
		c.addParticipant("Multi3", "localhost", 7103);
		c.addParticipant("Multi4", "localhost", 7104);
                final noalias SJSocketGroup ps;
		try (ps)
                {
                        ps = c.request();
			System.out.println("Multi1: connected to all participants");
			ps.send("Hello, Multi2 from Multi1","Multi2");
			ps.send("Hello, Multi3 from Multi1","Multi3");
			String str2 = (String)ps.receive("Multi2");
			System.out.println("Multi1 received from Multi2: " + str2);
			String str3 = (String)ps.receive("Multi3");
			System.out.println("Multi1 received from Multi3: " + str3);
		}
		finally {}
	}
	
	 public static void main(String[] args) throws Exception{

                Multi1 a = new Multi1();
                a.run();
        }
}