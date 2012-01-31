//$ bin/sessionj -ea -cp tests/classes -Dsessionj.transports.negotiation=s -Dsessitransports.session=a ClientRunner Type 1234 localhost 1 1

import java.util.concurrent.*;
import java.util.Random;

class ClientRunner implements Runnable {

  private static int port;
  private static String domain;
  private static int repetitions;

  private static long[][] times;
  private static String[][] results;

  private static int client;

  private int threadNum;

  private static ExecutorService exec = null;

  private static Random generator = new Random(System.currentTimeMillis());

  private static void setClient(String c) {
    if (c.equals("Simple"))
      client = 1;
    else if (c.equals("Request"))
      client = 2;
    else if (c.equals("Type"))
      client = 3;
  }

  private static Client nextClient() {
    int x;
    switch (client) {
      case 1:
        return new SimpleClient();
      case 2:
       return new RequestClient();
      case 3:
        switch ((x = generator.nextInt() % 3) > 0 ? x : -x) {
          case 0: return new IntClient();
          case 1: return new StringClient();
          case 2: return new ObjectClient();
        }
    }
    return null;
  }

  public ClientRunner(int threadNum) {
    this.threadNum = threadNum;
  }

  public void run() {
    int i;
    for(i = 0; i < repetitions; i++) {
      results[threadNum][i] = nextClient().client(domain, port, times, threadNum, i);
    }
    for(i = 0; i < repetitions; i++) {
      System.out.println("Thread number: " + threadNum + ". Client Number: " + i + ". Result: " + results[threadNum][i] + ". Time: " + times[threadNum][i]);
    }
  }

  public static void main(String[] args) {
    int i;

    if (args.length != 5) {
      System.out.println("Usage: java ClientRunner <server name> <port> <domain name> <core number> <repetitions>");
      return;
    }

    setClient(args[0]);

    port = Integer.parseInt(args[1]);
    domain = args[2];
    int numCores = Integer.parseInt(args[3]);
    repetitions = Integer.parseInt(args[4]);

    exec = Executors.newFixedThreadPool(numCores);

    times = new long[numCores][repetitions];
    results = new String[numCores][repetitions];


    for(i = 0; i < numCores; i++) {
      exec.execute(new ClientRunner(i));
    }
    exec.shutdown();
    
   // System.out.println(threadNum + ", " + i + ": " + "->" + times[i]);

  }

}
