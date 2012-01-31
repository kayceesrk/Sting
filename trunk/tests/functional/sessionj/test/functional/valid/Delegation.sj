package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * Simple session delegation (bug was introduced in runtime calls)
 */
public class Delegation extends AbstractValidTest3Peers {
    protocol startServer  { sbegin.?(int) }
    protocol middleClient { cbegin.!<int> }
    // delegate session
    protocol middleServer { sbegin.!< !<int> > }
    protocol endClient { cbegin.?(!<int>) }

    public void peer1(int port) throws Exception {
        final noalias SJServerSocket ss;
        final noalias SJSocket s;
        try (ss) {
            ss = SJServerSocket.create(startServer, port);
            try (s) {
                s = ss.accept();
                int i = s.receiveInt();
                assert i == 42;
            } finally {}
        } finally {}
    }
    
    public void peer2(int port) throws Exception {
        final noalias SJServerSocket ss;
        noalias SJSocket sClient, sServer;
        final noalias SJService chan = SJService.create(middleClient, "", port);
        try (ss) {
            ss = SJServerSocket.create(middleServer, port+1);
            try (sClient, sServer) {
                sServer = ss.accept();
                sClient = chan.request();
                sServer.send(sClient);
            } finally {}
        } finally {}
    }

    public void peer3(int port) throws Exception {
        final noalias SJService chan = SJService.create(endClient, "", port+1);
        final noalias SJSocket s1, s2;
        try (s1, s2) {
            s1 = chan.request();
            s2 = (!<int>) s1.receive();
            s2.send(42);
        } finally {}
    }

}
