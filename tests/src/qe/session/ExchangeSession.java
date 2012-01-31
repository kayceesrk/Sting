//$ javac -cp tests/classes/ tests/src/qe/session/ExchangeSession.java -d tests/classes/

package qe.session;

import qe.channel.*;

import java.util.*;

// Bidirectional exchanges between Requestor and Acceptor.
public class ExchangeSession extends Session
{
	public ExchangeSession(boolean debug, Channel in, Channel out)
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

			for (int i = 0, count = 0; i < len; i++)
			{
				msg = new Message(count, size);

				out.write(msg);

				msg = in.read();

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
				msg = in.read();

				if (debug)
				{
					msg.println();
				}

				msg = new Message(msg.getCount() + 1, msg.getSize());

				out.write(msg);
			}
		}
	}
}
