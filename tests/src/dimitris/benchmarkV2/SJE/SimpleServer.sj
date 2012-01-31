// sessionj -Dsessionj.transports.session=a sessionj/benchmark/SJE/SimpleClient

//package sessionj.benchmark.SJE;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class SimpleServer implements Server {

  protocol getInt ?(int)
  protocol sendInt !<int>
  protocol serverSide sbegin.^(getInt)

  private long start, end;

  public void server(int port, int numClients) {
    final noalias SJSelector sel = SJRuntime.selectorFor(sendInt);
    noalias SJServerSocket ss;
    noalias SJSocket s;

    try (ss) {

  //    SJSessionParameters params = SJTransportUtils.createSJSessionParameters("a", "a");

      ss = SJServerSocket.create(serverSide, port);
      try (sel) {
        sel.registerAccept(ss);
        while (numClients-- != 0) {
          try (s) {
            s = sel.select();
            s.send(5);
          } catch (Exception e) {System.out.println("1");} finally {}
        }
      }  catch (Exception e) {e.printStackTrace();}  finally {}
    }  catch (Exception e) {System.out.println("3");}  finally {}
  }

  public static void main(String []args) {
    SimpleServer s = new SimpleServer();
    s.start = System.nanoTime();
    s.server(1234, 1234);
    s.end = System.nanoTime();
  }

}
