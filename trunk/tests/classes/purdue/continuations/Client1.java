package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;
import org.apache.commons.javaflow.Continuation;

public class Client1
  implements Runnable,
             Serializable
{
    
    public void
      run(
      ) {
        
    }
    
    static class myRunnable
      implements Runnable,
                 Serializable
    {
        final private SJProtocol
          invitation =
          new SJProtocol(
          ("H4sIAAAAAAAAAO1ba2wcVxW+u7Zr51Hbzau0eUDbJE0CrFNCSoPTl9d2Y3fj" +
           "uLWTQkKbzO7c7E48\nO7OZuevYUqnaIlBaQQSIgoQiiqqWKqL9ESRU/qEKUK" +
           "A8RPmBCqL8KT+oBD8QQiARJM43j3tnNrNb\nB1PwuhMpn8+dc+859577ON/d" +
           "2X3pz6zLddhWl7uuYVuncmK+xt0cir40NT7FxTSJx0uXfr3/O5bY\n9FaWZQ" +
           "qsu8qrRe64gm0onNJmtYG6MMyBvG2avCTI0uCcw3a0tOqpfMvM/5fxLHeVKo" +
           "apC7ajEDYf\n8BoNyOYDsebwdEuDJ6BdPEVdgS/fyZe6p158qPyV57OMUYv1" +
           "NducL5u2CFr4de7d9srhwh+//36/\nzsaEOoc8q8dLl2/ftPlX9/5hRQe63F" +
           "OzXQOjFux9hbCRH5HJQDM4V6vN1SjU2xGtHHQ5FS1362Gr\nauvGSUMrmrxg" +
           "uOJy37bbvvunc/1eSDpNeiJYfyTSqIORf+idzannNwyxx3/6yN+3eEYzpdPs" +
           "MZbx\nOrVOWSkY1gzXYX71zqmHx0+cvbmDglE704kJoqq3tpjU/BAvG1fMKU" +
           "K5vUWrSKOPm1+79MLkX86i\nEXqXrc2Ry20t15GlJznc26LNQZK1MsWlWq1b" +
           "RkkTciG+uPczb3z1yYG/ZVnHMdbDTV7llqA1vuWY\nH3pTs8oDU0IrzUw7Wo" +
           "mP+BUGC2xV1bcJM4KtV4vAX7DeOi2wa4TmlDnN5JqYOcewyoNyvPi7xht3\n" +
           "q/3zIC9xY5ZfOXQ0Xxsxo8ztbGHuUF2coW2XYK91+Au2XfPbzIw/d/TV8X8U" +
           "/AVbtPV5eO5oHFat\n7oTRzGH4uSujmR3dtX//v27eB+816sZ91PeNLRpoK3" +
           "709e23v/nZLOscYytNw+ITdZxNBdar85Kp\nIbx5U3O9Lmyg3XqSBjqhVXlQ" +
           "XlnlomLr8gmcdl2i7aZ8TlccrtGZtMoXctAIdi3NpeqNZ/4W6u9G\n8R+uPs" +
           "FuaqUOvF6z37AMcZd01ycWuEEEuy5a9MzByAelqbsgfBiQE6zHnaENIOg0Cd" +
           "UrlRqwWyo+\nCmEnYJeIbQVZ42i0xgrL1nnetC2l/4VYeLYQbF3DkyAyK4TD" +
           "G+1C+BjgDsC+Ja54QykE6yzZtXmp\nekYsLEvTuo2U5CTvlYbOQxgGjDR1Dh" +
           "iSih0ikmA110+HmMMwuU6QHKwvVQwmpVNE18GdIpIKGk0F\nvQ6trWt4Isdy" +
           "vzR3j4jko1mDUi2ZmXRsYZdsc5g2P+1MyzU1YTuCbW6mCnp6vQie8HhF6W03" +
           "hEnA\nA7TYTK7N8rxmKv05ipOkDH5vRhzHdg5olk7HUvkIHqEnNyQ9lsObDg" +
           "1m+iEcATwkWJfnUDq7SANq\ncJYnAsLnhHSzNv5AOvikdJAV7MYGI4i1tNAf" +
           "KQVRWutVG9HLNCuHZrnjGLrq1G8gPAI4ThGSNaX+\nZWov/WH6w7leFVszK7" +
           "2WefBA2fRtCDqAh6ZBUqT+buJeMdPekT9EecjbE5FSeHorJw5Xh9w/1RAA\n" +
           "J5LGBiiqQameAU5KxbOJXcKSUl3yS3JqzHejH2/HFRUVMwinADPvmvNnIZwG" +
           "OP/7Ac7TXovNwJRd\nd0p8lAiAf15Fiv+VObgYXaVd3gqTujuiO/akg71p6b" +
           "lgd03SWqA1ESkFy7TDqcvFmZ0WkUuJNDFV\nqnC9bnLasL1SDpp3U3NYk1v+" +
           "rxA+D/iCr73P1uSIM9+LaldpQvBqTcRqnFc1AF9e4gqzmWJndKR9\nFIdpmz" +
           "hXjRiPEaE8344ekDLgqGh48b42FMNwl/yyNPA0hG8A6DRYHWix4OSMsDN0H5" +
           "A+qpph5Q4S\n0CGHP4HdLpeuDmopTUB4HvAC4FtScT11SSZEtE+o0wvhgqeg" +
           "9AyHRLA3Ndx1J02itAdsU+cOXYve\neu3RkfufOutze0sR5RhJ9i8yiIFpEC" +
           "3fE7l67Gp99ShS4i1Vku4erW6bQ4mtskfZGoOOVaIVOvXC\nnEcFHR8raNSY" +
           "8k/kEn1AcysHtZq8e9UY9fU6dRcO9F3dv331BxtOvN7BsqN0wbA1fVQr0S4d" +
           "I8ZJ\n9wG3QmGaq919j9eF1Wd6CPvpf5aMrZf99wzShUkrcjPS2zHWUyEveU" +
           "p/BdZhasX45dD/uGGQevaB\nCzfSbI0cGZlQVykIN5GhB8K57boEYRtgO+BW" +
           "dSmB4BNwgOL8fRAUqU+8DAAU21/INQCwJ4n9AxQX\nXQgRBkS4KIQxwDhAUc" +
           "F90WtQI7P0l0pINtbGHwRbbINuuFq1aJTrHgMMead0cJmO78bZDHf/YSq6\n" +
           "HmONPZEp5TV57GQg/Azw85iCbW1FsnHNjpBsVbzSA7vcxENT1/9XBdsD4SBg" +
           "YmHjYJ+I3hUTST9F\nbqhu4OwSbEtTXTDvvUUURw1u6jG2Pwjhd4A3AZKVs3" +
           "MQfFYeU0TpOkDSbJ8VHAN8KqbIZCGkJC8d\nYGsWC+EJwJOLdX4x7uNzipxC" +
           "OAt4CvC0opwQFIE6l0QkAV9M4pCAJUIE37uKhVBgwHnFeiH4pBXw\nzSQ2C3" +
           "hOcVgISYS0OVOFcCFB0RtXeH15WbLKj3j1XmGZ2/robnRoeDhlQ4lsCMKPAT" +
           "8BLIIdsK0Q\nfgl4fZGm2o5oQFAM4PcpNWi3zLnsB5hSg1SRUoOAGsxJCjDR" +
           "rhTAsxK+APS/CbMIerC59ath9fJe\nfqoQe5/qvcWXxtaIq/gGgWDrGx+FH1" +
           "VkHpMmu1F8HPBEXIHzKfMo4NNN4wPt3DsHZ2kqmk7ZMxDU\n29lFvbaFkEQO" +
           "74SgmFDkdSoE/wUn4EGp2B1XpIRnqfKBZT/AlPCkROHqiYKneEkE73a8t1Vj" +
           "luBl\nfEAcsgdJHkaXAXk43QbJMc2BaQ5MB5jmwHZRLJMcGPnGxrBdL5o8IQ" +
           "WOpCkwzXQszXRLPBEs+wGm\nmS7NdFef6QiyvQlZbXgxWa31D1RiP8fyvggW" +
           "e9LktzZZHIrZh/Eong0TFW2WDdOklya9dIBp0msX\nRfsnvei7UJn08otJeh" +
           "CW81UuzVFpjkoHmOaodlG0f47K/DAhR8nD8j2co9JUlKaidIBpKmoXRfun\n" +
           "othXR1Hu/Tf4RtRIDk0AAA=="));
        
        public void
          run(
          ) {
            try {
                final SJService c =
                  SJService.
                    create(
                    invitation,
                    "localhost",
                    1000,
                    SJParticipantInfo.
                      TYPE_MOBILE);
                c.
                  participantName(
                  "client1");
                c.
                  addParticipant(
                  "client2",
                  "localhost",
                  20102,
                  SJParticipantInfo.
                    TYPE_SERVER);
                c.
                  addParticipant(
                  "client3",
                  "localhost",
                  20103,
                  SJParticipantInfo.
                    TYPE_PC);
                SJSocketGroup ps =
                  null;
                try {
                    ps =
                      c.
                        request();
                    ps.
                      continuationEnabled =
                      true;
                    System.
                      out.
                      println(
                      "Client1 is connected to all participants");
                    SJRuntime.
                      pass(
                      ("Hello, Client2 from Client1. I will send you an Integer and " +
                       "a Double:"),
                      "client2",
                      ps);
                    SJRuntime.
                      pass(
                      new Integer(
                        2011),
                      "client2",
                      ps);
                    String str =
                      (String)
                        SJRuntime.
                          receive(
                          "client2",
                          ps);
                    System.
                      out.
                      println(
                      "Client1 received: " +
                      str);
                    SJRuntime.
                      pass(
                      new Double(
                        3.14),
                      "client2",
                      ps);
                    SJRuntime.
                      pass(
                      new Double(
                        1.11),
                      "client2",
                      ps);
                    int i =
                      0;
                    {
                        SJRuntime.
                          negotiateOutsync(
                          false,
                          ps);
                        while (SJRuntime.
                                 outsync(
                                 i <
                                   10,
                                 ps)) {
                            System.
                              out.
                              println(
                              "sending: " +
                              i);
                            SJRuntime.
                              pass(
                              new Integer(
                                i),
                              "client2",
                              ps);
                            if (i %
                                  2 !=
                                  0) {
                                {
                                    SJRuntime.
                                      outlabel(
                                      "ODD",
                                      ps);
                                    SJRuntime.
                                      pass(
                                      "Odd Number",
                                      "client2",
                                      ps);
                                }
                            } else {
                                {
                                    SJRuntime.
                                      outlabel(
                                      "EVEN",
                                      ps);
                                    SJRuntime.
                                      pass(
                                      "Even Number",
                                      "client2",
                                      ps);
                                }
                            }
                            i =
                              i +
                                1;
                        }
                    }
                    SJRuntime.
                      pass(
                      "This is continuation msg.",
                      "client3",
                      ps);
                }
                catch (Exception ex) {
                    System.
                      out.
                      println(
                      "client1 Exception: " +
                      ex);
                    ex.
                      printStackTrace();
                }
                finally {
                    SJRuntime.
                      close(
                      ps);
                }
            }
            catch (Exception ex) {
                System.
                  out.
                  println(
                  "client1 Exception: " +
                  ex);
                ex.
                  printStackTrace();
            }
        }
        
        public myRunnable() {
            super();
        }
        
        final public static String
          jlc$CompilerVersion$jl =
          "2.3.0";
        final public static long
          jlc$SourceLastModified$jl =
          1329257224000L;
        final public static String
          jlc$ClassType$jl =
          ("H4sIAAAAAAAAAO0cCZAU1fXP7H3IXoDIJccq4LEbVIy6KMLCyuLssu4uqGt0" +
           "6Z3+O9vQ0910/9nD\nGMsjEcUKiRG8olJJNJZHqgRjDjUeUSTGHBVThSRVmK" +
           "RIvKKpWKlEKkGT9/7v/t09OzuiqAHsrZo3\n7//3//vXu3737HvobVLk2KTB" +
           "oY6jmcbaBjZiUYdDs28tTTKnoWtFh2I7VG3WFcfpBkJvcs372upt\nNdcsj5" +
           "NYD6k1zMW6pjjdA7aZSQ10D2jOsE1mWKY+ktJN5nIcxeOs2fuHfrFhxZQCUt" +
           "VDqjSjiylM\nSzabBqPDrIdUpmm6j9rOYlWlag+pMShVu6itKbp2OTQ0DRjY" +
           "0VKGwjI2dTqpY+qD2LDWyVjU5mN6\nlQlSmTQNh9mZJDNth5HqxFplUGnMME" +
           "1vTGgOa0qQ4n6N6qqznlxJ4glS1K8rKWg4MeGtopFzbGzB\nemhersE07X4l" +
           "Sb0uhes0Q2Xk2OwecsX150MD6FqSpmzAlEMVGgpUkFoxJV0xUo1dzNaMFDQt" +
           "MjMw\nCiOTx2QKjUotJblOSdFeRiZlt+sQJGhVxrcFuzAyIbsZ5wRnNjnrzA" +
           "KntbK48r2NHe/OgBOHOas0\nqeP8i6HT9KxOnbSf2tRIUtFxX6Zhc+vFmalx" +
           "QqDxhKzGos3i4364KvH6U8eKNlNytFnJZbE3uf/0\nqdNeWvznsgKcRqllOh" +
           "qKQmjl/FQ7XErTsAXSPVFyRGKDR3y68/mLr3qA/jVOSltJcdLUM2mjlZRR\n" +
           "Q2128RLAE5pBRe3K/n6HslZSqPOqYpOXYTv6NZ3idhQBbilsgOPDFiGkFD6F" +
           "8FGI+KtFwEh5s65R\ng81vcNYy0sSow5xGx042WhlbzdBGkFamGRku5r102D" +
           "JtKKYa6bCStnR6SqPfexiHGjcUi8Eqp2Zr\nnA7iudzUVWr3Ju/b+/Mrlp1/" +
           "w/Xi/FDm3EkyMkeM2hAcFc+ej1GfHunMGIbSp1MSi/Fx6lBSxU4u\ntm1lBD" +
           "Vo+OqXpt2+U7kLzgX2x9Eup3z5sSFcPHY6Ja+BafbVsxUwBaSnNzn+qtcn3/" +
           "Hb+3fESUFO\nI5OQlS2mnVZ0lBNPq2rd4bIpIF712UKea+y/bWx7ZNeLe+b6" +
           "4s5I/SgtHN0TtWhW9jnYZpKqYKV8\n9rf+e/nfby4689E4KQTVxI1XQKRA06" +
           "dnjxHSpibPMuFaChKkon/UwssZbNBQYMEIKxFUC1mBsxif\nNUFu1PZdW/y5" +
           "l5+o2MFX7Nm/qoCh7KJMaFONf/7dNqVQv+e2jpu3vL3hkgLQX8viZ06GoeXR" +
           "Yb3D\nWakoLW9tb6redLLzA36yZVo6nWEoYGCnFV03h6jay7ihqgkYRW6LYA" +
           "Mq+8CmgTb06sBILNGKDYJU\n5rAaDZPGb75l3p0vo12x+H5MxKl5E8SK2TGE" +
           "c0YRsTwNBXe8v1qwBOvEAirndV26Ys31s/iCg+xO\ndAvDkqGvAqflVYEW9D" +
           "8gKswEQ+RLypobd1/QdtGFjcI0zs/Loh20WuV8/P5bWo1FT07YZcRROIqd\n" +
           "tVLzEyTuMEbmJjyWrsBhUWBdK7oEyXMPJ33wAvyBtSv+dfX2X2+phIF7SInm" +
           "tGiGouNpO+1CaXO4\njiwWlz+56u59v2SvcJH0tQSnP3V4tL1brQQU+Ixdgz" +
           "XFD29Nx0lJD6nm7l8x2GpFz6DA9oADd5rd\nygQ5KkQPO2PheZqkFZiaraGB" +
           "YbP1szCw24XYGvFSoZK8TdV/xd/7+EHBwYJwEbXNZtoCv2LPOI/C\nohVGVW" +
           "s4FrOw2+d55+kczhTyF2cwNG4xIyWWrQ0qGEVhmDKoMWHRsXEdI1PkIdoZsP" +
           "dpivGdK3bc\nQMS4RpwDAlufdd5SNBpQNJjw3Tt3LdxusKl7+SGVuEEbRk6+" +
           "5QBvqsMecndsk7l5uUqB6026LjMm\nLFJyQNNVnNl84DE7rySK7jeVdN13YW" +
           "rzPXFXmyssbr2O9/XZn5dTv8pIm6rWr3E7BBq+v+q4+Y++\ntalaOADf1oAW" +
           "fDADv/6YJeSqFy97dzpnE0sijyWuKTpRGoY5eXakeQlNaaM2BG3B8Xl6BTqd" +
           "pd+6\n896Od673tqHZQst8XN5DMNRcAy7I06cNcAg0QWbTGUNLcokTPO5bcO" +
           "3uW65p/CfY+ktIKdVpGsIK\nEJDpl4QiXjDq3TbEK8tEA9CkirTg2e3pDdgv" +
           "ptgpykTk4i0Gv9v4ovJJVidNUm2Qjl4Xdm8PsPHZ\nzcvDbmWGDYFA5uCXf2" +
           "8TpmmJPutWfLvnmRX7EkLA+kx1RAh31rIgNvO2qgG3qmH0VsVbTli48L1Z\n" +
           "Z3IvB9NYCXOfkqeDUrbjjuNP3/MVCD5aSTnGsu0Z1NoEGYcmTsHLB/e2ruUq" +
           "xfC23bdk5eIC0+7H\nkIQU7YRblT8mRGlUgdtQhUAakMLIUXB2/mw4+y/AfK" +
           "ewjyhajMzMR3ZHLV6oGRo7Rw5XxQ5Q+iH+\nCBY5O2SiSlbnINKPIMVIqbMO" +
           "pJu5lhbJ5T4ZgSYJpyHShwAGCcq5bNETbFFmmCpt1k3Dp/+KHbgd\nZWR8Vo" +
           "27M2UMwrcsvohwH7MegX2IE3b7BEYKk6Y1Iklb2IH5L5DbQEkesn+KdyLyJQ" +
           "RXjjk4gi9K\nwlwWcFCKI+ITPEPPObUD7sqXX3QPhccJktXZLGDns1m5s/a4" +
           "jc+qkWv5smR3cVBsBjW4Bge8/1JQ\nfuy9JKPhnRFM9Jg0d67j+rDIozZsII" +
           "dpQuQGBBtBynSqDNJmiOwlfRNskAzexDSW2bZpL1cMFexR\najVWwaWXHJOr" +
           "Wq7rqx7DWDUiX0PwdYiD+IBysG2MTMsazH3IJIepC1fIAb4hB4DwanIWE9xk" +
           "yaE6\nUHI3p443W6am4DhWDlLb1lR/Ur9D5FYEt8EOyZaS/j0WuNDguXuHXB" +
           "ESlnLesxlDI9n1DUTuQnC3\nxxoDGklfxMikEGtu65eAA+LKECh5ZtsfBG6i" +
           "ks9//CUguD3X2hB801+UPzMEWyVha84poUj5UxIl\neTT3fhLzeCNM+Ja/Z4" +
           "h8B8E9n9jgWxF5AMGDn/4CR0DXQifQZWbsJG0Bzy8MVaD4sZzBtqCUFnEJ\n" +
           "k7Qzghrbb6NuGmqDq10dIAsgE4GSK6YFcJ/xWMS7WeAxomTRlRygakZH2zZO" +
           "4m73EuiO3KTK/wOR\nHQieF9TzTEWasNiPgtQKhTGatlioxZ1+CwQvRoRPha" +
           "CPRZgXPLEqOM9uE6/YELJpgZjtwaChl4Lj\n3cXBGHqoJzZJUZYMNiLyBwR/" +
           "ZPjegVNRcaRkkSG43csx0opmNLQBAGONXy7fIgfuOr5KtCPyFwSv\nInhNEo" +
           "6GKUmPjv1ztBmHyJucAPEFDug9DKgOXhMw7scF8Qe/pwYuQifkvwj12YqRHM" +
           "h1E8p3sV2S\ns1e8h9RqYOshyFFhFvqIeGYI138FOoNTDDxWWK44A22K1eRd" +
           "mSwSfjjp0otKfv/MsxPX/KaAxFvg\numMqaovCH9lC/Au3E2fA1NVha9G5fA" +
           "qVQ/jEvhofqACzCXL+nCFc35Q+qgdm20pKB2CUZvDJCVKg\nKzxiXwAzmXH/" +
           "ZNjqZauXtfsXOUR6oOMF3sEU7UTkMgS9CNb4VyJERPiPwHfuVYj4V4qcVxEE" +
           "/l3j\nQC4hCPzorCdM8HXjQMJwBIFIGJFrEFyLwA9EzwxewrLjWiEaXsRTF6" +
           "5w9WOiqjlKuk9LZRRG/TBa\nDrAffEj26XmquwqKDo+XQzWeX4uVSZuBj8Ji" +
           "FQgqQwRSny/Ex0t+IMT3i6NHIPvHGGHMof+vBHIq\nItch2HBg6yAXISLuAg" +
           "huzHVJQCCDebIJERHMhwjBKB+BjM5FMLEZwZYQIRZHJIoNowXmD34ReQzB\n" +
           "4wc7+LbwGE/5MS0izyD4KYJn/UgVET9e2Zkr/kTwQq7QE8EhEnd9dgkHEnEi" +
           "2OMHmYiIGBHBn3IF\njwj2+iEjIrniv7EDQ0TezEEYFya8heBtGfedwtu9S2" +
           "Lzq+BKtXLp0ih+yRm/ABIrQVC6/uD8OanH\nIu4L929RaBCFBoeP5zziFxiF" +
           "BhEhCg3c0GBYhgCrD9cQ4GREvBeG4jclBxEeTMv/Ktl/2S+fA4Te\nv/K3/p" +
           "JZLfsQvzhgZEJ2lXy48JhkyQOUJxD8JEzAA+PGIvbjMfcHqd//4M05NAljHh" +
           "mPAvy3uQf1\nmheRXMHh2YjISCj4+hWRKOA5jOOBI36BUcATET67AQ8n1DH3" +
           "LRJ/L9ZqMJrCN39eFCSDoPYjIAha\nfxg4+ciXR748WmDkyyNC5Ms/gi8P/M" +
           "ZlqZnp02kOV94WufLIY5PIYx/iDu2IX2DksSPCZ9djA4j3\n5PDOiYPxzvn/" +
           "USr0b4H8J4GhmjH+5yv+MAKuYWGvnpNwmHn1yHlHzjtaYOS8I0LkvD/6bwWk" +
           "8z7/\nYJw3Ikfy1TrytZGvjRYY+dqIEPnaD3lRLs3ha1dEvjZyqSRyqdECI5" +
           "caESKXehA/dcdyq8jCFsqz\nGGOk2OGZqt38oQShyB8qMvrFOV7HSK3/4tlL" +
           "oeuRRFJRzWyQGa2BiKkWpo2VgZlnj95w0TuV1ynP\nXSqyK9SGs6suMzLpBV" +
           "t303nnViZzZPktdlMzYkU4L+nJeZMptvFkb35izYKuRfPmnFTxWpwUjpGV\n" +
           "t8at7KQsYxuB9H3QWvnQ6XqPzdqR7PnUDE65oGBA+1mcJ9wUOTpHJeEOd2oK" +
           "Z+Yst8MT5Wc/hU+g\nCraoDD518Kkn4o9/I7EGQS1vPS63oFiZPl0KyhiJZv" +
           "MSoVzwXTe1zeh0qR22ltaYfFlx0/R7Xn1k\nb+d4cfgiHfnsURnBg31ESnK+" +
           "ggqe6WNmvhF46+dOnPnQlZ2v9LmpEwu2MlI4aGoqLqBge0ANshPi\nMlLup5" +
           "L29GBqvtTTMJ9J2fNxU50nZ720Zu6zVs0LPIGyTEdegskKM7oeTLsawIstm/" +
           "ZrfLklHFZa\nfA1PgELmmgjjiWH9Ms664HHR5yl+vNgHS09b3ooCvzYRmWOH" +
           "Y/8Dw0UO+WNfAAA=");
    }
    
    
    public static void
      main(
      String[] args)
          throws Exception {
        myRunnable myClient1 =
          new myRunnable(
          );
        SJSocketGroup.
          executeExportable(
          myClient1);
    }
    
    public Client1() {
        super();
    }
    
    final public static String
      jlc$CompilerVersion$jl =
      "2.3.0";
    final public static long
      jlc$SourceLastModified$jl =
      1329257224000L;
    final public static String
      jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK0XW2wUVfTuo9vXarulVKQvHjXQCLuK0Yg1kWYDoWUJdVuI" +
       "lJBldvbudsrdmXHu\nne2ABlGMIB8mBnxG4cfEaPhQiPqhURPAtz+YgD/wg1" +
       "ESxciHkRg0nnvv7GtmqTGxydzex3m/98RV\n1EQtFKeYUs3QZ+Nsj4mpWI3s" +
       "LFYZjU+OTygWxbkkUSidgoeMuutvbdu7sac2BlFgGnXpxijRFDo1\nYxl2YW" +
       "ZqRqOOhZaYBtlTIAZzKfpoPLD8xtw3B8d7Q6hjGnVo+iRTmKYmDZ1hh02jaB" +
       "EXs9iio7kc\nzk2jmI5xbhJbmkK0vQBo6MCYagVdYbaFaRpTg5Q4YBe1TWwJ" +
       "nuXLFIqqhk6ZZavMsChDnalZpaQk\nbKaRREqjbCSFInkNkxx9FO1DwRRqyh" +
       "OlAIA9qbIWCUExsYHfA3ibBmJaeUXFZZTwbk3PMTToxaho\nPLQJAAC1uYjZ" +
       "jFFhFdYVuEBdUiSi6IXEJLM0vQCgTYYNXBhafFOiANRiKupupYAzDC3ywk3I" +
       "J4Bq\nFWbhKAwt9IIJSuCzxR6f1XhrSyT61+GJP5aAx0HmHFYJlz8CSAMepD" +
       "TOYwvrKpaI1+340bHtdl8Q\nIQBe6AGWMKN3fLA1deWTQQnT2wBmi4jFjHrj" +
       "vr7+c6M/tIa4GC2mQTUeCnWaC69OuC8jjgnR3VOh\nyB/j5cdP059t3/82/j" +
       "mIWsZQRDWIXdTHUCvWc0l33wz7lKZjebsln6eYjaEwEVcRQ5zBHHmNYG6O\n" +
       "JtibCpsRe8dECDXDF4CvgORflC8MtSWJhnV2d5zOMjTCMGU0QS01YdpWzsYJ" +
       "iFam6bYI8wx2TMOC\nYyGBHaVoErwmUcV2OKtb5wIB0LLPm3EEwnOjQXLYyq" +
       "hvXv7q8fWbnj0k/cdjzhWSAZ7gGq/lyn0v\neKBAQBBfwMNTmm/UspQ9PG2c" +
       "J8/1v/K58jo4A4xCtb1Y6ByYC/MVkNbMW1WS1Zwcg50CIZNRu/df\nWfzqd2" +
       "+dDaJQw8qSqlxuMKyiQnhwlFOpy2XnfYGYGvJGdiPevx7efOr81xdXVmOcoS" +
       "Ff6vkxeeos\n8xrfMlScg9JUJf/Snxt/O9K09r0gCkM+cmsrEEeQ3gNeHnUp" +
       "NFIuR1yXUAq1532KtzEw0FyNwnyN\n8qVTBgj4otsjoKhk1w9E7rrwUftZoX" +
       "G56HXUVMdJzGQKxar+n7IwhvuLL08ceeHqwR0hSFrTlD5n\nKGLaWaKpDqDc" +
       "Vp91XLwcD5tfTo50Preavi9c3KoVizZTsgRDlVYIMeZwLsNEmYrVlERRicAS" +
       "0SxU\nNMiFDAFCUlczUILwbFAz4ou6j744/NoFXlVMYZgenn1CUuSIi6EAX1" +
       "f6Hvm5n0dwd1VtqAO7pQLR\n4cmd47sOLROa15Jb5R5c4qjkT8oNvM2UY6KY" +
       "fez308fallQlXFVLbkCsS13b8v3yMuWq2BIjKPYL\noI8IgbnN4mlb17lhy0" +
       "9SF82IV9ooPHJH9d+s7IuWdfCRa9FnlDM7ZXHuqnfqet0u3nv8ezy8Lqo2\n" +
       "KC2tzDBXE1zCpGLioCuvhVbPWx02iy5ZzZ7Q5EPDK1a1/wTZc5PCEHMv0xhG" +
       "Ap3Lz3m2iIqh/OeK\nMegxileeWKn34dCM9kWQ56RbLHzNvx5ppNY8kLVWva" +
       "Ai6nqFAB1gohb4uuDrcFuH+M8fY3zpkonN\nl3vmDeV/jfMkQyHL1qm/k09Y" +
       "WhEaZcnt5M8PvPHjqcvpbulnOe4s900ctThy5BFc2k0eaUvn4yCg\nz9y59M" +
       "S+9KVs0JXuQYbCJUPLCQVS/hypO6yrGI9/K+BTXeOpjYznT7KgSDKoYlQMo4" +
       "2NV+1v3nlJ\ndEZprJOhoWvBD3uGRLEPZxVaDsb6QdM/R9aNh0LOtopWt8M3" +
       "OI9W5UzvrBYBOUs2kN3r7LrClFH3\nfrz12PVv2SXh7Gqz4nT6HH9Z26bU9N" +
       "H7z5dikXeOF4OoeRp1itFb0dk2hdi8b0yD0jTpXqbQLXXv\n9YOwnPpGKvnV" +
       "5422GrbeNlmbaWFWl2OiM+5wAsjkG7VBGDCgpekKkTUX4oFgvcBmBMy4Ke28" +
       "GfIG\nvMe3046vvLmeWFD1RJIYOuYllz+tdXwu4edRKZmg+T+ktgF6qJxtg8" +
       "CQtnXmD3G/FusdFZt8QhQM\n0pCdRQgMkZy+FA3U2KbuBiza7M6XfLZY5Pup" +
       "KH/QqMvO7Vp52ox9KZOo/KOjGSb/vE1IrYNr9hHT\nwnlNiNcs3W2Kf/uhAz" +
       "aadJkIwepZiPuExDkgBhqOw09Pi3ixncA/mGoBRTUPAAA=");
}
