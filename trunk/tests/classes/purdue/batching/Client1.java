package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client1 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1XXWwUVRS+u+0CbaGt/AotoPwUMLgrRFBo+etafpdSs0Ui" +
       "BGHYvd0OzM6sM3dL\nawgxGpP6k0aMSDQkvEh4UB8wMfrmk2miiQ8+GHzwCR" +
       "8k0QdjjA9i4vlmZu+d2U7XRmyCpn34eu6c\ne37uueeec/bDn1nCsdkahzuO" +
       "bplnk2KkxJ0klh6VPZjlop/IU7nxb7s+NkX77TiLZdjsIi+e4bYj\n2OLMWW" +
       "1IS5WFbqTSlmHwnCBNncM2W19Tq8vyNDPvL+ZqTuQGdSMv2PpMRTzlCqWkeC" +
       "okDkurqywB\nrTNnyRXY8oxcmp29cazw9vtxxkhiUckyRgqGJXwJb8+etZ8e" +
       "zfz4+UpvT1vEniOu1lO5u1vbl3+z\n54eGOrg8p2Q5Ok4t2IOZipAXkT6f0z" +
       "lcKg2XKNQdiFYSvKSKlrPmqFm08vqArp0xeEZ3xN2WtZs+\n+Wms1Q1JvUFf" +
       "BGsNRBp7cPKNf69OfV/azV786rnfV7hKY7nn2UUWc51aqLRkdPMcz0P93A3Z" +
       "kwdP\nj66qo2CUztfjgmjruhqXmu7mBX3CnSKUHTWkAkLbjSvj1/t+GYUQvI" +
       "uXhsnk2pp5ZOajDG6pIXOY\naK1AcSkWy6ae04RMxBtbXr71zkup3+Ks7gSb" +
       "ww1e5KagHF9xwgu9oZmFVFZouXP9tpbjPd6Gzgxr\nKno6oUawRSoJvIR18z" +
       "TDZgnNLnC6yfkhdbZuFjrlefF/vvpfKtsV80nsT040H9/7SFfXn6u24eQl\n" +
       "CsF2illbDQGt4Yv3OrZ+/0qc1R9gjYZu8t4yHnOGNed5ztDgT9rQHNeFxZTe" +
       "A7rBe7Ui99eNRS4G\nrbz8AqOJccpPZbN/0OYaPeImj0iCI9g8OrzyxlXfTv" +
       "62iX94XYI9XIvtW53VpZu62CnNtYgpZpRg\nDwSXrjooWSNV7QTRAVgn2Bzn" +
       "HGWMoOdXYTcqNmCDZDwOYhVgtQjljtxxPLijwbTyPG1YpuJ/LaZe\nXgVbWP" +
       "XFj0yDsHm1XhCPATYBNt8D45ZiCFafs0ojknVZTK3lUE4FVvICklLRVRDbAN" +
       "snNQ54QjLW\ni0C30ByvtiO+lU7RS7R/92rpB6xeBO9ohwjUtWpVvtcVbQur" +
       "vsiz7JbqdotAcR3SqW+Qmj7bElbO\nMp6ih0mvxnQMTVi2YMsnY/meLhH+Fx" +
       "7eKK25sekB7KVEMLg2xNOaofhjFCfZ/zxvemzbsvdrZp5K\nRuEZfIInS6M+" +
       "y+MdqCiMtYI4BMgIlnANSmM36UBVxtLUTfmwkGYWhD9IA73SQFywZVVKEGup" +
       "oTWw\n8qO0wN3Wky/QrRwZ4rat55VT34HIAvopQnKn5H9E8tIerr9y102hnG" +
       "l0JdMYaqToHRDPAo5XVKPj\nSv4uGiRCqt1y3G3lR9w3EVhVKqsyYnNVgP5Q" +
       "RwAcjTob4Jg6lPIMcEIyrkW6hJRSLnkreTXadPhx\nJ8w4KRkjlIohB7NW2c" +
       "7xvdS7vOccWP4rLt4MXmLCvQDJezKY0AM2UtfMJ/3k66NQUcgCK/8W6+yy\n" +
       "vLs4JV3bRBXZ3CDPlw1O+dwsaV98NolDm3wRv4JwB4sRj7vP0uQLj30W5DZp" +
       "QvBiSYR2XFU7ABfv\nc4YxGWND8KQtFId+i8aFEjVrPdCtPwjWDxlwbNTdeM" +
       "+rkJVw57y1VPAaCBdeF2yuz0XCyRth52n2\nkzaKmm4mDxNQDcA/X2/CoTFR" +
       "pVIviDcBlwBvScYSckn2C8hH7GkGcdllUPeCQZoN26t+1/QZNI3t\nt4w8t2" +
       "kEvv3lhZ5Dr456PzxMNeOF5jtvaEUMDJ0mys1lbFrmzp6+6cQ4iBWAlYCH1L" +
       "gHwhttAGqa\nagGhxqXIMQug5qipDFiAjVFzFUBNEo+CqEwr3m/QaR6KAGou" +
       "cS9JTTL3NOKAcGO2C6AmjB0gugHp\nMGM3CG8YAOyLmhIAspuzMRBeNw8xgm" +
       "0eINuzVy77AE+HGLE4iGluDm4gTgFOA6alM10DMQAoTJuN\nybsfiCLAvFfj" +
       "N8M2bNXUQAiA+9yHVKsCoQrvC1ENCHAhqvcA7pMGMj0tBzCqugwIr0kA3ojq" +
       "HoAx\n1TNARDWAyTsDiMsRjOYw4wrg3Ygqvu1/UMUB12cq9Uylnmh8plLPVO" +
       "r/dqV2/W3+Cxvjdk6+GQAA\n"));
    
    public void run(int singleSession) throws Exception {
        final SJService c = SJService.create(invitation, "localhost", 1000);
        c.addParticipant("client2", "localhost", 20101);
        SJSocketGroup ps = null;
        try {
            ps = c.request(5);
            System.out.println("Client1 is connected to all participants");
            SJRuntime.pass(
              ("Hello, Client2 from Client1. I will send you an Integer and " +
               "a Double:"),
              "client2",
              ps);
            SJRuntime.pass("Hello2:", "client2", ps);
            SJRuntime.pass("Hello3:", "client2", ps);
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
    final public static long jlc$SourceLastModified$jl = 1319724208000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0aa2wcxXn2/H4QP2ICxHmQxIGkJOeCAAEOpIlJiMPFMTkT" +
       "wBSc9d74vM7e7rI7\ndz5HQAMISEFJi0igpLShElWgDS0JhSK1opUKTcurVa" +
       "iSFAn+UFFQoSqqWlALtN83szv7uPORlqbq\nI5bu8zf7zXzfzHzP3Zl975Ia" +
       "1yFJl7qubpnjSTZpU5dDa2ScasxNptcOqI5LM72G6rqDQBjWNn2s\nb9zfdu" +
       "uaBFGGSLtprTB01R0cc6x8dmxwTHeLDjndtozJrGExj2MJj4sWfDjx4p1rO6" +
       "tIyxBp0c00\nU5mu9Vomo0U2RJpzNDdCHXdFJkMzQ6TNpDSTpo6uGvoW6GiZ" +
       "INjVs6bK8g51N1DXMgrYsd3N29Th\nMv2HKdKsWabLnLzGLMdlpDU1rhbU7j" +
       "zTje6U7rKeFKkd1amRcW8gN5NEitSMGmoWOs5I+avo5hy7\nV+Nz6N6owzSd" +
       "UVWj/pDqzbqZYWRufIRccdfl0AGG1uUoG7OkqGpThQekXUzJUM1sd5o5upmF" +
       "rjVW\nHqQwMnNKptCp3la1zWqWDjNyarzfgCBBrwa+LTiEkZPj3Tgn0NnMmM" +
       "5C2lpf2/zRXQPvnw4ahzln\nqGbg/Gth0JzYoA10lDrU1KgY+EE+ubPvmvys" +
       "BCHQ+eRYZ9FnxcLvX5l660dzRZ/OMn3Wc1sc1j48\nf9bsQyt+01CF06i3LV" +
       "dHU4isnGt1wKP0FG2w7hmSIxKTPvHHG356zdZv0d8lSH0fqdUsI58z+0gD\n" +
       "NTO9Hl4HeEo3qXi6fnTUpayPVBv8Ua3F27Ado7pBcTtqALdVNsbxok0IqYOf" +
       "Ar+ziPhrQMBIx0qV\naWNp4On2Gjo12dlJd5yRJKMuc7tdR+u2804mT7tHsB" +
       "8YQ3e5AUWUM21CUWCJs+LuZoBtrrGMDHWG\ntb1vPH/jqsu/uE0oDw3OmyEj" +
       "pwhBSV9Q0mNPFIXznY5mKbZtheOok+guxVsOzX7goPo1UAJshqtv\noXytyk" +
       "Q1Qhh0TsVo0hv4Yh9gKpjKsNax9a2Zu1959LkEqSobUVLy4WrLyakGGoXvQu" +
       "2euDgFbKkr\nbtHlZP/+rnVPHH7htUWBbTPSVeJypSPRZebH992xNJqBkBSw" +
       "v/8va/5wb82FTyZINfghRCKmgv2A\nW8+Jy4i4To8fhnAtVSnSNFqy8EYGGz" +
       "QRWjDCZgStwjZAFx2xCfII9sFttZ898sOm5/iK/WDXEoqK\nacqE67QF+h90" +
       "KIXnr31l4N5d7955bRU4q20LnTNSa+dHDF0rwpBTot6G08ug2bxzoKd1x1L3" +
       "Ka7i\nBj2XyzN1xKAQnVXDsCZoZpjx8NQWCoU8AsFONI9AJAPzHDaAkVirrR" +
       "TAPMvEiuSpHTvvW/zgEYwm\nNt+YGeh1fKakyB90KQgXlRCxPRstuCNYNvj/" +
       "ZrGA5sXp69Zu2jafrzzMbonXKEqGgS+cW9EXVmPW\nAZthFoSfwGQ23X30in" +
       "VXX9UtAuLZFVn0gztnOJ9g/K4+c/kzJx82E2glte64dPkUSbiMkUUpn6Vn\n" +
       "edgUWHptWpD8pLDkkxcQCNZv/PMtB36xqxkED5E63V2tm6qB2nb7hfeWSRgx" +
       "FlueufLrH7zEXue2\nGbgLTn9WsTTQbVRDnnzB4UJb7eN7cglSN0RaedJXTb" +
       "ZRNfJouUOQtt1e72GKnBShR1OwyDc9MhzM\nirtqSGzcUatDu12NvRGvF77J" +
       "+7T8Tfx9jD80HGyIxNDea+VsyCbO6ZdRWLTKaMYuKoqNwy7gg+dw\nOE/YX4" +
       "KBaNxiRupsRy+oWDthcVLQGS+SeOfpjHRKJTp5k+k5ilWdZ3Y8UijcI5aDwX" +
       "bF9C1NI4mm\nwUTGPnh42QGTzXqDK6nOK9WwXgpCCORQA/aQJ2EHfK0SV2lw" +
       "w5qXKBURmiAnGRmc2TnAY0FFSxTD\n76lL770qu/PhhOfNTTYPY2cE/hzMy+" +
       "260sxZGX1U53EIPPzDloVnP/nOjlaRCYJYA17wyQyC56et\nJFtfuP79OZyN" +
       "oiGPXi8ULZGB4cwKO9K7kmb1kg3BWHBGhVGhQRcZ9x/85sB72/xtuNTGyLyw" +
       "ohLM\nTDmB51UYsw5wKC/BZnN5U9e4xQkee8+77eh9t3b/CWL9taSeGjQHRQ" +
       "UYyJxrI3UuBPVBBwqVVaID\neFJTTvAc9P0G4hdTnSxlomTxF4P/+4P/UMX4" +
       "vJPIO1nKO7H6M8uWfTT/Qp4WYH0rYUM6KwxQG57b\nfcb5r90OabuPNGLJ15" +
       "9HM0+RaRgTVKzReXryXL0eq8D+wPUbRZ3fH1RbhNQchJePQCbUN1SFl4Ym\n" +
       "gSSRwshJsNhgNpz9RphvJ/sndcHIvEpkT2rtMt3U2SVSXAs7RnOBhB1ucnbI" +
       "5HrJ6hJENiEAOfXu\nZjAH5oUmJDcGZASaJJyLyOcRXMcihiF7DIV7NJhWhv" +
       "YalhnQX2bHHnigKo898XamgUHhE+OLyDiC\nzQiMT0E4GhAYqdYse1KSdrFj" +
       "C8ZgU6GWVMCYZPQgIrzFphSO4AZJWMRC0VZ1RbLF/fUjbT/gnu6D\nprdhPO" +
       "lJVhezUNCKs/Jm7XPriD2Ra9kSqDys0oIOb3KhVHYpOCaOXpnX8c0H4s2UNG" +
       "+u00awyUsQ\n7CDF9CDyBQRbwQIMqhZoL5Spkr4DNkhWImIaqxzHctaoZgZi" +
       "RXYjPrJgBqeVeyzXdZvPUGlF5HYE\nd0BS5wKlsP2MzI4J876TSDHTow+kgG" +
       "1SANQKM2NMcJMlh9ZQy9uc6bzbqkwW1LG+QB1HzwST+jUi\n2xHAXjTInpL+" +
       "GAtV56h3X8lNEWNp5CN7Mc/LoW8jci+CnT5rzM6Svhxe+COseRxeaWUmuTOE" +
       "Wn5I\nDYTA+5Xk89dgCQi+VG5tCO4JFhXMDMEuSdhTdkpoUsGUREuq5qvHYx" +
       "5vRwn3S8IkmGJkgmkr72h0\nNSQt4ceh5r9kivvDSqzhCpC0C8IGPeqg6ZqZ" +
       "pGd8A7BVsGWhlqfFKqhdfRaJQRb6UCRZpLUxePs2\n0PWnSdwbXgfDkZv0iD" +
       "8icgDBE4J6maVKD1eeDlObVMZozmaRHg8GPRA8fYLwbyEYUxEWhzXWAvoc\n" +
       "tPB1CqoNPVRufDscB6Xh+O9dECt81DcbTbQlg7sQeRHBSwy/LHMqOo60LDIB" +
       "b3JSRg7eX5PrAEAs\nw38e3xp4e3QCl+hH5BCCVxD8ShJOgSnJhIfjy/SZhs" +
       "gRToD0iwL9F7/WcIWLJSsuiH/iOyePXQZ5\nJezxqTmIyNUIrkEwFBSfiIhC" +
       "C0FQ27UgEhRvZYs+BEFVdyzlHoLRclUegqCuWYqIXzuJd8XjXKIh\nCKqkXY" +
       "gEddWnKrgQ4W83kwiCeudiRG5EcFOUwHdFVCgIbilXuiCQJYYI0KLEiBDCtQ" +
       "cCWTOIGM7t\n/e4IQUkgcpwz1nJEHkCwG8FxSZd7EHkIwTeOm4ypUzIiexE8" +
       "8mmF74/K2BdkWkS+g+C7CB4P8ici\nQRT9XrmsiOCpcgkRwX9INvj/JRxLHk" +
       "TwfJD6EBGZC8HL5VIagl8GiQyRcllp6nSFyJEyhGlRAjfl\nV8tkoxX/A9kI" +
       "wZsnMs6JjFMq/ETGOZFx/lsJ/1sZh8e+teIIIXKqxo/CFngnxQShOCn2j3ld" +
       "J/T1\nInZTht/yufPq95rvUJ+9TpygtEfPw1eZ+dx5e47SxZ9r1spcyGhglr" +
       "3UoAUqQvgMEJgQZ30gdmnF\nE7B1/MAhOA2tSi9ffOaSpt8mSPUUdyravIcb" +
       "KMs7ZujMBXqr//Bli7mxTYnPp63QeUXVmP6zBD8l\nFQerJfelooN6osepjU" +
       "50olyFnXwCLbBF9fBbAL/pRPzx/0hsQ9Au7kQAUMSc47cAgkP7+BH1gKPn\n" +
       "dKYXvDtN98x5+M0n3tjQIdQnLn4tKLl7FR4jLn/5J5IgYV4lCbz3s2fN23fz" +
       "htdHvCM8ZZyRKt1k\n5S8w+FPHNrz1Tw/e+lcVNWrjBxDO5IbgK5qSF8dnSp" +
       "GR6oKlZ5ClsjVk6fzBRLH8E8WU246/M+G3\n0Nv2heW2vdTNeEZfwEity2/+" +
       "VV5Y6eU0fh1J6ONAVdd7iR/M6OI3bKpHVFceHUZu9ZVe2ovcxePz\nbJSrOg" +
       "1+cz9hVa+WVQCWjsqOokLw2F758hQrv1hEGVi/Qc0svzSmYPBTthdLPL9Urf" +
       "w8Ck+dfZq4\nsaJbSXlJEoilF1JQgCXmx2VN7Q3HemFG2Y3fXuTxWPSbk7hH" +
       "UayoW+RxE+d0u/fxihtieXPcXmKO\n24uwh3XetTU86z817lre/Uht/qFNi3" +
       "5it/1cmIl/h7EOj27zhhG+tRHCa22Hjupc23XiDofN5/oo\nIy2xu3OM1Pso" +
       "3+dHRM99/HYU9sTWY9woHioqfwcheQtPeisAAA==");
}
