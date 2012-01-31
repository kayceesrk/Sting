package benchmarks;

public class LocalRun {
    
    public static void main(String[] args) throws Exception {
        new Thread() {
            
            public void run() {
                try {
                    FServer.main(new String[] { "4441" });
                }
                catch (Exception x) { throw new RuntimeException(x); }
            }
        }.start();
        Thread.sleep(1000);
        new Thread() {
            
            public void run() {
                try {
                    FClient.main(new String[] { "localhost", "4441", "0",
                                 "10" });
                }
                catch (Exception x) { throw new RuntimeException(x); }
            }
        }.start();
    }
    
    public LocalRun() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1225483756000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAIVXW2xURRiePbvd3hbbLQWRtrSUGiCErZJI1MbEpmlDYZHa" +
       "LY3UkGX27OzuaWfP\nOZ6Zsz2gwVsiyIPGgKhR4cXExPCgEC+JRk3Au77wAL" +
       "7AC0ZNFAMPRmLQ+M/MXs8uleRMZ2f++ee/\nfP83P6euoibmoBgjjBmWOR/j" +
       "+23C5Gil5onOWSyxfQo7jKTHKGZsBjaS+r5/jdn3os9s01BgDnWZ\n1ig1MJ" +
       "vJOZabzc3kDOY5aMC26P4stXhRY52O+9fdXPz+0PaeIOqYQx2GmeCYG/qYZX" +
       "Li8TkUyZN8\nijhsNJ0m6TkUNQlJJ4hjYGocAEHLhIuZkTUxdx3CpgmzaEEI" +
       "djHXJo68s7QYRxHdMhl3XJ1bDuOo\nMz6PC3jY5QYdjhuMj8RROGMQmmaPoY" +
       "NIi6OmDMVZEFwZL3kxLDUOT4h1EG8zwEwng3VSOhJaMMw0\nR/3+E2WPh3aA" +
       "ABxtzhOes8pXhUwMC6hLmUSxmR1OcMcwsyDaZLlwC0erb6kUhFpsrC/gLEly" +
       "tMov\nN6W2QKpVhkUc4WiFX0xqgpyt9uWsKlu7wpF/jkz9NQAZB5vTRKfC/j" +
       "AcWuM7NE0yxCGmTtTBG27s\n2OQet1dDCIRX+ISVzOidH+6O//pZv5LpaSCz" +
       "S2Ixqd/c2tt3fvSn1qAwo8W2mCGgUOO5zOpUcWfE\nswHdK8saxWastPn59J" +
       "d7nnqH/KahlkkU1i3q5s1J1ErM9Fhx3gzzuGEStbork2GET6IQlUthS/6G\n" +
       "cGQMSkQ4mmBuY56Tc89GCDXDF4AvhtS/djFw1B63dEynXTPG5oVw1BPjbYuB" +
       "AFjb668cCjDbZtE0\ncZL621e+fWJ8x/OHVR4EdoqXcbQ8BVHP5bGzwGIl/S" +
       "gQkDqXC3Qp70cdB+8XqPeePt/32lf4TYgl\n+MSMA0SaHFgMiREObVmSFMYq" +
       "JTUJMwwZh0z+8vqxniebrmlIa0gM8fLihOXkMRW5VZUAiR/yw6/R\nDX8c2X" +
       "nmwneXNlSAyNFQXX3UnxT4HvRH1rF0kgb+qKh/5e9t14423fe+hkJQNEAbHE" +
       "OyoQbX+O+o\nwflIiTOEL8E4as/43APK4BCGxSqHxRgRQ6fKPkS822egpJsb" +
       "z4bvuvhJ+xfS4xIzdVRRWIJwhfNo\nJcszDiGwfunVqaMvXz30aBAqy7ZVZj" +
       "kK226KGroHR26vLQ1hXlqA4/fTI50vbGYfaCg4h1qNfN7l\nOEUJUCmm1Fok" +
       "6SSXXBKt4i1JFxCJSApoBxgsSUGR8tUOFACEDQo7tqr72PGNb1wUpW/LwKwQ" +
       "JSIt\nRZ5cWBcQ4/q6TfG7V+C0u+I2FOuCciCyMbF3+77Dg9Lz4omiPlSoL7" +
       "IJQf8lGORTj/959kTbQMWo\nTdUG9clxoBhOMR8saa5YWj7hoL5b0ap8Eg49" +
       "cj3yHD63V5FfV20+xk03f8/JH8nGByN6g5Jv5Za9\nmZICoeXoVMp385Llu1" +
       "O+QhXgn9vyYvuM9hGXCW9YuQ0wHS3KTRN4hU2xIzZawI9+n8/+66KFnoeD\n" +
       "OeNrTVRLsYzr3s7aQyPV3kM9Ob5LxbhaWtYBERDfeviWFZlX/pVcW0W49enU" +
       "ZDqhRJhsRxojsBJj\n/4spyVU9aqeDQ9e1j1cOSSYJpTBTZvpbjfpOoqZBkH" +
       "a2lb26A77+JbySgl3Q4ciSEFUZU91EA9v9\nD3dNCST1A5/uPnHjB35Zwq7C" +
       "hEJPj1dfQLO4iqTvvVCIht89mddQ8xzqlM0XNvkspq4gpTlwmo0V\nF+NoWc" +
       "1+bSuk3v2RMkR6/RCputbPwdVgCfEamEjanfACyBaThxrAgIMuw8RUVTfggR" +
       "Izy3MNwjbl\nGHloJwrFfuelNW/9fObKdLeqVtUUrqvry6rPqMZQ2tVuC75Y" +
       "u9QNUvrcprWnDk5fTmlFGtzKURBw\nJKbjXjnVmnKlhInlFUyMUcskgs9Le4" +
       "pCDStWbrFh06sDjfg9omIn7xLD3UuS9P8yOLStTbowpwF0\nVfa9pYuw3rtx" +
       "Tye2aPDkBQ9wFMoDdKVgwlbKZmGxYBnpQBVXB1T4xEgh4y2lHkq8rKvq/jej" +
       "em59\n8Py+DWft6Deqykt9cTM0pxmX0moEVs3DtkMyhrypWeHRln+gzWirNH" +
       "FihUmAEi/wH5Y1EsWoDQAA\n");
}
