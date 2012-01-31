//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/Utils.sj -d tests/classes/

package oopsla.schat.common;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.httpservlet.*;

public class Utils
{		
	public static String makeTimeStamp()
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
	
	public static SJSessionParameters createSJSessionParameters(String setups, String transports)
	{
		SJSessionParameters params;
		
		if (setups.equals("d") && transports.equals("d"))
		{
			params = new SJSessionParameters();
		}
		else
		{
			List ss = new LinkedList();
			List ts = new LinkedList();				
			
			parseTransportFlags(ss, setups);
			parseTransportFlags(ts, transports);
								
			params = new SJSessionParameters(ss, ts);
		}

		return params;
	}
	
	private static void parseTransportFlags(List ts, String transports)
	{
		if (transports.equals("d"))
		{
			//ts.add(new SJFifoPair());
			ts.add(new SJStreamTCP());
			
			return;
		}
		
		char[] cs = transports.toCharArray();
		
		for (int i = 0; i < cs.length; i++)
		{
			switch (cs[i])
			{
				/*case 'f':
				{
					ts.add(new SJFifoPair());
					
					break;
				}*/
				case 's':
				{
					ts.add(new SJStreamTCP());
					
					break;
				}					
				case 'm':
				{			
					ts.add(new SJManualTCP());
					
					break;
				}					
				case 'h':
				{			
					ts.add(new SJHTTPServlet());
					
					break;
				}
				default:
				{
					throw new RuntimeException("[Utils] Unknown transport flag: " + cs[i]);
				}
			}
		}					
	}	
}
