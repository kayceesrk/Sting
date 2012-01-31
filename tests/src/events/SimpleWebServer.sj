import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class SimpleWebServer {
    // change: protocol implies final noalias
    protocol pServerToClient { ?<Get>.!{OK200: !<String>, ERROR500: !<String>} }

	public void run(int port) throws SJIOException {
		using (noalias SJServerSocket{sbegin.pServerToClient} ss = SJServerSocket.create(port);
               final noalias SJSelector{pServerToClient} selector = SJSelector.create(pServerToClient))
        {
			selector.registerAccept(ss);

			while (true) {
				using (noalias SJSocket{pServerToClient} s = selector.select(SJSelector.ACCEPT)) {
					// Only one case, hence no typecase needed.
                    readResourceAndReply(s);
				} catch (SJIOException e) {
				    write500Error(s);
				}
			}
		}
	}
}
