package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi4 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAM1YW2wUVRg+u22BttBWLiKWS7i0FpVdUUGQcl3KzaVUt4hK" +
       "AIeZw3bo7Mw6e7a0\nhhij0RRiUINIYkh40fCgPmBi9M0n00QTH3ww+OATPk" +
       "iiD8YYH8TE/5vLOTPrdGmEJvDw9T/nP+e/\nz/n/5ZPfWFPFZasqvFIxHftk" +
       "RoyVeSWDpU8V9hW4GCTymD7xQ+9ntlh8Pc1SeTazxEvHuVsR7N78\nSW1Ey1" +
       "aFaWVzjmVxXZCkTaMu66kr1WP5kpn/L+VJbtKHTMsQrCcfXs96l7LyejZ2HZ" +
       "pW1mgCOsdP\nkinQ5St5d2bhyqHiex+mGaMbC8qONVa0HBHc8M9s7/riYP6X" +
       "r5b5ZzoTzhzwpB7Tb65fvOT77T83\nN8DkWWWnYsJrwe7Lh5f8iAwEnE2j5f" +
       "JomULdjWhlwMuoaFVWHbRLjmGeMLXjFs+bFXGzvWvt57+e\n6/BC0mjRjmAd" +
       "kUjjDDx/+Nbi1P6iHezVb4/+tdQTmtJfYq+wlGfUfCUlb9rD3ID42asLR/a9" +
       "OL6i\ngYJRPtWIBNHRB+oldQcvmkk5nSE0t8jJhbm+C5ZmF7MF4Zp2EU5015" +
       "EZEfmkdXHio4Hfx5Ed2J4u\nj5JBXXWrzDZqzUFm19W5s59orUhRK5Wqtqlr" +
       "QpbplXWvX3v/teyfadZwmM3iFi9xW9AXsPRwzCtN\nHx50NZ33+Qc25VlryZ" +
       "cJMYItUCXil7NXxTJI8KxD+oe/86hyqm6oJgM1mf+qSe96sLf3nxUb4WGZ\n" +
       "XN1Isemsc0Fr/vqD7vU/vZFmjXtZi2XavL+KTzrP2gyuWxqSk7O0imdCBxX5" +
       "CdPi/VqJB+uWEhdD\njiF3oLRpgqpU6RwccrlGn3KrT2TAEWwOOams8cR3kr" +
       "2d4n+mRbDl9diB1hm9pm2KLVJdu5hi5Qh2\nT3TpiYOQlVLUFhBdgG7BZlWG" +
       "qTIEfYQhu0WxAT2S8TiI5YAVIlYj8sQL0RPNtmPwnOXYiv+dmPoj\nK9j8mp" +
       "0gMs3C5bVyQWQBjwDW3ppxTTEEa9Sd8phkXRBT6y9UOpGVjPMaKegSiCcAGy" +
       "ZVDlgnGT0i\n0hq0iv+QI4xhW+gnOkixWgZxaRTRVGwWkWeqVlRgdShtfs2O" +
       "9EWV3zYReUlHTGoSJGbAdYSjO9ZO\n+v7o47ArliYcV7Alk7ECSxeKYIfHD0" +
       "ptXlhygJ2Ub4trIzynWYp/juIkm51vTZ/rOu4ezTboZSg+\niy1YsihpW7q3" +
       "OxSY6gCxF7BPsCZPoVR2lRyqUZaj1slHhVQzL74hFeSlgrRg99cIQaylhI7I" +
       "KojS\nPO9Yn1GkrBwY4a5rGsqoH0E8DXiGIiRPSv6ndF/qQ/rDXLfGaqbFu5" +
       "nDBCOv3gBxCPBcKBrtVfK3\n0tQQE+29ujscY8z7JiKr8AFVSlyu3pm/lQuA" +
       "QpJvgIPKKWUZ4HnJuJxoEkpKmeSvZGqOTYcdN+KM\nw5IxRqUYM7DgVF2d76" +
       "IW5X/OkeUdMfFqNIlNXgIkb0O0oE+4KF3byATFN0ChopBFVkEWG9yqzF16\n" +
       "UESmTSmioA9xo2pxquc2SQfXZ9J1SJNfxB8gRgCnfO5uR5NfeOrLKLdVE4KX" +
       "yiJ24pI6ATh9lzOs\nyRiro562UxwGHZoKytSTzUhT/jj6fsiA46DpxXtOSI" +
       "bh1v21FHAWxDjgjGCzAy4KTmYE+udKHSXN\ntDP7CegNwJ9AblOFpj5VSv0g" +
       "zgHeBrwjGQvJJNkvcD/hTBuI8x6DuhcU0gi4uOZHzIBFQ9cexzK4\nSxPt9W" +
       "9O9z11Ztz/lWGrUS42xvmDOo1Q+6uWMB+t4swib8IMNDdNgFgCWApYpoY6EP" +
       "4AA1AzUzsI\nNRQlDlMANS1NZYwCPJQ0PQHUILEGRDis+L8378zoA1DTxwUQ" +
       "al65rUEGRC9gM0DNEd5yG2B7nOHt\n+S0f0Jc0CwBkz/bLzu/ZMUa0mQNkE/" +
       "YfRa9kD8QYqTSIaW4BW0EcARwFTEv/uQzCAPBp0zF5jwMx\nDLBuV/nVuI6y" +
       "al0gXIC3JR+i9CAI9byOJrUZwMtJHQZwl7SJ6WksgDdVLwHhtwLA2aQeAXhL" +
       "dQYQ\nSc/85O8/iPMJjLY4w3tzLoZv9WMhsdb7D4W2fwEEURaj7xMAAA=="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 7104);
            ss.addParticipant("Multi2", "localhost", 7102);
            ss.addParticipant("Multi3", "localhost", 7103);
            SJSocketGroup ps = null;
            try {
                ps = ss.accept("Multi1");
                System.out.println("Multi4: connected to all participants");
                SJRuntime.pass("Hello, Multi3 from Multi4", "Multi3", ps);
                SJRuntime.pass("Hello, Multi2 from Multi4", "Multi2", ps);
            }
            finally {
                SJRuntime.close(ps);
            }
        }
        finally {
            { if (ss != null) ss.close(); }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Multi4 a = new Multi4();
        a.run();
    }
    
    public Multi4() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1320939571000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0aa5AcRbln7/0g98gRIJeEPC6QYLInMUHgEmJy3JELm8uR" +
       "uwS4iJe5mb69SWZn\nhpnevU1E5KEQQGMhhFch+QNSIlokCFqlhVYJBgFRYx" +
       "HiD/iDpVCCJWUplAL6fd0zPY/d20QgllV6\nVfPd1/11f9399ffq7n30LVLj" +
       "uSTtUc8zbGtnmu12qMehPb6TasxLD28cUl2P6r2m6nkjQBjTdnxg\nbDvYds" +
       "OGFFFGSbtlrzMN1RuZdO18dnJk0vCKLpnv2OburGkzn2MJjwsXvTf1ws0bO6" +
       "tIyyhpMaxh\npjJD67UtRotslDTnaG6cut46Xaf6KGmzKNWHqWuoprEHGtoW" +
       "DOwZWUtleZd6W6hnmwVs2O7lHery\nMYPKDGnWbMtjbl5jtusx0prZqRbU7j" +
       "wzzO6M4bGeDKmdMKipe1eTa0kqQ2omTDULDWdlglV0c47d\n/VgPzRsNmKY7" +
       "oWo06FK9y7B0Rs5M9pAr7roUGkDXuhxlk7YcqtpSoYK0iymZqpXtHmauYWWh" +
       "aY2d\nh1EYmT0tU2hU76jaLjVLxxg5PdluSJCgVQMXC3Zh5NRkM84J9mx2Ys" +
       "8iu7W5tvn9W4femQ87DnPW\nqWbi/Guh07xEpy10grrU0qjo+G4+fefAlfk5" +
       "KUKg8amJxqLNusXf35p5/cdnijadZdps5ro4pr13\n3py5R9b9rqEKp1Hv2J" +
       "6BqhBbOd/VIZ/SU3RAu2dJjkhMB8SfbPnZldc9Qv+YIvUDpFazzXzOGiAN\n" +
       "1NJ7fbwO8IxhUVG7eWLCo2yAVJu8qtbmZRDHhGFSFEcN4I7KJjledAghdfAp" +
       "8K0k4q8RASMNm/Im\nM1amvZ2MLGbUY16352rdTt7V87Q7h0RHBSXulu2KyH" +
       "PGlKLAcuYkTcsEPdxgmzp1x7SHX3vumr5L\nb9krNgqVy58NCEnwT4f804I/" +
       "URTOeCbqoJDROtdVd6NtFK8/Mvfew+o3QOKwcs/YQ/nClKlqhNBp\nRUXX0R" +
       "sa3gBgKujFmNZx3euz7/vNt55Jkaqy7iMjK/ttN6eaqAGBvbT7wyUpoDhdSf" +
       "UtN/afbt30\n+NHnX1kSKjIjXSX2VdoT7WNhUvCurVEd/E/I/u6/b/jzHTUX" +
       "PJEi1WB04HaYCsoCNjwvOUbMTnoC\nn4NrqcqQpomShTcyENBUZMEImxG0Cu" +
       "WAvehITJC7q3dvrP3kyz9qeoavOPBsLREXOEyZsJO2cP9H\nXEqh/pV7hu7Y" +
       "/9bN26vAMh1H7DkjtU5+3DS0InQ5LW5aOD0d1ebNQz2t+5Z7T/ItbjByuTxT" +
       "x00K\nrlg1TXuK6mOM+6K2iN/j7gYk0TwObgs84JgJjMRaHaUA6lnGMaRP77" +
       "jzrqX3v4yuw+GCmYUmxmdK\niryiS0G4pISI5bmowR3hssHYd4kFNC8dvmrj" +
       "jr0L+cqj7Jb5haJkGNrCyoq20I8hBnSG2eBrQpXZ\ncduxyzZdcXm38H7nVm" +
       "QxCPascz5h//0D1tqnTj1qpVBLar2d0uYzJOUxRpZkApa+5mFRYMMbhwUp\n" +
       "iADLjr+AcGDjmr9df+iX+5th4FFSZ3j9hqWauNveoLDeMtEhwWLPU1sfePcX" +
       "7FWum6G54PTnFEs9\n3TY1YsnnHy201T52IJcidaOklUd41WLbVDOPmjsKMd" +
       "rr9Ssz5JQYPR5vRXDpke5gTtJUI8MmDbU6\nIu1qbI14vbBN3qbln+LvA/xQ" +
       "cbAgokB7r51zIHS48y+hsGiVUd0pKoqD3c7nnedxuEDoX4rB0Chi\nRuoc1y" +
       "iomChhJlIwGM+IeOOZjHTKTXTzFjNyFFM4X+24p1C4RawFhe1K7LdUjTSqBh" +
       "Ph+fDR1Ycs\nNuc1vkl1fl6GyVHoQiBgmiBDHnFdsLVKXKXCjWl+VFSEa9Im" +
       "DVPHma0AHosqaqLofnvd8MOXZ+98\nMOVbc5PD3dhZoT2H8/K6tlo5WzcmDO" +
       "6HwMLfa1l87hNv7msVkSD0NWAFx2cQ1p+xnlz3/OfemcfZ\nKBry6PVd0TLp" +
       "GM6uJJH1NGuUE0gtU90sZSJ+w7Iq8IiwuNC8+/BDQ2/vDYRysYN+enHFLbH0" +
       "5PDo\nilZV6LMJcMgsQYNzecvQuP4JHg+vuvHYXTd0/xU8/3ZST02aoxYDdZ" +
       "m3PZbigosfcSFv6RMNwK6a\ncoLnSGBFCQkEi8H/g7DTeTfgmUae6VKeqf5z" +
       "Vq9+f+EFPDjAutaBIDordFAbnrnvrPNe+TIE7wHS\niFneYB6VPUNmoGdQMS" +
       "3nQco3+HpM/AZDB9AoUvvBMOkipOYwnDfCMSHLoSqcE5oEkkYKI6fAIsPZ\n" +
       "cPZbYb6d7EPuASMLKpH9UWtXG5bBLpLDtbATVBMI29EiZ4dMrpKsLkJkDMEO" +
       "Ruq9XaAGzHdQSG4M\nyQjGJWElItsRfJbFFEK2GI22aLBsnfaathXSX2Qn7n" +
       "4Y6UjU+JJpYJD+JPgiYiDYiWDX8QnHQgIj\n1Zrt7Jak/ezEPC+oTqQk5ZyV" +
       "jO5H5GoE7rSDI7AlYQmLuFbVE5EVxRi41UHA/S0Oi75ceISTrNaw\niE9Ksv" +
       "JnHXDrSNTItRTDnY3uXMGAM1okbl0M9oe91+cNPOeAO5mW5s91xjgWeb6BDe" +
       "QwPYh8AcG1\nsNEmVQu0F3JSSd8HApJph5hGn+va7gbV0sElZLdhlQ0zOKNc" +
       "tVzX9QFDpRWRGxF8CSI4H1AOdpCR\nuYnB/BsQOczMeIUc4CY5ACQGsxNMUM" +
       "iSQ2uk5AtnJm/Wp2dhOzYXqOsaejip3yJyG4KvgIRkS0n/\nDouk4rjvwSY3" +
       "xZSlkffsxaAuu76ByO0Ivh6wxlAs6WvxlBplzd3telvfzY0hUgo8ZzgIHKYk" +
       "n3+E\nS0Dw1XJrQ/C1cFHhzBDcIQkHyk4JVSqckijJrbn3ZMzjjThhvyTsBl" +
       "WMTXDYzrsa7YfYJOw4UvxY\npngwuok1fAMk7fyoQk+4qLqWnvaVbwhEBSKL" +
       "lPxdrIJENWCRGmGRKyDJYlibhKO2iaY/Q+J+9zro\njtykRfwFkccQHBTUS2" +
       "xVWrjygyi1SWWM5hwWa3F/2ALBk/8n/EcI5nSEpdEda4H9HLHx7ARJhRHJ\n" +
       "Kr4d9YNScYJDFviKAA3URhNlyeBWRJ5D8DzDO2NORcORmkWm4Ngmx8jBYTW9" +
       "CQDe6CEu+NbAUdEN\nTWIQkV8h+DWCI5JwGkxJBjzsX6bNDERe4gQIvzhgcM" +
       "prjSaymJlCQsev81bkscUwz3d9NjWHEbkc\nwRUIrgxTTEREOoUgzOBaEAlT" +
       "tLKpHYIwdzuRpA5B2VwOQZjWLEckSJ3EufDjScQQhLnQfkTC7Okj\npVWIFB" +
       "BMIQizmjWI7EHw+TiBL17kIQi+WC5BQSATCbIPEZFIxAjRDAOBzAyEGe1FcE" +
       "uMoKQQOclx\naS0idyO4B8FJCYoHEHkAwYGTNsb0gReRhxB886MOfjA+xiNh" +
       "PEXkUQR8ft8NoyQioa88VC72Ifhe\nubCH4L/E5//vEk4k2iF4NgxwiIj4hO" +
       "CFcoELwYthuEKkXOyZPigh8lIZwow44WUEx4KY86kAOZdf\n02wUbi52fcnv" +
       "HBf5V/IEobiSD+7TPTeSOSbeH/nb6c1XvN18k/r0VeJyqj3+8NBn5XOrDhyj" +
       "Sz/T\nrJV5+mpgtrPcpAUqcjy8tU+JS1UYdnnFq8ZN/E4nvHauGl679OxlTX" +
       "9IkeppHq/a/MotlOVdK3Kd\nBa3Vf/tV68yEUJLzaSt0XlY1aTyb4tfR4ga7" +
       "5BU63qknfm/d6MYnyrewk0+gBTMB+M6Ab7Z/O8j/\nI7ENQbt4fAKgzBd7O8" +
       "1zS1li8HSCZchpZoY5TV9Row6md8hfWeafEUofFIZcI2cwo+A/N98+78Hf\n" +
       "P/7alg6hA+JNflHJs3i0j3iXD+6PYYQFlUbgrZ/+xIJHr93y6rh/xaoshtSs" +
       "YBs6Lk75dFyrkwVl\nqRTsafB1+x8J/icFW2pIPHYvAoPz+C8mKgu29FGfv+" +
       "wKYR2q6no79cNZXfyxsnpc9eS9a+zXEKU/\ndoj9hoHPs1GuagF8Zx9nVcfK" +
       "KgDmiUp/USH4AqJsmGbla4QfgfWbFJLeSS7UVeJeWIHjZxXMFtG+\nYompl2" +
       "oZv+PD+/yAJt4CDTstf2sCxNKnPhzgHDFdPtaHVP/IU6SyFTNdeeUYT/DFC1" +
       "Wx4lYjjxWc\n0xr/pMC1sUQnlYhwYjXFwIevxEeU05NW4P/KRFt4ZMeSnzpt" +
       "PxdKE/wSpA5vw/OmGX0Oi+C1jksn\nDL73deJxzOFTBetsK/lVAiONYYGLWh" +
       "Ots/zpGVtjaZKryfai8i8YFShLxCQAAA==");
}
