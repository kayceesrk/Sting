package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client1
  implements Runnable
{
    final private SJProtocol
      invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1ba2wdxRWee23HzgPbeVLIozySkAS4DqWhBDsJ+NXY3DgG" +
       "OwESSjJ3d3zvxnt3\nN7tz/ZD64KkGfkRCvERLBQoghPqQUqmCfxABSlUkpJ" +
       "aHaCV+hR9Fgh9VVbVSg8Q5s7szu9d7L27d\nqDbZSDk5s2fmnDNnZs755u7m" +
       "V1+QJs8lGz3meYZtHcvxaYd5OWz63MjgCOOjwB7Rzn7U9VuLrzuX\nJZk8aS" +
       "6zcoG5Hidr8sfoBO2ocMPs6LFNk2kcNHVOuWRLXa1C5Gsm/p+M0NyklQxT52" +
       "RLPhzeIQZ1\nyOEdseFo6eoqS0jtwjFwBW35Rh5vHnnlruKTL2UJgRGrHduc" +
       "Lpo2D0b4fW7b9NqB/F/f+LbfZ21C\nn/1C6xHt/E3r1v/ptk8XN6DLLY7tGT" +
       "hrTr6VDwf5ERkOJJ1TjjPlQKg3Y7RyKMupaHkbD1hlWzfG\nDFowWd7w+Pm2" +
       "TTf87vOT7SIkjSY84aQ9EmnsgzO/7uvVqeeXdZP7373vnxuE0ox2nPyYZIRT" +
       "q5SW\nvGGNMx3VL9s68oPBoyeuaoBgOJONuEDQ9Zo6i9rTzYrGjDXFUG6uMy" +
       "oy6BbzmbMvD//tBA5C77LO\nFJjcVHcfWXqSwR11xuwDnhYhLuVyxTI0yuVG" +
       "fGXHQx8//WDHP7Kk4TBpYSYrM4vDHt9w2A+9Sa1i\nxwin2vioSzXW53fozJ" +
       "OlZV8nquFktdoE/oYV+zRPFnHqFhms5IqYOtewip1yvvjvCjHveufnTqYx\n" +
       "Y4LNnDoOXxlRo9RtraNuf4VPwrFL0Fc//Hnbdvwx44OnDp0Z/Ffe37AFW59G" +
       "yw3V03IqbhjNHE4/\nNzOa2f5tXV1fXrUTrTvgxm7wfW2dAXTxOz/bfNMnj2" +
       "RJ4wBZYhoWG6pgbsqTVp1pJsXw9pjUEy6s\ngdM6BhMdomUWtJeUGS/ZunyC" +
       "RpvOwnFTNkdLLqOQk5b6TA4lnFwCa6m8EeqvBn/X8v9y93FyZT1x\nYHVRl2" +
       "EZfLc018ZneUA4WR5tCnWo5Fqpajcy1yPJcdLijcMB4JBNQvESJUayXQq+i8" +
       "xWJNt47CjI\nHoeiPRZbts56TNtS8vf47KsFJ6uqngSRWcxdVq0Xme8huRnJ" +
       "znku+FgJOGnUbGdaip7is6vSsG8j\nLbnIO6Si55DpRdJX0ziSbinYwiMFln" +
       "p+OcQ1DIvrEPDB/lLNYFEaeXQf7OKRUlCtKvA61Laq6omc\ny+1S3a08Uo8m" +
       "DCi1oGbYtbmt2WYvHH44mZZnUm67nKyvJQo8vZQHT1i8o7S2HZlhJHfAZjMZ" +
       "nWA9\n1FTykxAnCRl8b/pc13b3UkuHtFQ8iI/Qk8uSHsvpjYYKM+3IHERyFy" +
       "dNwqA0dhomVGWsBwAIm+LS\nzMr4A2ngHmkgy8nlVUow1lJDe6QVRGml6Nan" +
       "F2FV9k8w1zV05dSfkbkPyRGIkOwp5b+G8dIeLn+4\n1ktje2aJGNmDOFAO/Q" +
       "wZHQkLVSNIkfI9gL1iqkXK74Y6JM5EpBVmb2XEZSrJ/VtNAcnRpLkhKahJ\n" +
       "Kc+QjEnB84ku4ZZSLvktuTTmhfDjs7igJAXTsBVjDo7YFVdj/VAf/eMcaf5P" +
       "XDwdXcQmsQBSdnN0\nQ4+5uHUtPRdsvmEIFYQs0gpWscGtyLXLjvIIZpcqRr" +
       "QS0ysmg/3cKvlgeDMMR23yRPwdmYeQPOxL\nv29TOePM61HpUso5Kzs81uM5" +
       "1QPJo/NcYNYSbI3OtA3iMGoDJHEAEBgRRPDLaP6QAceOhoj3JSEb\nhlvz21" +
       "LBY8g8heRpTpYFUtxwckXIJMBlaaNMDSu3DwjkAPwn0NvkAbJWW2kImZ8jER" +
       "P/hRRcCi7J\neoHjE/q0IvOCEED1QoOAP9dVXQWHTUB8e21TZy7cGs794Yd9" +
       "tz96woe+lsKRAGpst5ijDoVtl4PZ\nlSF2wuUx054U+dqwKtS/OTZrpgFo9j" +
       "sRxL6tPmIvQL3SSkmQvd4lrTtxVPYQWWFANoJqrIMX5jR2\n0PE2TmEwpO3I" +
       "3XMv9Ur7qCOvLA4BX5erK2Qgb2r+y5m31hz9YwPJ9gMut6neTzU4vQMA1ABG" +
       "eyUI\n35Sz51bhwrLJFqDt8DcLylZL/4VCuGfQAjMj3g6QlhJY6YGqkScNJi" +
       "3E71T+LR3u3OSKVy+HVew7\n2DekbiDIXAmK9oZr3nQWmU1INiO5RmF5ZHzc" +
       "ikRB5TZkFBZOxNBIFEieDXpGcmMSaEaiINxs8COS\nCIRDZgDJIBKFoHZGbw" +
       "/VgMzfKmGNXhl/EBy9Nbrh0XLBKFYEcArhmjRwHtJ69WqGWeEAND0B9GJP\n" +
       "ZKk5I9NRBpm3kLwdE5CN9bAp3k4j2FQ1Z1og52tYqGn6/yogNyKzD8nQ7OZB" +
       "7o5esRKxMkSuu2Jg\nTuNkQ01ZsO6tBWz2G8zUYyC5E5n3kXyARIJZchIZH8" +
       "zGBFGUi0SiUx8tHEZyb0yQySJzgbHRHmSO\nIRlHckGA2fPIHEfiXjAbtcEf" +
       "MiKFT8/V+Om4jR8pTIfMT5Dcj+QBhdSQUbjjkST8heSnSdALyTzB\nTxevYD" +
       "bIEckTCiwi42M9JM8kgUAkzyroh0wSjqsN8JB5IUHQGhecEoIXYz/y+T/EVg" +
       "Gx35DMDW1w\nzdjf25sCiEQAgcwbSN5EMoeCSjYiIyL5+zmqWnC1GRlVND9M" +
       "q2laTUlaTS8ewQKvpkAyi1TlnJIV\nsm+hVkihJXwT43+SMIfqub7+Ozr1Fl" +
       "XeU2MvtsTrVKlsBf8PXuVysrr6UXj5zahLejM2i0hKcQHm\nJ/GhQEavGR+U" +
       "Hvn64MxPQc0lEydIvSab0/szZJKw0y5kFFCIvNdCxn/ThOROKdgeF6R4IMUD" +
       "SFI8\nQL4RdVQI4Fa6XN1KByzOiviLXFhcZW3d9Q2orccXQO1IS0RaItISkZ" +
       "YIMq9KROSHy167UjBZQoXo\nSitEWghIWgjSQpAWgpmCBV4IgGTOJST9zrkk" +
       "/fqfYcf+04H4biP2pMYX5VnMaVlhLl4sEgULrFik\nNSGtCWlNSGsCmTc1YV" +
       "FCTbglvQikKZykKTxN4WkKJ/M/hZ9KSOEyyVzEKTzN1GmmTjN1mqnJvMnU\n" +
       "0Y+e8GHrVxMuu/BRRQAA"));
    
    public void
      run(
      ) {
        try {
            final SJService c =
              SJService.
                create(
                invitation,
                "localhost",
                1000);
            c.
              participantName(
              "client1");
            c.
              addParticipant(
              "client2",
              "localhost",
              20102);
            SJSocketGroup ps =
              null;
            try {
                ps =
                  c.
                    request();
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
                Continuation co =
                  Continuation.
                    startWith(
                    new myRunnable(
                      ));
                SJRuntime.
                  send(
                  co,
                  "client2",
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
            
        }
    }
    
    static class myRunnable
      implements Runnable
    {
        
        public void
          run(
          ) {
            System.
              out.
              println(
              "started!");
            Continuation.
              suspend();
            System.
              out.
              println(
              "hello");
        }
        
        public myRunnable() {
            super();
        }
        
        final public static String
          jlc$CompilerVersion$jl =
          "2.3.0";
        final public static long
          jlc$SourceLastModified$jl =
          1326279262000L;
        final public static String
          jlc$ClassType$jl =
          ("H4sIAAAAAAAAAJVWW2xURRie3W23t9V2S0FCL9xqSlPZVRJNpA/aNBAKS6i7" +
           "BaSELGfPmd1OmT3n\nODNnu0VDUBNBHkwMeMF4eTExGl6UqC8mmgDen2oivu" +
           "ALRkkUow9GYtD4z5xz9nJ2W+MmZ3Yu/z//\n9fv/uXATtXKGEhxzTixzPiEW" +
           "bczVaOXmsS54IrN7WmMcG5NU43wGDrL60X/IgXfjT+0Ko9As6jWt\nCUo0Pj" +
           "PHLKcwNzNHeJmhDbZFFwvUEt6NDXds33x74etTu/sjqHsWdRMzIzRB9EnLFL" +
           "gsZlGsiIs5\nzPiEYWBjFsVNjI0MZkSj5DgQWiYI5qRgasJhmKcxt2hJEvZy" +
           "x8ZMyfQ3UyimWyYXzNGFxbhAPal5\nraQlHUFoMkW4GE+haJ5gavDH0AkUTq" +
           "HWPNUKQLgm5VuRVDcmd8p9IO8koCbLazr2WVqOEdMQaH2Q\no2Lx8B4gANa2" +
           "IhZzVkVUi6nBBup1VaKaWUhmBCNmAUhbLQekCLRu2UuBqN3W9GNaAWcFWhuk" +
           "m3aP\ngKpDuUWyCLQ6SKZugpitC8SsJlr7orG/z0z/uQEiDjobWKdS/ygwDQ" +
           "WY0jiPGTZ17DLechLnpg45\nA2GEgHh1gNilmbj7w/2pGx+vd2n6m9DsU7mY" +
           "1W8/MDC4NPFDR0Sq0W5bnMhUqLNcRXXaOxkv25Dd\nayo3ysOEf/hJ+tNDJ9" +
           "/BP4dR+xSK6hZ1iuYU6sCmMenN22CeIiZ2d/fl8xyLKdRC1VbUUmtwR55Q\n" +
           "LN3RCnNbE3NqXrYRQu3wtcB3BLm//XIQqHOSEmyK+xJ8XqBtAnPBk5zpSdth" +
           "hoOTkK2CmI5K8ywu\n2xaDZSFZZSpLCXcuhEJg3EAQaBSycpdFDcyy+lvXv3" +
           "xix55nT7thk6nm6SbQiCssUStMhlzJGC4u\nph3T1HIUo1BIyVklE9R14ARj" +
           "2qIETvnJpcHzn2mvQTjALZwcx8rq0IK0WTJtW7GuTFZROQUzDZIm\nq/edvL" +
           "HulW/evhJGkaa1JVXZ3GmxokZlevhg6vXEBU8gq4aDud1M9q9n9l789qtrW6" +
           "pZLtBwA/ga\nOSV4NgXjwCwdG1Ccqte/9Neu3862Pvh+GLUAIqXjNcgkAPhQ" +
           "UEYdiMb9giRtiaRQV77B8E4BDlqo\nMViOMTn0uLkCsegLKKhq2a2no/de/a" +
           "jrirLYL3vdNfUxg4ULong1/jMMY9i/9vL02RdunjocAdja\ntoo5KgPlXfVw" +
           "k1oZMlt+eW+857mt/AMV2Q5SLDpCJhiUZ41SawEbWaHqU7ymFqoSBA6I5aCU" +
           "AQiy\nFC5yTbRDJcjKJsUisbbv3Iujr16V5cRW/lgjVfMVlBubQ3IcaTiU60" +
           "GZuH1Va6EAHHMNiI1mjuw+\nenqTMrj2ujFv4V2OSo2w3Cn7i58Kxdzjf1x6" +
           "vXNDVcOx2uuG1LjRFRESKMpVb/Rvr6o+5gFNzldB\nE1FKS78lfPTKeAwuV9" +
           "ZVSzr16O+xZ7TLR9zi21sfux2mU7z/je/w6MMxvUkNiXpN2ndjFfhbVwT+\n" +
           "XtUCq8CIZB4aHbmn6ycAxjKYj3ubaQz93pTKS5ntqhho/7sYrA94JKhPvNT/" +
           "SGSOfB6WcPPqQENn\nr2car/UNAJLVK6oyq18p0A0u6oBvFXwHvb6g/uVhXA" +
           "69Hmab5oLt5GglF5ZJ4//M8QmBIswxeWP7\nnmakCN2x5LXv54fe/PHi9XSf" +
           "G3z3jbO54ZlRy+O+c5SULlum38aVJCjqy2MbL5xIf58Le9ptF6il\nZBFDGb" +
           "C3ER8VuEEnrTYqHwUDKzU20Gdtw8vUfT/pm5aObrlkx79Q5bnyxmmDh0beob" +
           "QmuLWBjtoM\n54lSu82turb6Oyig4DZRRKA76tZK6wMuz6wKr+SRq8O2b1FP" +
           "FdfuQ6gc+hcoHr9luAsAAA==");
    }
    
    
    public static void
      main(
      String[] args)
          throws Exception {
        Client1 myClient1 =
          new Client1(
          );
        Continuation c =
          Continuation.
            startWith(
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
      1326279262000L;
    final public static String
      jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cC5AUxbV39/53ch8+Iv/PqWBgT4ha6qEIy50cLsd5d6Ae" +
       "0WNut29vYHZmmOm9\nWyxj/MRA1FBSir8iYiyNZTRVAcuYxERjqSGJ+VRMBT" +
       "EpTJVaUSuaxEolUglq3uue6ZnZ3VsxSMLh\nUrWP1/26X//er2du3mPvknLb" +
       "IlGb2rZq6BujbItJbQ6NgY00wexoz6ouxbJpMqYptt0LhP7Ehg/V\ndXsab1" +
       "gZJqE+0qQbyzRVsXuHLCOTGuodUu2sRWaZhrYlpRnM4ZjH4/y5h0d+sXXV1A" +
       "ip7yP1qt7D\nFKYmYobOaJb1kbo0TQ9Qy16WTNJkH2nUKU32UEtVNPVqaGjo" +
       "MLCtpnSFZSxqd1Pb0IaxYZOdManF\nx3Qr46QuYeg2szIJZlg2Iw3xjcqw0p" +
       "JhqtYSV23WGicVgyrVkvZmci0Jx0n5oKakoOGkuLuKFs6x\npR3roXmNCtO0" +
       "BpUEdbuUbVL1JCMzc3vIFTdfAg2ga2WasiFDDlWmK1BBmsSUNEVPtfQwS9VT" +
       "0LTc\nyMAojEwZlSk0qjKVxCYlRfsZmZzbrkuQoFU13xbswsjE3GacE5zZlJ" +
       "wz853Wmoq6D27uen8WnDjM\nOUkTGs6/AjrNyOnUTQepRfUEFR0PZaJ3dFyR" +
       "mRYmBBpPzGks2iw79cm18beemSnaTC3QZg2Xxf7E\n4XOmTX9p2RvVEZxGlW" +
       "nYKopCYOX8VLscSmvWBOmeJDkiMeoSf9z9kyuu+xb9c5hUdZCKhKFl0noH\n" +
       "qaZ6MubglYDHVZ2K2jWDgzZlHaRM41UVBi/DdgyqGsXtKAfcVNgQx7MmIaQS" +
       "fiH4USL+1SJgpCam\nqVRni6L2RkYWM2ozu8W2Ei1mxkpmaAtIK1P1DBfzfp" +
       "o1DQuKqRavUxZHGDcSCsHipuUqmgZSudLQ\nktTqTzz8+s+vabvkq9vEsaGo" +
       "OXNj0I8PFvUPhkfOxyChEGc+HqVS7Noyy1K2oLZkr39p+j37lK/D\nGcBe2O" +
       "rVlC81NFKGEDotLmpMYp4qdgCmgKT0JyZc99aUe3/7yAthEiloUOKyst2w0o" +
       "qGMuFqUJMz\nXC4FRKk5V6ALjf2Xm1c/vv/Fg/M80WakOU/j8nuixszJ3XzL" +
       "SNAkWCSP/V3/Wvm328vPeyJMykAN\ncbcVEB/Q6hm5YwQ0p9W1QriWSJzUDu" +
       "YtvIbBBo34FoywDkGDEBA4iwk5E+QG7NCNFWe+/MPaF/iK\nXVtX7zOKPZQJ" +
       "zWn0zr/XohTqD97ddfvOd7euj4CumqY4c0YqzMyApiay0OXkoLLh9JIoNu/s" +
       "bW3Y\nvtD+Lj/iajWdzjBlQKNgnBVNM0Zosp9x69Tos4TcAMFO1A2AIQMV6N" +
       "eAkVirGRoG8SxgKqKTJ9xx\n5/xdL6MxMfnGTEKl4zMlWV7RHEI4L4+I5eko" +
       "wRO8ZYP6bxILqJvfc+WqDdvm8JX72S1wClnJ0NOF\ns4rqQjs6HZAZZoD18U" +
       "Rmwy0HLl19+WUtwh4uKsqiE3Q6yfl4/Xd26EufnrhfD6OUVNgbpd7HSdhm\n" +
       "jMyLuywdycOiwHpW9QiS6xMWfPwCvIHVa/55/d5f76yDgftIpWq3q7qi4Wnb" +
       "nUJ7C/iLHBZXP732\nvkO/ZK9y2fTUBac/LZtv7dYpPk0+d/9wY8V3dqfDpL" +
       "KPNHCfr+hsnaJlUHL7wGvbMacyTk4K0IMe\nWLibVmkOpuWqqm/YXEUt8+12" +
       "GbZGvEroJm9T/5H49yH+UHCwIPxCU8xIm+BMrFkXU1i0wmjSzIZC\nJnY7l3" +
       "eeweFsIX9hBkPjFjNSaVrqsIKhE8YmwyoT9hwbj2dkqjxEKwPWPk0xqHPEjl" +
       "uKENeIpSCw\nzTnnLUUjiqLBhMPet3/JXp1Ne50fUqUTqWG45JkQcKEa7CH3" +
       "wRboWjGuUuD6E46fDAnTlBhStSTO\nbDHwmFtUEkX3HZU9D1+WuuPBsKPNtS" +
       "Y3Y6d5+uzNy25eq6eNpDqocjsEGn64/tRFT7yzvUF4As/W\ngBZ8PAOv/pTl" +
       "5LoXr3p/BmcTSiCPmGOKFkjDcHqRHYktpyk1b0PQFpxWpJev0/naXfse6npv" +
       "m7sN\nK0y0zKcWPQQ9WWjAs4v0WQ04RJcgs+mMria4xAkeD59944E7b2j5B9" +
       "j69aSKajQNQQUIyIz1gTAX\njHqvBdFKm2gAmlSbFjx7Xb0B+8UUK0WZiFvc" +
       "xeD/nXxRxSSrmyaoOkzz14Xd1/jYeOzmF2G3JsNG\nQCAL8Cu+t3HDMEWfTa" +
       "se6Ht21aG4ELABI7lFCHfOsiAyc7cqilsVzd+qcPsZS5Z8MOc87uVgGu0w\n" +
       "96lFOijVL9x72jkHb4IopIPUYADbmUGtjZNxaOIUvHFwb+tYriqMaTs9S1Yj" +
       "bi2dXgRJSPk+uEp5\nY0K4RhW4AtUKJIoURk6Cs/Nmw9lfCfOdyv5L0WJkdj" +
       "GyM2rFElVX2YVyuHp2hNIP8Ye/yNkhE2/m\nFyKSQjDESJW9CaSbOZYWyTUe" +
       "GcFGSTgLEW4McI98ci5b9PlbVOtGksY0Q/fov2JHbkcZmZBT4+xM\nNYM4Lo" +
       "cvIpsRcDdrH+eEAx6BkbKEYW6RpJ3syPwXyK2vJA/ZkIx2IXItgi+NOjiCay" +
       "RhHvM5KMUW\n8QmeoeucOgF35MsrOofC4wTJ6gLms/O5rJxZu9wm5NTItdwk" +
       "2V3hF5thFe6+Pu+/ApQfey/PqHhj\nBBM9Ks2Z67gBLPKoDRvIYVoRuRnBLS" +
       "BlGlWGaQwie0nfDhskgzcxjTbLMqyVip4Ee5Rah1Vw0yWn\nFKqW69ruMgw1" +
       "IHIbgh0QB/EB5WB7GJmeM5jzZEkOMz5YIQe4XQ4A4dWUHCa4yZJDg6/kbM54" +
       "3qwt\nmYLjWDNMLUtNepN6BZG7EdwDOyRbSvq3me9Cg+fuHnJtQFhqeM8Yhk" +
       "ay69uI3Idgt8saAxpJX8rI\n5ABrbuuXgwPiyuAruWbbGwSupJLPv70lILi3" +
       "0NoQ7PIW5c0Mwf2SsLvglFCkvCmJkjyabx6Lebwd\nJDwgCVtAFAMT7DEyVo" +
       "K2g2MUeuwrfipT3OM/xHJ+AJJ2rl+gBy0UXT0ZdYSvC7YKtsxXck4xAuG+\n" +
       "yyLcy3yP1iSLnsQQTWY0VP1xEne6V0J35CY14u+I/AjB04J6saFIDQ99z0+t" +
       "VRijaZMFWuzyWiB4\nrkT4nxC00Qjz/SdWD+fZa+ANFCIa1RfSPOq3g1Jw3K" +
       "sq2AoXdcUmIcqSAfcKLyM4wPBZPKei4nh+\ndQQuv3KMNFz5o6sBgC3D/xy+" +
       "5XDhtjyV6ETkIIJXEfxREk6GKUmHh/0LtBmHyGucAO4XB3TvyvMM\nKxVVTA" +
       "V0IQpTTeNTURx/UDNGuBNxH5biQvnT0sW++8MZxe8PA5aiJ4YKXSCK3QeXF+" +
       "wV7iNNKphI\niA2SMAtti3jUBrdmBTqDL/Hdxlcq9tBqxWx1bxomCT7cc+jl" +
       "lb9/9rlJG34TIeF2uCUYSrJd4Y88\nIWyEoN4eMrRk1lx6EZ9C3UgVwAZ8Dg" +
       "HMJsr5c4Zw61EGqOabbQepGoJRYuDK4iSiKQM4lXNgJrMe\nmQJH0LaurdO7" +
       "/yCyHjqudA+sfB8i/Qg2IFC8mwQiImpG4EldPSJeJF4wgkfghehHErsjSBcK" +
       "2RF4\nAeSRRK8IfAEkIjci+DICL347z393yQ0HhWi4gcL4YIWjN5OSqq2kB9" +
       "QUyC31ok85wGHwLbmn56r0\nWijaPMwM1Lj+LhSStgSfIIUiCMoCBNJcLDLG" +
       "u7EvMvaK+SOQw6OMMOrQ/1cC+TwiWxFsO7J1kMsR\nESE0glsLxdYIZAxMti" +
       "MiYuAAwR8cI5BBrQgydiK4M0AIhRE5xiHVUkQeRPAQgmMSz/HiowgeO2Zj\n" +
       "jB4zIrIXweNHO/ie4BhPeqEgIt9H8AMET3kBHiKem3+mUNiG4NlCERuC4yRc" +
       "+ewSjiRQQ/A7LzZD\nRIRWCF4pFHMh+IMXaSFSKGwaPZ5C5LUChHFBwhtuCN" +
       "XgfxCJTxZzQqW/ktCieridrFmxouTyC7p8\nRD4CECKbj84FkmYsViKoOkpW" +
       "JW9KSt605E1L3nSsEMa4NwUQinmeMys95Oqx6iEXIuK+uREv94/C\ne04v/k" +
       "7Pe+sqb5aBF2H89atk1sQ+watfRibmVsnrqme9uNNF8xx6KEi4Cov3I/jGqP" +
       "uD1F0fvznH\nJ2HUI+Oe0nutdlTv2xApFDtdgIgMFPzvwRApxQOleKAUD3w2" +
       "CWM8HuAEuF03erfrDp3RFL6CcYME\nGSO0nQAxwuYx4ANLrq7k6kquruTqjj" +
       "PCCeLqfA+SVxiZAY0W8HQrSp6u5NBIyaGVHFrJoZ24hDHu\n0ACEzyzgvGJH" +
       "47yKfw4Q+PiF/wVPoGaULxvCX0TArXnQ6RUkjDGnV/JtJd9W8m0l33acEca+" +
       "b/O/\np5S+bfnR+DZETuSLWckVlVxRyRWVXNFxRjgBXNGbBVzRspIrKnkcUv" +
       "I4JY9T8jjHGeEE8Dj+P9LE\nylUikUsgVRPPrzTXST+Gf9TvpB9zEwIhPp6R" +
       "Ju+FV3dG1zGvEH5VOn20xIw8qeTWy9+r+4ry/JXi\nQ9KmYP61Nj2TPnv3AT" +
       "r/orpEgSyA1cwwF2p0mIqPtDF5WVjkloJhFxbNuLSaZ4Txsm9FepbOP31B\n" +
       "7ZthUjZKDr9Gp7Kbsoyl+3L8QGvlEyf3m5mzKbnzaRyeemlkSP1pmGflEom8" +
       "8tJzBju1BtN31VjB\nifLTnconUA9bhJ+/NsFvOhH/+P9IbETQxFujvEQuFs" +
       "c+Sta5okTsvtb5kD8/d1qXpaZVJp/p7pjx\n4J8ef717gjhnkZB0bl5OUH8f" +
       "kZSUz7SWf788u9gIvPXzn5v92LXdrw44eZQinYyUDRtqEhcQucon\n1DnZ8b" +
       "AQ6Zabh7/T4dfvbF5/oc3L1yPusucyUmHzdLGFN89Lv5eb0ZQnsRSbtTfS/F" +
       "74qUnNPC9j\n2YBiy4RTgVSw+ZleAwlc+Txr5KpOgd/M4qsKxfLmiWWMDSPp" +
       "bIhgsreIMcrKLxBmBNavUT3Fhvim\nfkFkjor0g5zAbBHVsnnq7FiY8Z6F4V" +
       "mA0MS4NJH2UDWiMtEutz8FphvpEdPlY30KIn4N/j24TEoU\nfPMvkvFlix91" +
       "/urasglq4nf6fICk85U/F9I8UQ359ixQAztd6aRExTxyk3O1w0m9m5jz0oZ5" +
       "z5mN\nPxPC5KbHrcQ8WhlN82cE9OEVpkUHVS5BlRzWmXyuX4OTKJSclfGchV" +
       "6ZH8Stos9tPAcn9sHSDi5E\nN2RD/wEzr9Pq31kAAA==");
}
