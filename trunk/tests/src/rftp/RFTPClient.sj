//$ bin/sjc tests/src/rftp/RFTPClient.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ rftp.RFTPClient -s localhost 8888 -m active -g testfile

package rftp;

import java.io.*;
import java.util.*;

import sj.runtime.*;
import sj.runtime.net.*;
import sj.runtime.util.*;

public class RFTPClient
{
	private static final int ACTIVE = 1;
	private static final int PASSIVE = 2;

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 8888;
	private static final int DEFAULT_MODE = ACTIVE;

	private static final int CHUNK_SIZE = 4;

	private protocol p_binsend { ![!<byte[]>]* }
	private protocol p_binrecv { ?[?(byte[])]* }

	private protocol p_control	
	{
		![
			!{
				GET:
					!<String>
					.!{
						ACTIVE:  !<begin.@p_binsend>,
						PASSIVE: ?(begin.@p_binrecv)
					},
				PUT:
					!<String>
					.!{
						ACTIVE:  !<begin.@p_binrecv>,
						PASSIVE: ?(begin.@p_binsend)
					}
			}
		]*
	}
	
	private protocol p_rftp
	{ 
		begin		
		.!<String>.!<String>
		.?{
			LOGIN_OK:   @p_control,
			LOGIN_FAIL:
		}
	}	
	
	private int mode = DEFAULT_MODE;
	
	public RFTPClient(String host, int port, int mode, LinkedList get, LinkedList put) throws Exception
	{			
		this.mode = mode;
	
		BufferedReader br = null;
		
		SJServerAddress c =	SJServerAddress.create(p_rftp, host, port);
		SJSocket s_rftp = SJRSocket.create(c);

		try (s_rftp)
		{
			s_rftp.request();

			br = new BufferedReader(new InputStreamReader(System.in)); // Should have some way to type local I/O streams.
						
			System.out.print("User: ");
						
			String uid = br.readLine(); 
			
			System.out.print("Password: ");
			
			String passwd = br.readLine();

			s_rftp.send(uid);
			s_rftp.send(passwd);

			s_rftp.inbranch()			
			{
				case LOGIN_OK:
				{
					<s_rftp>.spawn(new ControlThread(get, put));
				}
				case LOGIN_FAIL:
				{
					System.out.println("Login rejected.");
				}
			}				
		}
		finally
		{
			if (br != null) br.close();
		}
	}

	private class ControlThread extends SJThread
	{
		private LinkedList get;
		private LinkedList put;
		
		public ControlThread(LinkedList get, LinkedList put)
		{
			this.get = get;
			this.put = put;
		}
	
		public void run(@p_control s_control) throws Exception
		{			
			protocol pa_sendfile { begin.@p_binsend }							
			protocol pa_recvfile { begin.@p_binrecv }		
			
			protocol pp_sendfile { begin.@p_binsend }		
			protocol pp_recvfile { begin.@p_binrecv }			
		
			boolean doneGet = get.isEmpty();
			boolean donePut = put.isEmpty();
			boolean done = doneGet && donePut;					
						
			s_control.outwhile(!done)
			{
				if (!doneGet)
				{
					s_control.outbranch(GET)
					{
						String filename = (String) get.remove(0);
					
						s_control.send(filename);

						doneGet = get.isEmpty();

						if (mode == ACTIVE) 
						{
							s_control.outbranch(ACTIVE)
							{																														
								String host = s_control.getLocalAddress().getHostName();
								int v = SJRuntimeUtils.findFreeTCPPort();

								SJServerAddress c_active1 = SJServerAddress.create(pa_sendfile, host, v);															
								SJServerSocket ss_active = SJRServerSocket.create(pa_recvfile, v);

								s_control.send(c_active1);

								SJSocket sa_recvfile = null;

								try (sa_recvfile)
								{
									sa_recvfile = ss_active.accept();

									<sa_recvfile>.spawn(new BinaryReceiveThread(filename));									
								}
								finally
								{
									if (ss_active != null) ss_active.close();
								}																				
							}
						}
						else // if (mode == PASSIVE)
						{
							s_control.outbranch(PASSIVE)
							{			
								SJServerAddress c_passive1 = (@pp_recvfile) s_control.receive();
								SJSocket sp_recvfile = SJRSocket.create(c_passive1);							

								try (sp_recvfile)
								{
									sp_recvfile.request();

									<sp_recvfile>.spawn(new BinaryReceiveThread(filename));
								}
								finally
								{

								}							
							}
						}
					}
				}					
				else //if (!donePut)
				{				
					s_control.outbranch(PUT)
					{				
						String filename = (String) put.remove(0);
					
						s_control.send(filename);

						donePut = put.isEmpty();

						if (mode == ACTIVE) 
						{
							s_control.outbranch(ACTIVE)
							{																														
								String host = s_control.getLocalAddress().getHostName();
								int v = SJRuntimeUtils.findFreeTCPPort();

								SJServerAddress c_active2 = SJServerAddress.create(pa_recvfile, host, v);															
								SJServerSocket ss_active = SJRServerSocket.create(pa_sendfile, v);

								s_control.send(c_active2);

								SJSocket sa_sendfile = null;

								try (sa_sendfile)
								{
									sa_sendfile = ss_active.accept();

									<sa_sendfile>.spawn(new BinarySendThread(filename));									
								}
								finally
								{
									if (ss_active != null) ss_active.close();
								}																				
							}
						}
						else // if (mode == PASSIVE)
						{
							s_control.outbranch(PASSIVE)
							{			
								SJServerAddress c_passive2 = (@pp_sendfile) s_control.receive();
								SJSocket sp_sendfile = SJRSocket.create(c_passive2);							

								try (sp_sendfile)
								{
									sp_sendfile.request();

									<sp_sendfile>.spawn(new BinarySendThread(filename));
								}
								finally
								{

								}							
							}
						}				
					}
				}
				
				done = doneGet && donePut;
			}
		}

		private class BinaryReceiveThread extends SJThread
		{	
			private String filename;
			
			public BinaryReceiveThread(String filename)
			{
				this.filename = filename;
			}
		
			public void run(@p_binrecv s) throws Exception
			{
				byte[] filebytes = new byte[0];
			
				s.inwhile()
				{
					byte[] bytes = (byte[]) s.receive();
				
					/*for (int i = 0; i < bytes.length; i++)
					{				
						System.out.print(bytes[i] + " ");
					}
					
					System.out.println();*/
					
					byte[] tmp = new byte[filebytes.length + bytes.length];
					
					System.arraycopy(filebytes, 0, tmp, 0, filebytes.length);
					System.arraycopy(bytes, 0, tmp, filebytes.length, bytes.length);
					
					filebytes = tmp;
				}
				
				writeFile(filename, filebytes);
			}
		}		
		
		private class BinarySendThread extends SJThread
		{	
			byte[] filebytes;
			
			public BinarySendThread(String filename)
			{
				try
				{
					this.filebytes = readFile(new File(filename));
				}
				catch (Exception x)
				{
					
					System.out.println("File could not be read: " + filename);
					
					throw new RuntimeException("File could not be read: " + filename);
				}
			}	
			
			public void run(@p_binsend s) throws Exception
			{		
				int i = 0;

				s.outwhile(i < filebytes.length)
				{					
					int length = (filebytes.length - i < CHUNK_SIZE) ? filebytes.length - i : CHUNK_SIZE;
					
					byte[] bytes = new byte[length];
					
					System.arraycopy(filebytes, i, bytes, 0, length);
					
					s.send(bytes);

					i += length;	
				}
			}			
		}
	}	

	private static byte[] readFile(File file) 
	{
		InputStream is = null;
		
		try
		{
			is = new FileInputStream(file);

			int length = (int) file.length();

			byte[] bytes = new byte[length];

			for (int offset = 0, numread = 0; offset < length && numread >= 0; offset += numread) 
			{
				numread = is.read(bytes, offset, length - offset);
			}
			
			return bytes;			
		}
		catch (IOException ioe)
		{
			throw new RuntimeException("File could not be read: " + file);
		}
		finally
		{
			try { if (is != null) is.close(); } catch (Exception x) { } 
		}
	}	

	private static void writeFile(String filename, byte[] filebytes)
	{
		BufferedWriter out = null;
	
		try 
		{
			out = new BufferedWriter(new FileWriter(filename));
			
			out.write(new String(filebytes));
		} 
		catch (IOException e) 
		{
			throw new RuntimeException("File could not be written: " + filename);
		}
		finally
		{
			try { if (out != null) out.close(); } catch (Exception x) { }
		}
	}

	public static void main(String[] args) throws Exception
	{			
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		int mode = DEFAULT_MODE;
	
		LinkedList get = new LinkedList();
		LinkedList put = new LinkedList();
	
		for (int i = 0; i < args.length; )
		{
			if (args[i].equals("-s"))
			{
				host = args[++i];
				port = Integer.parseInt(args[++i]);
				
				i++;
			}
			else if (args[i].equals("-passive"))
			{
				mode = PASSIVE;
				
				i++;
			}
			else if (args[i].equals("-g"))
			{
				while (++i < args.length && !args[i].startsWith("-"))
				{
					get.add(args[i]);
				}
			}			
			else if (args[i].equals("-p"))
			{
				while (++i < args.length && !args[i].startsWith("-"))
				{
					put.add(args[i]);
				}
			}			
			else if (args[i].equals("--help"))
			{
				System.out.println("Usage: RFTPClient [OPTIONS] [-g FILES] [-p FILES]");
				System.out.println();
				System.out.println("Options:");
				System.out.println("  -s host port            Specify target server.");
				System.out.println("  -passive                Use passive mode.");
				System.out.println("  --help                  Display this information.");
				System.out.println();
				System.out.println("Examples:");
				System.out.println("  RFTPClient -s localhost 8888 -g foo.txt");
				System.out.println("  RFTPClient -s host 1234 -passive -g file1 file2 -p file3");
				System.out.println();
				
				System.exit(0);				
			}
			else
			{
				throw new RuntimeException("Invalid parameter: " + args[i]);
			}
		}
	
		new RFTPClient(host, port, mode, get, put);
	}
}
