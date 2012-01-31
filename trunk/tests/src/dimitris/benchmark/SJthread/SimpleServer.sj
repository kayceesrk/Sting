package sessionj.benchmark.SJthread;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import java.util.concurrent.*;


public class SimpleServer implements Server {

  protocol getInt ?(int)
  public static final noalias protocol sendInt !<int>
  protocol serverSide sbegin.^(getInt)

  private int throughput = 0;

  private long start, end;


  static class ServerThread extends SJThread {
		public void srun(noalias @(sendInt) s) {
			try (s) {
			  s.send(5);
			}
			catch (Exception x){}
		}
	}

  public void server(int port, int numClient) {
    noalias SJServerSocket ss;
    noalias SJSocket s;
    try (ss) {
      ss = SJServerSocket.create(serverSide, port);
      while (numClient -- != 0) {
        try (s) {
          s = ss.accept();
          <s>.spawn(new ServerThread());
        } catch(Exception e) {e.printStackTrace();}
          finally {}
      }
    } catch(Exception e) {e.printStackTrace();}
      finally {}
  }

  public static void main(String args[]) throws Exception {
    SimpleServer s = new SimpleServer();
    s.start = System.nanoTime();
    s.server(1234,1234);
    s.end = System.nanoTime();
  }

}
