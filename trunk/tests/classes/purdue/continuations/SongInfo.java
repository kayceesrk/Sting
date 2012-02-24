package purdue.continuations;

import java.io.Serializable;

class SongInfo implements Serializable {
    String name;
    int rating;
    
    public SongInfo(String name, int r) {
        super();
        this.name = name;
        rating = r;
    }
    
    public String Name() { return name; }
    
    public int Rating() { return rating; }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1328822109000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAALVXXWxURRSe3W23f6tlS/kJbSkUiDTArppIlD7gptJQWGjd" +
       "LUSWkGV6d3Z3yt17\nr3fmbhdCCGgiyIOJAfyLyouJ0fCgEPXFqIngvzHBBH" +
       "yBF4ySKEZflBg0npm5+3d3W+ODTXZm7sw5\nM2fO+c53puduoVZmowgjjFHT" +
       "mInwgxZhsjWnZ4jGWSS5bRLbjGRGdczYFCyktf1/091vh5/Y6ke+\nFOoxzJ" +
       "hOMZvK26aTy0/lKSvZaIVl6gdzusndHRv22LTqzuxXx7f1BVB3CnVTI8kxp9" +
       "qoaXBS4ikU\nKpDCNLFZLJMhmRQKG4RkksSmWKeHQNA04GBGcwbmjk1YgjBT" +
       "LwrBHuZYxJZnlifjKKSZBuO2o3HT\nZhwtiM/gIo46nOrROGV8JI6CWUr0DH" +
       "scHUH+OGrN6jgHgovj5VtE5Y7RMTEP4p0UzLSzWCNllZYD\n1MhwNOjVqNx4" +
       "9XYQANW2AuF5s3JUi4FhAvUok3Rs5KJJblMjB6KtpgOncLRszk1BqN3C2gGc" +
       "I2mO\nlnrlJtUSSHVItwgVjhZ5xeROELNlnpjVRGsiGPrr5OQfKyDiYHOGaL" +
       "qwPwhKyz1KCZIlNjE0ohRv\nO5HT43ucfj9CILzII6xkYmve2xW/+eGgkulr" +
       "IjMhsZjW7mzsH7gc+74jIMxot0xGBRTqbi6jOumu\njJQsQPfiyo5iMVJe/C" +
       "jxyZ6jb5Kf/Kh9HAU1U3cKxjjqIEZm1B23wThODaJmJ7JZRvg4atHlVNCU\n" +
       "3+COLNWJcEcrjC3M83JcspD688FviTsOioajrqRp5MaNrBlhMxzFOGGcRZmt" +
       "RS3HzjgkCnDl1HAk\nztOkZJk2fOaiVt40CHzjgqWTaM0eJXHi3bM+H1y235" +
       "t4OqB0q6lniJ3WXr/xxeEt258+ocIooOfa\nytGAOjtSezYkv3sI8vnk7gsF" +
       "TJUbY7aND4r0KR27PPDip/gVCAo4h9FDRN7dN9siWlC6f152Ga3m\n5jiMME" +
       "AnrfUevbnspW/fuORHgaYME69Mjpl2AesCJOWU6nGP864AtlZ7Ed7s7F9O7r" +
       "hw5ctra6tY\n52h1Qwo2aooUGvJ63zY1kgGKqm7//J9bfz3V+tA7ftQCeSnc" +
       "jQFPkObLvWfUpdJImZbEXQJx1JVt\nuHgnBwfN1lxYtCE57oZQiF8H/O52wd" +
       "gpGrEYFk2PAhGEq9dzB0l6t58M3nv1/a5L0illfuyuIdIk\n4SrbwlWITNmE" +
       "wPy1FyZPnbl1fG8A8tuyFCw4ClrOtE61EqgsqU9QcYOMQNbP50cWPLOBvStR" +
       "0EEL\nBYfjaZ0AoWNdN2dJJs0lo4Vr2FOSFjgrNA3kB1mT1mEj5Q7LVwQEN6" +
       "GXyNLe088Nv3xVEJAl/bUY\nbPRLS/3yeyFUDXkvcUZEMTRrJL9JmxaAW4ou" +
       "+T27/LUfLtxI9CokqQqxqoGka3VUlZCh6LKEa1bO\nd4KUvrhu5bkjievTij" +
       "176l25xXAKD5z9jgw/HNKaJH0A6lhJXnCNT7TDlcsjeXkkLRkQSdxbDStQ\n" +
       "4gEVoNBwct+2/SeGZGSVu4TCOtFE1cYbG6ZLDR620fp5KWJMFOdqBtHDvx87" +
       "/82ZkB/5U6iNsjFq\nYF0AhO1UnNCkLHm2OPTBrldvf82vS59Uk1AY1l9qJN" +
       "HduIYfHrxSDAffOlvwo7YUWiCfFtjgu7Hu\nCLCn4HHARt3JOLqrbr2+0Kuq" +
       "NlIhmX4vNGqO9aZ/NY4wFtJi3O7J+Hb49bo9Kvc1Ge/zWWKwWSoM\nynbIcg" +
       "PPq0fIuAFrSIFNdbsvdDkFlfvG3R8RTQzy3caiikkMqA190tyYwh+q4m+jWz" +
       "ncxFPIo2ak\n8v4DDhC5MTDXe0XmxfHHfgs9hS/u87sovo+jDm5aG3RSJHoT" +
       "CG6YF4I75KutCoJAcvPwPeu7fgQW\nn6NAhd3JBIEnqjFVDpGsXPg/V65Bz1" +
       "299oSLfY8G8vQzvwSHwlPDY7ReaaQeRZ12vaHSaX111aPf\n7VG591YP0ayc" +
       "l0/+lWwSALydZeBJuXU1oEClxo+JOhv7XFSicv8/2LgX0JyoollKRue2EnKp" +
       "vfyQ\nEhVyacP/RuoFrw1d3r/2Yyv8uXwaVF7ZbfDUzTq6XpvxNeOgZZMslX" +
       "a1qfy3ZJeFzGn2puOSk6rf\n0lSidKgsy0JHfM1YTWqfIq0S+ge1/iYaOg4A" + "AA==");
}
