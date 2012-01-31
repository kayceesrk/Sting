//package purdue.continuations;

import org.apache.commons.javaflow.Continuation;

class Test1 {
    static class myRunnable implements Runnable {
        
        public void run() {
            System.out.println("started!");
            for (int i = 0; i < 10; i++) this.echo(i);
        }
        
        private void echo(int x) {
            System.out.println(x);
            Continuation.suspend();
        }
        
        public myRunnable() { super(); }
        
        final public static String jlc$CompilerVersion$jl = "2.3.0";
        final public static long jlc$SourceLastModified$jl = 1326277012000L;
        final public static String jlc$ClassType$jl =
          ("H4sIAAAAAAAAAJVWXWwUVRS+u9tu/xbbLaU2LS20lEBT2VUTTaSJ2jQQShep" +
           "u9VIDVlmZ+7u3jI7\nM957Z7tFQ1ATQR5MDKho/HkxMRpelKgvJpoA/j9hIr" +
           "7gC0ZJFKMPRmLQeO6dmf2Z2YJuMnfvzzn3\n/H3nnHv6KmplFCUYZoyYxlKC" +
           "r1iYydHMLWGVs0Rm97xCGdZmdIWxBTjIqgf+IQ+/G39qVxiFFlGv\nYU7rRG" +
           "ELRWraheJCkbAKRRstU18p6CZ3bwzcsX3T9eWvj+4eiqDuRdRNjAxXOFFnTI" +
           "PjCl9EsRIu\n5TBl05qGtUUUNzDWMpgSRSeHgNA0QDAjBUPhNsUsjZmplwVh" +
           "L7MtTKVMbzOFYqppME5tlZuUcdST\nWlLKStLmRE+mCONTKRTNE6xr7DF0GI" +
           "VTqDWvKwUg7E95ViTljcmdYh/IOwmoSfOKij2WloPE0Dja\n4OeoWjw+BwTA" +
           "2lbCvGhWRbUYCmygXkclXTEKyQynxCgAaatpgxSOBle9FIjaLUU9qBRwlqMB" +
           "P928\ncwRUHdItgoWjdX4yeRPEbNAXs7po7Y3G/j4+/+dGiDjorGFVF/pHgW" +
           "nEx5TGeUyxoWKH8ZqdODm7\nz14fRgiI1/mIHZrpzR8+lLry8QaHZqgJzV6J" +
           "xax6/e71wxemf+iICDXaLZMRAYUGy2VU592TqYoF\n6O6v3igOE97hJ+lP9x" +
           "15B/8cRu2zKKqaul0yZlEHNrQZd94G8xQxsLO7N59nmM+iFl1uRU25Bnfk\n" +
           "iY6FO1phbim8KOcVCyHUAV8EvrXI+Yk14qiNY8YTbImjpJixJKNq0rKpZuMk" +
           "QJUTw5YYz+KKZVJY\nFpIuR0XcfctyKARmrfenmA543GXqGqZZ9a3LXz6xY+" +
           "7ZY07ABMhcrTja7EhK1EsCN4OAO8ZLK2nb\nMJScjlEoJKWsFcB0HDdNqbIi" +
           "Eqby5IXhlz9TXoMwgDsYOYSltaHlFjEC0503rCcztWychZkCYMmq\nfUeuDL" +
           "7yzdvnwyjStKakqps7TVpSdAELL4l6XXH+E0DTuB/TzWT/enzPmW+/urS1hm" +
           "6OxgNJF+QU\nSTPmjwI1VaxBUapd/9Jfu3470XrP+2HUApko3K4AgiCxR/wy" +
           "GpJnyitEwpZICnXlA4Z3cnDQcp3B\nYoyJocdBCsSiz6egrGHXno7efvGjrv" +
           "PSYq/cddfVxQzmTvLEa/FfoBjD/qVT8ydeuHr0UQB2xbJk\nzFEFKG9tTDOh" +
           "lSbQ8st7Uz3PbWMfyMh2kFLJ5gJgUJYVXTeXsZblsi7F62qgLD3ggFgOShjg" +
           "P6vD\nRY6JVqgMqGxSJBIDfSdfnHj1oigjlvRHv1DNU1BsbAqJcUvgUKyHBX" +
           "D7atZC4h90DIhNZPbvPnBs\nTBpcf92ku3AvR+VgUu4UfcWDQin3+B9nX+/c" +
           "WNNwsv66ETmOOiJCHEWZ7Ine7TXVJ91EE/O10Dyk\n0sJvCS97RTyGVyvnsh" +
           "UdfeT32DPKuf1O0e1tjN0Owy7d9cZ3eOL+mNqkgkTd5uy5MSz1CYPMbTdM\n" +
           "/D2y9dUSI5K5b2LLbV0/QWKskvNxdzONoc8bQnkhs10WA+V/F4MNPo/49YmX" +
           "hx6MFMnnYZFubh0I\ndPRGpql630BC0kZFJbKGpALd4KIYfMJhnW4/kP/iMC" +
           "6GXjdnm2LBsnN6FQurwPimGJ/mKEJtgwXb\n9jwlJeiKZbdtPz/y5o9nLqf7" +
           "nOA7b5tNgedFPY/zvpFSuiwBv9EbSZDU5yZHTx9Of58Lu9pt56il\nbBJNGr" +
           "AnmB8Ni3urTl0D3zB83a5Tu/+zU9ssSsoKPDibOi5Ul2VzlkPzADgQ3n4393" +
           "QGbMFq0ZSm\nBAySG4uV4E6Fo85aD/YSfHD1jg2OHgg8tZ0HoTp24cDWs1b8" +
           "C9l3qo+2Nng55W1dr0NtPYKjFsV5\nIm1oc9qJJf/yHDpJEzU4WtOwljpjh4" +
           "dI3AoesVqyPHt6agXLedlVQv8C8PNw74kMAAA=");
    }
    
    
    public static void main(String[] args) {
        Continuation c = Continuation.startWith(new myRunnable());
        System.out.println("returned a continuation");
        Continuation d = Continuation.continueWith(c);
        System.out.println("returned another continuation");
        while (d != null) { d = Continuation.continueWith(d); }
    }
    
    public Test1() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1326277012000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAJVXXWxURRSe/e3farulVNI/CtRAU9lVE4xSE2kaGgqL1G5p" +
       "pIYsd++d3d4ye+/1\nztztgoagJoI8mBj8j8KLidHwoBD1QaMmgP++YAK+wA" +
       "tGSRQjD0Zi0Hhm5u7u3btLiU3u7PycM3PO\nme98c3riKopQGyUoplQ3jYUE" +
       "22dhKlozu4BVRhPprVOKTbE2ThRKZ2Aho+75V599P/70liAKzKFO\nwxwjuk" +
       "Jn5m3Tyc/PzOu0ZKNByyT78sRk7o51e2xcc2Pxu0Nbe0OofQ6160aaKUxXx0" +
       "2D4RKbQ7EC\nLmSxTcc0DWtzKG5grKWxrStE3w+CpgEHUz1vKMyxMZ3G1CRF" +
       "LthJHQvb4szyZArFVNOgzHZUZtqU\noY7UglJUkg7TSTKlUzaaQtGcjolGH0" +
       "cHUDCFIjmi5EGwO1X2Iil2TE7weRBv1cFMO6eouKwS3qsb\nGkMr/RoVj4e2" +
       "gQCoNhUwmzcrR4UNBSZQpzSJKEY+mWa2buRBNGI6cApDPTfdFISaLUXdq+Rx" +
       "hqEV\nfrkpuQRSLSIsXIWh5X4xsRPcWY/vzjy3tSMa++fI1F+DcONgs4ZVwu" +
       "2PgtKAT2ka57CNDRVLxetO\n4sXJXU5fECEQXu4TljJjd360M3Xls5VSpreB" +
       "zA6BxYx6476+/nNjP7WEuBnNlkl1DoUaz8WtTrkr\noyUL0N1d2ZEvJsqLn0" +
       "9/sevgu/jXIGqeRFHVJE7BmEQt2NDG3X4T9FO6geXsjlyOYjaJwkRMRU0x\n" +
       "hnDkdIJ5OCLQtxQ2L/olC8m/AHwDbr+JNww1MUxZgi4wlOQ9mqS2mrQcW3Nw" +
       "EqDKdMMRGM/gkmXa\nMMwnXY0S3/v2xUAA3OrzpxgBPG4xiYbtjPr25W+e3L" +
       "ztucPywjjIXKsATvKkhPckCDMccA8KBMTW\nyzgaZbTGbFvZx7Ok9NS5/te+" +
       "VN6E2EMMqL4fCxcDi2HegtK9S5LIeDUFJ6GnAEIyatfBKz2v//DO\n2SAKNS" +
       "SSVGVywrQLCuFYKGdOp3ucfwUgNOQHcqOzfz+y/dT5by+uq0KaoaG6TKvX5J" +
       "my2h9621Sx\nBkxU3f6Vv7f8cTTywAdBFIb047FWADaQzQP+M2oyZrTMPtyX" +
       "UAq15eocb2UQoEWPw7yN8aZDwgPu\nostnoCCu689E777wSdtZ4XGZ49o9ZJ" +
       "jGTGZMvHr/MzbGMH/x1amjL1099FgIctSyxJ2jEkjeUZtb\n3CqNo+W3k6Md" +
       "z6+nH4qbbdELBYcpWYKBixVCzEWsZZggo7iH+ATfQABiWeAtAH2GwEbSRStQ" +
       "BFQ2\nYIbEiq4XXx5+4wLnDkvEo5ubVjaQT6wJ8HZt3SIf93PgdlW9hWzfKx" +
       "2IDad3b91zeLVw2LvdiDtw\nN0fF+kyc4I9JGQqF7BN/nj7WOli1cMRjwKC7" +
       "S9XEkWpw+29GyOIxOfTotdizypndkjY7ay9is+EU\nNhz/EQ9viqkNOKCFmd" +
       "Z6gouYVMJSTeT1SybydvF+VYEeSj80vPautl8A6DfJ4bg7OY3hsTa4/fzM\n" +
       "ZpHcyv9O7pW+oPjtiRd7HwnN618Fefq4eV33LNcqjXrDAwlm1xoqLqpXGNAO" +
       "IeqAbx18y11SF798\nMc6bTjcHeTsg2lUSPkGGopaTJboKHSqqncb4rF6E/0" +
       "EWXCzfzJOhoWvBj7uHBL2Eswotx7S2kqkv\nVGrqD2Fra8WzHvgGl/BMCC6D" +
       "AkokDM/ZhCxWGtjurwtqciKj7v9057Hr37NLAptVeuT79JXqM2pW\n8TD3/e" +
       "eL8eh7xwtB1DSHOkRtpxhsViEOZ6o5cJqOu5MpdFvNem2lJcuK0QpM+vww8R" +
       "zrJ2YvYMKs\nBiqCiydKAWTxzsMNoRDJ6YYiiqhNJcADwUaezTcI25StF6Ba" +
       "Kbrl1AsDb/186vJ0l0xpWXOuqSv7\nvDqy7hR2tVmcVFYtdYKQPjOy6sSB6U" +
       "vZoMtRGxgKAY54d3OpctVB6UoZE8uqmBgnpoE525fXJMHq\nZqJSwcNiqQ40" +
       "fLxRxk6cVR+4gAzYkrR+S86HSjmichMbwFkionTrTR5kKFwAyIrFtCUVZmGy" +
       "aOqa\n0CMeNg94YlczA3cfEZUXf3VX1P3PJCt7dfW5PetOW/GvZbKXq+8mKI" +
       "FzDiFeIHr6UcvGOV0Y2yRh\naYkfePO7GlWATKRKdSyMtaVOUXAX1+GjRYHr" +
       "fCnwH5Y2eFQ+DgAA");
}
