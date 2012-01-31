//bin/sessionjc tests/src/benchmarks/benchmark1/BinaryTree.sj -d tests/classes/

package benchmark1;

import java.io.*;

public class BinaryTree implements Serializable{

    public int value = 0 ;
    public BinaryTree left = null;
    public BinaryTree right = null;

    public BinaryTree (BinaryTree left, 
		       BinaryTree right,
		       int value) {
		this.left = left;
		this.right = right;
		this.value = value;
    }

    public BinaryTree (int value) {
		this.left = null;
		this.right = null;
		this.value = value;
    }

    public boolean isNode () {
    	return (this.left == null);
    }

    public void inc () {
		this.value++;
		if (this.left != null) {
		    this.left.inc();
		    this.right.inc();
		}
    }

    public void print () {
		if (this.isNode()) {
		    System.out.print("val:"+this.value);
		} else {
		    System.out.print("val:"+this.value+" left:(");
		    this.left.print();
		    System.out.print(")");
		    System.out.print(" right:(");
		    this.right.print();
		    System.out.print(")");
		}
    }
    
    static public BinaryTree createBinaryTree(int i) {
    	BinaryTree left = null;
    	BinaryTree right = null;	
        BinaryTree res = null;
    	if (i==0) {
    	    res = new BinaryTree(left, right, i);
            } else {
                left = createBinaryTree(i-1);
                right = createBinaryTree(i-1);
                res = new BinaryTree(left, right, i);
            }
            return res;
        }
}    
	