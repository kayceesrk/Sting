package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi3 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1aXWxURRSeblugLbRVioj8hZ+Wn+CuKBCh5XcpICylugUV" +
       "FLi9d7q99O69672z\npfUHFWKCJqJEgYQQeRF5EB8wMfrmk2miiQ8+GHzwCR" +
       "8k0QdjjA9i4nxz787cu2wXtFRrsn349syc\nmXPOnDlzzrlJr/xMaj2XLPao" +
       "55mOfSTOhnPUi2PoU+mdacp6OHlIH/m242ObzbkeI1UpMjlLs73U\n9Ri5L3" +
       "VEG9QSeWZaiaRjWVRnXFL7kEuWlpUqWL5k4v9VCcm1er9pGYwsTRW2J8SmhN" +
       "yeiGyHpkVF\nmoBO7xFuCnT5Sk5PTl9+MvPu+zFC+I4ZOccazlgOC3b4aza3" +
       "fro39ePn8/01s0us2SOkHtJvrpkz\n95vNP9RVw+QpOcczcWpG7k8VNvke6Q" +
       "447UO53FCOu7oN3oqDF1fe8hbvtbOOYfaZWq9FU6bHbja1\nrvzkp1PNwiU1" +
       "Fp9hpDnkaazByVfcXpyan7WFvPLVwd/nCaFV+nPkGKkSRrUoKSnTHqAGxE9d" +
       "ln52\n5+GTC6u5M3JHa3BBfOmScpe6hWbMUnc6iWluhvIj3OsfwdLsTCLNXN" +
       "PO4BBtZWSGRK6zzo1c6v7l\nJG4HtsdyQ175KHuC6tQcpMUW4XJXl9m2m9Na" +
       "hjsum83bpq4xGamXV5+4dvZ44rcYqT5AplCLZqnN\n+COYdyByME0f6HE1nX" +
       "b6C9pTpCHry4QYRmaoKPEjWgSy9BMO1yyPiN/p4qitZR+Ubdx6TuxtCcnA\n" +
       "7yweiHm3YHIcJsdvNTm2bXlHx58L10JKjovr4Ppnl9mg1X1xvm3N96/FSM1j" +
       "pN4ybdqVR4ZIkUaD\n6paGu05amidMaOZvps+0aJeWpcG4PktZv2PIGSitHe" +
       "FBr3T29LtU45mhwSfi4DAyjTtMWSPEz+f2\nzmb/8IoZWVCOHWid1GHaJtsg" +
       "1TWxO7wdRu4JD4U4CFkiRW0AsQywnJEp3gCPMsbfdIFdr9iAFZKx\nCkQroI" +
       "1F4k2u2B9eUWc7Bk1ajq34X7M7z9mMtBTNBJ6pYy4tlgviEYCwcfW/yrimGI" +
       "zU6E5uWLLO\nsDurfDwKQyN5ZSuloAsgxL1tHFU5oEMylrJQ0dI8v8TgRgoF" +
       "q4vTQbSoYeDiGha+1fUslECLRQVW\nF6S1FM3Is3RKcZtYKMcPmrx8cTHdrs" +
       "Mc3bG28qfM35ntWRpzXEbmjsYKLJ3JghkaXSi1PQRiJ2AX\nDx2LaoM0qVmK" +
       "f4r7SZZh35pO13XcHZpt8CST2YcpWDKr1LQ8XldBYFUziG7A44zUCoVS2VV+" +
       "oCJl\nSV7U6RCTaqZHJ6SCtFQQY+SBIiHwtZTQHBoFXpoulnUaGX4rewap65" +
       "qGMuo7EE8BnuYekisl/yO+\nX+rD9RfuuiESM/ViZxK9ldx6A8RBwKGCaBR+" +
       "yd/I+5mIaJHAtzjGsHgToVEhFyslLlUp6w91BMD+\nUmcDPKMOpSwDHJaMiy" +
       "VNQkgpk/yRvJq+8bDjRpTRKxnDPBQjBqadvKvTbbza+c85NLwrJl4NX2Kt\n" +
       "uADJezQc0H0uQtc24kHwdXNXcZeFRsEtVrt5eXexHhbqg6WItN5PjbxFeTw3" +
       "SjrYPplvhzT5In4F\n8RLgmM/d7mjyhVd9FuY2aIzRbI5FVlxQKwAnJjjDGo" +
       "2xLHzSJu6HHoc3GDle3s1Qff8wnD+kw7HQ\nFP6eViAL7tb9sRTwBoi3AG8z" +
       "MjXgIuDkjZCjvBOXOrKaacd3c+A5AD+B3FqPN6MqlLpAnAGcBZyT\njJncJF" +
       "kvsL/EmkYQ5wWDVy8o5N3knKLPq26L9287HMugLm+0r3/5Yueu10/63z+26g" +
       "ojHaH/CcG7\nsd15i5kP57FmrmhWA821IyAWABYCFqn+EITfCwFU+9VUvgmK" +
       "fFKIghqZGa2fuwT4AKAatvrRGKui\ntiVKNXAA1YA8CKLQ5Phf0BOu+wKoBk" +
       "iEkmqZxtRLgUgCtgJUK7MexHbAjihjEwi/6wCkSrUjANk2\nkFMg/LYhwgj3" +
       "EwDZB/h5eS9gX4RRFQMxzlVIuM4AiHczLiXwIogBgDVuOkYvsyBcgDdW5Vej" +
       "Oo6q\n6glCKHoe8IKqiSBUhn+5VKUDvFqqyAEmSKUan9oGeFOVMxB+NQKcLl" +
       "WmAO+o4gSiVKUZvQSBOF+C\n0RhliGO9d2u5aB9LuQChPsdLfsYDitL+bT7g" +
       "AX8v7QOuVFJ7JbXfDeWV1F5J7f/n1L5SpvZ1Y03t\nqj//zzp6wLik9koGr2" +
       "TwSgavZHAykTP42koGryRqUknUlURdSdRk4iXqVTJji382afwLcNUpNVIm\n" +
       "AAA="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 7103);
            ss.addParticipant("Multi2", "localhost", 7102);
            ss.addParticipant("Multi4", "localhost", 7104);
            SJSocketGroup ps = null;
            try {
                ps = ss.accept("Multi1");
                System.out.println("Multi3: connected to all participants");
                String str4 = (String) SJRuntime.receive("Multi4", ps);
                System.out.println("Multi3 received from Multi4: " + str4);
                String str1 = (String) SJRuntime.receive("Multi1", ps);
                System.out.println("Multi3 received from Multi1: " + str1);
                SJRuntime.pass("Hello, Multi1 from Multi3", "Multi1", ps);
                String str2 = (String) SJRuntime.receive("Multi2", ps);
                System.out.println("Multi3 received from Multi2: " + str2);
                SJRuntime.pass("Hello, Multi2 from Multi3", "Multi2", ps);
            }
            finally {
                SJRuntime.close(ps);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Multi3 a = new Multi3();
        a.run();
    }
    
    public Multi3() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1320939668000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ba5AcRbl37/0g98glQC458rhAosmeQEIBFwjJkZALm8tx" +
       "ewS4iJe52b67SWZn\nxpnezR6FSEAgQhlFEl4ViT9ASoUqE0SwULRKMCoqZS" +
       "hDqOLxA4tHCZaUpaQU0O/rnul57N4mEIIR\n96r2u6/n6/766+7vNbtfP/Q2" +
       "qXJsknCo42imsTnBJizqcGiObKYqcxKptf2K7dB0j644ziAQhtVN\nH2gb9r" +
       "bcsCZOYkOk1TBX6JriDI7bZnZsfHBcc/I2mW2Z+sSYbjKXYwGP8+e9t/W3t6" +
       "xtryBNQ6RJ\nM1JMYZraYxqM5tkQaczQzAi1nRXpNE0PkRaD0nSK2pqia9dA" +
       "R9OAiR1tzFBY1qbOAHVMPYcdW52s\nRW0+p/cwSRpV03CYnVWZaTuMNCc3Kz" +
       "mlK8s0vSupOaw7SapHNaqnnS+S60g8SapGdWUMOk5Peqvo\n4hy7VuNz6F6v" +
       "gZj2qKJSb0jlFs1IM3JadIRcceel0AGG1mQoGzflVJWGAg9IqxBJV4yxrhSz" +
       "NWMM\nulaZWZiFkRmTMoVOtZaiblHG6DAjp0T79QsS9Krj24JDGJkW7cY5wZ" +
       "nNiJxZ4LTWVze+f2v/u7Ph\nxEHmNFV1lL8aBnVEBg3QUWpTQ6Vi4OFsYmfv" +
       "VdmZcUKg87RIZ9FnxfzHLk++8bPTRJ/2In3Wc10c\nVt87Z+asAyv+VFeBYt" +
       "RapqOhKoRWzk+136V05y3Q7umSIxITHvHnA7+86vrv0T/HSW0vqVZNPZsx\n" +
       "ekkdNdI9Ll4DeFIzqHi6fnTUoayXVOr8UbXJ27Ado5pOcTuqALcUNs7xvEUI" +
       "qYFPDD7nE/FXj4CR\nunVZnWlnJ5zNjMxn1GFOl2OrXVbWTmdpVwaJlgJK3C" +
       "X75ZHnlK2xGCxnZtS0dNDDNaaepvaw+uCr\nv7l21aVf3S4OCpXLlQY2SfBP" +
       "+PwTgj+JxTjjqaiDYo9W2LYygbaR33Zg1j37lW/BjsPKHe0ayhcW\n21qJEA" +
       "adVdJ19PiG1wuYAnoxrLZd/8aMe5/77tNxUlHUfSTlw9WmnVF01ADPXlrd6a" +
       "IUUJzOqPoW\nm/svt6575OAzLy3wFZmRzgL7KhyJ9jE3uvG2qdI0+B+f/V3/" +
       "XPPXO6rOezROKsHowO0wBZQFbLgj\nOkfITro9n4NrqUiShtGChdcz2KCtgQ" +
       "UjbETQLJQDzqItIiB3V4dvrP7c8z9peJqv2PNsTQEXmKJM\n2EmLf/6DNqXw" +
       "/KW7++/Y9fYtGyvAMi1LnDkj1VZ2RNfUPAw5OWxaKF4a1eatfd3NOxY7P+JH" +
       "XKdl\nMlmmjOgUXLGi6+ZWmh5m3Be1BPwedzewE40j4LbAAw7rwEis1YrlQD" +
       "2LOIbEKW0771y4+3l0HRbf\nmOloYlxSkucPOmMIFxQQsT0LNbjNXzYY+xax" +
       "gMaFqavXbto+l688yG6R28hLhr4tLClpC6sxxIDO\nMBN8ja8ym247dNm6K6" +
       "/oEt7vzJIs+sCe05yPP35Xr7H8yWkHjThqSbWzWdp8ksQdxsiCpMfS1Txs\n" +
       "Ciy1NiVIXgRYdOQF+BNr1/5j275ndzXCxEOkRnNWa4ai42k7fcJ6i0SHCItr" +
       "nrz8vsO/Yy9z3fTN\nBcWfmS/0dBuUgCWfezDXUv2DPZk4qRkizTzCKwbboO" +
       "hZ1NwhiNFOj/swSU4K0cPxVgSXbukOZkZN\nNTBt1FArA7tdib0RrxW2yfs0" +
       "/Vv8fYAfVBxsiCjQ2mNmLAgd9uxLKCxaYTRt5WMxC4edywd3cDhH\n6F+cwd" +
       "S4xYzUWLaWUzBRwkwkpzGeEfHOUxlpl4doZw2mZSimcK7acU8R4xaxHBS2M3" +
       "LeUjUSqBpM\nhOf9B5ftM9jMV/kh1bh5GSZHvguBgKnDHvKIa4OtleIqFW5Y" +
       "daNiTLgmdVzT0yjZWcBjXklNFMNv\nr0k9eMXYzvvjrjU3WNyNne7bsy+X03" +
       "m5kTHT2qjG/RBY+HtN88989K0dzSIS+L4GrODIDPznp64k\n1z/zhXc7OJuY" +
       "ijx6XFe0SDqGM0rtyEo6phXbkGqm2GOUifgNyyrBI8DifP2u/Q/0v7Pd25SL" +
       "LfTT\npY5kgKpUy9GoBOiNlpYYtg5wSC5BiTNZQ1O5CgoeDy698dCdN3T9HZ" +
       "z/RlJLdZqhBgON6dgYynLB\nyw/akLqsEh3AtBoyguegZ0iRTfDWg//7+Lrm" +
       "l1Q1I124KBy7PsAD/6dAcbK2J18C5UsUyhdf/Zll\ny96fex6PNcDuYpi/vc" +
       "QApe7pe08/56WbIBfoJfWYNPZl0XaSZAo6GgWzfB7zXP9Ri3lkn+9P6sWb\n" +
       "Qp+fwxFStR9eX/w5IWmiCrx2NAgkgRRGToIN86Xh7K8CedvZRzxPRuaUIruz" +
       "Vi/TDI1dKKdrYkd5\nOpAFBJucHTJRJKsLEeGmBWutdbaASjHX3yG53icjGJ" +
       "WEJYgMI9jEQsolewwFe9QZZpr26Kbh03/P\njt6bMdIWeeLuTB2DbCrCF5EM" +
       "AgOB+YkSDvkERipV05qQpF3s6GICaGGgJY9si2S0GxFubBOTTo4g\nJwkLWM" +
       "DpK46I+XginsPvA9zVFr/pbjGPvZLVBSzgLaOsXKk9bm2RJ3ItX/aVJKgEOQ" +
       "3eHgMR9WIw\nZRy9MqvhGxh4uUlprqxTRrDJMyHsIKfpRuQrCG4CndGpkqM9" +
       "kC1L+g7YIJkQCTFW2bZpr1GMNHiX\nsQ34yAQJTi32WK5ru8cw1ozIrQhug9" +
       "yCTygn28vIrMhk7nczcpqp4Qdygq/JCSBlmRFhgpssOTQH\nWu7mTOXdVqXH" +
       "4DjW56hta2lfqBcQ+SaCO2CHZE9Jf5gFXhLw3L1DbggpSz0f2YPphhz6JiJ3" +
       "I7jH\nY41JgqQvx/fnIGvuuVea6QluDIGW54T9SeA1T/L5l78EBDuLrQ3Bnf" +
       "6ifMkQ3CsJe4qKhCrliyRa\n8mi+fTzkeDNM2C0JE6CKIQFTZtZW6WoIc8KO" +
       "A82PRcS9wUOs4gcgaecGFXrURtU10glX+fphq2DL\nAi33FCsghfZYxAdZ4M" +
       "spySKljtN0VkfTnyJxd3gNDEdu0iL+hshjCB4X1EtMRVp47PEgtUFhjGYs\n" +
       "Fuqx2++B4KdlwidC0CcjLAyeWBOc56CJb3WQn2iBBOX7QT8oFcd7/QNf4aGe" +
       "2qiiLRlwJ/0HBAcY\nfpvNqWg4UrPIVnihlHNk4DU6sQ4AfteIuOBbBS+xtm" +
       "8SfYg8j4AH5Rck4WQQSQY8HF+kzxREXuQE\nCL84off+2RzMiTHJhdyQf9F4" +
       "VhZ7XMFTZ5dN1X5ENiL4PIKr/WwVEZGZIfCTwabSKVnobYaH99CT\nybLL1x" +
       "C8jsBPH+snIywJy6YVSycR+OnQYkS8lEu86Z5wuSACPx3bhYifwB1TZofIlx" +
       "Bch8BPrC5A\nZBuCG8IEvo8iFUJwc7EcCYHMZcgOREQuEyIEkxwEMjkRweLr" +
       "CL4RIsTiiBzn0LgckfsQ7EFwXOIy\n5/wAgu8ctzkmj/2IPITg4WOdfG94jr" +
       "1+SEfkEQQ/RPCoH6gR8d31j4uFXwRPFIu8CE6QsPP/Szia\ngIvgWT/GIiJC" +
       "JILnisVOBH/0IyYixcLf5HERkReLEKaECS8jeKUw7PUcS9hDxP+So+iXIwgi" +
       "4esI\nX4sg+HDhC8Hb5RBVDlEfx+TlEFUOUf+rhE9TiDpThqiVxxqi/Pel/9" +
       "obFoLjEqLKkagcicqRqByJ\nTjDCpzMSrShHonLAIeWAUw445YBzghE+TQFn" +
       "iYw83GuuFRVhocpLXi45z60mJghFNbFXCuzYgZ+W\nI1cn+LWPW658p/Fm5a" +
       "mrRVFda7hmepWRzSzdc4guvKhRLVK1X8dMa7FOc1T8CIwFx3FRDwrTLi5Z\n" +
       "JbmO14/5FbMVqeULz1jU8HqcVE5Sd9/iPhygLGsbgTI86K186IL80yKbEpWn" +
       "Jdd+WcW49qs4r6QV\nxbcFF2jCg7rDJbf1dlhQfoTtXIAmTAPgcyp8ZhDxx/" +
       "8jsQVBK++NyhE7LM52kkrxokSv6hvbU5l7\nd4L/6Lkqr1ILf/9F/vFKt4ig" +
       "sBa639YyGpO/TN7ecf9rj7w60CZ0QFwnmldwoyc4Rlwp4qto4NWQ\nc0rNwH" +
       "s/9dk5D1038PKIWwkZe5+RypyppXFx8aawVkcb8Zjc2JPh0wWfpe7GLi22sY" +
       "WGxGP3PDA4\nh1/2Kr2xhfeR+KUUsVn7KjrfiT8xvZPfs6gcURxZLxq6yFV4" +
       "Tyt0/YrLWS9XNQc+ZxxhVa8UVQBM\nEuMd+RjB4u347ElWfoHwI7B+nRpjbJ" +
       "xv6kmiBjXeDHoC0iI6K19g6oVaxusJsRTZo4lrDJqZkNfk\ngFh4SwEniAtx" +
       "+VwfUf0Dtyjii/EnbVneGK4AEMX1+ZJHjTzqOadpbikB18YCnYwFNif0JO/5" +
       "8LOx\n/vuUqBW4F+TUuQc2LfiF1fJroTTeJbYarLzN6nqwkj+AV1s2HdX42d" +
       "dw2GhxUZcx0lJwoYqRer/B\nt7pb9F7Ob81gb2xdxNXk7HzsP7CRagl/OQAA");
}
