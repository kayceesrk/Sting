package sessionj.benchmark.SJE;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;


public class SimpleClient implements Client {

  protocol getInt ?(int)
  protocol clientSide cbegin.@(getInt)

  public String client(String domain, int port) {
    final noalias SJService serv = SJService.create(clientSide, domain, port);
    int x = 0;
    noalias SJSocket s;
    try (s) {
      s = serv.request();
      x = s.receiveInt();
      //System.out.println("client:" + x);
    } catch (SJIOException e) {e.printStackTrace();}
      catch (SJIncompatibleSessionException ee) {ee.printStackTrace();}
      finally {}
      return "" + x;
  }

  public static void main(String [] args) {
    new SimpleClient().client("", 1234);
  }
}
