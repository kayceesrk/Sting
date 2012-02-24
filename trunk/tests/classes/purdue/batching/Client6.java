package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client6 {
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
            ss = SJServerSocketImpl.create(invitation, 20102);
            ss.participantName("client2");
            ss.addParticipant("client1", "localhost", 20102);
            SJSocketGroup client1Socket = null;
            try {
                client1Socket = ss.accept("client1");
                SJRuntime.pass("Hi, from client2", "client1", client1Socket);
                String str =
                  (String) SJRuntime.receive("client1", client1Socket);
                Integer ii =
                  (Integer) SJRuntime.receive("client1", client1Socket);
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
    final public static long jlc$SourceLastModified$jl = 1329790687000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cbWwcxXXuzt828VcSkjjfMZC0ybnhIyU4NMSOTRwujomd" +
       "EIzAWd+Nz5vs7S67\nc/aFUsqHIEDVSAgClIZGRbQRXyoJAiqV0iKgoUChTa" +
       "uQVoI/VBRaPoWAqA20783szu7enS+BNCWm\nZ+me3+6beW/mzfvate89+A4p" +
       "tS0Staltq4a+Ocq2mtTm0BjcTOPMjvau7lEsmybaNcW2+4AwEN/0\nmbphT/" +
       "21q8Ik1E8adGOFpip237BlpJPDfcOqnbHIbNPQtiY1gzkcc3icM+/w6IvbVj" +
       "dFSG0/qVX1\nXqYwNd5u6IxmWD+pSdHUILXsFYkETfSTep3SRC+1VEVTr4CB" +
       "hg6CbTWpKyxtUXsdtQ1tBAc22GmT\nWlymezNGauKGbjMrHWeGZTNSF9usjC" +
       "gtaaZqLTHVZq0xUjakUi1hX06uIuEYKR3SlCQMnBxzd9HC\nObZ04n0YXqXC" +
       "Mq0hJU7dKSVbVD3ByKzsGXLHzRfAAJhanqJs2JCiSnQFbpAGsSRN0ZMtvcxS" +
       "9SQM\nLTXSIIWRaWMyhUEVphLfoiTpACNTssf1CBKMquRqwSmMTMoexjnBmU" +
       "3LOjPfaa0tq/n05p5PZsOJ\nw5oTNK7h+stg0sysSevoELWoHqdi4qF09Lau" +
       "i9PTw4TA4ElZg8WYFac8vj725q9miTFNecas5bY4\nED+8ZPqM/Sv+WhnBZV" +
       "SYhq2iKQR2zk+1x6G0Zkyw7smSIxKjLvHX635z8dX303+ESUUXKYsbWjql\n" +
       "d5FKqifaHbwc8JiqU3F37dCQTVkXKdH4rTKDX4M6hlSNojpKATcVNszxjEkI" +
       "qYBPCD4XEPFTiYCR\niW0Kiw/3Ak+7XVOpzk6P2psZ+SajNrNbbCveYqatRJ" +
       "q2DOI4MIaWhMKUIc0Ybck3M4MCJ4yGQrDX\n6dl+p4GRrjK0BLUG4rtff/7K" +
       "jgtuulGcIlqes1RGThYSo67EqGC/hIRCnG8j2qfQ3wrLUrai32Su\n2T/jB/" +
       "uUu+E0QCu2egXlmw6NliCESacXDCvtnlN2AaaAzQzEJ1795rS7/njfs2ESyR" +
       "taYvJmp2Gl\nFA2tw/WlBkdcNgWMqjnbtPPJfvfmNY8ceOHV+Z6RM9Kc43u5" +
       "M9F35mbr3TLiNAGxyWN/xz9XvX9r\n6dJHw6QEHBJCElPAkMC/Z2bLCPhQqx" +
       "uPcC+RGKkeytl4FQMFjfo2jLAGQZ2wDTiLiVkL5KHs0HVl\n33jliepn+Y7d" +
       "qFfrC4+9lAkfqvfOv8+iFO6/emfPrTve2XZJBLzWNMWZM1Jmpgc1NZ6BKScH" +
       "3Q6X\nl0CzeXtva932RfZj/Igr1VQqzZRBjUKYVjQwcZoYYDxO1ftiIg9FoI" +
       "maQQhpYJ4DGjASezVDI2Ce\neYJGdMrE225fsPMVDCsmV8xkdD++UpLhN5pD" +
       "COfnEPF6BlrwRG/bEAi2iA3ULOi9dPWmG+fynfvZ\nLXQuMpKh5wtnFvSFTk" +
       "w/YDPMgDjkmcym7x28cM3Gi1pEZFxckEU3uHOC8/Hm7+jSlz856YAeRisp\n" +
       "szdLl4+RsM0YmR9zWTqWh5cC613dK0hudlh45A14gtUrP75m78s7akBwPylX" +
       "7U5VVzQ8bbtbeG+e\nzJHF4oon1//o0O/Ya9w2PXfB5U/P5Aa6DYrPk88+MF" +
       "Jf9vCuVJiU95M6nv0VnW1QtDRabj/kb7vd\nuRkjJwXowVwsEk+rDAfTs13V" +
       "JzbbUUt82i7B0YhXCN/kY2r/LX4+ww8aDl6IDNHQbqRMSCvW7PMp\nbFphNG" +
       "FmQiETp53NJ8/kcI6wvzAD0ahiRspNSx1RsIjCKmVEZbxa4oMbGWmSh2ilda" +
       "amKJZ3jtnx\nSBHiHrEcDLY567ylaUTRNJhI3fsOLNurs+mv80Mqd2o2LJy8" +
       "EALJVAMd8mxsga8V4ioNbiDuZMyQ\nCE2Qk7QErux04DGvoCWK6beU9+6+KH" +
       "nbvWHHm6tNHsZO9fzZW5fdvF5PGQl1SOVxCDz8cO0pix99\ne3udyARerAEv" +
       "ODID7/7UNnL1C5d9MpOzCcWRR7sTihbKwHBaIY200aSaTyFlTLGSlIn0Ddsq" +
       "wMPH\n4hztjn0/6fngRlcpK02M04WOZB2NU3WEZq8Ao9FZBaatARwKTzDiVF" +
       "pX49wEBY/dZ1138PZrWz6C\n4H8JqaAaTUGVARYz85JABQxRvs+CyqVDDADX" +
       "qk4Jnn2uI2Upwd0P/u7m+zqloKnpidxN4dy1Ph4e\nr0I66tJHwTpzdJR7So" +
       "UWFDMMU3DYsvqe/qdWH4oJ2xs0EluF3TsbNH2rWlBwVYMWRKXhIy+rkAG2\n" +
       "5eUBMb1BtVdSE7QIp6NtFbkd3FSByYw0+tx/lWIPr1HMVrl+EqwmHHpp+V+e" +
       "enrypj9ESLiTVGmG\nkuhUeI1FKqG4ofYwlLAZc/l5fAk1o1hY12HgA2aT5P" +
       "o5Q9ClMkg132q7SMUwSGk3ElBkRDRlEJey\nBFYy+75pjJR0bOjodpUKVbBr" +
       "ilE0xWiuKYY7v7Zs2adzl/KyAiSsgiU0FZigVD5716lLXr0eyr4u\n2BnksO" +
       "40hskYmYA5RcGHPV7eOKmiAh8nur3UUSUeGLu9ap2Q0n3wFOvJhPqYKvD0WS" +
       "2QKFIYOQnO\n2FsNZ5+E9TaxL+i6jMwpRHakli1TdZV9S4qrZUcfYODJKOsO" +
       "Z4qsDMnwW4hcjgBK5wp7C8QQ5iQ4\nJFd5ZARMEs5EJIUAHhj90USO6PePqN" +
       "TBXNo1Q/foLxXeSiB98a0E7jj6qWRQPgf5HkTk2wiuBHOM\nG+ZWSZrPfNlO" +
       "sUWxgytzM1034Cit3n/piOJFh2S1lPkCUDYr4ecus8bgDYfd5IRqK6lBNZmG" +
       "8sIT\nJwUcZmRGtiu6Zcx6uLS5TgJ35PFuc7mI+uMmBDcHCKS5kCowfPpU4V" +
       "3mSiCHx5AwpugvlUDOYL70\nnr1vpxBztz4x687n2D3Z6LfuEdVWma82XAmR" +
       "Ctm2pVV8lQD5ekyaYy0TBvGS1/Q4QIppReSHCHaC\nM2hUGaHt8Nwn6dvhmG" +
       "VpL5bRYVmGtUrRExA8kxvwlgErmJrvttzwLqnYOkR+jOAeqJK5QClsD5hs\n" +
       "ljDnDaQU0xi8IQXcKwVA8T0tiwlqX3Ko8105ymnkwzoSSTintSPUslSfH/0Z" +
       "kfsQ3A8akiMl/SHm\ne9xFg3BPvzrgrlV8ZjsWznLqW4j8DMHDLmssdyV9OS" +
       "NTAqx5YmqDGgT51/mu3BzjCbGoF4T/5W0B\nwQP59obgIW9T3soQ7JGEXXmX" +
       "hCblLUlcyaN5/His460g4RFJ2AqmGFhgr5G24rQTgpyIRr7L/8oS\n9/gPsZ" +
       "QfgKSd7TfoIQtNV09EHePrAVWBynxXzilG4GHQZRHuY75XsJJFb3yYJtIauv" +
       "4EiTvTy2E6\ncpMe8SEiLyF4WVDPNxTp4aGf+6nVCmM0ZbLAiJ3eCAR/KhL+" +
       "JwRtLMIC/4nVwnn2GZjGofBSfZXX\nA/44KA3HzfcQK1zUNZu4uJYMeEJ6E8" +
       "FbDP9mw6noONKyyCgjDVJGSlH16BoAEMvwl8O31IbnG88l\nuhF5F8F7CN6X" +
       "hJNhSTLh4fw8YyYgws35PainUKD7JqXOX/JjDY8b4u/MF/MhCgkthto3snbl" +
       "Su/h\nAhHKnxkcAaX7EFERbEawxSvTERHFKAKv/q1FxCtw8xbGCDwNHE1JjG" +
       "A0XyWMwKtHuRGIUhXBd/LV\nsAi+61WwiFyD4FoE13n1KCLXI7gBwTHUZ6QZ" +
       "ke8j2H6MrL7kUg+ROxDceXT7IBsREdUUgrvzlVkI\nZDkkNCTKoQDBXychkP" +
       "WNyDc/RbA7QAiFETnO2XU5Io8ieAzBcUntuxB5AsEvj5uMscsHRJ5G8Myx\n" +
       "Ct8TlPGcVxUg8jyCFxC86OV6RLyI//t8GRzB/nzJG8EJkrn+fwlHk7MRvOGl" +
       "aURElkXw93zpF8Hb\nXtJFJF8GHTu1IvJhHsKEIOEjBB97mTPjoTJXxsZrrl" +
       "yEiPtaR/wF4xjy6Ax2lO+P5QuQwGsn/iJZ\nMpta+E1W4I02f6sQuOM+xYQ2" +
       "SIbleInJKHRxkHAZXmKYCa0PamdMDZyYhDHPZQc7ur+UQcXou5IP\ngp5C0M" +
       "VD+Io3NHSUoscsrc5FxKsjPAIeTrFcOEbhxXKhWC6MV8I4Lxc4AR6+672H7y" +
       "6d0SS+XnAL\nhzwlRMdXoIQAEOo5MXLhF0iSlwdyWzHpFZPe59lgMekVCcWk" +
       "53vjvNJID2o0T6ZbWcx0xYRGigmt\nmNCKCe2rSxjnCQ1A6I08yav9WJJX4d" +
       "ej3v/78tej3uUY/2AYPgMBT2PBdJeXMM7SXTGrFbNaMasV\ns9oJRhj/Wc3/" +
       "p0yZ1dqOJash8lV+JCumomIqKqaiYio6wQjjPxWFduZJRSuKqaiYcUgx4xQz" +
       "TjHj\nnGCE8Z9x/A8/vv/oRMpq8VX6QMsK/k/w85w2LAShaMPi9lCxLd83mb" +
       "L6UfFeWts2flBzg/LMpaIb\nQUOw2UyHnk6dtesgXXBeTTxPt6NKZpiLNDpC" +
       "xXeOsFNLWDTSALGLCraXWMO/je21Gon0Ll9w2sLq\nv4VJyRgNi+qdm+soS1" +
       "u6r38BjFY+dyejWVlKyV5P/UjThZFh9bkwb0EiupbkdCULTmoN9iqpsoIL\n" +
       "5UfYxBdQS0RPqwb4TCHih/9GYj2CBtFwCED4M3G2Y7TYyUt02+XgdSNzmk7x" +
       "v3h2ZOLUxK8bIf9I\nlfOdtdwmMj2WmlKZ/P74LTPvfeOR19dNFDYgerTNy2" +
       "mT5p8j+rS5PUNAwpxCEvjoZ74+58Gr1r02\n6LSQiEQYKRkx1ARuLjIpaNXZ" +
       "F5FyqVj8nAafTkexnfkUm+tIPHvPY6TM5h30Cis2t8kb7+YllLU3\n0vxB+B" +
       "eTm3mDqpJBxZaNNgLd8XKb3wV62vF1VsldTYXPrCPs6uO8BoBlYqQ5EyLY9S" +
       "Zy6hg7P1fE\nEdi/RvUkG+ZKbRTNOyKTwU5gtYjOy+S4eq6V8e/lYw8Xlyb6" +
       "P6lGVPYeBGJueycUUCGWy2V9QfP3\ntZ+KnIH/My7bBAT//C+6EmUKHjXy4N" +
       "2/Ik3ON9e4NebYZMinnMAd/F98pwkcds6Zku0GTtvB+Nz9\nm+Y/bdb/VliN" +
       "2xqwHBtZpDXN3wPJh5eZFh1S+eGXi45IJl9rGyO1WZ3oGKlwUa7nFWJkB+81" +
       "hiPx\nqpPbyNJM6D9ck67o0VIAAA==");
}
