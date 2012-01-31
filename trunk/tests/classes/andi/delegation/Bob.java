package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Bob {
    final private SJGProtocol AliceBob = null;
    final private SJProtocol bob =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1Ya2wUVRS+u23pC9oC5Q0aeQkqW5CHQAulXVrasrQNLWJA" +
       "HtPZy3ba2Zlh5m4f\nUdGoJPgI8YFgCAmJUfGBDySo/wwxpokmGv1h0MRf+E" +
       "MS/WGMj0QwnnNn9t6Z7XZpxCo/SsLXc++5\n95xzzz33nDN79idS4NhkoUMd" +
       "RzONnggbtKgTwaFLdbR0UNYJ5D516Oua9ww293KYhGKkMEmTXdR2\nGJke61" +
       "H6lKoU0/SqqKnrVGUgqXrAJktySuUsVzJx/4W45AK1W9PjjCyJpbdX8U1VYn" +
       "tVYDtqWpCh\nCdHs6gFTUJer5NnCjjM7E8deDhMCO6ZZpj6Y0E3m7XDX1C36" +
       "YEfsh49uddfMybKmjUvdp15dM3fe\nV3XfF+ehyUWW6Wh4akZmxtKbXI+0e5" +
       "zqAcsasMDVi9FbEeRFpLechTuMpBnXDmhKl05jmsOuli9a\nceHHoxXcJfk6" +
       "zDBS4fM0rsGT33V9cXJ+Vj155LO9v9/ChYbUg+QQCXGjKqWUmGb00jiKn7i0" +
       "Y0/L\n/iPz88AZVn8+XhAsvT3XpdbThJbtTicwxU5QOMIU9wi6YiSqOpitGQ" +
       "k8xOIcMn0i1+snhl5p//kI\n3g7aHrYGnNxRtp2qVOujmRbh5a7OsW0b0EoC" +
       "HJdMpgxNVZiI1DOrH7t0/NGqX8MkbzcpojpNUoPB\nI7hld+BgitrbaSsqbX" +
       "AXVMdIadKViWIYmSajxI1oHsjCT3i4Ct8RF+V8SEZ8+Plwb+V13dNs9MNb\n" +
       "G+aeUIYhuQ2ImablSuhteWnXxZY/Ym7MdpnxQdyf5zvI0pzGdNmKoXZf35pc" +
       "IVifVUZ4F5miOZup\nBc6C+9AHcUEcc40CmxmZ6ntZTYrTvU2xqtNmWwQMny" +
       "wfiMcvKPz24sfT93+ZR8KNpEQ3lXijojLT\nbibFrNumTrepxwes2k3chIn9" +
       "RYAV8D8MwqYJ+7lAcKHSRXWftc2kqBu0RM04jZE8XekKPhw3B0FG\nybcaKh" +
       "gpaG1ra7/bctDeBStDs79gpLB+e11rtGmFNQBsRddUyrkz4TQpOx2sERQWGR" +
       "6s4cY7amqu\nzV+HcWSBRXVg8pwcG5TiT04uXvPd4TDJbwZPaAZtTWFtiJGy" +
       "OFV1BV95VFccbkIFZMsDEHOtSpJ6\n45IkZd1mXMyg0oIhSHdSZyd4VIGaUO" +
       "oSEeQwMgliQlrDxS8De+ewf/i4GbktF9vTOqFGMzS2Uagr\nZ6N8n4xM9g+5" +
       "OBSyWojaiMQ9CGsZKXJ6Ib8wyOZpdolkI6wXjFVIrERYxQKZRqzY5V9RbEBg" +
       "RXXT\nkPzP2eirNSOVGTOeZ4qZTTPkXkKiDqGekXzVtAYFa15uv8m0wv0mh5" +
       "4ynl+EsFm57Q8kOm5/YEZc\nRbMQWIjEVoRYkLEXiS0ITQi1grHMf1S3iZHu" +
       "lTyE6FgwLgUZDYLxAhtdfwcvzjcSPpEnPIXEToT7\nRql6CfO1ZorjNlIYfe" +
       "m2rBVo74blMH3DzB/BG5ivTcgU5VmdllaZMSPOsleI28R8ZaRPgyYNxLTb\n" +
       "JjNVU98MaQtyiuHoCmR0RuaNxPIsncG8GRpcKLQtR4L3W5DEinWq9NGookv+" +
       "UfCTaDZdaxps27Sb\nFCMOCTVxL06hJbOyTYvjJdICQxVIaAg9EIxcoVB2Dg" +
       "6UoSwKrSsdYELN1OCEUCAsDoUZmZ0hBH0t\nJFT4Rp6XpvJlDfEE3EpbH7Vt" +
       "LS6N+gaJgwiwuVisFPy3YL/Qh9efvuvSQMyU8J3RwOO7gkQ/wkBa\nNLa3gl" +
       "8LXXtANC9W9ZBe+JvwjdJ1RyqxqUzPf8ojIDjZzoaQkoeSliHIdHY6q0kYUt" +
       "IkdySu5uGx\nsONKkPGAYAxCKAYM7DBTtkobIaW6z9k3/FdMPOe/xAJ+AYK3" +
       "1h/QB2wMXSMe8YKvHVwFLvONvFvM\ns1Pi7sKdzPe1J0R0qN00ntIphGSZoL" +
       "3thbAdpYkX8QsSxxFOuNwtpiLfy4d+bqnCGE1aLLDilFyB\ncOomZ+gjMZb6" +
       "T1oOfug0oZmyoJXRfL3Mm/78IRyOCzXu70lpMu1u1R0LAU8i8RrC64xM9LgY" +
       "cDKY\n+qFtFjqSimZEtgFADsA/ntwCB74tZCi1IvE2wjsI7wrGDDBJ1Avcn2" +
       "VNGRLnOQOqFyqEznluxo8I\n7Tr0qk3wcUBt+Jy8/OmDDVufOOJ+MRmyA2be" +
       "FwfvfpvBOQl0itvKp/+mcO2doFc0ggVDSFQh8Iqz\nQvbESLj9H4JsOcuRkD" +
       "1l1l4UQTabo+lCETZkaz4RsrZMCO3/bVuEhOxmbqjNQeJ+hD0IssvYgMR+\n" +
       "hMz2Awm3IUCQYb08yBAVnRxFwq3oAYa/1CPIpoKnTB6nZoARCiMxxgWiFomH" +
       "EA4hjEl1Oo3E4wiH\nx0zHyBUQiacQnr5R5eeCOp6RhQ2J5xCeRzgmyxUSMv" +
       "m+mK0IIZzMVn8QbpIiMjZlB+FVWWmQcAsF\nwhvZKgjCWVk3kMhWBEauDkic" +
       "z8IoCzIucMb7gd823J9BhyX1dTeS1HN/DQd+FeVfS4GZkX6YuIbw\nF0JGMc" +
       "jG+F+LwXjOH8/54zl/POeTmyLnI/yW2bzzWy/7G2bG2Oh/HQAA"));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(bob, 20101);
            SJSocket aliceSocket = null;
            try {
                aliceSocket = ss.accept("alice");
                System.out.println("Bob accepted Alice\'s connection");
                {
                    String recvd =
                      (String) SJRuntime.receive("alice", aliceSocket);
                    System.out.println("Bob received: " + recvd);
                    SJRuntime.pass("Hello Alice from Bob", "alice",
                                   aliceSocket);
                    {
                        SJRuntime.negotiateNormalInwhile("alic", aliceSocket);
                        while (SJRuntime.insync("alic", aliceSocket)) {
                            SJRuntime.pass(new Integer(5), "alice",
                                           aliceSocket);
                        }
                    }
                    {
                        String _sjbranch_$0 =
                          SJRuntime.inlabel("alice", aliceSocket);
                        if (_sjbranch_$0.equals("BRANCH1")) {
                            {
                                System.out.println(
                                  "Bob received inbranch BRANCH1 from alice");
                            }
                        } else
                                  if (_sjbranch_$0.equals("NOOP2")) {
                                      {  }
                                  } else {
                                      throw new SJIOException(
                                        "Unexpected inbranch label: " +
                                        _sjbranch_$0);
                                  }
                    }
                }
            }
            finally {
                SJRuntime.close(aliceSocket);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Bob b = new Bob();
        b.run();
    }
    
    public Bob() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1319027693000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0abXAV1fXuy/eH5IOAfAaBqNDCi6g4SlBMQiLBR4hJQI2j" +
       "Yd++m2Rh3+52977k\n4TgOaitUp3SsAtVq6Vis9aMdwfoxrbW1arHa1ooVsR" +
       "39Y6fVqbYyjspYtD3n3v1+Lw+qxdopmXkn\n595z77nnnnvuOWd3z/1vkxLb" +
       "InGb2rZq6OvjbKNJbQ6N5HqqMDvet7JHtmyaatdk2+4HwqCy7mN1\n7e66a1" +
       "fEiDRA6nWjVVNlu3/EMjLDI/0jqp21yEmmoW0c1gzmcMzhsWTu4bFfb145vY" +
       "jUDJAaVe9j\nMlOVdkNnNMsGSHWappPUsltTKZoaIHU6pak+aqmypl4JAw0d" +
       "FrbVYV1mGYvavdQ2tFEcWG9nTGrx\nNd3OBKlWDN1mVkZhhmUzUptYL4/KzR" +
       "mmas0J1WYtCVI6pFItZX+JXE1iCVIypMnDMHBywt1FM+fY\n3In9MLxSBTGt" +
       "IVmh7pTiDaqeYmRWdIa346YLYQBMLUtTNmJ4SxXrMnSQeiGSJuvDzX3MUvVh" +
       "GFpi\nZGAVRqaNyxQGlZuyskEepoOMTImO6xEkGFXB1YJTGJkUHcY5wZlNi5" +
       "xZ4LRWl1Z/dEPPByfBiYPM\nKapoKH8pTGqMTOqlQ9SiukLFxEOZ+C1dl2Zm" +
       "xAiBwZMig8WY1pMfWZN442ezxJjpecas5rY4qBw+\na8bMfa1/qihCMcpNw1" +
       "bRFEI756fa41BasiZY92SPIxLjLvHnvb+8dNO99K8xUt5FShVDy6T1LlJB\n" +
       "9VS7g5cBnlB1KnpXDw3ZlHWRYo13lRq8DeoYUjWK6igB3JTZCMezJiGkDH4S" +
       "/FYQ8VeJgJHijri9\nnpFGRm1mN9uW0izrKbU5RTU6zI27GQdkkc+EMUmCLc" +
       "yIXicNbG+FoaWoNajc/fqzV3Vc+NUt4nDQ\noBwJGJmIjOM+43ibkSSSxHlO" +
       "RJMTKmm1LHkjXoXsNftm3rpXvgMUDBu11Ssp34c0VowQJp1e0FO0\n+/esCz" +
       "AZzGBQadj0xrTbXrzn6RgpyustEl5np2GlZQ0P3L0e9c5yUQrYSVPUWvOt/b" +
       "cbVj24/7lX\n5/l2y0hTznXKnYnXYU5U55ah0BS4G5/9jg9XvHNzyTkPxUgx" +
       "3DHwMkwG24Ar2xhdI3QtWlwXg3sp\nSpCqoZyNVzJQ0FhgwwirEdQKu4CzaI" +
       "gIyL3ToetKT3v5saqn+Y5dR1YT8Hh9lIlrUeeff79FKfS/\n+s2em7e9vfmy" +
       "IriIpinOnJFSM5PUVCULU04M3yQUL4Vm89aeltqtC+2H+RFXqOl0hslJjYLn" +
       "lTXN\nGKOpQcZdT13AzXHvApqoToKXAoc3qAEjsVdTGgXzzOMH4lMabtk+//" +
       "aX0VOYXDGT8UZxSUmWdzRJ\nCOflELE9Ey24wd823O0NYgPV8/suX7luyxy+" +
       "8yC7BU4j6zGM8Z4YcFpQ8C50YkTxTUW96v1r9jy/\nrTpGYgOkTLU7VV3WUF" +
       "l2tzD+PL40wuLKx9d8+9Bv2Gv8aH1rQ8FmZHN9xFo5cBHO3j9aV/rAznSM\n" +
       "lA2QWh4PZZ2tlbUMHvwARDS73elMkBNC9HB0Eq64xbtNM6KWHlg2aue+bwIc" +
       "RyNeLkybj6n5p/j7\nGH+od2wIn1nfbqRNcLTWSRdQ2LTMaMrMSpKJ0xbzyY" +
       "0czhbHF2OwNKqYkTLTUkdlTCtIOWhaoeAD\n+dCJIL13hFZGZ2qawsFdABed" +
       "GRAP+D3jrvLMI5+0O8lX+bobD1y06pKLm0VYW1SQRTcoJhU57m1d\n+rLHJ+" +
       "3XY+gPSu31AeXFbMbIvITL0tE8NgXWt7JPkERox1mnITjL1VhbHo0hvgRBC2" +
       "iqKOkraXoe\nJUV0hP86QFFNkV16IsVRJCbi/d79S/fobMbr3I7LnEQPsy3f" +
       "SUEE1kA1PIRbcJsLcfU2Oqg4YVYS\nzk8ZUbUUStYKPOYW1L+YflNZ390XD9" +
       "+yK+b4iyqTO8pTfI/hy2U3rdHTRkodUrmnAx9yuObkRQ+9\ntbVWxBrfm4Gj" +
       "ODIDv39qG9n03BUfNHI2koI8uhxnt8ALw6cW0kgbHVbzKaSUydYwZSI5gG0V" +
       "4BFg\nsUTbsfeunoNbXKWsNDESFDqSXqpQdZRGJcBbsLjAtFWAQ7YK9zyd0V" +
       "WF5yuCx92Lrzuw/drm9yC8\nXEbKIZlJU52BxTReFkqbIY70W5AXdYgB4H2q" +
       "0oJnv+trIkrw93NyQRPTU7mbwblrjqiLLn0MrDBH\nF7mnUUiAhGGYgsOGlX" +
       "cOPLHyUELYWNJIbRT27W9kfkFhkhb4lpEjS1PIvtry8oCoVq/ay6kJygLl\n" +
       "axtFcgC3UIbJkIkGbvcK2R5ZJZstrtgmCacjDr2k7A9PPDl53QtFJNZJKjVD" +
       "TnXKPEkjFZAdUXsE\n8t+suex8LkL1WDnAWic0T/Lk5wxBhXKSagFpu0j5CK" +
       "zSbqQgSynS5CSKckbWLDY7aiF0dK9e3XO6\nuHTKGdK05yGKtPW2drevWGSC" +
       "cyyRMYxw6iUgfcZyLTGOlhjPtcRY5xeWLv1ozjk8bwEJukDE6QUm\nyBVP33" +
       "bKWa9+BfLKLtg5RPnuDHrJBJmAUVfGB0SePznxoBwfQbr9+FApHjK7/UcBQk" +
       "r2wpOvvyYk\n4FSGJ9YqgcSRwsgJYAO+NJw9am06+4Q3l5HZhcjOqqVLVV1l" +
       "53nL1bCjvI+QUQabnB0yYR6r8xAZ\nRTAG4d/eAM6Dge91yZU+GcFGj3AmIj" +
       "y1AtsNuhFvxEBwRIUOhtSuGbpP/y07+rjFSEOkx9FMBYPM\nPMz3ACKbEFwD" +
       "T5KKYfpCzyysN9+NcL35TWcx7k88ZlMLyx9ybFz+UI93FFs8hmWI3IDgxjDh" +
       "CkSu\nR7AZwdUeYWFwqyKc++r1aQiuPRaEA2HClz3CNnZ0mQ7cuEDL04m/w9" +
       "sR2Y5gx1EuPY8FUhnZFsk+\nWp+bxnQD7pyw33RPmAUt+FwWyAGirBypXW4N" +
       "kR5vL3f4FyJoMKOqrbJAnrgc3BbObsuo+NICYve4\nNEfWCUls8pwYB3jLtC" +
       "ByJ4Lvwv3QqDxK2+Ep06NvBQV5T0JCjA7LMqwVsp4CTzq8FrsMkGBqvm5v\n" +
       "X99zGUq1iHwfwT1ghXxBb7HdjMyMLOa8wvSWmRju8Ba4z1sAnlWmRZigkj0O" +
       "tYGWo5yJfFhHahiO\nY/UotSw15Qv1CiIPIADxKryRHv0HLPBwjefuHnJVyF" +
       "gq+cz20K17E5GHETzissbU16MvY2RKiDWP\nUm3gV/hlCLTcgOMvYlHfL//D" +
       "3wKCPfn2huBH/qZ8yRA86hF25hUJTcoXSbS8o3n8WMjxZpjwY4+w\nEUwxJG" +
       "CfkbEU2gm+VNzjQPM/IuLu4CGW8APwaGcHDXrIQtPVU3HH+HpAVaCyQMs5xS" +
       "J4MHRZxPpZ\n4B2ux6JPGaGpjIZXf4KHO9PLYDpy827Eu4i8hGC/oF5gyN4N" +
       "lx4NUqtkxmjaZKERt/sjEPzxOOEz\nIWjjEeYHT6wGzrPfwNc5kIupgWTsvq" +
       "Af9AzHfe8DvsJFXbNRRNtjwJOLvyN4h+FHH07Fi+NZFmaB\n9d4aaVnV46sA" +
       "gC/Dfw7fEhsehvwr0Y3IewjeR/CBRzgRRPICHs7PM2YCIh9yAoRfXNB9q1Ln" +
       "5+Jd\nsNNh3KF4sHD/Z3DkemDS5rIr2YsIX8dAYPoZOiIiG0XgS1+DiJ/h5s" +
       "2MEfiJ39HkxAiuypcKI8ib\nwCH4+mebpCHi51afKulC5FsI+Fw/5zkXkZ0I" +
       "vhMmcJWILAXBrnzpCwIvzSBbERFpRogQzD8QeHmD\n8OM83vwwRJD4q7xjHL" +
       "WWIfIYgp8iOCYhkyv2SQRPHbM1xg/LiDyL4LlPu/ju8BrP+9EWkRcQ7EPw\n" +
       "oh9DEfE96cv5IiOCV/IFRQSfk4jw/0s4mliI4G0//CEioheCg/nCGoJ3/WCG" +
       "SL7INH7IQuTDPIQJ\nYcJhN0rVBt9S4WunnODU+mmCU+F3DKEXyfwZNNQzzu" +
       "se6UIECewKB7W8hP9qUDseu47HruOx63js\n+pwR/sdjFwBpefRhin9r6hFl" +
       "HKHv3vzb9VyneIQgFMUjbuWHbQXeiEQK43hR3+ZLDlZfLz91ufjC\nWR8uke" +
       "nQM+nFOw/Q+edXK3nqsyqYYS7U6CgV7y7C9SULC36yXsU/8fjlAkV9y+afuq" +
       "DqLzFSPE6Z\nVZ3T2UtZxtID30RhtPxv11/NiiglKk/d6PSLikbUZ2K88kMU" +
       "i+SUR4YntYRLRCqtsKD8CKdzAWpA\nRfi9rx6zCyL++H8k1iGoF2VSaAtfE2" +
       "c7TmFQXqJb8IZtyIIm+llQR1ahJr62QP7Src67r9zanR5L\nTavMy1Vuatz1" +
       "5wdf720QNiCKRefm1GsG54iCUbcOAVaYXWgFPvqpL86+/+re15LO52npG4wU" +
       "jxpq\nCjcn7QpbdbQhbfcUi79T4dfuKLY9n2JzLxKP3HMZKbV5KW9hxeZWm/" +
       "IaRKGsPUVNB2M/mdzEy+qK\nk7LtfbwPlenmVuGGimu5nJXerqbCb1bhXUnL" +
       "8xrAIiQ9mJUIls5ID42z8yXCj8D+NQpp8ghX6p2m\nUO5dYCcgLaJ7ckvJcq" +
       "2Mf/LDuhCXJqrWVCPuFUEDMZtPXGmHEJev9QnNP1A0J/0CP715XyDDjwSi\n" +
       "GCxb8KiRx07O6V7nDRi3xhyblALKCfVgNVKbkcRKnCnRK+DUPitz9q2b96RZ" +
       "9ythMW59chl+Gc9o\nWrDsLICXmhYdUrmZlIkiNJPL+QIjNZG6WUYq/QbX8u" +
       "/E2N/DnnAs4i9x+3gmK/0LSUM+/1YvAAA=");
}
