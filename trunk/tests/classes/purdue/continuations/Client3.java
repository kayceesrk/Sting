package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client3 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAM1Xb2gcRRSfu8s1/9o0/W+btmqbtE2RO6O22ib2T64Xm3Sb" +
       "Bi9tMaG2c7vTu23m\ndtfdueQOpIgiVIVixSpIQShKEfVDBdFvUkQKCoJ+kC" +
       "r4qX6woB9ERMEKvre7N7t7vVyLKBjIuzf7\nZn7v78y8ee9nknRs0uswx9FN" +
       "41RKVC3mpHDocbmxHBOTwB5Xr34z9IEh1l6Pk5hCWkuslGe2I8hK\n5RSdpe" +
       "my0Hk6Y3LOVAFIgxWbbGmK6oo8ZOL9xVzkpFrUuSbIFqW2PO0uSsvl6chy1L" +
       "SxThNSM38K\nTEFdnpJzrblLRwuvvhUnBFassExeLXBT+Cu8OXv7Pjqs/PjJ" +
       "3d6cngZzDrmox9Wb29eu+3rvD+0J\nNLnNMh0dvRbkLqW2yIvIhC8ZrFhWxY" +
       "JQb8JopVCWCqLl9B42Sqamn9RpnjNFd8TNxX0DH/50ttsN\nSQuHL4J0hyKN" +
       "c9Dz+24PF3xfPUye+eLJ39e7oDH1KXKaxFyjlgcoim7MMA3hF/bnjo2dOLMh" +
       "AcGw\n5lowQTB1c7OkDrOC3iinCwS1CwxcWOq5wKlRSOeErRsFdGJTE8wQ5E" +
       "7++tW3J345g9lB2+NWxWle\nZaPGHBQTm98ihOkGC/qagCimaXkIM2MXp66M" +
       "/aF4ScmbWhXXJ+7QmMeZyvTZW4zBStvWZNlB4GkB\nslgqlQ1dpUJum0vbnr" +
       "v22rPp3+IkMU3aGGclZgjYkeunI1Gm6sykTVWW9SYMKqSz5GEijCArgpL1\n" +
       "tpe7q+pC5Ltole0afArhU7fCx0e2Dg39tWEHemaBizsgLD1NFtD2z97YtP37" +
       "5+OkZZR0cN1g42U8\nWhTSpTGVUyySDKeO4xoCm+0kZHSclpg/7igxUTQ1+Q" +
       "WVJq/Cbgl0ThZtRuFI6fSYFEoEWQTOBda4\n8OvA3h7xD9MhyL3NxL7WBUO6" +
       "oYtdUt1icedFI8jyui8uKEL1ScBdyGxGskWQNmcG6kLAkVATdwRi\nJFul4C" +
       "FkNiLpFZEKkTOmwjPaDVNjGW4agfzL5q5EjnzXlcgXPz7twmb1uMgMIHkAyY" +
       "O3F1wLBIK0\nqKZVlaLz4s5uOyig0EjGOS2BLiCzA8nOeZUjeVgKIB/BRUUd" +
       "71rBMNYuqXHgUfOS8NCPS4sIp+JR\nETo066F8q2toy+u+SF/2SLg9InSuz+" +
       "pwZQHMhG0KUzX5PtiFsEUMh1Nh2oKsm0/kW7pK+F9YdKLU\ndj8yWSQjkG/O" +
       "6CzLUB7Iz0Kc5NXrWZO1bdPeTw0NzofCEfyElqxu9Fm6N1oDjHUjcwCJIkjS" +
       "VSiV\nXQaH6pRl4CJnFSHVLIt+kArGpYK4IGvqQDDWEqE7NPKjtMydltUKkJ" +
       "VDs8y2dS0w6ltkckgmIUJy\nppS/D+ulPkx/LdedkZrpcFdmsJ+SS28g8wSS" +
       "qRo0XvZSvht6mAi0e/YOw13n7onQqHaMBkpsFpwz\nfwYuIDncyDckRwOnAs" +
       "uQTEvBmw1NwpIKTPJGMjX0v7DjRlRwTAqqUIoRA3Nm2VbZCFxU3nYODf8V\n" +
       "Ey+Hk5h0EyBlj4QL+qSNpWtoKb/4JiBUELLQyM9iwi7L3MWh6HpuhcipRaaV" +
       "OYN67pK8v7wVliOa\n3BG/IuN2DVVP+phJ5Q6PfRyWdlIhWMkSkRkXghlITv" +
       "/PBXw+QX/Y08UQh0kTegML7mQ9dCm/Gz4/\nZMBxou7Ge1GNrYVb9cYS4EVk" +
       "XPKSIAt9KRaczAiZg+5b6ihR3UgdBAJnAP74uEkHer6glMaReRnJ\nOSSvSM" +
       "EqMEneF7i+wZwuZM67Ari9UCE0gmvrnlQTHFqv/SbXmA397PXPn84eeOGM11" +
       "4bQUMXaea8\nZwPGgOvQPg4Aan/TJ0AeriO1ePs3QLOXzXBDjPgUWarD0QNX" +
       "rwam8CpO0PAJS2ExnNGhB9t+6hQP\nUmtQdtIEDF8SvLt8ebL1uyufrjzxVY" +
       "LER6AbNqk2QlXYqqPQGEHz6hQhVhVr9x7XhIVzbUC74T8O\nYCuk/S4gPFxo" +
       "nvGQtaOkrQhaMnBFKCTBaT76HvOetvBQJfe8swZSlj2SHbfcdF4ksQHoUxOH" +
       "9u2z\nKkHkG3Gue11/A7DhR6pXEAAA"));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20103,
                                           SJParticipantInfo.TYPE_PC);
            ss.participantName("client3");
            ss.addParticipant("client2", "localhost", 20102,
                              SJParticipantInfo.TYPE_SERVER);
            ss.addParticipant("client1", "localhost", 20101,
                              SJParticipantInfo.TYPE_MOBILE);
            SJSocketGroup sg = null;
            try {
                sg = ss.accept("client1");
                {
                    SJRuntime.negotiateNormalInwhile("client1", sg);
                    while (SJRuntime.insync("client1", sg)) {
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", sg);
                            if (_sjbranch_$0.equals("ODD")) {
                                {  }
                            } else
                                      if (_sjbranch_$0.equals("EVEN")) {
                                          {  }
                                      } else {
                                          throw new SJIOException(
                                            "Unexpected inbranch label: " +
                                            _sjbranch_$0);
                                      }
                        }
                    }
                }
                System.out.println("Client3 is connected to all participants");
                String str = (String) SJRuntime.receive("client1", sg);
                System.out.println("Client3 received string: " + str);
            }
            catch (Exception ex) {
                System.out.println("client3 Exception: " + ex);
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
        Client3 a = new Client3();
        a.run();
    }
    
    public Client3() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328886482000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ZbWwcxXXuzt828UecEOJ8x0DShrsmfAhwICSOTWwujrFN" +
       "ACNw1rvju032dpfd\nOfscIRqIgJSqVCgJaSktUkWFaKlUgmiRWtEioGkLtG" +
       "qoQooEf1K1IKACtZSoBdr3ZnZnP+58oajt\nr1q68cy+mffevHnvzXtvHn+X" +
       "1LoOSbvUdXXL3J1mszZ1eWtN7qYqc9Ojg8OK41Kt11BcdwwAE+qu\nT/SdT7" +
       "TfuS1JEuOkw7Q2G7rijuUdq5jLj+V1t+SQFbZlzOYMi3kYy3BcvvqjmZfuGe" +
       "xKkdZx0qqb\no0xhutprmYyW2DhpKdDCJHXczZpGtXHSblKqjVJHVwx9L0y0" +
       "TCDs6jlTYUWHuiPUtYxpnNjhFm3q\ncJr+xyxpUS3TZU5RZZbjMtKW3a1MK5" +
       "ki041MVndZT5bUTenU0Nxbye0kmSW1U4aSg4kLs/4uMhxj\nph+/w/QmHdh0" +
       "phSV+ktq9uimxsjy+Aq54+5rYAIsrS9QlrckqRpTgQ+kQ7BkKGYuM8oc3czB" +
       "1Fqr\nCFQYWTwnUpjUYCvqHiVHJxhZFJ83LEAwq5GLBZcwsiA+jWOCM1scO7" +
       "PQae2oa/n43uEPV8CJA88a\nVQ3kvw4WLYstGqFT1KGmSsXC08X0oYEbi0uS" +
       "hMDkBbHJYs7mc390XfbNny4Xc7oqzNnBdXFC/eiS\nJUuPb/5DYwrZaLAtV0" +
       "dViOycn+qwB+kp2aDdCyVGBKZ94M9Gfn7jvu/St5OkYYDUqZZRLJgDpJGa\n" +
       "Wq/Xr4d+Vjep+LpjasqlbIDUGPxTncXHII4p3aAojlro2wrL837JJoTUwy8B" +
       "vwEi/pqwYaSp19Cp\nyS5Mu7sZ6WHUZW7GddSMXXS0Is2AtjLdLHI1n6Al23" +
       "JgmMvQklKwDbohE6wuIal5M4kE7HJJ3OIM\nUM9tlqFRZ0J99NSvbuu75ksH" +
       "xPmhznlMMljHqabDVPHsOQ2SSHDk81E9hfg2O44yi2ZTuuP40q8f\nU74Jhw" +
       "FCcfW9lO85MVODLSzaUNWr9AY2OQA9BVRmQu3c9+biB1957IUkSVX0LFn5sd" +
       "9yCoqByuGb\nUodHLg4BneqOa3Yl2n++d/uTJ158fU2g44x0l5le+Uo0nVVx" +
       "4TuWSjVwTQH6I3/f9t7B2sueSpIa\nsEeUtgJ6BOa9LE4jYkI9vjvCvaSypH" +
       "mqbONNDAQ0E9owti3YtAkFgbPojDHIPdnp/XVfePUnzS/w\nHftOrzXkHUcp" +
       "EybUHpz/mEMpfH/9a8MHD797z00pMFrbFmfOSJ1dnDR0tQRLzo5aHbKnodq8" +
       "c7Sn\n7b4L3B/yI27UC4UiUyYNCl5aMQxrhmoTjLup9pBL5J4IJNEyCR4NbG" +
       "HCAERir3ZiGtSzgs9IL+o8\n9MDah15Fr2JzwSxE6+OckhL/0J3Adk0ZEMdL" +
       "UYM7g22DH9gjNtCydvTmwV0HVvGdh9Gt8wYliTCw\nhYuq2kI/3j6gM8wCNx" +
       "SozK4vn7x2+w3XZ4RjXF8VxRDYtMbxBOsPD5ibnllwwkyiltS5u6XdZ0nS\n" +
       "ZYysyfooPc3DoeiNDo4KkH85rDvzBgLC+m1/u+Pobw63AOFxUq+7/bqpGHja" +
       "7pCw3goXRwzF3meu\n+9bpl9kbXDcDc0H2l5TKvd1OJWTJl56Ybq/7wcOFJK" +
       "kfJ2388ldMtlMxiqi543B9u73exyw5KwKP\nXsXi3umR7mBJ3FRDZOOGWhOS" +
       "dg3Oxn6DsE0+p/Wf4u8T/KHi4EBcEB29VsGGW8VZcTWFTSuManYp\nkbBx2a" +
       "V88TLerhT6l2RAGkXMSL3t6NMKxlAYpEzrTPhznDyfkS55iE4RvH2BYnTnqR" +
       "33FAluEZtA\nYbtj5y1VI42qwcTNfezExqMmW3KKH1K9F7Jh3BS4ELhLDZAh" +
       "v4wdsLVqWKXCTajehZkQrknN64aG\nnG0AHKuraqJYfn/96KPX5w49kvSsud" +
       "nmbuy8wJ4Dvtzu68yCpelTOvdDYOEftZ67/ql37msTN0Hg\na8AKzowg+H7O" +
       "FrLvxVs+XMbRJFTE0eu5onXSMZxfTSJbaE6vJJA6pjg5ysQdDtuqgiOE4nLj" +
       "yLHv\nDL9/wBfKVhv9dLUjGTBnQPL0zBycWwVJ1rJsgWHP4LfHnx08nRVynb" +
       "S0WXGmn46ZEapSfbqMGXSN\nF1dZth36EASDRRWKpq5yexA4Hr14/8kH7sx8" +
       "ADfRTaSBGrQAIQ+o77KbItE4XDljDsRSfWIC2Hlz\nQeAc8606Jg9vPxBY+b" +
       "jSiCtdjivZ/7mNGz9edRm/pGA/m0EGXVUWKI0vPHjeJa/fBUHEAGnCQHSo\n" +
       "iEaXJfPQQymYOfDL0nM8DRibDgWOqElkH0NBAEhI7TFIiQKaEG1RBVKZZtFJ" +
       "I4SRs2BzATcc/fXA\nbxf7jLJnZGU1sEe1bqNu6uxKSa6VfXoNYaQz9oUjRV" +
       "QTEuGV2OHfJhlpcPeAEjDPXSK4KQBjo0nA\nRdi5GZtbWEQd5Izx8IxG09Jo" +
       "r2GZAfzX1bcScYZ8K5EvnnwaGQRjMbzY2YMN9+iFMwNOBgBGalTL\nnpWgw+" +
       "zT3QOgQKGRlLMuET2EHT5icxLH5lYJWMNCjl5xxT2PYvSd/BD0kXJ7eOjJhd" +
       "+3EtUVLOQh\n46g8rn1snbEvci97Jbobwyc3rUMyGbpFt4IV4uotRR0zL3Am" +
       "c8I8XudN4pBHPzhBkunBzhex2QcH\nbVBlmvZChCzh94GAZBAk2OhzHMvZpp" +
       "gaOIbcTvwEqSM5p9Jnua/9PsJEG3buwuZuiCc4QUnsCUaW\nxoh5pRpJZn70" +
       "gyRwQBKAMGVxDAkKWWJoC4084czn0/q0HBzHjmnqOLoWMPV77HwFG5BFo5wp" +
       "4d9n\nocQAz90/5OaIsjTxlb0YYsilb2HnIDaHfNQYGEj4JkYWRVBzp7sFbj" +
       "RuDKGR7z8DIpDaSTz/CLaA\nzVcr7Q2b+4NNBZxhc1gCHq7IEqpUwJIYyaP5" +
       "xn+Dj7eigCMSMAuqGGFw1Co6Ku2HG0rYcWj4H2Hx\nifAh1vIDkLBLwwo95a" +
       "DqmlraU75hEBWILDTyTjEFYbOPIjnGQrUqiWJUzUPib6Dpz5N9b3k9LEds\n" +
       "0iL+gp2j2DwpoFdbirTwxNNhaLPCGC3YLDLjoWAGNk//H/A/ARhzAdaGT6wV" +
       "znPMwkwOggo9FFV8\nL+wHpeL4KR/4Cr/rq40qxhLBvdh5CZuXGRa3ORQNR2" +
       "oWmYEkUtIoQOqc3g4N+DL85+GthcTVCUxi\nCDvHsXkFm99JwNnAkrzwcH2F" +
       "OfOw8yoHwPWLBP2csy0czmJ8ihvi1cX1EOyurZp9TDqQWefPnH5U\nS6K2VM" +
       "SRHCcdOvhDCAQ0YMWYFfUpSDUVWAwXRyiF3aa4+e2K3SPjehKtiHnw2vrXnn" +
       "1u4a7fpkiy\nH2JzS9H6FV4nhDANQmk3bxlayd50FWehZaYB2jZM3gHZAsk/" +
       "Rwg5kzJJjRC3A6QhD1R64d7KkpSh\n8HD0EuBkxWOLQd59O/uGRGJ5iiTWQ5" +
       "Sc2rEVGA0kXanHtzNYKi8n8BrAaq9ERrBdE6lvAcNL53oq\n4M8c99zwfsvd" +
       "yvM3i+SsI1oI7DOLhYsfPknXXtWiVihHNzLLvsCg09SQVbSkKHIA2Quqpv7b" +
       "eW4T\nlIFSo5vWnr+u+U+QLM1RTG73Po5QVnTMUDoHs5V/u8q8PCaUOD/t01" +
       "3XpvL6L5K8PCQqSmUPRtFF\nPdE6UpMTZZQfYZeoJoGIUKU64LfY0xz+H4Ht" +
       "2HSIYjA2b1ctf1YE+qVMz6rnB1bdV1KpjQ6O4//Q\nuyXLC3zDjl7QmczG7l" +
       "/2yB+fPDXSKXRAPJ+tLnvBCq8RT2h+PQcorKxGgc9+/vMrH7995I1Jv+Tx\n" +
       "HhjLtKVruLlEMqrVZYO/SsHi73z4bfUEu7WSYCvU5bghQRrr8sfN6oItf3/j" +
       "Ly1CWEdT3e8nf7yw\nmz8e1EyCj/IUNfpwWf4uGXlu5Hw2yV2dA7/lZ9jVax" +
       "UVANPfRHspQbAimZg/x86vEH4E9m9QcPt5\nnABLuBgSKdAT4Ba7baUyUy/X" +
       "Mp7lYn3Nh4navG6l5bMwAMtL7zj+QLDLaX1G9Q89DSRWwNWpyqQ7\nesWJin" +
       "Gp6lHj8GOOqcG7K7k2lulkIiScyBd07N4DHVY1F8XNwHsRVlcd37XmObv9l0" +
       "Jr/FfbeiwL\nFQ0jXJ8O9etsh07p/PDrRbXa5rxmQOSVngoZr6AHY366abFm" +
       "A38RwjU4upBry3mlxL8A4mG9zXYg\nAAA=");
}
