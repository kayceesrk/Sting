package sessionj.runtime.transport.tcp;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.Set;

class ChangeRequest {
	final SelectableChannel chan;
	final ChangeAction changeAction;
	final int interestOps;
	final AsyncConnection conn;

	ChangeRequest(SelectableChannel sc, ChangeAction register, int opRead) {
		this(sc, register, opRead, null);
	}

	ChangeRequest(SelectableChannel chan, ChangeAction changeAction, int interestOps, AsyncConnection conn) {
		assert chan != null;
		assert changeAction != null;
		this.chan = chan;
		this.changeAction = changeAction;
		this.interestOps = interestOps;
		this.conn = conn;
	}

	boolean execute(SelectingThread thread, Set<SelectableChannel> modified) throws IOException {
		return changeAction.execute(thread, this, modified);
	}
}
