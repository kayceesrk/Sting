//$ javac -cp tests/classes/ tests/src/qe/session/StreamSession.java -d tests/classes/

package qe.session;

import qe.channel.*;

import java.util.*;

// Unidirectional communication from Requestor to Acceptor.
public class StreamSession extends Session
{
	public StreamSession(boolean debug, Channel in, Channel out)
	{
		super(debug, in, out);
	}

	public void run()
	{
		int len = getSessionLength();

		if (isRequestor())
		{
			int size = getMessageSize();

			Message msg;

			for (int i = 0; i < len; i++)
			{
				msg = new Message(i, size);

				out.write(msg);
			}
		}
		else
		{
			Message msg;

			for (int i = 0; i < len; i++)
			{
				//msg = in.read().copy(); // Do the copy here to promote asynchrony?
				msg = in.read(); // Copy refactored as a property of the channel.

				if (debug)
				{
					msg.println();
				}
			}
		}
	}
}
