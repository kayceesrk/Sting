//package sessionj.benchmark.SJthread;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import java.util.concurrent.*;


public class RequestServer implements Server {

  public static final noalias protocol request ?(String).!<int>
  protocol serverSide sbegin.@(request)

  private int throughput = 0;

  private long start, end;


  static class ServerThread extends SJThread {

    private int parse(String str){
      int numEnd = str.indexOf(' ', 7); 
      String strNum = str.substring(7, numEnd);
      return Integer.parseInt(strNum);
    }

		public void srun(noalias @(request) s) {
			try (s) {
        String str = (String) s.receive();
			  s.send(parse(str));
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
    RequestServer s = new RequestServer();
    s.start = System.nanoTime();
    s.server(1234, 1234);
    s.end = System.nanoTime();
  }

}
