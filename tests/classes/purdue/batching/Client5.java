package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client5 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1afWwcRxWfPdu1nQTHtfNBm7gtJUmTKj2TQqKmdpvUZxvb" +
       "XByDnVKlIune3eS8\n8d7uZnfOsVGpEAUpBSlVC23FhxSpKqoQRBBQKar4UI" +
       "VQJJCQ4A9UkPirRaIS/IEQAokg8X77MbN7\n2TvchLROu5Hy/GbezHszb968" +
       "39vd+85fWYfnsm0e9zzDtk7mxbLDvTyaATc7NcvFHLHHyxd/N/x9\nS2x9Pc" +
       "e0Iuus8VqJu55gm4on9UV9sC4Mc7BgmyYvC9I0tOSynS21+qJAMwv+ab7mjv" +
       "K8YVYE21mM\npg/6kwbl9MHEdFj6YIMlULt0kpYCW4GRpzpnX/xk9Ssv5Bij" +
       "GRsd21yumrYIZwRjHtj+8pHin392\nazBmS8qYw77W4+VL+7YO/PaBN7rbsO" +
       "Qux/YM7Fqw9xejSYFHZkLJ0JLjLDnk6h3wVh6yvPKWt+2I\nVbMrxglDL5m8" +
       "aHji0vrte176y9le3yXtJvUI1hvzNMZg57v/tzrVf9MI++yvjv3zFl+pVj7F" +
       "HmOa\nv6gNSkvRsBZ4BerX7Zr91NQjZ25vI2c4p9txQDT0jhaHWhjhVeOyM4" +
       "Urd7SYFZt0r/ncxW/O/O0M\nJmF1OWeJTG5vGUdWJc3g3hZzDhGvV8kvtVrd" +
       "Msq6kIH44t7HX3v2c4P/yLG2h1kXN3mNW4Ji/JaH\nA9ebulUdnBV6eWHO1c" +
       "t8LBgwVGRra4FOqBFsowqCIGD9OC2yG4TuVjmdZF9CnWtY1SG5X/zt8/fd\n" +
       "6v58gpe5scgv3zqm98fUKHW7Wqg7XBen6dql6Gvt/qJtO8Gchannj7469a9i" +
       "ELAlu7IMy23RtpzY\nQu5svZCSq1vl+bSVtIq9kdRZuaOsz/BGuUNhQidlLm" +
       "NABUlGp8mC9ceu1ITuzR/SHXkSDqO13qhu\nRijv6PzDqz/f9Mhv2lhunK0x" +
       "bb0yrpeF7U6ybjHvcm/eNitLzoGD/hLWne4i2kv/c6Rso1y/r5Dc\np5e4GV" +
       "vtJOuaJysFu8KLrM3US8lQCZIPpRJ227duFqx97MGx6civTt2NwjSPwfnLwz" +
       "Q3fufw8H9u\n3w9nOmRxgpa0pcUEvfsXX9ux749fyLH2SdqpYfHpOpJ+kfVU" +
       "eNnUEbcFU/f8JWyiNHiCImhar/Gw\nvabGxbxdkT0w2nGR8piyOUce0ynZrw" +
       "2YPCSCvY8uiVqNrz5P690irvBaC/aBVuLQ6g3DhmWI+6W5\n9WKFmUewG+NN" +
       "Xx2U7JOq7gdzD8h+wbq8BcosgtJ0JF6jxCBDUvARMD7ZKxI5Ro44Gh/RbVHg" +
       "FEzb\nUvJfi5XDsGAbGnpCz3QLlzfofQ3MCEiBArFsO8tStFPE8Fj3AvTEyi" +
       "IsniY+9JpqhqbaRXx3++NH\n0KgquPGRsv5kR6huU8Xw9FrJqNZ1wZU5aeCS" +
       "YAONl5IixKFAdo9Q0/N9kuiRxzsdadE0MDMgH08I\n2LZWrkDujLlCNS+3wC" +
       "41sdDU9DsqYB8WMcRv3HdYUEZb39DQ8xZ2zx6KR/eiQaUW6Z9xbWGXbXOU\n" +
       "chTUjtSpnuQuoXhTWRgtPSU0xw1uVjBAmhkCUwKh9XabXF/kBd1U8rN0zLJW" +
       "DJYx5rq2O6FbFUqb\n1QfRZdMKbkrrlhs+IR3bC2YexBCswzcojV2gkG0wVq" +
       "DKky8JaaY/2SENLEgDOcFublAC70sNvbFW\n6Jx+f9hYpUrndHiRu64Ru0e/" +
       "B+OAnCIPyZFSfp7mS3sIiOj01yau6xp/ZgEPAHLqm2AWQU5HqlGd\nSvkBKr" +
       "oTqn1IGqECBPp7Y60IXZQRl6sk/G+1BRA3bW8gyvJ5tTKQJSk4l7okhJRaUt" +
       "CSR/PYtVjH\nm0nBp6VgmUIxscBZu+6W+TgluSAbxZr/lyVeiB9ih38AUnZP" +
       "PKBPuAhdq5IPg2+GXEUui7XCU2xz\n6/LscnMi9rAmVcyW53mlbuLq90g+nN" +
       "5J06FN3oi/g3kG5NlA+lFblzdc+1FculYXgtcckRjxDTUC\n5OuZ4G0RmM0E" +
       "u+Intp7Oc84GjFPhZcQqr2/H86AMnAjvKVdEbBQ25aAtFXwRzHdBvifYulCK" +
       "iyMj\nCxHfJ23UdMPKHyJCuQx/Qr0dHj0aqisxDeYlkB+CvCwFm2lJEvAwP2" +
       "VMD5hXfAHVUzBIdf7WhncZ\nMyZV1hM2wI8ee1//5aNjH3viTPDsZql6PVGr" +
       "Bw+q8IFp0NPB3b7BO5i2hwrltsOjo+pJBMxu/wEj\nXFPHRTAfAtkDcreq6c" +
       "EE9SuIKpnXg1E1cWotDaKK5ZVU0SCqwj+aFBxUVTOYoLoFGU0re0HGVdEL\n" +
       "ZgJkEmRKlbBgiiCHQK6ipGPbwMyBHLlKVe9wdQjmGMjxle2DPQQmKMBAVHEw" +
       "lBTICoqdBRNUUAlB\nvLQCkSVRAFE1ECsh0HJgrjEgHwDzKMhnQK5JNXAOzO" +
       "Mgn79mNppXHGD8ZPmlqzV+IWnjSVVIgHkK\n5GmQL6vyAIwCiefSQB/kq2l4" +
       "D7JKwO69K1gJzIOcV8gOJgBmkAtpiA3yA4XTYNJAtzkag3klRdCT\nFPwY5K" +
       "cKOZckQkpkud4Q8i4w0fuf4JPNVaDnQOtXbeots3xTkng/5b9ulsr6xFt41S" +
       "3Yxsau6IFH\nG5AqO9G8FeS2pAAopuGEtK1J/zT1weoUND2ZZ8TKPg5SpRhr" +
       "yWdG5RBcbW03yF0rNN20pLoPjKof\nlOCgiH0iSH0d5OqWZ+r+K46BZqIwqD" +
       "aLsIcnB0pruKwaahnNvyBZ7ZHVHiBZ7cHeFZjtC34iwi+A\n/hPwpCV4Fa8F" +
       "IiCXOH7fuwDHiWibVwccXQFOnUrAy9uMO6fiSKDtzSAigwiWQcR7BiJiL0lH" +
       "7XrJ\n5CkIMZwhRAYEGRBkQJABQYrgOgcCItq5lKQv8+6VJP3Wv9xK/ADU/x" +
       "VNoqfJj9C0N0D+hK4kWKQK\nrjOwyDAhw4QMEzJMYKsFE+LffCQm3Js9CGQp" +
       "nGUpPEvhWQpnqz6Fa8dSUvj+LIVnmZplmTrL1Fmm\nZqslUyd+YIV2z38BO/" +
       "EuON0+AAA="));
    
    public void run(int singleSession) throws Exception {
        final SJService c = SJService.create(invitation, "localhost", 1000);
        c.participantName("client1");
        c.addParticipant("client2", "localhost", 20102);
        SJSocketGroup ps = null;
        try {
            ps = c.request();
            System.out.println("Client1 is connected to all participants");
            SJRuntime.pass(
              ("Hello, Client2 from Client1. I will send you an Integer and " +
               "a Double:"),
              "client2",
              ps);
            SJRuntime.pass(new Integer(2011), "client2", ps);
            String str = (String) SJRuntime.receive("client2", ps);
            System.out.println("Client1 received: " + str);
            SJRuntime.pass(new Double(3.14), "client2", ps);
            SJRuntime.pass(new Double(1.11), "client2", ps);
            int i = 0;
            {
                SJRuntime.negotiateOutsync(false, ps);
                while (SJRuntime.outsync(i < 10, ps)) {
                    System.out.println("sending: " + i);
                    SJRuntime.pass(new Integer(i), "client2", ps);
                    if (i % 2 != 0) {
                        {
                            SJRuntime.outlabel("ODD", ps);
                            SJRuntime.pass("Odd Number", "client2", ps);
                        }
                    } else {
                        {
                            SJRuntime.outlabel("EVEN", ps);
                            SJRuntime.pass("Even Number", "client2", ps);
                        }
                    }
                    i = i + 1;
                }
            }
        }
        finally {
            SJRuntime.close(ps);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Client5 a = new Client5();
        a.run(1);
    }
    
    public Client5() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1327975403000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ca3BU5fXb3bwTyAsQCe9EBAsbocKIQZGERIJLEklAjKPh" +
       "ZvfL5sLde6/3fpss\n1lofo6idMuMoan2UqWPL+JoKjtqZWltHLVattrSDtD" +
       "P6R8dq63Mclaloe8733efuzRqktKCbmT05\n9zvfd873OK97d+956D1SbBok" +
       "alLTlDV1S5Rt06nJoTa4hcaZGe1d2yMZJk20KZJp9gFhIL75S3nj\nntpr1o" +
       "RJqJ/UqdoqRZbMvmFDSyeH+4ZlM2OQ2bqmbEsqGrM45vA4q/Hw6Evb1zZESH" +
       "U/qZbVXiYx\nOd6mqYxmWD+pStHUIDXMVYkETfSTWpXSRC81ZEmRL4eOmgqC" +
       "TTmpSixtUHM9NTVlBDvWmWmdGlym\n3RgjVXFNNZmRjjPNMBmpiW2RRqTmNJ" +
       "OV5phsspYYKRmSqZIwLyNXknCMFA8pUhI6TonZq2jmHJs7\nsB26V8gwTWNI" +
       "ilN7SNFWWU0wMit7hLPipvOhAwwtTVE2rDmiilQJGkidmJIiqcnmXmbIahK6" +
       "Fmtp\nkMLItDGZQqcyXYpvlZJ0gJGp2f16BAl6lfNtwSGMTM7uxjnBmU3LOj" +
       "PPaXWXVH1xU89ns+HEYc4J\nGldw/iUwaGbWoPV0iBpUjVMx8FA6emvnRenp" +
       "YUKg8+SszqLPqlOe2BB7+zezRJ+GgD7dXBcH4oeX\nTZ+xf9Wb5RGcRpmumT" +
       "Kqgm/l/FR7LEpLRgftnuJwRGLUJv52/e8uuuoB+s8wKeskJXFNSafUTlJO\n" +
       "1USbhZcCHpNVKlq7h4ZMyjpJkcKbSjR+DdsxJCsUt6MYcF1iwxzP6ISQUviE" +
       "4HM+EX8VCBiZ1Cqx\n+HAv8DTbFJmqbHHU3MLIckZNZjabRrxZTxuJNG0exH" +
       "6gDPygEoY8QtXmoLEZFDlxNBSC1U7PtjwF\n1HSNpiSoMRDf/cYLV7Sff+MN" +
       "4hxR96zJMnKSkBm1ZUYF+6UkFOJ861FDxQ6uMgxpG1pO5ur9M368\nT7oHzg" +
       "P2xZQvp3zZodEihDBoSV7H0uaaZSdgEmjNQHzSVW9Pu/PP9z8XJpFA5xJzGj" +
       "s0IyUpqB+2\nNdVZ4rIpoFZN2codJPv9m9Y9euDF1+a7as5IU4715Y5E65mb" +
       "ve+GFqcJ8E4u+9v/tebDW4qXPxYm\nRWCS4JSYBKoEFj4zW4bPilpsj4Rric" +
       "RI5VDOwisYbNCoZ8EIqxDUCN2As5iUNUHuzA5dW3L6q09W\nPsdXbPu9ao+D" +
       "7KVMWFGte/59BqXQ/todPbfsfG/7xRGwW10XZ85IiZ4eVOR4Boac5Dc8nF4C" +
       "1ebd\nvS01OxaZj/MjLpdTqTSTBhUKjlpSFG2UJgYY91S1Hq/InRHsRNUgOD" +
       "VQzwEFGIm16qERUM8AtxGd\nOunW2xbc/So6Fp1vzBQ0QD5TkuENTSGE83OI" +
       "eD0DNXiSu2xwBVvFAqoW9F6ydvMNc/nKvewWWhcZ\nh6FrC2fktYUODECgM0" +
       "wDT+SqzOYfHrxg3aYLm4VvXJyXRReYc4Lzccfv7FRXPjX5gBpGLSkxtzgm\n" +
       "HyNhkzEyP2aztDQPLwXWu7ZXkOz4sPCrF+AKlq/49Oq9r+ysAsH9pFQ2O2RV" +
       "UvC0zS5hvQGxI4vF\n5U9t+MmhP7DXuW665oLTn57JdXQbJY8ln3lgpLbkkV" +
       "2pMCntJzU8/ksq2ygpadTcfojgZpvVGCMT\nfHR/NBahp8VxB9OzTdUjNttQ" +
       "izy7XYS9ES8Ttsn7VP9b/H2JH1QcvBAxoq5NS+kQWIzZ51FYtMRo\nQs+EQj" +
       "oOO5MPnsnhHKF/YQaicYsZKdUhUEiYRmGeMiIzni/xzvWMNDiHaKRVJqcoJn" +
       "iW2nFPEeIW\nsRIUtinrvB3ViKJqMBG89x1YsVdl09/gh1RqZW2YOrkuBMKp" +
       "AnvI47EBtpaPq6NwA3ErZoaEa4KY\npCRwZkuAR2NeTRTDby7t3X1h8tb7wp" +
       "Y1V+rcjc1z7dmdl9m0QU1pCXlI5n4ILPxw9SmLH3t3R42I\nBK6vASv4agZu" +
       "+8mt5KoXL/1sJmcTiiOPNssVLXQcw6l5dqStlSblnA1BXzAvzyjPoLOU2/f9" +
       "rOej\nG+xtWK2jZz4l7yGoiSCBS/OMWQc4ZJqgs6m0Kse5xgkeu5dee/C2a5" +
       "o/AV9/MSmjCk1BUgEKMvNi\nX8oLTr3PgESlXXQAS6pMCZ59tt2A/2KSkaRM" +
       "pCz2YvB/F19UPs1aT+MU0qfcdeHwbg8bl92CPOy6\n02wUFDKAX/69jWmaLs" +
       "ZsXXtv/9NrD8WEgg1qiW1Cua1l6Z6JnJZ/IoMG+J7hoJnkU6zWwFHgq+tk\n" +
       "czXVQQfgGJRtImaD+UkwmJF6j1mvkczhdZLe4kyZ+LMEi15c+renn5my+U8R" +
       "Eu4gFYomJToknjuR\nckhaqDkMqWlGX3kun0LVaBnAGnRowGyyM3/OELZPGq" +
       "SKZ7adpGwYpLRpCUgeIoo0iFNZBjOZff80\nRoraN7Z32fsI2a2tc1HUuWiu" +
       "zoU7Tlux4ou5y3m6ABLWwBQa8gyQyp+7c96y166DdK4TVgaxqSuN\n7i9GJm" +
       "KskPA2jqctVggowxuFLjckVIhbwS43CyekeB/cn7oyIe+lEtxXVgokihRGJo" +
       "ARuLPh7JMw\n3wb2NW2UkTn5yJbUkhWyKrNzHHHVbJxuBBI57yVnh0w0h9U5" +
       "iFyGAJLhMnMruAlmhSwkV7hkBMwh\nnIFICgHcBHodhtOj39ujXAVFaVM01a" +
       "W/zMYfkODGLavF2plyBgmxn+9BRL6H4ApQxLimb3NI85kn\nfkmmSF9wZnbs" +
       "6gLc2jX30hLF0wiH1XLvEWSzEhZuM6v3N1jspiRkU0oNysk0JAyuOEfAYUZm" +
       "ZBuh\nnZhsgEuT74mvxTne7TYXkVHciOAmH4E05dsK9JWerXAvcyWQw2NIGF" +
       "P0/5VAvss84Tt73VZqZS99\nUlbLEayebPJq94hsysyT7a0GH4VsW9MyPhyA" +
       "kDwmzdKWiYN4ybN07OCIaUHkLgR3gzEoVBqhbXAn\n59B3wDE7ybqYRrthaM" +
       "YaSU2A20xuxCYNZnByULOz4F3OxtYg8lME90LeywU6wvaAymYJs54qOmLq\n" +
       "/Q2OgPscAZBOT8tigrvvcKjxXFmbU8+7tSeScE7dI9QwZI8d/RWR+xE8ADvk" +
       "9HToDzPPDSwqhH36\nlT5zreAj2zAVdoa+g8gvEDxis8YE1qGvZGSqjzUPSa" +
       "2QcCD/Gs+VHV1cIQZ1nfDn7hIQPBi0NgQP\nu4tyZ4Zgj0PYFTglVCl3SuLK" +
       "OZonjsU83vETHnUI20AVfRPs1dJGnHaAkxPeyHP5X5niHu8hFvMD\ncGhneh" +
       "V6yEDVVRNRS/l6YKtgyzxX1ilG4PbOZhHuY57Hqg6L3vgwTaQVNP2JDm4NL4" +
       "XhyM2xiI8R\neRnBK4J6niY5Fh76pZdaKTFGUzrz9bjb7YHgLwXC/4SgjEVY" +
       "4D2xajjPPg3DOCResifzetDrBx3F\nseM9+AobtdUmLq4dBjwgvY3gHYbfw3" +
       "AqGo6jWWSUkTpHRkqS1eg6AODL8J/Ft9iEWz/XJLoQeR/B\nBwg+dAgnwZSc" +
       "gIfjA/pMRISr8weQT6FA+9lIjTfZx+wdF8Sfgi/hXSQSWgxZb6R79Wr3tgIR" +
       "yu8W\nLAHF+xCREWxBsNVN0BERySgCN/+tRsRNcAMTYwTuDownJUYwGpQJI3" +
       "DzUa4EIlVF8P2gHBbBD9wM\nFpGrEVyD4Fo3H0XkOgTXIziK/Iw0IfIjBDuO" +
       "ktX/OdVD5HYEd4xvHWQTIiKbQnBPUJqFwEmHxA6J\ndMhH8OZJCJz8RsSbny" +
       "PY7SOEwogc4+i6EpHHEDyO4JiE9l2IPIng18dMxtjpAyLPIHj2aIXv8ct4\n" +
       "3s0KEHkBwYsIXnJjPSKux/9jUARHsD8oeCM4TiLXt5cwnpiN4C03TCMioiyC" +
       "fwSFXwTvukEXkaAI\nOnZoReTjAMJEP+ETBJ+6kTPjRMjYiRohFyFiP8wR30" +
       "QcRfSckf+5mfuI2Hns4XvYxJ8VO8zq2BE8\np2ZkcnaTffcS2uCwLMXLCxFs" +
       "8hMuxcteBH3+/RlzD45Pwpgns5ON7zsvyBQ9V84NoLshaNoh/LYq\nRMcpes" +
       "yU6mxE3PzBJVyESCFNKKQJhTTh20k4wdMEToCb7lr3prtTZTSJjxXs3MFJHd" +
       "q/AakDgFD3\n8REBv0ZovMwX0QqhrhDqjmSBhVBXIBRCnef58motPajQgEi3" +
       "uhDpCgGNFAJaIaAVAto3l3CCBzQA\noTcDglfb0QSv/L/E8/2Al/8qytcyxo" +
       "8Kw4sRLMEmf9ALJJxgQa8Q2wqxrRDbCrHtOCOc+LHN+/Wl\nE9tajya2IfJN" +
       "vjErhKJCKCqEokIoOs4IJ34oCt0VEIpWFUJRIeKQQsQpRJxCxDnOCCd+xPH9" +
       "dhOv\n14qX4n3lJvjP3RutEioEoSihYtc/MQ3PO0tZ1aR4Jaztmz6qul569h" +
       "Lxonqdv1BMu5pOLd11kC44\ntyoeUKmonGn6IoWOUPF2EVZZCYsiGCB2Ud7S" +
       "EOv4G9dumZBI78oFpy6s/HuYFI1RbKjWalxPWdpQ\nPcUIoLd0xFWIZmVtSv" +
       "Z8akcaLogMy8+HefkQUXEkp6aYf1CLv85IheGfKD/CBj6BatgifL2+ET7T\n" +
       "iPjj/5FYi6BOFAsCEP5cnG1WeRy3mk127ZYeQ07JzHkee/PM+9569I31k8Tx" +
       "ieJojTn1ybxjRIE0\nu1QHSJiTTwLv/ex35jx05frXB606DhHCSERWWXBlH3" +
       "vqeF3PrHJW/LvY9kyc6vjaE2dS6r47F6kU\nBSEiExkpGtHkBLKMTPFoOm+Y" +
       "kAluiRQ5246fU+HTbm17e9C255oZj+iNjJSYvDpe/oXlFnDjdbrE\neeyNNH" +
       "0U/tWUJl56qmhQMp2aGr7Kd7mF7Xz16vg8K5xVnQyfWV+xqk8DDwBTx0hTJk" +
       "Swnk1k3hgr\nP1t4GVi/QtUkL6wWmYCgMZNj+bnHyl/Ix3IsNk2UcpK1qFNI" +
       "EIi5lZpQQLGYH5c1tjWMt5JU5HT8\n2bhTH8D/SwBRYCiT92yRRz3n1GC9ss" +
       "YVMVgdG3PUsTEDe1hq1XPDIjhTs03LqiEYn7t/8/xn9Nrf\nCzWx6/yVYu2K" +
       "tKJ4yxl58BLdoEMyP+1SUdxI53NdyUh1VlE5RspslO/zOaJnKy8bhj3xqo0r" +
       "xbJM\n6D/Ip5fMnlIAAA==");
}
