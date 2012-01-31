//$ bin/sessionjc tests/src/benchmarks/NoAliasBinaryTree.sj -d tests/classes/

package benchmarks;

import java.io.*;

public class NoAliasBinaryTree /*extends BinaryTree*/ implements Serializable
{	
	private int value = 0;
	private noalias NoAliasBinaryTree left = null;
	private noalias NoAliasBinaryTree right = null;

	public NoAliasBinaryTree(noalias NoAliasBinaryTree left, noalias NoAliasBinaryTree right, int value)
	{
		this.left = left;
		this.right = right;
		this.value = value;
	}

	public NoAliasBinaryTree(int value)
	{
		this.value = value;
	}

	public boolean isLeaf()
	{
		return (this.left == null && this.right == null);
	}

	public void inc()
	{
		this.value++;
		
		if (this.left != null)
		{
			this.left.inc();
		}
		
		if (this.right != null)
		{
			this.right.inc();
		}
	}

	public void printNode()
	{
		if (this.isLeaf())
		{
			System.out.print("leaf=" + this.value);
		}
		else
		{
			System.out.print("node=<" + this.value + ">");
		}
	}
	
	public void print()
	{
		if (this.isLeaf())
		{
			System.out.print("leaf=" + this.value);
		} 
		else
		{
			System.out.print("node=<" + this.value + ", left:(");
			this.left.print();
			System.out.print("), right:(");
			this.right.print();
			System.out.print(")>");
		}
	}

	public void println()
	{
		print();
		System.out.println();
	}
	
	static public noalias NoAliasBinaryTree createDepth(int depth)
	{
		noalias NoAliasBinaryTree l = null;
		noalias NoAliasBinaryTree r = null;
		noalias NoAliasBinaryTree res = null;
		
		if (depth == 0)
		{
			res = new NoAliasBinaryTree(0);		
		} 
		else
		{
			l = createDepth(depth - 1);
			r = createDepth(depth - 1);
			
			//res = new NoAliasBinaryTree(l, r, depth);
			res = new NoAliasBinaryTree(l, r, 0);
		}
		
		return res;
	}	
}
