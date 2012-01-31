package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client2 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1ba4wcRxHu3Xv6kbtz/AiOH5DEduwE9mwFhzh3tpN74bus" +
       "z0fu7IANsWd3+nbH\nNzuznu29h8QjECIcfliKQhIBQYkSUIR4SEZC8C9YAR" +
       "mBhMRTAYlfAYlI8AMhBBJGor6ene6Zvdn1\nicPiLp5ILld3dVdVV3dXfb3j" +
       "fPMvrK3isV0VXqlYrnM+IxbKvJJB0+cmxya5mCL2bP7qb/q/44jt\nb6VZKs" +
       "s6SryU415FsC3Z88as0VsVlt076No2zwvS1Dfvsb1NtUqRr5n5/6Wk5rZ80b" +
       "JNwfZmg+m9\nclKvmt4bmQ5Ld9VZAnVz58kV2PKNPNMx+dpjhS98Nc0Yzdhc" +
       "du2Fgu2K2gx/zMO7v3cy+6fX3+2P\n2RYz5oTUejZ/7f7tO37x8B/WtMDlzr" +
       "JbsbBqwd6VDSb5EZmoSfrmy+X5MoV6D6KVgSyjo1XZddIp\nuaY1bRk5m2et" +
       "irjWvfvAd/98qUeGpNWmHsF6QpHGGKz8vddXp/u3DrAnfvr4P3ZKpan8BfZJ" +
       "lpJO\nbdJaspYzw02oX79v8mNj5y7e2ULBKM+1YoNo6N3NNnWAF6y4PW0Xhl" +
       "fgtIRb/SXYhlPonRSe5RSw\niD1NdIZUPmi/cPVrE3+9iN2B7+nyfKX5KXuU" +
       "57k1y+s9wuYebDLtOPFGgQJXKlUdK28IdVJfO/jk\nm89/pvfvadZyhnVym5" +
       "e4I+gS7DwTWZiRn5nyjDwf9gf0Zdm6kq8TagTbrE+Jf6LlQVZxwuJ61BLx\n" +
       "90a51N1NL5RjLl4n5m4K6dC6moVt1JmjO7gobKk6B5s7lHXdsq9hZuyV01fG" +
       "/pn1z3LONRcwv6V+\ngeWqF8QxgzhmFscxPXJPf/+/7zyEpZXJqSO0kG1NJh" +
       "hrfvSlPff//qk0ax1la23L4eNVpK0s6zJ5\n3jZwAAdtoyJd6KGLPE3LHjdK" +
       "vNZeW+Ki6JqqB0bbrtJN1Danih43KF2t85kMJILdQkHS3kj1d5G/\n28R/ee" +
       "4Eu6OZuGa1vd9yLHFEmesWS78dgm2q65FKoepepfAImPeBZATrrMzQBRCUbg" +
       "LxWi0G2a8E\n7wezD+QeEbkKasTp8Ig1jmvyQdt1tPxnzZcSKSdyKZGeWnzW" +
       "CI/X6wXzAZAHQA6tcMGbWiBYa94t\nLyjRc2JpZZxOb6ilNvmgUvQimCGQ4Y" +
       "bGQQaUYK8IVWCj4tdL7GFQfceJh+UN4WZtU1pF+BwcFqFq\nUK+q5nWgbVNd" +
       "j1rLI0rdQyJUsGYtqsWkZsJzhZt37SFKAXQ/nYptCNcTbEcjUc3T20Sth0cH" +
       "Kmv7\nwUyAfIgOm82NWT5o2Fp+ieKkMIXvzbDnud4xwzEpORVOoQuebI3rVs" +
       "ubChSmesCcAnlMsDZpUBm7\nTAuqMzZICIXPC2VmY7RDGfiIMpAW7PY6JYi1" +
       "0tATatWitFEOGzYLtCsnZrnnWaZ26rdgHgc5SxFS\nI5X8WzRf2cP2B3u9Ln" +
       "Jm1sqZgwCKaurbYEwQHqgGilHyowTOIqpl4h+gaiTvRKgV5HBtxOM6yf1L\n" +
       "LwHkXNzaQHJ6UdozkGkleCnWJRwp7ZLfUltj3wg/3o4KikqwQEcx4uCkW/Xy" +
       "fISqpH+dQ83/iYuX\nw5vYJjdAyR4IH+hpD0fXMTO1wzdBoaKQhVq1XWzxqm" +
       "rv0lMiBOqVisl8kZtVm9N57lJ8bXoHTYc2\ndSP+BuZJkM/60g+6hlpx6vth" +
       "6TpDCF4qi8iIF/UIkKdXuMBuJNgXXmk3xWHKJWBSJkBghRDBN8L5\nQwUcAy" +
       "0Z71sCNgh33m8rBZ8H8xzI84Ktr0lx4NSOsDl6VigbJcNyMseJUA7AXzW9bR" +
       "UCrvoojYP5\nMohc+FeU4DZySdULzI8Z0wXmZSmg6gWDhEK3170VJ2zCfcdc" +
       "2+QevRre+snHhx95+qIPgB2NJgnU\nuF4hY5QNOnYZWl2JYiddnrbdOZmvLa" +
       "dq+E/LjrxtEaY9EALx+5qC+ByVq3zx+ii+2ZNuIFZH+jS7\n1aLURKXZJJfs" +
       "BQww8XY3aDLl8NBL9ZhRKR43yn0B3C8zcnyDfnDW5G0dv7vyxpZzP29h6RGC" +
       "6q5h\njhh5usqjhNoIWVeKFMv58tGHpAvr5zqJ9tCfNCnbrPyXCunpYeS4Hf" +
       "J2lHUWycoglZAsa7GNXPQh\n6r/p6YXO3vP122lLh08Nj+tHCZg7SNGx4AC0" +
       "XQWzG2QPyN0a3oPxQSyIxs3dYDQwjgXUIBoxLwVK\ng9wXh6BBNJ5bCpgECe" +
       "E5MKMgYyAaTh0SoSdfPTrzj0pQsDdGO2r3cItpVYxSzipUJYoKsJsycI1y\n" +
       "fP1uBiniJDUrEvVFelTduaJyUwrMGyA/jAjYrmZAFQ/WEFDVzcUW2LUGFhqa" +
       "/r8K2H1gjoOML20d\n7MPh91YscKbIDVQtJDjBdjaU1fa9K4fmiMVtM4KY+8" +
       "D8EuRXIArZsktgfGQbEYQhL4iCqj50OAPy\n0YgglQZzg4HSUTDnQWZAbghK" +
       "ewnMBRDvhtlojATByBS+sFzjl6M2PqEBHphPgTwB8mkN28BoEPJU\nHBgD+V" +
       "wcDgNZIWDq5hUsBUaCPKuRIxgf+IG8EIcIQb6ocSCYOFDXGO2BeTlG0BUVvC" +
       "IFr0Z+9/N/\nva5DZd9mqQPd9OY4MTSUAIhYAAHmdZAfgCyjoLJdYGQkf7xM" +
       "VauuNoPRRfPXSTVNqilLqunNI1jl\n1ZRIql1XznnNqlo5vFprpdQSfKDx/y" +
       "nDMuroDrHET6zqxRr53iW/tSplW5t/Qot89JXP7EhP8AhO\n6fTWgWYRxIoK" +
       "kKdSyEkp3jA6kJ67fmhWpqDhhsmbpL+dLeujGpg4DHUYjAYMoY9dYPzPTyCP" +
       "KsH+\nqCDBBQkuAElwAXtH1FMpoNfpBv06HXUEL+CXuaCyxtTYw++AGnthFV" +
       "SRpFgkxSIpFkmxYCuqWIR+\nyhxyqzmbx1SI/qRCJIWAJYUgKQRJIVgsWOWF" +
       "gEjqjzFJv285Sb/571X6/1GQv1fpZoN/ap6G+vS9\n6IqWiVjBKisTSTVIqk" +
       "FSDZJqwFZMNWiPqQYPJk+AJIWzJIUnKTxJ4Wzlp/BXY1K4SjI3cQpPMnWS\n" +
       "qZNMnWRqtmIydXvct1l5krr+A79sVW2VRQAA"));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20102);
            ss.participantName("client2");
            SJSocketGroup client1Socket = null;
            try {
                client1Socket = ss.accept("client1");
                System.out.println(
                  "Client2 accepted connection request from Client1");
                String str =
                  (String) SJRuntime.receive("client1", client1Socket);
                Integer ii =
                  (Integer) SJRuntime.receive("client1", client1Socket);
                SJRuntime.pass("Hi, from client2", "client1", client1Socket);
                Double d = (Double) SJRuntime.receive("client1", client1Socket);
                Double d2 =
                  (Double) SJRuntime.receive("client1", client1Socket);
                System.out.println("Client2 received:\nString: " + str +
                                   "\nInteger: " + ii + "\nDouble1: " + d +
                                   "\nDouble2: " + d2);
                {
                    SJRuntime.negotiateNormalInwhile("client1", client1Socket);
                    while (SJRuntime.insync("client1", client1Socket)) {
                        Integer i =
                          (Integer) SJRuntime.receive("client1", client1Socket);
                        System.out.println("Received: " + i);
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", client1Socket);
                            if (_sjbranch_$0.equals("ODD")) {
                                {
                                    String str3 =
                                      (String)
                                        SJRuntime.receive("client1",
                                                          client1Socket);
                                    System.out.println(str3);
                                }
                            } else
                                      if (_sjbranch_$0.equals("EVEN")) {
                                          {
                                              String str4 =
                                                (String)
                                                  SJRuntime.receive(
                                                    "client1", client1Socket);
                                              System.out.println(
                                                "Client2 received: " + str4);
                                          }
                                      } else {
                                          throw new SJIOException(
                                            "Unexpected inbranch label: " +
                                            _sjbranch_$0);
                                      }
                        }
                    }
                }
                Continuation co =
                  (Continuation) SJRuntime.receive("client1", client1Socket);
                System.out.println("Executing continuation code...");
                Continuation.continueWith(co);
            }
            catch (Exception ex) {
                System.out.println("client1Socket Exception: " + ex);
            }
            finally {
                SJRuntime.close(client1Socket);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Client2 a = new Client2();
        a.run();
    }
    
    public Client2() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1326279315000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cC5AcRbV39/535D75kP/3gESTPROBAhIIyeWOXNjcHblL" +
       "gItwmdvt25tkdmYy\n03u3oShAEBNBU0YIvwpEkU8hWGVCISoKUoBR8VNiGY" +
       "JWsAoohRJUylJSGsD3umd6Pru3icRoEvaq\n9t3rft2vf+/XM7vvsXdIuW2R" +
       "uE1tWzX0jXG2xaQ2h8bARppkdrxnVbdi2TTVqim23QuE/uSGD9R1\nexpvXB" +
       "klkT7SpBvLNFWxe4csI5se6h1S7ZxFZpqGtiWtGczhmMfjgjmHR362ddWUGK" +
       "nvI/Wq3sMU\npiZbDZ3RHOsjdRmaGaCWvSyVoqk+0qhTmuqhlqpo6jXQ0NBh" +
       "YFtN6wrLWtReQ21DG8aGTXbWpBYf\n061MkLqkodvMyiaZYdmMNCQ2KsNKS5" +
       "apWktCtdniBKkYVKmWsjeT60g0QcoHNSUNDSck3FW0cI4t\n7VgPzWtUmKY1" +
       "qCSp26Vsk6qnGJkR7iFX3HwpNICulRnKhgw5VJmuQAVpElPSFD3d0sMsVU9D" +
       "03Ij\nC6MwMnlUptCoylSSm5Q07WdkYrhdtyBBq2q+LdiFkfHhZpwTnNnk0J" +
       "n5Tqurou79W7rfmwknDnNO\n0aSG86+ATtNDndbQQWpRPUlFx0PZ+O0dV2an" +
       "RgmBxuNDjUWbZWc8uTbx5jMzRJspBdp0cVnsTx4+\nd+q0l5a9UR3DaVSZhq" +
       "2iKARWzk+126Eszpkg3RMkRyTGXeIP1/zoyhu+Qf8UJVUdpCJpaNmM3kGq\n" +
       "qZ5qdfBKwBOqTkVt1+CgTVkHKdN4VYXBy7Adg6pGcTvKATcVNsTxnEkIqYRP" +
       "BD69RPzVIGCkplVT\nqc4Wxe2NjCxi1GZ2i20lW8yslcrSFpBWpupZLub9NG" +
       "caFhTTLV6nHI4wZiQSgcVNDSuaBlK50tBS\n1OpPPvz6T69tu/QL28Sxoag5" +
       "c2PQjw8W9w+GR87HIJEIZz4WpVLs2jLLUragtuQ++9K0u/cp98IZ\nwF7Y6j" +
       "WULzUyUoYQOi0qakxaPVXsAEwBSelPjrvhzcn3/PqRF6IkVtCgJGRlu2FlFA" +
       "1lwtWgJme4\nMAVEqTks0IXG/vMtqx/f/+LBuZ5oM9Kcp3H5PVFjZoc33zKS" +
       "NAUWyWN/5z9X/vW28vOfiJIyUEPc\nbQXEB7R6eniMgOYsdq0QriWWILWDeQ" +
       "uvYbBBI74FI6xD0CAEBM5iXGiC3IAduqniUy9/v/YFvmLX\n1tX7jGIPZUJz" +
       "Gr3z77UohfqDd3XftvOdretjoKumKc6ckQozO6CpyRx0OT2obDi9FIrN23sX" +
       "N2xf\nYH+bH3G1mslkmTKgUTDOiqYZIzTVz7h1avRZQm6AYCfqBsCQgQr0a8" +
       "BIrNWMDIN4FjAV8Ynjbr9j\n3q6X0ZiYfGMmoNLxmZIcr2iOIJybR8TyNJTg" +
       "cd6yQf03iQXUzeu5atWGbbP5yv3s5juFnGTo6cLZ\nRXWhHZ0OyAwzwPp4Ir" +
       "Ph1gOXrb7i8hZhDxcWZdEJOp3ifLz+Ozv0pU+P369HUUoq7I1S7xMkajNG\n" +
       "5iZclo7kYVFgPat6BMn1CfOPvABvYPXaf3x27y931sHAfaRStdtVXdHwtO1O" +
       "ob0F/EWIxTVPr73v\n0M/Zq1w2PXXB6U/N5Vu7dYpPk8/bP9xY8a3dmSip7C" +
       "MN3OcrOlunaFmU3D7w2narU5kgpwXoQQ8s\n3M1iaQ6mhlXVN2xYUct8u12G" +
       "rRGvErrJ29R/KP4+wA8KDhaEX2hqNTImOBNr5iUUFq0wmjJzkYiJ\n3c7jna" +
       "dzOEvIX5TB0LjFjFSaljqsYOiEscmwyoQ9x8ZjGZkiD9HKgrXPUAzqHLHjli" +
       "LCNWIpCGxz\n6LylaMRRNJhw2Pv2L9mrs6mv80OqdCI1DJc8EwIuVIM95D7Y" +
       "Al0rxlUKXH/S8ZMRYZqSQ6qWwpkt\nAh5zikqi6L6jsufhy9O3PxB1tLnW5G" +
       "bsTE+fvXnZzWv1jJFSB1Vuh0DDD9efsfCJt7c3CE/g2RrQ\ngiMz8OonLSc3" +
       "vHj1e9M5m0gSebQ6pmi+NAxnFduR5TStFtqQCqZYacqED4dlFeHhY3GBdue+" +
       "B7vf\n3eZuygoT7XSxI1lDk1QdpuEZoDU6p0i31YBDuAlCnMnqapKLoODx8D" +
       "k3Hbjjxpa/g/FfT6qoRjMQ\nZYDETF8fiHvByvdaEL60iQagWrUZwbPXVaTQ" +
       "Jrjrwf+dfF1nFBU1PZW/KOzb5ePh8Sq2Rx36CEhn\n3h7ln1KxCSUMwxQcNq" +
       "26v+/ZVYcSQvYGjNQWIfehBULQ5m5aHDctnr9p0fZPLFny/uzzuQOESbXD\n" +
       "QqYU6aBUv3DPmecevBkClA5Sg7FtZxYVOkHGoPVT8DLCHbFj1Kow3O30jFyN" +
       "uNB0esElIeX74Jbl\njQmRHFXgdlQrkDhSGDkNNsmbDWd/Fcx3CvuIQsbIrG" +
       "JkZ9SKJaqusovkcPXs6FWBkXGhGs4UWXnz\nvwiRNIIhRqrsTSDtzDHFSK7x" +
       "yAg2SsLZiHBrgTvlk3vZos/folo3UrRVM3SP/oviSwkYWr6UQI2z\nP9UMAr" +
       "0QX0Q2I+B+2D7BCQc8AiNlScPcIkk72dE5OJBeX0kesiEZ7ULkOgTXjzo4gm" +
       "slYS7zeTDF\nFgEMnqHrvToBx5Eb/UXnUHggIVldyHymP8zKmbXLbVyoRq7l" +
       "ZsnuSr/YDKtwOfaFByvABGDv5VkV\nr5RgskelOXMdM4BFHtZhAznMYkRuQX" +
       "ArSJlGlWHaCqG/pG+HDZLRnZhGm2UZ1kpFT4FVSq/DKrgK\nk0mFquW6trsM" +
       "Iw2IfBnBDgiU+IBysD2MTAsN5jx6ksOMDVbIAW6TA0D8NTnEBDdZcmjwlZzN" +
       "Gcub\ntaXScBxdw9Sy1JQ3qVcQuQvB3bBDsqWkf5P5bjx47u4h1waEpYb3bM" +
       "XYSXZ9C5H7EOx2WWPEI+lL\nGZkYYM0t/nJwQ1wZfCXXeHuDwJ1V8vmXtwQE" +
       "9xRaG4Jd3qK8mSH4qiTsLjglFClvSqIkj+ah4zGP\nt4KE+yVhC4hiYII9Rt" +
       "ZK0nZwj0KPfcX/yhT3+A+xnB+ApJ3nF+hBC0VXT8Ud4euGrYIt85WcU4zB\n" +
       "fcBlEe1lvmdvkkVPcoimshqq/hiJO90roTtykxrxN0R+gOBpQb3EUKSGR77j" +
       "p9YqjNGMyQItdnkt\nEDxXIvxPCNpohHn+E6uH8+w18IoKEY3qC2ke9dtBKT" +
       "juXRZshYu6YpMUZcmAe4WXERxg+LCeU1Fx\nPL86ArdjOUZGUfX4agBgy/Cf" +
       "w7ccbuSWpxKdiBxE8CqC30vC6TAl6fCwf4E2YxB5jRPA/eKA7mV6\nrmGl44" +
       "qpgC7EYaoZfGyK4w9qxgh3Iu7TVFwof5y60HelmFf0SjFgKXpy6Mh3imK3x+" +
       "UFeUT7SJMK\n9hIChRRMSdsiHszBHVuBzuBYfHf3lYo9tFoxF7uXD5MEHwU6" +
       "9PLK3z773IQNv4qRaDtcHAwl1a7w\nB6QQQ0Kcbw8ZWipnLr2YT6FupApgAz" +
       "61AGbj5fw5Q7gIKQNU8822g1QNwSit4NcSJKYpAziVc2Em\nMx+ZDOfRtq6t" +
       "07sSIbIeOq50T698HyL9CDYgULzLBSIihEbgiWA9Il5YXjCcR+DF60cTyCPI" +
       "FIrf\nEXjR5NGEsgh80SQiNyH4HAIvmDuf+S6c4dhQiIYbNYwNVjhKNCGl2k" +
       "pmQE2DEFMvFJUDHAZHEz49\nV7/XQtHmMWegxnV+kYg0LPi8KRJDUBYgkOZi" +
       "YTJel31hslfMH4EcHmWEUYf+vxLIpxHZimDb0a2D\nXIGIiKcRfLFQoI1ABs" +
       "RkOyIiIA4Q/JEyAhnhiohjJ4I7AoRIFJHjHF8tReQBBA8iOC7BHS8+iuCx\n" +
       "4zbG6AEkInsRPH6sg+8JjvGkFxci8l0E30PwlBftIeL5/GcKxXAIni0UviE4" +
       "QWKXjy/haKI2BL/x\nAjVERJyF4JVCARiC33lhFyKFYqjRgytEXitAGBMkvO" +
       "HGUw3+Z5P4sDEUN/2FRBbWw1Wla8WKkssv\n6PIR+RBAhGw+NhdImrFYiaDq" +
       "GFmVvCkpedOSNy1505OFcJJ7UwCRVs9z5jxU+srVJ6uvXICI+0JH\nfCngGP" +
       "zoNHaUr2TlHTPwfoy/m5XMJhV/5RZ4ScwvxoEaeW39umTInS9a6MhDQcLVWP" +
       "wagvtH3R2k\n3nvkrTkxCaMeGPeY3ru2Y3oJh0ihGOpCRGTA4H85hkgpLijF" +
       "BaW44ONJOMnjAk6AW3ajd8vu0BlN\n43sZN0IoECu0nQKxwuaTwBuWnF7J6Z" +
       "WcXsnpnWCEU8Tp+R4trzCyAxot4OlWlDxdyaGRkkMrObSS\nQzt1CSe5QwMQ" +
       "XVTAebUei/Mq/hzU+60Mfw7qFUf5yUMUrXf0BqwKuruChJPM3ZW8Wsmrlbxa" +
       "yaud\nYIST36v531lKr7b8WLwaIqfylazkikquqOSKSq7oBCOcAq7ozQKuaF" +
       "nJFZU8Dil5nJLHKXmcE4xw\nCnic1kLfweC/ulwlMr4E0j3xHE1znBRm+FV/" +
       "J4WZm3/Mtnw/AQ9lcOTZJ7de8W7d55XnrxKZfJqC\nidra9GzmnN0H6LyL65" +
       "IF0gVWM8NcoNFhKn6sjVnOoiIJFQy7oGhqptU8P4yXpivWs3TeWfNr/xgl\n" +
       "ZaMk+2t0KtdQlrV0X+4faK38x1kAZ4Q2JTyfxuEpl8WG1B9HefoukfErL49n" +
       "sNPiYJ6vGis4UX6E\nU/gE6mGL8JevTfCZTMQf/4/ERgRNvDWKR2yVONtR0t" +
       "MVJLqp5rA8ljkJG/kbz7Zckpr4O2TkH1vn\n/Ng/PwFbt6VmVCYz2uyY/sAf" +
       "Hn99zTghAyKr6Zy8xKL+PiKzKV9FLf+N86xiI/DWz39y1mPXrXl1\nwEm/FO" +
       "tipGzYUFO4uJgSlOpwIdYjNxY/Z8Gny9nYrkIbm69I3HvPYaTC5jlni29sfl" +
       "pUnglTbNbe\nWPO70acmNPPkjmUDii2TVAXyyeaniw1kgeXzrJGrmgSfGcVX" +
       "FWktKAAYJsbMXIRgxriYNcrKLxR2\nBNavUT3Nhvim9oscU7EBkBOYLaJGLk" +
       "/V86WMZwrC/GcuTeROVI24zNYLxPzUiDhAr5guH+sjir8v\ndWPsevxyuExc" +
       "FHz9LzL65YoeNfL4DOeUdn7yz6UxTyYjvs0J1OCX7p0Eqph1bmJYDZxEvcnZ" +
       "L22Y\n+5zZ+BMhNW4y3UpMrZXVNH/+QB9eYVp0UOWiUslhndi57bDlhVK5Mp" +
       "7h0CvzHf+S6LODZ+zEPlj6\nCpeWm3ORfwOfh5DxDVoAAA==");
}
