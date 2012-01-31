package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Carol {
    final private SJGProtocol AliceBob = null;
    final private SJProtocol carol =
      new SJProtocol(
      ("H4sIAAAAAAAAAM1XTWxUVRS+M+0AbaGt8idQQPkpYHBGDBCw5a+liDCUhimo" +
       "oMDjze30wZv3hvfu\nlDb+xGiMBSNKAmgMCS40LNQFJkZ3rkwTTVy4MLhwhQ" +
       "tJdGGMcSEmnu/93PveMDMQoQldfD33nXt+\n7jnn3nPm099ZynXYMpe7rmFb" +
       "x9NirMTdNJY+lduV42KQyCP6xI/dn1ui43qSJbJsapEXj3HHFWxO\n9rg2om" +
       "XKwjAzvbZpcl2Qpq5Rh62sq9Vj+ZqZ/5fwNKf0YcPMC7YyG4pnPKGMFM/ExG" +
       "FpaYUloH3s\nOLkCW76Rc1NzV54pnP8oyRhJzC7Z5ljBtEUg4e/ZtvzL/dlf" +
       "v17s71lQZc9eT+sR/eb6joU/bPul\nqQEuTyvZroFTC/ZQNhTyIzIQcLpGS6" +
       "XREoW6E9FKg5dW0XKX7beKdt4YMrRjJs8arrjZtnzNF7+d\nbfdC0mjSF8Ha" +
       "I5HGHpx89e3Vqe/zetir3x3+e5GnNKGfZK+whOfULKUla1gneB7qp6/KvbDr" +
       "6PiS\nBgpG6VQjEkRbV9RLag8vGLfkFKHsrCMVEXrSfG/i44E/xiEE75KlUb" +
       "d+He3jOjdGeDWb6+qI7SFa\nK1BoisWyZeiakLV4Zd3r1y6+lvkryRoOsWnc" +
       "5EVuCSrzRYf86JuaVcjkhKafGHQ0nff5G7qyrKXo\n64QawWarOvBr1ivVLJ" +
       "siNKfAKZkPxtQ5hlXoihx5ed2rY+VvPS9kZ1KJlZ3Q1TR0p291Nbnj0e7u\n" +
       "f5dshFSJxDeSvQV1BLSmbz7oXP/zG0nW+DRrNg2L95dx97OsNc91U4Pvvabm" +
       "unBhDt2GIcPk/VqR\nB+vmIhfDdl5+gdHUBJWzsjk47HCN7nyLT6TBEWwGBU" +
       "p546nvIH8XiP+ZWsEeqccOrE7pNixDbJbm\n2sQdZkOwB6JLTx2ULJOqNoPo" +
       "BKwQbJp7gqpL0G0N2c2KDVglGWtBLAEsFbE6kzsORnc0WXae95q2\npfjfiz" +
       "t/jQWbVfEliEyTcHilXhCPA9YAnrg945piCNao26Uxybog7qwRUelEVjLOaa" +
       "noEogNgI01\njQPWS8ZKEekhmuu/+Ahj2D/6iQ5SrJZBXBpFNBWbROS1q1QV" +
       "eB1qm1XxRZ5li1S3VUSe3BGDugmp\nGXBsYeu2uZ3uH10OyzU1YTuCLazFCj" +
       "ydK4IvPL5RWvNisx3QR/k2uTbCezVT8c9SnGRX9L3pcxzb\n2alZeXoZCgfw" +
       "CZ7Mq/ZZHm9nqDDRDmIXYLdgKc+gNHaVDlRhrJd6LB8V0szM+AdpYI80kBRs" +
       "foUS\nxFpqaI+sgijN9Lb15QuUlb0j3HGMvHLqJxD7ADmKkNwp+Z+RvLSH9I" +
       "e5bonVTLMn2YtRR4reAPEs\n4LlQNfqw5G+h8SKm2nt1e+z8mHcnIqvwAVVG" +
       "HK7emX/UEQCD1c4GOKAOpTwDHJSMy1VdQkkpl/yV\nTM3RyfDjRpzxvGSMUS" +
       "nGHMzZZUfnO6hF+dc5srwnLl6NJjHlJUDyNkQLeshB6Vr5dFB8AxQqCllk\n" +
       "FWSxwSnL3CUHRWQslSpy+jDPl01O9dwq6UB8KolDm7wRf4I4BRj1uU/Zmrzh" +
       "ia+i3BZNCF4sidiO\nS2oH4OX7nGHWYqyKnrSN4jBo01RQop5sRJryJ9H3Qw" +
       "YcGw0v3jNCMgy37q+lgjMgTgPOCDY94KLg\nZEbYKRoHpY2iZljpPQT0BuBf" +
       "oDfl0uSoSqkfxDuAdwHnJGMuuST7BeSr7GkFcd5jUPeCQRoBOyp+\n7QyYNH" +
       "TttM08d2gqvv7tS327T4/7P0csNcrFxjh/jiVfNdPQeRlb5nsDZmA4NQFiEW" +
       "Ax4GE104Hw\n5xeAGpna6g8usfHf66exL7VmsMuADwFqyGquxVgb9211taEL" +
       "oOaPx0CEM47/e/beTEwANbRcAKHG\nnLuaf0BsAnjxUeOH920boCfO2ArCnx" +
       "QAO6qNEADZ6tlZEH6rjzGiMwBA9m7/Ld0LGIgxEkkQk9w5\ntoA4DDgCmJS2" +
       "5ZWbd5WGJs1G7dYIwnsdi3dr/GrcxknV8UB4KwEoqz4GQr3K8kdAtDsBXqzW" +
       "mAD3\nSXeZnH4EeFO1IBB+BwG8Va21AN5WDQVEte5Qu22AOF+F0RpnXAS8Hz" +
       "7xnqOt/wEFYvB2RRQAAA=="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(carol, 8889);
            SJSocket aliceSocket = null;
            try {
                aliceSocket = ss.accept();
                System.out.println("Carol accepted Alice\'s connection");
                {
                    String recvd =
                      (String) SJRuntime.receive("alice", aliceSocket);
                    System.out.println("Carol received: " + recvd + "\n");
                    SJRuntime.pass("Hello, Alice from Carol", "alice",
                                   aliceSocket);
                }
            }
            finally {
                SJRuntime.close(aliceSocket);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Carol c = new Carol();
        c.run();
    }
    
    public Carol() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1318557935000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0abWwdxXHv+fuD+CMmhHw4JDGQtMkziRIKcSA4rk0cHMfY" +
       "TgBTcM536+dL7t0d\nd/ucZ0QpHwVSUNNSEr6Ukv6AorYgNaEUKlpaqdC0pS" +
       "1qUEOQ+PhBxYcKVVHVglqgndm92/t4zy8p\nkKoStfTGcze7s7MzszOzu/fQ" +
       "26TCc0nao55n2Nb2NJtyqMehPbadasxLD20cUF2P6l2m6nnDQBjV\ntn1obD" +
       "3QdMOGFFFGSLNld5qG6g1PuHYuMzE8YXh5l5zm2OZUxrSZz7GAx5rF7+/8zS" +
       "0b55aRhhHS\nYFhDTGWG1mVbjObZCKnP0uwYdb1OXaf6CGmyKNWHqGuopnE1" +
       "NLQtGNgzMpbKci71Bqlnm5PYsNnL\nOdTlYwYv+0i9Zlsec3Mas12Pkca+7e" +
       "qk2p5jhtneZ3iso49UjhvU1L2ryLUk1Ucqxk01Aw1n9QWz\naOcc23vwPTSv" +
       "NUBMd1zVaNClfIdh6YwsSPaQM267CBpA16osZRO2HKrcUuEFaRYimaqVaR9i" +
       "rmFl\noGmFnYNRGJkzLVNoVO2o2g41Q0cZmZ1sNyBI0KqGqwW7MHJyshnnBD" +
       "abk7BZxFqbK+s/uHXg3dPA\n4iCzTjUT5a+ETq2JToN0nLrU0qjo+F4uvaf3" +
       "sty8FCHQ+OREY9Gm8/THtvS98dMFos3cIm02c18c\n1d4/e978w51/rClDMa" +
       "od2zPQFWIz51Yd8CkdeQe8e5bkiMR0QPzZ4C8uu+679E8pUt1LKjXbzGWt\n" +
       "XlJDLb3Lx6sA7zMsKt5uHh/3KOsl5SZ/VWnzZ1DHuGFSVEcF4I7KJjiedwgh" +
       "VfBT4Pc5Iv5qETBS\n3pP2tjPSyqjHvHbP1dpVSzfadWrSDHfudmyQRz4zdi" +
       "oKTGFecjmZ4HsbbFOn7qj24Ku/vqb7oq/s\nEsZBh/IlAFsj43TION2lurZJ" +
       "FIVznYlOJ5TS6brqFC6G/PWH599zSP0mqBim6hlXUz4TZWc5Qui0\nsmSs6A" +
       "pXWi9gKjjCqNZy3Rtz7n3uO0+nSFnReNEnX/bYblY10eTBAmn2h0tSwFPakv" +
       "5abOw/37rp\nkSPPvLQk9FxG2goWVGFPXBCLklp3bY3qEHBC9nf9Y8Nf7qg4" +
       "99EUKYdVBnGGqeAdsGhbk2PEFkZH\nEGRwLmV9pG68YOK1DBS0MzJhhPUIGo" +
       "VngC1aEgLy+PTejZVnPf/juqf5jINQ1hCJeUOUiYXRFNp/\n2KUU3r9098Ad" +
       "e9++5fIyWIqOI2zOSKWTGzMNLQ9dTomvJRRPR7d562BH4+7l3g+5iWuMbDbH" +
       "1DGT\nQuxVTdPeSfVRxoNPUyTQ8fgCmqgfgzgFIW/UBEZiro4yCe5ZJBKkZ7" +
       "fsuXPpvucxVjhcMbNwTXFJ\nSZ6/aFMQLikg4vN89OCWcNqwuneICdQvHbpi" +
       "47Zdi/jMo+yW+Q95yTDF36SA07KSa6EHc0roKsY1\nf7/+4LN761MkNUKqDK" +
       "/HsFQTleX1C+cvEk0TLK5+cst97/2WvcxNG3obCjYvXxgltqqRhXDOkcmm\n" +
       "yu/vz6ZI1Qhp5BlRtdhW1cyh4Ucgp3ld/ss+clKMHs9PIhh3yNU0L+npkWGT" +
       "fh5GJ8CxNeLVwrV5\nm4Z/ib8P8Yd6xwcRNZu77KwDodY97UIKk1YZ1Z28oj" +
       "jYbTXv3MrhQmG+FIOhUcWMVDmuMaliYUGq\nQdMaXW+P8aYzQXppQjdnMSNL" +
       "wXAXwkJnNmQEvs54qFx1bEsHnUKVb7vt6MWbLr2kXSS2FSVZ9INi\n9IS59/" +
       "Za6548+YiVwnhQ6W2PKC/lMUaW9AUsfc3jo8CGNg4Jkkju2OssBGcHGltfRG" +
       "OIr0HQAZqq\n0DBVBGqaW0RNCS3hv25QVVtinlKoNArFRM4/dGTtQYvNe5V7" +
       "cpVf7GHFFYYpyMImKIencRfWcymu\ncqqjmp9qFRH+tAnD1FGyTuCxuKQFRP" +
       "fbq4YevCSz5/6UHzHqHB4qzwhjRiiX17bFytq6MW7wWAdR\n5P2G01c8+tbu" +
       "RpFtwngGoeLYDML3p64n1z1z5butnI2iIY9eP9wtk4n4zFIaWU8zRoFC0AvP" +
       "KNEr\n0mmNedehBwbe2RWoYaOD0b+UEQapRo1JWmzM1SW6bQIcalRY29mcZW" +
       "i8ShE8Hlx949E7b2j/G6SU\ny0k1lDBZajHwkdbLY8Uy5I5hF6qhbtEAIk5d" +
       "VvAcDuILLB6muhnKRF0Uzuf0kk5l6YWTwb5bwCVy\nbiBHGuVIF8qR6vnM2r" +
       "UfLDqXZyro3gnjzS3RQa15+t4zzn7pJqgkekkt1pj9OVwVfWQGxlkVNwU8\n" +
       "Y/oRoBrLzv4wItSKjUV/WP4RUnEIdjvhmFByURV2KXUCSSOFkZNAMaE0nP0X" +
       "QN657CPajZGFpcj+\nqJVrDctg58vhGthxWgNqiOgjZ4dMdMnqfETGEWQg4H" +
       "s7wHUYrLWAXBuSERiSsAqRMQQwSNSJZIuR\naIsay9Zpl2lbIf137PjjFCMt" +
       "iTe+ZmoY1GIJvojwsH0VAvfYhKMhAfYbmu1MSdJednwhGlwn8iT1\nHCpyHy" +
       "J8RUxNOziCSUlYwiIxWPVEnYJqDOJvP+C+icNHXy+8XpCszmORUJZk5UsdcG" +
       "tJvJFz+VJo\n2ajlJg3YIUYS3Odh/WHv9TkDd1wQgqal+bLOGMNHns6xgRym" +
       "A5EvI7gJDG1SdZJ2QYEs6btBQbKI\nE2J0u67tboB9HISEzFZ8ZYMEpxZ7Le" +
       "e1K2CoNCJyK4LbIKXzAeVgBxiZnxjMP3+Rw8yMv5ADfFUO\nAGXWnAQTVLLk" +
       "0Bh58pUzkzfr1jNgjs2T1HUNPRTqBUS+geAO0JBsKekPs8i+AO0eGLku5iy1" +
       "vGcX\nZn/Z9U1E7kZwT8Aac7akr2Nkdow1D7frbX2KL4bIUxA5w0FgZyf5/D" +
       "OcAoI9xeaG4M5wUqFkCO6V\nhP1FRUKXCkUST9I03zoRcrwZJ+yThClwxZiA" +
       "Q3bO1WgP5CaxjiOPn4iIB6JGrOAGkLRzog497qLr\nWnrad74BUBWoLPLkW7" +
       "EMKtqARWqYRQ6gJIshbQL2/SYu/RkS97tXQXfkJlfEXxF5DMHjgnqhrcoV\n" +
       "rjwepdapjNGsw2It9oUtEPzk/4T/CsGcjrA0arEGsOewjTtRKCqMSFXxvWgc" +
       "lI4TbFkhVgRo4Daa\neJYMeJD+PYLDDE+sORUXjvQsshM2wXKMLGz905sAQC" +
       "zDfz7fCtg8uuGS6EfkeQQ8Kb8gCaeASDLh\nYf8ibWYg8iInQPrFAYPtYGO0" +
       "kMXKFAZWcWedwwYjvNz1uVQcQuRKBKMItoUVJiKimkIQBuqG0mVU\nbKfBs3" +
       "vszXQV4WsIXkcQlny10xFWxWULM/RInBA6wHJEgopL7Ds/mfoNQVhC7UUkLL" +
       "o+VjWGyBcR\nXIsgLIbOQ+R6BDfECXzyonxBcHOxugaBrD/IbkRE/REjRAsT" +
       "BLKgEAH+awi+HiMo/HjiBKezdYjc\nh2A/ghOSSznnBxB8+4SNMX2+RuQhBA" +
       "9/3MEPxMc4EKZhRB5B8AMEj4bJFZEwxP6oWMpE8ESxbIng\nfyRVfHoJx5Mk" +
       "ETwb5kVERFpD8FyxfIfgD2GWQ6RYypo+lyHyYhHCjDjhZQSvBKmKx8oBcawf" +
       "Owfl\nJ5mL/csEglBcJgQ3AZ4bKTMTV6X8mveWS9+pv1l96gpx+tUcvzLptn" +
       "LZ1fuP0qUX1GtFbuxqmO0s\nN+kkFSknft+wvOQB5iZ+ABQeH5cNrVt65rK6" +
       "11OkfJprtyb/5SBlOdeKnJdBa/U/vo9bkFBKUp6m\nybkXl00Yv0zxmwBxeV" +
       "BwYR7v1BG/Mqh144JyE87lAjSAiqrh1wy/2f6JHf+PxCYEzeLaDIAi7Dnd\n" +
       "RVFRYnABis9QAM0MC6DuvEYdrAWRv7LK31AU3uUMuEbWYLJMub31/tceeXWw" +
       "RfiA+HxgccENfrSP\n+IQgOJWGERaWGoG3fuqzCx+6dvDlMf/oUlkOddykbe" +
       "g4OWVd3KuTD8oKqVj8nQm/lb5iVxZTbOFC\n4hl7MSOVHv+4o7RiC78/4HfS" +
       "QlkHy9reST0xq41fs5aPqZ482I19uFH4XUbscwsuZ62c1anwW3CM\nWb1S1A" +
       "FWoHr68wovq5SBaWa+RsQRmL9JoUKe4Eo9zxHKvQD8BKRFdFPh1WKhl/EDQb" +
       "wlCGjiFtOw\n0/KzGCDmi4mrrBTi8rE+ovtHLlGVK7C+leeT8d2AuBzMlzQ1" +
       "8ljDOXX72wrujQU+qUSUE3uDt1P8\nQwa8mZmdXAT+9zDaosPblvzcafqV8J" +
       "ngm5UqPDnPmWb0IjKCVzouHTe46avEtaTDJYVQ1ZD4loKR\n2vCB63mHaGvD" +
       "rLAt4g73EC2v/Buw0y5TaiUAAA==");
}
