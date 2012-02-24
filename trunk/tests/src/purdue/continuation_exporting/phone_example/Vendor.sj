//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/Vendor.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Vendor & 
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;


public class Vendor{
participant vendor;
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
		SongInfo[] songsInfo = new SongInfo[4];
		songsInfo[0] =  new SongInfo("Song1", 8);
		songsInfo[1] =  new SongInfo("Song2", 9);
		songsInfo[2] =  new SongInfo("Song3", 10);
		songsInfo[3] =  new SongInfo("Song4", 7);
		Song[] songs = new Song[4];
		songs[0] = new Song("Song1");
		songs[1] = new Song("Song2");
		songs[2] = new Song("Song3");
		songs[3] = new Song("Song4");
		
		Album beatlesAlbum = new Album("Beatles 2012", 4);
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(onlineShopping, 20102, SJParticipantInfo.TYPE_SERVER);
			ss.participantName("vendor");
			ss.addParticipant("PC", "localhost", 20103, SJParticipantInfo.TYPE_PC); 
			ss.addParticipant("phone", "localhost", 20101, SJParticipantInfo.TYPE_MOBILE);	  
                        final noalias SJSocketGroup sg;
			
                        try(sg)
                        {
                                sg = ss.accept("phone");
                                String albumRequest = (String)sg.receive("phone");
				System.out.println("Vendor: Received request from phone: " + albumRequest);
				sg.send(beatlesAlbum, "phone");
				int i = 0;
				sg.inwhile("phone")
				{
					sg.send(songsInfo[i], "phone");
					sg.inbranch("phone")
					{
						case BUY:
						{
							System.out.println("Vendor: Phone has elected to buy song " + songs[i].Name() + ". Sending song...");
							sg.send(songs[i], "phone");
						}
						case NOOP:
						{
						}
					}	
					i++;			
				}
				
                        }
			catch(Exception ex)
			{
				System.out.println("Vendor Exception: " + ex);
				ex.printStackTrace();
			}
			finally {}
                }
		catch(Exception ex)
               {
			System.out.println("Vendor Exception: " + ex);
                        ex.printStackTrace();
		}
                finally{}

	}
	
	public static void main(String[] args) throws Exception
	{
		Vendor a = new Vendor();
		a.run();
	}
}