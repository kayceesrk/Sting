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
          1327974732000L;
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
           "8FGI+KtFwEh5s65R\ng81vcNYycgqjDnMaHTvZaGVsNUMbQVqZZmS4mPfSYc" +
           "u0oZhq9DsN4wjjhmIxWNzUbEXTQSqXm7pK\n7d7kfXt/fsWy82+4Xhwbipo7" +
           "N0bmiMEagoPhkfMx6tMjnRnDUPp0SmIxPk4dCqjYwMW2rYyg4gxf\n/dK023" +
           "cqd8FxwLY42uWUrzo2hGvGTqfktSvNvla2AqaA0PQmx1/1+uQ7fnv/jjgpyG" +
           "lbErKyxbTT\nio7i4SlTrTtcNgWkqj5btnON/beNbY/senHPXF/KGakfpXyj" +
           "e6LyzMo+B9tMUhWMk8/+1n8v//vN\nRWc+GieFoJG48QpIEij49OwxQkrU5B" +
           "kkXEtBglT0j1p4OYMNGgosGGElgmohK3AW47MmyG3ZvmuL\nP/fyExU7+Io9" +
           "s1cVsI9dlAklqvHPv9umFOr33NZx85a3N1xSAGprWfzMyTC0PDqsbjgrFaXl" +
           "re1N\n1ZtOdn7AT7ZMS6czDAUMzLOi6+YQVXsZt081AVvITRBsQGUfmDJQgl" +
           "4dGIklWrFBkMocxqJh0vjN\nt8y782U0Jxbfj4k4NW+CWDE7hnDOKCKWp6Hg" +
           "jvdXCwZgnVhA5byuS1esuX4WX3CQ3YluYVgy9FXg\ntLwq0IJuB0SFmWB/fE" +
           "lZc+PuC9ouurBRWMT5eVm0g1arnI/ff0ursejJCbuMOApHsbNWan6CxB3G\n" +
           "yNyEx9IVOCwKrGtFlyB5XuGkD16AP7B2xb+u3v7rLZUwcA8p0ZwWzVB0PG2n" +
           "XShtDo+RxeLyJ1fd\nve+X7BUukr6W4PSnDo+2d6uVgAKfsWuwpvjhrek4Ke" +
           "kh1dzrKwZbregZFNge8NtOs1uZIEeF6GEf\nLBxOk7QCU7M1NDBstn4WBna7" +
           "EFsjXipUkrep+q/4ex8/KDhYEJ6httlMW+BO7BnnUVi0wqhqDcdi\nFnb7PO" +
           "88ncOZQv7iDIbGLWakxLK1QQWDJ4xOBjUmLDo2rmNkijxEOwP2Pk0xrHPFjh" +
           "uIGNeIc0Bg\n67POW4pGA4oGEy57566F2w02dS8/pBI3VsOAybcc4ER12EPu" +
           "hW0yNy9XKXC9SddTxoRFSg5ouooz\nmw88ZueVRNH9ppKu+y5Mbb4n7mpzhc" +
           "Wt1/G+PvvzcupXGWlT1fo1bodAw/dXHTf/0bc2VQsH4Nsa\n0IIPZuDXH7OE" +
           "XPXiZe9O52xiSeSxxDVFJ0rDMCfPjjQvoSlt1IagLTg+T69Ap7P0W3fe2/HO" +
           "9d42\nNFtomY/LewiGmmvABXn6tAEO8SXIbDpjaEkucYLHfQuu3X3LNY3/BF" +
           "t/CSmlOk1DWAECMv2SUKAL\nRr3bhnhlmWgAmlSRFjy7Pb0B+8UUO0WZiFy8" +
           "xeB3G19UPsnqpEmqDdLR68Lu7QE2Prt5editzLAh\nEMgc/PLvbcI0LdFn3Y" +
           "pv9zyzYl9CCFifqY4I4c5aFsRm3lY14FY1jN6qeMsJCxe+N+tM7uVgGith\n" +
           "7lPydFDKdtxx/Ol7vgLBRyspxxC2PYNamyDj0MQpeOfg3ta1XKUY1bb7lqxc" +
           "3Fva/RiSkKKdcJny\nx4QojSpwCaoQSANSGDkKzs6fDWf/BZjvFPYRRYuRmf" +
           "nI7qjFCzVDY+fI4arYAUo/xB/BImeHTFTJ\n6hxE+hGkGCl11oF0M9fSIrnc" +
           "JyPQJOE0RPoQwCBBOZcteoItygxTpc26afj0X7EDt6OMjM+qcXem\njEH4ls" +
           "UXEe5j1iOwD3HCbp/ASGHStEYkaQs7MP8FchsoyUP2T/FORL6E4MoxB0fwRU" +
           "mYywIOSnFE\nfIJn6DmndsBd+fKL7qHwOEGyOpsF7Hw2K3fWHrfxWTVyLV+W" +
           "7C4Ois2gBrffgPdfCsqPvZdkNLwz\ngokek+bOdVwfFnnUhg3kME2I3IBgI0" +
           "iZTpVB2gyRvaRvgg2SwZuYxjLbNu3liqGCPUqtxiq465Jj\nclXLdX3VYxir" +
           "RuRrCL4OcRAfUA62jZFpWYO5z5bkMHXhCjnAN+QAEF5NzmKCmyw5VAdK7ubU" +
           "8WbL\n1BQcx8pBatua6k/qd4jciuA22CHZUtK/xwIXGjx375ArQsJSzns2Y2" +
           "gku76ByF0I7vZYY0Aj6YsY\nmRRizW39EnBAXBkCJc9s+4PATVTy+Y+/BAS3" +
           "51obgm/6i/JnhmCrJGzNOSUUKX9KoiSP5t5PYh5v\nhAnf8vcMke8guOcTG3" +
           "wrIg8gePDTX+AI6FroBLrMjJ2kLeD5haEKFD+WM9gWlNIiLmGSdkZQY/tt\n" +
           "1E1DbXC1qwNkAWQiUHLFtADuMx6LeDcLPD2ULLqSA1TN6Gjbxknc7V4C3ZGb" +
           "VPl/ILIDwfOCep6p\nSBMW+1GQWqEwRtMWC7W402+B4MWI8KkQ9LEI84InVg" +
           "Xn2W3iFRtCNi0Qsz0YNPRScLy7OBhDD/XE\nJinKksFGRP6A4I8MXzdwKiqO" +
           "lCwyBLd7OUZa0YyGNgBgrPHL5VvkwF3HV4l2RP6C4FUEr0nC0TAl\n6dGxf4" +
           "424xB5kxMgvsABvYcB1cFrAsb9uCD+4PfUwEXohPwXoT5bMZIDuW5C+S62S3" +
           "L2iveQWg1s\nPQQ5KsxCHxHPDOH6r0BncIqBxwrLFWegTbGavCuTRcIPJ116" +
           "Ucnvn3l24prfFJB4C1x3TEVtUfgj\nW4h/4XbiDJi6OmwtOpdPoXIIH9RX4w" +
           "MVYDZBzp8zhOub0kf1wGxbSekAjNIMPjlBCnSFR+wLYCYz\n7p8MW71s9bJ2" +
           "/yKHSA90vMA7mKKdiFyGoBfBGv9KhIgI/xH4zr0KEf9KkfMqgsC/axzIJQSB" +
           "H531\nhAm+bhxIGI4gEAkjcg2CaxH4geiZwUtYdlwrRMOLeOrCFa5+TFQ1R0" +
           "n3aamMwqgfRssB9oMPyT49\nT3VXQdHh8XKoxvNrsTJpM/BRWKwCQWWIQOrz" +
           "hfh4yQ+E+H5x9Ahk/xgjjDn0/5VATkXkOgQbDmwd\n5CJExF0AwY25LgkIZD" +
           "BPNiEigvkQIRjlI5DRuQgmNiPYEiLE4ohEsWG0wPzBLyKPIXj8YAffFh7j\n" +
           "KT+mReQZBD9F8KwfqSLixys7c8WfCF7IFXoiOETirs8u4UAiTgR7/CATEREj" +
           "IvhTruARwV4/ZEQk\nV/w3dmCIyJs5COPChLcQvC3jvlN4u3dJbH4VXKlWLl" +
           "0axS854xdAYiUIStcfnD8n9VjEfeH+LQoN\notDg8PGcR/wCo9AgIkShgRsa" +
           "DMsQYPXhGgKcjIj3wlD8puQgwoNp+V8l+y/75XOA0PtX/tZfMqtl\nH+IXB4" +
           "xMyK6SDxcekyx5gPIEgp+ECXhg3FjEfjzm/iD1+x+8OYcmYcwj41GA/zb3oF" +
           "7zIpIrODwb\nERkJBV+/IhIFPIdxPHDELzAKeCLCZzfg4YQ65r5F4u/FWg1G" +
           "U/jmz4uCZBDUfgQEQesPAycf+fLI\nl0cLjHx5RIh8+Ufw5YHfuCw1M306ze" +
           "HK2yJXHnlsEnnsQ9yhHfELjDx2RPjsemwA8Z4c3jlxMN45\n/z9Khf4tkP8k" +
           "MFQzxv98xR9GwDUs7NVzEg4zrx4578h5RwuMnHdEiJz3R/+tgHTe5x+M80bk" +
           "SL5a\nR7428rXRAiNfGxEiX/shL8qlOXztisjXRi6VRC41WmDkUiNC5FIP4q" +
           "fuWG4VWdhCeRZjjBQ7PEG1\nmz+UIBT5Q0VGvzjH6xip9V88eyl0PZJIKqqZ" +
           "DTKRNRAx1cK0sRIv86TRGy56p/I65blLRXaF2nB2\n1WVGJr1g624679zKZI" +
           "4sv8VuakasCOclPTlvMsU2nuzNT6xZ0LVo3pyTKl6Lk8IxsvLWuJWdlGVs\n" +
           "I5C+D1orHzpd77FZO5I9n5rBKRcUDGg/i/OEmyJH56jc2+FOTeHMnOV2eKL8" +
           "7KfwCVTBFpXBpw4+\n9UT88W8k1iCo5a3H5RYUK9OnS0EZI9FsXiKUC77rpr" +
           "YZnS61w9bSGpMvK26afs+rj+ztHC8OX2Qh\nnz0qEXiwj8hEzldQwTN9zMw3" +
           "Am/93IkzH7qy85U+N3ViwVZGCgdNTcUFFGwPqEF2QlxGyv1U0p4e\nTM2Xeh" +
           "rmMyl7Pm6G8+Ssl9bMfdaqeYEnUJZZyEswWWFG14NpVwN4sWXTfo0vt4TDSo" +
           "uv4QlQyFwT\nYTwxrF/GWRc8Lvo8xY8X+2DpactbUeDXJiJz7HDsf/eTCyta" +
           "XwAA");
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
      1327974732000L;
    final public static String
      jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK1XW2wUVRg+e+n2ttpuKRXpjUsNNMKuYjRiTaTZQGhZQt0W" +
       "IiVkmZ09u51ydmac\nc2Y7oEEUI8iDiQGvUXgxMRoeFKI+aNQE8O4LJuALvG" +
       "CURDHyYCQGjf85Z/Y2s9SY2GROz+X/z/lv\n3///e+IqaqIWilNMqWbos3G2" +
       "x8RUjEZ2FquMxifHJxSL4lySKJROwUFG3fW3tu3d2FMbgygwjbp0\nY5RoCp" +
       "2asQy7MDM1o1HHQktMg+wpEIO5N/rueGD5jblvDo73hlDHNOrQ9EmmME1NGj" +
       "rDDptG0SIu\nZrFFR3M5nJtGMR3j3CS2NIVoe4HQ0OFhqhV0hdkWpmlMDVLi" +
       "hF3UNrEl3ixvplBUNXTKLFtlhkUZ\n6kzNKiUlYTONJFIaZSMpFMlrmOToo2" +
       "gfCqZQU54oBSDsSZW1SIgbExv4PpC3aSCmlVdUXGYJ79b0\nHEODXo6KxkOb" +
       "gABYm4uYzRiVp8K6AhuoS4pEFL2QmGSWpheAtMmw4RWGFt/0UiBqMRV1t1LA" +
       "GYYW\neekm5BFQtQqzcBaGFnrJxE3gs8Uen9V4a0sk+tfhiT+WgMdB5hxWCZ" +
       "c/AkwDHqY0zmML6yqWjNft\n+NGx7XZfECEgXughljSjd3ywNXXlk0FJ09uA" +
       "ZouIxYx6476+/nOjP7SGuBgtpkE1Hgp1mguvTrgn\nI44J0d1TuZEfxsuHn6" +
       "Y/277/bfxzELWMoYhqELuoj6FWrOeS7rwZ5ilNx3J3Sz5PMRtDYSK2IoZY\n" +
       "gznyGsHcHE0wNxU2I+aOiRBqhi8AXwHJvygfGGpLEg3r7O44nWVoDcOU0QS1" +
       "1IRpWzkbJyBamabb\nIswz2DENC5aFRJXJ4S/cOhcIgHJ9XqARiMqNBslhK6" +
       "O+efmrx9dvevaQdBsPNVc2BnzisXjtY9zl\n4g0UCIjLF/ColFYbtSxlD0eL" +
       "8+S5/lc+V14HH4AtqLYXC1UDc2E+AtOaeZNJsgrFMZgpECkZtXv/\nlcWvfv" +
       "fW2SAKNUwoqcrmBsMqKoTHRBlBXe5z3hMIpSFvQDd6+9fDm0+d//riympoMz" +
       "TkQ5yfkyNm\nmdf4lqHiHGSk6vUv/bnxtyNNa98LojDAkFtbgfABVA9436hD" +
       "zkg5C3FdQinUnvcp3sbAQHM1CvMx\nyodOGSDgi26PgCKBXT8QuevCR+1nhc" +
       "blXNdRkxQnMZPIiVX9P2VhDPsXX5448sLVgztCgFXTlD5n\nKGLaWaKpDrDc" +
       "Vg82Ll6Oh80vJ0c6n1tN3xcubtWKRZspWYIhOSuEGHM4l2EiO8VqMqFIQGCJ" +
       "aBYS\nGUAgQ+AiqasZKEF4NkgV8UXdR18cfu0CTyamMEwPB52QFDliYyjAx5" +
       "W+Q77u5xHcXVUb4L9bKhAd\nntw5vuvQMqF57XWr3IV7OSr5QbmBV5dyTBSz" +
       "j/1++ljbkqqEq2qvGxDjUte2fL68fHNVbMkRFPMF\nUD6EwNxm8bSt69yw5S" +
       "Opi2bEK9UTDrmj+m+W7UWlOvjItegzypmdMid31Tt1vW4X7z3+PR5eF1Ub\n" +
       "pJZWZpirCS5hUjFx0JXXQqvnzQ6bRXGsoic0+dDwilXtPwF6bpIYYu5mGkMn" +
       "oHP5+ZstImMo/zlj\nDHqM4pUnVup9ODSjfRHkmHSTha/m1zON1JoHUGvVCy" +
       "qirlcI0AEmaoGvC74Ot2KI//wwxocuCWw+\n3DNvKP9rnCcZClm2Tv0FfMLS" +
       "ilAfS24Bf37gjR9PXU53Sz/LLme5r9Go5ZGdjnil3eSRtnS+FwT1\nmTuXnt" +
       "iXvpQNutI9yFC4ZGg5oUDKj5G6xbqK8fi3Aj7VNZ7ayHh+kAUFyCCLUdGDNj" +
       "Zetb552yRR\nGaWxToaGrgU/7BkSyT6cVWg5GOv7S3/7WNcVCjnbKlrdDt/g" +
       "PFqVkd5ZTQKyhWwgu9fZdYkpo+79\neOux69+yS8LZ1WLF7+lz/Gltm1JTR+" +
       "8/X4pF3jleDKLmadQpOm5FZ9sUYvO6MQ1K06S7mUK31J3X\n97+y2Rup4KvP" +
       "G201z3rLZC3SwqwOY6Iy7nACyOQTtUEYMLhL0xUicy7EA8F6gc0ImnFT2nkz" +
       "4Aa8\nx6fTji+9uZ5YUPVEkhg65imXH611fC7h61Epmbjzf4C2AXqo/NkGgS" +
       "Ft68wf4n4t1jsqNnmHKB5I\nAzqLEBgCnD6IBmpsU7cDFm12+0veWyzy/UKU" +
       "v2PUZed2rTxtxr6UICr/1miGhj9vE1Lr4Jp5xLRw\nXhPiNUt3m+LffqiAjT" +
       "pdJkKwuhbiPiF5DoiGhvPw1dMiXmwn8A/msh0LLA8AAA==");
}
