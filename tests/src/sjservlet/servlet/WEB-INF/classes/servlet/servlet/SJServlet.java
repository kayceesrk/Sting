package servlet.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.util.*;
import servlet.server.Server;

public class SJServlet extends HttpServlet {
    final public static String SUCCESS = "success";
    final public static String FAIL = "fail";
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    private void run(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException, SJIOException {
        final SJProtocol p_server_dual =
          new SJProtocol(
          ("H4sIAAAAAAAAAFvzloG1uIhBvTi1uDgzPy9Lr6SyILVYD8SFsIK9nJ1S0zPz" +
           "QoC8+GQGCGBkYmCo\nKGJQw6MLSZOqQPIqHY01VRBNGng0BUOk0O1i9GFgTc" +
           "7IzEkpYdDwgWnXB2vSh2vXR9FuDbRJGc0m\nEJmflJWaXAKyC2LJxfXz8jlV" +
           "f2+FuE20ID+nMj0nvwSqA6LGUXV7qM+L/fIQNdJY1PiDTY1P/mMm\nI3vJ8Q" +
           "knM8jJHAX5xZklQPtLGCR8YJr0S0syc/QDoDLWFQUFFcDgV8UbJnkp2ALfFI" +
           "8eXyA7MT3V\nOT83tzQvMzmxBB6oK50OM/pw77sPDlTuXIg6kFQJgyjCkZAA" +
           "hYRjIUMdA1NBRQHQmTJoXg/ISUxO\n9cjPSUktik9e/vhUjat3ZwfYZJa8xF" +
           "ygkUI+WYllifo5iXnp+sElRZl56UAflzAIgET1QKJ6EFEA\njpOkJoYCAAA="));
        Map params = request.getParameterMap();
        String[] ha = (String[]) params.get("HOST");
        String[] pa = (String[]) (String[]) params.get("PORT");
        if (ha == null || pa == null) {
            throw new SJIOException("[SJServlet] Invalid parameters: " + ha +
                                    ", " + pa);
        }
        String host = ha[0];
        int port = Integer.parseInt(pa[0]);
        final SJService c = SJService.create(p_server_dual, host, port);
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        pw.println("<html>");
        pw.println("<body>");
        SJSocket s = null;
        try {
            s = c.request();
            SJRuntime.pass(s, "Hello from SJServlet.");
            pw.println(SUCCESS);
        }
        catch (SJIncompatibleSessionException ioe) { pw.println(FAIL); }
        catch (SJIOException ioe) { pw.println(FAIL); }
        finally {
            pw.println("</body>");
            pw.println("</html>");
            pw.close();
            SJRuntime.close(s);
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
        try {
            this.run(request, response);
        }
        catch (SJIOException ioe) { throw new IOException(ioe); }
    }
    
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
          throws ServletException,
        IOException {
        try {
            this.run(request, response);
        }
        catch (SJIOException ioe) { throw new IOException(ioe); }
    }
    
    public SJServlet() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1235503909508L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAL1Xa2xURRSefXS3pavtllKRPni0PKqwJSYYpTHYNK1t2dLS" +
       "bYmtYLm9d3Y77d17\nL3fmbi+EENBE8BEfAXxF5Y+J0fBDIeofoyaCb2OCCf" +
       "gH/mCURDH6R4lB45m5d193tzUY4yYzd3bm\nzJnz+M45MyevogpqohjFlBJd" +
       "m4mxvQamotenZrDMaCwxMCyZFCvdqkTpKCxMyrv/Ijveij7U50e+\nCVSn6V" +
       "0qkejotKlbqenRaUJtE60wdHVvStWZy7GEx+bW63NfHh5oDKCaCVRDtASTGJ" +
       "G7dY1hm02g\nSBqnp7BJuxQFKxMoqmGsJLBJJJXsA0Jdg4MpSWkSs0xMRzDV" +
       "1QwnrKOWgU1xZnYyjiKyrlFmWjLT\nTcpQbXxGykgdFiNqR5xQ1hlHoSTBqk" +
       "L3oAPIH0cVSVVKAWFDPKtFh+DY0cvngXwRATHNpCTj7Jbg\nLNEUhpZ7d+Q0" +
       "btsKBLA1nMZsWs8dFdQkmEB1jkiqpKU6EswkWgpIK3QLTmFo2bxMgajSkORZ" +
       "KYUn\nGVrqpRt2loCqSpiFb2FoiZdMcAKfLfP4rMBbQ6HIn48N/74CPA4yK1" +
       "hWufwh2NTi2TSCk9jEmoyd\njdes2LH+cavJjxAQL/EQOzRdq98di1/5YLlD" +
       "01iGZkhgcVK+fmdT87mu76oCXIxKQ6eEQ6FIc+HV\nYXel0zYA3Q05jnwxll" +
       "38cOTj8YNv4B/9qLIfhWRdtdJaP6rCmtLtjsMwjhMNO7NDySTFrB8FVTEV\n" +
       "0sV/MEeSqJibowLGhsSmxdg2EEJhaD5oCnJ+Ud4xFEkMAJYzKmYxOsPQGoYp" +
       "ozupKe+kznTuW0ho\nc743z/l8oFKTN7xUwGKfrirYnJRfu/z5/p6tjx5xnM" +
       "UB5krE0K00y8795g5APp/gvJgD0TFUl2lK\ne3mA2IfONb/wifQymB3Up2Qf" +
       "Ftr55oK8h013LJg/uvPR1w8jCcAxKdcfvLLsxW9eP+tHgbI5JJ6b\n7NXNtK" +
       "RyGGSDps49zrsC6GnzYrjc2T8/Nnj6/BcX1+XRzFBbSZCV7uRBsspreVOXsQ" +
       "JJKM/+uT/6\nfjlacffbfhSEyIPcwyRADARyi/eMomDpzCYerksgjqqTJYov" +
       "YmCguQKFeR/hXa2DDvBFvUdAkbOu\nPRzaeOG96rNC42x6qynIgwnMnGCJ5v" +
       "0/amIM8xefHz56/OrhBwIQnobh+JyhkGFNqUS2YcstxfHF\nxVM4bH461Vn7" +
       "5Ab6jnBxFUmnLSZNqRjysaSq+hxWJplISNGC5CdyDlgiMgW5C9LgpAqMHF0N" +
       "Xwbg\nWSY7xJbWH3u2/aULPH8YwjANPM6EpMgWE20+3q8rWeT/mzmC6/NqQ8" +
       "TPOgpE2hO7BnYfWSU0L2S3\n3v1j5xj6xYwfOK1fMBZ6eZ3JQ4Xs/+3Qqa+P" +
       "R/zIP4HChPYSTVK5seg2B/xlMqyHxb73x1659hW7\nJFybRxsXrMkuzRQ7pI" +
       "JAuOt8Jhp680Taj8ITqFZUSUljOyTV4o6fgDpHu93JOLqpaL24ZjkJujMX\n" +
       "TU1epBcc68V5PkPBmFPzcaUDbUFTA5ZdAm0jtMVuJhVfvhjlXZ3tA0iGqSVD" +
       "RRZ7Nom+RfQrHd8F\nGJzL7cvnWgHBVFw5bNiYGOvu7kkkBPViuCAIMHDFYk" +
       "4xFrHlQ2J9Y5FMt0Grd2WqLyNTMCkRceCW\nMgLx8WYhDe86QZJgb1d/nP+5" +
       "xz3SLt0moNzq4hrlcZ0FJWCweb5KLm4hh+//NfKIdGaXU2/riqO3\nR7PSm0" +
       "58i9vvjchlCkgV040NKs5gNQf9oDg3CMduWBD6g+LikwdfILGlfe366h8gTc" +
       "5TAaLu5AiG\nW542moWGKA3SDZeG5R6jeOWJZhq3B6bJp34BSgfHJfe54k2d" +
       "xehdZBYLKjDcmMNLJbS10BpcvDR4\n8CJczruesjkrW25dkDZykNr5Qu58IV" +
       "yTJFU+6XkYtJRl0GPL2ODXIyHOdoAk0QijpWlo2CRpuEdl\n3IveMy2vfn/6" +
       "8ki9gxnnNtxaciEt3OPciMUx1QZH7cqFThDUZ25fefLAyKUpv5u6B0C8jE4U" +
       "oeiD\nBVEgJsbseWa25VxSBW0rtEbXJY3lXFImBCFnGCbJSPBYKWvqbDlwTb" +
       "262NTTjBmxPuhcm4/gPRa/\nArrUa/6JmhqQiec5OZBPLeNZhs6djuix/qGc" +
       "e3MoyMWsaWmMpDFEagGZsAC4MgCrwsglpvbz4Szv\n0vZC09uK4iAObdA1+u" +
       "CNxYG/PP8FyMZ5t0fwhRxboej3YfZfKsMVGHeVGf8flTkERUzR4T3z77UB\n" +
       "e1Tl3gD8/re05OHuPC/lVed2r/vIiH4mbrW5J2AY3mFJS1ULa3jBOGSYOEmE" +
       "sGGnohvi8wTcPj1P\nEV6/nZEQ9XGH8Kn8NP/7tDFP9vIGiY3+BjoI287bEA" + "AA");
}
