package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Alice {
    final private SJProtocol alice =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1Zb2wcRxWfO/viPwmOGycuIUkRbRISRM+UNoHEpml8sRO7" +
       "F9vUThGpaLq3N7nb\neG93uzvnnFGpgIIUQAoq0Fb8kSohUD7QqE1RKaoQqE" +
       "IQCQRSi6gKEh+gfGgRfEAIgUSQeL/9M7N7\nvrukLU6D2Eh+92bfzHtv3rx5" +
       "77ebJ/7Ccp7Ltnvc8wzbOpUXyw738hgG3Pz0PBcLxJ7QL7409rQl\ntr6SZZ" +
       "ki66nxWom7nmDDxVPakjZSF4Y5UrBNk+uCNI02XLaro1ZfFGhmwb+MrzmnVw" +
       "2zLNiuYrR8\nxF80IpePJJbD0k1NlkDt0ilyBbYCIw/3zJ/7SOUr38oyRis2" +
       "Oba5XDFtEa4I5hzc8eyx4qs/emcw\nZ0uLObO+1hP6pb1bt7148I99XXC517" +
       "E9A7sW7O3FaFEQkblQMtpwnIZDod6JaOUhy6toeduPWTW7\nbJw0tJLJi4Yn" +
       "Lq3fccszfz476Iek26Qngg3GIo052Pl7L69OPd88zj7583v/cYOvNKPfzx5k" +
       "Gd+p\njUpL0bAWeRnq1+2e/9j0fWdu7KJgOKe7cUA09d0dDrUwzivGijNFKH" +
       "d2WBVbtN987OK35/56Bovg\nXdZpkMkdHfPIKrcyuKfDmqPEaxWKS61Wtwxd" +
       "EzIRz+156OVHPz3y9yzruof1cpPXuCUox2+4Jwi9\nqVmVkXmh6YsLrqbziW" +
       "DCaJGtrQU6oUawTSoJgoT187TI1gjNrXA6yQ0Jda5hVUblfvG7wd93p/tz\n" +
       "F9e5scRXbh3Lh0I1my+rZso6TbdthZqM9BVqhhudz6Bo206gYXH6m8efn/5n" +
       "Mcjakl1exvquaG9O\nbHO7O3pVcjVLr17erU7ZON5SR/Y422B4h7hDiUNnZy" +
       "5jQhllR6PFgg3FLtkRzase1Rx5Ng4jx69T\ndyWU53p++/yPh+97oYtlJ1m/" +
       "aWvlSU0XtjvF+kTV5V7VNssN58AdvgvrTvcSHaS/LCnbJP33FVIs\ntRI3Y9" +
       "5Osd4qWSnYZV5kXaZWSiZPUI6ouLDt50jUPTM7OxdEeeencjf1CbZmaubuqY" +
       "WJKB2cuhsl\ncx4K8iuTOTv5nrGxf9+4D8nkkBeHyc0tHRZofT/52s69v/ts" +
       "lnVP0e4Ni8/U0RqKbKDMdVNDdhdM\nzfNdGKZieZISbkar8XDcX+OiapflEx" +
       "jNXaRqp2wuUBQ1aglrAyYPiWBvozxQ3vjqR8jfLeINXn7B\n3tVJHFpdM2ZY" +
       "hrhdmlsvrvyeCrax6YmvFKo+IBXeDmYfyH7Ber1FqkKCSnok7ldikDEpuA3M" +
       "HpC9\nIlGP5Izj8Rl9FqVUwbQtJf9l560kWra/lcSTMD59wuVNel8GUwA5RC" +
       "mq286yFO0Ssd6teUGnhWdR\n354hHtauiw9DU90ivrt9IlakmlUFtSBSNpR8" +
       "EKobLhueVisZlbomuDInDVwSbFvzdaU8cSid3WM0\n9PyYJJ7I452NtGQyYD" +
       "4McldCwLZ3CgVKbCwUarjSArvUxkJb01dDwG4VMRDQvL0QY0Y73Nj05HVs\n" +
       "kn00nsRLBqEv0j/n2sLWbfMQFSSoHa8TxOQuNfa2sjApBkoYThrcLGOCNDMK" +
       "pgRC/vaZXFviBc1U\n8rN0mhI+Bm5MuK7tHtGsMtXIyt14ZJMHm1s9lhs+KQ" +
       "M7CKYKYgiW8w1KYxcoM5uMFQiM8oaQZoaS\nD6SBRWkgK9g7mpQg+lLDYGwU" +
       "BmfInzZRrtA5zS5x1zVi1+U3YByQ+ylCcqaUn6f10h4SIjr9tYlb\n2e+vLO" +
       "CdQC59DcwSyOlINQCrlB8gHJ5Q7fefcYIj0D8YG0WtRBlxuaq1/1JbAHFb7Q" +
       "1EWT6vPANp\nSMHjLV1CSimXgpE8mgdXw4/XkoKPS8EypWLCwXm77up8kmpZ" +
       "UHRiw/+Kixfih5jzD0DKPhhP6JMu\nUtcq58Pkm6NQUchio/AUu9y6PLvsgo" +
       "i9v0kV83qVl+smrv6A5MPlPbQc2uSN+BuYR0AeDaSHbU3e\n8Mz349K1mhC8" +
       "5ojEjG+oGSBfTwVXRWC2E+yOn9h6Os8FG92a8JURA1jfiddBmThRW6daEbFR" +
       "2ujB\nWCr4PJgnQZ4SbF0oxcWRmYWM3yBt1DTDyh8lQrUMP6HenEevOupKzI" +
       "B5BuR7IM9KwfXkkmx4WN9i\nzgCY53wBwSYYJFC/tenzxpxJMPqIjeZHb8Kv" +
       "/OyBiTs/dyZ4k7MUOE8A8+DdlW5eyS41gp865t1M\nNguR9dxFMLeAvB/kVg" +
       "XVwQSAFERh4PXiCl/6fTSkhu3g9E9BfDcUXu5vJ7gt6dWBVvgZ5KAU3Awm\n" +
       "ArjB9ysFqpUMZKIVKgY5LAXbOm9evW1LKJgA4P5rt1S2uTOmT7z/+8Ar8UTG" +
       "81dSYQ+YX4O8lBTc\nC+YFkBeT0WkbgbdU0Db8j4gr+xxKFyE2kpFS+/YL1O" +
       "9B/nCFpneBmQKZBrlTCj4Exg/wiaTgDhH7\nBNIS7dJ7jmdqPoLb1k4UZs71" +
       "InzCkxOltfeBeRXEhxESnLKzYAJwmhDEUSuIRJtB96+BWAlBJgtm\nlbHOAT" +
       "APgHwCZFWA1uNgHgL5zKrZaA/mwPh96Atv1viFpI0vKowG5mGQL4F8WSEvMK" +
       "r/PtYKT4F8\ntRWUArlGcMTqIA+Q8wpsgAmwAsiFViAC5LsKOoBphQPaAwQw" +
       "z7UQDCQFPwD5YdDFm3q5bI5vpJeD\nUR+sWn7oAmnqzJf5xAXy+jozyC+u9d" +
       "4DRrWMq9xLwKjq/qe07Kdln6Vl//+l7Od0zbVNWfJH05Kf\nVnaWVva0sqeV" +
       "faXgf6qyJ6D8/jdb19Uns7fsIxvIqtT1tHyn5Tst32n5ZtdS+W4C5vvSAp7W" +
       "aZbW\n6bROp3WaXUt12v9PcPAD/wERoVSzVjEAAA=="));
    
    public void run(int singleSession) throws Exception {
        final SJService c = SJService.create(alice, "localhost", 1000);
        c.addParticipant("carol", "localhost", 8889);
        c.addParticipant("bob", "localhost", 20101);
        SJSocketGroup ps = null;
        try {
            ps = c.request(5);
            System.out.println("Alice is connected to all participants");
            SJRuntime.pass("Hello, Bob from Alice", "bob", ps);
            SJRuntime.pass("Hello, Carol from Alice", "carol", ps);
            String recvdBob = (String) SJRuntime.receive("bob", ps);
            System.out.println("Alice received from Bob: " + recvdBob);
            String recvdCarol = (String) SJRuntime.receive("carol", ps);
            System.out.println("Alice received from Carol: " + recvdCarol);
            {
                SJRuntime.negotiateNormalInwhile("bob", ps);
                while (SJRuntime.insync("bob", ps)) {
                    SJRuntime.pass("hi", "bob", ps);
                    {
                        String _sjbranch_$0 = SJRuntime.inlabel("bob", ps);
                        if (_sjbranch_$0.equals("INVITE")) {
                            {
                                System.out.println(
                                  "Bob received inbranch BRANCH1 from alice");
                                String a =
                                  (String) SJRuntime.receive("bob", ps);
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
        finally {
            SJRuntime.close(ps);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Alice a = new Alice();
        a.run(1);
    }
    
    public Alice() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1319085038000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ca5AUxbl37/2Qe/AQOB4Cp0ICe9GopR6KcNzJ4XKc3IF6" +
       "lh6zu317A7Mz40zv\n3UIRg49EooaUpfiKhoplQnxVBEtNVYyJpQajRhOSQp" +
       "Iq/WPKaOKzLJVKUPN93TM9j91bIAYlulTt\nd939dX/f193fa4b99v63SIVt" +
       "kZhNbVs19HUxtsGkNodGYh1NMjvWt7xXsWya6tAU2+4HxGBy7Sfq\nmp1NVy" +
       "6LksgAadaNxZqq2P3DlpFND/cPq3bOIseZhrYhrRnMoZhH48w5B0afv2Z5Sx" +
       "lpGCANqt7H\nFKYmOwyd0RwbIPUZmklQy16cStHUAGnSKU31UUtVNHUjTDR0" +
       "YGyraV1hWYvaq6htaCM4sdnOmtTi\nPN3BOKlPGrrNrGySGZbNSGN8nTKitG" +
       "WZqrXFVZu1x0nlkEq1lH0ZuZxE46RiSFPSMHFS3N1FG6fY\n1oXjML1WBTGt" +
       "ISVJ3SXl61U9xcjM8Aq549bzYAIsrcpQNmxIVuW6AgOkWYikKXq6rY9Zqp6G" +
       "qRVG\nFrgwMnVMojCp2lSS65U0HWRkcnher0DBrBp+LLiEkYnhaZwS3NnU0J" +
       "35bmtlZf3H1/Z+dBzcOMic\nokkN5a+ERTNCi1bRIWpRPUnFwv3Z2E3dF2Wn" +
       "RQmByRNDk8Wcxcc/ujr++q9nijktBeas5Lo4mDxw\n2rTpexb/raYMxag2DV" +
       "tFVQjsnN9qr4Npz5mg3ZMkRUTGXORvVv32os330n9GSXU3qUwaWjajd5Ma\n" +
       "qqc6nHYVtOOqTsXoyqEhm7JuUq7xoUqD9+E4hlSN4nFUQNtU2DBv50xCSBV8" +
       "IvA5j4h/NQgYKV8a\ns9cxMoNRm9lttpVsU/SU2paiGk1z5W7DCTmkM240Eo" +
       "EtTAubkwa6t8zQUtQaTO549dlNned9b4u4\nHFQoRwK4ayQc8wjHwFSTlEQi" +
       "nOp4VDpxKIstS9mAxpC7Ys/023Yrd8IRw1ZtdSPlO4mMliOERScX\n9RUdnq" +
       "V1Q0sBRRhMTtj8+tTb/3TP01FSVtBfxOVgl2FlFA2v3DWQZoddGAOa0hrW10" +
       "K83752xUN7\nn3t5rqe5jLTmGVT+SjSI2eFTt4wkTYHD8cjf8q9l795YccbD" +
       "UVIOVgZ+himgHWC0M8I8AobR7joZ\n3EtZnNQN5W28lsEBjfo2jLAeQaPQDL" +
       "iLCSEBuX/af1XlN156rO5pvmPXlTX4fF4fZcIwmrz777co\nhfGXb+29cdtb" +
       "11xcBqZomuLOGak0swnQmxwsOTZoSyheCtXmzV3tjVsX2I/wK65RM5ksUxIa" +
       "Bd+r\naJoxSlODjDufJp+j4/4FTqI+AX4KXN6gBoTEXs3ICKhnAU8Qmzzhpp" +
       "vn3fES+gqTH8wktCkuKcnx\ngdYIwrl5SOxPRw2e4G0brHu92ED9vL5Llq/d" +
       "Mpvv3E9uvtPJSYKeLZxS1Ba6MKaAzjADnIunMmuv\n23f+igsvaBPu7qSiJH" +
       "rAmFOcjrd+W7e+6PGJe/UoakmlvU4afJxEbcbI3LhL0tE87IpW3/I+gXJd\n" +
       "/vyDb8BjrG768IpdL26rB8YDpEq1u1Rd0fC27R5hvQXCQYjExsdX/2j/79kr" +
       "XDc9c0Hxp+Xy3dwa\nxWfJp+8daap8cHsmSqoGSCMP6YrO1ihaFjV3AIKy3e" +
       "EMxskxAXwwwIpo0i7dwbSwqfrYhg213Hfa\n5Tgb29XCNvmchk/Fv0/wg4qD" +
       "HeH2mzuMjAmxwjruXAqbVhhNmblIxMRlp/PFMzicJfQvyoA1HjEj\nVaalji" +
       "iYGZEKBT04nzeekRZ5f1ZWZ2qGYrrmaBx3EhFuDItAV1tDVy21IoZawUQo3r" +
       "134S6dTXuV\n30+Vk4NhIuR5DwiOGhwfj64WmFkxqlLXBpNOBIwIr5QcVrUU" +
       "SnYy0JhTVAnF8huq+nZckL7p7qhj\nyHUm92AneKbsyWW3rtYzRkodUrkLAu" +
       "M+0HD8SQ+/ubVRBAHPzYABHJyANz5lCdn83KUfzeBkIkmk\n0eF4ofnSJ5xY" +
       "5EQ6ltC0mncg6AZOKLLKt+hM7ZbdP+l9b4t7DEtNdMrHF70EPVWI4alF1qyA" +
       "NuSN\noK6ZrK4meeYgaOw49ap9N1/Z9gG4+YtJNaQVGaozUJAZFwcSWPDn/R" +
       "ZkKJ1iAhhRXUbQ7HdNBlwX\nU6w0ZSJXcTeDf3v4popp1iqapOoIzd8XLl/p" +
       "kOk7KJlufRT0MI9MJCRb8QOOG4YpKKxfftfAE8v3\nx4WWJYzUBqHhzt5M3+" +
       "bmFZUqYYHrGT64WMVUbUlBGuC4m1V7KTVBK+BitA0igINBKrCYkfE+Q1+m\n" +
       "2MMrFLNdyk+CKYODr6j66xNPTlr7xzIS7SK1mqGkuhSeSJEayGCoPQxZas5c" +
       "dA4XoX60GmAjejcg\nNlHKzwnCWSoJqvmk7SbVw8Clw0hBJlGmKQkU5TSQpH" +
       "VHAtLonpUre8Wprr2iYk4NJCrdPWu6+zvd\n6zezlquZMdTMWL5mRru+tnDh" +
       "x7PP4PkEcD0XxGopskCpefr2E057+TuQ73XDbiF49WTRScbJOAwm\nCj668b" +
       "zGiRHV+HDQ48WMWvH41+Ml6YRU7IZnUo8nJMZUgWfJOtGIIYaRY+DePWk4+W" +
       "GQt4X9l5bM\nyKxiaIdr5UJVV9nZkl0DO3S7ZGRCaIQTRVKmJHg2Nnj8B+2r" +
       "tteDS2HggF10rYdGkJWIU7ChIzBY\nwLnIGQP+GTU6qFCHZuge/oXiWwkEL7" +
       "6VwIhzPjUM8uYg3X3Y2ITgW6CiScPcIFFzmS/WKbbIclAy\nN871QBu5Nfm7" +
       "DiuebUhSZzCfUwqTErbvEhsfHHDITUqptpJJqOks5BUeO8ngACPTw+bp5i+r" +
       "oWvz\nMwmMyOvd4lIR2ce1CK4LIEhrsaNAl+o7Cq+bz4EcGIPDmKw/DwT5Jv" +
       "NF9PD2nGzL3eGE0MhhbJJc\n5FfiEdVWmS8BXAoOCckuyar4ogCi9Jg4RynG" +
       "JbDLc3acINm0Y+OHCO4AndeoMkI74LlO4rfCbcrU\nXYjRaVmGtUzRU+Aj02" +
       "twyAAJphQalhveLg+2ERs/RnAXpLycoWS2EzQzxMx5bSjZjA8OSAZ3SwaQ\n" +
       "XE8NEcHTlxQafT3ncMbzaZ2pNNzTyhFqWarPXP6CjXsQ3AsnJGdK/APM9ziL" +
       "CuHefl3AKmv5yg7M\njuXSN7DxcwQPuqQxp5X4RYxMDpDm8WcJpB9Iv9HXc0" +
       "OJx8Sinq/9t7cFBPcV2huCB7xNeZIh2CkR\n2wuKhCrliSR68moePRJyvBFE" +
       "PCQRG0AVAwL2GVkrSbvAlwmn4+v+T0Tc6b/ECn4BEne6X6GHLFRd\nPRVzlK" +
       "8XjgqOzNdzbrEMnvhcEtF+5ntvKkn0JYdpKquh6Y+TbWd5FSxHatIi3sfGCw" +
       "heFNhzDUVa\neOQXfmydwhjNmCww4w5vBoI/lxCfC0IbCzHPf2MNcJ/9BkZr" +
       "yK9UX4J1n98PSsVxwzr4Crfpqk1S\n9CUBHpVeR/AGw/9o4Vg0HKlZZJSRZs" +
       "kjo6h6bAUA8GX4x6FbYcOjjWcSPdh4G8E7CN6ViGNBJBnw\ncH2BOeOwwdX5" +
       "HUibkKH7uqTRn9ljqg5mlDASOfEni7OG8HW9S6piNzbWIViPwIt4LdgQ2SUC" +
       "L6Ft\nYIf4OM5TG687Vm78CYJPEXjJb+1YiFOCUuUKJcMINkrEAmy42ap4Le" +
       "NlyB4OweWFUlwEmyVievHN\ne4/KMq8LZNP8mVkSm1I8QQ88vPMsKjDinmek" +
       "ThKswu4xCMYFEZditwZBbfB0xjyBLxQx5vFvY4f2\nlg8MwdeTmuftG71NZA" +
       "qCqYfIei42rkRwFYKrJeIsbNyC4NYggqukyCkR3Fko2UQgk0KyFRsiKQwg\n" +
       "/NkiApnliaj7UwQ7AohIFBtHOMdYhI2HETyC4IgkONux8RiCXx0xHmMnUdh4" +
       "EsFTn5X5ziCPZ7zc\nCBvPIngOwfNexoMNL+79oVAeg2BPoRQGwVESv7+6iE" +
       "PJXBC85iUr2BC5BoJ/FEpCELzppR7YKJRH\njJ1gYOP9AohxQcQHCD4UiUMo" +
       "fVj6WdIHbHgvvAq+KEMQSgYO8ooMweElAwAiFUd7uLssEKVK4asU\nvg5ng6" +
       "XwVUJ85cNXRVKxDE2Gro5S6CpFKFKKUKUIVYpQ//eIL0eECjxaLfms8cl7a/" +
       "qFvWe97EjF\np1IYKoWhUhgqhaGjDPHlCEOhB6XFpUBUijekFG9K8aYUb44y" +
       "xJcj3vDvpWB7uXDqgdIg/r3TOU65\nG0Eoyt3cWjXb8n2jLFTMywuRr7nwvf" +
       "rvKk9dImpBmoNFfZ16NnPq9n103jn1yQI1pTXMMBdodISK\nUIcVcVFRsARs" +
       "FxSt5VnBv/zulXSV9S2ad+L8ur9HSfkYhaFNzuAqyrKW7qsegdnKYVeMzgwd" +
       "Slie\nppGW88uG1WeivNRLVIfllXQHF7UHa8JqraCg/ApbuAANcETV8JkDn9" +
       "lE/ON/EdmEoFkUdgKIim/h\nhEsZvcrDcJ1dr6VmVCa/aX/DjLtfe+jVVRPE" +
       "9Yna9Dl55eH+NaI+3a2tAg6zinHgs5/6+qz7L1/1\nSsIpvImqoLWqzgpXYb" +
       "qiY388c0qP+TexOnNJauKX0jgR0/tmY5SJSp7oKCPlI4aaQpLRb/s0nQ+M\n" +
       "5AqPRDPy2PFzInw6nWPvLHTs+WbGo/kcRipt/uMExTeWXz/Pa6rFfewqa30v" +
       "+stJrbxMuDyh2LII\nKvDDA/m/KxD4uQAuZ63c1RT4zDzIrj4seAGYNka/n4" +
       "sQrD2M/mCMnZ8lvAzsX6N6mte1R0cQXJ/L\ns/z8a+VVEVg/5+JE2a1qxOTv" +
       "OAAyv6oWGehCPs5rbGs41Krf6G2YvssijeD3AEUxaK7o3SKNTZzS\n1c4XCr" +
       "kiFlbH6/PU8foc1m3yynusWZwcNiznBxySs/esnfuk2fQ7oSTujyxUYRFRVt" +
       "P8hae+dqVp\n0SGV33WVKEM1uaQ/Y6QhVPzPSK3X4ee8Q8y9D3aFc7F9P1eJ" +
       "7bnIfwBj7b0uG0QAAA==");
}
