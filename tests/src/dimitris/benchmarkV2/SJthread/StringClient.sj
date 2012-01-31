//package sessionj.benchmark.SJE;


import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import java.util.Random;

public class StringClient implements Client {

  protocol reqRep !<String>.?(String)
  protocol clientSide cbegin.@(reqRep)

  private static Random generator = new Random(System.currentTimeMillis());
  private String requestString;

  public StringClient() {
    requestString = new String("Number " + (generator.nextInt() % 1024) + " is beeing send");
  }

  public String client(String domain, int port, long [][] times, int i, int j) {
    final noalias SJService serv = SJService.create(clientSide, domain, port);
    times[i][j] = System.nanoTime();
    String x = null;
    noalias SJSocket s;
    try (s) {
      s = serv.request();
      s.send(requestString);
      x = (String) s.receive();
    } catch (SJIOException e) {}
      catch (SJIncompatibleSessionException ee) {}
      catch (ClassNotFoundException cnf) {}
      finally {}
      times[i][j] = System.nanoTime() - times[i][j];
      return (String) x;
  }
}
