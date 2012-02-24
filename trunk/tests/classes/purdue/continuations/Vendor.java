package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;

public class Vendor {
    final private SJProtocol onlineShopping =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1Zb2wcRxWfO9u1nQTn2vxp0yRFFCckQM9UbUpTp01t124c" +
       "LrapHf4koune7vhu\nk73dZXfOsRGCCgQEEEEFSiWEVKkqiqq2qlJEKZ9QhZ" +
       "Alyv8PqFRCAsoHKsEHhBBIBIn3m92d2T3v\nXVxahyJcqS9v5s289+bNm/d+" +
       "e37qz6wnDNhgyMPQ9tzTZbHk87CMYcTNHp3lYo7YU+byrw4964pd\nrxRZoc" +
       "J6G7xR5UEo2PbKaWPBGGoK2xka8xyHm4I0DS8GbF9HrVIUaWbRfwWpuces24" +
       "4l2L5Ksn1I\nbhpS24cy22Hp7S2WQL3qaXIFtiIjD/XOXvhg7WuPFxmjHdt8" +
       "z1mqOZ6Id0RrRvZ893jlj99/a7Rm\nZ86aaan1lHnptl27fznyh/4uuNzne6" +
       "GNUwt2XSXZFEVkJpYML/r+ok+h3otolSEr62iFg8fdhmfZ\n87ZRdXjFDsWl" +
       "zXtu/s6fzpdkSLodmhGslIo01uDk7768Oj2/Y5Q9+OL9f79BKi2YH2WfYAXp" +
       "1Fat\npWK7Z7gF9Zv2z37k6APnbuyiYPhnu3FBtPQdnS51lNfsvDu9ShhBjd" +
       "MRromO4BhubWhWBLZbwyH2\ndtCZUnmH88jyt2b+cg63A9+L/mLYOcvu4ya3" +
       "F3irR7jcAx22HSPeqFHgGo2ma5uGUJl64cCnX/r6\np4b+VmRdJ1kfd3iDu4" +
       "IewQ0nMwczzDNzgWHy8WjBcIVtbEQ6oUawbTpLooyWiazihMOVUkfc0/Eh\n" +
       "udbK82Hv1suGZ9I9S29tRXgKLY50dqDieX6k4czRx068cPQflShnq561hP1d" +
       "yUH8EMx10qv9Hb2q\nBoZr1i/vVqdcHM3VUTzBrrHDe7hPUaOLcZawwELRMW" +
       "izYFtST+yIEdaPGf6w8p+R41frlxLLe3pf\nfuEH2x/4RRcrTrANjmdYE4Yp" +
       "vGCS9Yt6wMO651iL/uG7pQubzvYRLdH/RVK2TfkvFVIsjSp3Ut5O\nsr46WR" +
       "nzLF5hXY5Rzb6gqBhRaWGDF0jUPTU9PRNFeZC2zwvWNXr8w0nU/WaQpGkZu8" +
       "sr07Q48c5D\nh/5140FkkE8ujJOPOztsMPp/+I29t/3mM0XWPUlHt10+1URX" +
       "qLABi5uOgfc95hihdKFEdXKesm3K\naPB4vKHBRd2z1AyM9ixTodM25yiEBn" +
       "WDjRFThkSwt1ASaG+k+pvI353iP3zWgr2tkzi2etUh27XF\nXcrcZrHKlynY" +
       "1emhVAclB5Squ8C8F+R2wfrCM1RZBNXxRLxBi0HuUIJbwdwCcqvI1Bi14kR6" +
       "Rb9L\nmTTmeK6W/1Ssvk8LtrVlJo5Mvwh4i96XwIyAjFJmmp6/pET7RKphG2" +
       "HUXuFZ0qyniI+jpoexqW6R\nPt3B9BW0qopKQKJsS3YiVrfdskOjUbVrTUNw" +
       "bU4ZuCTY7tZXShniUyIHx2kYyphkZtT1Hku0FApg\npkFmMgI22CkUqKypUO" +
       "jhSgvsUhsLbU2/dgG7RaQ6dauzMUxM/N3aMvMaXGYfSqfkgk0AivTPBJ7w\n" +
       "TM+5hwoL1I42CSXygFpvW1l8xQNVDCds7lhYoMwMg7kf5BRlsMONBT5mOFp+" +
       "nu5GIcDIjfEg8IIj\nhmtRrat9AFMeebAjb1oduKoCWwJjgRAE6JEGlbGLlG" +
       "ctxsYIT/JFocxsyU4oAzVloCjY9S1KEH2l\noZQaxcHZIpeNWzW6p+kFHgR2" +
       "Kvl/DcYBaVCE1Eolf5r2K3tIiOT2N2be2Aa5cwywXm19FUwAEiaq\ngTmV/D" +
       "BB6Yxq2UdGCVNAfyk1SlqCNhJwXTn/qY8A4uadDcTXh9KegWiXHs11CSmlXY" +
       "pG6mo+thZ+\nvJoVLCjBEqVixsFZrxmYfIIqU1RCUsM3xMWL6UvskRegZLen" +
       "E3o+QOq6VjlOvhkKFYUsNYpvsSto\nKrvFOZH6BFMqZs06t5oOnv6A4uPtvb" +
       "Qd2tSL+CuYh0C+Eknv9Qz1wgvPp6UbDSF4wxeZFd/UK0Ae\nWRdcEYHTTrA/" +
       "fWOb6T7nPPReQkt2Ci49ma6DKnGSJk21ImGTtDGjsVLwBTBPgjwl2KZYioej" +
       "Moud\nJSiubDQM2y0fI0K1DP/EentC+l7RT2IKjHwzz4J8WwmuJZdUw8P+nD" +
       "UDYJ6TAgJBMEjgfFfLLxQz\nDsHhIx6aH32rvvKjj4+/7/Pnos8xV4NslIlm" +
       "YDV52aTQ2G5TQl1q4p5bI6f9OuG4xYRpYtO7yIGR\nxJWeZTBDIO8BuVnjbz" +
       "AR1gTR8HYzGI1fc3EviAa2q0G8IHfmAV2Qw0pwE5gEiUa/Lmn0q2UgY3nw\n" +
       "FWRcCXZ3Bv36a1hhtgxSlp/FStmOzuA7830uMVVmRtXwnyiFvWB+BvLzrEDi" +
       "nBdBfpyNTtsIvIGC\ntsF8WKzup0fqFamROrc+hawksk+9vErT+8DcC3IEZF" +
       "IJ7gQjk+lkVnC3SP3gkAtL6fMidAwJtXa3\nE8V5cK2IZ3h2obImn9ZvQX4H" +
       "olAkOw8mQpEZQRpegihYGJUcG+R0RlAoglljUHIYjPwJRf4atCaI\n6FEwnw" +
       "R5cM1stEddYD4L8rnXa/xi1sYXNZgC8yUQeftf1hAJjG6UX80DPiAP52EekD" +
       "dJw18biADy\nhEYFYKKmDvJ0XrcHeUb3eDB5Dbt9JwfzXI5gICt4Xgq+B4zc" +
       "rhNPuvNe0oRXNGPVD/+HmzHI8pVt\nN2B0l7jC7QOMLui/X6/065WerVf6/5" +
       "tKf31upR9xqs3GivJ+8PWU987fFJk/icpvisxMm79NFN4P\nch+msm0hV/Bf" +
       "bQvr1X+9+q9X//Xqz95U1b+U/jM6/i7eCu3l0xn4N8W+Ch+QJQAA"));
    
    public void run() throws Exception {
        SongInfo[] songsInfo = new SongInfo[4];
        songsInfo[0] = new SongInfo("Song1", 8);
        songsInfo[1] = new SongInfo("Song2", 9);
        songsInfo[2] = new SongInfo("Song3", 10);
        songsInfo[3] = new SongInfo("Song4", 7);
        Song[] songs = new Song[4];
        songs[0] = new Song("Song1");
        songs[1] = new Song("Song2");
        songs[2] = new Song("Song3");
        songs[3] = new Song("Song4");
        Album beatlesAlbum = new Album("Beatles 2012", 4);
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(onlineShopping, 20102,
                                           SJParticipantInfo.TYPE_SERVER);
            ss.participantName("vendor");
            ss.addParticipant("PC", "localhost", 20103,
                              SJParticipantInfo.TYPE_PC);
            ss.addParticipant("phone", "localhost", 20101,
                              SJParticipantInfo.TYPE_MOBILE);
            SJSocketGroup sg = null;
            try {
                sg = ss.accept("phone");
                String albumRequest = (String) SJRuntime.receive("phone", sg);
                System.out.println("Vendor: Received request from phone: " +
                                   albumRequest);
                SJRuntime.send(beatlesAlbum, "phone", sg);
                int i = 0;
                {
                    SJRuntime.negotiateNormalInwhile("phone", sg);
                    while (SJRuntime.insync("phone", sg)) {
                        SJRuntime.send(songsInfo[i], "phone", sg);
                        {
                            String _sjbranch_$0 =
                              SJRuntime.inlabel("phone", sg);
                            if (_sjbranch_$0.equals("BUY")) {
                                {
                                    System.out.println(
                                      "Vendor: Phone has elected to buy song " +
                                      songs[i].Name() + ". Sending song...");
                                    SJRuntime.send(songs[i], "phone", sg);
                                }
                            } else
                                      if (_sjbranch_$0.equals("NOOP")) {
                                          {  }
                                      } else {
                                          throw new SJIOException(
                                            "Unexpected inbranch label: " +
                                            _sjbranch_$0);
                                      }
                        }
                        i++;
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("Vendor Exception: " + ex);
                ex.printStackTrace();
            }
            finally {
                SJRuntime.close(sg);
            }
        }
        catch (Exception ex) {
            System.out.println("Vendor Exception: " + ex);
            ex.printStackTrace();
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Vendor a = new Vendor();
        a.run();
    }
    
    public Vendor() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328861215000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0bC4xU1fXO7P8j+wPkj8Cq0MJstWrURRFWVhaH3XV3QVmi" +
       "y5uZuzMP3rz3fO/O\n7kCNQU0VbUJiBH9VaayWWDUtGPtJGz/xg1VrW5sgNd" +
       "EmxVqtWEubVtKi9px737vvM7MDqLTG7iZz\n9t577j3n3HPP783c98j7pMK2" +
       "SMymtq0a+oYY22RSm0MjsYEmmR3rX9mrWDZNdWiKbQ8AYii5/mN1\nze6m61" +
       "dESWSQNOvGUk1V7IGMZeTSmYGMauctcoppaJvSmsEcigU0zp93ZPTlm1ZOLy" +
       "MNg6RB1fuZ\nwtRkh6EzmmeDpD5Lswlq2UtTKZoaJE06pal+aqmKpm6GiYYO" +
       "jG01rSssZ1G7j9qGNoITm+2cSS3O\n0x2Mk/qkodvMyiWZYdmMNMY3KCNKW4" +
       "6pWltctVl7nFQOq1RL2VeTa0k0TiqGNSUNEyfH3V20cYpt\nnTgO02tVENMa" +
       "VpLUXVK+UdVTjMwOr5A7br0UJsDSqixlGUOyKtcVGCDNQiRN0dNt/cxS9TRM" +
       "rTBy\nwIWRaWMShUnVppLcqKTpECNTwvN6BQpm1XC14BJGJoWncUpwZtNCZ+" +
       "Y7rZ7K+o9u6f3wFDhxkDlF\nkxrKXwmLZoUW9dFhalE9ScXCw7nY9q61uRlR" +
       "QmDypNBkMWfpqT9eHX/nydlizvQic3q4LQ4lj5wz\nY+arS9+qKUMxqk3DVt" +
       "EUAjvnp9rrYNrzJlj3ZEkRkTEX+VTf82u3fJ++FyXVXaQyaWi5rN5Faqie\n" +
       "6nDaVdCOqzoVoz3DwzZlXaRc40OVBu+DOoZVjaI6KqBtKizD23mTEFIFnwh8" +
       "1hPxV4eAkZo1QM6w\nYvYGRpYwajO7zbaSbWbOSuVoGxgrU/Uct/IhmjcNC7" +
       "rpNjNj6BT6StbUaJukkEduE0YjEdjojLDT\naWChKwwtRa2h5K4DL16z/NKb" +
       "t4ojRLNz5GSgcs455udsxwQLEolw2i1ooEKBSy1L2YSOk7/u1Zl3\n7VXuhe" +
       "MAtdjqZsp3HRktRwiLziwZVzo8r+yClgJGM5ScuOWdaXf/9qHnoqSsaGyJy8" +
       "FOw8oqGpqH\n60zNDrswBqyqNWzbxXj/5ZZVj+176Y35npUz0lrgfIUr0Xnm" +
       "hnVvGUmaguDkkb/jXyv+elvFeY9H\nSTl4JCpbAUsCB58V5hFwonY3IOFeyu" +
       "Kkbrhg47UMFDTq2zDCegSNwj7gLCaGBOSx7PANlV977ed1\nz/Edu2GvwRcf" +
       "+ykTTtTknf+ARSmMv3Fn72073r9pXRm4rWmKM2ek0swlNDWZhyUnB/0OxUuh" +
       "2Rzc\n0964bZH9I37ENWo2m2NKQqMQpxVNM0ZpaojxQNXkC4o8FoEm6hMQ08" +
       "AdhjQgJPZqRkbAPItEjdiU\nidtvX3DPaxhXTK6Yyeh/XFKS5wOtEYTzC5DY" +
       "n4kWPNHbNkSCjWID9Qv6r1y5futcvnM/uYVOJy8J\ner5wVklf6MT8AzbDDA" +
       "hEnsms/9b+y1ZdcXmbCI1nlCTRDS6d4nS89Tu69CVPTNqnR9FKKu0N0u3j\n" +
       "JGozRubHXZKO5WFXtPpX9guUmx4WHn0DHmP1mn9et+dXO+qB8SCpUu1OVVc0" +
       "PG27W3hvkdQRIrH5\nidX3Hf4le5PbpucuKP6MfGGwW6P4PPncfSNNlT/cmY" +
       "2SqkHSyNO/orM1ipZDyx2EBG53OINxclIA\nH0zGIvO0y3AwI+yqPrZhRy33" +
       "abscZ2O7Wvgmn9Pwifj7GD9oONgRKaK5w8iakFesUy6hsGmF0ZSZ\nj0RMXH" +
       "YuXzyLwznC/qIMWKOKGakyLXVEwSqKTDB0zFX9GcM0wWf4ghYI9/IgrRwE/C" +
       "zFGs8xPR4t\nItwrloDRtobOXJpHDM2Dify9d9/iPTqbcYAfVJVTuGH15IUR" +
       "yKga6JGnZAv8rRRVaXRDSSdtRkR4\nSmZULYWSnQk05pW0RrH81qr+XZentz" +
       "8QdTy6zuSh7DTPpz257NbVetZIqcMqj0Xg5UcaTj3j8YPb\nGkU28OINeMLR" +
       "CXjjU5eRLS9d9eEsTiaSRBodTjhaKIPD6aU0soym1WIKqWSKlaZMpHHYVgka" +
       "PhLn\na3fsfbD30FZXKRebGKtLHUkfTVJ1hIYlwIh0dollq6AN1ScYcjanq0" +
       "leVQgau86+Yf/t17f9AxLA\nOlJNNZqlOgOLmbUuUAZDpB+woIJZLiaAe9Vl" +
       "Bc0B15lCSvD2c2pJE9NThZvBtT1H1UWXPgpWWKCL\nwtMoJUDcMExBYePK+w" +
       "efXnk4LmwsYaQ2Cft2NiLMpI9LtaCkVAkLIlDm6GKVMrRlRWlA/G5W7Yup\n" +
       "CVqDU9A2iTwO7qjAYkZafG6+QrEzqxSzXcpPgpWDg6+oev3pZyav/00ZiXaS" +
       "Ws1QUp0Kr6dIDRQy\n1M5AyZo3l1zERagfrQbYiEEOiE2S8nOCoEslQTWftF" +
       "2kOgNcOowUFBRlmpJAUc4BSVp3JRgp7+7p\n6RVavQqmDzNStmz1WlfLUAa7" +
       "NhhDG4wV2mC08yuLF3809zxeUwDL5SDT9BILlJrn7j7tnDe+CTVf\nF2wVon" +
       "F3DuNjnEzAhKLgox6vbZw8UY0PE91e3qgVj4vdXrlOSMVeeIb1eEJxTBV49q" +
       "wTjRhiGDkJ\nDt2ThpMfBnmns0/ps4zMKYV2uFYuVnWVXSjZNbBj9ESo9vxd" +
       "Tg6J6JLUhdjg+e9qRqrtjRA2mOGh\naz00AlsizsIGz2tZFgggcsagf0aNDp" +
       "bTocGzlsS/wo49YzEyMTTiaKaGQdUcpLsfG5sRfAMsM2mY\nmyRqPvMlOMUW" +
       "NQ5K5ia3bmg7WvO6Ditea0hS5/mPIExKuLxLrCU44JCbnFJtJZtQ0/BgSD12" +
       "ksER\nRmaGvdKtXlZD1+Y6CYzI473RpSJKjq0Ibg4gSGspVWAk9anC6xZyIE" +
       "fG4DAm6+NHkK8zXxoOC+sU\nTK68E0MjxyEyucJvkiOqrTJfDXcxBBYkuyyn" +
       "4qM/5NUxcc4RT0hgl9ffOEGyacfGHQjuBAvWqDJC\nO+AZTeK3wdnIMlyIsd" +
       "yyDGuFoqcg1qXX4JABEkwtNiw3/G2p2EZs3IvgPqhoOUPJbDfYWYiZ83Wh\n" +
       "ZNMSHJAMviMZQKE8LUQEtS8pNPp6jnJa+LTlqTScU88ItSzVZ/y/w8aDCL4H" +
       "GpIzJf5R5ns0RYNw\nT78u4GO1fGUHFrhy6bvYeBjBIy5pLEslfgkjUwKkeR" +
       "5ZBjUE0m/09dyU4DGxqBc5/+1tAcGuYntD\n8JC3KU8yBI9KxM6iIqFJeSKJ" +
       "njyax06EHO8GET+QiE1gigEB+42claSdEJlECPF1PxcRd/sPsYIf\ngMSd6z" +
       "foYQtNV0/FHOPrBVWBynw95xTL4KHNJREdYL7vSyWJ/mSGpnIauv4E2XaWV8" +
       "FypCY94u/Y\neBHBSwJ7iaFID4/8xI+tUxijWZMFZtzjzUDw63HEfwWhjYVY" +
       "4D+xBjjPAQNzL1RLqq9cetgfB6Xh\nuEkaYoXbdM0mKfqSwC3YeAvBHxn+wM" +
       "Kx6Dhe5TXKSLPkkVVUPbYKAMQy/OfQrbDh+cRziW5s/BnB\newgOSsTJIJJM" +
       "eLi+yJwJ2PiAI6AIQobuNx5Ti37B3W/oaZCAf7Gedxs5XJICal0u3Yq92ODf" +
       "6qsI\nNnjFNDZE4YjA024DNrxitGgRi8DT1bGUrwhGilWtCPISsQgbblkpvj" +
       "TxSlkPh+CaYrUogmslYmbp\nCt57lJUFWKDs5c+0ktjU0pV04OGaF0iBETcg" +
       "RyokwSrsclAdRFyF3TIE5UHtjKmBzxExpjJ3sGP7\nRg0Cv68nE5G3CwwLkS" +
       "YEzcfIej42tiC4DsH1EnEBNm5DsD2IWIsNUfwhuKtYVYhAVm9kGzZE9RZA\n" +
       "+Ms6BLIcE+nxfgTfDSAiUWyc4GJgCTa4BHsQnJBKZCc2eBL96QnjMXa1g40n" +
       "ETz1WZnvDvJ41iti\nsPE8Ah4mX/BKE2x4CerlYgUHgleK1RoIviCJ9v8XcS" +
       "wlBoI/eFUFNkRRgODtYtUCgne8GgEbxRL+\n2JUANj4ogpgQRBxyk//MMZN/" +
       "lz5suHm/IP8v+xLkfwSf/Hcz3NWBxDSescYz1vFscDxjjSPGM9a0\nohlrqZ" +
       "bIZQvS1NLPkqZKP44Ffvflj2OBkTF+o4ncjoAH22B6K4r4n6a38Sw2nsXGs9" +
       "h4FvuCIb4k\nWazRfy0C7zmEH7X4LZGVIrgGbrPxn1fnOTc0CUJxQ9O9Xmlb" +
       "vh9OQnfV+T37m644VH+j8uyV4pJS\nc/Ae6nI9lz1753664KL6ZJHL0DXMMB" +
       "dpdISKnzjwEmdU3LEDtotK3jpbxe9qeLcQy/qXLDh9Yd2f\noqR8jLvMTc5g" +
       "H2U5S/dda4LZynFfcp4dUkpYnqaR6ZeVZdQXovx2orjQWPDGQnBRe/AaY60V" +
       "FJQf\n4XQuQAOoCG/qNGPFQcQf/49ITJmkWdxFBhB5XZztGLdviyLdm7SOdb" +
       "V41rU8n6Qm1khIP/K28xNZ\n4f3SXkvNqkzWL7fOeuDtxw70TRQ2IN7fmFfw" +
       "CoV/jXiHw71KCBzmlOLAZz/71TmPXNv3ZsK5YRb5\nPSPlI4aaws1FDgWtOt" +
       "yJHJCKxc/p8FnnKHZdMcUWOhLP5vMYqbT52zWlFVv4Agi/6C+Utaes9VD0\n" +
       "Z5Nb+d318oRiy/t3gTdnCl+MCbzvwuWslbuaCp/ZpXcV2VfUALBsjJJ8hH9l" +
       "Eo2OsfMLRByB/WsU\nwk+GK/UDUyj3b2AnIC02P8kXuHqhlfG7O3i108WJq+" +
       "GqEZPvJQGy8OY3MnhLiMt5fUrz991Mjzbg\n7zryKlEw1IoLy/mSR40iHOTS" +
       "fOj8UMatscAmIz7lBEZQpeL1ELxPOyXsBc4bScm5r66f/4zZ9Ath\nNO5bQ1" +
       "V4yy2naf7b0b52pWnRYZVbSpW4K23yTc8CjRd7LmL8/rbXR2mjM8WaOfx9BF" +
       "yDvbncWCbl\nI/8B1aeW4/Y2AAA=");
}
