package sjservlet.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.util.*;
import sjservlet.server.Server;

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
        catch (Throwable x) {
            pw.println(x);
            pw.println(FAIL);
        }
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
    final public static long jlc$SourceLastModified$jl = 1235507476927L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAL1Xa2xURRSefXTb0tV2S6mEPni0SKuwJSYYpTHYNK1tWWjZ" +
       "bYmtQLm9O7ud9u69\nlztztxdCCGgiiMZHAF9R+WNiNPxQiPrHqIng25hgAv" +
       "6BPxglUYz+UWLQeGbu3bu7d7c1GOMmM3d2\n5syZ8/jOOTOnrqEKaqAoxZQS" +
       "TZ2Jsr06pqLXpmawzGg0MTQiGRQnexWJ0lFYmJR3/0W2vxV5eMCP\nfBOoXt" +
       "V6FCLR0WlDM9PTo9OEWgZaoWvK3rSiMYdjCY+NbTfmvjw81BRAtROolqgJJj" +
       "Ei92oqwxab\nQOEMzkxhg/Ykkzg5gSIqxskENoikkH1AqKlwMCVpVWKmgWkc" +
       "U03JcsJ6aurYEGfmJmMoLGsqZYYp\nM82gDNXFZqSs1GUyonTFCGXdMRRKEa" +
       "wk6R50APljqCKlSGkgbIzltOgSHLv6+TyQLyIgppGSZJzb\nEpwlapKh5d4d" +
       "rsbtm4EAtlZmMJvW3KOCqgQTqN4WSZHUdFeCGURNA2mFZsIpDC2blykQVemS" +
       "PCul\n8SRDS710I/YSUFULs/AtDC3xkglO4LNlHp8VeGs4FP7z6MjvK8DjIH" +
       "MSywqXPwSbWj2b4jiFDazK\n2N543YweHxw3m/0IAfESD7FN07P63bHY1Q+W" +
       "2zRNZWiGBRYn5Rt3N7ec7/muOsDFqNI1SjgUijQX\nXh1xVrotHdDd6HLki9" +
       "Hc4ofxj8cPvoF/9KOqQRSSNcXMqIOoGqvJXmdcCeMYUbE9O5xKUcwGUVAR\n" +
       "UyFN/AdzpIiCuTkqYKxLbFqMLR0hVAnNB40g+xfhHUPhxBBgOatgFqUzDHUw" +
       "TBndQQ15B52h9sKO\n3LeQ1OKcb53z+UCpZm+AKYDGAU1JYmNSfu3K5/v7Nj" +
       "92xHYXh5gjE0NN7hHR3Nc9Avl8gvdiDkbb\nWD2GIe3lQWIdOt/ywifSy2B6" +
       "MAEl+7DQ0DcX5D1sumvBHNKbj8BBGEkAkEm54eDVZS9+8/o5PwqU\nzSMxd7" +
       "JfMzKSwqGQC5x65zjvCiCo3Yvjcmf/fHTLmQtfXOrII5qh9pJAK93JA2WV1/" +
       "aGJuMkJKI8\n++f+GPjlWMW9b/tREKIP8g+TADUQzK3eM4oCpjuXfLgugRiq" +
       "SZUovoiBgeYKFOZ9mHd1Nj7AFw0e\nAUXeuv5IaP3F92rOCY1zKa62IBcmML" +
       "MDJpL3/6iBMcxfen7k2Ilrhx8KQIjquu1zhkK6OaUQ2YIt\ntxXHGBcvyWHz" +
       "0+nuuifX0XeEi6tJJmMyaUrBkJMlRdHmcHKSiaQUKUiAIu+AJcJTkL8gFU4q" +
       "wMjW\nVfdlAZ5lMkR0acPxZztfushziC4M08hjTUiKLDHR7uN9R8ki/9/CEd" +
       "yQVxuiftZWINyZ2Dm0+8gq\noXkhu7XOH8tl6BczfuC0dsFY6Oe1Jg8Vsv+3" +
       "Q6e/PhH2I/8EqiS0n6iSwo1Ft9rgL5NlPSz2vT/2\nyvWv2GXh2jzauGDNVm" +
       "mu2C4VBMI9F7KR0JsnM35UOYHqRKWUVLZdUkzu+AmodbTXmYyhW4rWi+uW\n" +
       "naS73Whq9iK94FgvzvM5Csacmo+rbGgLmlqw7BJo66EtdrKp+PLFCO/qLR9A" +
       "spKaMlRlsWeD6FtF\nv9L2XYDBudy+fK4NEEzFtcOCjYmx3t6+REJQL4ZLgg" +
       "ADVyxqF2QRWz4k1tcXyXQHtAZHpoYyMgVT\nEhEHbiojEB9vFNLwrhskCfb3" +
       "DMb4n/ucI63SbQLKbQ6uUR7XOVACBlvmq+biJnL4wV/Dj0pnd9o1\nt744ev" +
       "tUM7Ph5Le48/6wXKaEVDNNX6fgLFZc6AfFuUE4dt2C0N8iLj958AUSmzrXrK" +
       "35AdLkPBUg\n4kzGMdz01NEcNERpkG66NCz3GMUrTyTbtC0wTT71C1DaOC65" +
       "0xVv6i5G7yKjWFCB4SYXL1XQ1kBr\ndPDS6MGLcDnv+srmrFy5dUDaxEFq5Q" +
       "u5/YVwTZF0+aTnYdBalkGfJWOdX5GEONsAkkQljJamoRGD\nZOAulXUue8+0" +
       "vvr9mSvxBhsz9o24reRSWrjHvhWLY2p0jtqVC50gqM/eufLUgfjlKb+TuodA" +
       "vKxG\nkkLRXQVRICbGrHlmtrouqYa2GVqT45Kmci4pE4KQM3SDZCV4sJQ1da" +
       "4cOKZeXWzqacb06AB0js3j\neI/Jr4EO9e3/RE11yMTznBzIp5bxHEP7Tke0" +
       "6OCw614XBW7MGqbKSAZDpBaQCQuAKwOwKoxcYmo/\nH87yLmMtNL21KA5i0O" +
       "KO0eM3Fwf+8vwXIBvn3R7BF3JsRVJ7ALP/Upkt0HY5yuz6H5U5BEUsqcGb\n" +
       "5t9rA/aodt8A/P63tOTxbj8x5VXnd3d8pEc+E7da9xlYCW+xlKkohTW8YBzS" +
       "DZwiQthKu6Lr4vME\n3PpKHiO8gtsjIezjNulTIKFLyiee1ufJYN5AsdDfXH" +
       "zxveMQAAA=");
}
