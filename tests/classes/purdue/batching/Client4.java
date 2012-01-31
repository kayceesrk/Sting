package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client4 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1XTWwVVRS+fW2BttBWARFaQPkpYPBVjKBA+Wstv49SfUUi" +
       "BGGYd/s6MG/mOXNf\naQ0hRmNSf9KIEYmGhI2EhbrAxOjOlWmiiQsXBheucC" +
       "GJLowxLsTE892Zd+/MY/okYhM07eLruXPu\nPX/33HPO+/BnVu97bIXPfd9y" +
       "nZNpMVrkfhrLgMruzXIxQOQxc+Lbro8d0X49xWoybGaBF05wzxfs\nvsxJY9" +
       "joLAnL7uxxbZubgiRtHvHY6qpSJSuQzIK/Gim53hyy7JxgqzPl453yUKc63h" +
       "k7Dk3LKzQB\n3RMnyRToCpScm5m9cij/9vspxujE/KJrj+ZtV4Qngj07Vn56" +
       "MPPj50uDPW0Jew5IqcfMmxvaF3+z\n44eGWpg8q+j6FrwW7P5M+VAQkf6Qs3" +
       "mkWBwpUqg7EK00eGkdLX/FQafg5qxByzhh84zli5stK9d9\n8tN4qwxJnU1f" +
       "BGuNRBp74Pnavxenvy/sZi9+9dzvS6TQGvN5dpbVSKPmaSkZyznFcxA/e032" +
       "6N7j\nY8tqKRjF03W4INq6qtqldvO8lXSnM4Th5Tm5cG/ggm04+c6s8CwnDy" +
       "c6qsiMiNxkX5i43P/LGG4H\ntqeKI371LHuam9wa5pUW4XLXVzm2n2gjT4Er" +
       "FEqOZRpCZeqV9S9fe+elzt9SrPYIm8VtXuCOoEew\n5EjMMcM8NeAZJu8NNm" +
       "zOsKZCIBNiBJuvsyTIaJnIKk5wrlW5iP9z9f9iySurS0Nd+lZ1qZ0PdXX9\n" +
       "uWwjPC2Sy5soTG1VDhgNX7zXseH7V1Ksbg9rtC2H95XwujOsOcdN28A99diG" +
       "L01opXwftGzeZxR4\nuG4scDHk5tQXKK2foITVOgeGPG7Qq24KiDQ4gs0hZ7" +
       "U1Unw72dsm/uH1CPZgNXaodUaX5Vhiq1LX\nIm4/iQSbV/FFCoWoFUrgVhAd" +
       "gFWCzfJPUZ4IepVldqNmA9YoxmMglgGWi1jGqB2HozsaHDfHe2zX\n0fyvq7" +
       "sSq7rSldiXMD4NwuOVckE8AlgHePQOGNc0Q7A60y2OKtZ5cXudiDIrslIXkF" +
       "aCLoLYCNg0\nqXLA44qxWkSaiOEHJR/xLTeQPqKh+Z7oMgxYnYje0RYRKWiV" +
       "okKry9LmVXxRvmxX4raLSM0dtqid\nkJh+zxWu6dpP0vOkt+P4tiFcT7DFk7" +
       "FCSxeI8AuPb1TaZGx6ATspEWxuDPMew9b8cYqTaouBNb2e\n53q7DSdHhSP/" +
       "DD7BkoVJn5V7e8oCa1pB7ANkBKuXCpWyq+RQhbIearJ8RCg1c+MflII+pSAl" +
       "2KIK\nIYi1ktAaWYVRmiu39ebydCsHhrnnWTlt1HcgsoABipDaqfgf0XmlD9" +
       "dfvuumWM40ypM9mHXU0Rsg\nngUcLotGI1b8bTRfxETLotzt5kblm4isyvVV" +
       "K/G4LkB/aBcAB5N8AxzSTmnLAEcU41KiSUgpbVKw\nUldjTIUdN+KMo4oxSq" +
       "kYMzDrljyT76QOFjznyPJfMfFq9BLr5QUo3hPRhB70kLpOLh0mXz+FikIW\n" +
       "WYW3WOuV1N2lKOnabhWRNYd4rmRzyudmRYfHZ9JxSFMv4lcQcrwYDbi7XEO9" +
       "8JrPotwmQwheKIrY\njot6B+DsXc6wJ2OsiXraQnEYcGloKFKztiLd+oNo/V" +
       "ABx0ZLxntOmSyH2wzWSsBrICS8LtjskIuE\nUzfCTtNkrHQUDMtJ7yegGoB/" +
       "odx6n4ZDnUp9IN4EnAO8pRgLyCTVL3A+YU8ziPOSQd0LCmlCbK/4\nudNv00" +
       "y227Vz3KPB9/qXZ3r3vToW/B5x9KQXm/KCkR4xsC2aK9eVsGmRnEBD1fUTIJ" +
       "YAlgIe0EMf\niGC0AehpqgWEHpcSxyyAnqNuZ8ACrE2aqwB6kngYRHlaCX6a" +
       "TvFQBNBzibwkPcnc0YgDQsZsG0BP\nGFtAdAN64oztIIJhALAraUoAqG7Oxk" +
       "EE3TzGiLZ5gGrPQbnsBzwVY9SkQExxc5CBOAY4DpiSznQJ\nxCAgP2U6Ju9+" +
       "IAoA506VX43r8HRTAyF/s8rnPqxbFQhdeF9IakCAM0m9B3CXNJCpaTmAMd1l" +
       "QARN\nAvBGUvcAjOueASKpAUzeGUCcT2A0xxkXAO8mVPGN/4MqDrg8XamnK/" +
       "Wtyqcr9XSl/m9Xak1Jy5v/\nApjXa6LfGQAA"));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 20101);
            SJSocket client1Socket = null;
            try {
                client1Socket = ss.accept("client1");
                System.out.println(
                  "Client2 accepted connection request from Client1");
                String str =
                  (String) SJRuntime.receive("client1", client1Socket);
                String ii =
                  (String) SJRuntime.receive("client1", client1Socket);
                String d = (String) SJRuntime.receive("client1", client1Socket);
                System.out.println("Client2 received:\nstring: " + str +
                                   "\nInteger: " + ii + "\nDouble: " + d);
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
    
    public Client4() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1319724421000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0abWwcxXX2bJ8/iT9iQojzQRIDCU3OBQECHEgTkxCHi2N8" +
       "JoApOOu9sb3J3u6y\nO3c5p0ADEZBCmypKAuWjTZGoUihQEgpFakUrFZqWr1" +
       "ahSlIk+ENFQYWqqGpBLdC+N7M7+3HnI4Wm\namks3fObfTPvzbx5X7szD79D" +
       "alyHpFzqurplbkixCZu6HFojG6jG3FRmdb/quDTbY6iuOwiEYW39\nR/q6fa" +
       "03r0oQZYi0mdYyQ1fdwXHHyo+ND47rbtEhp9iWMTFmWMzjWMLjgvkfbHrhtt" +
       "UdVaR5iDTr\nZoapTNd6LJPRIhsiTTmaG6GOuyybpdkh0mpSms1QR1cNfTN0" +
       "tEwQ7OpjpsryDnUHqGsZBezY5uZt\n6nCZ/sM0adIs02VOXmOW4zLSkt6gFt" +
       "SuPNONrrTusu40SY7q1Mi615EbSSJNakYNdQw6Tkv7q+ji\nHLtW4nPo3qDD" +
       "NJ1RVaP+kOqNupllZE58hFxx56XQAYbW5igbt6SoalOFB6RNTMlQzbGuDHN0" +
       "cwy6\n1lh5kMLIjEmZQqc6W9U2qmN0mJHp8X79ggS96rlacAgjJ8a7cU6wZz" +
       "NiexbarbXJpg9v73/vFNhx\nmHOWagbOPwmDZscGDdBR6lBTo2Lg+/nUrt6r" +
       "8jMThEDnE2OdRZ9lp/7w8vSbP5kj+nSU6bOW2+Kw\n9sG5M2cdXPa7+iqcRp" +
       "1tuTqaQmTlfFf7PUp30QbrniY5IjHlE3868POrtjxE/5Agdb0kqVlGPmf2\n" +
       "knpqZns8vBbwtG5S8XTt6KhLWS+pNvijpMXboI5R3aCojhrAbZWNc7xoE0Lq" +
       "4KfAr4uIv3oEjLQv\nV5k2ngGebo+hU5OdlXI3MJJi1GVul+toXXbeyeZp1w" +
       "j2A2PoKjegiHKmbFIUWOLMuLsZYJurLCNL\nnWFt7+vPXb/i0q9sE5uHBufN" +
       "kJGThKCULygl2J9NFIXznYpmKdS2zHHUCXSX4k0HZ919QP0mbAIo\nw9U3U7" +
       "5WZVM1Qhh0VsVo0hP4Yi9gKpjKsNa+5c0Z97z84LMJUlU2oqTlw5WWk1MNNA" +
       "rfhdo8cXEK\n2FJn3KLLyf7j7WseP/T8qwsC22aks8TlSkeiy8yL692xNJqF" +
       "kBSwv+tvq/60s+b8JxKkGvwQIhFT\nwX7ArWfHZURcp9sPQ7iWqjRpHC1ZeA" +
       "MDBW0KLRhhE4IWYRuwF+2xCfII9v7W5OcP/7jxWb5iP9g1\nh6JihjLhOq3B" +
       "/g86lMLzV7/Rv3P3O7ddXQXOattizxlJ2vkRQ9eKMOSkqLfh9LJoNm/v727Z" +
       "vth9\nkm9xvZ7L5Zk6YlCIzqphWJtodpjx8NQaCoU8AoEmmkYgkoF5DhvASK" +
       "zVVgpgnmViRWp6+647F953\nGKOJzRUzDb2Oz5QU+YNOBeGCEiK2Z6EFtwfL" +
       "Bv/fKBbQtDBzzer12+bxlYfZLfIaRckw8IWzK/rC\nSsw6YDPMgvATmMz6O4" +
       "5ctubKK7pEQDyzIos+cOcs5xOM391rLn36xENmAq0k6W6QLp8mCZcxsiDt\n" +
       "s/QsD5sCy6zOCJKfFBZ9/AICwfr1f71p/692N4HgIVKruyt1UzVwt90+4b1l" +
       "EkaMxeanL//W+y+y\n17htBu6C059ZLA1069SQJ593qNCafGxPLkFqh0gLT/" +
       "qqydapRh4tdwjSttvjPUyTEyL0aAoW+aZb\nhoOZcVcNiY07anVI29XYG/E6" +
       "4Zu8T/M/xN9H+EPDwYZIDG09Vs6GbOKccgmFRauMZu2iotg47Dw+\neDaHc4" +
       "X9JRiIRhUzUms7ekHF2gmLk4LOeJHEO09lpENuopM3mZ6jWNV5ZscjhcI9Yi" +
       "kYbGdsv6Vp\npNA0mMjYBw4t2W+yma/zTar1SjWsl4IQAjnUAB3yJOyAr1Xi" +
       "Kg1uWPMSpSJCE+QkI4szOwt4zK9o\niWL4jtrM3ivGdj2Q8Ly50eZh7LTAn4" +
       "N5uZ2Xmzkrq4/qPA6Bh3/QfOqZT7y9vUVkgiDWgBd8PIPg\n+cnLyZbnr31v" +
       "NmejaMijxwtFi2RgOL2SRpbTMb2cQpJMdcYoE+kbllWBR4jFBcZdB77T/+42" +
       "XykX\n2xinK23JANWoXqDxGWA0OqfCsDWAQ70JRpzLm7rGTVDw2HvO1iN33t" +
       "z1Fwj+V5M6atAcVBlgMbOv\njhS+EOUHHahcVogO4FqNOcFz0HekmBL89eD/" +
       "vuA/lDU+7xTyTpXyTqw8Y8mSD+edz/MErG856KSj\nwgC1/tl7Tjv31Vsgj/" +
       "eSBqwB+/Jo92kyBYOEikU7z1ee79dhWdgXxIIGUfj3BeUXITUH4G0kkAkF\n" +
       "D1XhLaJRICmkMHICLDaYDWe/DubbwT7hXjAytxLZk5pcops6u0iKa2ZHbzFQ" +
       "4caecKbI6lrJ8CJE\n1iMAaXXuRjAK5kUsJDcEZASaJJyNyBcRXMMi5iF7DI" +
       "V71JtWlvYYlhnQX6q8lEg84kuJPPH0U8+g\nHorxRWQDgo0IjE9BOBIQGKnW" +
       "LHtCknazo4vRYFmhltyAccnoPkR4i00qHMF1krCAhYKw6oocjPr1\nA3Af4C" +
       "i5Ndz0FMZzoWR1IQtFrzgrb9Y+t/bYE7mWzcGWh7e0oMMLXijDXQzuiaOX53" +
       "V8IYKoMynN\nm+uUEWzyygQ7SDHdiHwZwRawAIOqBdoD1aukbwcFyQJFTGOF" +
       "41jOKtXMQsQYW4ePLJjByeUey3Vt\n9RkqLYjcguBWyPVcoBS2j5FZMWHe5x" +
       "MpZmr0gRSwTQqAEmJGjAkqWXJoCbU85Uzl3VZkx2A71hao\n4+jZYFK/ReRr" +
       "CEAX9bKnpD/CQkU77ru/yY0RY2ngI3sw/cuhbyGyE8EunzUmbUlfysj0CGse" +
       "jZdb\n2QnuDKGWH1gDIfDaJfn8PVgCgq+XWxuCHcGigpkh2C0Je8pOCU0qmJ" +
       "Joya2591jM460o4S5JmABT\njEwwY+Udja6E1CX8ONT8t0xxX3gTa/gGSNp5" +
       "YYMeddB0zWzKM75+UBWoLNTydrEKSlqfRWKQhb4f\nSRYZbRxeyg10/SkS94" +
       "bXwnDkJj3iz4jsR/C4oF5iqdLDlafC1EaVMZqzWaTHfUEPBE8dJ/xHCMZk\n" +
       "hIXhHWuG/Ry08C0Lqg09VG58LxwHpeH4r2MQK3zUNxtNtCWD2xF5AcGLDD84" +
       "cyo6jrQssgle8KSM\nHLzWptYAgFiG/zy+NfBS6QQu0YfIQQQvI/iNJJwEU5" +
       "IJD8eX6TMFkcOcAOkXBfrvgy3hOhcLV1wQ\n//J3Zh67DPJ62ONTcwCRKxFc" +
       "hWAoKEEREYUWgqC2a0YkKN7KFn0IgqruaMo9BKPlqjwEQV2zGBG/\ndhKvkM" +
       "e4REMQVEm7EQnqqk9VcCHC33EmEAT1zoWIXI/ghiiBa0VUKAhuKle6IJAlhg" +
       "jQosSIEMK1\nBwJZM4gYzu39jghBSSByjDPWUkTuRnAPgmOSLvcg8m0E9x8z" +
       "GZOnZET2IvjupxW+Lyrj4SDTIvIo\ngu8jeCzIn4gEUfQH5bIigifLJUQE/y" +
       "XZ4P+XcDR5EMFzQepDRGQuBC+VS2kIfh0kMkTKZaXJ0xUi\nh8sQpkQJ3JRf" +
       "KZONln0GshGCN45nnOMZp1T48YxzPOP8rxI+WxknwHgUXC2OFCLHbvysbL53" +
       "lEwQ\niqNk/xzYdULfMWJXafg1oNuufLfpVvWZa8SJSlv0wHyFmc+ds+cIXf" +
       "iFJq3MjY16ZtmLDVqgIphP\nA4EJcRgIYhdXPCJbww8gguPSqszShacvavx9" +
       "glRPcumi1Xs4QFneMUNnMNBb/ZdvY8yJKSU+n9ZC\nx2VV4/ovEvwYVZy8ll" +
       "yoig7qjp63NjjRifIt7OATaCbiOk4b/KYS8cf/I7EVQZu4NAFAEXl9smsC\n" +
       "ZYn+kT+24Q17avCGvaKoURs/NiB/xfG+WJUehPc7ek5n8shkx+wH3nj89YF2" +
       "YQPietn8khte4THi\nipl/7gkS5laSwHs/87m5D9848NqIdzSo4GlDwdKzuD" +
       "jlhqhVxxuKJRWLv9Phd4an2DPKKbbUkXj2\nns9I0uWX/yortvR+Gr+RJJS1" +
       "v6rz3cSPpnXySzbVI6orDwsjF/tK7+1FruPxeTbIVZ0Mvzkfs6pX\nyhoAlo" +
       "nKV4sKwZN7ZfskK79QxBFYv0HNMcbLNuVL4hBTuRHsBGaL6B3FElcvtTJ+FI" +
       "Xn0D5N3GHR\nrZS8NgnE0isqKMAW0+WyPqH5h67QKPfiZxd5Mhb93CRuVhQr" +
       "bjXyKHJOW73vVtwaS2xSCSkn8gRU\nWutdZMPT/+lxN/BuTGrzDq5f8DO79Z" +
       "fCavxbjbV4dps3jPA9jhCetB06qvPNrxW3Omw+14cYaY7d\npmOkzke5nh8U" +
       "PR/h96WwJ7Ye5dq/v6j8E/4NhU6MKwAA");
}
