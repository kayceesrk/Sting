// To test, run all servers before running Multi1
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/multiparty/Multi4.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.multiparty.Multi4 &
package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi4
{
        participant Multi4;
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
                        ss = SJServerSocketImpl.create(invitation, 7104);
                	ss.addParticipant("Multi2", "localhost", 7102);
                	ss.addParticipant("Multi3", "localhost", 7103);
			final noalias SJSocketGroup ps;
                	try (ps)
                	{
				ps = ss.accept("Multi1");
                        	System.out.println("Multi4: connected to all participants");
                        	ps.send("Hello, Multi3 from Multi4", "Multi3");
                        	ps.send("Hello, Multi2 from Multi4", "Multi2");
			}
			finally{}
                }
                finally {}
        }

         public static void main(String[] args) throws Exception{

                Multi4 a = new Multi4();
                a.run();
        }
}
