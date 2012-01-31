package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client3 {
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
    
    public Client3() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1319724408000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0abZAcRbVn7/uD3EeOALl8kOQCiSR7gkABF4jJkZALm8uR" +
       "PQIcwmVutm9vLrMz\nw0zv3V4KMJACIlSiqSQgEQ1WYQU0KAmCVGmhVYJRvr" +
       "SClUSq4A8WQgmWlKVQCuh73TM9s7NzSxRj\nKeaq9t3red3vdff7nOne/w6p" +
       "ch2SdKnr6pY5mmQTNnU5tIZGqcbcZHp1n+q4NNNtqK7bD4RBbcNH\n+voDLb" +
       "evShBlgLSa1jJDV93+EcfKZ0f6R3S34JAzbcuYyBoW8ziW8Lhk3gfjL9y1ur" +
       "2CNA2QJt1M\nM5XpWrdlMlpgA6QxR3ND1HGXZTI0M0BaTEozaeroqqFvgo6W" +
       "CYJdPWuqLO9Qdx11LWMMO7a6eZs6\nXKb/MEUaNct0mZPXmOW4jDSnRtUxtT" +
       "PPdKMzpbusK0Wqh3VqZNybyK0kkSJVw4aahY7TUv4qOjnH\nzpX4HLrX6zBN" +
       "Z1jVqD+kcqNuZhiZHR0hV9xxJXSAoTU5ykYsKarSVOEBaRVTMlQz25lmjm5m" +
       "oWuV\nlQcpjEyflCl0qrVVbaOapYOMnB7t1ydI0KuObwsOYeTUaDfOCXQ2Pa" +
       "KzkLbWVjd+eHffe2eCxmHO\nGaoZOP9qGDQrMmgdHaYONTUqBr6fT+7quS4/" +
       "I0EIdD410ln0WTb/B1en3vzxbNGnPabPWm6Lg9oH\nF86YeXjZb+sqcBq1tu" +
       "XqaApFK+da7fMoXQUbrHua5IjEpE/8ybqfXbf52/T3CVLbQ6o1y8jnzB5S\n" +
       "R81Mt4fXAJ7STSqerh0edinrIZUGf1Rt8TZsx7BuUNyOKsBtlY1wvGATQmrg" +
       "p8DvHCL+6hAw0rZc\nZdpIGni63YZOTXZu0h1lJMmoy9xO19E67byTydPOIe" +
       "wHxtAZN6CAcqaMKwoscUbU3QywzVWWkaHO\noLbv9eduXnHll7YK5aHBeTNk" +
       "5DQhKOkLSgr2nyOKwvlORbMU27bMcdQJdJfCbYdn3n9I/TooATbD\n1TdRvl" +
       "ZlvBIhDDqvbDTpDnyxBzAVTGVQa9v85vQ9Lz/ybIJUxEaUlHy40nJyqoFG4b" +
       "tQqycuSgFb\n6ohadJzsP9y95vEjz7+6ILBtRjpKXK50JLrM3Oi+O5ZGMxCS" +
       "Avb3/XXVH3dWXfxEglSCH0IkYirY\nD7j1rKiMItfp8sMQrqUiRRqGSxZez2" +
       "CDxkMLRtiIoFnYBuiiLTJBHsHe31L92aM/aniWr9gPdk2h\nqJimTLhOS6D/" +
       "fodSeP7qV/t27n7nrusrwFltW+ickWo7P2ToWgGGnFbsbTi9DJrN2we7mrcv" +
       "dp/k\nKq7Tc7k8U4cMCtFZNQxrnGYGGQ9PLaFQyCMQ7ETjEEQyMM9BAxiJtd" +
       "rKGJhnTKxInt62696FDxzF\naGLzjZmGXsdnSgr8QYeCcEEJEdsz0YLbgmWD" +
       "/28UC2hcmL5h9Yatc/nKw+wWeY2CZBj4wvllfWEl\nZh2wGWZB+AlMZsM9x6" +
       "5ac+01nSIgnluWRS+4c4bzCcbv7jGXPn3qETOBVlLtjkqXT5GEyxhZkPJZ\n" +
       "epaHTYGlV6cFyU8Kiz5+AYFg/ea/3Hbwl7sbQfAAqdHdlbqpGqhtt1d4b0zC" +
       "iLDY9PTV33j/RfYa\nt83AXXD6MwqlgW69GvLki46MtVQ/tjeXIDUDpJknfd" +
       "Vk61Ujj5Y7AGnb7fYepsgpRfTiFCzyTZcM\nBzOirhoSG3XUytBuV2JvxGuF" +
       "b/I+TX8Xfx/hDw0HGyIxtHZbORuyiXPmFRQWrTKasQuKYuOwi/jg\nWRzOEf" +
       "aXYCAat5iRGtvRx1SsnbA4GdMZL5J456mMtEslOnmT6TmKVZ1ndjxSKNwjlo" +
       "LBdkT0LU0j\niabBRMY+dGTJQZPNeJ0rqcYr1bBeCkII5FAD9pAnYQd8rRxX" +
       "aXCDmpcoFRGaICcZGZzZecBjXllL\nFMN31KT3XZPd9VDC8+YGm4exswJ/Du" +
       "bldlxt5qyMPqzzOAQe/kHT/HOfeHt7s8gEQawBL/h4BsHz\nM5aTzc/f+N4s" +
       "zkbRkEe3F4oWycBwdpkd6V5Os3rJhmAsOKvMqNCgS4z7Dn2r792t/jZcbmNk" +
       "nl9W\nCWYmTuAFZcasARzKS7DZXN7UNW5xgse+C7Ycu/f2zj9DrL+e1FKD5q" +
       "CoAAOZdX1RnQtBvd+BQmWF\n6ACe1JATPPt9v4H4xVQnS5koWfzF4P/e4D9U" +
       "MT7vJPJOlvJOrPzMkiUfzr2YpwVY33LYkPYyA9S6\nZ/ecdeGrd0Da7iH1WP" +
       "L15tHMU2QKxgQVa3SenjxXr8UqsDdw/XpR5/cG1RYhVYfg5SOQCfUNVeGl\n" +
       "oUEgSaQwcgosNpgNZ78e5tvO/kVdMDKnHNmTWr1EN3V2mRTXxI7TXCBhh5uc" +
       "HTK5UbK6DJENCEBO\nrbsRzIF5oQnJ9QEZgSYJ5yPyBQQ3sCLDkD0Gwj3qTC" +
       "tDuw3LDOgvseMPPFCVR554O1PHoPCJ8EVk\nFMFGBMYnIBwLCIxUapY9IUm7" +
       "2fEFY7CpUEsqYEQyegAR3mKTCkdwkyQsYKFoq7oi2eL++pG2F3BP\n90HT2z" +
       "Ce9CSrS1koaEVZebP2ubVFnsi1bApUHlbpmA5vcqFUdjk4Jo5entfxzQfiza" +
       "Q0b65ThrDJ\nSxDsIMV0IfJFBJvBAgyqjtFuKFMlfTtskKxExDRWOI7lrFLN" +
       "DMSK7Hp8ZMEMzoh7LNe1xWeoNCNy\nB4I7IalzgVLYAUZmRoR530mkmKnFD6" +
       "SArVIA1ArTI0xwkyWH5lDL25ypvNuKTBbUsXaMOo6eCSb1\nG0S2IYC9qJM9" +
       "Jf1RFqrOUe++khuKjKWej+zGPC+HvoXITgS7fNaYnSV9KbzwF7HmcXi5lZng" +
       "zhBq\n+SE1EALvV5LP34IlIPhy3NoQ7AgWFcwMwW5J2Bs7JTSpYEqiJVXztR" +
       "Mxj7eKCfdJwgSYYtEE01be\n0ehKSFrCj0PNf8sUD4SVWMUVIGkXhQ162EHT" +
       "NTNJz/j6YKtgy0ItT4sVULv6LBL9LPShSLJIayPw\n9m2g60+RuDe8BoYjN+" +
       "kRf0LkIILHBfUKS5UerjwVpjaojNGczYp6PBD0QPDUScJ/hGBMRlgY1lgT\n" +
       "6LPfwtcpqDb0ULnxnXAclIbjv3dBrPBR32w00ZYM7kbkBQQvMvyyzKnoONKy" +
       "yDi8yUkZOXh/Ta4B\nALEM/3l8q+Dt0QlcoheRwwheRvBrSTgNpiQTHo6P6T" +
       "MFkaOcAOkXBfovfs3hChdLVlwQ/8R3Xh67\n9PNK2ONTdQiRaxFch2AgKD4R" +
       "EYUWgqC2a0IkKN5iiz4EQVV3POUeguG4Kg9BUNcsRsSvncS74gku\n0RAEVd" +
       "JuRIK66hMVXIjwt5sJBEG9cykiNyO4pZjAd0VUKAhuiytdEMgSQwRoUWIUEc" +
       "K1BwJZM4gY\nzu39niKCkkDkBGespYjcj2APghOSLvci8iCCb54wGZOnZET2" +
       "IXj4kwo/UCxjf5BpEfkugu8heCzI\nn4gEUfT7cVkRwZNxCRHBf0k2+P8lHE" +
       "8eRPBckPoQEZkLwUtxKQ3Br4JEhkhcVpo8XSFyNIYwpZjA\nTfmVmGy07FOQ" +
       "jRC8cTLjnMw4pcJPZpyTGed/lfDpyjg89q0WRwhFp2r8KGyed1JMEIqTYv+Y" +
       "13VC\nXy8iN2X4LZ+7rn238U71mRvECUpr8Xn4CjOfu2DvMbrw841azIWMOm" +
       "bZiw06RkUInwYCE+KsD8Qu\nLnsCtoYfOASnoRXppQvPXtTwuwSpnORORYv3" +
       "cB1leccMnblAb/WfvmwxO7Ip0fm0jLVfVTGi/zzB\nT0nFwWrJfaniQV3Fx6" +
       "n1TvFEuQrb+QSaYItq4TcPflOJ+OP/kdiCoFXciQCgiDlHbwEEh/bRI+o+\n" +
       "R8/pTB/z7jTtmPXQG4+/vq5NqE9c/JpXcvcqPEZc/vJPJEHCnHISeO9nzpmz" +
       "/9Z1rw15R3jKKCMV\nusniLzD4U8c2vPVPDd76VxQ0auMHEM7kpuArmpIXx2" +
       "dKgZHKMUvPIEtlc8jS+YPxQvwTxZTbjr+z\n4Tff2/b5cdte6mY8o89jpNrl" +
       "N//KL6z0chq/jiT0cbCi493ED6d18Bs2lUOqK48Oi271lV7aK7qL\nx+dZL1" +
       "d1Bvxmf8yqXolVAJaOyvaCQvDYXvnKJCu/VEQZWL9BzSy/NKZg8FO2FUo8v1" +
       "St/DwKT519\nmrixoltJeUkSiKUXUlCAJebHZU3uDcd7YUbZg99e5PFY8Tcn" +
       "cY+iUFa3yOMWzukO7+MVN8R4c9xW\nYo7bCrCHNd61NTzrPz3qWt79SG3u4Q" +
       "0Lfmq3/EKYiX+HsQaPbvOGEb61EcKrbYcO61zbNeIOh83n\n+ggjTZG7c4zU" +
       "+ijf54dFz/38dhT2xNaj3CgeLCj/AE0JDzR6KwAA");
}
