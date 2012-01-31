//DISABLED
//$ ant -Dsessionj.transports.negotiation=s -Dsessionj.transports.session=a -Dtestcase=Selector functionaltest  
package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * New feature: selector
 * (required by events implementation)
 */
public class Selector extends AbstractValidTest3Peers {
    protocol from2 ?(int)
    protocol pClient2 cbegin.^(from2)
    protocol from3 ?(boolean)
    protocol pClient3 cbegin.^(from3)
    protocol pSel { @(from2), @(from3) }
    protocol pServer2 sbegin.@(from2)
    protocol pServer3 sbegin.@(from3)

    public void peer1(int port) throws Exception {
        final noalias SJSelector sel = SJRuntime.selectorFor(pSel);
        noalias SJServerSocket ss1, ss2;
        noalias SJSocket s;
        try (ss1, ss2) {
            ss1 = SJServerSocket.create(pServer2, port);
            ss2 = SJServerSocket.create(pServer3, port+1);
            try (sel) {
                sel.registerAccept(ss1);
                sel.registerAccept(ss2);
                int i = 0; boolean b; int j;
                while (i < 2) {
                    try (s) {
                        s = sel.select();
                        typecase (s) {
                            when (@(from2)) j = s.receiveInt();
                            when (@(from3)) b = s.receiveBoolean();
                        }
                    } finally {}
                    i++;
                }
            } finally {}
        } finally {}
    }
    
    public void peer2(int port) throws Exception {
        final noalias SJService serv = SJService.create(pClient2, "", port);
        noalias SJSocket s;
        try (s) {
            s = serv.request();
            s.send(42);
        } finally {}
    }

    public void peer3(int port) throws Exception {
        final noalias SJService serv = SJService.create(pClient3, "", port+1);
        noalias SJSocket s;
        try (s) {
            s = serv.request();
            s.send(false);
        } finally {}
    }
}
