package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client6 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1afYwdVRW/83aX3W3rdum2hdJuBWxLa/CtVdtQdqFlv9hd" +
       "Xreru0VSImXee7fv\nTXfezHTmvu2uQWJEk6JJDSoQgYRoJIRgo9UghvgRYk" +
       "wTTUzgDwIm/oVRSfQPY4wm1sTzm497Z17n\nPdbWwrYOCadn5tx7zrnnnnt+" +
       "Z+7b7/6FdXgu2+ZxzzNs63heLDncy+Mx4GanZrmYI/Zo6dwbQz+w\nxJa3c0" +
       "wrsM4arxW56wm2sXBcX9AH6sIwB0Zs0+QlQZoGF122s6VWXxRoZsF/mq+5o1" +
       "Q1zLJgOwvR\n9AF/0oCcPpCYDksfarAEahePkyuwFRh5rHP2+U9XvvGdHGM0" +
       "Y4Njm0sV0xbhjGDMXdtfPlz4088/\nGIzZnDLmkK/1aOn83i39r9/1++42uN" +
       "zl2J6BVQt2fSGaFERkJpQMLjrOokOh3oFo5SHLq2h52w5b\nNbtsHDP0oskL" +
       "hifOr92++6U/n+71Q9Ju0hvBemORxhis/NZ3V6febxpmn//1A//Y6ivVSifY" +
       "w0zz\nnVqvtBQMa56XoX7NrtnPTD146uY2CoZzsh0bRENvabWpw7xipO3pNU" +
       "J3K5yWsC5YgqlblYFZ4RpW\nBYvY0UJnTOXt5pPnnpv56ynsDnzPOYte6yz7" +
       "FC9xY4E3eoTN3dNi2kHi9QoFrlarW0ZJFzJTn9/z\nyJtPfGHg7znWdj/r4i" +
       "avcUvQIdh6f2Jheml+ztVLfCwYMFhgq2uBTqgRbIPKkiCj/USWccLieuUS\n" +
       "8W+fv9TtLQ+UVb5wnZi7PqZD6WoVtknrJJ3BC8KmNTjY2qGCbTuBhvmpbx95" +
       "deqfhSCXi3Z5CfPb\nogU6Ma92tfSq6OpWqfrubrXK0eFUHbkjbJ3hjXKHok" +
       "gbZi5hQBnFSKfJgvXFjt6E7lUP6s6g9J+R\n49eqExTKOzp/++ovNj74WhvL" +
       "jbNVpq2Xx/WSsN1J1i2qLveqtlledPYf8F1Yc7KLaC/9nyNlG6T/\nvkKKpV" +
       "7kZszbSdZVJSsjdpkXWJupF5MnKyhSVHLYjS/cIFj72L1j01GQnbobZWseg/" +
       "MXZmtu/MND\nQ/++eR8SyCGLE+TS5hYT9O5fPrVj7+++lGPtk7RSw+LTdYBD" +
       "gfWUecnUccxHTN3zXeilcnmMkmta\nr/HweVWNi6pdlm9gtOMc1Ttlc44iph" +
       "MorA6YPCSCfYD2XHnjq8+Tv5vFRZ5uwW5qJQ6tXjNkWIa4\nU5pbK5ZfgwRb" +
       "3/DGVwpVe6XCO8HcBrJPsC5vnsqMoKIeiVcpMcigFHwCjE/2iETBkSOOxEd0" +
       "W5Q+\nI6ZtKflvWi8lAdr+UhJvwvh0C5c36H0TzDDICKVjyXaWpGiniKG37g" +
       "VYC88i5J4mHtaujT+GptpF\nfHUUre1NVQXnPlLWl3wRqttYNjy9VjQqdV1w" +
       "ZU4aOC9Yf+PRpDxxKJ3dw/To+TFJvJHbOx1p0TQw\nMyCfTAjYtlahQDmNhU" +
       "I9XmiBnW9ioanp91XAPi5iHUDjusP2M1r6+oY3/8Xq2X3x7F4wqDEj/TOu\n" +
       "LeySbY5SpYLa4Tp1n9wlSG8qC7Olp4jHcYObZQyQZgbBFEHI326T6wt8RDeV" +
       "/DRts+wsAzfGXNd2\nJ3SrTMWzci9e2eTBprTXcsHHZGB7wVRBDME6fIPS2F" +
       "lK2QZjI9Sn8kUhzfQlX0gD89JATrAbGpQg\n+lJDb+wpDE6fP2ysXKF9OrTA" +
       "XdeInaO3wDggJyhCcqSUn6H50h4SItr91YnjusqfOYLPBTn1HTAL\nICcj1e" +
       "hlpXw/tegJ1T4wDVNPAv29sacIY5QRl6si/C+1BBA3bW0gyvIZ5RnIohQ8m+" +
       "oSUkq5FDzJ\nrXn4cvjxTlLwWSlYolRMODhr190SH6ciF1Sj2OP/xMWz8U3s" +
       "8DdAym6LJ/QxF6lrlfNh8s1QqChk\nsadwF9vcuty73JyIfdpJFbOlKi/XTR" +
       "z9HsmH0ztpOrTJE/E3MI+DPBFI77Z1ecK1H8elq3UheM0R\niRHPqBEgT2eC" +
       "90RgNhPsiu/YWtrPORswTo2XEeu8XozXQZk4Ed5TrYjYKG1KwbNU8GUw3wP5" +
       "vmBr\nQikOjswsZPw6aaOmG1b+IBGqZfgn1Nvh0feOOhLTYF4C+RHIy1JwHb" +
       "kkAQ/zU8b0gHnFF1A/BYPU\n7W9puPmYMam/nrABfvQN/PavHhq759FTweec" +
       "pbr2RMcefN0jBqZB3wi7fYO3MG03tctth0ZH1fcI\nmFv9z4zQp45zYD4Ksh" +
       "vkY6qzBxP0ryCqZV4LRvXEqb00iGqWl9NFg6g+/0hScEB1zWCC7hZkNK3t\n" +
       "BRlXTS+YCZBJkCnVwoIpgBwEuYSWjm0DMwdy+BJVvc/dIZgHQI4ubx3sPjBB" +
       "AwaimoPBpEB2UOw0\nmKCDSgjirRWIbIkCiKqBWAmBlgNzmQF5P5iHQD4Hcl" +
       "m6gWfBPALyxctmo3nHAcYvll+5VONnkza+\nqhoJMI+BfA3k66o9AKNA4sk0" +
       "0Af5Zhreg6wQsPv/FSwH5kHOKGQHEwAzyNk0xAb5ocJpMGmg2xyN\nwbySIu" +
       "hJCn4C8jOFnIuKlVgpMeZKw8qPgIlugoKfei4BR/vFMq+g5Z1J4qbKv4uWyj" +
       "a1vvxKXIr7\nFxGJN9GHj7ZVKuzE440gNyUFQDNtC0h/MjpNI7AyBU335XGx" +
       "vJ8UqWOMPclvRxUQHHENOaPll2m6\naWt1BxjVRyjBARH7wSD1WsjVLc/U/a" +
       "uO/maiMKWuE+EbnhworeGoajgZmn8ysh4k60FAsh6EXRXY\n7Qt+KsLfA/0v" +
       "4UlL8AquByIUT8HzO64CPCeiXb8ygOkiEOtEAmjeYwQ6EccETe1gBhYZWGRg" +
       "cdWD\nRezadNSuF02eghBDGUJkQJABQQYEGRCkCK5wICCifSul6Mu6ezFFv/" +
       "XdmPp7Uf9uTD02+YM07Q8g\nf8SrJEykCq4wmMjQIEODDA0yNGArBQ3iv/9I" +
       "NLg9+wTISjjLSnhWwrMSzlZ8CdeOppTwfVkJzyo1\nyyp1VqmzSs1WSqWON9" +
       "uxP7uCpOc/MiCB9CE/AAA="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20102);
            ss.participantName("client2");
            ss.addParticipant("client1", "localhost", 20102);
            SJSocketGroup client1Socket = null;
            try {
                client1Socket = ss.accept("client1");
                String str =
                  (String) SJRuntime.receive("client1", client1Socket);
                Integer ii =
                  (Integer) SJRuntime.receive("client1", client1Socket);
                SJRuntime.pass("Hi, from client2", "client1", client1Socket);
                Double d = (Double) SJRuntime.receive("client1", client1Socket);
                Double d2 =
                  (Double) SJRuntime.receive("client1", client1Socket);
                System.out.println("Client2 received:\nString: " + str +
                                   "\nInteger: " + ii + "\nDouble1: " + d +
                                   "\nDouble2: " + d2);
                {
                    SJRuntime.negotiateNormalInwhile("client1", client1Socket);
                    while (SJRuntime.insync("client1", client1Socket)) {
                        Integer i =
                          (Integer) SJRuntime.receive("client1", client1Socket);
                        System.out.println("Received: " + i);
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", client1Socket);
                            if (_sjbranch_$0.equals("ODD")) {
                                {
                                    String str3 =
                                      (String)
                                        SJRuntime.receive("client1",
                                                          client1Socket);
                                    System.out.println(str3);
                                }
                            } else
                                      if (_sjbranch_$0.equals("EVEN")) {
                                          {
                                              String str4 =
                                                (String)
                                                  SJRuntime.receive(
                                                    "client1", client1Socket);
                                              System.out.println(
                                                "Client2 received: " + str4);
                                          }
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
                SJRuntime.close(client1Socket);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Client6 a = new Client6();
        a.run();
    }
    
    public Client6() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1327975369000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cbWwcxXXuzt828VcSkjjfMZC0ybnhIyI4EGLHJg4Xx8RO" +
       "CEbgrO/G5032dpfd\nOftCKeVDEKBqJAQBSkOjItqIL5UEAZVKaRHQUKDQpl" +
       "VIK8EfKgotn0JA1Aba92Z2Z3fvzpdAmhLT\ns3TPb/fNvDfz5n3t2vceepeU" +
       "2haJ2tS2VUPfHGVbTWpzaAxupnFmR3tX9yiWTRPtmmLbfUAYiG/6\nXN2wp/" +
       "66VWES6icNurFCUxW7b9gy0snhvmHVzlhktmloW5OawRyOOTzOmXd49KVtq5" +
       "sipLaf1Kp6\nL1OYGm83dEYzrJ/UpGhqkFr2ikSCJvpJvU5popdaqqKpV8JA" +
       "QwfBtprUFZa2qL2O2oY2ggMb7LRJ\nLS7TvRkjNXFDt5mVjjPDshmpi21WRp" +
       "SWNFO1lphqs9YYKRtSqZawryBXk3CMlA5pShIGTo65u2jh\nHFs68T4Mr1Jh" +
       "mdaQEqfulJItqp5gZFb2DLnj5gthAEwtT1E2bEhRJboCN0iDWJKm6MmWXmap" +
       "ehKG\nlhppkMLItDGZwqAKU4lvUZJ0gJEp2eN6BAlGVXK14BRGJmUP45zgzK" +
       "ZlnZnvtNaW1Xx2S8+ns+HE\nYc0JGtdw/WUwaWbWpHV0iFpUj1Mx8VA6envX" +
       "JenpYUJg8KSswWLMilOeWB9761ezxJimPGPWclsc\niB9eMn3G/hV/rYzgMi" +
       "pMw1bRFAI756fa41BaMyZY92TJEYlRl/jrdb+55JoH6D/CpKKLlMUNLZ3S\n" +
       "u0gl1RPtDl4OeEzVqbi7dmjIpqyLlGj8VpnBr0EdQ6pGUR2lgJsKG+Z4xiSE" +
       "VMAnBJ8LifipRMDI\nxDaFxYd7gafdrqlUZ6dH7c2MLGXUZnaLbcVbzLSVSN" +
       "OWQRwHxsAPKmGpI1RvyTc3gyInjIZCsNvp\n2Z6ngZmuMrQEtQbiu9944aqO" +
       "C2++SZwj2p6zWEZOFjKjrsyoYL+EhEKcbyNaqNDgCstStqLnZK7d\nP+MH+5" +
       "R74DxAL7Z6JeXbDo2WIIRJpxcMLO2eW3YBpoDVDMQnXvPWtLv/eP9zYRLJG1" +
       "xi8manYaUU\nDe3D9aYGR1w2BcyqOdu488l+75Y1jx548bX5npkz0pzjfbkz" +
       "0XvmZuvdMuI0AdHJY3/nP1d9cFvp\n0sfCpARcEoISU8CUwMNnZssIeFGrG5" +
       "FwL5EYqR7K2XgVAwWN+jaMsAZBnbANOIuJWQvkwezQ9WXf\nevXJ6uf4jt24" +
       "V+sLkL2UCS+q986/z6IU7r92V89tO97ddmkE/NY0xZkzUmamBzU1noEpJwcd" +
       "D5eX\nQLN5Z29r3fZF9uP8iCvVVCrNlEGNQqBWNM0YpYkBxiNVvS8q8mAEmq" +
       "gZhKAG5jmgASOxVzM0AuaZ\nJ2xEp0y8/Y4FO1/FwGJyxUxGB+QrJRl+ozmE" +
       "cH4OEa9noAVP9LYNoWCL2EDNgt7LVm+6aS7fuZ/d\nQuciIxl6vnBmQV/oxA" +
       "QENsMMiESeyWz63sGL1my8uEXExsUFWXSDOyc4H2/+ji59+VOTDuhhtJIy\n" +
       "e7N0+RgJ24yR+TGXpWN5eCmw3tW9guTmh4VH3oAnWL3qk2v3vrKjBgT3k3LV" +
       "7lR1RcPTtruF9+bJ\nHVksrnxq/Y8O/Y69zm3Tcxdc/vRMbqDboPg8+ewDI/" +
       "Vlj+xKhUl5P6nj+V/R2QZFS6Pl9kMGt9ud\nmzFyUoAezMYi9bTKcDA921V9" +
       "YrMdtcSn7RIcjXiF8E0+pvbf4udz/KDh4IXIEQ3tRsqExGLNvoDC\nphVGE2" +
       "YmFDJx2tl88kwO5wj7CzMQjSpmpNyERKFgGYV1yojKeL3EBzcy0iQP0UrrTE" +
       "1RLPAcs+OR\nIsQ9YjkYbHPWeUvTiKJpMJG89x1Ytldn09/gh1TuVG1YOnkh" +
       "BNKpBjrk+dgCXyvEVRrcQNzJmSER\nmiAnaQlc2enAY15BSxTTby3v3X1x8v" +
       "b7wo43V5s8jJ3q+bO3Lrt5vZ4yEuqQyuMQePjh2lMWP/bO\n9jqRCbxYA15w" +
       "ZAbe/alt5JoXL/90JmcTiiOPdicULZSB4bRCGmmjSTWfQsqYYiUpE+kbtlWA" +
       "h4/F\nOdqd+37S8+FNrlJWmhinCx3JOhqnUHdkrwCj0VkFpq0BHEpPMOJUWl" +
       "fj3AQFj91nXX/wjutaPobg\nfympoBpNQZUBFjPz0kANDFG+z4LKpUMMANeq" +
       "Tgmefa4jZSnB3Q/+7ub7OqWgqemJ3E3h3LU+Hh6v\nQjrq0kfBOnN0lHtKhR" +
       "YUMwxTcNiy+t7+p1cfignbGzQSW4XdOxs0fataUHBVgxZEpeEjL6uQAbbl\n" +
       "5QExvUG1V1ITtAino20VuR3cVIHJjDT63H+VYg+vUcxWuX4SrCYcemn5X55+" +
       "ZvKmP0RIuJNUaYaS\n6FR4jUUqobih9jCUsBlz+fl8CTWjWFrXYeADZpPk+j" +
       "lD0KUySDXfartIxTBIaTcSUGRENGUQl7IE\nVjL7/mmMlHRs6Oh2lQpVsGuK" +
       "UTTFaK4phju/sWzZZ3OX8rICJKyCJTQVmKBUPnf3qUteuwHKvi7Y\nGeSw7j" +
       "SGyRiZgDlFwcc9Xt44qaICHyi6vdRRJR4Zu71qnZDSffAc68mE+pgq8PxZLZ" +
       "AoUhg5Cc7Y\nWw1nn4T1NrEv6bqMzClEdqSWLVN1lZ0nxdWyow8w8GyUdYcz" +
       "RVaGZHgeIlcggNK5wt4CMYQ5CQ7J\nVR4ZAZOEMxFJIYBHRn80kSP6/SMqdT" +
       "CXds3QPfrLhbcSSF98K4E7jn4qGZTPQb4HEfk2gqvAHOOG\nuVWS5jNftlNs" +
       "UezgytxM1w04Sqv3XzqieNEhWS1lvgCUzUr4ucusMXjDYTc5odpKalBNpqG8" +
       "8MRJ\nAYcZmZHtim4Zsx4uba6TwB15vNtcLqL+uBnBLQECaS6kCgyfPlV4l7" +
       "kSyOExJIwp+islkDOYL71n\n79spxNytT8y68wV2Tzb6rXtEtVXmqw1XQqRC" +
       "tm1pFV8lQL4ek+ZYy4RBvOQ1PQ6QYloR+SGCneAM\nGlVGaDs890n6djhmWd" +
       "qLZXRYlmGtUvQEBM/kBrxlwAqm5rstN7xLKrYOkR8juBeqZC5QCtsDJpsl\n" +
       "zHkHKcU0Bm9IAfdJAVB8T8tigtqXHOp8V45yGvmwjkQSzmntCLUs1edHf0bk" +
       "fgQPgIbkSEl/mPke\nd9Eg3NOvDrhrFZ/ZjoWznPo2Ij9D8IjLGstdSV/OyJ" +
       "QAa56Y2qAGQf51vis3x3hCLOoF4X95W0Dw\nYL69IXjY25S3MgR7JGFX3iWh" +
       "SXlLElfyaJ44Hut4O0h4VBK2gikGFthrpK047YQgJ6KR7/K/ssQ9\n/kMs5Q" +
       "cgaWf7DXrIQtPVE1HH+HpAVaAy35VzihF4GHRZhPuY7yWsZNEbH6aJtIauP0" +
       "HizvRymI7c\npEd8hMjLCF4R1AsMRXp46Od+arXCGE2ZLDBipzcCwZ+KhP8J" +
       "QRuLsMB/YrVwnn0GpnEovFRf5fWg\nPw5Kw3HzPcQKF3XNJi6uJQOekN5C8D" +
       "bDv9pwKjqOtCwyykiDlJFSVD26BgDEMvzl8C214fnGc4lu\nRN5D8D6CDyTh" +
       "ZFiSTHg4P8+YCYhwc34f6ikU6L5JqfOX/FjD44b4O/PFfIhCQouh9o2sXbnS" +
       "e7hA\nhPJnBkdA6T5EVASbEWzxynRERDGKwKt/axHxCty8hTECTwNHUxIjGM" +
       "1XCSPw6lFuBKJURfCdfDUs\ngu96FSwi1yK4DsH1Xj2KyA0IbkRwDPUZaUbk" +
       "+wi2HyOrr7jUQ+ROBHcd3T7IRkRENYXgnnxlFgJZ\nDgkNiXIoQPDXSQhkfS" +
       "PyzU8R7A4QQmFEjnN2XY7IYwgeR3BcUvsuRJ5E8MvjJmPs8gGRZxA8e6zC\n" +
       "9wRlPO9VBYi8gOBFBC95uR4RL+L/Pl8GR7A/X/JGcIJkrv9fwtHkbARvemka" +
       "EZFlEfw9X/pF8I6X\ndBHJl0HHTq2IfJSHMCFI+BjBJ17mzHiozJWx8ZorFy" +
       "HivtYRf8E4hjw6gx3l+2P5AiTw2om/SJbM\nphZ+kxV4o83fKgTuuE8xoQ2S" +
       "YTleYjIKXRIkXI6XGGZC64PaGVMDJyZhzHPZwY7uL2VQMfqu5IOg\npxB08R" +
       "C+4g0NHaXoMUurcxHx6giPgIdTLBeOUXixXCiWC+OVMM7LBU6Ah+967+G7S2" +
       "c0ia8X3MIh\nTwnR8TUoIQCEek6MXPglkuQVgdxWTHrFpPdFNlhMekVCMen5" +
       "3jivNNKDGs2T6VYWM10xoZFiQism\ntGJC+/oSxnlCAxB6M0/yaj+W5FX49a" +
       "j3/7789ah3OcY/GIbPQMDTWDDd5SWMs3RXzGrFrFbMasWs\ndoIRxn9W8/8p" +
       "U2a1tmPJaoh8nR/JiqmomIqKqaiYik4wwvhPRaGdeVLRimIqKmYcUsw4xYxT" +
       "zDgn\nGGH8Zxz/w4/vPzqRslp8lT7QsoL/E/w8pw0LQSjasLg9VGzL902mrI" +
       "5UvJvWto0f1tyoPHuZ6EbQ\nEGw206GnU2ftOkgXnF8Tz9PtqJIZ5iKNjlDx" +
       "nSPs1BIWjTRA7KKC7SXW8G9je61GIr3LF5y2sPpv\nYVIyRsOieufmOsrSlu" +
       "7rXwCjlS/cyWhWllKy11M/0nRRZFh9PsxbkIiuJTl9yYKTWoO9Sqqs4EL5\n" +
       "ETbxBdQS0dWqAT5TiPjhv5FYj6BBNBwCEP5cnO0YLXbyEt12OXjdyJymU/wv" +
       "nh2ZODXx60bIP1Ll\nfGctt4lMj6WmVCa/P37rzPvefPSNdROFDYgubfNyGq" +
       "X554hObW7PEJAwp5AEPvrZb8556Op1rw86\nLSQiEUZKRgw1gZuLTApadfZF" +
       "pFwqFj+nwafTUWxnPsXmOhLP3vMYKbN5D73Cis1t88a7eQll7Y00\nfxj+xe" +
       "Rm3qCqZFCxZaONQH+83PZ3ga52fJ1VcldT4TPrCLv6JK8BYJkYac6ECHa9iZ" +
       "w6xs7PFXEE\n9q9RPcmGuVIbRfOOyGSwE1gtovMyOa6ea2X8e/nYw8Wlif5P" +
       "qhGV3QeBmNveCQVUiOVyWV/S/H3t\npyJn4P+MyzYBwT//i65EmYJHjTx496" +
       "9Ik/PNNW6NOTYZ8ikncAf/F99pAoedc6Zku4HTeDA+d/+m\n+c+Y9b8VVuM2" +
       "ByzHRhZpTfP3QPLhZaZFh1R++OWiI5LJ19rGSG1WJzpGKlyU63mFGNnBe43h" +
       "SLzq\n5DayNBP6D5xqs4bTUgAA");
}
