package benchmarks;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import benchmarks.BigObject;

public class FClient {
    final private SJProtocol p =
      new SJProtocol(
      ("H4sIAAAAAAAAAFvzloG1uIhBvTi1uDgzPy9Lr6SyILVYD8SFsIK9nJ1S0zPz" +
       "QoC8+GQGCGBkYmCo\nKGJQw6MLSZOqQPIqHY01VRBNGng0BUOk0O1i9GFgTc" +
       "7IzEkpYdDwgWnXB2vSh2vXR9FuDbRJGc0m\nEJmflJWaXAKyC2LJxfXz8jlV" +
       "f2+FuE20ID+nMj0nvwSqA6LGUXV7qM+L/fIQNdJY1PiDTY1P/mMm\nI3vJ8Q" +
       "knM8jJHAX5xZklQPtLGCR8YJr0S0syc/QDoDLWFQUFFcDg18QTJv6lJeVAv6" +
       "diiwBVPPp8\n8vMLIHqYXU4umqF5axs4IFmS8lMqCxnqGJgrQCRTQUVBMf5I" +
       "CUpNTs0sw2q/KR5tvkB2Ynqqc35u\nbmleZnJiCTxiVzodZvTh3ncf7B7uXI" +
       "g6kFQJgygioCCRColLJJfKoAV/QE5icqpHfk5KalF88vLH\np2pcvTs7ID7N" +
       "S8wFGinkk5VYlqifk5iXrh9cUpSZlw4M9RIGkaTUvOSM3MSi7GI9p8x0SAwC" +
       "AIhC\nVzkOAwAA"));
    
    public FClient(String server, int port, int msgSize, int sessLen)
          throws Exception {
        super();
        final SJService c = SJService.create(p, server, port);
        SJSocket ds = null;
        SJSocket s = null;
        long timeStarted = -1;
        long timeFinished = -1;
        try {
            ds = c.request();
            int k1 = 0;
            while (SJRuntime.outsync(k1++ < 1, new SJSocket[] { ds })) {
                System.out.println("Client Receiving...");
                BigObject bo =
                  (BigObject) SJRuntime.receive(new SJSocket[] { ds });
            }
            boolean foo = true;
            timeStarted = System.nanoTime();
            s = c.request();
            int k = 0;
            while (SJRuntime.outsync(k++ < sessLen, new SJSocket[] { s })) {
                System.out.println("Client Receiving2...");
                BigObject bo =
                  (BigObject) SJRuntime.receive(new SJSocket[] { s });
            }
            timeFinished = System.nanoTime();
        }
        finally {
            SJRuntime.close(new SJSocket[] { ds, s });
        }
        System.out.println(Math.round((float) (timeFinished - timeStarted) /
                                        1000.0));
    }
    
    public static void main(String[] args) throws Exception {
        new FClient(args[0], Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1225483842000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVYe2wcxRmfvTvf2fGF5BwnpLHjvJxi0+QMqOkDI8AxceOw" +
       "IcbnpOCK2nu7c3fj\nzO1ud2bPlygCAhJJqdQKkVCqtiBVlaqiSG2DoJVapZ" +
       "VIH/ShqvkjoErQP6gKUguCf9qI0qrfzOzd\n7u1drpZ2bnbne833+M03vvAu" +
       "6mEeyjPMGHHslTw/4WImR6e4gk3O8oVDc4bHsDVNDcYWYGHJXP4v\nOfaj3G" +
       "MHE0hbRAO2M0WJwRYqnuOXKwsVwuoe2u469ESZOjyQ2Cbj9l0frf7+zKGhJF" +
       "q3iNYRu8AN\nTsxpx+a4zhdRtoqrReyxKcvC1iLK2RhbBewRg5KTQOjYoJiR" +
       "sm1w38NsHjOH1gThAPNd7EmdjY86\nypqOzbjnm9zxGEfr9RWjZkz4nNAJnT" +
       "A+qaN0iWBqsS+hh1FCRz0lapSBcJPe2MWElDgxI74D+RoC\nZnolw8QNltRx" +
       "YlscbYtzNHc8ei8QAGuminnFaapK2QZ8QAPKJGrY5YkC94hdBtIexwctHG25" +
       "rlAg\n6nUN87hRxkscbY7TzakloOqTbhEsHG2Mk0lJELMtsZhFonUknf3Pk3" +
       "P/2g4RB5stbFJhfxqYRmJM\n87iEPWybWDFe8/PnZh/0hxMIAfHGGLGimdr9" +
       "46P6Oz/fpmiGOtAckbm4ZH70qeGtV6b+2pcUZvS6\nDiMiFVp2LqM6F6xM1l" +
       "3I7k1NiWIx31j8xfyvHnz0Bfz3BOqdRWnToX7VnkV92Lamg3kG5jqxsfp6\n" +
       "pFRimM+iFJWf0o58B3eUCMXCHT0wdw1ekfO6ixDKwKPB8xRSfxvEwNGamWlK" +
       "sM3zbEXQ5upivGFV\n08DY4XjhUMiygw61sLdkfu+t3546cO+Xz6owiNQJdE" +
       "EGFcHplarhHWf5QDzSNClyg8gttfcpzzNO\niJyvn76y9Ru/Nr4NnoQdMXIS" +
       "S4O11ZQYgem2rpAwHRbULMwMiDfE8e1vnht6pOf9BEp0hAW9+XHG\n8aoGFZ" +
       "FVdQBhH40nXycN7z15+MWrv3tjLExDjkbbqqOdU2T3zrhjPcfEFqBHKP7rHx" +
       "58/+mez76U\nQCkoGQANbkCooQJH4jpasnyygRhiL0kd9Zdi2wPA4OCG1ciG" +
       "xZiV83Xg8F54IPtRIciTETHI1Ijk\nBwRlMLYHiUfXHk/f8trP+n8pndKArn" +
       "URjCtgrgohFybCgocxfH/j2bmnz7975gtJKD3XVcHnKO36\nRUrMOrDc2Fo7" +
       "YgeWyJ9/XJxc/9W97OUESi6iPlKt+twoUgxYa1DqrGJriUuwyUWATeIJOCtb" +
       "BFwC\niFuiIEi5w9VqkKcdKj+/efDcM+Pfek1ggyv9tRFsTElLU/J9AABd7k" +
       "voyCvwZO24NOeRKpR9LcCl\np0a++7cX35ofVJmkwHtXG35GeRSAy1D0u8I1" +
       "O7ppkNSXP7HjwsPzbxYVsA20uvKA7Vf3Pf86Hr87\na3ao5yQcMeIlL4e63O" +
       "qoJsaxphsa1Rq4YUPohgN1E7sC5KS9w6KgB8PgA6YdV2HMjhceOrR8dqeM\n" +
       "v3KqYLg51KzU19t0euiTXRFiRhyoUGPcAWQNS2z5K6/ff/iBz08ol9zaVcR9" +
       "4A9Lygn5z8/ad13a\neNVOiKpKs5Wmz3SUYJyjMb0hMoiheFWzwqGCWmqcd3" +
       "v+/wZCxeTUP09f/OP5rAS3DGEzxDaoSH12\nn8K0DmdhTMTJS0efu/YH/qaM" +
       "dggvwvyhejvyHzMiyPeZq7Vc+ofPVxMos4jWy37GsPkxg/qijBeh\nI2HTwU" +
       "cdrW1Zb+0u1FE62YTP4XjSR9TGgS0V8XZKUIt5r8Iyt65prpjtl9Qjctyhci" +
       "rBQY7wF0cZ\n1yM1Q/R4SHMbiTvUDITn25xUsWg6g9SR0KfJxP8cJN1NsZg1" +
       "wysOpf24TGxV3wGQairPPt6FK8K0\ne735wp6xCycV01gXpkgmRXUJADYrhF" +
       "rC3DtAxq6uKabY//SD55y+3f9+SWgN0WW8i/YjPl8FLbjT\nVnd34dMdx1U8" +
       "yXte/c6z439+WQFP0bFOKIvFqLvysOi2/Xls4iaUturf14XtMMyhH5124Liw" +
       "iSn7\neCXj+/tf0fT+S3+R9vRXFd1CS4ZJs4KEGYw0O/tJWeW0pNqugBKFQP" +
       "lpmYJI+HTr9RpcidZnHvgg\n+4Rx+aFGHG7hqI877l6Ka5h2wL+9XWN7WLb5" +
       "YQFfvu1r/QuJn3B5YHZsjjq0DbmAbh7DNccO/eGh\nbbGtxNXlakP3JyvkNw" +
       "lZt6rU2y4nrUyTrQW+xospFeOWZssinpvgORqE/2jHlqUdCsR8F7QYTN73\n" +
       "up9r7VcS2b+qlLmYHP0g8dNNo7JZSxUNpsyM3+Xar2otNzBp55rmrj4GzzZ4" +
       "1ga7Wqsa9n7dMQ06\n79tBx74i2W7ueBDfKQZS15BEQ3odFxyQfgAUTFNsl9" +
       "WdQZ61lfCoTSjyDsf7NHVsLPqtZkHINeLk\nm3dkWGw/tMX7F5V9UlekYmIh" +
       "QI2q6bYo3muA7aYwp0MzFtRl1yCL131S0jJHqSqciJJij6u4JuBj\nzSGWFq" +
       "llTflJjKfBg5nguiNAa3Pbvx3U5djceWV57BU396rKlsYFNgO3yJJPafRci8" +
       "zTrodLylWZ\nxiknfs7AFS5EIPHlrAz2qbr2P+MqCs1REQAA");
}
