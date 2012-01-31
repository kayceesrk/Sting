//$ javac -cp tests/classes/ tests/src/qe/session/QEStreamSession.java -d tests/classes/

package qe.session;

import qe.channel.*;

import java.util.*;

// Unidirectional communication from Requestor to Acceptor.
public class QEStreamSession extends QESession
{
	// ins/outs length should match the session length.
	public QEStreamSession(boolean debug, QECell[] ins, QECell[] outs)
	{
		super(debug, ins, outs);
	}

	public void run()
	{
		int len = getSessionLength();

		if (isRequestor())
		{
			/*if (len != outs.length)
			{
				throw new RuntimeException("[QEExchangeSession] Bad number of cells.");
			}*/

			int size = getMessageSize();

			Message msg;

			for (int i = 0; i < len; i++)
			{
				msg = new Message(i, size);

				outs[i].write(msg);
			}
		}
		else
		{
			/*if (len != ins.length)
			{
				throw new RuntimeException("[QEExchangeSession] Bad number of cells.");
			}*/

			Message msg;

			for (int i = 0; i < len; i++)
			{
				msg = ins[i].read().copy(); // Do copy here because QESessions don't use channels.

				if (debug)
				{
					msg.println();
				}
			}
		}
	}
}
