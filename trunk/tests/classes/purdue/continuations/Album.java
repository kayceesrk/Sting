package purdue.continuations;

import java.io.Serializable;

class Album implements Serializable {
    String name;
    int count;
    
    public Album(String name, int count) {
        super();
        this.name = name;
        this.count = count;
    }
    
    public String Name() { return name; }
    
    public int Count() { return count; }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328822223000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAALVXXWxURRSe3W23fytl2/KT/vBTINIAu2oiUZqIm0pDYaG1" +
       "LURKyDK9O7udMnvv\n9c7cdiGEgCaCPJgYwL+ovJgYDQ8KUV+Mmgj+GxNMwB" +
       "d4wSiJYvRFiUHjmZm7f3e3NT64yZ07d+ac\nmTPnfOc7s+duoXruoBgnnFPL" +
       "nI6JgzbhqrUmp4kheGxs2wh2OEkPMMz5OEykjP1/091vR5/YGkSB\nCdRmWg" +
       "lGMR+fciw3OzU+RXneQStsix3MMkt4K1atsWnVndmvjm/rCqHWCdRKzTGBBT" +
       "UGLFOQvJhA\nkRzJTRKHJ9Jpkp5AUZOQ9BhxKGb0EAhaJmzMadbEwnUIHyXc" +
       "YjNSsI27NnHUnoXBJIoYlsmF4xrC\ncrhAC5PTeAbHXUFZPEm56E+icIYSlu" +
       "aPoyMomET1GYazILg4WThFXK0YH5TjIN5MwUwngw1SUKk7\nQM20QMv9GsUT" +
       "r94OAqDakCNiyipuVWdiGEBt2iSGzWx8TDjUzIJoveXCLgJ1zrkoCDXa2DiA" +
       "syQl\n0FK/3IieAqkm5RapItAiv5haCWLW6YtZWbSGw5G/To78sQIiDjanic" +
       "Gk/WFQWuZTGiUZ4hDTIFrx\nths7PbTH7Q4iBMKLfMJaJrHmvV3Jmx8u1zJd" +
       "NWSGFRZTxp2N3T2XE983haQZjbbFqYRCxclVVEe8\nmf68DeheXFxRTsYKkx" +
       "+NfrLn6JvkpyBqHEJhw2JuzhxCTcRMD3j9BugnqUn06HAmw4kYQnVMDYUt\n" +
       "9Q3uyFBGpDvqoW9jMaX6eRvpXwCeJV4/LBuBGhNs0s3F+LRADwnCBY9zx4jb" +
       "rpN2SRywKqjpKpCn\nSN62HPjMxu0pyyTwjXM2I/HCAnm514LZQACO2e1POQ" +
       "b43GqxNHFSxus3vji8ZfvTJ3QAJeg8KwFe\neuNY+cY8pnZAgYBaul2iU3sv" +
       "4Tj4oMya/LHLPS9+il+BWIBPOD1E1JEDs3WyBaX75iWVgVJKDkEP\nA2JSRs" +
       "fRm50vffvGpSAK1SSWZHFw0HJymElsFDKpzdvOPwOQWu0Hdq29fzm548KVL6" +
       "+tLUFcoNVV\nmVetKTOn1+96xzJIGpiptPzzf2799VT9g+8EUR2ko/Q1BhhB" +
       "di/z71GRQf0FNpJnCSVRS6bq4M0C\nHDRbdmDZRlS/FUIhnyZ4FngYbJaNnI" +
       "zKpk0jCMLV4TuD4rrbT4bvufp+yyXllAIttpbx5xgROsmi\nJYiMO4TA+LUX" +
       "Rk6duXV8bwjS2rY1LAQK2+4ko0YeVJZU5qU8QVoi6+fz/Quf2cDfVShoormc" +
       "K/Ak\nI8DjmDFrlqRTQhFZtIw0FVeBsyKTwHmQLykGC2l32IEZQHANVokt7T" +
       "j9XN/LVyXv2Mpfi8HGoLI0\nqL7boVioc8k9YpqYeTXnjTg0B5Qy43Hes8te" +
       "++HCjdEOjSRdGFZVcXO5ji4OKhQttnTNyvl2UNIX\n1608d2T0+qQmzbZKV2" +
       "4x3dz9Z78jfQ9HjBoZH4LylVcHXBOQbV/x8EgdHilLemQSd5TCCkx4QAco\n" +
       "0je2b9v+E70qstpdUmGdbOJ64Y1Vw/kqDzto/bwUMShrcimD6OHfj53/5kwk" +
       "iIITqIHyQWpiJgHC\nd2pOqFGNfEsc+mDXq7e/FteVT0pJKA3rzlcz6G5cxg" +
       "8PXJmJht86mwuihgm0UN0osCl2Y+ZKsE/A\nnYAPeINJdFfFfGV918Wsv0gy" +
       "3X5olG3rT/9SHKEvpWW/0ZfxjfB0eG9UeJdlfCBgy85mpbBctb22\nF3hR2k" +
       "LFDVhDCWyqWL3N4xRUeFev/ohsEgLVG5ZrqkyM6/UCytqEhh8qwW+jVzi8vN" +
       "PAo1aseOsD\nCpCp0TPXLUWlxfHHfos8hS/uC3ogvlegJmHZGxiZIawGAjfM" +
       "i8Ad6q5WwkBobHPf3etbfgQSn6M+\nRb3BUQIXU3O8ECFVuPB/LlzLfWf12x" +
       "Od6Xo0NEU/CypsaDhVXUErlforQdTsVBqqnNZVUTy6vTcq\nvP3FQzYr56WT" +
       "f+WaUcDdzgLulNy6MlCgfPXHcIWNnfC0eza2/0827gUwDxTBrATjcxsJmVSv" +
       "7lCy\nOC6t+jek7+xG7+X9az+2o5+rW0HxXt0Al9uMy1h5spf1w7ZDMlTZ1K" +
       "BT31avDGRNrbucUHRU+lZ2\nEq1DVUWWOvJr2q5R9jRf5dE/UsEyPCwOAAA=");
}
