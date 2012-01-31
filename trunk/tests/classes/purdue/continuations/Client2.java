package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client2 {
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
            ss = SJServerSocketImpl.create(invitation, 20102,
                                           SJParticipantInfo.TYPE_SERVER);
            ss.participantName("client2");
            ss.addParticipant("client3", "localhost", 20103,
                              SJParticipantInfo.TYPE_PC);
            ss.addParticipant("client1", "localhost", 20101,
                              SJParticipantInfo.TYPE_MOBILE);
            SJSocketGroup client1Socket = null;
            try {
                client1Socket = ss.accept("client1");
                System.out.println(
                  "Client2 accepted connection request from Client1");
                String str =
                  (String) SJRuntime.receive("client1", client1Socket);
                System.out.println("String: " + str);
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
                System.out.println("Receiving continuation object");
            }
            catch (Exception ex) {
                System.out.println("client1Socket Exception: " + ex);
                ex.printStackTrace();
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
        Client2 a = new Client2();
        a.run();
    }
    
    public Client2() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1327944838000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cbWwcxXXuzt828VcSkjjfMZC0ybkJHyI4EBzHJg4Xx8RO" +
       "CEbgrO/G5032dpfd\nOftCKeVDEKBqJAQBSkOjItqIL5UEAZVKaRHQUKDQpl" +
       "VIK8EfKgotn0JA1Aba92Z2Z3fvzpdAmhLT\ns3TPM/tm3pt587527/Y99C4p" +
       "tS0Staltq4a+Ocq2mtTm0BjcTOPMjvau7lEsmybaNcW2+wAxEN/0\nubphT/" +
       "11q8Ik1E8adKNNUxW7b9gy0snhvmHVzlhktmloW5OawRyKOTTOmXd49KVtq5" +
       "sipLaf1Kp6\nL1OYGm83dEYzrJ/UpGhqkFp2WyJBE/2kXqc00UstVdHUK2Gg" +
       "oQNjW03qCktb1F5HbUMbwYENdtqk\nFufpXoyRmrih28xKx5lh2YzUxTYrI0" +
       "pLmqlaS0y1WWuMlA2pVEvYV5CrSThGSoc0JQkDJ8fcXbRw\nii2deB2GV6mw" +
       "TGtIiVN3SskWVU8wMit7htxx84UwAKaWpygbNiSrEl2BC6RBLElT9GRLL7NU" +
       "PQlD\nS400cGFk2phEYVCFqcS3KEk6wMiU7HE9AgWjKrlYcAojk7KHcUpwZt" +
       "Oyzsx3WmvLaj67pefT2XDi\nsOYEjWu4/jKYNDNr0jo6RC2qx6mYeCgdvb3r" +
       "kvT0MCEweFLWYDGm7ZQn1sfe+tUsMaYpz5i1XBcH\n4ofPmj5jf9tfKyO4jA" +
       "rTsFVUhcDO+an2OJjWjAnaPVlSRGTURf563W8uueYB+o8wqegiZXFDS6f0\n" +
       "LlJJ9US70y6HdkzVqbi6dmjIpqyLlGj8UpnB+yCOIVWjKI5SaJsKG+btjEkI" +
       "KYdPCD7rifirQsBI\nVbumUp0tidqbGVnCqM3sFtuKt5hpK5GmLaCtTNXTXM" +
       "0HaMY0LOgmW7xJGeQwYTQUgs1NzzY0DbRy\nlaElqDUQ3/3GC1d1XHjzTeLY" +
       "UNWctTGYx5lF/czwyDkPEgpx4o2olUJqbZalbEVryVy7f8YP9in3\nwBmALG" +
       "z1Ssq3GhotQQiTlhR0Ju2eKXZBSwFNGYhPvOataXf/8f7nwiSS16HE5MVOw0" +
       "opGuqEa0EN\nDrtsDKhSc7ZC5+P93i1rHj3w4mvzPdVmpDnH4nJnosXMzRa+" +
       "ZcRpAjySR/7Of6764LbSpY+FSQmY\nIUpbAfUBq56ZzSNgOa2uF8K9RGKkei" +
       "hn41UMBDTq2zDCGgR1QkHgLCZmLZA7sEPXl33r1Sern+M7\ndn1drc8p9lIm" +
       "LKfeO/8+i1K4/tpdPbfteHfbpRGwVdMUZ85ImZke1NR4BqacHDQ2XF4C1ead" +
       "va11\n2xfZj/MjrlRTqTRTBjUKzlnRNGOUJgYY9071Pk/IHRBIomYQHBmYwI" +
       "AGhMRezdAIqGceVxGdMvH2\nOxbsfBWdickFMxmNjq+UZPiF5hDC+TlI7M9A" +
       "DZ7obRvMf4vYQM2C3stWb7ppLt+5n9xCp5ORBD1b\nOKOgLXRi0AGdYQZ4H0" +
       "9lNn3v4EVrNl7cIvzh4oIkusGmE5yON39Hl778qUkH9DBqSZm9Wdp9jIRt\n" +
       "xsj8mEvS0Tzsilbv6l6BcmPCwiNvwGOsXvXJtXtf2VEDjPtJuWp3qrqi4Wnb" +
       "3cJ688SLLBJXPrX+\nR4d+x17nuumZCy5/eibX221QfJZ89oGR+rJHdqXCpL" +
       "yf1PGYr+hsg6KlUXP7IWrb7c7FGDkpgA9G\nYBFuWqU7mJ5tqj622YZa4pN2" +
       "CY7GdoWwTT6m9t/i73P8oOJgR8SFhnYjZUIwsWZfQGHTCqMJMxMK\nmTjtbD" +
       "55JodzhP6FGbBGETNSblrqiIKpE+YmIyoT/hwHNzLSJA/RSoO3T1FM6hy145" +
       "4ixC1iOShs\nc9Z5S9WIomowEbD3HVi2V2fT3+CHVO5kapgueS4EQqgGMuQx" +
       "2AJbK0RVKtxA3ImTIeGa4sOqlsCV\nLQEa8wpqoph+a3nv7ouTt98Xdqy52u" +
       "Ru7FTPnr112c3r9ZSRUIdU7ofAwg/XnrL4sXe214lI4Pka\nsIIjE/CuT11B" +
       "rnnx8k9ncjKhONJod1zRQukYTiskkRU0qeYTSBlTrCRlIobDtgrQ8JE4R7tz" +
       "3096\nPrzJFcpKE/10oSNZR+NUHaHZK0BvdGaBaWugDekmKHEqratxroKCxu" +
       "4zrz94x3UtH4Pzv5RUUI2m\nIMsAjZl5aSDvBS/fZ0H60iEGgGlVpwTNPteQ" +
       "soTg7gf/d/N9nVJQ1fRE7qZw7lofDY9WIRl16aOg\nnTkyyj2lQguKGYYpKG" +
       "xZfW//06sPxYTuDRqJrULvnQ2avlUtKLiqQQu80vCRl1VIAVfkpQE+vUG1\n" +
       "V1ITpAino20VsR3MVIHJjDT6zH+VYg+vUcxWuX4SzCYcfGn5X55+ZvKmP0RI" +
       "uJNUaYaS6FR4jkUq\nIbmh9jDksRlz+fl8CTWjFQDr0PEBsUly/ZwgyFIZpJ" +
       "pvtV2kYhi4tBsJSDIimjKISzkLVjL7/mmM\nlHRs6Oh2hQqpsKuKUVTFaK4q" +
       "hju/sWzZZ3OX8rQCOKyCJTQVmKBUPnf3qWe9dgOkfV2wM4hh3Wl0\nkzEyAW" +
       "OKgrd4PL1xQkUF3kR0e6GjStwmdnspOyGl++De1eMJ+TFV4J6zWjSiiGHkJD" +
       "hjbzWcfBLW\n28S+pOkyMqcQ2uFatkzVVXaeZFfLjt7BMDIx6woniqQMSfA8" +
       "bFyBAFLnCnsL+BDmBDhEV3loBEwi\nzsBGCgHcJvq9iRzR7x9RqYO6tGuG7u" +
       "FfLryVQPjiWwlcceRTySB9DtI9iI1vI7gK1DFumFslaj7z\nRTvFFskOrsyN" +
       "dN3QRm71/q7DiicdktRS5nNA2aSEnbvEGoMXHHKTE6qtpAbVJNwiUo+dZHCY" +
       "kRnZ\npuimMeuha3OZBK7I493mUhH5x80IbgkgSHMhUaD79InC6+ZyIIfH4D" +
       "Am668UQU5nvvCevW8nEXO3\nPjHryhfYPdno1+4R1VaZLzdcCZ4Kya5Iq/g8" +
       "AeL1mDhHWyYMYpfn9DhAsmnFxg8R7ARj0KgyQtvh\nvk/it8Mxy9ReLKPDsg" +
       "xrlaInwHkmN+AlA1YwNd9lueFdUrB12PgxgnshS+YMJbM9oLJZzJznjpJN\n" +
       "Y/CCZHCfZADJ97QsIih9SaHO13OE08iHdSSScE5rR6hlqT47+jM27kfwAEhI" +
       "jpT4h5nvdhcVwj39\n6oC5VvGZ7Zg4y6lvY+NnCB5xSWO6K/HLGZkSIM0D0w" +
       "rIQZB+na/nxhiPiUU9J/wvbwsIHsy3NwQP\ne5vyVoZgj0TsyrskVClvSaIn" +
       "j+aJ47GOt4OIRyViK6hiYIG9RtqK005wcsIb+br/lSXu8R9iKT8A\niTvbr9" +
       "BDFqqunog6ytcDogKR+XrOKUbgZtAlEe5jvgevkkRvfJgm0hqa/gTZdqaXw3" +
       "SkJi3iI2y8\njOAVgb3AUKSFh37ux1YrjNGUyQIjdnojEPypiPifILSxEAv8" +
       "J1YL59lnYBiHxEv1ZV4P+v2gVBw3\n3oOvcJuu2sRFXxLgAektBG8z/KaGY9" +
       "FwpGaRUUYaJI+UourRNQDAl+E/h26pDfc3nkl0Y+M9BO8j\n+EAiToYlyYCH" +
       "8/OMmYANrs7vQz6FDN0nKXX+lB9zeNwQf2a+mA9RSGgx5L6RtStXejcX2KD8" +
       "nsFh\nULoPGyqCzQi2eGk6NkQyisDLf2ux4SW4eRNjBJ4EjiYlRjCaLxNG4O" +
       "WjXAlEqorgO/lyWATf9TJY\nbFyL4DoE13v5KDZuQHAjgmPIz0gzNr6PYPsx" +
       "kvqKUz1s3IngrqPbB9mIDZFNIbgnX5qFQKZDQkIi\nHQog/HkSApnfiHjzUw" +
       "S7A4hQGBvHOboux8ZjCB5HcFxC+y5sPIngl8eNx9jpAzaeQfDssTLfE+Tx\n" +
       "vJcVYOMFBC8ieMmL9djwPP7v80VwBPvzBW8EJ0jk+v9FHE3MRvCmF6axIaIs" +
       "gr/nC78I3vGCLjby\nRdCxQys2PsqDmBBEfIzgEy9yZrymjJWx8RorF2HDfa" +
       "wjvsE4hjg6gx3l82P5ACTw2Ik/SJbEphZ+\nkhV4os2fKgSuuHcxoQ2SYDl2" +
       "MRiFLgkiLscuupnQ+qB0xpTAiYkY81x2sKP7pgwyRl9P3gh6AkET\nD+Ej3t" +
       "DQUbIeM7U6FxteHuEh8HCK6cIxMi+mC8V0Ybwixnm6wBFw813v3Xx36Ywm8f" +
       "GCmzjkSSE6\nvgYpBIBQz4kRC79EkLwiENuKQa8Y9L7IBotBr4goBj3fE+eV" +
       "RnpQo3ki3cpipCsGNFIMaMWAVgxo\nX1/EOA9oAEJv5gle7ccSvAo/HvV+78" +
       "sfj3rdMX5gGD4dAQ9jwXCXFzHOwl0xqhWjWjGqFaPaCYYY\n/1HN/1WmjGor" +
       "jiWqYePrfEtWDEXFUFQMRcVQdIIhxn8oCu3ME4raiqGoGHFIMeIUI04x4pxg" +
       "iPEf\ncfw3P75fdCJmtXiVPlCygv8Ifp5ThoUgFGVY3BoqtuV7kymrChWvoL" +
       "Vt44c1NyrPXiaqETQEi810\n6OnUmbsO0gXn18TzlDyqZIa5SKMjVLxzhJVa" +
       "wqKQBrBdVLC8xBr+NrZXaiTSu3zBaQur/xYmJWMU\nLKp3Lq6jLG3pvvoFMF" +
       "r5wpWMZmUJJXs99SNNF0WG1efDvASJqFqSU4ssOKk1WKukygoulB9hE19A\n" +
       "LYgIX71vgM80Iv74f0TWI2gQBYcAhD8XZztGiZ28SLdcDvYbmVN0in/j2ZGJ" +
       "UxNfN0L6kSrnnbXc\nIjI9lppSmXx//NaZ97356BvrJgodEJXZ5uUUR/PPEd" +
       "XZ3JohwGFOIQ589LPfnPPQ1eteH3RKSEQi\njJSMGGoCNxeZFNTq7E6kXAoW" +
       "P6fBp8cRbE8+weYaEo/e8xgps3ndvMKCzS3txqt5CWHtjTR/GP7F\n5GZeoK" +
       "pkULFloY1ATbzckneBSnZ8nVVyV1PhM+sIu/okrwJgmhhpzoQIVr2JnDrGzs" +
       "8VfgT2r1E9\nyYa5UBtF8Y7IZNATWC0252VyTD1Xy/h7+VjDxcWJ+k+qEZUV" +
       "BwGZW94JGVSI5XJeX1L9feWnIqfj\nb8ZlmYDg1/+iKlGm4FEjDV79K9LkvL" +
       "nGtTFHJ0M+4QSu4G/xnSJwWDlnSrYZOMUG43P3b5r/jFn/\nW6E1bkHAcixk" +
       "kdY0fw0kX7vMtOiQyg+/XFREMvlaV4DI85WjY7xKk9fnEm8Tczp41TGcg71O" +
       "ri1L\nM6H/AG71PCfRUgAA");
}
