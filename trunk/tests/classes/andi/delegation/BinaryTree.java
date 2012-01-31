package andi.delegation;

import java.io.*;

public class BinaryTree implements Serializable {
    public int value = 0;
    public BinaryTree left = null;
    public BinaryTree right = null;
    
    public BinaryTree(BinaryTree left, BinaryTree right, int value) {
        super();
        {
            BinaryTree _sjtmp_left = null;
            _sjtmp_left = left;
            left = null;
            this.left = _sjtmp_left;
        }
        {
            BinaryTree _sjtmp_right = null;
            _sjtmp_right = right;
            right = null;
            this.right = _sjtmp_right;
        }
        this.value = value;
    }
    
    public BinaryTree(int value) {
        super();
        this.left = null;
        this.right = null;
        this.value = value;
    }
    
    public boolean isNode() { return this.left == null; }
    
    public void inc() {
        this.value++;
        if (this.left != null) {
            this.left.inc();
            this.right.inc();
        }
    }
    
    public void print() {
        if (this.isNode()) {
            System.out.print("val:" + this.value);
        } else {
            System.out.print("val:" + this.value + " left:(");
            this.left.print();
            System.out.print(")");
            System.out.print(" right:(");
            this.right.print();
            System.out.print(")");
        }
    }
    
    public static BinaryTree createBinaryTree(int i) {
        BinaryTree left = null;
        BinaryTree right = null;
        BinaryTree res = null;
        if (i == 0) {
            {
                BinaryTree _sjtmp_right = null;
                BinaryTree _sjtmp_left = null;
                _sjtmp_right = right;
                _sjtmp_left = left;
                right = (left = null);
                res = new BinaryTree(_sjtmp_left, _sjtmp_right, i);
            }
        } else {
            left = BinaryTree.createBinaryTree(i - 1);
            right = BinaryTree.createBinaryTree(i - 1);
            {
                BinaryTree _sjtmp_left = null;
                BinaryTree _sjtmp_right = null;
                _sjtmp_left = left;
                _sjtmp_right = right;
                left = (right = null);
                res = new BinaryTree(_sjtmp_left, _sjtmp_right, i);
            }
        }
        {
            BinaryTree _sjtmp_res = null;
            _sjtmp_res = res;
            res = null;
            return _sjtmp_res;
        }
    }
    
    final public static String jlc$CompilerVersion$jl = "2.3.0";
    final public static long jlc$SourceLastModified$jl = 1316107320000L;
    final public static String jlc$ClassType$jl =
      ("H4sIAAAAAAAAALVYXWwURRyf++hdPw7bOwpWWigtRWigd8ZURPugtUIoHLa0" +
       "hUgNKdvdueuUvd11\nd+56JQRBE0GiRuRLicqDJkbDg0LUF6Mmgt/GBBPwBV" +
       "4wSqIYfRFi0Pifmb3bvb2jpA+Q7HRu5j//\n+X/8/r+Z4eRVVGWZKG5hyyK6" +
       "Nhmn0wa2eKuPT2KZWvHh9YOSaWGlT5UsawQmxuTt/5Et70efXudH\nvlEU0/" +
       "RelUjWyISpZ9MTIxPEyptosaGr02lVp7bGMh0PLrkx9d2+9c0BVD+K6ok2TC" +
       "VK5D5dozhP\nR1EkgzPj2LR6FQUroyiqYawMY5NIKtkJgroGG1skrUk0a2Jr" +
       "CFu6mmOCMStrYJPvWRhMooisaxY1\nszLVTYuihuSklJMSWUrURJJYtCeJQi" +
       "mCVcV6Eu1G/iSqSqlSGgTnJwteJLjGxFo2DuK1BMw0U5KM\nC0uCO4imUNTq" +
       "XVH0uGMDCMDScAbTCb24VVCTYADFhEmqpKUTw9QkWhpEq/Qs7ELRgpsqBaFq" +
       "Q5J3\nSGk8RlGTV25QTIFUDQ8LW0LRPK8Y1wQ5W+DJmStbA6HIvwcGry2GjI" +
       "PNCpZVZn8IFi3yLBrCKWxi\nTcZi4fVs/HD/1myLHyEQnucRFjK9Sz/anLzy" +
       "aauQaa4gM8CxOCbfWNWy8FzvzzUBZka1oVuEQaHE\nc57VQXumJ28AuucXNb" +
       "LJeGHys6Evtu55F//mR9X9KCTrajaj9aMarCl9dj8M/STRsBgdSKUsTPtR\n" +
       "UOVDIZ3/hnCkiIpZOKqgb0h0gvfzBkIoDJ8Pvl4k/rHfiKI5jxBNMqdHTIzj" +
       "1iRFyyi2qJWwTDkh\naQpJKFjFaY7yRIlknmm+Y8rnA6davAWmAhrX6aqCzT" +
       "H57cvf7Fqz4bn9Il0MYrZNACa2Q9zZIe7s\ngHw+rnouw6KIVa9pStOsRvJ7" +
       "zy189UvpdYg8RMAiOzF30D8VZC0sundGCulzCrAfehLgY0xu3HNl\nwfEf3z" +
       "nrR4GKNJIsDq7VzYykMiQU6iZmb+edAQB1eGFcae8/Dmw8ff7bi8sdQFPUUV" +
       "Zn5StZnbR7\nQ2/qMlaAhxz1x/5Z9+ehqgc+8KMgFB/QD5UANFDLi7x7lNRL" +
       "T4F7mC+BJKpLlTleSyFAUy6HWRvh\n/XoX4ubaiJvDGjYZZU1MIAjS1ejxgT" +
       "Pb9WdC91z4uO4sD0qBBOtdbDmMqSipqAMRhhwYv/jK4KEj\nV/c9EYAiNgwO" +
       "Cx9FISM7rhI5D0vuLK1C5oHCkPX7qZ6GF7usDzkKakgmk6XSuIqBtSVV1aew" +
       "MkY5\nbUVdFMmZCYIVGQeGA7IcU0GRCIfhywGCK3BIvKnx8NHO1y4wljF4vO" +
       "aDjQFuaYCHpRAbL6UNmiQD\njJGzKe3gord+OX15qFFAR/D+kjLqda8R3M9j" +
       "X2ewWLTNtAOXPrOi7eTuoUvjghNjpbFbo2Uz9534\nCXc+HJErlHgATqc893" +
       "Cpj7WdRW8R9xZxSxayUm908ghEt0NkJNI5vG399v3tPJUiPiDa7SlwTZdY\n" +
       "aUJxP2YXKRCJ6qH/a5uOTrYd3/MyvymEiZABhxK3UubR8+ZT1QeaYvlj3Fu/" +
       "yXPdwt2oNQSmEXdy\nVeEXa7tEELq9KXbNNZUVzl124cyrVDisaasYWZ9AvE" +
       "v1TLHv8bks85XZao/kvSiFLKyckWbXsluM\nw0Jk1997T/1wJOJHfif6rMgs" +
       "O8oVzm+Pip2fbH7j+vf0Eg+8Q2Qi/uWn0BbJxbGrz+eiofdOZPwo\nPIoa+B" +
       "1M0ugWSc0ywhiFW5TVZw8m0ZyS+dIbkTj+e4pE3eKtNte2Xgp1SgP6TJr1qy" +
       "uwJotzjZ38\nGk/yfT6Ddfp5/imqyjEbeZIE2kQ6Hy3Rdzd8tba+2sr6kra+" +
       "oIpT1MFnRXXL4Kuz1dVVVjdQMM8k\n6YkSfXkXcJEDSxttvD+X2mRA9Hjxog" +
       "08zOhq4c0uhpyq9j3+V+RZ6cw2vw3uBEU1VDe6VJzDahHC\nQb5XELR1zQjh" +
       "jfx67IAoMPxQ57KVdb/CSXqTS0JUK1AGvAW0kUKK+e1BmvXtodXjq9eeaK55" +
       "U2CC\nfOXn4BJ4LLv1ly7qKUVhrVlqKA9ac0mym+BrtpPdPDsiKtDMLTloG5" +
       "zOjAgU4fsKQyyJUxQe13UV\nSxpfrLiQgvLlP0ZLDI/B12ob3nqbDCf8hJO9" +
       "VgdzOlH4MmNWJjfaxYUKf2+DyVkoSgNedbwojVka\neQd8q+Hrto3srmQkl2" +
       "7lbbs4s/2s3wFJtvjLurKRszu2dsPbWTaxRLHzaOC7Cwq533DZcquzjaJa\n" +
       "Rwm7UjaV/Y+BeNfK7ee2L//ciH7N79LFt2cYHoCprKq66d3VDxkmThFuT1iQ" +
       "vTDuBbjQel5AYInz\ng1v6vJB9CUDFZFn/oFFgyQbOkuxciotzKe/7Hx11CS" +
       "VGEQAA");
}
