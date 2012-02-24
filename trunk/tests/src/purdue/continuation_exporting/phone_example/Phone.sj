// To test, run Vendor and PC first
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/Phone.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Phone  
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;

public class Phone implements Runnable, Serializable{


	public void run()  {
	}
	
	static class myRunnable implements Runnable, Serializable   //We Must make the class serializable in order to be able to transfer continuation
	{
		participant phone;	
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

		public void run()
		{
		   try
		   {
			final noalias SJService c = SJService.create(onlineShopping, "localhost", 1000, SJParticipantInfo.TYPE_MOBILE);
			c.participantName("phone");	//Although still not forced by the compiler, it is important to provide *this* participant's to the runtime.
										 	   
			c.addParticipant("vendor", "localhost", 20102, SJParticipantInfo.TYPE_SERVER);		
			c.addParticipant("PC", "localhost", 20103, SJParticipantInfo.TYPE_PC);		
		
			final noalias SJSocketGroup ps;
			try (ps) 
			{
				ps = c.request();
				ps.continuationEnabled = true;
				System.out.println("Phone: connected to all participants");		
				ps.send("Please send me the songs in Beatles Album 2012, and I will decide which songs to buy!", "vendor");
				Album a = (Album)ps.receive("vendor");
				System.out.println("Received Album info. Album Name: " + a.Name() + ", Number of songs: " + a.Count());
				int i = 0;
				ps.outwhile(i++ < a.Count() - 1)
				{
				     System.out.println("Retrieving song information...");
				     SongInfo si = (SongInfo)ps.receive("vendor");
				     System.out.println("Received song information. Song Name: + " + si.Name() + ", rating: " + si.Rating());
				     //Integer rating = (Integer)ps.receive(vendor);
				     if(si.Rating() > 8)
				     {
					ps.outbranch(BUY)
					{
						System.out.println("Buying song...");
						Song mySong = (Song)ps.receive("vendor");
						ps.send(mySong, "PC");
					}
				     }
				     else
				     {					
					ps.outbranch(NOOP) 
					{
						System.out.println("Song not good enough to buy... ignored!");
					}
				     }
									     

				}
			}
			catch(Exception ex)
			{
				System.out.println("phone Exception: " + ex);
				ex.printStackTrace();
			}
			finally{}	
		   }
		   catch(Exception ex)
		   {
			System.out.println("phone Exception: " + ex);
                        ex.printStackTrace();
                   }
		}
  	}

	public static void main(String[] args) throws Exception{
		myRunnable myPhone = new myRunnable();
		SJSocketGroup.executeExportable(myPhone); //Instead of calling myPhone.run(), give it to this function and we'll take care of the rest
	}
}