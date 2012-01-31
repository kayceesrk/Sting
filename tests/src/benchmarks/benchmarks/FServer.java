package benchmarks;

import java.io.*;
import java.net.*;
import sessionj.runtime.*;
import sessionj.runtime.net.*;
import benchmarks.BigObject;

public class FServer {
    final private static int[] msgSizes = { 100, 10000 };
    final private static int[] sessLens = { 0, 1, 10, 100, 1000 };
    final private SJProtocol p =
      new SJProtocol(
      ("H4sIAAAAAAAAAFvzloG1uIhBvTi1uDgzPy9Lr6SyILVYD8SFsIK9gp1S0zPz" +
       "QoC8+GQGCGBkYmCo\nKGJQw6MLSZOqQPIqHY01VRBNGvisgkih28Xow8CanJ" +
       "GZk1LCoOED064P1qQP166Pot0aaJMymk0g\nMj8pKzW5BGQXxJKL6+flc6r+" +
       "3gpxm2hBfk5lek5+CVQHRI2j6vZQnxf75SFqpLGo8QebGp/8x0xG\n9pLjE0" +
       "5mkJM5CvKLM0uA9pcwSPjANOmXlmTm6AdAZawrCgoqivGHiWdeOdDrqdjCXx" +
       "WPNp/8/AKI\nHmaXk4tmaN7aBg5HlqT8lMpChjoG5goQyVRQUVCM36Dg1LwU" +
       "bJab4tHjC2Qnpqc65+fmluZlJieW\nwCN1pdNhRh/ufffBjuHOhagDSZUwiC" +
       "ICCRKhkHhEcqYMWtAH5CQmp3rk56SkFsUnL398qsbVu7MD\n4s28xFygkUI+" +
       "WYllifo5iXnp+sElRZl56cAQL2EQSUrNS87ITSzKLtZzykyHxB4AyXr4MAoD" +
       "AAA="));
    private ServerSocket dummy = null;
    
    public FServer(int port) throws Exception {
        super();
        this.dummy = new ServerSocket(port + 1);
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(p, port);
            int run = 0;
            int id = 0;
            while (true) {
                System.out.println("Run: " + run++);
                for (int i = 0; i < msgSizes.length; i++) {
                    for (int j = 0; j < sessLens.length; j++) {
                        SJSocket ds = null;
                        SJSocket s = null;
                        try {
                            ds = ss.accept();
                            while (SJRuntime.insync(new SJSocket[] { ds })) {
                                System.out.println("Server Sending....");
                                SJRuntime.pass(new SJSocket[] { ds },
                                               new BigObject(-1, msgSizes[i] -
                                                               86));
                            }
                            s = ss.accept();
                            while (SJRuntime.insync(new SJSocket[] { s })) {
                                System.out.println("Server Sending2...");
                                SJRuntime.pass(new SJSocket[] { s },
                                               new BigObject(id++, msgSizes[i] -
                                                               86));
                            }
                        }
                        finally {
                            SJRuntime.close(new SJSocket[] { ds, s });
                        }
                    }
                }
            }
        }
        catch (SJIncompatibleSessionException ise) {
            System.err.println(" incompatible client: " + ise);
        }
        catch (SJIOException ioe) { System.err.println(" IO error: " + ioe); }
        finally {
            if (ss != null) ss.close();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new FServer(Integer.parseInt(args[0]));
    }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1225483971000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK1YfWwcRxWfvTvf2fGl8Rk7hNhJnNhpHXDOUEjVYqFim1hx" +
       "sm5cn5s2hsjZ2507\nj71f7Myez1FVKAhSWj5UNSlFbVOpalVRRQJS9UMqSi" +
       "s1BcqHEPkjrZBa/igCJChq/4EICuLNzO59\n7G2v/2BpxrO7782b9/V7b+78" +
       "O6iDeihPMaXEsVfzbMPFVMxOcRXrjOYLh+c1j2Jj2tQoXYQPy/rJ\n/5JjP8" +
       "l97VACKUuo13YmTaLRxRXP8csriyuEVj005DrmRtl0WLBjyx6fHX5//denDw" +
       "8k0ZYltIXY\nBaYxok87NsNVtoSyFraK2KOThoGNJZSzMTYK2COaSU4BoWOD" +
       "YErKtsZ8D9MFTB2zwgl7qe9iT8gM\nX6ooqzs2ZZ6vM8ejDPWoq1pFG/cZMc" +
       "dVQtmEitIlgk2DfhndhRIq6iiZWhkIt6qhFuNix/EZ/h7I\nNxE4plfSdByy" +
       "pNaIbTC0K8pR03jkCBAAa8bCbMWpiUrZGrxAvfJIpmaXxwvMI3YZSDscH6Qw" +
       "tP0D\nNwWiTlfT17QyXmZoW5RuXn4Cqi5hFs7CUH+UTOwEPtse8VmDt46ms/" +
       "+5d/6fQ+BxOLOBdZOfPw1M\nOyNMC7iEPWzrWDJe9fNnZo/7gwmEgLg/Qixp" +
       "Jvc+f5v6l5d2SZqBGJqjIhaX9fdvGNxxefKPXUl+\njE7XoYSHQpPmwqvzwZ" +
       "eJqgvRvbW2I/+YDz++vPCz4199Gv81gTpnUVp3TN+yZ1EXto3pYJ2BtUps\n" +
       "LN8eLZUoZrMoZYpXaUc8gzlKxMTcHB2wdjW2ItZVFyGUgaHAOIPkXy+fGNo0" +
       "A6FcwV6ernLaXJXP\n16wrChx2MJo4JkTZIcc0sLesP/X2L+88eORb90g38N" +
       "AJZEEEFcHoK5bmrdF8sD1SFLHlR3hsSd0n\nPU/b4DFfvfvyjh/8XHsULAka" +
       "UXIKiwMr6yk+A9P1bSFhup5Qs7DSwN/gxz8/fGbgKx3vJlAiFhbU\n2ssZx7" +
       "M0k3tW5gG4fSQafHES/n7v3DNXfvXmaD0MGRppyY5WTh7de6KG9RwdG4Ae9e" +
       "2//69D7z7Q\ncdOzCZSClAHQYBq4GjJwZ1RGU5RPhIjBdUmqqLsUUQ8Ag4EZ" +
       "1hsU5nNWrLeAwTthQPSjE0Gc7OGT\nCI2G+ACn9EV0EHh09evpT77+0+5XhV" +
       "FC6NrSgHEFzGQi5OqBsOhhDO/ffGj+gbPvnP5iElLPdaXz\nGUq7ftEkehVY" +
       "PtqcO1wDg8fP3y5M9Hx3P30ugZJLqItYls+0ookBazXTdNaxscwE2OQagE3g" +
       "CRgr\nWwRcAohbNmEjaQ5XqUCcxmR+flvfmQf3PfI6xwZX2Ku/KUyj8DPvEQ" +
       "uyuxLAz/07n/jTM28v9MmA\nkRg93AKTjTwSp4XFu11ugd3tJAjqS5/Yff6u" +
       "hbeKEr96my120PatA4+9gfd9PqvHpG0SKklV6DWi\n8Hm0RUf+3MuCJOZ2zB" +
       "+s6tjlCCZOOcjN0Ff3LADWmvRRdl/hxOGT9+wRzpW7cYaxarUmJCVep2CH\n" +
       "sbb5PsPLYz1RyJ3/uPvCb89mRaZnCJ0htmbyOKC3yASPKQyRLU5dvO3c1d+w" +
       "t4RN6rnGDzZQbYXB\nY1oDDNx4pZJL//gxK4EyS6hHFHfNZsc00+cxvQTlmU" +
       "4HL1W0uel7c6mVdWWihiWD0dBoEBvN8rof\nYc2p+bozkthbYdwAoy9I7L5I" +
       "YiuKyxefEww7xbxbuirJQBw3K0MZ1yMVjcHZ01R0SVWGOi1aLgBq\nU9patw" +
       "XIy/i/kBx5L/Hi1hGBaKmiRuUpow1Paz/T1KYIpTa54t9YbIDe5FYVJFQ51K" +
       "pKgq8n+TQM\nJ0+b2C7LIjnGp5l6NCYkeUzIT5uOjTnAhN9kwBMnX2sK4WM1" +
       "BiB2RYwzJ/Sqx1KuMnBrcoX8IiF8\nKsOgpYtrZppodv4mD0MTai82hcB2aa" +
       "0ZPg3F5jcSR0TxyR9+5M+LEAk61z/UvaduFxnAsj4oSBDc\n2BR8N8PoD4Kv" +
       "Pz74vhQTfDWPTfFpmgccxwcV27SmVqvALhjDMIYCgUPxArVWgUL9KRDTYfiW" +
       "tRFq\n2i80tTHLy46m4OhrWJSMaxAC537mw0ELyjxzoLmre/zkfW/cOnfH7e" +
       "MSrj/VdotbwM1GBLnOzto3\nX+y/Yid4nqTpakMoJChjaFQNtwyihz/KVeFw" +
       "QX6SLbewnhuaZa1d5nDbKG5ol4HaoT3fZsTC/JYW\nKCpdIwxqg4mui+hXOw" +
       "ywFKZwmdgSKQKXKdIq17bhamDa26M/PTZ6/pRkGm0nqq53oyzesegrxDT4\n" +
       "cVdhj+G27pDsv/vROadr77+fTQQJIut0O+mz9joIwXGa7m3DpjqOK3mSX3jt" +
       "8Yf2/f45WcGLjrEh\nD8xn6ormqt1GBbg+xAk/0IZnDtZweZt2oLeyiS4uvX" +
       "KPH069oqjdF/8gDtNtSbpm+BFnCqGy4WYw\nRcoNkBEAE6pjz4EQlDy044Nu" +
       "g6LnOX3He9lvapdOhD7IM9TFHHe/iSvYjIHh/W39GgXlS9d/r3sx\n8QIT3W" +
       "XsTSKmx84FdAsxcLxQwyg+roOxHHhiObbVjs/EYQmGH9quHaxJ+xiMXTA2B9" +
       "I2ywtgt+ro\nmrng28EN8DvVGGyXvwPEl1s+fTssufe3L7l8PlIrt/e1lFv+" +
       "OMeno631U9hOChPM/4dy9iCfjvNp\nqdrWlPzx04LlGwylLGgPBcXHXcm1H1" +
       "5WHGIoDYGrSA35fA4AMxNchHl6bmv5QUr+bKLvuXxy9BU3\n95pskcKfNjIq" +
       "6iz5ptnY5DWs066HS0QIysiWT9b7J+ByX083/uZJV6qq/A/DlunfaxMAAA==");
}
