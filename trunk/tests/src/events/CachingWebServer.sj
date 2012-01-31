import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class CachingWebServer {
    // change: protocol implies final noalias
    public protocol pServerToClient { ?<Get>.!{OK200: !<String>, ERROR500: !<String>} }
    private protocol pServerToCache { ![!<Get>.?<String>]* } // doesn't include sbegin/cbegin
    private protocol pCacheToServer { ^pServerToCache }
    private protocol pSelect { pServerToClient, pServerToCache } // { } means set, possibly singleton

	private void runServer(int port) throws SJIOException, SJIncompatibleSessionException {
	    // new statement: using, from C#
		using (noalias SJServerSocket{sbegin.pServerToClient} ss = SJServerSocket.create(port);
		       // not done yet: {} annotation for socket/session socket/selector session types
		       // noalias SJSelector{pSelect} selector = SJRuntime.selectorFor())
		       noalias SJSelector selector = SJRuntime.selectFor(pSelect))
		{
			selector.registerAccept(ss);

			while (true) {
				using (noalias SJSocket{pSelect} s = selector.select(SJSelector.ACCEPT | SJSelector.RECEIVE))
				{
                    typecase (s) {
                        when (pServerToClient) {

                        }
                        when (pServerToCache) {

                        }
                    }
                }
			}
		} // can have catch and finally, none mandatory
	}

	private void runCache(int port) {
        using (noalias SJServerSocket{sbegin.pCacheToServer} ss = SJServerSocket.create(port)) {

        }
    }
}
