//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/SpinChannel.sj -d tests/classes/

package popl.bmarks.bmark2;

import popl.bmarks.*;

public interface SpinChannel extends Channel
{
	public int getSpins();
	public int getNospins();
}
