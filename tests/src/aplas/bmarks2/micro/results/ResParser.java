//$ javac ResParser
//$ java ResParser inFile.txt BODY 100 outFile.csv JT

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

public class ResParser
{
	public enum Version 
	{ 
		JT, JE, ST, SE; 
	
		public static Version parseVersion(String m)
		{
			if (m.equals("JT"))
			{
				return JT;
			}
			else if (m.equals("JE"))
			{
				return JE;
			}
			else if (m.equals("ST"))
			{
				return ST;
			}
			else if (m.equals("SE"))
			{
				return SE;
			}
			else
			{
				throw new RuntimeException("[ResParser] Version value cannot be parsed from : " + m);
			}
		}
	}	
	
	public enum TimerFlag
	{
		BODY, INIT, FULL; 
		
		public static TimerFlag parseTimerFlag(String m)
		{
			if (m.equals("BODY"))
			{
				return BODY;
			}
			else if (m.equals("INIT"))
			{
				return INIT;
			}
			else if (m.equals("FULL"))
			{
				return FULL;
			}
			else
			{
				throw new RuntimeException("[ResParser] Timer flag cannot be parsed from : " + m);
			}
		}		
	}
	
	private static final String INIT_PREFIX = "[TimerClient] Initialisation: ";
	private static final String BODY_PREFIX = "[TimerClient] Body: ";
	private static final String CLOSE_PREFIX = "[TimerClient] Close: ";
	private static final String RESULT_SUFFIX = " nanos";
	
	// These control output formatting.
	//private static final Version[] versions = new Version[] {Version.JT};
	private static Version[] versions;
	private static final int[] sizes = new int[] {1024};
	private static final int[] lengths = new int[] {10, 100};
	private static final int[] numClients = new int[] {900};
	
	public static void main(String[] args) throws Exception
	{
		String inFile = args[0];
		TimerFlag tf = TimerFlag.parseTimerFlag(args[1]); 
		int repeats = Integer.parseInt(args[2]); // "Inner" repeats.		
		String outFile = args[3];
		String vers = args[4];
		
		ResParser.versions = new Version[] {Version.parseVersion(vers)}; 
		
		List<Results> results = new LinkedList<Results>();
		
		parseResults(inFile, results, tf, repeats);
		
		System.out.println("Parsing summary: ");
		
		for (Results res : results)
		{
			System.out.println(res.params);
		}				
		
		//sort(results); // Already in order.
		
		writeResults(results, outFile);
	}
	
	private static void parseResults(String inFile, List<Results> results, TimerFlag tf, int repeats) throws IOException
	{
		BufferedReader in = null;

		try
		{
			in = new BufferedReader(new FileReader(inFile)); 
			
			String m; 
			
			do
			{
				in.mark(1024 * 4);
				
				m = in.readLine();				
			}
			while (Parameters.parseParameters(m) == null);

			in.reset();
			
			while ((m = in.readLine()) != null)
			{
				Results current = ((LinkedList<Results>) results).peekLast();				
				
				Parameters params = Parameters.parseParameters(m); 				 
								
				if (current == null || !params.equals(current.params))
				{
					params.trial = 0; // Should be 0 anyway.
					
					current = new Results(params, repeats);
					
					results.add(current);
				}
				
				long[] values = new long[repeats];
				
				for (int i = 0; i < repeats; i++)
				{
					values[i] = parseResult(in, tf);	
				}									
				
				current.addTrial(values);				
			}			
		}		
		finally
		{
			if (in != null)
			{
				in.close();
			}
		}		
	}
	
	private static long parseResult(BufferedReader in, TimerFlag tf) throws IOException
	{
		long init = 0;
		long body = 0;
		long close = 0;
		
		String m = null;
		
		m = in.readLine();
		
		if (tf == TimerFlag.INIT || tf == TimerFlag.FULL) 
		{
			m = m.substring(INIT_PREFIX.length());
			m = m.substring(0, m.length() - RESULT_SUFFIX.length());
			
			init = Long.parseLong(m);
		}
		
		m = in.readLine();
		
		//if (tf == TimerFlag.INIT || tf == TimerFlag.BODY || tf == TimerFlag.FULL) 
		{
			m = m.substring(BODY_PREFIX.length());
			m = m.substring(0, m.length() - RESULT_SUFFIX.length());
			
			body = Long.parseLong(m);
		} 
		
		m = in.readLine();
		
		if (tf == TimerFlag.FULL) 
		{
			m = m.substring(CLOSE_PREFIX.length());
			m = m.substring(0, m.length() - RESULT_SUFFIX.length());
			
			close = Long.parseLong(m);
		}		
		
		return init + body + close;
	}
	
	private static void writeResults(List<Results> results, String outFile) throws IOException
	{
		FileWriter fw = null;
		PrintWriter pw = null; 
		
		try
		{
			fw = new FileWriter(outFile);
		  pw = new PrintWriter(fw);	
			
			for (Version version : versions)
			{
				for (int size : sizes)
				{
					for (int length : lengths)
					{
						for (int clients : numClients)
						{
                            pw.print(size + ",");
                            pw.print(length + ",");
							pw.print(clients + ",");
							
							Results res = filterResults(results, new Parameters(version, clients, size, length, -1));
							
							for (int i=0; i<res.results.length; ++i)
							{
								pw.print(res.results[i]);
                                if (i != res.results.length - 1) pw.print(',');
							}
							
							pw.println();
						}
					}
				}
			}
		}
		finally
		{
			if (pw != null)
			{
				pw.flush();
				pw.close();
			}
			
			if (fw != null)
			{				
				fw.close();				
			}
		}		
	}	
	
	private static Results filterResults(List<Results> results, Parameters params)
	{
		for (Results res : results)
		{
			if (res.params.equals(params))
			{
				return res;
			}
		}
		
		throw new RuntimeException("[ResParser] No results found for parameters: " + params);
	}	
}

class Results
{
	public Parameters params;	// Should initially have trial as 0.
	public long[] results;	
	public int trials = 0;
	
	public Results(Parameters params, int repeats)
	{
		this.params = params;
		this.results = new long[repeats];
	}
	
	public void addTrial(long[] toAdd) // FIXME: doing too much rounding overall, should use e.g. BigDecimal.
	{
		for (int i = 0; i < results.length; i++)
		{
			long x = results[i] * trials;
			
			results[i] = Math.round(((double) (x + toAdd[i])) / ((double) (trials + 1)));
		}
		
		trials++;
		params.trial++; // These two should be the same value (so a bit redundant perhaps).
	}
		
	public String toString()
	{
		return params.toString() + "\n" + Arrays.toString(results);
	}
}

class Parameters
{
	private static final String PARAMETERS_HEADER = "PARAMETERS:";
	private static final String VERSION_HEADER = "VERSION=";
	private static final String CLIENTS_HEADER = "CLIENTS=";
	private static final String SIZE_HEADER = "SIZE=";
	private static final String LENGTH_HEADER = "LENGTH=";
	private static final String TRIAL_HEADER = "TRIAL=";
	
	public ResParser.Version version;
	public int clients; // numClients;
	public int size; // serverMessageSize;
	public int length; // sessionLength;
	public int trial; // As a result of parsing, this field contains the trial number. But we overwrite this field from the outsie when we use this class to contain the total number of trials.

	public Parameters(ResParser.Version version, int clients, int size, int length, int trial)
	{
		this.version = version;
		this.clients = clients;
		this.size = size;
		this.length = length;
		this.trial = trial;
	}
	
	//Parameters: version=JE, clients=10, size=100, length=1, trial=0
	public static Parameters parseParameters(String m)
	{
		m = m.toUpperCase();
		
		if (!m.startsWith(PARAMETERS_HEADER))
		{
			return null;
		}
		
		m = m.substring(PARAMETERS_HEADER.length());
		
		String[] params = m.split(",", -1);
		
		ResParser.Version version = null;
		int clients = -1; 
		int size = -1; 
		int length = -1; 
		int trial = -1;
		
		for (String param : params)
		{
			param = param.trim();
			
			if (param.startsWith(VERSION_HEADER))
			{
				version = ResParser.Version.parseVersion(param.substring(VERSION_HEADER.length()));
			}
			else if (param.startsWith(CLIENTS_HEADER))
			{
				clients = Integer.parseInt(param.substring(CLIENTS_HEADER.length()));
			}
			else if (param.startsWith(SIZE_HEADER))
			{
				size = Integer.parseInt(param.substring(SIZE_HEADER.length()));
			}
			else if (param.startsWith("LENGTH="))
			{
				length = Integer.parseInt(param.substring("LENGTH=".length()));
			}
			else if (param.startsWith(TRIAL_HEADER))
			{
				trial = Integer.parseInt(param.substring(TRIAL_HEADER.length()));
			}
			else
			{
				throw new RuntimeException("[ResParser] Unrecognised parameter: " + param);
			}
		}
		
		return new Parameters(version, clients, size, length, trial);
	}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof Parameters))
		{
			return false;
		}
		
		Parameters them = (Parameters) o;
		
		return (version == them.version && clients == them.clients && size == them.size && length == them.length); // Not including trial.
	}
	
	public String toString()
	{
		return "Parameters[version=" + version + ", clients=" + clients + ", size=" + size + ", length=" + length + ", trial=" + trial + "]";
	}
}
