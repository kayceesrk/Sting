package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;

public class Phone
  implements Runnable,
             Serializable
{
    
    public void
      run(
      ) {
        
    }
    
    static class myRunnable
      implements Runnable,
                 Serializable
    {
        final private SJProtocol
          onlineShopping =
          new SJProtocol(
          ("H4sIAAAAAAAAAO1ab2wcRxWfO9vxnwTHjZP0T5IiihOSKD1TaEKDQ5P4Ejd2" +
           "L7ap7dKmtM3e7vi8\n8d7uZnfOsaFC0Aop8CFSUVugKhRVoHwAPgTRP6raoq" +
           "hUkfgnFSRUkPhUPlAJPiCEQCJIvN/u3szu\nee9ix2lo04uU5zf7Zt578+bN" +
           "vN/s7Y/+xtp8j/X53PdNxz6REwsu93NohtzEyAQXk8Q+rF/4/b6f\n2GLz21" +
           "mWKbD2Mi8XuecLtrFwQpvT+ivCtPrzjmVxXZCmgXmPbW+oNRCFmln4LxNobt" +
           "NnTMsQbHuh\nOrw/GNQvh/cnhsPSR2ssgTrFE+QKbIVGHm+fOPu50hPfzzJG" +
           "Iza4jrVQshwRjQj7HNz64lThLz/7\ncNhnU0qfsUDrw/rFPZu3/Pbgnztb4H" +
           "KH6/gmZi3YDYXqoDAi45FkYN51510K9TZEKwdZTkXL75uy\ny45hTpta0eIF" +
           "0xcX12697fm/nukJQtJq0RPBemKRRh/MfNel1annNw6yL//yoX/dHCjN6CfZ" +
           "l1gm\ncGq90lIw7VluQP2aHRMPjhw/fUsLBcM91YoFoq4fa7Co+UFeMhetKU" +
           "K5rcGo2KBPW9+88IPxv5/G\nIHiXdefJ5NaGeWQbaQZ3NxhzlHitRHEplyu2" +
           "qWtCJuLZ3Y+99dSj/f/MspYHWAe3eJnbgnL85gfC\n0FuaXeqfEJo+O+lpOj" +
           "8cdhgosNXlUCfUCLZBJUGYsEGeFtgqoXklTiu5LqHOM+3SQGy+jfbNPVzn\n" +
           "5hxfPGUM7w2G72gwfKwiTtH2ShnfOMwFx3HDMbMjzx07P/LvQpiYRcdYgOWW" +
           "qvuuD+aGwJGdjR0p\nepqtz6R50ijHBlNHZY+xdaZ/iLuUDrQi1gI6GDhMNB" +
           "osWG9s6xzR/Jmjmisj7jLy9Tq1AyJ5W/sf\nz7++8fibLSw7xLosRzOGNF04" +
           "3jDrFDMe92ccy5h39x8IXFhzqoNoD/3PkrIN0v9AIYVPK3Ir5u0w\n65ghK3" +
           "nH4AXWYmnFZEqEhwwdGazvLIlaR8fGxsPA9tHwacFaBqful4HG33U0jYpXzd" +
           "MctOQW52l2\naOe+ff+9ZS+i7JIrY+TrpgYDtM43nt62509fzbLWYQqBafPR" +
           "Ck79Aus2uG5pSNy8pfmBCxvpHJym\n1BrVyjxqd5W5mHEM+QRG2y7QQaZsTl" +
           "IoNTrtV4dMDhLBPkS7RHkTqM+Rv5vEZe5rwT7SSBxZXbXP\ntE1xpzS3Vizx" +
           "6BHsungzUAcle6SqO8HcAbJXsA5/lo4WQed0VdylxCADUnA7mIDsFolDRvY4" +
           "Fu/R\naVNG5S3HVvLfiKXXYcHW1zyJItMpPF6rF8wgSB7kkBS8pQSUurrjLk" +
           "jRdhGr1Jof1lW4XK3So8RH\n4VTNyIdWEZ/23vja1KoKz4iqst7kg0jdRsP0" +
           "tXLRLFU0wZU5aeCiYFtqtzGljksZ7k1R0w+ClXgi\n132sqiWTAfNZkHsSAt" +
           "bXKBQ4bWOhUM3FFtjFOhbqml6+gH1SxAp4rbMRPqz6u77myTJcZvfFc3XO\n" +
           "JORE+sc9Rzi6Yx2iEwdqBysED7lHRbmuLFri7iKaQya3DHSQZgbAHAdBaltc" +
           "m+N5zVLyM7Q2EvqF\nbhz2PMc7otkGHYKle/HIIQ9uTHssJ2zIwPaAmQYpCd" +
           "YWGJTGzlGe1RjLE5Dk80Ka6U0+kAZMaSAr\n2E01ShB9qaEn1oqC0xt0O2yU" +
           "aJ3G5rjnmbHk/wMYG8ShCMmeUv5jGi/tISGqq786sce6gpF54Hk5\n9B0wAq" +
           "RSVQ2wKeX7CUMnVAcFZpBwBvT3xFrVWqGMeFwdqf9RUwBx0+YG4qlJKc9A5q" +
           "Tg2VSXkFLK\npbAll+aRd8OPd5KCeRUzMF8A+eK7ZvxZMF8BefTqT3CB9lpi" +
           "BSaciqfzITp6wzMy1rwia3AunqVt\nQYZJ2R3xHTvtYW/aRi7aXeOUC5QTsV" +
           "aUpi1eRSZndlLELpdSxYQ+w42KhbOtW/LR8HYaDm1yy/8D\nzDMg3wmldzma" +
           "PMIyL8WlqzUheNkViR7PqB4gzzUFV0Vg1RPsiK/YWlrPSQfggnCiGQOKP4wf" +
           "9DJx\nqiiEDsMqW00bPWxLBV8H8wLIi4KtiaTYODKz2Cm6jEgbZc20c0eJ0G" +
           "GNP5HeNp+usmpLjIJ5BeRV\nkNek4HpySVZ0jE/p0w3mfCAglAeDdC3ZXPPu" +
           "Zdyii8ARB9Wdrulv/+KRw3d/7XR4B7XV9QLHRMUz\nKjynU2hMuxKAfEIpjk" +
           "2VNzueD86VXcHVJzLfdgHMx0FuA/mEum2ACZE1iALzaxtD6sQVPQBEiSf1\n" +
           "bge/Avk1iIL/XfUEtyd9U3eWY0nBASm4FUwVlofv2JaF5UHuUkgezDDICMjd" +
           "CpeDOQoS5MQKcCrr\nAzMFcu8KVV0O5AXzeZAHl2ac3QcmRJUgxTS4CaLCfg" +
           "ZMCAsTgjheBJE4LyxLsyBWQpDJgmmijOYE\nG8MoMI+DfGOlxs8lbTyl0BGY" +
           "b4F8G+RphXnAqMr33TQkA/K9NBAD8h6p4B9cwVKwC8hPFVwBE6IN\nkJfSYA" +
           "jIywp8gElDEvUhBpjzKYLupOB1kDcEWzVHkMnx5iUUGFkJFACjCvT/raSD/G" +
           "4F9XxL41ed\n6vW/fCGVeD8Y/A4gla0Ty/gNQrANtY+qCCmzU6psR3MXyK1J" +
           "wUNoAo2ESXjg0jG4goK64XxSLO0n\nVbopxloSGapZYFNl9oB8aomm6yKzz4" +
           "BRiEYJDojYDy6pb908zfYtLXiTtKWeKMqE60X0hCc7SmvY\nXhn4kgm2SBMN" +
           "vVfBwjU/wSYaaqKI5aOIQPBzvAKs96Jh2J52JMyQKCP/wUEZV7KQnkzUv6tc" +
           "GE/G\nS1Vmf7OGvd+O+Gt+gs0a1qxhl1/DbkqtYQetYqW8uIANrrSAqe9bUr" +
           "+LAakpYJf4Igbk6hawZp1q\n1qnmBJt16v0iuEbqVE/8G1V8dKre4qJH9/8A" +
           "i0SEQsYwAAA="));
        
        public void
          run(
          ) {
            try {
                final SJService c =
                  SJService.
                    create(
                    onlineShopping,
                    "localhost",
                    1000,
                    SJParticipantInfo.
                      TYPE_MOBILE);
                c.
                  participantName(
                  "phone");
                c.
                  addParticipant(
                  "vendor",
                  "localhost",
                  20102,
                  SJParticipantInfo.
                    TYPE_SERVER);
                c.
                  addParticipant(
                  "PC",
                  "localhost",
                  20103,
                  SJParticipantInfo.
                    TYPE_PC);
                SJSocketGroup ps =
                  null;
                try {
                    ps =
                      c.
                        request();
                    ps.
                      continuationEnabled =
                      true;
                    System.
                      out.
                      println(
                      "Phone: connected to all participants");
                    SJRuntime.
                      pass(
                      ("Please send me the songs in Beatles Album 2012, and I will d" +
                       "ecide which songs to buy!"),
                      "vendor",
                      ps);
                    Album a =
                      (Album)
                        SJRuntime.
                          receive(
                          "vendor",
                          ps);
                    System.
                      out.
                      println(
                      "Received Album info. Album Name: " +
                      a.
                        Name() +
                      ", Number of songs: " +
                      a.
                        Count());
                    int i =
                      0;
                    {
                        SJRuntime.
                          negotiateOutsync(
                          false,
                          ps);
                        while (SJRuntime.
                                 outsync(
                                 i++ <
                                   a.
                                     Count() -
                                   1,
                                 ps)) {
                            System.
                              out.
                              println(
                              "Retrieving song information...");
                            SongInfo si =
                              (SongInfo)
                                SJRuntime.
                                  receive(
                                  "vendor",
                                  ps);
                            System.
                              out.
                              println(
                              "Received song information. Song Name: + " +
                              si.
                                Name() +
                              ", rating: " +
                              si.
                                Rating());
                            if (si.
                                  Rating() >
                                  8) {
                                {
                                    SJRuntime.
                                      outlabel(
                                      "BUY",
                                      ps);
                                    System.
                                      out.
                                      println(
                                      "Buying song...");
                                    Song mySong =
                                      (Song)
                                        SJRuntime.
                                          receive(
                                          "vendor",
                                          ps);
                                    SJRuntime.
                                      send(
                                      mySong,
                                      "PC",
                                      ps);
                                }
                            } else {
                                {
                                    SJRuntime.
                                      outlabel(
                                      "NOOP",
                                      ps);
                                    System.
                                      out.
                                      println(
                                      "Song not good enough to buy... ignored!");
                                }
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    System.
                      out.
                      println(
                      "phone Exception: " +
                      ex);
                    ex.
                      printStackTrace();
                }
                finally {
                    SJRuntime.
                      close(
                      ps);
                }
            }
            catch (Exception ex) {
                System.
                  out.
                  println(
                  "phone Exception: " +
                  ex);
                ex.
                  printStackTrace();
            }
        }
        
        public myRunnable() {
            super();
        }
        
        final public static String
          jlc$CompilerVersion$jl =
          "2.3.0";
        final public static long
          jlc$SourceLastModified$jl =
          1329255889000L;
        final public static String
          jlc$ClassType$jl =
          ("H4sIAAAAAAAAAO0bbZAUxbV37/vD+wREDlDgUPBjLyZi1CMgnJwcLnfn3YFw" +
           "Fh5zO317A7Mzw0zv\n3R6xjGgpaipUWYJfZaSSmJAYTQnGfFXKxFJD1MREki" +
           "CxSv+YSrSiRpOomKDJe90zPTN7uyuImg/3\nqu7d637d773ufl8zN33fq6TM" +
           "sUnMoY6jmcamGJuwqMOhObyJJpgT61/Vq9gOVTt0xXEGgDCU2Pie\ntnZv4z" +
           "UroyQySJoMc5muKc7AqG2mk6MDo5qTsckplqlPJHWTuRwn8bhg3pHxX2xf1V" +
           "JC6gdJvWb0\nM4VpiQ7TYDTDBkltiqaGqe0sU1WqDpJGg1K1n9qaomtbYaBp" +
           "gGBHSxoKS9vU6aOOqY/hwCYnbVGb\ny/Q646Q2YRoOs9MJZtoOIw3xTcqY0p" +
           "Zmmt4W1xzWHiflIxrVVWcLuYpE46RsRFeSMHBa3FtFG+fY\n1on9MLxaAzXt" +
           "ESVBvSmlmzVDZeTk7Blyxa2XwACYWpGibNSUokoNBTpIk1BJV4xkWz+zNSMJ" +
           "Q8vM\nNEhhZEZepjCo0lISm5UkHWJkeva4XkGCUVV8W3AKI1Ozh3FOcGYzss" +
           "4scFo95bXv3tT79ilw4qCz\nShM66l8Ok2ZnTeqjI9SmRoKKiYfTsZ1d69Mz" +
           "o4TA4KlZg8WYZfO/vyb+0k9OFmNacozp4bY4lDhy\n7sxZB5b9oaoE1ai0TE" +
           "dDUwitnJ9qr0tpz1hg3dMkRyTGPOJP+362/up76Z+jpLKLlCdMPZ0yukgV\n" +
           "NdQOF68APK4ZVPT2jIw4lHWRUp13lZu8DdsxoukUt6MMcEthoxzPWISQSvgt" +
           "hd8NRPw0ImCksnfU\nNGjM2cTIEkYd5rQ5dqLNSttqmraBrTLNSHMjH6IZy7" +
           "ShmWyzcAq0lZSl0zaPQQZl1Y1HIrDMmdku\np4N9rjR1ldpDiT0vPnnliktu" +
           "vEEcIBqdqyUj84XgWFAwzEYJramJvrRhKMM6JZEIl9KMhio2cplt\nKxPoQJ" +
           "ltB2bdsV/5MhwLbI+jbaV89ZFxXDtO+nTB+NLhe2cXYAoYz1BiytUvzbjzN9" +
           "96PEpKcsaY\nuOzsNO2UoqOZeE7V5IrLpoB1tWbbeC7Zr920+sGDTz2/wLd2" +
           "RlonOeHkmehEc7NPwTYTVIUg5bO/\n7R8rX7+l7PyHoqQUPBO3XQGLAkefnS" +
           "0j5EztXmDCtZTESc3IpIVXM9ig8cCCEdYiaBCWAmcxJUtB\nHtMOX1v+qWd/" +
           "XPM4X7EX/uoDcbKfMuFMjf75D9iUQv/zt/fesuvV7ZeXgPtaFj9zkoGRJ4bd" +
           "DrVS\n0Vpe2dfesOMs53v8ZKu0VCrN0MAgTCu6bo5TdYjxONUYiIk8FMEG1A" +
           "5DSAN3GNKBkViiFRkDq8wR\nNGLTp+y8deFdz2JYsfh+TEPVPAWxY14E4WmT" +
           "iNiehYY7xV8tBILNYgG1C/s3rNp4w1y+4CC7M9xG\nRjL0XeCcgi7QiekHTI" +
           "WZEId8S9n4xUOXrl53WZuIjGcXZNENPq1yPv78XV3G0oenHjSiaBzlzibp\n" +
           "93ESdRgjC+IeS9fgsCmw/lX9guRlhzPffwG+YO3Kt7bt+9WuWhA8SCo0p1Mz" +
           "FB1P2+kWTpsjc2Sx\n2PrwmrsP/5K9wE3S9xJUf2ZmcrRbqwQc+LyDY43lD+" +
           "xORUnFIGng2V8x2FpFT6PBDkL+djrczjg5\nIUQP52KReNplFJiZ7aEBsdn+" +
           "WRrY7VIcjXilcEk+pv5f4uc9/EXDwYbIEE0dZsqCtGKfcjGFRSuM\nqlYmEr" +
           "Fw2mf55NkczhH2F2UgGreYkQrL1sYULKJInWlgquofNS0LfIZPaGakRR6knY" +
           "aIn6JY4rmm\nx4NEhHvFEjDa1qwzl+YRQ/NgIn3vP7h4n8FmvsgPqsKt27B4" +
           "8qMHJFQd9pFnZJssKMhVGt1Qws2a\nERGVEqOarqJmZwOPeQWtUUy/uaJ/z2" +
           "XJnfdEXY+usXgEO9X3aV8vp3WNkTJVbUTjsQi8/Ej9/LMf\nemVHg0gCfrwB" +
           "T3h/Bn7/ScvJ1U9d8fZsziaSQB7L3XB0hgwOpxXYkY7lNKlN2hCMB6cWmBWY" +
           "dIF+\n2/6v975xg7cNHRZG5/kFD8FQcwlcVGDOasCh1gS7TaUNLcGrCMFjz6" +
           "JrD916TdubEO8vJ5VUpylq\nMDCQ2ZeHil4I7AM2VCwrxADwppqU4Dng+Q7E" +
           "MKbYScpE7eIvppBF9dEE1cbo5PXg9G4+fWGB6T1p\nNg6Gl2N+4T2Mm6Yl5m" +
           "xe9dXBR1YdjgtDGjbVCWHErvrCFi7lipxeWJFhG+LMaC5NChnQ8pyzIC43\n" +
           "ac5F1IKzhu3WJ0R+BjdTYDIjzQH3Xak4o6sVq12qTMKFgEsvq3jukUenbXym" +
           "hEQ7SbVuKmqnwssj\nUgV1CXVGoRbNWEsv5CrUjmNx3IDBC5hNlfpzhrB9yj" +
           "DVA9p2kcpRkNJhqlAolOjKMKqyCDRp3TPM\nSGl3T0+v2MgNMHyEkZLla9bL" +
           "jcW/q0HttO0ZXQyNLjbZ6KKdpy9e/O7c83nNAKJ7QLeWAhOUqsfv\nPPXc56" +
           "+DUq4LlgzRtjuN8S9O6jBhKPgkx2sXNw9U4rNCt58XqsXTYLdfjxNSth8eUX" +
           "2ZUPNSBR4t\nawQSQwojJ4AX+Npw9iOgbwv7gE7KyJxCZFdq+WLN0NgSKa6e" +
           "HWUcgWou2OTskIkhWS1BhOe3LfCA\n5GyGOMFMn1ztkxE4knAOIjxvpVgoYs" +
           "gRg8ERVQZYUIcOzzaS/jQ7+ozEyJSsHndnqhgUw1l8EdmK\n4PMIrpSEQz4B" +
           "TDdhWhOStIAFMpviiOIGVfayWjfg7nb6TVcHXmRIVucHzyablYgJHrPmcIfL" +
           "bpqq\nOUpqWEvCIyH1xUkBRxiZle22XtmyBpoO36xQjzz37R4XUWvciOCmEI" +
           "G0FtoKjK6BrfCbkyWQI3kk\n5BV97ATyGRbIxtnKupWSp++UrJ5jUJmsC9rq" +
           "mOZoLFC8XQQRB9kuT2v40A8ZNi/NPeK6YWzywhsH\nSDHtiNyO4A4wbZ0qY7" +
           "QDHs4kfQecjay/hRorbNu0VyqGCkEwuRa7TNDgpFzdcsF3yY1tQORuBLuh\n" +
           "lOUCpbC9YGdZwtzXhFJMc7hDCviKFAAV8owsJrj7kkNDoOVuTjMftkJNwjn1" +
           "jFHb1gLG/3tEvoFg\nD+yQHCnp97PAMykahHf6NSEfq+YzO7CylVNfRuQ+BP" +
           "d7rLEelfSljEwPseYJZjnUFci/IdDycoUv\nxKZ+SP2nvwQE38y1NgT3+ovy" +
           "NUPwHUnYnVMlNClfJdGSR/Pdj0KPl8OEB/w9Q2Qfggc/MuG7Efkh\ngh99/A" +
           "ucAF8LnUC/mbYTtBNCr4iRgeaHcgZ7g1Zaxi1M0s4LeuyIjb5pqDHXu3rBFs" +
           "AmAi3XTEvg\ncdRjER1ggRfBkkV/YpSqaR1jW53E3ekVMB25SZf/GyK/RfA7" +
           "Qb3YVGQIi/wgSK1RGKMpi4VG3OWP\nQPBckfCxEPR8hIXBE6uH8xwwsbiAOl" +
           "ELFIrfDgZ6aTheFQLB0EM9s0mItmTAM+5rCP7C8D9HnIqO\n49ec44w0SRkp" +
           "RTNiqwFAsMY/Lt8yBx5VfZfoRuTvCN5E8JYknAgqyYyO83OMqUPkHU6AKg8F" +
           "eu9y\nTsr57r7fNJKMRHs70jhM5c8xLq+y/Yjwf1FoCDb5jw6IiDIZgb+j9Y" +
           "Xr49DzNa9uQj15Sv1ICYJS\n7PL3tTof4ZywbmO5ansEGUk4CxGvxhavjo6p" +
           "MEfwBb8sR2QbgmsQXOsX2Yhch+B6BMdRdJJWRL6E\nYMdxsvog9SsiOxHsOj" +
           "rhZB0iokREcGeu2hGBrPHEskSNFyIEiz8EsmgTOeZrCO4JESJRRIolQ3GB\n" +
           "hWsiRJ5A8OTxCt8blvG0X+og8msEzyA44BcwiPhp7GCusgTBoVwVCYL/knT8" +
           "ySUcTSGC4BW/9kBE\nlA4IXs9VUyD4q19JIJKrLMhfLyDyTg5CXZjAbfwII+" +
           "VjUP+YdkaWAn3HUwpsCSXo/1hKR37Vx5HP\nZxV+b+m/u5dvl0Iv+/hLfMms" +
           "iR3DPxAYmZrd5VVIEUWyrMAm/q8oooYJV2BzCMHG8Obk3YMPkZB3\nO3exo/" +
           "tPITz2BVqyMvRXgb4YwWOPGEcpOm9l9jlE/IrGJ6xHpFi4/A/n9f/7BRYLly" +
           "Lhk1u4cEIz\nvkLM926jyxgxZWUjC5tLPjmFzYeZu7eEUm4xFxdTVXGBxVxc" +
           "JBRzcSAXz8iZi5fpw+nU5ES86jgT\nceA7n5zfByEIJ+L3+zIIwcebiIv5tp" +
           "hviwss5tsioZhvP0C+bQh+c4wfEfsv8nFEl0gboTsgERji\n8Eu07t0mglDc" +
           "bRI3DaIe9yafu3e9zyOJC0+aGZOXbYGIn6bPync5lF9s3b7ujdrrlcc2iK/R" +
           "m8I3\nv1YY6dSi3YfowgtrEznuH5a7V0awI3xn6qyClzxW80+n/Us/Jf1LF5" +
           "52Zs2foqQ0z43BRrezj7K0\nbQSuFcBo5ZivEp6ctSPZ+jSOtVxaMqr9PMov" +
           "A4n7Q5PuB4cntYdvDVXbYUX52bdwBephi6rgtxl+\n5xLxw/8isRFBk3vjL6" +
           "ehWOlhXRpKnktwBYnQjl7hfrM1+SpXr62lNCa/Abl59j1/fPDFvini8MVN\n" +
           "6XmTLisH54jb0t6tHZAwp5AEPvqxM+bcd1XfC8Pu1Y7oZYyUjpmaiguIbgq4" +
           "QfZlPUaq/WuuhQte\nfikWtJmerY17Bzsx98DGBY9ajU/wq53ynnQFfvif1v" +
           "XghbAAXm7ZdETji60Q18MsvgIG7phLDcav\nrPlt1DnqiDnj/HBxDrbcyyXh" +
           "gCLutGUi/wbdxBX+/D8AAA==");
    }
    
    
    public static void
      main(
      String[] args)
          throws Exception {
        myRunnable myPhone =
          new myRunnable(
          );
        SJSocketGroup.
          executeExportable(
          myPhone);
    }
    
    public Phone() {
        super();
    }
    
    final public static String
      jlc$CompilerVersion$jl =
      "2.3.0";
    final public static long
      jlc$SourceLastModified$jl =
      1329255889000L;
    final public static String
      jlc$ClassType$jl =
      ("H4sIAAAAAAAAAK0XW2wUVfTuo9vXSrulVKQvHjXQCLtGoxFrlKaB0LKEui1E" +
       "SsgyO3t3d8rdmXHu\nne2ABlGMIB8mBnxG4cfEaPhQiPqhURPAtz+YgD/wg1" +
       "ESxciHkRg0nnvv7GtmqTGxydzex3m/98RV\n1EQtFKeYUs3QZ+Nsj4mpWI3M" +
       "LFYZjU9NTCoWxdkxolA6DQ9pddff2rZ3Y09tDKLADOrSjVGiKXS6\nYBl2vj" +
       "Bd0KhjoaWmQfbkicFcij4a96+4MffNwYneEOqYQR2aPsUUpqljhs6ww2ZQtI" +
       "iLGWzR0WwW\nZ2dQTMc4O4UtTSHaXgA0dGBMtbyuMNvCNIWpQUocsIvaJrYE" +
       "z/JlEkVVQ6fMslVmWJShzuSsUlIS\nNtNIIqlRNpJEkZyGSZY+ivahYBI15Y" +
       "iSB8CeZFmLhKCY2MDvAbxNAzGtnKLiMkp4t6ZnGRr0YlQ0\nHtoEAIDaXMSs" +
       "YFRYhXUFLlCXFIkoej4xxSxNzwNok2EDF4aW3JQoALWYirpbyeM0Q4u9cJPy" +
       "CaBa\nhVk4CkOLvGCCEvhsicdnNd7aEon+dXjyj6XgcZA5i1XC5Y8A0oAHKY" +
       "Vz2MK6iiXidTt+dHy73RdE\nCIAXeYAlzOjtH2xNXvlkUML0NoDZImIxrd64" +
       "t6//3OgPrSEuRotpUI2HQp3mwquT7suIY0J091Qo\n8sd4+fHT1Gfb97+Nfw" +
       "6ilnEUUQ1iF/Vx1Ir17Ji7b4Z9UtOxvN2Sy1HMxlGYiKuIIc5gjpxGMDdH\n" +
       "E+xNhRXE3jERQs3wBeBTkfxr5wtDLZMFQ8dxOsvQgwxTRhPUUhOmbWVtnIBY" +
       "ZZpuiyBPY8c0LDjm\nEyZHgbNSNAlOlAk4nNeCuUAA1OzzphyB+NxokCy20u" +
       "qbl796fP2mZw9JB/Kgc6WE8JKM47WMAZtz\nQIGAIL2QR6e03qhlKXt41jhP" +
       "nut/5XPldfAF2IRqe7FQOTAX5isg3TVvURmrpuQ47BSImLTavf/K\nkle/e+" +
       "tsEIUaFpZk5XKDYRUVwmOjnEldLjvvC4TUkDewG/H+9fDmU+e/vriqGuIMDf" +
       "kyz4/JM2e5\n1/SWoeIsVKYq+Zf+3Pjbkaa17wVRGNKR21qBMILsHvDyqMug" +
       "kXI14rqEkqg951O8jYGB5moU5muU\nL50yPMAX3R4BRSG7fiBy54WP2s8Kjc" +
       "s1r6OmOE5hJjMoVvX/tIUx3F98efLIC1cP7ghBzpqm9DlD\nEdPOEE11AOXW" +
       "+qTj4mV52PxycqTzuTX0feHiVq1YtJmSIRiKtEKIMYezaSaqVKymIopCBJaI" +
       "ZqCg\nQTKkCRCSupqBEoRng5IRX9x99MXh1y7womIKw/Tw5BOSIkdcDAX4us" +
       "r3yM/9PIK7q2pDGdgtFYgO\nT+2c2HVoudC8ltxq9+ASRyV/Sm7gXaYcE8XM" +
       "Y7+fPta2tCrh6lpyA2Jd5tqW71eUKVfFlhhBsV8I\nbUQIzG0WT9m6zg1bfp" +
       "K6aEa80kXhkTuq/2ZVX3Ssg49ciz6jnNkpa3NXvVPX63bxnuPf4+F1UbVB\n" +
       "YWllhrmG4BImFRMHXXkttGbe6rBZNMlq9oSmHhpeubr9J8iemxSGmHuZwjAR" +
       "6Fx+zrNFVAzlP1eM\nQY9RvPLESr0PhwraF0Gek26x8PX+eqSRWvNA1lr1go" +
       "qo6xUCdICJWuDrgm+B2znEf/4Y40uXTGy+\n3D1vKP9rnI8xFLJsnfob+aSl" +
       "FaFPltxG/vzAGz+eupzqln6W084K38BRiyMnHsGl3eSRtmw+DgL6\nzB3LTu" +
       "xLXcoEXekeYChcMrSsUCDpz5G6w7qK8fi3Er60a7x0I+P5kywokgyqGBWzaG" +
       "PjVfubd1wS\nnVEa62Ro6Frww54hUezDGYWWg7F+zvSPkXXToZCzraLVbfAN" +
       "zqNVOdM7q0VAjpINZPc6u64wpdW9\nH289dv1bdkk4u9qsOJ0+x1/Wtik1ff" +
       "S+86VY5J3jxSBqnkGdYvJWdLZNITbvGzOgNB1zL5Polrr3\n+jlYDn0jlfzq" +
       "80ZbDVtvm6zNtDCryzHRGXc4AWTyjdogDBjQ0nSFyJoL8UCwnmcFATNhSjtv" +
       "hrwB\n7/HtjOMrb64nFlY9MUZgnOIllz+tdXwu4edRKZmg+T+ktgF6qJxtg8" +
       "CQtnXmD3G/FusdFZt8PhQM\nUpCdRQgMkZy+FA3U2KbuBizaJKZLPlks9v1O" +
       "lL9m1OXndq06bca+lClU/sXRDGN/ziak1r01+4hp\n4ZwmhGuWzjbFv/3Q/x" +
       "pNuUwEYPUshH1C4hwQ4wzH4aenRbTYTuAfeNyOsDIPAAA=");
}
