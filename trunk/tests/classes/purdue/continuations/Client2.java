package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client2 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1aa2wcVxW+s7ZrOwmOGydp08ShLUmaoLImQKK2dpvUL2x3" +
       "4xjslCoVTWZ3b3Yn\nnp2ZzNx1bFQqREEKIAUVaCseUiUeqqo2goBKUcVDFU" +
       "KRQEKCH6gg8av8oAh+IIRAIkicbx73zqxn\nN25NWludSj05M+fec84999zz" +
       "nbnrF/7GOjyX7fG45xm2dTYvlhzu5fEYcLNTs1zMEXuqdOV3Q9+3\nxK7Xck" +
       "wrsM4arxW56wm2vXBWX9AH6sIwB0Zs0+QlQZoGF122v6VWXxRoZsF/mq+5o1" +
       "Q1zLJg+wvR\n9AF/0oCcPpCYDkvvabAEahfPkiuwFRh5onP22Y9VvvLtHGM0" +
       "Y5tjm0sV0xbhjGDM/XtfOlH488/e\nHYzZmTLmuK/1VOnq4V39v73/T91tcL" +
       "nLsT0Dqxbs5kI0KYjITCgZXHScRYdCvQ/RykOWV9Hy9pyw\nanbZOGPoRZMX" +
       "DE9c3bz34It/vdjrh6TdpDeC9cYijTFY+Z3XVqfe7xhmn/rVI//a7SvVSufY" +
       "Y0zz\nndqqtBQMa56XoX7TgdmPT52+cHsbBcM5344NoqF3tNrUYV4x0vb0Bq" +
       "G7FU5L2BIswdStysCscA2r\ngkXsa6EzpvIe8+kr35n5+wXsDnzPOYte6yz7" +
       "KC9xY4E3eoTNPdRi2jHi9QoFrlarW0ZJFzJTnz30\n+KtPfXrgnznW9jDr4i" +
       "avcUvQIdj9cGJheml+ztVLfCwYMFhgG2uBTqgRbJvKkiCj/USWccLieuUS\n" +
       "8W+fv9S9LQ+UVV6+TszdGtOhdLUK26R1ns7gsrBpDQ62dqhg206gYX7qmydf" +
       "mfp3Icjlol1ewvy2\naIFOzKsDLb0qurpVql7brVY5OpyqI3eSbTG8Ue5QFG" +
       "nDzCUMKKMY6TRZsL7Y0ZvQveox3RmU/jNy\n/EZ1gkJ5R+cfXvn59tO/aWO5" +
       "cbbBtPXyuF4StjvJukXV5V7VNsuLzpGjvgubzncR7aX/c6Rsm/Tf\nV0ix1I" +
       "vcjHk7ybqqZGXELvMCazP1YvJkBUWKSg679blbBGsfe3BsOgqyU3ejbM1jcH" +
       "55tubG3zs0\n9N/b70YCOWRxglza2WKC3v2Lr+07/MfP5lj7JK3UsPh0HeBQ" +
       "YD1lXjJ1HPMRU/d8F3qpXJ6h5JrW\nazx83lDjomqX5RsY7bhC9U7ZnKOI6Q" +
       "QKGwMmD4lg76I9V9746vPk707xJk+3YLe1EodWbxgyLEPc\nJ81tFiuvQYJt" +
       "bXjjK4Wqw1LhfWDuArlbsC5vnsqMoKIeiTcoMcigFHwIjE8OiUTBkSNOxkd0" +
       "W5Q+\nI6ZtKfmvWy8lAdr+UhJvwvh0C5c36H0VzDDICKVjyXaWpGi/iKG37g" +
       "VYC88i5J4mHtZujD+GptpF\nfHUUrb1NVQXnPlLWl3wRqtteNjy9VjQqdV1w" +
       "ZU4auCpYf+PRpDxxKJ3dE/To+TFJvJHbOx1p0TQw\nMyAfSQjYnlahQDmNhU" +
       "I9LrfArjax0NT02ypgHxSxDqBx3WH7GS19a8ObN7B69lA8uxcMasxI/4xr\n" +
       "C7tkm6NUqaB2uE7dJ3cJ0pvKwmzpKeJx3OBmGQOkmUEwRRDyt9vk+gIf0U0l" +
       "v0jbLDvLwI0x17Xd\nCd0qU/GsPIhXNnmwI+21XPAZGdheMFUQQ7AO36A0dp" +
       "lStsHYCPWpfFFIM33JF9LAvDSQE+yWBiWI\nvtTQG3sKg9PnDxsrV2ifji9w" +
       "1zVi5+j3YByQcxQhOVLKL9F8aQ8JEe3+xsRx3eDPHMHngpz6OpgF\nkPORav" +
       "SyUn6EWvSEah+Yhqkngf7e2FOEMcqIy1UR/o9aAoibtjYQZfmS8gxkUQqeSX" +
       "UJKaVcCp7k\n1jx2Pfx4PSn4hBQsUSomHJy1626Jj1ORC6pR7PH/4uLl+CZ2" +
       "+BsgZXfFE/qMi9S1yvkw+WYoVBSy\n2FO4i21uXe5dbk7EPu2kitlSlZfrJo" +
       "5+j+TD6Z00HdrkifgHmCdBngqkH7Z1ecK1H8WlG3UheM0R\niRHfUCNAvp4J" +
       "3hKB2UxwIL5jm2k/52zAODVeRqzzej5eB2XiRHhPtSJio7QpBc9SwefBfBfk" +
       "e4Jt\nCqU4ODKzkPFbpI2ablj5Y0SoluGfUG+HR9876khMg3kR5IcgL0nBTe" +
       "SSBDzMTxnTA+ZlX0D9FAxS\nt7+r4eZjxqT+esIG+NE38Gu/fHTsgc9dCD7n" +
       "LNW1Jzr24OseMTAN+kY46Bu8g2kHqV1uOz46qr5H\nwNzpf2aEPnVcAfN+kI" +
       "MgH1CdPZigfwVRLfNmMKonTu2lQVSzvJIuGkT1+SeTgqOqawYTdLcgo2lt\n" +
       "L8i4anrBTIBMgkypFhZMAeQYyCpaOrYHzBzIiVWqepu7QzCPgJxa2TrYQ2CC" +
       "BgxENQeDSYHsoNhF\nMEEHlRDEWysQ2RIFEFUDsRICLQfmOgPyETCPgnwS5L" +
       "p0A8+AeRzkM9fNRvOOA4xfLL+wWuOXkza+\nqBoJME+AfAnky6o9AKNA4uk0" +
       "0Af5ahreg6wRsHvnClYC8yCXFLKDCYAZ5HIaYoP8QOE0mDTQbY7G\nYF5OEf" +
       "QkBT8G+alCzkXFSqwcW69Y+T4w0U1Q8FPPKnC0X6zwClremSRuqvy7aKlsR+" +
       "vLr8SluH8R\nkXgTffhou6XCTjzeCnJbUgA003aB9Cej0zQCa1PQdF+eFCv7" +
       "SZE6xtiT/HZUAcER15AzWn6Fppu2\nVveCUX2EEhwVsR8MUq+FXN3yTN2/6u" +
       "hvJgpT6iYRvuHJgdIajqqGk6H5JyPrQbIeBCTrQa4lWOeg\n7gt+IsIfCv1P" +
       "5ElL8AruDSJ4TwF6CanrGOiJaDevDcR6E1B2LoFAbzE0nYuDhaZ2MEORDEUy" +
       "FHnn\nokjsonXUrhdNngId92bQkSFEhhAZQmQI8UYE6xwhiGjfSkGDodWgQe" +
       "trNvWnp/41m3ps8rdtGs6C\n9he8SuJHqmCd4UcGExlMZDCRwQRb8zAR/41J" +
       "woSsu9lHw3JBVtuz2p7V9qy2szVf27XTKbX9nqy2\nZyWcZSU8K+FZCWdrvo" +
       "TH2/PYH4NB0vM/saZFJrc/AAA="));
    
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
    final public static long jlc$SourceLastModified$jl = 1329257451000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cbWwcxXXuzt828VcSkjjfMZC0ybkJHyI4EBLHJg4X28RO" +
       "CEbgrO/G5032dpfd\nOftCKeVDEKBqJAQBSkOjItqIL5UEAZVKaRHQUKDQpl" +
       "VIK8EfKgotn0JA1Aba92Z2Z3fvzpdAmhLT\ns3TPM/tm3pt587527/Y99C4p" +
       "tS0Staltq4a+Ocq2mtTm0BjcTOPMjvau6VEsmybaNMW2+wAxEN/0\nubphT/" +
       "11q8Mk1E8adGOFpip237BlpJPDfcOqnbHIbNPQtiY1gzkUc2icM+/w6Evb1j" +
       "RFSG0/qVX1\nXqYwNd5m6IxmWD+pSdHUILXsFYkETfSTep3SRC+1VEVTr4SB" +
       "hg6MbTWpKyxtUXsdtQ1tBAc22GmT\nWpynezFGauKGbjMrHWeGZTNSF9usjC" +
       "gtaaZqLTHVZq0xUjakUi1hX0GuJuEYKR3SlCQMnBxzd9HC\nKbZ04HUYXqXC" +
       "Mq0hJU7dKSVbVD3ByKzsGXLHzRfCAJhanqJs2JCsSnQFLpAGsSRN0ZMtvcxS" +
       "9SQM\nLTXSwIWRaWMShUEVphLfoiTpACNTssf1CBSMquRiwSmMTMoexinBmU" +
       "3LOjPfaXWX1Xx2S8+ns+HE\nYc0JGtdw/WUwaWbWpHV0iFpUj1Mx8VA6envn" +
       "JenpYUJg8KSswWLMilOeWB9761ezxJimPGO6uS4O\nxA+fNX3G/hV/rYzgMi" +
       "pMw1ZRFQI756fa42BaMyZo92RJEZFRF/nrdb+55JoH6D/CpKKTlMUNLZ3S\n" +
       "O0kl1RNtTrsc2jFVp+Jq99CQTVknKdH4pTKD90EcQ6pGURyl0DYVNszbGZMQ" +
       "Ug6fEHz6iPirQsBI\nVZumUp0tidqbGWll1GZ2i23FW8y0lUjTFtBWpuppru" +
       "YDNGMaFnSTLTSjpEyNLmnxZmeQ1YTRUAh2\nOT3b4jRQz9WGlqDWQHz3Gy9c" +
       "1X7hzTeJ80OdcxbJYB7nGvVzxbPnPEgoxIk3onoK8a2wLGUrmk3m\n2v0zfr" +
       "BPuQcOA4Riq1dSvufQaAlCmLSkoFdp82yyE1oKqMxAfOI1b027+4/3Pxcmkb" +
       "yeJSYvdhhW\nStFQOVxTanDYZWNAp5qzNTsf7/duWfvogRdfm+/pOCPNOaaX" +
       "OxNNZ2628C0jThPgmjzyd/5z9Qe3\nlS59LExKwB5R2groEZj3zGweARNqdd" +
       "0R7iUSI9VDORuvYiCgUd+GEdYgqBMKAmcxMWuB3JMdur7s\nW68+Wf0c37Hr" +
       "9Gp93rGXMmFC9d7591mUwvXX7uq5bce72y6NgNGapjhzRsrM9KCmxjMw5eSg" +
       "1eHy\nEqg27+xtrdu+yH6cH3GlmkqlmTKoUfDSiqYZozQxwLibqve5RO6JQB" +
       "I1g+DRwBYGNCAk9mqGRkA9\n8/iM6JSJt9+xYOer6FVMLpjJaH18pSTDLzSH" +
       "EM7PQWJ/BmrwRG/b4Ae2iA3ULOi9bM2mm+bynfvJ\nLXQ6GUnQs4UzCtpCB0" +
       "Yf0BlmgBvyVGbT9w5etHbjxS3CMS4uSKILbDrB6Xjzd3Tqy5+adEAPo5aU\n" +
       "2Zul3cdI2GaMzI+5JB3Nw65o9a7pFSg3OCw88gY8xupVn1y795UdNcC4n5Sr" +
       "doeqKxqett0lrDdP\n4MgiceVT63906Hfsda6bnrng8qdncr3dBsVnyWcfGK" +
       "kve2RXKkzK+0kdD/6KzjYoWho1tx/Ct93m\nXIyRkwL4YCgWcadVuoPp2abq" +
       "Y5ttqCU+aZfgaGxXCNvkY2r/Lf4+xw8qDnZEgGhoM1ImRBVr9gUU\nNq0wmj" +
       "AzoZCJ087mk2dyOEfoX5gBaxQxI+WmpY4omENhkjKiMuHPcXAjI03yEK00eP" +
       "sUxezOUTvu\nKULcIpaDwjZnnbdUjSiqBhORe9+BZXt1Nv0NfkjlTsqGeZPn" +
       "QiCWaiBDHowtsLVCVKXCDcSdgBkS\nrik+rGoJXNkSoDGvoCaK6beW9+6+OH" +
       "n7fWHHmqtN7sZO9ezZW5fdvF5PGQl1SOV+CCz8cO0pix97\nZ3udiASerwEr" +
       "ODIB7/rUleSaFy//dCYnE4ojjTbHFS2UjuG0QhJZSZNqPoGUMcVKUiZiOGyr" +
       "AA0f\niXO0O/f9pOfDm1yhrDLRTxc6knU0TtURmr0C9EZnFpi2FtqQd4ISp9" +
       "K6GucqKGjsPvP6g3dc1/Ix\nOP9LSQXVaAqyDNCYmZcGEmDw8n0WpC/tYgCY" +
       "VnVK0OxzDSlLCO5+8H8X39cpBVVNT+RuCud2+2h4\ntArJqFMfBe3MkVHuKR" +
       "VaUMwwTEFhy5p7+59ecygmdG/QSGwVeu9s0PStakHBVQ1a4JWGj7ysQgq4\n" +
       "Mi8N8OkNqr2KmiBFOB1tq4jtYKYKTGak0Wf+qxV7eK1itsr1k2A24eBLy//y" +
       "9DOTN/0hQsIdpEoz\nlESHwnMsUgnJDbWHIY/NmMvP50uoGa0AWIeOD4hNku" +
       "vnBEGWyiDVfKvtJBXDwKXNSECSEdGUQVzK\nWbCS2fdPY6SkfUN7lytUSIVd" +
       "VYyiKkZzVTHc8Y1lyz6bu5SnFcBhNSyhqcAEpfK5u08967UbIO3r\nhJ1BDO" +
       "tKo5uMkQkYUxS81+PpjRMqKvBuossLHVXifrHLS9kJKd0HN7EeT8iPqQI3n9" +
       "WiEUUMIyfB\nGXur4eSTsN4m9iVNl5E5hdAO17Jlqq6y8yS7Wnb0DoaRiVlX" +
       "OFEkZUiC52HjCgSQOlfYW8CHMCfA\nIbrKQyNgEnEGNlII4H7R703kiH7/iE" +
       "od1KVNM3QP/3LhrQTCF99K4Iojn0oG6XOQ7kFsfBvBVaCO\nccPcKlHzmS/a" +
       "KbZIdnBlbqTrgjZyq/d3HVY86ZCkljKfA8omJezcJdYYvOCQm5xQbSU1qCbh" +
       "FpF6\n7CSDw4zMyDZFN41ZD12byyRwRR7vNpeKyD9uRnBLAEGaC4kC3adPFF" +
       "43lwM5PAaHMVl/pQhyOvOF\n9+x9O4mYu/WJWVe+wO7JRr92j6i2yny54Srw" +
       "VEh2ZVrF5wkQr8fEOdoyYRC7PKfHAZJNKzZ+iGAn\nGINGlRHaBvd9Er8djl" +
       "mm9mIZ7ZZlWKsVPQHOM7kBLxmwgqn5LssN75KCrcPGjxHcC1kyZyiZ7QGV\n" +
       "zWLmPICUbBqDFySD+yQDSL6nZRFB6UsKdb6eI5xGPqw9kYRz6h6hlqX67OjP" +
       "2LgfwQMgITlS4h9m\nvttdVAj39KsD5lrFZ7Zh4iynvo2NnyF4xCWN6a7EL2" +
       "dkSoA0D0wrIQdB+nW+nhtjPCYW9Zzwv7wt\nIHgw394QPOxtylsZgj0SsSvv" +
       "klClvCWJnjyaJ47HOt4OIh6ViK2gioEF9hppK047wMkJb+Tr/leW\nuMd/iK" +
       "X8ACTubL9CD1mounoi6ihfD4gKRObrOacYgZtBl0S4j/mewEoSvfFhmkhraP" +
       "oTZNuZXg7T\nkZq0iI+w8TKCVwT2AkORFh76uR9brTBGUyYLjNjpjUDwpyLi" +
       "f4LQxkIs8J9YLZxnn4FhHBIv1Zd5\nPej3g1Jx3HgPvsJtumoTF31JgAektx" +
       "C8zfArG45Fw5GaRUYZaZA8UoqqR9cCAF+G/xy6pTbc33gm\n0YWN9xC8j+AD" +
       "iTgZliQDHs7PM2YCNrg6vw/5FDJ0n6TU+VN+zOFxQ/yZ+WI+RCGhxZD7RrpX" +
       "rfJu\nLrBB+T2Dw6B0HzZUBJsRbPHSdGyIZBSBl//WYsNLcPMmxgg8CRxNSo" +
       "xgNF8mjMDLR7kSiFQVwXfy\n5bAIvutlsNi4FsF1CK738lFs3IDgRgTHkJ+R" +
       "Zmx8H8H2YyT1Fad62LgTwV1Htw+yERsim0JwT740\nC4FMh4SERDoUQPjzJA" +
       "QyvxHx5qcIdgcQoTA2jnN0XY6NxxA8juC4hPZd2HgSwS+PG4+x0wdsPIPg\n" +
       "2WNlvifI43kvK8DGCwheRPCSF+ux4Xn83+eL4Aj25wveCE6QyPX/iziamI3g" +
       "TS9MY0NEWQR/zxd+\nEbzjBV1s5IugY4dWbHyUBzEhiPgYwSde5Mx4TRkrY+" +
       "M1Vi7ChvtYR3yDcQxxdAY7yufH8gFI4LET\nf5AsiU0t/CQr8ESbP1UIXHHv" +
       "YkIbJMFy7GIwCl0SRFyOXXQzofVB6YwpgRMTMea57GBH900ZZIy+\nnrwR9A" +
       "SCJh7CR7yhoaNkPWZqdS42vDzCQ+DhFNOFY2ReTBeK6cJ4RYzzdIEj4Oa73r" +
       "v57tQZTeLj\nBTdxyJNCtH8NUggAoZ4TIxZ+iSB5RSC2FYNeMeh9kQ0Wg14R" +
       "UQx6vifOq4z0oEbzRLpVxUhXDGik\nGNCKAa0Y0L6+iHEe0ACE3swTvNqOJX" +
       "gVfjzq/d6XPx71umP8wDB8OgIexoLhLi9inIW7YlQrRrVi\nVCtGtRMMMf6j" +
       "mv+rTBnVVh5LVMPG1/mWrBiKiqGoGIqKoegEQ4z/UBTamScUrSiGomLEIcWI" +
       "U4w4\nxYhzgiHGf8Tx3/z4ftGJmDXiVfpAyQr+I/h5ThkWglCUYXFrqNiW70" +
       "2mrHJUvJTWto0f1tyoPHuZ\nqEbQECw2066nU2fuOkgXnF8Tz1PyqJIZ5iKN" +
       "jlDxzhFWagmLQhrAdlHB8hJr+dvYXqmRSO/yBact\nrP5bmJSMUbCo3rm4jr" +
       "K0pfvqF8Bo5QtXMpqVJZTs9dSPNF0UGVafD/MSJKJqSU5RsuCk1mCtkior\n" +
       "uFB+hE18AbUgInz1vgE+04j44/8RWY+gQRQcAhD+XJztGCV28iLdcjnYb2RO" +
       "0Sn+jWd7Jk5NfN0I\n6UeqnHfWcovI9FhqSmXy/fFbZ9735qNvrJsodECUaJ" +
       "uXUyXNP0eUaXNrhgCHOYU48NHPfnPOQ1ev\ne33QKSERiTBSMmKoCdxcZFJQ" +
       "q7M7kXIpWPycBp9uR7Dd+QSba0g8es9jpMzmBfQKCza3xhuv5iWE\ntTfS/G" +
       "H4F5ObeYGqkkHFloU2AsXxcmvfBUra8XVWyV1Nhc+sI+zqk7wKgGlipDkTIl" +
       "j1JnLqGDs/\nV/gR2L9G9SQb5kJtFMU7IpNBT2C12JyXyTH1XC3j7+VjDRcX" +
       "J+o/qUZUlh4EZG55J2RQIZbLeX1J\n9feVn4qcjr8Zl2UCgl//i6pEmYJHjT" +
       "R49a9Ik/PmGtfGHJ0M+YQTuIK/xXeKwGHlnCnZZuBUHYzP\n3b9p/jNm/W+F" +
       "1riVAcuxkEVa0/w1kHztMtOiQyo//HJREcnka10JIs9Xjo7xKk1en0t8hZjT" +
       "zquO\n4RzsdXBtWZoJ/Qfzrocw2lIAAA==");
}
