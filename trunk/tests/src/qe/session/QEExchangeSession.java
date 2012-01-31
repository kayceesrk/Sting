//$ javac -cp tests/classes/ tests/src/qe/session/QEExchangeSession.java -d tests/classes/

package qe.session;

import qe.channel.*;

import java.util.*;

// Bidirectional exchanges between Requestor and Acceptor.
public class QEExchangeSession extends QESession
{
	public QEExchangeSession(boolean debug, QECell[] ins, QECell[] outs)
	{
		super(debug, ins, outs);

		if (ins.length != 1 || ins.length != 1)
		{
			throw new RuntimeException("[QEExchangeSession] Bad number of cells.");
		}
	}

	public void run()
	{
		int len = getSessionLength();

		if (isRequestor())
		{
			int size = getMessageSize();

			Message msg;

			for (int i = 0, count = 0; i < len; i++)
			{
				msg = new Message(count, size);

				outs[0].write(msg);

				msg = ins[0].read().copy(); // Do copy here because QESessions don't use channels.

				count = msg.getCount() + 1;

				if (debug)
				{
					msg.println();
				}
			}
		}
		else
		{
			Message msg;

			for (int i = 0; i < len; i++)
			{
				msg = ins[0].read().copy();

				if (debug)
				{
					msg.println();
				}

				msg = new Message(msg.getCount() + 1, msg.getSize());

				outs[0].write(msg);
			}
		}
	}
}
