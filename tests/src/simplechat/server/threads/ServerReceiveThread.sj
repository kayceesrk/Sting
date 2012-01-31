//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/server/threads/ServerReceiveThread.sj -d tests/classes/

package simplechat.server.threads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import simplechat.server.*;

public class ServerReceiveThread extends SJThread
{	
	private MessageLog log;
	
	public ServerReceiveThread(MessageLog log) 
	{ 
		this.log = log;
	}
	
	public void srun(noalias ^(SimpleChatProtocols.p_client_send) s)
	{
		try (s)
		{
			s.inwhile()
			{
				String msg = (String) s.receive();
				
				log.addMessage(msg);
			}
		}		
		catch (SJIOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		finally
		{

		}
	}
}
