//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/client/ClientReader.sj -d tests/classes/ 
//$ bin/sessionj -cp tests/classes/ simplechat.client.ClientReader localhost 8888 

package simplechat.client;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import util.*;

public class ClientReader   
{		
	private static final String transports = "d";
	
	/*private String server;
	private int port;*/

	private SJSessionParameters sparams;
	
	public ClientReader()
	{
		/*this.server = server;
		this.port = port;*/
		
		this.sparams = TransportUtils.createSJSessionParameters(transports, transports);
	}
	
	private void run(String server, int port) throws SJIOException, SJIncompatibleSessionException
	{
		final noalias SJService c = SJService.create(SimpleChatProtocols.p_clientToServer, server, port);
		
		noalias SJSocket s;
		
		try (s)
		{
			s = c.request(sparams);

			s.outbranch(READ)
			{
				s.outwhile(true)
				{
					String msg = (String) s.receive();
					
					System.out.println(makeTimeStamp() + " " + msg);
				}
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
		
		ClientReader cr = new ClientReader();
		
		cr.run(server, port);
	}
	
	private static String makeTimeStamp()
	{
		Calendar c = new GregorianCalendar();

		String year = "" + c.get(Calendar.YEAR);
		String month = null;

		switch (c.get(Calendar.MONTH))
		{
			case Calendar.JANUARY: month = "01"; break;
			case Calendar.FEBRUARY: month = "02"; break;
			case Calendar.MARCH: month = "03"; break;
			case Calendar.APRIL: month = "04"; break;
			case Calendar.MAY: month = "05"; break;
			case Calendar.JUNE: month = "06"; break;
			case Calendar.JULY: month = "07"; break;
			case Calendar.AUGUST: month = "08"; break;
			case Calendar.SEPTEMBER: month = "09"; break;
			case Calendar.OCTOBER: month = "10"; break;
			case Calendar.NOVEMBER: month = "11"; break;
			case Calendar.DECEMBER: month = "12"; break;
		}
		
		int d = c.get(Calendar.DAY_OF_MONTH);		
		String day = ((d < 10) ? "0" : "") + d;
		
		int h = c.get(Calendar.HOUR_OF_DAY);
		String hour = ((h < 10) ? "0" : "") + h;
		
		int m = c.get(Calendar.MINUTE);
		String minute = ((m < 10) ? "0" : "") + m;
		
		return "[" + year + month + day + "-" + hour + minute + "]";	
	}	
}
