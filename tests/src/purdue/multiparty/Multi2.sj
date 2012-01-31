// To test, run all servers before running Multi1
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/multiparty/Multi2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.multiparty.Multi2
package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi2
{
        participant Multi2;
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
		final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(invitation, 7102);
                	ss.addParticipant("Multi3", "localhost", 7103);
			ss.addParticipant("Multi4", "localhost", 7104);
			final noalias SJSocketGroup ps;
                	try (ps)
                	{
				ps = ss.accept("Multi1");
                        	System.out.println("Multi2: connected to all participants");
                        	String str4 = (String)ps.receive("Multi4");
                        	System.out.println("Multi2 received from Multi4: " + str4);
                        	String str1 = (String)ps.receive("Multi1");
                        	System.out.println("Multi2 received from Multi1: " + str1);
                        	ps.send("Hello, Multi1 from Multi2","Multi1");
                        	ps.send("Hello, Multi3 from Multi2","Multi3");
                        	String str3 = (String)ps.receive("Multi3");
                        	System.out.println("Multi2 received from Multi3: " + str3);
			}
			finally{}
                }
                finally {}
        }

         public static void main(String[] args) throws Exception{

                Multi2 a = new Multi2();
                a.run();
        }
}
