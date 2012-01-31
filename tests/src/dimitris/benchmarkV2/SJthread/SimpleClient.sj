//package sessionj.benchmark.SJE;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;


public class SimpleClient implements Client {

  protocol getInt ?(int)
  protocol clientSide cbegin.@(getInt)

  public String client(String domain, int port, long [][] times, int i, int j) {
    final noalias SJService serv = SJService.create(clientSide, domain, port);
    times[i][j] = System.nanoTime();
    int x = 0;
    noalias SJSocket s;
    try (s) {
      s = serv.request();
      x = s.receiveInt();
      System.out.println("client:" + x);
    } catch (SJIOException e) {e.printStackTrace();}
      catch (SJIncompatibleSessionException ee) {ee.printStackTrace();}
      finally {}
      times[i][j] = System.nanoTime() - times[i][j];
      return "" + x;
  }

  public static void main(String [] args) {
    long [][] t = new long[1][1];
    new SimpleClient().client("", 1234, t, 0, 0);
  }
}
