package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client3 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAM1Xb2gcRRSfu8s1/9o0/W+btmqbtE2RO6O22ib2T64Xm3Sb" +
       "Bi9tMaG2c7vTu23m\ndtfdueQOpIgiVIVixSpIQShKEfVDBdFvUkQKCoJ+kC" +
       "r4qX6woB9ERMEKvre7N7t7vVyLKBjIuzf7\nZn7v78y8ee9nknRs0uswx9FN" +
       "41RKVC3mpHDocbmxHBOTwB5Xr34z9IEh1l6Pk5hCWkuslGe2I8hK\n5RSdpe" +
       "my0Hk6Y3LOVAFIgxWbbGmK6oo8ZOL9xVzkpFrUuSbIFqW2PO0uSsvl6chy1L" +
       "SxThNSM38K\nTEFdnpJzrblLRwuvvhUnBFassExeLXBT+Cu8OXv7Pjqs/PjJ" +
       "3d6cngZzDrmox9Wb29eu+3rvD+0J\nNLnNMh0dvRbkLqW2yIvIhC8ZrFhWxY" +
       "JQb8JopVCWCqLl9B42Sqamn9RpnjNFd8TNxX0DH/50ttsN\nSQuHL4J0hyKN" +
       "c9Dz+24PF3xfPUye+eLJ39e7oDH1KXKaxFyjlgcoim7MMA3hF/bnjo2dOLMh" +
       "AcGw\n5lowQTB1c7OkDrOC3iinCwS1CwxcWOq5wKlRSOeErRsFdGJTE8wQ5E" +
       "7++tW3J345g9lB2+NWxWle\nZaPGHBQTm98ihOkGC/qagCimaXkIM2MXp66M" +
       "/aF4ScmbWhXXJ+7QmMeZyvTZW4zBStvWZNlB4GkB\nslgqlQ1dpUJum0vbnr" +
       "v22rPp3+IkMU3aGGclZgjYkeunI1Gm6sykTVWW9SYMKqSz5GEijCArgpL1\n" +
       "tpe7q+pC5Ltole0afArhU7fCx0e2Dg39tWEHemaBizsgLD1NFtD2z97YtP37" +
       "5+OkZZR0cN1g42U8\nWhTSpTGVUyySDKeO4xoCm+0kZHSclpg/7igxUTQ1+Q" +
       "WVJq/Cbgl0ThZtRuFI6fSYFEoEWQTOBda4\n8OvA3h7xD9MhyL3NxL7WBUO6" +
       "oYtdUt1icedFI8jyui8uKEL1ScBdyGxGskWQNmcG6kLAkVATdwRi\nJFul4C" +
       "FkNiLpFZEKkTOmwjPaDVNjGW4agfzL5q5EjnzXlcgXPz7twmb1uMgMIHkAyY" +
       "O3F1wLBIK0\nqKZVlaLz4s5uOyig0EjGOS2BLiCzA8nOeZUjeVgKIB/BRUUd" +
       "71rBMNYuqXHgUfOS8NCPS4sIp+JR\nETo066F8q2toy+u+SF/2SLg9InSuz+" +
       "pwZQHMhG0KUzX5PtiFsEUMh1Nh2oKsm0/kW7pK+F9YdKLU\ndj8yWSQjkG/O" +
       "6CzLUB7Iz0Kc5NXrWZO1bdPeTw0NzofCEfyElqxu9Fm6N1oDjHUjcwCJIkjS" +
       "VSiV\nXQaH6pRl4CJnFSHVLIt+kArGpYK4IGvqQDDWEqE7NPKjtMydltUKkJ" +
       "VDs8y2dS0w6ltkckgmIUJy\nppS/D+ulPkx/LdedkZrpcFdmsJ+SS28g8wSS" +
       "qRo0XvZSvht6mAi0e/YOw13n7onQqHaMBkpsFpwz\nfwYuIDncyDckRwOnAs" +
       "uQTEvBmw1NwpIKTPJGMjX0v7DjRlRwTAqqUIoRA3Nm2VbZCFxU3nYODf8V\n" +
       "Ey+Hk5h0EyBlj4QL+qSNpWtoKb/4JiBUELLQyM9iwi7L3MWh6HpuhcipRaaV" +
       "OYN67pK8v7wVliOa\n3BG/IuN2DVVP+phJ5Q6PfRyWdlIhWMkSkRkXghlITv" +
       "/PBXw+QX/Y08UQh0kTegML7mQ9dCm/Gz4/\nZMBxou7Ge1GNrYVb9cYS4EVk" +
       "XPKSIAt9KRaczAiZg+5b6ihR3UgdBAJnAP74uEkHer6glMaReRnJ\nOSSvSM" +
       "EqMEneF7i+wZwuZM67Ari9UCE0gmvrnlQTHFqv/SbXmA397PXPn84eeOGM11" +
       "4bQUMXaea8\nZwPGgOvQPg4Aan/TJ0AeriO1ePs3QLOXzXBDjPgUWarD0QNX" +
       "rwam8CpO0PAJS2ExnNGhB9t+6hQP\nUmtQdtIEDF8SvLt8ebL1uyufrjzxVY" +
       "LER6AbNqk2QlXYqqPQGEHz6hQhVhVr9x7XhIVzbUC74T8O\nYCuk/S4gPFxo" +
       "nvGQtaOkrQhaMnBFKCTBaT76HvOetvBQJfe8swZSlj2SHbfcdF4ksQHoUxOH" +
       "9u2z\nKkHkG3Gue11/A7DhR6pXEAAA"));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20103,
                                           SJParticipantInfo.TYPE_PC);
            ss.participantName("client3");
            ss.addParticipant("client2", "localhost", 20102,
                              SJParticipantInfo.TYPE_SERVER);
            ss.addParticipant("client1", "localhost", 20101,
                              SJParticipantInfo.TYPE_MOBILE);
            SJSocketGroup sg = null;
            try {
                sg = ss.accept("client1");
                {
                    SJRuntime.negotiateNormalInwhile("client1", sg);
                    while (SJRuntime.insync("client1", sg)) {
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("client1", sg);
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
                System.out.println("Client3 is connected to all participants");
                String str = (String) SJRuntime.receive("client1", sg);
                System.out.println("Client3 received string: " + str);
            }
            catch (Exception ex) {
                System.out.println("client3 Exception: " + ex);
                ex.printStackTrace();
            }
            finally {
                SJRuntime.close(sg);
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
    final public static long jlc$SourceLastModified$jl = 1327944818000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ZbWwcxXXuzt828UecEOJ8x0DShnMTPgQ4EBLHJjYXx9gm" +
       "gBE4693x3SZ7u8vu\n3PkcIRqIgJSqVCgJaSktUkWFaKlUgmiRWtEioGkLtG" +
       "qoQooEf1K1IKACtZSoBdr3ZnZnP+58oajt\nr1q68cy+mffevHnvzXtvHn+X" +
       "1LoOSbvUdXXL3J1mszZ1eWtN7aYqc9NjQyOK41Ktz1BcdxwAk+qu\nT/SdT7" +
       "TfuS1JEhOkw7Q2G7rijuccq5DNjed0t+SQFbZlzGYNi3kYy3BcvvqjmZfuGe" +
       "pKkdYJ0qqb\nY0xhutpnmYyW2ARpydP8FHXczZpGtQnSblKqjVFHVwx9L0y0" +
       "TCDs6llTYQWHuqPUtYwiTuxwCzZ1\nOE3/Y4a0qJbpMqegMstxGWnL7FaKSk" +
       "+B6UZPRndZb4bUTevU0Nxbye0kmSG104aShYkLM/4uejjG\nngH8DtObdGDT" +
       "mVZU6i+p2aObGiPL4yvkjruvgQmwtD5PWc6SpGpMBT6QDsGSoZjZnjHm6GYW" +
       "ptZa\nBaDCyOI5kcKkBltR9yhZOsnIovi8EQGCWY1cLLiEkQXxaRwTnNni2J" +
       "mFTmtHXcvH9458uAJOHHjW\nqGog/3WwaFls0Sidpg41VSoWni6kDw3eWFiS" +
       "JAQmL4hNFnM2n/uj6zJv/nS5mNNVYc4OrouT6keX\nLFl6fPMfGlPIRoNtuT" +
       "qqQmTn/FRHPEhvyQbtXigxIjDtA382+vMb932Xvp0kDYOkTrWMQt4cJI3U\n" +
       "1Pq8fj30M7pJxdcd09MuZYOkxuCf6iw+BnFM6wZFcdRC31ZYjvdLNiGkHn4J" +
       "+A0S8deEDSNNfYZO\nTXZh2t3NyAZGXeb2uI7aYxccrUB7QFuZbha4mk/Skm" +
       "05MMz2BItKSGHeTCIBm1sSNzQDtHKbZWjU\nmVQfPfWr2/qv+dIBcWyoah5v" +
       "DNZxYukwMTxyToMkEhz5fNRKIbXNjqPMorWU7ji+9OvHlG/CGYAs\nXH0v5V" +
       "tNzNRgC4s2VHUmfYEpDkJPAU2ZVDv3vbn4wVceeyFJUhUdSkZ+HLCcvGKgTv" +
       "gW1OGRi0NA\nlbrjCl2J9p/v3f7kiRdfXxOoNiPdZRZXvhItZlVc+I6lUg08" +
       "UoD+yN+3vXew9rKnkqQGzBClrYD6\ngFUvi9OIWE6v74VwL6kMaZ4u23gTAw" +
       "HNhDaMbQs2bUJB4Cw6YwxyB3Z6f90XXv1J8wt8x76vaw05\nxTHKhOW0B+c/" +
       "7lAK31//2sjBw+/ec1MKbNW2xZkzUmcXpgxdLcGSs6PGhuxpqDbvHO1tu+8C" +
       "94f8\niBv1fL7AlCmDgnNWDMOaodok496pPeQJuQMCSbRMgSMDE5g0AJHYq5" +
       "0ognpWcBXpRZ2HHlj70Kvo\nTGwumIVodJxTUuIfuhPYrikD4ngpanBnsG0w" +
       "/z1iAy1rx24e2nVgFd95GN06b1CSCANbuKiqLQzg\npQM6wyzwPoHK7PryyW" +
       "u333B9j/CH66uiGAab1jieYP3hQXPTMwtOmEnUkjp3t7T7DEm6jJE1GR+l\n" +
       "p3k4FL2xoTEB8u+EdWfeQEBYv+1vdxz9zeEWIDxB6nV3QDcVA0/bHRbWW+G+" +
       "iKHY+8x13zr9MnuD\n62ZgLsj+klK5t9uphCz50hPF9rofPJxPkvoJ0sbvfM" +
       "VkOxWjgJo7Abe22+d9zJCzIvDoDSyum17p\nDpbETTVENm6oNSFp1+Bs7DcI" +
       "2+RzWv8p/j7BHyoODsS90NFn5W24TJwVV1PYtMKoZpcSCRuXXcoX\nL+PtSq" +
       "F/SQakUcSM1NuOXlQwdMLYpKgz4c9x8nxGuuQhOgXw9nmKQZ2ndtxTJLhFbA" +
       "KF7Y6dt1SN\nNKoGExf2sRMbj5psySl+SPVepIbhUuBC4Ao1QIb8DnbA1qph" +
       "lQo3qXr3ZEK4JjWnGxpytgFwrK6q\niWL5/fVjj16fPfRI0rPmZpu7sfMCew" +
       "74cruvM/OWpk/r3A+BhX/Ueu76p965r03cBIGvASs4M4Lg\n+zlbyL4Xb/lw" +
       "GUeTUBFHn+eK1knHcH41iWyhWb2SQOqY4mQpE3c4bKsKjhCKy40jx74z8v4B" +
       "Xyhb\nbfTT1Y5k0JwBydMzc3BuFSQZy7IFhj1D3554duh0Rsh1ytJmxZl+Om" +
       "ZGqUr1Yhkz6BovrrJsO/Qh\n9gWLyhdMXeX2IHA8evH+kw/c2fMB3EQ3kQZq" +
       "0DyEPKC+y26KBOFw5Yw7EEv1iwlg5815gXPct+qY\nPLz9QGDl40ojrnQ5ru" +
       "TA5zZu/HjVZfySgv1sBhl0VVmgNL7w4HmXvH4XBBGDpAnjz+ECGl2GzEMP\n" +
       "pWDCwC9Lz/E0YEg6HDiiJpF0DAcBICG1xyATCmhCtEUVyGCaRSeNEEbOgs0F" +
       "3HD01wO/Xewzyp6R\nldXAHtW6jbqpsysluVb26TWEkc7YF44UUU1KhFdih3" +
       "+bYqTB3QNKwDx3ieCmAIyNJgEXYedmbG5h\nEXWQMybCMxpNS6N9hmUG8F9X" +
       "30rEGfKtRL548mlkEIzF8GJnDzbco+fPDDgZABipUS17VoIOs093\nD4AChU" +
       "ZSzrpE9BB2+IjNSRybWyVgDQs5esUV9zyK0Xfyw9BHyu3hoScXft9KVFewkI" +
       "eMo/K49rF1\nxr7IveyV6G4Mn1xRhxwydItuBSvE1VsKOmZe4EzmhHm8zpvC" +
       "IY9+cIIk04udL2KzDw7aoEqR9kGE\nLOH3gYBkECTY6Hccy9mmmBo4huxO/A" +
       "QZIzmn0me5r/0+wkQbdu7C5m6IJzhBSewJRpbGiHkVGklm\nfvSDJHBAEoAw" +
       "ZXEMCQpZYmgLjTzhzOfT+rUsHMeOInUcXQuY+j12voINyKJRzpTw77NQYoDn" +
       "7h9y\nc0RZmvjKPgwx5NK3sHMQm0M+agwMJHwTI4siqLnT3QI3GjeG0Mj3nw" +
       "ERSO0knn8EW8Dmq5X2hs39\nwaYCzrA5LAEPV2QJVSpgSYzk0Xzjv8HHW1HA" +
       "EQmYBVWMMDhmFRyVDsANJew4NPyPsPhE+BBr+QFI\n2KVhhZ52UHVNLe0p3w" +
       "iICkQWGnmnmIKw2UeRHGehEpVEMabmIPE30PTnyb63vB6WIzZpEX/BzlFs\n" +
       "nhTQqy1FWnji6TC0WWGM5m0WmfFQMAObp/8P+J8AjLkAa8Mn1grnOW5hJgdB" +
       "hR6KKr4X9oNScfyU\nD3yF3/XVRhVjieBe7LyEzcsMa9ocioYjNYvMQBIpae" +
       "QhdU5vhwZ8Gf7z8NZC4uoEJjGMnePYvILN\n7yTgbGBJXni4vsKcedh5lQPg" +
       "+kWCfs7ZFg5nMT7FDfHq4noIdtdWzT6mHMisc2dOP6olUVsq4khO\nkA4d/C" +
       "EEAhqwYsyK+hSkmgoshosjlMJuU9zcdsXulXE9iVbEPHht/WvPPrdw129TJD" +
       "kAsbmlaAMK\nrxNCmAahtJuzDK1kb7qKs9Ay0wBtGybvgGyB5J8jhJxJmaJG" +
       "iNtB0pADKn1wb2VIylB4OHoJcLLi\nscUg7/6d/cMisTxFEushSk7t2AqMBp" +
       "Ku1OPbGSqVlxN4DWC1VyIj2K6J1LeA4aVzvRDw1417bni/\n5W7l+ZtFctYR" +
       "LQT2m4X8xQ+fpGuvalErlKMbmWVfYNAiNWQVLSmKHED2gqqp/3ae2wRloNTY" +
       "prXn\nr2v+EyRLcxST272Po5QVHDOUzsFs5d+uMi+PCSXOT3ux69pUTv9Fkp" +
       "eHREWp7J0ouqg3WkdqcqKM\n8iPsEtUkEBGqVAf8Fnuaw/8jsB2bDlEMxubt" +
       "quXPikC/lOlZ9fzAqvtLKrXRwXH8H3q3ZHmBb8TR\n8zqT2dj9yx7545OnRj" +
       "uFDohXs9VlD1fhNeLlzK/nAIWV1Sjw2c9/fuXjt4++MeWXPN4DYylauoab\n" +
       "SySjWl02+KsULP7Oh99WT7BbKwm2Ql2OGxKksS5/06wu2PJnN/7SIoR1NNX9" +
       "fvLHC7v540HNFPgo\nT1Gj75Xlz5GRV0bOZ5Pc1TnwW36GXb1WUQEw/U20lx" +
       "IEK5KJ+XPs/ArhR2D/BgW3n8MJsISLIZEC\nPQFusdtWKjP1ci3jWS7W13yY" +
       "qM3rVlq+BgOwvPSO4w8Eu5zWZ1T/0NNAYgVcnapMuqNXnKgYl6oe\nNQ4/5p" +
       "gavLuSa2OZTiZCwol8QcfuPdBhVXNR3Ay8h2B11fFda56z238ptMZ/rK3Hsl" +
       "DBMML16VC/\nznbotM4Pv15Uq23Oaw+IvNJTIeMV9GDMTzct1mzgL0K4BkcX" +
       "cm05r5T4FzBkCEVtIAAA");
}
