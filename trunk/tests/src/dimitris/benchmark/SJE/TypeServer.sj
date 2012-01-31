package sessionj.benchmark.SJE;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class TypeServer implements Server {

  protocol request ?(String)
  protocol sInt !<int>
  protocol sStr !<String>
  protocol sObj !<Object>
  protocol type {@(sInt), @(sStr), @(sObj)}
  protocol reqRep @(request).@(type)
  protocol serverSide sbegin.@(request).@(type)

  private long start, end;

  private Object oparse(String str){
    return (Object) new String(str);
  }

  private String sparse(String str){
    int numEnd = str.indexOf(' ', 7); 
    return str.substring(7, numEnd);
  }

  private int iparse(String str){
    int numEnd = str.indexOf(' ', 7); 
    String strNum = str.substring(7, numEnd);
    return Integer.parseInt(strNum);
  }

  public void server(int port, int numClients) {
    final noalias SJSelector sel = SJRuntime.selectorFor(reqRep);
    noalias SJServerSocket ss;
    noalias SJSocket s;
    int i; String x, str; Object o;
    try (ss) {
      ss = SJServerSocket.create(serverSide, port);
      try (sel) {
        sel.registerAccept(ss);
        while (numClients-- != 0) {
          try (s) {
            s = sel.select();
            str = (String) s.receive();
            try(s) {
              typecase(s){
                when (@(sInt)) {
                  i = iparse(str);
                  s.send(i);
                }
                when (@(sStr)) {
                  x = sparse(str);
                  s.send(x);
                }
                when (@(sObj)) {
                  o = oparse(str);
                  s.send(o);
                }
              }
            }  catch (Exception e) {}  finally {}
          }  catch (Exception e) {} finally {}
        }
      }  catch (Exception e) {} finally {}
    }  catch (Exception e) {} finally {}
  }

  public static void main(String args[]) throws Exception{
    TypeServer s = new TypeServer();

    s.start = System.nanoTime();
    s.server(1234,1222);
    s.end = System.nanoTime();
  }

}
