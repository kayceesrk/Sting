package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client1 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1bb2wcRxWfO9u1nQTHtZOUNnGB1EmTCs5t04QmdpvU/0jc" +
       "i23VTlulou3e3eRu\nk73d9e6cY0NVlSJE4EMQgraigCIhUIWgHwJU5Y9UCA" +
       "hFAgkJPqCCxKfygUoghBACiSDxfvtnZve8\nd7UJLoe7kfLuzbyZ92bfvHm/" +
       "N3vnb/6JdbgOG3S56+qWeTYnlm3u5tD0ubmpOS7miX2iePXXI98y\nxa43si" +
       "yTZ51VXi1wxxVsR/6stqgN1YRuDI1ZhsGLgjQNLzlsX1OtnsjXzPx/GU9zR7" +
       "GiGyXB9uXD\n6UPepCE5fSg2HZZuq7MEahXO0lJgyzfy2c65lx4pf/6rWcZo" +
       "xnbbMpbLhiWCGf6YB/a8eir/hx++\nxx+zM2HMjKf1ieK1Q7sGfvXA77vbsO" +
       "Qu23J1PLVg786Hk3yPzAaS4SXbXrLJ1XvhrRxkOeUtd/CU\nWbVK+hldKxg8" +
       "r7vi2tY9d73yx4u9nkvaDeoRrDfiaYzBk7//rdWp/ptH2TM/f/zvt3pKM8UF" +
       "9jTL\neIvaprTkdfMcL0H9lv1zH5568sLuNnKGfb4dG0RDb2+yqWOjvKyv2F" +
       "O4cm+TWZFJR4wXrn5t9i8X\nMAmry9pLZHJP0zgyS0kGDzaZc5J4rUx+qVZr" +
       "pl7UhAzElw5+/PXnnx36W5a1Pca6uMGr3BQU47c+\n5rve0Mzy0JzQiufmHa" +
       "3IJ/wBw3m2uerrhBrBtqsg8APWi9M8u0FoTpnTTvbF1Dm6WR6Wz4vPvshn\n" +
       "83P0EC9yfZGvdAGm9yeq299E3UxNnKfjl6Cv+TbkLcv255yb+srpK1P/yPuB" +
       "W7BKy7DcFj6enbCg\nO5ovqOBoZrGStKJmsTiaOCt7mvXp7ji3KWxo54xlDC" +
       "gh6Wg0WbD+yBE7rrmVk5otd8ZmtNYb1UkJ\n5B2dv73ykx1P/rKNZSfZJsPS" +
       "SpNaUVjOCdYtKg53K5ZRWrKPHvOWsOV8F9Fe+p8lZdvl+j2F5Eat\nwI3Iak" +
       "+wrgpZGbNKPM/aDK0QDx0/GVFqYe/9+i2CtU88PDEd+tWuOWHY5jA4tzJss5" +
       "N3jIz8a/dh\nONMmi1O0pJ1NJmjdP31x76HffSLL2k/Qk+omn64BBPKsp8SL" +
       "hoY4HjM011vCDkqLZyiSprUqD9qb\nqlxUrJLsgdGOq5TXlM158phGyX+zz+" +
       "QgEexddGjUajz1d9J6d4r/8JgL9r5m4sDqDSO6qYv7pbmt\nYpWZSLAbo01P" +
       "HZTcK1XdD+YIyLBgXe45yjSC0nYo3qTEIPdJwT1gDoF8UMRyjhxxOjqi26TA" +
       "GTMs\nU8l/IVYPy4Jtq+sJPNMtHF6n93Uw4yATFIhFy16Won0igs+a66MpVh" +
       "Zi8zTxgddUMzDVLqJPdzi6\nBfWq/BMfKuuPdwTqdpR0V6sW9HJNE1yZkwau" +
       "CTZQfygpQmwKZOcUNV3PJ7Eeub2zoZZMBsxDIHOr\nE7DBZj5Cco34SDVXmm" +
       "bX1mq6NQXsgIjUDPUOCUrS0Cfb6nrW4Bb2aPQ8LOpUrJH+WccSVtEyximr\n" +
       "Qe1ojSpS7lAd0FAWxFdPAc1JnRslDJBmvHN8BqRMx8fg2iIf0wwlv0j7L6tN" +
       "fxkTjmM5xzWzRIm2\n/DC6LFrBzUnd8oHPSsf2gjFAqoJ1eAalscsU5HXGxq" +
       "h25UtCmumPd0gDljSQFeyWOiXwvtTQG2kF\nzun3hk2UyrRPM4vccfTIyfsN" +
       "GAFSIw/JkVL+Ms2X9hAQ4e5vjh3wTd7MMVwh5NQ3wXwE5KOhatS3\nUn6Uyv" +
       "aYag/ERql0gf7eSCvEI2XE4Spt/1M9Ashi0rOBLKmHUisDeUoKLiUuCSGllu" +
       "S35NY8ux7r\neDMueFoKlikUYwucs2pOkU9SWvTTVKT5X1ni5egmdngbIGX3" +
       "RgP6jIPQNUu5IPhmyVXkskgr2MU2\npyb3LjsvItc9qWKuWOGlmoGj3yP5YH" +
       "onTYc2eSL+CuYLIC/60g9Zmjzhme9GpZs1IXjVFrERX1Ij\nQC6lgrdFYDQS" +
       "7I/u2Fbaz3kLwE+lmh6p1b4RzYMycMIKgXJFyIZhU/TbUsGnwXwb5DuCbQmk" +
       "ODgy\nsth5Kvuljaqmm7mTRCiX4SPQ2+HS5VIdiWkw3wP5PsgPpOAmWpIEPM" +
       "xPGNMD5jVPQBUYDNLNYFfd\n25BZg2rx4xbAjy7Ob/zsqYkHP3XBv/WZqsKP" +
       "Vff+VRc+MHS6T9ztGdzPMndRad02Mz6u7i5gct6V\nJFhTx1Uwd4McALlH3Q" +
       "LA+BUviCqyt4JRVXRi9Q2iyuvV1N0gx5LKbZBRVWeD8ethkMmkQhnkuCqT\n" +
       "wUyBPAiSV0UvGG8/Z0DWo9Zjg2AeAXl0vWz8j+tJMAWQ4uoe0HeEX7KBVJJq" +
       "ORBZc7GLYPyaKyaI\nFmMgsojyQW0BxIkJMlkw6wzhR8E8A/IxkHWpHy6B+S" +
       "TIhXWz0bhGAeNtymeu1/jluI3PqdIDzHMg\nz4O8oAoKMApWvphUJoB8OalC" +
       "AGkReHznClZTGIBcVrUAGB/KQV5JwniQVxWyg0mC6cb4Dea1BEFP\nXPAjkB" +
       "8rrF2SmCqx6P8NUz8AJnzH5H9N9NZ4u3YgHmj+nk+96pZvY2Ivx7x33lJZn1" +
       "jD+3bBttd3\nhXenzKBU2YnmXpDb44LH0dwNclvccWt3TmsKGm7Zc2J1X2FS" +
       "NRppyXup8hSSQQaRmTm4StMNy7b7\nwKiKQwmOicgXF4mvnBzNdA3Ne40y0E" +
       "gURNtNIujh8YHS2p14GBy9zAi60molrVZA0mqFbUCUPyBB\nfmIDgDyRzMD1" +
       "APlCFA8jQNm30ABBU2hdCa0LMUR8m6FyIQpeGRVXKaqlqJai2oZFNU9wRQS/" +
       "tvHe\nHZ8wBS/jhXp4oZVQJ3+hsfGhrjUFKXak2JFiR4odrKWwI/K947hVKx" +
       "g8ATpkUkihY10FKUKkCJEi\nRIoQrCUQgkjmzwloMHI9aND8d9WxP9PwfrEa" +
       "62nwE/Escl/WMxdHkUTBRkGRFCxSsEjBIgUL1ipg\nEf0ZhQSL4esBCzDvyK" +
       "tDmtvT3J7m9jS3sxbM7erL8yNpbk9TOEtTeJrC0xTOWj6FZ15OKM8Ppyk8\n" +
       "zdQszdRppk4zNWuVTB37exS0e/4Nfv7/IoBMAAA="));
    
    public void run(int singleSession) throws Exception {
        final SJService c = SJService.create(invitation, "localhost", 1000);
        int[][] costsMap = new int[3][3];
        costsMap[0][0] = 0;
        costsMap[0][1] = 1;
        costsMap[0][2] = 3;
        costsMap[1][0] = 1;
        costsMap[1][1] = 0;
        costsMap[1][2] = 1;
        costsMap[2][0] = 3;
        costsMap[2][1] = 1;
        costsMap[2][2] = 0;
        c.participantName("client1");
        c.addParticipant("client2", "localhost", 20102);
        c.addParticipant("client3", "localhost", 20103);
        c.setCostsMap(costsMap);
        SJSocketGroup ps = null;
        try {
            ps = c.request();
            System.out.println("Client1 is connected to all participants");
            SJRuntime.pass(
              ("Hello, Client2 from Client1. I will send you an Integer and " +
               "a Double:"),
              "client2",
              ps);
            Thread.currentThread().sleep(500);
            SJRuntime.pass(new Integer(2011), "client2", ps);
            SJRuntime.pass("Hello, Client3 from Client1.", "client3", ps);
            String str = (String) SJRuntime.receive("client2", ps);
            System.out.println("Client1 received: " + str);
            System.out.println("Client1 Finished.");
            SJRuntime.pass(new Double(3.14), "client2", ps);
            SJRuntime.pass(new Double(1.11), "client2", ps);
            int i = 0;
            {
                SJRuntime.negotiateOutsync(false, ps);
                while (SJRuntime.outsync(i < 10, ps)) {
                    System.out.println("sending: " + i);
                    SJRuntime.pass(new Integer(i), "client2", ps);
                    SJRuntime.pass("Loop send to client3 from client1.",
                                   "client3", ps);
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
        Client1 a = new Client1();
        a.run(1);
    }
    
    public Client1() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1326075715000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO1cC3QU5RX+dzfvRPLgIfJ+RAULScHHUUERQiKJISAJiKEa" +
       "Jrt/koHZmWXm37BY\na33UgtrDqUfR1trS1ldVbAuW2mqP9UWx1dYWW6Q9B8" +
       "9p7Wm1VY/aB9ai7b3/P/PPzO5kCaW0ASbn\n7M2duf9//9e997sz2dxtb5FC" +
       "yyR1FrUs1dDX1LENKWpxanSvoXFm1bW3LFVMiyYaNMWyOkDQFV/9\nkbpie/" +
       "X1i6Ik0klqdGO+pipWR59ppHv7OvpUK2OSSSlD29CrGczWmKPj/KkH17+4sW" +
       "VsjFR2kkpV\nb2cKU+MNhs5ohnWSiiRNdlPTmp9I0EQnqdYpTbRTU1U09Spo" +
       "aOgwsKX26gpLm9RaRi1D68eGNVY6\nRU0+pnOzlVTEDd1iZjrODNNipKp1jd" +
       "Kv1KeZqtW3qhab00qKelSqJax15BoSbSWFPZrSCw1HtTqr\nqOca65vwPjQv" +
       "U2GaZo8Sp06XgrWqnmBkYnYPueLaS6ABdC1OUtZnyKEKdAVukBoxJU3Re+vb" +
       "manq\nvdC00EjDKIyMGVApNCpJKfG1Si/tYmR0drulQgStSvm2YBdGRmY345" +
       "rgzMZknZnntJYUVXx489ID\nk+DEYc4JGtdw/kXQaUJWp2W0h5pUj1PR8f10" +
       "3e3Nl6fHRQmBxiOzGos28099bHnr6z+cKNqMDWiz\nhNtiV/zgOePG75n/+9" +
       "IYTqMkZVgqmoJv5fxUl9qSOZkUWPcoqRGFdY7wqWU/uvzah+ifo6SkmRTF\n" +
       "DS2d1JtJKdUTDTZfDHyrqlNxd0lPj0VZMynQ+K0ig1/DdvSoGsXtKAQ+pbA+" +
       "zmdShJBi+ETgoxDx\nU4YETruhT1F1OOQGTaU6m1VnrWFkNqMWs+otM16fSp" +
       "uJNK2P2634Mc2uz+2UwZGGrY9EYJHjsh1O\nA+tcZGgJanbFH3jtJ1c3XnLT" +
       "JnF8aHL2HBk5WQxW5wxWZ6snkQjXOxwNU2zcfNNUNqDDZK7bM/6L\nu5Uvwz" +
       "HAdljqVZSvNrK+ACl0mp03njS43tgMnALG0hUfce3rY+56+cFdURILjCmt8m" +
       "aTYSYVDc3C\ncaIae7hsCVhTbbZNB4399s2LH937wv5prnUzUpvjdLk90Wmm" +
       "ZO+7acRpAoKSq/7ODxa9c1vheTuj\npAA8EWIRg61Gx56QPYbPeeY4gQjXEm" +
       "sl5T05Cy9jsEHrPQtGWoGkStgGnMWIrAnyGPb+DUUff+UH\n5bv4ip1wV+mJ" +
       "i+2UCeepds+/w6QU7u//wtLbtry1cVUM3DWVEmfOSFEq3a2p8Qx0Odnvbzi9" +
       "BJrN\nmzvmVG2eaX2XH3GpmkymmdKtUYjPiqYZ62mii/EAVe0JhjwGwU5UdE" +
       "MsA/Ps0kCRWGsq0g/mGRAt\n6kaPuP2O6Xe/gvEkxTdmFPodnynJ8Bu1EaTT" +
       "coR4PR4teIS7bIgAa8UCKqa3X9GyetMUvnKvuhn2\nRUYqdH3hrLy+0IS4Az" +
       "bDDAhArsmsvmXfpYtXXlYvQuKsvCrawJ0TXI/bf0uzPu/JkXv1KFpJkbVG\n" +
       "unwriVqMkWmtjkrb8vBScO0t7ULkwMKMQy/AHVi9+u/X7XhpSwUM3EmKVatJ" +
       "1RUNT9tqE94bABlZ\nKq56cvlX3v8pe5XbpusuOP1xmdxAt0LxePK5e/uri7" +
       "69NRklxZ2kisO+orMVipZGy+0E4LYa7Jut\n5CSf3A/CAnHmyHAwLttVPcNm" +
       "O2qBZ7cLsDXyJcI3eZvKf4mfj/CDhoMXDjQYyRTgiTnpYgqLVhhN\npDKRSA" +
       "q7ncs7T+B0srC/KIOhcYsZKU6Zar+C2ROmJ/0q42kSbzyckbHyEM20ztQkxb" +
       "zONjseKSLc\nI+aBwdZmnbc0jTo0DSYwe/feuTt0Nu41fkjFdrKGGZMbQgBF" +
       "NdhDDsMm+Fo+rdLguuI2VEZEaIr3\nqVoCZzYbdEzNa4mi+63F7Q9c1nv7vV" +
       "Hbm8tTPIyd5vqzOy+rdrmeNBJqj8rjEHj4wcpTZ+18c3OV\nQAI31oAXHFqB" +
       "e/+UBeTaF648MIGricRRR4MdimbIwHB6nh1pWEB71ZwNwVhwWp5enk7na3fu" +
       "vm/p\nu5ucbViYwsh8at5D0BNBA56dp89i4CHBBJtNpnU1zi1O6Hjg7Bv23X" +
       "F9/d8g1q8iJVSjSUgqwEAm\nrPJluhDUO0xIVBpFA/Ck8qTQ2eH4DcQvppi9" +
       "lImUxVkM/m7z/M5vYctonKr9NHd92H1JoLrpedQt\nSbP1YJgB+vLvcathpE" +
       "SftS1f73y65f1WYWjdRmKDMHJ7eamACZ2Rf0LdJsSivqAZ5TO0BYG9IHbX\n" +
       "qNZCmgKbgGPRNggMB3dUoDMjwz1uvkix+hYrqTly6sSfNdjywuLfPP3sqNW/" +
       "iJFoEynTDCXRpPBc\nipRCEkOtPkhVM6l5F/EpVKwvAVqFAQ6UjZTz5wphG5" +
       "Vuqnlm20xK+mCUBiMByURMU7pxKufATCY9\nOIaRgsYVjW3OPkK269hgHdpg" +
       "Xa4NRpvOmDv3wynn8fQBRmiBKYzN00Ep3XXXaefsvxHSu2ZYGWBV\nWxrDYS" +
       "sZhtihmDxnhzTGhoQSfF5ocyGiTDwRtrlZOSGFu+Ex1R0T8mCqwONluWDqUM" +
       "LISeAU7my4\nehXmO5b9hz7LyOR8YnvUornwnMAulMNVskGGFUjsvJdcHSpZ" +
       "J1VdiAy/ghSlxFoLYYPZEIbiMleM\npF8KzkLGQAIPNN4AIlt0eluU6mAoDZ" +
       "qhu/KfscEDFCMjsu7YO1PKIEH2692HzKeQXAOGGDdSG6Ro\nGvPgmWKJdAZn" +
       "5mBZG/D2rrmX9lA8rZCqzvMeQbYq4eGOsuH+G7a6UQnVUpLdam8aEgh3ODnA" +
       "QUbG\nZzuhk6gsh0uL74nvjjzemxwtIsO4BcnnBicgtfn2CIOpZ4/cy9yhyc" +
       "HDHXpoCsiZzJMAZG+InZw5\nezIi685hbAtZ6fWHftVSmSdfXAhRDdUuSKv4" +
       "egFAfUCZbV/DuvGS5/nYQA4zB5mtSL4K7qNRpZ82\nwLOglG+G85fpvphGo2" +
       "ka5iJFT0Cg7V2BtwyYwSlBt+WC75EbW4XMfUjuh8yZDygH2w5GnjWY/TpS\n" +
       "DjPcf0MO8A05ACTkY7KU4O5LDVWeK3tzhvNmjYleOKcl/dQ0VY/n/RqZR5B8" +
       "E3ZItpTyR5jnERgN\nwjn9cp+Dl/GeDZhMy65vIPMoku84qjEFlvJ5jIz2qe" +
       "YgtgBSFdRf5bly8MgdxKRu2P6nuwQk3wpa\nG5Lt7qLcmSHZKQVbA6eEJuVO" +
       "SVzJo3niaMzjDb/gMSnYAKbom2C7kTbjtAnCoghTnsv/yhS3ew+x\nkB+AlJ" +
       "3rNegeE01XT9TZxrcUtgq2zHNln2IMHhAdFdEO5nkfK1W0x/toIq2h6w+TvN" +
       "29GLqjNukR\nf0FmD5KXhfRiQ5EeHvmeV1quMEaTKeZrcbfbAsm+UPA/EWgD" +
       "CaZ7T6wSzrPDQOCHVE315GoPe+Og\nNBwnQ4BY4bCO2cTFtVRwMzJvInmL4R" +
       "9wuBQdR1oWWc9IjRwjqah63WIgEMvwl6230IKHR9cl2pB5\nDwk3y79Kwckw" +
       "JQl42D+gzTBkDnABZGA4oPN2pcr7eID5Pi6Iv0efzZvESWQW5MmxJQsXug8i" +
       "yPTy\n5wt7gMLdyKxFwnc/6ab0yIj0FYmbMVci46bEgak0EjdXHkwSjcRNVD" +
       "v9gk+6STMyIrlF8umgrBfJ\ndW7Oi8wNSD6D5EY3g0VmI5JNSI5G4kZqkfk8" +
       "kluP1hj/5+QQmS8huXtwCyQrkRH5F5KvBSVmSGQC\nRTYjIxIon8CbWSGRGZ" +
       "FAqIeQPOwTRKLIHGU8nofM95E8juSoJAN8m55C8vRRG2PghAMZHjeeP9LB\n" +
       "t/vHeNHNI5Dhzv4Skp+72QEyLkb8MgjzkewNgnskQwTrTlzBYFAeyZ9cYEdG" +
       "4DKSt4MAG8k7Lkwj\nE4S5A4MxMgcCBMP8gn8g+cDF2ozE1LZjFVNnIuO8MB" +
       "J//Tg03h4+EI/P/9LOfU8tX6343nTxF9ZS\nWQ07jJfljIzMvuU8CEU+IVUW" +
       "4+WVSLr8An4Pdy+yyr9xh785Q1Mw4JFtYYP7yxxko54r+ZDp7hQG\nA+74ke" +
       "Qghx4wbbsAGTfjcAWXIxMmFmFiESYWJ6bguEoszpR5xeLjIK8AEll+JLnDOi" +
       "8Ee7C5Zt0A\noB2ieS6ar/OBcIjOITofzgJDdA4FJy46c8FwZn/LiL92b9YZ" +
       "7cU/LDjvAiRkN504kD00BSEGhhgY\nYmCIgUNMcJxgoOdPzwuNdLdGAyCwMY" +
       "TAEOlIiHQh0oVIdwIKjnGkAxKdG4BqC48E1fJ/39/370L8\nm9S+OwP860IU" +
       "dyfKv2fjR8NAwfGChiHohaAXgl4IekNMcOyDnvebTRL0Go4E9JDhVyfao1yI" +
       "USFG\nhRgVYtQQExxXGOV+SWZBiFEhFJEQikIoCqHoWBEc+1AUeS/gcWl+CE" +
       "Uh4pAQcULECRFniAmOfcTx\n/eshXrdkeHNfhUb+39pThaCWIBVVR52SoZbp" +
       "KdKRVXeZ14zeuPLdis8qz10harnV+GurNurp5Nlb\n99HpF1XEA4r7ljIjNV" +
       "Oj/VSU08DCpFFRNxKGnZm3muJiXpTMrawZa583/fQZ5X+MkoIB6vNW2zeX\n" +
       "UZY2dU/9PmitHHbh3olZm5I9n+r+sZfG+tTno7zipijSmVN9299pjr80Z5np" +
       "nyg/wrF8ApWwRViB\nbip8xhHxw3+jsBpJjaivCyR2lzjbrIqybgHY7HKnS0" +
       "01qTL5x8RbJ9z7h0dfWzZCHJ8oIz41p5K3\nt48oJe5Ut4QRJucbgbd+7mOT" +
       "t12z7NVuu+RhbCsjMVVnmbxTx+vhzK4Azb9h1JiJ0xTW+eBK7neL\nxcQeEq" +
       "UKY48wUtBvqAlUGdvpsXR+Y1sm+E7sHrnt+DkdPqvsbV8VtO25bsYRfSojRR" +
       "avI59/Ybml\nznlpa3EeO2K170afGFXLqzUXdCuWLEPpqxGfWwLeV9mdz7NM" +
       "ruoU+Ew8xKo+CDwATB1jz2YiPEGM\n7Rpg5ReIKAPr16jey0uQx7YheSaT4/" +
       "m5x8pr1mEFU0cmqh+rRp0suQ/CTND8YveK+fGxBvaGwRZf\njr2E//UsS+j5" +
       "v98mavJm8p4t6tjBNT1u12jhhhhsjs/kmOMzGdjDYrsEOtaNHZ3tWna1/fiU" +
       "Paun\nPZuq/rEwE6cifjGWd0xrmrcCsIcvSpm0R+WnXSzqAaf4XF9lpDKrDj" +
       "sjJQ7L93m/aPlbXmkbW+LV\n77hR/CoT+TfXPCcyyGEAAA==");
}
