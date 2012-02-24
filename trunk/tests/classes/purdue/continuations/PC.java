package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;

public class PC {
    final private SJProtocol onlineShopping =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1Yb2gcRRSfu8s1uaSm16at1qYVa1IbkTtLbbEmtvauiU16" +
       "+YOXWG3pn7ndyd02\nezvr7lxyARFRhKpQFERBBEFaCrYiFUS/SREpKAj6Qa" +
       "rgp/rBgn4QEQUj+N7+md29Xq4q6icDeXkz\nb+b33rz35s3bXPiBJG2L9NnM" +
       "tjVunMyIRZPZGRy6XHGsyMQ0sMeVy18OvWuI3qtxEiuQ9iqrlphl\nC7K+cJ" +
       "LO02xNaHo2z3WdKQKQBusW2dYS1RG5yMT9iTnISaWi6aog2wr+9qyzKSu3Zy" +
       "PbUdMdDZqQ\n8tJJMAV1uUpeai+eO1R++UycENixzuT6YlnnwtvhrtnX//5M" +
       "4bsPb3PXbGyyZtJBPa4s7erd9MW+\nb1MJNLnD5LaGpxbkloK/yfXIlCcZrJ" +
       "tm3QRXb0VvZVCWCbxl980YVa5qsxot6ayg2WJpVf/2974/\nnXZc0qbDjCDp" +
       "kKdxDZ787hvDBfMbcuSpT4/9stkBjSmPkydJzDFqbYBS0Iw5piL8yoHi0bET" +
       "p7Yk\nwBnmQhsGCJbe2SqoOVbWmsV0haBWmcER1rhH0KlRzhaFpRllPMTWFp" +
       "ghyPv1Vy+fnfrxFEYHbY+b\ndbt1lo0aC5BMbHmLECYNFvS3AClwbroIc2Nv" +
       "Hr409mvBDUqJq4u4P+Ebg54caGlNyaKGUrmxOa2c\nnGuKET9M1mj2fmYyQ2" +
       "WG0BdxgYq3icJmQXpCuXOA2pVxag5KuwkYvjpIAU+ebP/60kfrT3yeIPER\n" +
       "0qlzqo5QRXBrlKRExWJ2hetq3dz7oGPCyoUOoGn4jQPYOmm/Awg+pCWmh6wd" +
       "JR0V0JLnKiuQhE5L\n0dRwbxncGdJ3DkRtE5OTU6aN5vbC9llBErmZx24Q+4" +
       "eZwrT562KPF3tni23jwNMyXJpqtWZoChWy\nSp3b+cyVV57O/hwniSOkg+ms" +
       "Co4G124+EklqqsxNW1Rhw+6CwQLpqrqYCCPIuqBCuNXMKWINKeBH\npmb58B" +
       "mEz1wPHx+5a2jo9y278WQmHDEHbtnYYgNNffza1l3fPBsnbaMQVc1gEzWs5A" +
       "XSrTJFp3gn\n8zq1HW+nobbNwgWaoFXmjTurTFS4KmdQafIyFKdA5zRkB4UK" +
       "3uUyGZQIchMcLrDGgR8AezeKvxkO\nQW5vJfa0rhjSDE3skepWiT+fNIKsbZ" +
       "hxQBFquwTcg8wOJPcK0mHPQV4IqMC+uDMQI9klBc4wi+Qe\nEckQueJweEXK" +
       "gKuS17kRyD9rfZTIC+scJTLj+SclLNaAewWZB5DsgauncHNRiraJ0FNLbfdh" +
       "RMv8\nZ3YCeNS2Ojz0VLWJ8Ol2i1DRbYRya5wP1hOd8ODWq5pNqyWtXKOCBe" +
       "qkgiVBNjWWIcgTE9LZmoGh\n7fgkMiPDOypR+lqdGF+G0ImDYROgJWQOIilE" +
       "BLHYMgKyQ4QexkbVXlfma1/bMPMXDCCPhvNoXoN+\nBfCnLC64wvX9UBMQNl" +
       "eDpoxZUO2WlXlx6S7hcERjuooLpJpBZA4hAYUpndF5lqd6ID8NnpYNl2vG\n" +
       "sGVx6wA1VChT5UdwioMFG5pNywMfkY5NI3MUyTFBko5CqewiJEeDsjy0b6wu" +
       "pJqe6IRUcEIqiAty\nawMIel8ipEMjzzk9zrJhtQxxmpxnlqWFMvYrZJyqCi" +
       "9cSq6U8rdhv9SHCeFHvytyMTqdnXnsouXW\na8jMIdF9aGzxpHwvdK4RaOcJ" +
       "yEGHg/jp0Miv5oESiwXl7rfgCEjKzc6GRAsOFViGpCoFbzQ1CVMq\nMMkdyd" +
       "DY/4Yd16ICLgWLkIoRA4u8ZilsBMqJWxBCw3/ExIvhICadAEjZfeGEnrUwdQ" +
       "014yXfFLgK\nXBYaeVFMWDUZu/i0CH3xSIiiUmFqTcer3y15b3s7bEc0eSN+" +
       "QuZ5JC+40oc4lTc89kFY2kWFYFVT\nRFa8HqxA8uL/gv9EoC8nGAhHbBXEc5" +
       "rjgwktjhbqcc6H66BMHP9lhVrhs37aKO5YAjgaziA5K8hK\nT4oXJ7gtC/CB" +
       "IHVUqWZkxoFALcM/Hm7ShhY6uBITyLyF5DySC1JwM5gkHzzc32RNNzLvOALo" +
       "XFAh\n9NW9Df8QmNKhkz3A8fGDz4OrnzwxfPC5U+7HoRH0x1gmapZaYxkFXK" +
       "MZNadLhUecG2Uw2qxA81X3\nmca/zpdA9x+kT/kppxEAAA=="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(onlineShopping, 20103,
                                           SJParticipantInfo.TYPE_PC);
            ss.participantName("PC");
            ss.addParticipant("vendor", "localhost", 20102,
                              SJParticipantInfo.TYPE_SERVER);
            ss.addParticipant("phone", "localhost", 20101,
                              SJParticipantInfo.TYPE_MOBILE);
            SJSocketGroup sg = null;
            try {
                sg = ss.accept("phone");
                {
                    SJRuntime.negotiateNormalInwhile("phone", sg);
                    while (SJRuntime.insync("phone", sg)) {
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("phone", sg);
                            if (_sjbranch_$0.equals("BUY")) {
                                {
                                    Song s =
                                      (Song) SJRuntime.receive("phone", sg);
                                    System.out.println("PC: Received song: " +
                                                       s.Name() +
                                                       " from phone.");
                                }
                            } else
                                      if (_sjbranch_$0.equals("NOOP")) {
                                          {  }
                                      } else {
                                          throw new SJIOException(
                                            "Unexpected inbranch label: " +
                                            _sjbranch_$0);
                                      }
                        }
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("PC Exception: " + ex);
                ex.printStackTrace();
            }
            finally {
                SJRuntime.close(sg);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        PC a = new PC();
        a.run();
    }
    
    public PC() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328861065000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0Za2wcxXnu/H7gd5yHnYQkBpI2nAuFFHBKiA+b2Fwc43MC" +
       "GFFnb298t8ne7nZ3\nzj4jhHiUhlIJFRGgIEoqBE0fVCpBtJVa0SKgKaUPlU" +
       "ohrRT+hLagQtWoKkQQaL9vZnf2cZdLQW1/\n1dKNZ/ab+b5vvtd8882Tb5M6" +
       "xyYJhzqOZhp7EmzRog5vzcweqjInkR6fVGyHZpO64jjTAJhVd3+o\n7Xqq8/" +
       "ZtcRKbIV2GuVXXFGc6b5vFXH46rzklm5xtmfpiTjeZi7EMx2XrTi38ct94Xw" +
       "1pnyHtmpFm\nCtPUpGkwWmIzpLVACxlqO1uzWZqdIZ0Gpdk0tTVF126CiaYB" +
       "hB0tZyisaFNnijqmPo8Tu5yiRW1O\n0/uYIq2qaTjMLqrMtB1GOlJ7lHllsM" +
       "g0fTClOWwoRernNKpnnc+TW0g8RermdCUHE3tT3i4GOcbB\nUfwO05s1YNOe" +
       "U1TqLandqxlZRlZHV8gdD1wNE2BpQ4GyvClJ1RoKfCBdgiVdMXKDaWZrRg6m" +
       "1plF\noMLIitMihUmNlqLuVXJ0lpFl0XmTAgSzmrhYcAkjS6LTOCbQ2YqIzg" +
       "La2lHf+sHdk++eDRoHnrNU\n1ZH/eli0KrJois5RmxoqFQtPFhP7x64v9scJ" +
       "gclLIpPFnK3n/GBn6o2frBZz+irM2cFtcVY9tal/\n5StbX2+qQTYaLdPR0B" +
       "RCO+danXQhQyULrLtXYkRgwgP+dOpn19/6bfqXOGkcI/WqqRcLxhhpokY2\n" +
       "6fYboJ/SDCq+7pibcygbI7U6/1Rv8jGIY07TKYqjDvqWwvK8X7IIIQ3wi8Fv" +
       "nIi/VmwYqZtMJpw9\njAwx6jBn0LHVQatoZ4t0EAyVaUaRW/gsLVmmDcPcoJ" +
       "U3DQpjpWDpdJCvLiGVtoVYDDbYH3U2HSxz\nm6lnqT2rHjz+i5tHrv7SXUJ1" +
       "aG4uf4wsFVQTQaqwOkliMY63G41SCG2rbSuL6Cyl215Z+dBh5Wug\nAhCFo9" +
       "1E+U5jC7XYwqILq8aSpO+JY9BTwFBm1Z5b31jx8O++9WKc1FSMJyn5cdS0C4" +
       "qOJuE5UJdL\nLgoBSxqI2nMl2n+9e/vTR14+tt63bEYGyhyufCU6zNqo3G1T" +
       "pVkISD76B9/b9rf76i59Jk5qwQtR\n0ApYDzj1qiiNkOMMeUEI91KTIi1zZR" +
       "tvZiCghcCGsW3FpkPYBuiiJ8Igj18n76j/1Ks/bnmR79gL\nde2BmJimTDhO" +
       "p6//aZtS+H7sq5P33f/2vhtqwFUtS+ickXqrmNE1tQRLloZ9DdnLotm8dWio" +
       "457z\nne9zFTdphUKRKRmdQmxWdN1coNlZxoNTZyAQ8vgDkmjNQBwDN5jVAZ" +
       "HYqxWbB/OsECkSy3r2P7Dh\nkVcxllhcML3oc5xTUuIfBmLYri8D4nglWnCP" +
       "v23w/r1iA60b0jeO775rLd95EN1Gd1CSCH1fuKiq\nL4zimQM2w0wIPr7J7P" +
       "7y0Wu2X3ftoAiHF1RFMQHunOV4/PX3jxlbnl1yxIijldQ7e6TLp0jcYYys\n" +
       "T3koXcvDoeilx9MC5B0JG8+8AZ+wdvM7tx36zf2tQHiGNGjOqGYoOmrbmRDe" +
       "W+G4iKC46dmdj578\nFXuN26bvLsh+f6k80O1SAp58yZH5zvrvHSjEScMM6e" +
       "BHvmKwXYpeRMudgUPbSbofU+SsEDx8AIvT\nZkiGg/6oqwbIRh21NiDtWpyN" +
       "/Ubhm3xO+z/F34f4Q8PBgTgWupJmwYKzxD77KgqbVhjNWqVYzMJl\nl/DFq3" +
       "i7RthfHA6SORQxIw2Wrc0rmDmRNtPA8ymdNy0LfIYv6GakTyrSLkKwL1DM61" +
       "zT49Eixr1i\nCxjtQETn0jwSaB5MnNmHj2w+ZLD+41xRDW6yhhmTH0bgFNVB" +
       "jvwYtsHfqmGVRjerukdlTIQnNa/p\nWeTsQsCxrqo1iuX3NqQPXpvb/3jc9e" +
       "gWi4eyc32f9vlyBnYaBTOrzWk8FoGXn2o/54Jn3rqnQ5wG\nfrwBTzgzAv/7" +
       "8mFy68ufe3cVRxNTEUfSDUcbZXA4r5pEhmlOqySQeqbYOcrEEQ7bqoIjgOIy" +
       "/cHD\nT0yeuMsTypUWxupqKhkzFkDy9MwcnFMFSco0LYFh7/hjM8+Nn0wJuW" +
       "bM7KLQqcsMamhDVW4yNnhb\n/szsVBPqcEUcEKu6NOdKakGKRw2mL4ozC0xP" +
       "gcWMdAdMepvi5Lcr1pDkm4RPSRde1/CH557v3f3b\nGhIfJc26qWRHFZ47kC" +
       "Y4tKmTh9SsZG25QuSEC43QdqBDA7Ilkn+OEGSoZKge4HaMNOaBStLMwuFZ\n" +
       "oysZZGUTcDJwMMNI7cSOHZPC0K6F6XOM1AzvvP4Mup6iKtXmy3SNp8/FVZZt" +
       "hz7cLiBoFYqGpvLs\nUeA4ePEdRx+4ffAfcNjfQBqpTgsgWBDlqhtC1xw41a" +
       "dtyFRHxAQIpS0FgXPaC5wR/XpiL9oergTi\nSpTjio9+YvPmD9ZeyvMA2M8w" +
       "yKCvygKl6cWHz9107E7I08ZAZRBBJ4oY01KkDQ8BBa9kPB9xY3sj\nJv0Tfq" +
       "xvFte6CT+9JqTuMNw1fZqQ0FIF7ogtopNACCNnweZ8bjj6DPDbxz6m7BlZUw" +
       "3sUq3frBka\nu1ySa2f/voUw0hP5wpEiqj0S4eXY4SdLgZFGZy8YAYN46YGb" +
       "fTA2pgRchB1+g9JYyBzkjJngjCYD\n/CCpw+1Iwn9dfSuhs4ZvJfTFlU8Tg3" +
       "w3jPcoduaxWQA/U01rUYLWs8DRpDgiO0HOvGNpAvpIrTM4\ndEnxLEGiupQF" +
       "ImoUlQhgHrLu8AcXXW9Wc5RCRsvBdY765CSBU4ysjMYYL+/YCUOHyyT0Rar3" +
       "doll\noNqOMewHduwPKyA6hZ0vYHNnCCDSkQoA8mkWOPWipN38xKPeE/nyER" +
       "gg1wXtaF5zNBZIma6EmIBo\nh4sa3rIhtJ0W5uqlLYNDnu7iBElmCDtfweZe" +
       "MDudKvM0CVciCb8HJC2zXsHGiG2b9jbFyEKYyu3C\nTyZwsLzSZ7nh/VKwHd" +
       "h5AJsHIYHkBCWxp8A4IsTcipwk0x3+IAk8JAlAXroiggSlLzF0BEaucLr5\n" +
       "tJFsDvS0Y57athaw2N9j51FsDoCE5EwJ/y4L3ATRIDztt4Qco5mvTGI+KZe+" +
       "iZ3HsXnCQ41ZoIRv\nYWRZCDU/AoYhfUH8HYGRF819InCXl3je97eAzdcr7Q" +
       "2bx/xN+Zxh8w0JOFCRJTQpnyUxkqp58r/B\nx5thwDclYBFMMcRg2izaKh2F" +
       "cCICQmD4H2HxqaAS67gCJOySoEHP2Wi6RjbhGt8kiApEFhi5WqyB\nO5KHIj" +
       "7NAiVJiSKt5mm2qKPrt8m+u7wBliM26RF/x87z2LwgoFeZivTw2A+D0BaFMV" +
       "qwWGjGI/4M\nbF76P+B/AtBPB9gQ1Fg76HPaxAMTUhwtkON8JxgHpeF4JyvE" +
       "Cq/rmY0qxhLB3dg5hs1rDN8wOBQd\nR1oWpiJdkkZB0YzEdmggluE/F2+dAy" +
       "m07xIT2DmOzevY/FEClgJL8sDD9RXmtGHnDQ6AzAUJegWG\n5RVryWnTyAEH" +
       "vH5d8jrR/zytHy+VVzh4DrDOrdoRbNeHSm6Q06883ZsFf2/Zd92J1i8qL9wo" +
       "LjNd\n4drkiFEsXHzgKN1wRataoTjexEzrfJ3OU10W9uKi7gJkz69aidjO7w" +
       "J+ZaomvWXDeRtb/gyXi9PU\ntzvdj1OUFW0jcP2B2cpHLnyvjgglyk/nfN81" +
       "NXnt53FesRJFrrKXq/CioXBpq9kOM8pV2CcKXCAi\nvNF2wa/fvU3y/wjsxK" +
       "ZL1KexOVG1IlsR6FVXXbvr9i9YIyWVWmh3HP8HbhwvrzlO2lpBY/L2cu+q\n" +
       "x//09PGpHmED4h1vXdlTWnCNeMvzyktAYU01Cnz2C59c8+QtU69lvArMO+A+" +
       "86aWxc3FGsNWXTZ4\nTwoWf+fBb8QV7EglwVYoFXJHgmufw19Zqwu2/CGQP/" +
       "4IYR2qGTgR/1HvAH/PqM0ojrynh15Qyx9I\nQ++enM9muavl8FtdZVeerjuC" +
       "F3jbq25GjQKvkLGlpRjBwmms7zTS+KyILSATnUKMyuOEWL3FacWa\nwHZgB9" +
       "jtLZW5f7nl8ZsilgA9mHhC0MyEfLMGYPkLAY7fF+xyWh/TJQIvGDG4i9ap8u" +
       "IaFpoobJeq\nqh9x1HBMbW6E5xZaZqexgHBCX0Ck8ckkVvSWRb3CfalW176y" +
       "e/3zVudLwoi81+QGrKoUdT1YQQ/0\n6y2bzmncchpEPd3ibH4GpF3p7GG8xu" +
       "+PuWI3iTWX8TcrXIOjIW4oiVLsX3uwYfQOIQAA");
}
