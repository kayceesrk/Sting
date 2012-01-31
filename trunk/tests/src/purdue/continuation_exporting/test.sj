//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Test1
package purdue.continuations;
import org.apache.commons.javaflow.Continuation;


class Test1{
  
  static class myRunnable implements Runnable {
     public void run() {
       System.out.println("started!");
       for( int i=0; i<10; i++ )
         echo(i);
     }
     private void echo(int x) {
       System.out.println(x);
       Continuation.suspend();
     }
  }

  public static void main(String[] args) {
    Continuation c = Continuation.startWith(new myRunnable());
    System.out.println("returned a continuation");
    Continuation d = Continuation.continueWith(c);
    System.out.println("returned another continuation");
    while(d!=null) {
	d = Continuation.continueWith(d);
    }
  }
}