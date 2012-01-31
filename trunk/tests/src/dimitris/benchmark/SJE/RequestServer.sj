package sessionj.benchmark.SJE;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class RequestServer implements Server {

  protocol reqRep ?(String).!<int>
  protocol serverSide sbegin.@(reqRep)

  private int throughput = 0;

  private long start, end;

  private int parse(String str){
    int numEnd = str.indexOf(' ', 7); 
    String strNum = str.substring(7, numEnd);
    return Integer.parseInt(strNum);
  }

  public void server(int port, int numClients) {
    final noalias SJSelector sel = SJRuntime.selectorFor(reqRep);
    noalias SJServerSocket ss;
    noalias SJSocket s;
    try (ss) {
      ss = SJServerSocket.create(serverSide, port);
      try (sel) {
        sel.registerAccept(ss);
        while (numClients-- != 0) {
          try (s) {
            s = sel.select();
            String str = (String) s.receive();
            int x = parse(str);
            s.send(x);
	    throughput++;
          }  catch (Exception e) {} finally {}
        }
      } catch (Exception e) {}  finally {}
    }  catch (Exception e) {} finally {}
  }

  public static void main(String args[]) throws Exception{
    RequestServer s = new RequestServer();

    s.start = System.nanoTime();
    s.server(1234,1000);
    s.end = System.nanoTime();
  }

}
