package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client5 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1afWwcRxWfPdu1nQTHtfNBm7hfJGlSpWdSSNTUbpP6C9tc" +
       "HIOdUqWi6d7d5Lzx\n3u5md86xUakQBSmAFFToh2grVUWgCkEEAZWiig9VCE" +
       "UCCQn+QAWJv4pEK8EfCCGQCBLvtx8zu+e9\nq1OT1hYbKc9v5s28N/Pmzfu9" +
       "3b3v/JW1eS7b5XHPM2zrTF4sOdzLoxlwM5MzXMwSe6p0+XeD37fE\nzjdyTC" +
       "uw9iqvFrnrCbatcEZf0PtrwjD7h23T5CVBmgYWXba3qVZfFGhmwT/N19xWmj" +
       "PMsmB7C9H0\nfn9Sv5zen5gOSx+oswRqF8/QUmArMPJE+8xLn6g8+Y0cYzRj" +
       "q2ObSxXTFuGMYMz9u185UXjzZzcH\nY3akjDnuaz1VunJoZ99v7/9TZwuW3O" +
       "HYnoFdC/b+QjQp8Mh0KBlYdJxFh1y9B97KQ5ZX3vJ2nbCq\ndtk4behFkxcM" +
       "T1zZvPvAy3+50O27pNWkHsG6Y57GGOx8/9urU/03DLHP/Orhf97kK9VKZ9lj" +
       "TPMX\ntUVpKRjWPC9D/aZ9M5+cfOT8bS3kDOdcKw6Iht7e5FCHh3jFWHamcO" +
       "WeJrNik+4xn7n8zem/ncck\nrC7nLJLJ3U3jyCqnGTzYZM4x4vUK+aVarVlG" +
       "SRcyEF86+PjrT3+2/x851vIQ6+Amr3JLUIzf9FDg\nelO3Kv0zQi/Nz7p6iY" +
       "8GAwYKbGM10Ak1gm1VQRAErB+nBXad0N0Kp5PsSahzDasyIPeLvz3+vpvd\n" +
       "n4/zEjcW+PKtY3pvTI1St6+JuuM1cY6uXYq+5u4v2LYTzJmf/PrJ1yb/VQgC" +
       "tmiXl2C5JdqWE1vI\nHc0XUnR1qzSXtpJmsTeUOit3kvUY3gh3KEzopMwlDC" +
       "gjyeg0WbDe2JUa1725Y7ojT8JhtNbr1c0I\n5W3tf3jt59se+U0Ly42xDaat" +
       "l8f0krDdCdYp5lzuzdlmedE5ctRfwqZzHUS76X+OlG2V6/cVkvv0\nIjdjq5" +
       "1gHXNkZdgu8wJrMfViMlSC5EOphN3yrRsFax19YHQq8qtTc6MwzWNwfnmY5s" +
       "buGBz8z22H\n4UyHLI7TknY0maB3/uLZPYf++Pkca52gnRoWn6oh6RdYV5mX" +
       "TB1xO2zqnr+EbZQGT1METelVHrY3\nVLmYs8uyB0bbLlMeUzZnyWM6JfuNAZ" +
       "OHRLD30SVRq/HV52m9O8Q7vNaC3dpMHFq9btCwDHGfNLdZ\nrDDzCHZ9vOmr" +
       "g5JDUtV9YO4GOSxYhzdPmUVQmo7EG5QYZEAKPgzGJwdFIsfIESfjIzotCpxh" +
       "07aU\n/Ndi5TAs2Ja6ntAzncLldXpfBzMEMkyBWLKdJSnaK2J4rHsBemJlER" +
       "ZPER96TTVDU60ivrvD8SOo\nVxXc+EhZb7IjVLetbHh6tWhUarrgypw0cEWw" +
       "vvpLSRHiUCC7J6jp+T5J9MjjnYq0aBqYaZCPJQRs\nVzNXIHfGXKGayy2wKw" +
       "0sNDT9ngrYh0QM8ev3HRaU0da31PVcxe7Zg/HoXjCo1CL9064t7JJtjlCO\n" +
       "gtqhGtWT3CUUbygLo6WriOaYwc0yBkgzA2CKILTeTpPrC3xYN5X8Ah2zrBWD" +
       "ZYy6ru2O61aZ0mbl\nAXTZtIIb0rrlhk9Lx3aDmQMxBGvzDUpjlyhk64wNU+" +
       "XJF4U005vskAbmpYGcYDfWKYH3pYbuWCt0\nTq8/bLRcoXM6vsBd14jdo9+D" +
       "cUDOkofkSCm/SPOlPQREdPobE9d1gz9zGA8AcupbYBZAzkWqUZ1K\n+REquh" +
       "OqfUgaogIE+rtjrQhdlBGXqyT8b7UFEDdtbyDK8kW1MpBFKXghdUkIKbWkoC" +
       "WP5rFrsY63\nkoJPScEShWJigTN2zS3xMUpyQTaKNf8nS7wUP8Q2/wCk7O54" +
       "QJ92EbpWOR8G3zS5ilwWa4Wn2OLW\n5NnlZkXsYU2qmCnN8XLNxNXvknw4vZ" +
       "2mQ5u8EX8H8xTI04H0I7Yub7j2o7h0oy4ErzoiMeJ5NQLk\nuUzwrgjMRoJ9" +
       "8RPbTOc5awPGqfAyYpXXt+N5UAZOhPeUKyI2CptS0JYKvgjmuyDfE2xTKMXF" +
       "kZGF\niO+RNqq6YeWPEaFchj+h3jaPHg3VlZgC8zLID0FekYLttCQJeJifMq" +
       "YLzKu+gOopGKQ6f2fdu4xp\nkyrrcRvgR4+9b/zy0dGPfuF88OxmqXo9UasH" +
       "D6rwgWnQ08FdvsHbmXaACuWW4yMj6kkEzH7/ASNc\nU9tlMB8EOQByl6rpwQ" +
       "T1K4gqmTeDUTVxai0NoorllVTRIKrCP5kUHFVVM5igugUZSSt7QcZU0Qtm\n" +
       "HGQCZFKVsGAKIMdAVlHSsV1gZkFOrFLVe1wdgnkY5NTK9sEeBBMUYCCqOBhI" +
       "CmQFxS6ACSqohCBe\nWoHIkiiAqCqIlRBoOTDXGJCPgHkU5NMg16QaeAHM4y" +
       "Cfu2Y2GlccYPxk+aXVGr+UtPFlVUiAeQLk\nKyBfVeUBGAUSz6SBPsjX0vAe" +
       "ZI2A3f+vYCUwD3JRITuYAJhBLqUhNsgPFE6DSQPdxmgM5tUUQVdS\n8GOQny" +
       "rkXJQIObpeEfJOMNH7n+CTzSrQs6/5qzb1llm+KUm8n/JfN0tlPeIqXnULtr" +
       "W+K3rg0fqk\nynY0bwa5JSkAimk4IW1n0j8NfbA2BQ1P5imxso+DVCnGWvKZ" +
       "UTkEV1vbD3LnCk03LKnuBaPqByU4\nKmKfCFJfB7m65Zm6/4qjr5EoDKrtIu" +
       "zhyYHSGi6rhlpG8y9IVntktQdIVnu8nWCdg7kv+IkIPw36\nj8YTluAVvC+I" +
       "EF4CvITSdQzwRLTtawOn3gGAnU3gzrsMSGfjEKEdzLAjww6WYUeGHbHXqiN2" +
       "rWjy\nFOi4N4OODCEyhMgQIkOIqxGsc4Qgor2YggaDq0GD5j8CS/yW1P9BTq" +
       "Knwe/ZtD+DvImuJIqkCtYZ\nimRgkYFFBhYZWLA1Dxbx70oSLGTezR4dlguy" +
       "3J7l9iy3Z7mdrfncrp1Kye33ZLk9S+EsS+FZCs9S\nOFvzKTzxsy+0u/4LkT" +
       "yHAHM/AAA="));
    
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
