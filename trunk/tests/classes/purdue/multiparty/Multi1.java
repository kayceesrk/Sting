package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi1 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1YXWwUVRS+u22BttBWfkUKCtJa/NkifxFa/roWAZdS3QIG" +
       "5Gc6c9lOmZ1ZZu+W\nNuIfxgQ1YoighJDwosFEfcDE6JtPpokmPvhg8MEHxQ" +
       "dJ9MEY44OYeL87s/fOLNO1Uhsh2T58e+6c\ne37uuWfOOdMPfiE1eZcszdN8" +
       "3nTswQQbydF8AkuPSm9PU9bHyYP66DedH9ms+WqcxFJkapZm+6mb\nZ2Rual" +
       "Ab0toLzLTak45lUZ1xTR3DLmkrq1WwPM3E+4sJzTX6gGkZjLSliuLtQqhdir" +
       "eHxGHp3hJL\nQKd/kLsCW56R01PTl/ZkzrwTJ4RLzMk51kjGcpgv4e3Z3PLJ" +
       "rtRPn93t7VkQsWen0HpQv76meeHX\nm3+srYLL03JO3sSpGbkzVRTyItLrcz" +
       "qGc7nhHA91K6KVAC+hopVfusvOOoZ52NT6LZoy8+x6Y8vD\nH/98qkmEpNri" +
       "TxhpCkQae3DyB/9ZnXo+v4u88OWBPxYJpTH9KHmOxIRTs5WWlGkfoQbUT1+W" +
       "3r/9\n0MklVTwYuWPVuCC+9b4yl5rsohnzhjtFKFvLSAWE1llvj77b++tJCM" +
       "G7eG6Ym2wpm0e2EWVwdRmZ\nHZzWMjwu2WzBNnWNyUS8tPqlK2+daP89Tqr2" +
       "kWnUollqM57ji/Z5obc0O9OeZpp+pM/VdNrtbehI\nkfqspxNqGJmjksBLWJ" +
       "GnKTKFaW6G8pucGVLnmnamQ54XvzPFucu9P09SnZpD9MajQ3yWr2Y+T7mC\n" +
       "W/Q+AXOJG72Pb7m/s/OvJWshneNqOrjpBWUEtNrPz7eu+e7lOKneRuos06Y9" +
       "BdSCFGkwqG5pOE7S\n0vLChbn87ThsWrRHy1J/XZelbMAx5BMYrRnl6a1s9g" +
       "24VOM1oN4jEuAwMoPHTnkj1C/i/i5gN3nb\njCwux/atTuk0bZNtkOYa2fgv" +
       "hpHZJU+EUqhqlQo3gGgDLGNkWv4ITzvG3+Eiu06xAQ9IxioQSwEt\nLJSAcs" +
       "fe4I5a2zFo0nJsxf+q/FFCNVocJfTEj08tc2mpXhArACsBqyaDcUUxGKnWnd" +
       "yIZJ1l42to\nPOUCK3kzy6WiCyA6AevHNA5YJxltLNCLtLzXORD4Yh/q4TQs" +
       "3xFc+pGsZsHLW88CVbNUle91Udvs\nkifyLEmpbhMLlO4hk3clrqbXdZijO9" +
       "aj/L3lL5WdtzTmuIwsHIvlezqP+U9oeKO0thzEVsA2niEW\n1YZoUrMU/xSP" +
       "k+yunjfdruu4WzXb4BUlsxuP4Mn8qMfyeKmiwlgTiB7ATkZqhEFp7DI/UImx" +
       "JO/V\ndJhJM7PCD6SBJ6SBOCN3lShBrKWGpsDKj9Issa3byPBb2TlEXdc0lF" +
       "PfgtgN2MMjJHdK/odcXtrD\n9Rfvuj6UM3VCMomRSYpeA/E0YH9RNfq55G/k" +
       "Y0pItajWXY4xIt6JwKpYeJURl6rK9Kc6AuCpqLMB\n9qpDKc8AByTjYqRLSC" +
       "nlkreSV2NMhh/XwoxDkjHCUzHkYNopuDrdwlub9zoHlv+Ji5eDl1gjLkDy\n" +
       "Hgkm9GEXqWsbCT/5enmoeMgCK/8Wq9yCvLt4HwuMt1JFWh+gRsGiPJ8bJO2L" +
       "T+Xi0CbfiN9APAM4\n7nEfczT5hsc+DXLrNcZoNsdCOy6oHYAXb3GGNRZjWf" +
       "CkjTwOfQ6fJnK8i5uBNv5+sH7IgGOjKeI9\no0gWw617a6ngVRCvA3jxnO5z" +
       "kXDyRsgxPllKG1nNtBM7OPAagB9fb02eD6EqlUTJfBNwBnBWMuZx\nl2S/gH" +
       "zEngYQ5wSDdy8Y5KNjc8lXU6/Fh7WtjmVQlw/YV7843v34Kye9zxpbjYCh8c" +
       "8bifnotaNg\nMXNlAXuaxWTqW64ZBXEPYDFgiRoGQXgjD0BNWY0g1BgVOX4B" +
       "1Hw1nsELkIiatwBqkHgIRHFY8T5w\n/69hCaDmlbMg1IQzodEHxGZAF0BNHk" +
       "JBN2BLmLEJhDckALZHTQ8A2eXJKRBelw8xgu0fINu2V0bT\ngL4QIxYHMclN" +
       "YyOIfoD41p6UjnURhAkYnDQbY3dFEDnA0Ykavxy2UVDNDsQxgPi2lWN+XFyo" +
       "KsjP\nRjUmwPNRPQlwizSWyWlFgNdU9wHhNQ/AG1FdBXBa9RIQUY1h7I4B4l" +
       "wEoyHMOA+4UKzuK2R1l6Xk\nZqo7G+d/icSXl1qO9Tn+PeAHQEk/iGJMqB8A" +
       "3qvU/ErN/xfGKzW/UvNv55qvJvq1E6n5IFQ9vm0r\neKVQVwp1pVBXCjW59Q" +
       "r1CnGrDX8DvVBG/yYgAAA="));
    
    public void run() throws Exception {
        final SJService c = SJService.create(invitation, "localhost", 1000);
        c.addParticipant("Multi2", "localhost", 7102);
        c.addParticipant("Multi3", "localhost", 7103);
        c.addParticipant("Multi4", "localhost", 7104);
        SJSocketGroup ps = null;
        try {
            ps = c.request();
            System.out.println("Multi1: connected to all participants");
            SJRuntime.pass("Hello, Multi2 from Multi1", "Multi2", ps);
            SJRuntime.pass("Hello, Multi3 from Multi1", "Multi3", ps);
            String str2 = (String) SJRuntime.receive("Multi2", ps);
            System.out.println("Multi1 received from Multi2: " + str2);
            String str3 = (String) SJRuntime.receive("Multi3", ps);
            System.out.println("Multi1 received from Multi3: " + str3);
        }
        finally {
            SJRuntime.close(ps);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Multi1 a = new Multi1();
        a.run();
    }
    
    public Multi1() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1320939774000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0bbZAcRbVn7/uD3EeOALl85wIJJHsmAgVcICZHQi5sLsfd" +
       "EeAiXOZm++4mmZ0Z\nZnr3NogYQCFAmRJJACkgP8SiBCxNkK/CQi2BqIAoSB" +
       "Ipwx8oBQVLpARKAX2ve6bnY/eWQIwC3lbt\nu9f9ul93v35fM/vuvjdIheuQ" +
       "pEtdV7fMTUm2xaYuh9bQJqoxN9m3pkd1XJruNFTX7QfCoLbxA339\n7qarVi" +
       "eIMkCaTWu5oatu/6hjZUdG+0d1N++QWbZlbBkxLOZxLOBxxtz3xp6+dk1rGW" +
       "kYIA262cdU\npmudlslong2Q+gzNDFHHXZ5O0/QAaTIpTfdRR1cN/TIYaJmw" +
       "sKuPmCrLOtTtpa5l5HBgs5u1qcPX\n9DtTpF6zTJc5WY1ZjstIY2qTmlPbs0" +
       "w32lO6yzpSpHJYp0bavZRcQRIpUjFsqCMwcErKP0U759i+\nCvtheK0O23SG" +
       "VY36U8o362aakZnxGfLEbefCAJhalaFs1JJLlZsqdJBmsSVDNUfa+5ijmyMw" +
       "tMLK\nwiqMTB2XKQyqtlVtszpCBxk5Nj6uR5BgVA0XC05h5Oj4MM4J7mxq7M" +
       "5Ct7Wusv7963vemQU3DntO\nU83A/VfCpBmxSb10mDrU1KiY+G42uaProuy0" +
       "BCEw+OjYYDFm+byHzk+9+uOZYkxrkTHruC4Oau+d\nOm36c8tfqSnDbVTblq" +
       "ujKkROzm+1x6N05G3Q7imSIxKTPvEnvU9etPUe+ucEqe4ilZplZDNmF6mh\n" +
       "ZrrTw6sAT+kmFb3rhoddyrpIucG7Ki3eBnEM6wZFcVQAbqtslON5mxBSBV8F" +
       "vicT8alFwEjN2qzB\n9MVJdxMj8xh1mdvuOlq7nXXSWdqeQaKtghK3y3F55D" +
       "lpTFHgONPipmWAHq62jDR1BrW7X/7l5SvP\nvW6buChULm83ICTBPxnwTwr+" +
       "RFE448mog0JGyx1H3YK2kb/yuenf2qveARKHk7v6ZZQfTBkrRwiT\nlpR0HZ" +
       "2B4XUBpoJeDGotW1+detvz330iQcqKuo+U7FxlORnVQA3w7aXZWy5OAcVpi6" +
       "tvsbX/cv3a\n+/c9dXB+oMiMtBXYV+FMtI85ccE7lkbT4H8C9rf8Y/Vfb6o4" +
       "/YEEKQejA7fDVFAWsOEZ8TUidtLh\n+xw8S1mK1A0XHLyWgYDGQgdGWI+gUS" +
       "gH3EVLbIPcXb17deXn9v+o7gl+Yt+zNYRcYB9lwk6agvvv\ndyiF/oO39ty0" +
       "841rN5SBZdq2uHNGKu3skKFreZhyTNS0cHtpVJvX93Q0bl/kPsivuEbPZLJM" +
       "HTIo\nuGLVMKwxmh5k3Bc1hfwedzcgifohcFvgAQcNYCTOais5UM8ijiF5bM" +
       "uOmxfcvh9dh80FMwVNjO+U\n5HlHm4JwfgER29NRg1uCY4OxbxYHqF/Qd/Ga" +
       "jdvm8JOH2S30GnnJMLCFk0vawioMMaAzzAJfE6jM\nxhsOnLf2wgvahfdbXJ" +
       "JFN9hzmvMJ5u/sMpc9dvQ+M4FaUulukjafIgmXMTI/5bP0NA+bAutb0ydI\n" +
       "fgRY+OEHCBbWL3/7yj3P7qyHhQdIle6u0k3VwNt2u4X1FokOMRaXPXb+ne8+" +
       "w17iuhmYC25/Wr7Q\n061XQ5Z82r5cU+UPdmUSpGqANPIIr5psvWpkUXMHIE" +
       "a7nV5nihwVoUfjrQguHdIdTIubamjZuKGW\nh6RdjqMRrxa2ycc0/Et8PsAv" +
       "Kg42RBRo7rQyNoQOZ9Y5FA6tMpq284pi47TT+OQZHM4W+pdgsDSK\nmJEq29" +
       "FzKiZKmInkdMYzIj54MiOt8hKdrMn0DMUUzlM77ikUbhHLQGHbYvctVSOJqs" +
       "FEeN67b+ke\nk017mV9SlZeXYXIUuBAImAbIkEdcB2ytFFepcIOaFxUV4Zq0" +
       "Ud1I486WAI+5JTVRTL+xqu/uC0Z2\n3JXwrLnO5m7s+MCeg325beebGSutD+" +
       "vcD4GFv9cwb/EDr29vFJEg8DVgBR/OIOg/bgXZ+tQl78zg\nbBQNeXR6rmih" +
       "dAwnlJBI5wo6ohcIBH3B8SVmhSadYdyy9zs9b27zxXC2jZ55XslLMNPFFjyl" +
       "xJy1\ngEMuCTqbyZq6xjVO8Lj7lKsP3HxV+9/B128g1dSgGWoyUJAZGyJJLT" +
       "j1fgcylZViAFhSXUbw7Pft\nBvwXU50RykTO4h8G/3bzQ5XSrF6qUT1HC8+F" +
       "09d5bPpARbKOv7Ukbi1ZuLXEqhOXLn1/zuk8qgCb\nTli6tcQEteaJ244/9e" +
       "DXIOp3kVpMD7uzaCUpMgldior5PI9unqeoxoyxO/ActeKZoDvI1gip2AsP\n" +
       "KsGakB5RFR4w6gSSRAojR4Gsgt1w9hfCflvZx7xKRmaXInurVi7VTZ2dJZdr" +
       "YId+MYy0xHo4U2S1\nUTI8C5EhBDC+2t0MOsU8/4bk2oCMIDj3yYhcggCehs" +
       "LaJUcMhEfUmFaadhqWGdB/VfooEe/FjxLp\n8eRTwyB7ivFFhHvfDALzSBAO" +
       "BARGyjXL3iJJO9mhuXpQuVBL3swmyeh2RHIIxsZdHAGThPks5MtV\nV4RyFL" +
       "zvx7sBx5Wbwk1PkjykSlZnspBLjLPydu1za4n1yLN8OdCF8F3ndHgoDAXKs8" +
       "FucfaKrI4P\nVuDNxqV5e500hE2e4OAAuUwHIlchuBpUw6BqjnZCEizp20FA" +
       "Ms8R21jpOJazWjXT4EpG1mOXBTs4\nrli3PNc1PkOlEZFtCK6DlIEvKBfbzc" +
       "j02GLeKxe5zORoh1zgBrkAZCJTY0xQyJJDY6jlCWcyH7Yy\nPQLXsS5HHUdP" +
       "B5v6HSLfQHAjSEiOlPTvsVDuj/fuX3JdRFlq+cxOzCLk1NcQuRnBLT5rjP2S" +
       "vgwf\ni8OsuZteYaW3cGMItXyPGywCT2+Szz+DIyD4ZrGzIdgRHCrYGYJbJW" +
       "FX0S2hSgVbEi15NXceiX28\nFiXcJglbQBUjG+yzso5GV0FME3Ycav5Htrg7" +
       "fIkV/AIk7bSwQg87qLpmOukpXw+ICkQWanm3WAaZ\nsc8i0c9C75wkiz5tFJ" +
       "7tDTT9SRL3plfBdOQmLeItRB5A8KCgnmOp0sKVh8PUOpUxmrFZZMTtwQgE\n" +
       "j04Q/isEYzzCgvCNNcB99lv4sAZpiB7KQ+4N+0GpOP5THfgKH/XVRhNtyeB6" +
       "RJ5F8GuGL6k5FQ1H\nahYZg+dEuUYGno6TawHgK0TEBd8KeDZ1ApPoRuQFBP" +
       "sQ7JeEY2BLMuDh/CJjJiHyIidA+MUF/cfK\nxnACjBktJIL8/eHnszhiPc+T" +
       "PTYVexHhydYGBF8MUlNERAKGIMj5GhAJkrqiySCCQHqHkgYiGC2W\n/SEI0p" +
       "pFiPipk3gQ/V+lbgiCu9yJSJBvHVYihsiXEFyOIMiDzkTkKwi2RglcXCJzQf" +
       "DVYikNApl6\nkO2IiNQjQgjnJAhkLiF8+9cRbI8QlAQiRziSLUOES/IOBEck" +
       "jO5C5NsI7jpia4wfqhG5B8G9h7v4\n7uga3w8iMCKcugfB/UFcRSTwrg8Vi5" +
       "YIHikWKBF8QqLE/y/hUOIjgmeCkIiIiGgIflMs1CF4Pghw\niBSLVuOHMURe" +
       "LEKYFCX8HsFBP0otkVFqxeFEKXaIL9b482zQHOclh3IigpOwKxrXihIOK64h" +
       "eG0i\ndk3Ero+w+ETsmohdn1bCZyl2BU9Yyw8ndl0aiSuf2kg0EXAmAs5EwJ" +
       "kIOJ8wwmcp4CzhP5qvEb+Z\nR6pQeOnIXK+yiiAUlVV+WZTrhN7Hx8pIeQns" +
       "tRe+WX+N+vjFouKgOVo/ttLMZk7ZdYAu+EK9VqSC\nsYZZ9iKD5qh4c47FVw" +
       "lRGwPLLipZMbKW/8IeVA+V9S1bcMLCuj8mSPk4NYhNXmcvZVnHDNUowGj1\n" +
       "IxcnzowJJb6fplzreWWj+s8TvKpIFCIVFBNHJ3VEy49qnehG+RW28g00gIiq" +
       "4dsM36lEfPhfJDYh\naBY1hAAU4azGq5orSvQr4LA9mXl1pPxN8cq8Rm18aY" +
       "78lUe9X14K68J6HD2jM1kTcOOMu/5w/8u9\nLUIHRGn13ILq5vAcUV7tlwHB" +
       "CrNLrcBHP37S7Puu6H1pyKsRUX7ISHnO0tN4OOXJqFbHG8rDUrCN\nmC7At9" +
       "0TbHsxwRYaEg/Zc8HgXF74XlqwhbXZvEBXCGtPWdubiUentPGa0/Ih1ZXFNJ" +
       "Gi9sKa9Ugp\nOt9nrTwVqsesDznVwaIKgLmh8kJeIVjIpuwf5+RnCj8C5zeo" +
       "OcJ4Bqk8Lqp0lL2gJ7BbRH+bLzD1\nQi3jtRZYluXTREmnbiXlvwwAsbBiEx" +
       "d4RGyXr/Ux1T9UUaq8gr8fyNKP6M8motAwX/KqkcdPOaen\nvd9fuDYW6KQS" +
       "Ek6kJ+/78MVYC3ds3Aq8fxbQ5jy3cf7P7KZfCKXxC/qrsDYpaxjhqsYQXmk7" +
       "dFjn\nd18lahxtvtW3GGkqKC5npDZocFH/TYx+m1cQ42hsvcPV5E955d98E5" +
       "ZHizIAAA==");
}
