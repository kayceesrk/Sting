package sessionj.runtime.net;

import sessionj.runtime.transport.SJConnection;

public interface SJSelectorInternal {
	void notifyInput(SJConnection connection);
	void notifyAccept(SJServerSocket sjss, SJConnection connection);
}
