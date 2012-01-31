package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * New feature: session set types
 * (required by events implementation)
 */
public class SessionSetTypes extends AbstractValidTest3Peers {
    protocol startServer  sbegin.?(int)
    protocol middleClient cbegin.!<int>
    // delegate session
    protocol middleServer sbegin.!< !<int> >
    // receive of delegated session, two possible types
    protocol endClient cbegin.?({!<int>, !<boolean>})
    //protocol endClient cbegin.?(!<int>)

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
            s2 = ({!<int>, !<boolean>}) s1.receive();
            typecase (s2) {
                when (!<int>) { s2.send(42); }
                when (!<boolean>) {
                    s2.send(true);
                    assert false : "Should not get here";
                }
            }
        } finally {}
    }

}
