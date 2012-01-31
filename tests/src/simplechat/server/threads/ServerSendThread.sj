//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/server/threads/ServerSendThread.sj -d tests/classes/

package simplechat.server.threads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import simplechat.server.*;

public class ServerSendThread extends SJThread
{	
	private MessageLog log;
	private int i = 0; 
	
	public ServerSendThread(MessageLog log) 
	{ 
		this.log = log;
	}
	
	public void srun(noalias ^(SimpleChatProtocols.p_client_receive) s)
	{
		try (s)
		{
			s.inwhile()
			{
				String msg = log.getMessage(i++);
				
				s.send(msg);
			}
		}		
		catch (SJIOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		finally
		{

		}
	}
}
