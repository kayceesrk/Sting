package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client2 {
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
    
    public Client2() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1319723626000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0abWwcxXX2bJ8/iT9iQojzQRIDCU3OBQECHEgTkxCHi2Ns" +
       "E8AUnPXe+LzJ3u6y\nO3c5p0ADEZBCmypKAuWjTZGoUihQEgpFakUrFZqWr1" +
       "ahSlIk+ENFQYWqqGpBLdC+N7M7+3HnI4Wm\namks3fObfTPvzbx5X7szD79D" +
       "alyHpFzqurplbkixCZu6HFqjG6jG3NTg6n7VcWmmx1BddwgII9r6\nj/R1+1" +
       "pvXpUgyjBpM61lhq66Q+OOlc+OD43rbtEhp9iWMZE1LOZxLOFxwfwPNr1w2+" +
       "qOKtI8TJp1\nc5CpTNd6LJPRIhsmTTmaG6WOuyyToZlh0mpSmhmkjq4a+mbo" +
       "aJkg2NWzpsryDnUHqGsZBezY5uZt\n6nCZ/sM0adIs02VOXmOW4zLSkt6gFt" +
       "SuPNONrrTusu40SY7p1Mi415EbSSJNasYMNQsdp6X9VXRx\njl0r8Tl0b9Bh" +
       "ms6YqlF/SPVG3cwwMic+Qq6481LoAENrc5SNW1JUtanCA9ImpmSoZrZrkDm6" +
       "mYWu\nNVYepDAyY1Km0KnOVrWNapaOMDI93q9fkKBXPVcLDmHkxHg3zgn2bE" +
       "Zsz0K7tTbZ9OHt/e+dAjsO\nc85QzcD5J2HQ7NigATpGHWpqVAx8P5/a1XtV" +
       "fmaCEOh8Yqyz6LPs1B9enn7zJ3NEn44yfdZyWxzR\nPjh35qyDy35XX4XTqL" +
       "MtV0dTiKyc72q/R+ku2mDd0yRHJKZ84k8Hfn7VlofoHxKkrpckNcvI58xe\n" +
       "Uk/NTI+H1wKe1k0qnq4dG3Mp6yXVBn+UtHgb1DGmGxTVUQO4rbJxjhdtQkgd" +
       "/BT4dRHxV4+Akfbl\nKtPGB4Gn22Po1GRnpdwNjKQYdZnb5Tpal513MnnaNY" +
       "r9wBi6yg0oopwpmxQFljgz7m4G2OYqy8hQ\nZ0Tb+/pz16+49CvbxOahwXkz" +
       "ZOQkISjlC0p57ImicL5T0SyF2pY5jjqB7lK86eCsuw+o34RNAGW4\n+mbK16" +
       "psqkYIg86qGE16Al/sBUwFUxnR2re8OeOelx98NkGqykaUtHy40nJyqoFG4b" +
       "tQmycuTgFb\n6oxbdDnZf7x9zeOHnn91QWDbjHSWuFzpSHSZeXG9O5ZGMxCS" +
       "AvZ3/W3Vn3bWnP9EglSDH0IkYirY\nD7j17LiMiOt0+2EI11KVJo1jJQtvYK" +
       "CgTaEFI2xC0CJsA/aiPTZBHsHe35r8/OEfNz7LV+wHu+ZQ\nVBykTLhOa7D/" +
       "Qw6l8PzVb/Tv3P3ObVdXgbPatthzRpJ2ftTQtSIMOSnqbTi9DJrN2/u7W7Yv" +
       "dp/k\nW1yv53J5po4aFKKzahjWJpoZYTw8tYZCIY9AoImmUYhkYJ4jBjASa7" +
       "WVAphnmViRmt6+686F9x3G\naGJzxUxDr+MzJUX+oFNBuKCEiO1ZaMHtwbLB" +
       "/zeKBTQtHLxm9fpt8/jKw+wWeY2iZBj4wtkVfWEl\nZh2wGWZB+AlMZv0dRy" +
       "5bc+UVXSIgnlmRRR+4c4bzCcbv7jWXPn3iITOBVpJ0N0iXT5OEyxhZkPZZ\n" +
       "epaHTYENrh4UJD8pLPr4BQSC9ev/etP+X+1uAsHDpFZ3V+qmauBuu33Ce8sk" +
       "jBiLzU9f/q33X2Sv\ncdsM3AWnP7NYGujWqSFPPu9QoTX52J5cgtQOkxae9F" +
       "WTrVONPFruMKRtt8d7mCYnROjRFCzyTbcM\nBzPjrhoSG3fU6pC2q7E34nXC" +
       "N3mf5n+Iv4/wh4aDDZEY2nqsnA3ZxDnlEgqLVhnN2EVFsXHYeXzw\nbA7nCv" +
       "tLMBCNKmak1nb0goq1ExYnBZ3xIol3nspIh9xEJ28yPUexqvPMjkcKhXvEUj" +
       "DYzth+S9NI\noWkwkbEPHFqy32QzX+ebVOuValgvBSEEcqgBOuRJ2AFfq8RV" +
       "GtyI5iVKRYQmyElGBmd2FvCYX9ES\nxfAdtYN7r8jueiDheXOjzcPYaYE/B/" +
       "NyOy83c1ZGH9N5HAIP/6D51DOfeHt7i8gEQawBL/h4BsHz\nk5eTLc9f+95s" +
       "zkbRkEePF4oWycBweiWNLKdZvZxCkkx1spSJ9A3LqsAjxOIC464D3+l/d5uv" +
       "lItt\njNOVtmSAalQv0PgMMBqdU2HYGsCh3gQjzuVNXeMmKHjsPWfrkTtv7v" +
       "oLBP+rSR01aA6qDLCY2VdH\nCl+I8kMOVC4rRAdwrcac4DnkO1JMCf568H9f" +
       "8B/KGp93CnmnSnknVp6xZMmH887neQLWtxx00lFh\ngFr/7D2nnfvqLZDHe0" +
       "kD1oB9ebT7NJmCQULFop3nK8/367As7AtiQYMo/PuC8ouQmgPwNhLIhIKH\n" +
       "qvAW0SiQFFIYOQEWG8yGs18H8+1gn3AvGJlbiexJTS7RTZ1dJMU1s6O3GKhw" +
       "Y084U2R1rWR4ESLr\nEYC0OncjGAXzIhaSGwIyAk0SzkbkiwiuYRHzkD2Gwz" +
       "3qTStDewzLDOgvVV5KJB7xpUSeePqpZ1AP\nxfgisgHBRgTGpyAcCQiMVGuW" +
       "PSFJu9nRxWiwrFBLbsC4ZHQfIrzFJhWO4DpJWMBCQVh1RQ5G/foB\nuA9wlN" +
       "wabnoK47lQsrqQhaJXnJU3a59be+yJXMvmYMvDW1rQ4QUvlOEuBvfE0cvzOr" +
       "4QQdSZlObN\ndcooNnllgh2kmG5EvoxgC1iAQdUC7YHqVdK3g4JkgSKmscJx" +
       "LGeVamYgYmTX4SMLZnByucdyXVt9\nhkoLIrcguBVyPRcohe1jZFZMmPf5RI" +
       "qZGn0gBWyTAqCEmBFjgkqWHFpCLU85U3m3FZksbMfaAnUc\nPRNM6reIfA0B" +
       "6KJe9pT0R1ioaMd99ze5MWIsDXxkD6Z/OfQtRHYi2OWzxqQt6UsZmR5hzaPx" +
       "cisz\nwZ0h1PIDayAEXrskn78HS0Dw9XJrQ7AjWFQwMwS7JWFP2SmhSQVTEi" +
       "25Nfcei3m8FSXcJQkTYIqR\nCQ5aeUejKyF1CT8ONf8tU9wX3sQavgGSdl7Y" +
       "oMccNF0zk/KMrx9UBSoLtbxdrIKS1meRGGKh70eS\nxaA2Di/lBrr+FIl7w2" +
       "thOHKTHvFnRPYjeFxQL7FU6eHKU2Fqo8oYzdks0uO+oAeCp44T/iMEYzLC\n" +
       "wvCONcN+Dln4lgXVhh4qN74XjoPScPzXMYgVPuqbjSbaksHtiLyA4EWGH5w5" +
       "FR1HWhbZBC94UkYO\nXmtTawBALMN/Ht8aeKl0ApfoQ+QggpcR/EYSToIpyY" +
       "SH48v0mYLIYU6A9IsC/ffBlnCdi4UrLoh/\n+Tszj12GeD3s8ak5gMiVCK5C" +
       "MByUoIiIQgtBUNs1IxIUb2WLPgRBVXc05R6CsXJVHoKgrlmMiF87\niVfIY1" +
       "yiIQiqpN2IBHXVpyq4EOHvOBMIgnrnQkSuR3BDlMC1IioUBDeVK10QyBJDBG" +
       "hRYkQI4doD\ngawZRAzn9n5HhKAkEDnGGWspIncjuAfBMUmXexD5NoL7j5mM" +
       "yVMyInsRfPfTCt8XlfFwkGkReRTB\n9xE8FuRPRIIo+oNyWRHBk+USIoL/km" +
       "zw/0s4mjyI4Lkg9SEiMheCl8qlNAS/DhIZIuWy0uTpCpHD\nZQhTogRuyq+U" +
       "yUbLPgPZCMEbxzPO8YxTKvx4xjmecf5XCZ+tjBNgPAquFkcKkWM3flY23ztK" +
       "JgjF\nUbJ/Duw6oe8Ysas0/BrQbVe+23Sr+sw14kSlLXpgvsLM587Zc4Qu/E" +
       "KTVubGRj2z7MUGLVARzKeB\nwIQ4DASxiyseka3hBxDBcWnV4NKFpy9q/H2C" +
       "VE9y6aLVezhAWd4xQ2cw0Fv9l29jzIkpJT6f1kLH\nZVXj+i8S/BhVnLyWXK" +
       "iKDuqOnrc2ONGJ8i3s4BNoJuI6Thv8phLxx/8jsRVBm7g0AUAReX2yawJl\n" +
       "if6RP7bhDXtq8Ia9oqhRGz82IH/F8b5YlR6E9zt6TmfyyGTH7AfeePz1gXZh" +
       "A+J62fySG17hMeKK\nmX/uCRLmVpLAez/zubkP3zjw2qh3NKjgaUPB0jO4OO" +
       "WGqFXHG4olFYu/0+F3hqfYM8opttSRePae\nz0jS5Zf/Kiu29H4av5EklLW/" +
       "qvPdxI+mdfJLNtWjqisPCyMX+0rv7UWu4/F5NshVnQy/OR+zqlfK\nGgCWic" +
       "pXiwrBk3tl+yQrv1DEEVi/Qc0s42Wb8iVxiKncCHYCs0X0jmKJq5daGT+Kwn" +
       "NonybusOhW\nSl6bBGLpFRUUYIvpclmf0PxDV2iUe/GzizwZi35uEjcrihW3" +
       "GnkUOaet3ncrbo0lNqmElBN5Aiqt\n9S6y4en/9LgbeDcmtXkH1y/4md36S2" +
       "E1/q3GWjy7zRtG+B5HCE/aDh3T+ebXilsdNp/rQ4w0x27T\nMVLno1zPD4qe" +
       "j/D7UtgTW49y7d9fVP4Ju6Gd4owrAAA=");
}
