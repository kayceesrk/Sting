package benchmarks;

import java.io.Serializable;

public class BigObject implements Serializable {
    private int id;
    private int size;
    public byte[] bytes;
    
    public BigObject(int id, int size) {
        super();
        this.id = id;
        this.size = size;
        bytes = (new byte[size]);
    }
    
    public String toString() {
        return "BigObject<id=" + id + ", size=" + size + ">";
    }
    
    public int id() { return id; }
    
    public int size() { return size; }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1225482984000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAALVXWWwbRRger2M7h5vEISkhSZMeRkqhcVAR5YiQCKFR0ro0" +
       "JGnVGlXJej22J1nv\nLjtjxylVoSDa0gcQasshjr4gIaE+QCsOCQRIlBuE1I" +
       "eWl/alCJCgqH2BChXEPzPra+2GF2ppZ8cz\n//z3//2zxy8iH7VRhGJKiWnM" +
       "RdiihakYzfgc1hiNTG2aUG2KEyO6Suk0bMxos/+Q7W+HnhhTkCeG\n2gxzWC" +
       "cqnU7bZjaVnk4TmrfRSsvUF1O6yRyOVTzuWXN14dsDm7q9qCWGWogxxVRGtB" +
       "HTYDjPYiiY\nwZk4tulwIoETMRQyME5MYZuoOtkNhKYBgilJGSrL2phOYmrq" +
       "OU7YRrMWtoXMwmIUBTXToMzOasy0\nKUOt0Tk1pw5mGdEHo4SyoSjyJwnWE/" +
       "QRtBcpUeRL6moKCJdHC1YMCo6Do3wdyBsJqGknVQ0XjtTN\nEyPBUJ/7RNHi" +
       "8GYggKOBDGZpsyiqzlBhAbVJlXTVSA1OMZsYKSD1mVmQwlDXNZkCUb2lavNq" +
       "Cs8w\n1Ommm5BbQNUg3MKPMNThJhOcIGZdrpiVRWurP/j3oYk/V0LEQecE1n" +
       "Suvx8O9boOTeIktrGhYXnw\nSjZyZHxntkdBCIg7XMSSZvjm97ZFf/m4T9J0" +
       "16DZKnJxRru6oWfF6eEfG7xcjXrLpISnQoXlIqoT\nzs5Q3oLsXl7kyDcjhc" +
       "1PJj/f+fib+FcF1Y8jv2bq2YwxjhqwkRhx5gGYR4mB5erWZJJiNo7qdLHk\n" +
       "N8V/cEeS6Ji7wwdzS2VpMc9bCKEAPB54+pH8NfGBoeD9JCVNitA5Th3K87F5" +
       "weMBdXvcpaNDno2Z\negLbM9obF77es3Hz0wdlIHjyONIYao+D29MZ1Z6nka" +
       "IA5PEIpjfw/JL2D9u2usjzPr/v9IqXvlBf\nBW+CVZTsxkJpz0IdH+HQ+iVh" +
       "YaRUVOMwUyHmEMufXz7S/ZjvkoKUmtAQLS6OmnZG1Xl0ZS1A6MPu\nBKwl4f" +
       "dDW06e+eZcfykVGQpXVUj1SZ7hq92utU0NJwBBSuxf+Gvs0mHf3e8oqA7KBo" +
       "CDqRBuqMJe\nt4yKTB8qoAa3xRtFTUmXeQAaDNywUGYwH4Ni3gIOr4cHKgB1" +
       "OrnSxgeRHGUZAkFpd9kgMOnKk/7b\nzn7Y9JlwSgG+WspwbgozWQyhUiJM2x" +
       "jD+rkXJw4fvXjgYS+Un2XJ4DPkt7JxnWh5OHJjZf1wCxI8\nf347MdT6zAB9" +
       "V0HeGGogmUyWqXEdA96qum4u4MQME4ATKgM3gSngrGAcsAlgbkYHRtIdlicH" +
       "eVqj\n+iOd7UeeX/vKWY4PlvBXB/eV0FSh1RA0YZMMVHjOgaDnel//6eSFyX" +
       "aZMBKn11RBZfkZidXC400W\n98CqpSQI6lO3rjq+d/J8XGJYW6XHNhrZzB3H" +
       "fsBr7wtqNQrXC92E/1mXF8aFPXzsLxqKhKFIqNPD\ny7K9FEJAp3kZjODaqV" +
       "2bZg+uFlGUrhE8JeN8kZtX7HmBzbolq3uUN8RSWZA9f+w78f3RoKjrAKGj\n" +
       "xFB1HnX6oCznGq3AxWL3R9teu/IdOy88UKosrlh3vhr2tqtlRX/XmVzI/9ax" +
       "jIICMdQq2rlqsO2q\nnuUZHIOGTEecxShaVrFf2VwlKg4VkaPHnQhlYt01XY" +
       "oazDk1n9e7yrgBnlZ4ljllvMxVxh6PxSdD\n4kCvGFcViy5g2SSn8hsQUkii" +
       "GL9mQGP+3lAhhQNEsyOlubaUYT7cyyS4L8GOY0+HozgqvKvZPcCH\nlQz54o" +
       "sMcqf6xiBai6y6E97wZeWD5WGBo3VxlUpvua9a1TepiguScG6jJdP3Fuc9AA" +
       "YJDQo5XehY\n/P+dVt6DhLZbql2sgO5Jnrl8bQ142a9jIyU7tnDNWL4KX/j/" +
       "NuY0UJ5DcC8yDcyRrrAnC5KYkeIN\nFTbzVerZqM/lry3C1FKah3LdD3nT5E" +
       "tFpJvM0KorZeWhocq8bLQx3IiN6Yrs7LLEa0yEb0mM+U8A\nioEHNW5/wfbW" +
       "kl9kbclG5ZA70lCJ4e3SGXy6jUPrimtdOgWsHthxObhfPbVLcfhFGGpgpjWg" +
       "4xzW\na2DawJKY5nb3qfXPNk0r7zPRwGpeVmq08ZBDN1nD0TsqKuomeLqciu" +
       "qq1c3/h3gQhuqZKT8ZaoRE\nbnjKPC+PyTaarlCXF32fo27fdVKXloCtWql1" +
       "NZTiIBd2lApfJ6UeLQfIGmoBTjQUL9T8DtVZ9XEr\nP8G01adn+z+1Ql9J0C" +
       "t8JgXgWyWZ1fXy9lE291s2ThIhKiCbiSzXpxhqLF3p+cp+gWuzec+/igYn\n" +
       "u7cPAAA=");
}
