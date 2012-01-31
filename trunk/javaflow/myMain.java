import org.apache.commons.javaflow.Continuation;


class myMain{

    static class myRunnable implements Runnable {
	public void run() {
	    System.out.println("started!");
	    Continuation.suspend();
	    System.out.println("hello");
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
