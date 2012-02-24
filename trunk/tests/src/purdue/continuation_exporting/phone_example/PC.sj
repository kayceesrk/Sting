//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/PC.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.PC & 
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;



public class PC{
       	    participant PC;
	    private final noalias protocol onlineShopping {
                      participants: vendor, phone, PC
                      .phone: begin
                      .phone->vendor: <String>
                      .vendor->phone: <Album>
                      .phone:
                        [vendor->phone: <SongInfo>
                        .phone: {BUY: vendor->phone: <Song>
                                .phone->PC: <Song>,
                                NOOP:
                                }
                        ]*
}

	public void run() throws Exception 
	{		
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(onlineShopping, 20103, SJParticipantInfo.TYPE_PC);
			ss.participantName("PC");
			ss.addParticipant("vendor", "localhost", 20102, SJParticipantInfo.TYPE_SERVER);
			ss.addParticipant("phone", "localhost", 20101, SJParticipantInfo.TYPE_MOBILE);
                        final noalias SJSocketGroup sg;

                        try(sg)
                        {
                                sg = ss.accept("phone");
                               	sg.inwhile("phone")
				{
					sg.inbranch("phone")
					{
						case BUY:
						{
							Song s = (Song)sg.receive("phone");
							System.out.println("PC: Received song: " +s.Name() + " from phone.");
						}
						case NOOP:
						{
						}
					}				
				}
	
                        }
			catch(Exception ex)
			{
				System.out.println("PC Exception: " + ex);
				ex.printStackTrace();
			}
			finally {}
                }
                finally{}

	}
	
	public static void main(String[] args) throws Exception
	{
		PC a = new PC();
		a.run();
	}
}