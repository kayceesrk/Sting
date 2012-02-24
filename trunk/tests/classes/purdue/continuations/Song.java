package purdue.continuations;

import java.io.Serializable;

class Song implements Serializable {
    String name;
    
    public Song(String name) {
        super();
        this.name = name;
    }
    
    public String Name() { return name; }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328822202000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAJVWXWxURRSe3W13W1gsWwoibSlWiDTQXWOiQRqjTUNDYYHa" +
       "LQhryDK9d3Z3yuy9\n1ztz24UQA5oI8uAL4F9UHo3GB8WoL0ZNFP+NCSbgC7" +
       "5oIoli9EUbg8YzM3f/7i41bnJn5+ecOTNn\nvu+bef06aucuSnLCObWt2aQ4" +
       "4hCuSntmlhiCJzM7JrHLiTnGMOfTMJAzDv1D972ZeHx7GIWyqNuy\nRxnFfL" +
       "ro2l6hOF2kvOyidY7NjhSYLfwZm+bYeseN+a9O7uiNoK4s6qJWRmBBjTHbEq" +
       "QssiheIqUZ\n4vJR0yRmFiUsQswMcSlm9CgY2hYE5rRgYeG5hE8RbrM5adjN" +
       "PYe4KmalM43ihm1x4XqGsF0u0PL0\nLJ7DKU9QlkpTLkbSKJqnhJn8UfQYCq" +
       "dRe57hAhiuSld2kVIzpsZlP5gvobBMN48NUnFpO0wtU6CB\noEd1x+t3ggG4" +
       "xkpEFO1qqDYLQwfq1kti2CqkMsKlVgFM220Pogi05qaTglGHg43DuEByAq0O" +
       "2k3q\nIbDqVGmRLgKtDJqpmeDM1gTOrO609kTjf5+e/HMdnDis2SQGk+uPgt" +
       "PagNMUyROXWAbRjgte8uzE\nAa8vjBAYrwwYa5vRDe/uTV/7YEDb9Law2aOw" +
       "mDNu3NvXf2n0x86IXEaHY3MqodCwc3Wqk/7ISNkB\ndK+qzigHk5XBD6c+OX" +
       "D8NfJzGHVMoKhhM69kTaBOYpljfj0G9TS1iO7dk89zIiZQG1NdUVu1IR15\n" +
       "yohMRzvUHSyKql52kP6F4Ev49agsBIplbKuQ5LMC3S8IFzzFXSPleK7pkRRA" +
       "VVDLUxjPkbJju9As\npJyibRFo45LDSMr3L8tIt8yHQrDJviDhGKBzu81M4u" +
       "aMV3744ti2nU+d0scnIeevUaDbdNxkfVwg\nPQRAoZCaeYWEpk7dqOviI5Iy" +
       "5ROX+p//FL8EBwEJ4fQoUfsNzbfJEpzuXlRRxmp8nIAaBrjkjJ7j\n19a88O" +
       "2rF8Mo0lJV0tXOcdstYSaBUaFRtx8uOAJ4Wh9EdavYv57e9dblL69urOFboP" +
       "VNtGv2lLQZ\nDGbetQ1igizVpn/2r+2/nWm/7+0wagMuylRjwBBQe20wRgN9" +
       "RipSJPcSSaOl+aaNLxGQoPm6Dcsy\nrupdcBTy64Qv7gNQ1pEcTMiiWwMIjq" +
       "snsAcldAtPRO+68t7SiyopFU3sqhPPDBGaYYkaRKZdQqD/\n6nOTZ85dP/lI" +
       "BDjtOBoWAkUdb4ZRowwutzaSUu7AlMj65cLI8qeH+TsKBZ20VPIEnmEERBwz" +
       "Zs8T\nMyeUiiXqFFMJFSQrPgOCB2zJMZhIp8MJzQGCW0hKcnXP2WeGXrwiRc" +
       "dR+VpVB2DZXgE3hdqXjJHU\nqlxWIxtCshyqeiHlhVQ2+yX6e2r5AP04rHcW" +
       "H8oc3HHo1KBKiY4jHTbpOVP1PeWmBblo86KMGpf3\nVw1w9NgfJy58cy4eRu" +
       "EsilE+Ti3MZD75bk2hFsodmOLo+3tfXvhafK9Ov4ZZubC+crPe7MN1dNpy\n" +
       "eS4RfeN8KYxiWbRc3b7YEvsw8yQ2snB/8jG/M42WNYw33oVa+EeqnOwL8qUu" +
       "bJAtNZ2DurSW9Y4A\nQTrg6/H/UeW/jiChkCMrW5TDgCoHHf+4RS3EJkUkHw" +
       "JbNUhQDSSpRlhpeFA7WX3RAMIlKfpvdgOr\n18PJ/b/Hn8QfH9T3ZHcjgbZZ" +
       "Xume89+RoQfjRguZ7xS2M8zIHGEtoDW8KLR2qQdL7XAjmQeG7ty8\n9CcQs5" +
       "vodMLvnCLwOrOmK6lXAo7/t4APBJISXE9irvehSJF+FlaHrnHS9A5rdBppRM" +
       "cSt3Gh6hR7\nG0S0D75lPkaWtRJRWdy+qDr8p3RMAKB2VwCllaAOPajc0JDo" +
       "k7e0lN/VTY9t/SQ0Bi8d2viRk/hc\n3TvVZ1sM3k55j7F6ftTVo45L8lStKK" +
       "bZ4qi/aQBuq8eCUAyutdUyM9rnYaX50ke29jsthFVTvIz+\nBc1ogAKLDAAA");
}
