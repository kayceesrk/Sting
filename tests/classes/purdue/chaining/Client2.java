package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

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
            ss = SJServerSocketImpl.create(invitation, 20102);
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
            ss.participantName("client2");
            ss.addParticipant("client1", "localhost", 20101);
            ss.addParticipant("client3", "localhost", 20103);
            SJSocketGroup sockets = null;
            try {
                sockets = ss.accept("client1");
                System.out.println("Client2 is connected");
                ss.setCostsMap(costsMap);
                String str = (String) SJRuntime.receive("client1", sockets);
                System.out.println("String: " + str);
                Integer ii = (Integer) SJRuntime.receive("client1", sockets);
                System.out.println("Client 2 received from client1: " + ii);
                SJRuntime.pass("Hi client1, from client2", "client1", sockets);
                Double d = (Double) SJRuntime.receive("client1", sockets);
                System.out.println("Client 2 recived:" + d);
                System.out.println("Client2 Finished.");
                Double d2 = (Double) SJRuntime.receive("client1", sockets);
                System.out.println("Client2 received:\nString: " + str +
                                   "\nInteger: " + ii + "\nDouble1: " + d +
                                   "\nDouble2: " + d2);
                {
                    SJRuntime.negotiateNormalInwhile("client1", sockets);
                    while (SJRuntime.insync("client1", sockets)) {
                        Integer i =
                          (Integer) SJRuntime.receive("client1", sockets);
                        System.out.println("Received: " + i);
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", sockets);
                            if (_sjbranch_$0.equals("ODD")) {
                                {
                                    String str3 =
                                      (String)
                                        SJRuntime.receive("client1", sockets);
                                    System.out.println(str3);
                                }
                            } else
                                      if (_sjbranch_$0.equals("EVEN")) {
                                          {
                                              String str4 =
                                                (String)
                                                  SJRuntime.receive("client1",
                                                                    sockets);
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
                SJRuntime.close(sockets);
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
    final public static long jlc$SourceLastModified$jl = 1327975851000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cbWwcxXXuzt828VcSkjjfMZC0ybkJHyI4EBLHJg4Xx9hO" +
       "CEbgrO/G5032dpfd\nOftCKeVDEKBqJAQBSkOjItqIL5UEAZVKaRHQUKDQpl" +
       "VIK8EfKgotn0JA1Aba92Z2Z3fvzpdAmhLT\ns3TPb/fNvDfz5n3t2vceepeU" +
       "2haJ2tS2VUPfHGVbTWpzaAxupnFmR3vXdCuWTRNtmmLbfUAYiG/6\nXN2wp/" +
       "661WES6icNurFCUxW7b9gy0snhvmHVzlhktmloW5OawRyOOTzOmXd49KVta5" +
       "oipLaf1Kp6\nL1OYGm8zdEYzrJ/UpGhqkFr2ikSCJvpJvU5popdaqqKpV8JA" +
       "QwfBtprUFZa2qN1DbUMbwYENdtqk\nFpfp3oyRmrih28xKx5lh2YzUxTYrI0" +
       "pLmqlaS0y1WWuMlA2pVEvYV5CrSThGSoc0JQkDJ8fcXbRw\nji0deB+GV6mw" +
       "TGtIiVN3SskWVU8wMit7htxx84UwAKaWpygbNqSoEl2BG6RBLElT9GRLL7NU" +
       "PQlD\nS400SGFk2phMYVCFqcS3KEk6wMiU7HHdggSjKrlacAojk7KHcU5wZt" +
       "Oyzsx3WuvKaj67pfvT2XDi\nsOYEjWu4/jKYNDNrUg8dohbV41RMPJSO3t55" +
       "SXp6mBAYPClrsBiz4pQn1sfe+tUsMaYpz5h13BYH\n4ofPmj5j/4q/VkZwGR" +
       "WmYatoCoGd81PtdiitGROse7LkiMSoS/x1z28uueYB+o8wqegkZXFDS6f0\n" +
       "TlJJ9USbg5cDHlN1Ku6uGxqyKeskJRq/VWbwa1DHkKpRVEcp4KbChjmeMQkh" +
       "5fAJwecyIn4qEcBp\ntw0rqg6H3KapVGdLovZmRpYwajO7xbbiLWbaSqRpS9" +
       "wZxY9pSUvupAxKmjAaCsEmp2c7nAbWudrQ\nEtQaiO9+44Wr2i+8+SZxfGhy" +
       "zhoZOVkIi7rCog57Egpxvo1omEJxKyxL2YoOk7l2/4wf7FPugWMA\nddjqlZ" +
       "TvNjRaghAmLSkYT9o8b+wETAFjGYhPvOataXf/8f7nwiSSN6bE5M0Ow0opGp" +
       "qF60QNjrhs\nClhTc7ZN55P93i1rHz3w4mvzPetmpDnH6XJnotPMzda7ZcRp" +
       "AoKSx/7Of67+4LbSpY+FSQl4IsQi\nBqpGx56ZLSPgPK1uIMK9RGKkeihn41" +
       "UMFDTq2zDCGgR1wjbgLCZmLZDHsEPXl33r1Sern+M7dsNd\nrS8u9lImnKfe" +
       "O/8+i1K4/9pd3bfteHfbpRFwV9MUZ85ImZke1NR4BqacHPQ3XF4Czeadva11" +
       "2xfZ\nj/MjrlRTqTRTBjUK8VnRNGOUJgYYD1D1vmDIYxBoomYQYhmY54AGjM" +
       "RezdAImGeeaBGdMvH2Oxbs\nfBXjickVMxn9jq+UZPiN5hDC+TlEvJ6BFjzR" +
       "2zZEgC1iAzULei9bs+mmuXznfnYLnYuMZOj5whkF\nfaED8w7YDDMgAHkms+" +
       "l7By9au/HiFhESFxdk0QXunOB8vPk7OvXlT006oIfRSsrszdLlYyRsM0bm\n" +
       "x1yWjuXhpcB61/QKkpsWFh55A55g9apPrt37yo4aENxPylW7Q9UVDU/b7hLe" +
       "mydlZLG48qn1Pzr0\nO/Y6t03PXXD50zO5gW6D4vPksw+M1Jc9sisVJuX9pI" +
       "6nfUVnGxQtjZbbD4nbbnNuxshJAXowCYuM\n0yrDwfRsV/WJzXbUEp+2S3A0" +
       "4hXCN/mY2n+Ln8/xg4aDF25qMFIm5BNr9gUUNq0wmjAzoZCJ087m\nk2dyOE" +
       "fYX5iBaFQxI+WmpY4oWD1heTKiMl4m8cGNjDTJQ7TSOlNTFOs6x+x4pAhxj1" +
       "gOBtucdd7S\nNKJoGkzk7H0Hlu3V2fQ3+CGVO8UaVkxeCIEsqoEOeRq2wNcK" +
       "cZUGNxB3UmVIhKb4sKolcGVLgMe8\ngpYopt9a3rv74uTt94Udb642eRg71f" +
       "Nnb11283o9ZSTUIZXHIfDww7WnLH7sne11IhN4sQa84MgM\nvPtTV5JrXrz8" +
       "05mcTSiOPNqcULRQBobTCmlkJU2q+RRSxhQrSZlI37CtAjx8LM7R7tz3k+4P" +
       "b3KV\nssrEOF3oSHponKojNHsFGI3OLDBtLeBQcYIRp9K6GucmKHjsPvP6g3" +
       "dc1/IxBP9LSQXVaAqqDLCY\nmZcGSl+I8n0WVC7tYgC4VnVK8OxzHSlLCe5+" +
       "8HcX39cpBU1NT+RuCueu8/HweBXSUac+CtaZo6Pc\nUyq0oJhhmILDljX39j" +
       "+95lBM2N6gkdgq7N7ZoOlb1YKCqxq0ICoNH3lZhQxwZV4eENMbVHsVNUGL\n" +
       "cDraVpHbwU0VmMxIo8/9Vyv28FrFbJXrJ8FqwqGXlv/l6Wcmb/pDhIQ7SJVm" +
       "KIkOhddYpBKKG2oP\nQwmbMZefz5dQM1oBsA4DHzCbJNfPGYIulUGq+VbbSS" +
       "qGQUqbkYAiI6Ipg7iUs2Als++fxkhJ+4b2\nLlepUAW7phhFU4zmmmK44xvL" +
       "ln02dykvK0DCalhCU4EJSuVzd5961ms3QNnXCTuDHNaVxjAZIxMw\npygWr+" +
       "WhvHFSRQU+R3R5qaNKPCl2edU6IaX74PHVkwn1MVXgsbNaIFGkMHISnLG3Gs" +
       "4+CettYl/S\ndRmZU4jsSC1bBs8P7DwprpYdfYBhZGLWHc4UWRmS4XmIXIEA" +
       "SucKewvEEOYkOCRXeWQETBLOQCSF\nAJ4U/dFEjuj3j6jUwVzaNEP36C8X3k" +
       "ogffGtBO44+qlkUD4H+R5E5NsIrgJzjBvmVkmaz3zZTrFF\nsYMrczNdF+Ao" +
       "rd5/6YjiRYdktZT5AlA2K+HnLrPG4A2H3eSEaiupQTWZhvLCEycFHGZkRrYr" +
       "umXM\neri0uU4Cd+TxbnO5iPrjZgS3BAikuZAqMHz6VOFd5kogh8eQMKbor5" +
       "RATme+9J69b6cQc7c+MevO\nF9g92ei37hHVVpmvNlwFkQrZrkyr+CoB8vWY" +
       "NMdaJgziJa/pcYAU04rIDxHsBGfQqDJC2+C5T9K3\nwzHL0l4so92yDGu1oi" +
       "cgeCY34C0DVjA132254V1SsXWI/BjBvVAlc4FS2B4w2SxhzqtHKaYxeEMK\n" +
       "uE8KgOJ7WhYT1L7kUOe7cpTTyIe1J5JwTutGqGWpPj/6MyL3I3gANCRHSvrD" +
       "zPe4iwbhnn51wF2r\n+Mw2LJzl1LcR+RmCR1zWWO5K+nJGpgRY88S0EmoQ5F" +
       "/nu3JzjCfEol4Q/pe3BQQP5tsbgoe9TXkr\nQ7BHEnblXRKalLckcSWP5onj" +
       "sY63g4RHJWErmGJggb1G2orTDghyIhr5Lv8rS9zjP8RSfgCSdrbf\noIcsNF" +
       "09EXWMrxtUBSrzXTmnGIGHQZdFuI/53r1KFr3xYZpIa+j6EyTuTC+H6chNes" +
       "RHiLyM4BVB\nvcBQpIeHfu6nViuM0ZTJAiN2eiMQ/KlI+J8QtLEIC/wnVgvn" +
       "2WdgGofCS/VVXg/646A0HDffQ6xw\nUdds4uJaMuAJ6S0EbzP8Yw2nouNIyy" +
       "KjjDRIGSlF1aNrAUAsw18O31Ibnm88l+hC5D0E7yP4QBJO\nhiXJhIfz84yZ" +
       "gAg35/ehnkKB7puUOn/JjzU8boi/M1/MhygktBhq38i6Vau8hwtEKH9mcASU" +
       "7kNE\nRbAZwRavTEdEFKMIvPq3FhGvwM1bGCPwNHA0JTGC0XyVMAKvHuVGIE" +
       "pVBN/JV8Mi+K5XwSJyLYLr\nEFzv1aOI3IDgRgTHUJ+RZkS+j2D7MbL6iks9" +
       "RO5EcNfR7YNsRERUUwjuyVdmIZDlkNCQKIcCBH+d\nhEDWNyLf/BTB7gAhFE" +
       "bkOGfX5Yg8huBxBMclte9C5EkEvzxuMsYuHxB5BsGzxyp8T1DG815VgMgL\n" +
       "CF5E8JKX6xHxIv7v82VwBPvzJW8EJ0jm+v8lHE3ORvCml6YREVkWwd/zpV8E" +
       "73hJF5F8GXTs1IrI\nR3kIE4KEjxF84mXOjIfKXBkbr7lyESLuax3xF4xjyK" +
       "Mz2FG+P5YvQAKvnfiLZMlsauE3WYE32vyt\nQuCO+xQT2iAZluMlJqPQJUHC" +
       "5XiJYSa0PqidMTVwYhLGPJcd7Oj+UgYVo+9KPgh6CkEXD+Er3tDQ\nUYoes7" +
       "Q6FxGvjvAIeDjFcuEYhRfLhWK5MF4J47xc4AR4+K73Hr47dUaT+HrBLRzylB" +
       "DtX4MSAkCo\n+8TIhV8iSV4RyG3FpFdMel9kg8WkVyQUk57vjfMqIz2o0TyZ" +
       "blUx0xUTGikmtGJCKya0ry9hnCc0\nAKE38ySvtmNJXoVfj3r/78tfj3qXY/" +
       "yDYfh0BDyNBdNdXsI4S3fFrFbMasWsVsxqJxhh/Gc1/58y\nZVZbeSxZDZGv" +
       "8yNZMRUVU1ExFRVT0QlGGP+pKLQzTypaUUxFxYxDihmnmHGKGecEI4z/jON/" +
       "+PH9\nRydS1oiv0gdaVvB/gp/ntGEhCEUbFreHim35vsmU1YiKN9HatvHDmh" +
       "uVZy8T3Qgags1m2vV06sxd\nB+mC82viebodVTLDXKTRESq+c4SdWsKikQaI" +
       "XVSwvcRa/m1sr9VIpHf5gtMWVv8tTErGaFhU79zs\noSxt6b7+BTBa+cKdjG" +
       "ZlKSV7PfUjTRdFhtXnw7wFiehaktOOLDipNdirpMoKLpQfYRNfQC2oCL96\n" +
       "3wCfaUT88N9IrEfQIBoOAQh/Ls52jBY7eYluuxy8bmRO0yn+F8/2TJya+HUj" +
       "5B+pcr6zlttEpttS\nUyqT3x+/deZ9bz76Rs9EYQOiOdu8nP5o/jmiQZvbMw" +
       "QkzCkkgY9+9ptzHrq65/VBp4VEJMJIyYih\nJnBzkUlBq86+iJRLxeLnNPhs" +
       "dBS7MZ9icx2JZ+95jJTZvHVeYcXmdnfj3byEsvZGmj8M/2JyM29Q\nVTKo2L" +
       "LRRqAtXm7Xu0AzO77OKrmrqfCZdYRdfZLXALBMjDRnQgS73kROHWPn54o4Av" +
       "vXqJ5kw1yp\njaJ5R2Qy2AmsFtF5mRxXz7Uy/r187OHi0kT/J9WIyqaDQMxt" +
       "74QCKsRyuawvaf6+9lOR0/F/xmWb\ngOCf/0VXokzBo0YevPtXpMn55hq3xh" +
       "ybDPmUE7iD/4vvNIHDzjlTst3A6TcYn7t/0/xnzPrfCqtx\newKWYyOLtKb5" +
       "eyD58DLTokMqP/xy0RHJ5GtdyUhtVic6RipclOt5hRjZznuN4Ui86uA2sjQT" +
       "+g91\neDivylIAAA==");
}
