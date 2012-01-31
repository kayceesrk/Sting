package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class MultiServerSocketTry extends AbstractValidTest3Peers
{
    protocol p sbegin.?(int)
    protocol dualp ^(p)

	public void peer1(int port) throws Exception
	{
	    noalias SJServerSocket ss1;
	    noalias SJServerSocket ss2;
	    noalias SJSocket s1; noalias SJSocket s2;
	    try (ss1, ss2) {
	        ss1 = SJServerSocket.create(p, port);
	        ss2 = SJServerSocket.create(p, port+1);
            try (s1,s2) {
                s1 = ss1.accept();
                s2 = ss2.accept();
                int a = s1.receiveInt();
                int b = s2.receiveInt();
                assert a == 43;
                assert b == 42;
            } finally {}
        } finally {}
	}
	
	public void peer2(int port) throws Exception
	{
	    noalias SJSocket s;
	    final noalias SJService c = SJService.create(dualp, "", port);
	    try (s) {
            s = c.request();
            s.send(43);
        } finally {}
	}

	public void peer3(int port) throws Exception
	{
        noalias SJSocket s;
	    final noalias SJService c = SJService.create(dualp, "", port+1);
	    try (s) {
	        s = c.request();
	        s.send(42);
        } finally {}
    }
}

