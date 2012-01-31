package purdue.multiparty;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Multi2 {
    final private SJProtocol invitation =
      new SJProtocol(
      ("H4sIAAAAAAAAAO1aXWxURRSe3XaBtlCqFBH5Cz8tP8FdCT8RWn5LAWEp1S2o" +
       "oMDtvdPtpXfvXe+d\nLa0/+ENM0ESUKJAQIi8iD+IDJkbffDJNNPHBB4MPPu" +
       "GDJPpgjPFBTJxv7t2Ze5ftQizVmmwfvj0z\nZ+acM2fOnHN206u/kITnkiUe" +
       "9TzTsY8l2XCeekkMfSqzO0NZDyeP6CPftX9is7k34iSWJpNzNNdL\nXY+RB9" +
       "LHtEEtVWCmlepwLIvqjEtqG3LJsopSBcuXTPy/mJCc0PtNy2BkWbq4PSU2pe" +
       "T2VGQ7NC0u\n0QR0eo9xU6DLV3JmcubKk9n3PogTwnfMzDvWcNZyWLDDX7O1" +
       "5bP96Z++WOCvmVNmzT4h9Yh+a93c\ned9u/bGuBiZPyTueiVMz8mC6uMn3SH" +
       "fAaRvK54fy3NWt8FYSvKTylrdkv51zDLPP1HotmjY9dmt6\ny6pPfz7dJFxS" +
       "a/EZRppCnsYanHzlncWp+dnbyCtfH/5jvhAa058jJ0hMGNWspKRNe4AaED91" +
       "eebZ\n3UdPLarhzsgfr8UF8aVLK13qNpo1y93pJKa5WcqPcL9/BEuzs6kMc0" +
       "07i0O0VpAZErnBOj9yufvX\nU7gd2B7PD3mVo+wJqlNzkJZahMtdW2HbXk5r" +
       "We64XK5gm7rGZKReWXvy+rnXUr/HSc0hMoVaNEdt\nxh/B/EORg2n6QI+r6b" +
       "TTX9CWJg05XybEMDJTRYkf0SKQpZ9wuCZ5RHzOEEdtqfigbOP2c2JvcyBj\n" +
       "tpKVL7hFk5MwOXm7yfEdK9rb/1q0HlLyXFw71z+nwgat7ssLret+eD1Oah8j" +
       "9ZZp064CMkSaNBpU\ntzTcdYelecKEJv5m+kyLdmk5Gozrc5T1O4acgdLECA" +
       "96pbOn36UazwwNPpEEh5Fp3GHKGiF+Abd3\nDvuHV8zIwkrsQOukdtM22Sap" +
       "bjq7+0BkpLlkRgiFqKVS4CYQywErGJniDfBYY/xlF9n1ig1YKRlr\nQLQAWl" +
       "kk6uSKg+EVdbZj0A7LsRX/m8pHiWRucZTITOCfOubSUrkgVgOEjWv/VcZ1xW" +
       "CkVnfyw5J1\nlt1d/eOxGBrJK1slBV0EIe5t86jKAe2SsYyFSpfm+YUGN1Is" +
       "W12chub7wsPAxbUsfKsbWSiNlooK\nrC5Kay6ZkWfplOK2sFCmHzR5EeNiul" +
       "2HObpjbecPmr8227M05riMzBuNFVg6iwUzNLpQansExG7A\nHh46FtUGaYdm" +
       "Kf5p7idZjH1rOl3XcXdptsFTTfYApmDJ7HLT8nhdRYGxJhDdgMcZSQiFUtk1" +
       "fqAS\nZR28tNMhJtXMiE5IBRmpIM7IQyVC4GspoSk0Crw0QyzrNLL8VvYNUt" +
       "c1DWXU9yCeAjzNPSRXSv7H\nfL/Uh+sv3nVDJGbqxc4OdFhy600QhwFHiqJR" +
       "/iV/M+9qIqJFGt/mGMPiTYRGxYyslLhUpaw/1REA\nB8udDfCMOpSyDHBUMi" +
       "6VNQkhpUzyR/Jq+sbDjptRRq9kDPNQjBiYcQquTnfwmuc/59Dwnph4LXyJ\n" +
       "CXEBkvdoOKD7XISubSSD4OvmruIuC42CW6xxC/Lu4j0s1A1LERm9nxoFi/J4" +
       "bpR0sH0y3w5p8kX8\nBuIlwAmfu9PR5AuPfR7mNmiM0VyeRVZcVCsAJyc4wx" +
       "qNsTx80uncDz0ObzPyvLybofr+UTh/SIdj\noSn8Pa1IFt2t+2Mp4E0QbwPe" +
       "YWRqwEXAyRshx3k/LnXkNNNO7uXAcwA+ArkJj7ekKpS6QJwFnAOc\nl4xZ3C" +
       "RZL7C/zJpGEBcEg1cvKOQ95dySL1ndFu/idjmWQV3ebt/46sXOPW+c8r8F2a" +
       "o3jPSF/hcJ\n3pPtLVjMXF3AmnmiZQ00J0ZALAQsAixWXSIIvxcCqPaL93N3" +
       "122L2qyGo3VylwEfAlSrVj8aY03U\nqlS51g2gWo+HQRTbG/8b9ITruwCq9R" +
       "FBpJqlMXVRIDoA2wGqidkIYidgV5SxBYTfbwDS5RoRgGwY\nyGkQfsMQYYQ7" +
       "CYDsAPyMvB9wIMKIxUGMc/0RrjMA4sWMS/G7BGIAYI2bjtELLAgX4I1V+bWo" +
       "juOq\nboIQip4HvKCqIQiV218uV+MAr5Yrb4AJUqPGp6oB3lKFDIRfhwBnyh" +
       "UowLuqLIEoV2NGLz4gLpRh\nNEYZ4ljv314o2sZSKECoJP6fpX3A1Wpqr6b2" +
       "e6G8mtqrqf3/nNpXydS+YaypXf3GWva3WUBJar/D\nr7KACZLaqxm8msGrGb" +
       "yawclEzuDrqxm8mqhJNVFXE3U1UZOJl6jXyIwt/tmk8W+87PD5UiYAAA=="));
    
    public void run() throws Exception {
        SJServerSocket ss = null;
        try {
            ss = SJServerSocketImpl.create(invitation, 7102);
            ss.addParticipant("Multi3", "localhost", 7103);
            ss.addParticipant("Multi4", "localhost", 7104);
            SJSocketGroup ps = null;
            try {
                ps = ss.accept("Multi1");
                System.out.println("Multi2: connected to all participants");
                String str4 = (String) SJRuntime.receive("Multi4", ps);
                System.out.println("Multi2 received from Multi4: " + str4);
                String str1 = (String) SJRuntime.receive("Multi1", ps);
                System.out.println("Multi2 received from Multi1: " + str1);
                SJRuntime.pass("Hello, Multi1 from Multi2", "Multi1", ps);
                SJRuntime.pass("Hello, Multi3 from Multi2", "Multi3", ps);
                String str3 = (String) SJRuntime.receive("Multi3", ps);
                System.out.println("Multi2 received from Multi3: " + str3);
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
        Multi2 a = new Multi2();
        a.run();
    }
    
    public Multi2() { super(); }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1320939736000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAAO0ba5AcRbl37/0g98glQC458rhAosmehIQCLhCSIyEXNpfj" +
       "9ghwES9zs313k8zO\njDO9mz0KkYdAhDKKJLwqEn+AlApVJohgoWiVYFRUyl" +
       "CGUMXjBxaPEiwpS0kpoN/XPdPz2L1NJAQD\n7lXtd1/P1/31193fa3a/fuht" +
       "UuXYJOFQx9FMY0uCTVjU4dAc2UJV5iRS6/oV26HpHl1xnEEgDKub\nP9A27m" +
       "25YW2cxIZIq2Gu1DXFGRy3zezY+OC45uRtMtsy9Ykx3WQuxwIe5817b9tvb1" +
       "nXXkGahkiT\nZqSYwjS1xzQYzbMh0pihmRFqOyvTaZoeIi0GpekUtTVF166G" +
       "jqYBEzvamKGwrE2dAeqYeg47tjpZ\ni9p8Tu9hkjSqpuEwO6sy03YYaU5uUX" +
       "JKV5ZpeldSc1h3klSPalRPO18k15J4klSN6soYdJye9FbR\nxTl2rcHn0L1e" +
       "AzHtUUWl3pDKrZqRZuS06Ai54s5LoAMMrclQNm7KqSoNBR6QViGSrhhjXSlm" +
       "a8YY\ndK0yszALIzMmZQqdai1F3aqM0WFGTon26xck6FXHtwWHMDIt2o1zgj" +
       "ObETmzwGltqG58/9b+d2fD\niYPMaarqKH81DOqIDBqgo9SmhkrFwMPZxM7e" +
       "K7Mz44RA52mRzqLPyvmPXZZ842eniT7tRfps4Lo4\nrL539sxZB1b+qa4Cxa" +
       "i1TEdDVQitnJ9qv0vpzlug3dMlRyQmPOLPB3555XXfo3+Ok9peUq2aejZj\n" +
       "9JI6aqR7XLwG8KRmUPF0w+ioQ1kvqdT5o2qTt2E7RjWd4nZUAW4pbJzjeYsQ" +
       "UgOfGHzOI+KvHgEj\ndeuzOtOWJJwtjMxn1GFOl2OrXVbWTmdpVwaJlgJK3C" +
       "X75ZHnlG2xGCxnZtS0dNDDtaaepvaw+uCr\nv7lm9SVf3S4OCpXLlQY2SfBP" +
       "+PwTgj+JxTjjqaiDYo9W2rYygbaRv/7ArHv2K9+CHYeVO9rVlC8s\ntq0SIQ" +
       "xaUtJ19PiG1wuYAnoxrLZd98aMe5/77tNxUlHUfSTlwzWmnVF01ADPXlrd6a" +
       "IUUJzOqPoW\nm/svt65/5OAzLy3wFZmRzgL7KhyJ9jE3uvG2qdI0+B+f/V3/" +
       "XPvXO6rOfTROKsHowO0wBZQFbLgj\nOkfITro9n4NrqUiShtGChdcz2KBtgQ" +
       "UjbETQLJQDzqItIiB3V4dvrP7c8z9peJqv2PNsTQEXmKJM\n2EmLf/6DNqXw" +
       "/KW7++/Y9fYtmyrAMi1LnDkj1VZ2RNfUPAw5OWxaKF4a1eatfd3NOxY7P+JH" +
       "XKdl\nMlmmjOgUXLGi6+Y2mh5m3Be1BPwedzewE40j4LbAAw7rwEis1YrlQD" +
       "2LOIbEKW0771y4+3l0HRbf\nmOloYlxSkucPOmMIFxQQsT0LNbjNXzYY+1ax" +
       "gMaFqavWbd4+l688yG6R28hLhr4tLC1pC2swxIDO\nMBN8ja8ym287dOn6Ky" +
       "7vEt7vzJIs+sCe05yPP35Xr7HiyWkHjThqSbWzRdp8ksQdxsiCpMfS1Txs\n" +
       "Ciy1LiVIXgRYdOQF+BNr1/zj+n3P7mqEiYdIjeas0QxFx9N2+oT1FokOERZX" +
       "P3nZfYd/x17muumb\nC4o/M1/o6TYqAUs+52CupfoHezJxUjNEmnmEVwy2Ud" +
       "GzqLlDEKOdHvdhkpwUoofjrQgu3dIdzIya\namDaqKFWBna7EnsjXitsk/dp" +
       "+rf4+wA/qDjYEFGgtcfMWBA67NkXU1i0wmjaysdiFg47hw/u4HCO\n0L84g6" +
       "lxixmpsWwtp2CihJlITmM8I+KdpzLSLg/RzhpMy1BM4Vy1454ixi1iBShsZ+" +
       "S8pWokUDWY\nCM/7Dy7fZ7CZr/JDqnHzMkyOfBcCAVOHPeQR1wZbK8VVKtyw" +
       "6kbFmHBN6rimp1GyJcBjXklNFMNv\nr0k9ePnYzvvjrjU3WNyNne7bsy+X03" +
       "mZkTHT2qjG/RBY+HtN88989K0dzSIS+L4GrODIDPznp64i\n1z3zhXc7OJuY" +
       "ijx6XFe0SDqGM0rtyCo6phXbkGqm2GOUifgNyyrBI8DiPP2u/Q/0v7Pd25SL" +
       "LPTT\npY5kgKpUy9GoBOiNlpUYth5wSC5BiTNZQ1O5CgoeDy678dCdN3T9HZ" +
       "z/JlJLdZqhBgON6dgUynLB\nyw/akLqsFh3AtBoyguegZ0iRTfDWg//7+Lrm" +
       "l1Q1I124KBy7weWR8nlBbuTJl0D5EoXyxdd8Zvny\n9+eey2MNsLsI5m8vMU" +
       "Cpe/re089+6SbIBXpJPSaNfVm0nSSZgo5GwSyfxzzXf9RiHtnn+5N68abQ\n" +
       "5+dwhFTth9cXf05ImqgCrx0NAkkghZGTYMN8aTj7K0HedvYhz5OROaXI7qzV" +
       "yzVDYxfI6ZrY0Wsd\nI22RJ5wpslIkwwsQ4QYGK651toJiMdfrIbneJyMYlY" +
       "SliAwj2MxCKiZ7DAV71BlmmvbopuHTf196\nKSGfxpcSeuLuTx2DnCrCF5EM" +
       "AgOB+bESDvkERipV05qQpF3s6CID6GKgJY9sq2S0GxFuchOTTo4g\nJwkLWM" +
       "D1K46I/HgintvvAxxnbgk23S3mEViyOp8FfGaUlSu1x60t8kSu5cu+kgSVIK" +
       "fBO2Qgrl4E\nBo2jV2U1fA8DXzcpzZV1ygg2eT6EHeQ03Yh8BcFNoDM6VXK0" +
       "B3JmSd8BGyTTIiHGats27bWKkQYf\nM7YRH5kgwanFHst1bfcYxpoRuRXBbZ" +
       "Bh8AnlZHsZmRWZzP2GRk4zNfxATvA1OQEkLjMiTHCTJYfm\nQMvdnKm82+r0" +
       "GBzHhhy1bS3tC/UCIt9EcAfskOwp6Q+zwKsCnrt3yA0hZannI3sw6ZBD30Tk" +
       "bgT3\neKwxVZD0FfgWHWTN/fcqMz3BjSHQ8lyxPwm87Ek+//KXgGBnsbUhuN" +
       "NflC8ZgnslYU9RkVClfJFE\nSx7Nt4+HHG+GCbslYQJUMSRgyszaKl0DwU7Y" +
       "caD5kYi4N3iIVfwAJO2coEKP2qi6RjrhKl8/bBVs\nWaDlnmIFJNIei/ggC3" +
       "xFJVmk1HGazupo+lMk7g6vgeHITVrE3xB5DMHjgnqxqUgLjz0epDYojNGM\n" +
       "xUI9dvs9EPy0TPhYCPpkhIXBE2uC8xw08d0O8hMtkKB8P+gHpeJ4L4HgKzzU" +
       "UxtVtCUD7qT/gOAA\nw++0ORUNR2oW2QavlXKODLxMJ9YDwG8cERd8q+BV1v" +
       "ZNog+R5xHwoPyCJJwMIsmAh+OL9JmCyIuc\nAOEXJ/TeQpuDmTGmupAh8q8b" +
       "z8pij8t5Au2yqdqPyCYEn0dwlZ+zIiIyMwR+MgjZ5dHl/jxT8JuT\n5ZWvIX" +
       "gdgZ841k9GWBqWSiuWSCLwE6HFiHjJlnjTPeGyQAR+IrYLET91O6acDpEvIb" +
       "gWgZ9SnY/I\n9QhuCBP4PookCMHNxbIjBDKLITsQEVlMiBBMbxDItESEia8j" +
       "+EaIEIsjcpyD4gpE7kOwB8Fxicic\n8wMIvnPc5pg86iPyEIKHj3XyveE59v" +
       "rBHJFHEPwQwaN+iEbEd9Q/LhZ4ETxRLOYiOEECzv8v4WhC\nLYJn/eiKiAiO" +
       "CJ4rFjUR/NGPlYgUC3yTR0REXixCmBImvIzglcKA13MsAQ8RPxj9z8IXgrfL" +
       "Iaoc\noj6KycshqhyiPqmET1OIOlOGqFXHGqL8b+CLfnOPIBKijvCdPYITJE" +
       "SVI1E5EpUjUTkSnWCET2ck\nWlmOROWAQ8oBpxxwygHnBCN8mgLOUhl5uNdc" +
       "JyrCQpWXvFxynltNTBCKamKvFNixAz8qR65O8Gsf\nt1zxTuPNylNXiaK61n" +
       "DN9Gojm1m25xBdeGGjWqRqv46Z1mKd5qj4+RcLjuOiHhSmXVyySnI9rx/z\n" +
       "K2YrUisWnrGo4fU4qZyk7r7FfThAWdY2AmV40Fv5rwvyT4tsSlSellz7pRXj" +
       "2q/ivJJWFN8WXKAJ\nD+oOl9zW22FB+RG2cwGaMA2Az6nwmUHEH/+PxBYErb" +
       "w3KkfssDjbSSrFixK9qm9sT2Xu3Qn+c+fq\nvEot/OUX+ccr3fKBwlroflvL" +
       "aExWvN3ecf9rj7w60CZ0QFwnmldwoyc4Rlwp4qto4NWQc0rNwHs/\n9dk5D1" +
       "078PKIWwkZe5+RypyppXFx8aawVkcb8Zjc2JPh0wWfZe7GLiu2sYWGxGP3PD" +
       "A4h1/2Kr2x\nhfeR+KUUsVn7KjrfiT8xvZPfs6gcURxZLxq6yFV4Tyt0/YrL" +
       "WS9XNQc+ZxxhVa8UVQBMEuMd+RjB\n4u347ElWfr7wI7B+nRpjbJxv6kmiBj" +
       "XeDHoC0iI6K19g6oVaxisJsRTZo4lrDJqZkNfkgFh4SwEn\niAtx+VwfUv0D" +
       "tyjii/EnbVnYGP7tXxTX50seNfKo55ymuUUEXBsLdDIW2JzQk7znw5dg/fcp" +
       "UStw\nL8ipcw9sXvALq+XXQmm8S2w1WHmb1fVgJX8Ar7ZsOqrxs6/hsNHioi" +
       "5npKXgQhUj9X6Db3W36L2C\n35rB3ti6kKvJWfnYfwCmKB7qfzkAAA==");
}
