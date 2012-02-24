package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client1 {
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
            SJRuntime.pass(new Double(3.14), "client2", ps);
            String str = (String) SJRuntime.receive("client2", ps);
            System.out.println("Client1 received: " + str);
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
        Client1 a = new Client1();
        a.run(1);
    }
    
    public Client1() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1329791065000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0cW2wc1fXurt924lcSQpy3TUhosiYppEACIXZs4rCxTeyE" +
       "YATOePfanmR2Zpm5\na28opTwEAapGQhCgPBoV0Ua8VBIEVCqlRUBDgUKbVi" +
       "GtBD8gCi1PISAqgface2fuzOyOF4eUksBa\n2uMz99x7zn2c18zunAfeIcWW" +
       "SaIWtSzV0DdH2dYUtTg0BjbTOLOiPWu6FdOiiVZNsaxeIPTHN32u\nbthde9" +
       "XqMAn1kTrdWKmpitU7bBrpoeHeYdXKmGR2ytC2DmkGsznm8Dij8dDoC9vWNE" +
       "RIdR+pVvUe\npjA13mrojGZYH6lK0uQANa2ViQRN9JFandJEDzVVRVMvhY6G" +
       "DoItdUhXWNqk1jpqGdoIdqyz0ilq\ncplOY4xUxQ3dYmY6zgzTYqQmtlkZUZ" +
       "rTTNWaY6rFlsVIyaBKtYR1CbmchGOkeFBThqDjlJizimbO\nsbkd26F7hQrT" +
       "NAeVOHWGFG1R9QQjs7JHyBU3nQsdYGhpkrJhQ4oq0hVoIHViSpqiDzX3MFPV" +
       "h6Br\nsZEGKYxMG5MpdCpLKfEtyhDtZ2Rqdr9uQYJe5XxbcAgjk7O7cU5wZt" +
       "OyzsxzWl0lVZ/d0P3JbDhx\nmHOCxjWcfwkMmpk1aB0dpCbV41QMPJiO3txx" +
       "QXp6mBDoPDmrs+iz8oTH1sfe/O0s0achoE8X18X+\n+KGl02fsW/l6eQSnUZ" +
       "YyLBVVwbdyfqrdNmVZJgXaPUVyRGLUIf5u3e8vuOI++q8wKesgJXFDSyf1\n" +
       "DlJO9USrjZcCHlN1Klq7BgctyjpIkcabSgx+DdsxqGoUt6MY8JTChjmeSRFC" +
       "SuETgk+MiL8KBIxM\nalFYfLgHeFqtmkp1tjhqbWbke4xazGq2zHhzKm0m0r" +
       "R5APuBMjQnFKYMasZoc9DIDAqcOBoKwVqn\nZ9udBkq62tAS1OyP73rtucva" +
       "zr3+OnGKqHn2VBk5TkiMOhKjNnsSCnG+9aifYv9WmqayFe0mc+W+\nGT/Zq9" +
       "wFpwG7YqmXUr7o0GgRQhi0JK9baXWNsgMwBXSmPz7pijen3f6Xe58Jk0iga4" +
       "nJxnbDTCoa\naodjS3W2uGwKKFVTtmoHyX73hrUP73/+lfmukjPSlGN7uSPR" +
       "duZm77tpxGkCfJPL/tZ/r37/puLT\nHwmTIjBIcElMAUUC+56ZLcNnQ8scf4" +
       "RricRI5WDOwisYbNCoZ8EIqxDUCN2As5iUNUHuyg5eXXLy\ny49XPsNX7Hi9" +
       "ao977KFM2FCte/69JqXQ/spt3TfteGfbhRGw2lRKnDkjJan0gKbGMzDkOL/Z" +
       "4fQS\nqDZv71lWs32R9Sg/4nI1mUwzZUCj4KYVDVScJvoZ91O1Hp/IXRHsRN" +
       "UAuDRQz34NGIm1pkIjoJ4B\nTiM6ddLNtyy482V0Kym+MVPQ/PhMSYY3NIUQ" +
       "zs8h4vUM1OBJ7rLBEWwRC6ha0HPRmk3XzeUr97Jb\naF9kJEPXFk7JawvtGH" +
       "5AZ5gBfshVmU0/OnDe2o3nNwvPuDgvi04w5wTn447f0aGveGLyfj2MWlJi\n" +
       "bZYmHyNhizEyP+awtDUPLwXWs6ZHkJzosPCLF+AKVi/7+Mo9L+2oAsF9pFS1" +
       "2lVd0fC0rU5hvQGR\nI4vFpU+s/+nBP7JXuW665oLTn57JdXQbFI8ln7Z/pL" +
       "bkoZ3JMCntIzU8+is626BoadTcPojfVqvd\nGCMTfHR/LBaBZ5l0B9OzTdUj" +
       "NttQizy7XYS9ES8Ttsn7VP9H/H2OH1QcvBARoq7VSKYgrJizz6Gw\naIXRRC" +
       "oTCqVw2Gl88EwO5wj9CzMQjVvMSGnKVEcUTKIwSxlRGc+WeOd6RhrkIZppna" +
       "lJiumdrXbc\nU4S4RawAhW3KOm+pGlFUDSZC9979y/fobPpr/JBK7ZwNEyfX" +
       "hUAw1WAPeTQ2wdbycZUK1x+3I2ZI\nuCaISVoCZ7YEeDTm1UQx/MbSnl3nD9" +
       "18T9i25soUd2PzXHt252U1rdeTRkIdVLkfAgs/VH3C4kfe\n3l4jIoHra8AK" +
       "vpiB2358C7ni+Ys/mcnZhOLIo9V2RQulYzgxz460ttAhNWdD0BfMyzPKM+gM" +
       "7da9\nP+/+4DpnG1al0DOfkPcQ9ESQwFPzjFkLOOSZoLPJtK7GucYJHrtOvf" +
       "rALVc1fwS+/kJSRjWahKQC\nFGTmhb6EF5x6rwmJSpvoAJZUmRQ8ex27Af/F" +
       "FHOIMpGyOIvB/518Ufk0ax2NU3WE5q4Lh3d52Ljs\nFuRh15Vmo6CQAfzy72" +
       "3MMFJizJY1d/c9ueZgTCjYgJHYKpTbXlbKM5GT8k9kwATfMxw0k3yK1RI4\n" +
       "Cnx1nWqtoinQATgGbauI2WB+CgxmpN5j1qsVa3itklomp0z8WYJNLy79+5NP" +
       "Tdn05wgJt5MKzVAS\n7QrPnUg5JC3UGobUNJNacTafQtVoGcAadGjAbLKcP2" +
       "cI26cMUM0z2w5SNgxSWo0EJA8RTRnAqSyF\nmcy+dxojRW0b2jqdfYTs1tG5" +
       "KOpcNFfnwu0nLV/+2dzTeboAElbDFBryDFDKn7l93tJXroF0rgNW\nBrGpM4" +
       "3uL0YmYqxQ8CaOpy12CCjD24RONyRUiBvBTjcLJ6R4L9ydujIh76UK3FVWCi" +
       "SKFEYmgBG4\ns+Hsh2C+DexL2igjc/KRbakly1VdZWdJcdVsnG4EEjnvJWeH" +
       "TAzJ6ixELkEAyXCZtQXcBLNDFpIr\nXDICJgmnIJJEALeAXoche/R5e5TroC" +
       "itmqG79BfZ+AMS3LZltdg7U84gIfbzPYDI9xFcBooYN1Jb\nJWk+88QvxRLp" +
       "C87MiV2dgNu75l7aongaIVmd7j2CbFbCwh1m9f4Gm92UhGopyQF1KA0JgytO" +
       "CjjE\nyIxsI3QSk/VwafE98bXI493mcBEZxfUIbvARSFO+rUBf6dkK9zJXAj" +
       "k0hoQxRX+tBPJd5gnf2eu2\nUytn6ZOyWg5j9WSjV7tHVEtlnmxvFfgoZNuS" +
       "VvHhAITkMWm2tkwcwEuepWMHKWYZIncguBOMQaPK\nCG2FOzlJ3w7HLJN1MY" +
       "020zTM1YqeALc5tAGbDJjB8UHNcsE75cbWIPIzBHdD3ssFSmG7QWWzhNnP\n" +
       "FKWYen+DFHCPFADp9LQsJrj7kkON58renHrerS0xBOfUNUJNU/XY0d8QuRfB" +
       "fbBDsqekP8g8N7Co\nEM7pV/rMtYKPbMVUWA59C5FfInjIYY0JrKSvYGSqjz" +
       "UPSS2QcCD/Gs+VE11cISZ1nfCn7hIQ3B+0\nNgQPuotyZ4ZgtyTsDJwSqpQ7" +
       "JXElj+axr2Ieb/kJD0vCVlBF3wR7jLQZp+3g5IQ38lz+T6a423uI\nxfwAJO" +
       "00r0IPmqi6eiJqK183bBVsmefKPsUI3N45LMK9zPNQVbLoiQ/TRFpD058ocX" +
       "t4KQxHbtIi\nPkTkRQQvCeo5hiItPPQrL7VSYYwmU8zX4063B4K/Fgj/F4I2" +
       "FmGB98Sq4Tx7DQzjkHipnszrfq8f\nlIrjxHvwFQ7qqE1cXEsGPCC9ieAtht" +
       "/CcCoajtQsMspInZSRVFQ9uhYA+DL8Z/MttuDWzzWJTkTe\nRfAegvcl4TiY" +
       "kgx4OD6gz0REuDq/B/kUCnSejdR4k33M3nFB/Cn4Et5FIaHFkPVGulatcm8r" +
       "EKH8\nbsEWULwXERXBZgRb3AQdEZGMInDz32pE3AQ3MDFG4O7AeFJiBKNBmT" +
       "ACNx/lSiBSVQQ/CMphEfzQ\nzWARuRLBVQiudvNRRK5BcC2CI8jPSBMiP0aw" +
       "/QhZfc2pHiK3IrhtfOsgGxER2RSCu4LSLAQyHRI7\nJNIhH8GbJyGQ+Y2IN7" +
       "9AsMtHCIUR+Yqj6wpEHkHwKIKvJLTvRORxBL/5ymSMnT4g8hSCp49U+G6/\n" +
       "jGfdrACR5xA8j+AFN9Yj4nr8PwVFcAT7goI3gqMkcn17CeOJ2QjecMM0IiLK" +
       "IvhnUPhF8LYbdBEJ\niqBjh1ZEPgwgTPQTPkLwsRs5MzJCxo7VCLkIEedhjv" +
       "gm4gii54z8z83cR8TysYfvYRN/ViyZ1bHD\neE7NyOTsJufuJbResizFy/MR" +
       "bPQTLsbLHgS9/v0Zcw+OTsKYJ7ODje87L8gUPVfyBtDdEDTtEH5b\nFaLjFD" +
       "1mSnUmIm7+4BIuQKSQJhTShEKa8O0kHONpAifATXete9PdoTM6hI8VnNxBpg" +
       "5t34DUAUCo\n6+iIgF8iNF7ii2iFUFcIdYezwEKoKxAKoc7zfHmVkR7QaECk" +
       "W1WIdIWARgoBrRDQCgHtm0s4xgMa\ngNDrAcGr9UiCV/5f4vl+wMt/FeVrGe" +
       "NHheHFCJZgkz/oBRKOsaBXiG2F2FaIbYXYdpQRjv3Y5v36\nUsa2liOJbYh8" +
       "k2/MCqGoEIoKoagQio4ywrEfikJ3BISilYVQVIg4pBBxChGnEHGOMsKxH3F8" +
       "v93E\n6zXipXhfuQn+c/dGu4QKQShKqDj1TyzT885SVi0pXgdr28YPqq5Vnr" +
       "5IvKhe5y8U06ank6fuPEAX\nnF0VD6hUVM6M1CKNjlDxdhFWWQmLIhggdlHe" +
       "0hBr+RvXbpmQSM+KBScurPxHmBSNUWyo1m5cR1na\n1D3FCKC3cthViGZlbU" +
       "r2fGpHGs6LDKvPhnn5EFFxJKeimH/QMn+dkQrTP1F+hA18AtWwRfh6fSN8\n" +
       "phHxx/8jsRZBnSgWBCD8qTjbrPI4bjWb7Not3aaaVJl8HnvjzHveePi1dZPE" +
       "8YnSaI051cm8Y0R5\nNKdUB0iYk08C7/30d+Y8cPm6VwfsOg4RwkhE1VlwZR" +
       "9n6nhdz+xyVvy72LZMnKbwtSfOpNR9dy5S\nKQpCRCYyUjRiqAlkGZni0XTe" +
       "MCET3BIpktuOnxPh025ve3vQtueaGY/ojYyUWLw2Xv6F5ZZv43W6\nxHnsiT" +
       "R9EP71lCZeeqpoQLFkTQ1f3bvcsna+anV8nhVyVcfDZ9YXrOrjwAPA1DHSlA" +
       "kRrGcTmTfG\nys8UXgbWr1F9iJdVi0xA0JjJsfzcY+Uv5GM5FocmSjmpRlSW" +
       "EQRibqUmFFAs5sdljW0N460kFTkZ\nfzYu6wP4fwkgCgxl8p4t8qjnnBrsV9" +
       "a4IgarY2OOOjZmYA9L7XpuWARnarZp2RUE43P3bZr/VKr2\nD0JNnCp/pVi7" +
       "Iq1p3nJGHrwkZdJBlZ92qShulOJzXcFIdVZROUbKHJTv81miZwsvG4Y98aqV" +
       "K8XS\nTOi/Woqe/pxSAAA=");
}
