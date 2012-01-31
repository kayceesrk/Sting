//package sessionj.benchmark.SJthread;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import java.util.concurrent.*;


public class TypeServer implements Server {

  protocol request ?(String)
  public static final noalias protocol sInt !<int>
  public static final noalias protocol sStr !<String>
  public static final noalias protocol sObj !<Object>
  protocol type {@(sInt), @(sStr), @(sObj)}
  public static final noalias protocol threadp @(request).@(type)
  protocol serverSide sbegin.@(request).@(type)

  private int throughput = 0;

  private long start, end;


  static class ServerThread extends SJThread {

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


	 	public void srun(noalias @(threadp) s) {
			try (s) {
        String str = (String) s.receive();
			  typecase(s){
          when (@(sInt)) {
            s.send(iparse(str));
          }
          when (@(sStr)) {
            s.send(sparse(str));
          }
          when (@(sObj)) {
            s.send(oparse(str));
          }
        }
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
    TypeServer s = new TypeServer();
    s.start = System.nanoTime();
    s.server(1234, 1234);
    s.end = System.nanoTime();
  }

}
