//$ bin/sjc tests/src/rftp/RFTPServer.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ rftp.RFTPServer 8888 tests/src/rftp/rep/

package rftp;

import java.io.*;
import java.util.*;

import sj.runtime.*;
import sj.runtime.net.*;
import sj.runtime.util.*;

public class RFTPServer
{
	private static final int CHUNK_SIZE = 4;

	/*private protocol asciisend { ![!<char>]* }
	private protocol asciisend { ?[?(char)]* }*/
	
	private protocol p_binsend { ![!<byte[]>]* }
	private protocol p_binrecv { ?[?(byte[])]* }

	private protocol p_control	
	{
		?[
			?{
				GET:
					?(String)
					.?{
						ACTIVE:  ?(begin.@p_binsend),
						PASSIVE: !<begin.@p_binrecv>
					},
				PUT:
					?(String)
					.?{
						ACTIVE:  ?(begin.@p_binrecv),
						PASSIVE: !<begin.@p_binsend>
					}
			}
		]*
	}
	
	private protocol p_rftp
	{ 
		begin		
		.?(String).?(String)
		.!{
			LOGIN_OK:   @p_control,
			LOGIN_FAIL:
		}
	}
	
	private HashMap users = new HashMap();
		
	private String rep;
		
	public RFTPServer(int port, String rep) throws Exception
	{			
		populateUsers();

		this.rep = (rep.endsWith("/")) ? rep : rep + "/";
	
		SJServerSocket ss = null;
		
		try
		{
			ss = SJRServerSocket.create(p_rftp, port);
			
			while (true)
			{			
				SJSocket s_rftp = null;

				try (s_rftp)
				{
					s_rftp = ss.accept();

					String id = s_rftp.receive();
					String pwd = s_rftp.receive();

					if (pwd.equals((String) users.get(id)))
					{
						s_rftp.outbranch(LOGIN_OK)
						{
							<s_rftp>.spawn(new ControlThread(rep));
						}						
					}
					else
					{
						s_rftp.outbranch(LOGIN_FAIL)
						{
							
						}						
					}				
				}
				catch (Exception x)
				{
					x.printStackTrace();
				}				
			}
		}
		finally
		{
			if (ss != null) ss.close();
		}
	}

	private class ControlThread extends SJThread	
	{
		private String rep;
		
		public ControlThread(String rep)
		{
			this.rep = rep;
		}
	
		public void run(@p_control s_control) throws Exception
		{
			protocol pa_sendfile { begin.@p_binsend }
			protocol pa_recvfile { begin.@p_binrecv }
								
			protocol pp_sendfile { begin.@p_binsend }
			protocol pp_recvfile { begin.@p_binrecv }		
		
			s_control.inwhile()
			{
				s_control.inbranch()
				{
					case GET:
					{
						String filename = rep + s_control.receive();

						s_control.inbranch()
						{
							case ACTIVE:
							{
								System.out.println("GET ACTIVE: " + filename);						
							
								SJServerAddress c_active1 = (@pa_sendfile) s_control.receive();
								SJSocket sa_sendfile = SJRSocket.create(c_active1);							
								
								try (sa_sendfile)
								{
									sa_sendfile.request();

									<sa_sendfile>.spawn(new BinarySendThread(filename));
								}
								finally
								{
								
								}
							}
							case PASSIVE:
							{
								System.out.println("GET PASSIVE: " + filename);						
	
								String host = s_control.getLocalAddress().getHostName();
								int v = SJRuntimeUtils.findFreeTCPPort();
							
								SJServerAddress c_passive1 = SJServerAddress.create(pp_recvfile, host, v);															
								SJServerSocket ss_passive = SJRServerSocket.create(pp_sendfile, v);

								s_control.send(c_passive1);
								
								SJSocket sp_sendfile = null;
								
								try (sp_sendfile)
								{
									sp_sendfile = ss_passive.accept();

									<sp_sendfile>.spawn(new BinarySendThread(filename));									
								}
								finally
								{
									if (ss_passive != null) ss_passive.close();
								}															
							}
						}
					}
					case PUT:
					{
						String filename = rep + s_control.receive();

						s_control.inbranch()
						{
							case ACTIVE:
							{
								System.out.println("PUT ACTIVE: " + filename);							
							
								SJServerAddress c_active2 = (@pa_recvfile) s_control.receive();
								SJSocket sa_recvfile = SJRSocket.create(c_active2);							
								
								try (sa_recvfile)
								{
									sa_recvfile.request();

									<sa_recvfile>.spawn(new BinaryReceiveThread(filename));
								}
								finally
								{
								
								}
							}
							case PASSIVE:
							{
								System.out.println("PUT PASSIVE: " + filename);						
	
								String host = s_control.getLocalAddress().getHostName();
								int v = SJRuntimeUtils.findFreeTCPPort();
							
								SJServerAddress c_passive2 = SJServerAddress.create(pp_sendfile, host, v);															
								SJServerSocket ss_passive = SJRServerSocket.create(pp_recvfile, v);

								s_control.send(c_passive2);
								
								SJSocket sp_recvfile = null;
								
								try (sp_recvfile)
								{
									sp_recvfile = ss_passive.accept();

									<sp_recvfile>.spawn(new BinaryReceiveThread(filename));									
								}
								finally
								{
									if (ss_passive != null) ss_passive.close();
								}															
							}
						}					
					}
				}
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

				//s.outwhile(i++ < 3)
				s.outwhile(i < filebytes.length)
				{
					//s.send(new byte[] { (byte) i });
					
					int length = (filebytes.length - i < CHUNK_SIZE) ? filebytes.length - i : CHUNK_SIZE;
					
					byte[] bytes = new byte[length];
					
					System.arraycopy(filebytes, i, bytes, 0, length);
					
					s.send(bytes);

					i += length;	
				}
			}
		}				
	}	
	
	private void populateUsers()
	{
		users.put("guest", "passwd");
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
		new RFTPServer(Integer.parseInt(args[0]), args[1]);
	}	
}
