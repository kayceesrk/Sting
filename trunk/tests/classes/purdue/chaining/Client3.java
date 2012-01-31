package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client3 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAM1Ya2wUVRS+u9vSF7SF8pCXDwSkKrtWhYAtr25b2rKUxhaM" +
       "EB6zM7e7Q2dnxpm7\n7VaJUYxJMRElgkRDQqIiIUACRKP/DDGmiSYm+sOgib" +
       "/whyT6wxijiZh4zszsvTPLdGnEJpDwce6c\ne8/rnnvOWS78Siptiyy3qW2r" +
       "hn4wzsZMasdx6VIDvQOUDQK5X574ru2KzpZcj5JIilTlaC5NLZuR\n+amD0o" +
       "iUyDNVSyQNTaMyA0mtBYusKivVYbmSifsn4kiulLOqpjCyKlU8nnAOJfjxRO" +
       "A4anqwRBOi\nkT4IpqAuV8mxqoGzz2SOfxAlBE7MMw1tLKMZzDvh7tmy4pOd" +
       "qZ8/u8/dszhkzw5H6n755tolS7/d\n8lNNDE2uNg1bRa8ZuSdVPORGpN/jtB" +
       "ZMs2BCqFditOLIi4to2ct36jlDUYdUKa3RlGqzmw0rWj7+\n5WijE5IKDb4w" +
       "0uiLNO5Bzx+9vTjxfWE7eemrfX/e6wiNyM+RF0nEMWqukJJS9WGqoPiZzQN7" +
       "ew+M\nL4tBMMzRCrwg2PpQuUttpxk17E5nMMnKUHBhjuuCJumZxACzVD2DTq" +
       "wsI9Mn8int5MSZ/t/G8XbQ\n9qhZsMtn2dNUpuoILbUIL3dNmWPbgZYyELhc" +
       "Lq+rssR4pp5d88q1tw8n/oiS2B5STTWaozqDR3Dv\nnoBjkjw8aEky7XQ3tK" +
       "ZIXc6ViWIYmSeyxM1oJ5F5nNC5xim62KOPwnu5xcVIiTCLrCgjJGUYpith\n" +
       "uPe93Vd7/0q5eZc2lDE8HysaY9pINDlWNZe1Km1Jupy9vVnl8qk9VEZ0N5mj" +
       "2h3UpLoCwdXGcIOC\nhUOCw4w0+Z5Jt2Rnt0tmK7efgOGzRbZ7/MqqH65+Pv" +
       "/ANzES7SK1miEpXZLMDKuH1LCsRe2soSkF\nc9Nmx4SZo9WAjfA3CsLmcfsd" +
       "gRBLKU01n7U9pDoLWpKGQlMkpknp4CtwCwqUB3L/uUWMVHTu6uxz\no7yMRF" +
       "oaGInt6OgwC4xUyZoK7rbkrWK2xVFA/NZsi3Y93Nb2z7L1mOgmWLEZzFxc5o" +
       "BU88W7K9f+\n+GqUVPSA96pO+/JY3FOkXqGyJuEzTWqS7VjVCOVuCBKuT8pR" +
       "b12boyxrKPwLKq2cgHoldA5CFCUo\n6nUuEUcOI7MgD4Q1jvhHwN7F7D++Tk" +
       "YeKMf2tM5oU3WVbeTqIMZTriGMzC354ghFUU9wgRuRWIOw\nlpFqexjKBIOi" +
       "XGTXCjbCOs54EokWhMdZoGDwHbv9O2p0SKmkZuiCv7q8K4Gm67gS+OLFx23A" +
       "XObX\nSGxC2Aw6mUVLdF7z8ytkwxzjrKVsilWHkdn+pWeIU364sIXlfQvUQc" +
       "e3wBd+TT1cYBUS2xBSQcY+\nJLYidCNsCAsFQvIOGNeCjE7OOMGmNo3B8/Kt" +
       "uHvC2FNIDCLsnKLqVcw3SEm2O/ZgkhWHqD6gvcsS\ny+JlMX+ibmC+pl4qyr" +
       "O6KG1uyRfuyx4uDjJL9IkRFUYqENNvGcyQDa0DahQUEN3WJCjZjCydjOVZ\n" +
       "uoB5X2hwI9f2GBIHEGB3jUalEZqUNME/CnHio6FrTadlGVa3pCtQPTO78BNa" +
       "sjDsM3ePP7FIIxJD\nCBl4fo5CruwyOFSiLAmDJi0wrqYp+IErULmCKCOLSo" +
       "RgrLmERt/Ki1KTs61TycCt7BihlqUqwqjv\nkdARDIgQ38n5F+E814fXX7zr" +
       "ukDO1Donk4FycwMJZzrIF0XjMMr5m2DGDoh2OlM7VArnTfhWxSYj\nlFhUVO" +
       "G/hQsIZphvCJZwSliGMMIZp0NNwpQSJrkrfjWHpsOOG0FGgTPGIBUDBg4YeU" +
       "umXVAd3efs\nW/4vJl72X2KlcwGct86f0EMWpq6uxL3k64dQQch8K+8WY1ae" +
       "3110kPl+m3ERA3KWKnmNQj7Xc9o7\nXgXHURp/Eb8j8RbCcZe71ZD4C4986u" +
       "fWSYzRnMkCO06JHQjv3OUMbTJGs9/TBojDoAGTkwkTi+ob\nWc776wcPOG5U" +
       "nXjPKpLFcMvumgt4DYkzCB8yMtPjYsLxGyGjMBdzHTlJ1ePbAaAG4D/F0cSG" +
       "Hw8i\nlfqQOI9wAeEiZywAk3i/wPMhe+qRuOQwoHuhQhiTl5T85O/XYDDthu" +
       "mfWvDj7/qXhzq3HRl3fxvp\nYtwNjLruz1oxr/smd9zeDKrXF42onEBiNUIc" +
       "ISFmYCTcSQ9BDJcNSIjpMXTqRBBj5VTmTYTWsDET\nQcwUjqVidNgyLQMQEm" +
       "JuuaOBBolnERyPxDyxAYm9CPuCDEeA2/oR0mEzAYLoV0eRcHt3gOFv6gi8\n" +
       "GbvFcRhBCzAiUSSmuRU4YXoe4QWEaelDp5F4GeHwtOmYvNchMY5w5E6VXw7q" +
       "eF20MCTeQHgT4Zho\nTEiIMnsirN0gnAzrNAh3SbuYngaD8L7oKUi4LQHhbF" +
       "ivQDgnOgQSYeV+8j6AxKUQRn2QcQXho7Ca\n7fzHUf2/7p4zdicXAAA="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20103);
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
            ss.participantName("client3");
            ss.addParticipant("client1", "localhost", 20101);
            ss.addParticipant("client2", "localhost", 20102);
            SJSocketGroup sockets = null;
            try {
                sockets = ss.accept("client1");
                System.out.println("Client3 is connected.");
                ss.setCostsMap(costsMap);
                String str = (String) SJRuntime.receive("client1", sockets);
                System.out.println("Client3 received from Client1: " + str);
                System.out.println("Client3 Finished.");
                {
                    SJRuntime.negotiateNormalInwhile("client1", sockets);
                    while (SJRuntime.insync("client1", sockets)) {
                        String str2 =
                          (String) SJRuntime.receive("client1", sockets);
                        System.out.println("Client3 received from client1: " +
                                           str2);
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", sockets);
                            if (_sjbranch_$0.equals("ODD")) {
                                {  }
                            } else
                                      if (_sjbranch_$0.equals("EVEN")) {
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
                SJRuntime.close(sockets);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Client3 a = new Client3();
        a.run();
    }
    
    public Client3() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1327975870000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0aa3AdVfnszftB8+iDvp8BWm1vbAUGSKG0aUJTbtPQhEKD" +
       "kG52T5Jt9+4uu+cm\ntx0Gy0sQx84gLQiCdRiYioDaIuqIgyKP8lbrTKnOwB" +
       "8YHgqMHVE6Cuj3nbN79nFvbitYxhnNzP3y\n7X7nfN93vvO9ds8++C6p8FyS" +
       "9qjnGba1Jc22OdTj0B7cQjXmpXvX9qiuR/V2U/W8PiAMaJs/Njbu\na7p2TY" +
       "oo/aTZsleahur1jbh2bnikb8Tw8i6Z69jmtmHTZj7HAh7nLPhw7IUb184oIw" +
       "39pMGwepnK\nDK3dthjNs35Sn6XZQep6K3Wd6v2kyaJU76WuoZrGdhhoWyDY" +
       "M4YtleVc6m2gnm2O4sBmL+dQl8sM\nbmZIvWZbHnNzGrNdj5HGzBZ1VG3NMc" +
       "NszRgea8uQyiGDmrp3JbmapDKkYshUh2HglEywilbOsbUT\n78PwWgPUdIdU" +
       "jQZTyrcals7InOQMueKWC2EATK3KUjZiS1Hllgo3SLNQyVSt4dZe5hrWMAyt" +
       "sHMg\nhZHp4zKFQdWOqm1Vh+kAI1OT43oECUbVcLPgFEYmJ4dxTrBn0xN7Ft" +
       "mt9ZX1H93c88Fc2HHQWaea\nifpXwqTZiUkb6BB1qaVRMfFoLr2ra1NuZooQ" +
       "GDw5MViMWXnKTy7OvPWLOWLMjCJj1nNfHNA+PHPm\nrIMrX68pQzWqHdsz0B" +
       "ViK+e72uNT2vIOePcUyRGJ6YD4yw1Pb9rxPfqnFKnuIpWabeayVhepoZbe\n" +
       "7uNVgGcMi4q764eGPMq6SLnJb1Xa/BrMMWSYFM1RAbijshGO5x1CSBX8FPj1" +
       "EfFXgwB2u31ENSzY\n5HbToBb7YtrbwsgyRj3mtXqu1urkXD1HWzV/FN+mZa" +
       "2Fk/IoacKYosAiZyYDzgTvXGObOnUHtL2v\nPXdVx4VfvUlsH7qcryMjJwth" +
       "6UBY2mdPFIXznYiOKQy30nXVbRgw+WsOzrrjgHo3bAOYwzO2U75a\nZawcIU" +
       "xaVjKftIfR2AWYCs4yoE3a8db0O393/1MpUlY0p2TkzU7bzaomukUQRM2+uC" +
       "QFvKkl6dPF\nZL9387qHDz3/ysLQuxlpKQi6wpkYNPOTdndtjeqQlEL2t/99" +
       "zZ9vrTj7kRQph0iEXMTA1BjYs5My\nYsHTFiQiXEtZhtQNFSy8loGBxiILRl" +
       "iPoFH4BuzFpISCPIcdva7yCy//vO4pvuIg3TVE8mIvZSJ4\nmsL973Mphfuv" +
       "fLPn1t3v3nhZGYSr44g9Z6TSyQ2ahpaHKSfH4w3V09Ft3tnf1rhzifdjvsU1" +
       "Rjab\nY+qgSSE/q6Zpj1F9gPEE1RRJhjwHgSXqByGXgXsOmMBIrNVRRsE9i2" +
       "SL9NRJu25bdNfLmE8cbpgp\nGHdcU5LnN1oUhAsLiHg9Cz14UrhsyABbxQLq" +
       "F/VevnbzTfP5yqPsFvsXeckwjIXTS8ZCJ9Yd8Blm\nQwIKXWbz1w5ftO7SS1" +
       "pFSlxakkU3hLPO+YTzd3dZKx6bfMhKoZdUeltkyGdIymOMLMwELH3Pw0uB\n" +
       "9a7tFaSgLCw+9gJCwcZVf7tm/69314PgflJleJ2GpZq42163iN4iJSPBYvtj" +
       "F3/76IvsVe6bYbig\n+jPzhYluoxqJ5LMOjTZV/nBPNkWq+kkjL/uqxTaqZg" +
       "49tx8Kt9fu38yQk2L0eBEWFadNpoOZyVCN\niE0GannE2uU4GvFqEZt8TMM/" +
       "xd/H+EPHwYugNNhZB+qJO/cCCotWGdWdvKI4OO0sPnk2h/OE/6UY\niEYTM1" +
       "LluMaoit0TtiejBuNtEh88kZEZchPdnMWMLMW+znc7nikUHhErwGFbEvstXS" +
       "ONrsFEzT5w\naPl+i818jW9Sld+sYccUphCooibYkJdhF2KtFFfpcAOaXyoV" +
       "kZq0EcPUUbNlwGNBSU8U02+p6t17\nyfCue1N+NNc5PI2dGsZzqJfXcrGVtX" +
       "VjyOB5CCL8w4ZTlj7yzs5GUQnCXANRcGwG4f1pq8iO56/4\nYDZno2jIo91P" +
       "RYtlYjitlEVW0WGjmEEqmeoOUybKNyyrBI8Ii3PM2w/c13PkpsAoqx3M06W2" +
       "ZAPV\nqDFKkxpgNjqjxLR1gEPHCU6czVmGxl1Q8Nh7xnWHb7u29a+Q/C8j1d" +
       "SkWegywGNmXxZrfSHL97nQ\nuXSIARBadVnBsy8IpIQRjm89XdYYeFLBegot" +
       "ekoJJhnbdgSHrWvv6X987dGM8JNBW98mfNRXRmx1\nN9dqUUmtBl3IICPHVq" +
       "uUs6wqygPyb7PhraYOtK5gSXObqMMQUipMZmRiJFTXqN7IOtVpk/qTeOX3\n" +
       "6RVVf3j8iSmbf1tGUp2k1rRVvVPl/RCpgUaEeiPQbuadFedzFerHqgE2YpIC" +
       "ZpOl/pwh2FIdpGZE\n2y5SPQJS2m0dGoIyUx1EVc4ETebeP52R8o6NHd3Cql" +
       "8iytIGRsrWrwZFIe1pvF9dmnMDV0qjK6UL\nXSnV+bnlyz+afzZvC0DqGlBr" +
       "RokJas1Td5565is3QNvWBauFGtSdwzSXIROwJqgu78WhPfFTfTU+\nB3SHqb" +
       "9WPOl1h902IRUH4PEzlAn9LVXhsbFOIGmkMHIS7HuoDWevg74z2CcMPUbmlS" +
       "L7UiuXQ//P\nzpPiwMbHnSAYmZS4w5kiK1MyPA8RC4HNSLW3FXIA8wsUkmtD" +
       "MoIrJeF0RLYg2Mpi2UCO6I+OqLHA\nhdpN2wrpS0ovJVZ++FJid3z7iFIkeb" +
       "6ECI+XbSCTQWscl3k4Si/XbGebJM1ix5lloBWOXvqK8HQj\nmU0rvbZY3uNr" +
       "i92R23StZFiFyPUIbogTrkBkB4JrEIwWMwWCqz4F4XCccLUk7GbH15dAeEWu" +
       "5PJC\nZe9C5OsIdh6n6IUs0nionug70cmCpqMbcH+zwstgs1jUUc9lkYqdZO" +
       "VrHXCblLgj17JLstsU3ftR\nwzNYpKtbDTkKZ6/KGfgSACrtuDRf1wmDeMm7" +
       "cRwgxbQhcgeCO8HVTaqO0nZ4YpP0nWAg2ZQLNTpc\n13bXqJYOaXN4I96yQY" +
       "NpxW7Ldd0dMFQaEdmD4DsQd1ygFLaPkVkJYf5LQylmYvyGFHCPFABt8/QE\n" +
       "EzSy5NAYufKNM5EP69CHYTvWj1LXNfRQqd8jshfBd8FCcqSkP8QiD6q478Em" +
       "18WcpZbPbI/lmbcR\neQjB9wPW2KhK+gpGpsZY85K0ClIED4bIVVBdQiEuDd" +
       "PvP8IlILi/2NoQPBAuKtQMwQ8kYU9RldCl\nQpXEldyaR06EHm/HCfskATLy" +
       "tJiCvXbO1WgnpEURx5HL/4iK+6KbWME3QNLOijr0kIuua+lp3/l6\nwFRgss" +
       "iVv4tl8BgXsEj1schbU8miVxuhes7E0J8gcX96FUxHbjIi/oLICwheFNQLbF" +
       "VGuPLTKLVO\nZYxmHRYbcVc4AsHB/xM+E4I5HmFRdMcaYD/7bHyzAC2XEem5" +
       "HojmQek4wSsIyBUBGriNJq4lg5sR\neQPBmwyPWTgVA0d6FhljpFnKyKqGlV" +
       "4HAHIZ/gt6Kw+edsKQ6EbkHQTvInhPEk4GlWTBw/lFxkxA\n5AgnQPlFgcE7" +
       "kMZos4/de/j0EHmOwMGDwGdlwLHiACJDCIYRjIQdOSKi70QQVsQGRMJetmgP" +
       "jCBs\nco+n+0UQGqk/Tgg7nCWIhI3M9hPSjiESdlGfqr1C5BsIbkUQdjfnIn" +
       "IbgtvjhE2IiH4EwbeKNSoI\nZEMhdBENRYwQ7TQQyA5BZOx7EdwXIygpRE5w" +
       "fVqByMMIfoTghBRHvuifIXj0hMkYvwAj8jiCX31a\n4fviMp4O6yoizyB4Fs" +
       "FzYbVEJMyZLxWrgQh+U6z8Ifgvyf3/u4TjqXoIXg8LHSKiTiF4q1gBQ/DH\n" +
       "sGwhUqwGjV+cEDlShDAhTuBu9n6x2sPT41pxiBR7zc/fzS/wj64IQnF0FZw7" +
       "eW6kh0wc3vMPD268\n9Ej9V9QnLxdvcJvjB3QdVi57xp7DdNH59VqRE+IaZj" +
       "tLTDpKRRnD062UOHwAsUtKvpJfx9+Ahccz\nZb0rFp22uO7NFCkf55C3yb+5" +
       "gbKca0Xe+cJo9d8+/Z2TMEpSn6bRGReVjRjPpPixjTjpKfiEIz6p\nLX6+U+" +
       "vGFeVbOIMr0AAmwlegzdgp+G86+X8kNiFoFoe0AJTlYm/HOZYsSgyOGPEaup" +
       "uJYXfTkdeo\ng40e8lfW+E8LhQdvPa6RNZh8Z3fL7HvfePi1DZOED4gPWhYU" +
       "fFMSnSM+agnOWUDCvFIS+OgnPz/v\nwas3vDroH0UoK6FJG7UNHRen9Ma9On" +
       "mhdEjD4u80+K33Dbu+mGELA4lX7wWMVHr8c6PShi38IoZ/\nASGMtb+s5Ujq" +
       "0Skt/FC/fFD15OFE7FOiwi+FYh8AcT1r5aqmwW/OMVb1flEHwDZRGcwrBE8K" +
       "FX2c\nlZ8r8gis36TQ/vJOVrnIEcaFp8gy0BZRNV8Q6oVext934rlXQBNn5o" +
       "adlh9qAbHwSBwFdAp1uaxP\n6P6RI3vlSnw9K1+/xlt9cZKbL7nVyKObc9rk" +
       "PzNwbyzwSSVinNgdPIjwP5zB08apyTDwv9HS5h/c\nvPAJp+lZ4TXBd1RVeH" +
       "iQM83ouXEEr3RcOmTwza8Sp8gO13UHPNklvt5hpDpAuZ2/LEZex7/PwJF4\n" +
       "dT33kbG88i/pnBqS/icAAA==");
}
