//$ bin/sessionjc tests/src/benchmarks/BinaryTree.sj -d tests/classes/

package benchmarks;

import java.io.*;

public class BinaryTree implements Serializable
{
	private int value = 0;
	private BinaryTree left = null;
	private BinaryTree right = null;

	public BinaryTree(BinaryTree left, BinaryTree right, int value)
	{
		this.left = left;
		this.right = right;
		this.value = value;
	}

	public BinaryTree(int value)
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
	
	static public BinaryTree createDepth(int depth)
	{
		BinaryTree left = null;
		BinaryTree right = null;
		BinaryTree res = null;
		
		if (depth == 0)
		{
			res = new BinaryTree(0);		
		} 
		else
		{
			left = createDepth(depth - 1);
			right = createDepth(depth - 1);
			
			//res = new BinaryTree(left, right, depth);
			res = new BinaryTree(left, right, 0);
		}
		
		return res;
	}
}
