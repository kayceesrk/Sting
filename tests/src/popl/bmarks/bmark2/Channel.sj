//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/Channel.sj -d tests/classes/

package popl.bmarks.bmark2;

import popl.bmarks.*;

public interface Channel
{
	public Message read();
	public void write(Message msg);
}
