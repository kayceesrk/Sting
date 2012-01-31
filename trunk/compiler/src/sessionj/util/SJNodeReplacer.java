/**
 * 
 */
package sessionj.util;

import polyglot.ast.*;
import polyglot.visit.NodeVisitor;

/**
 * @deprecated Modifications to the job AST are not visible within the current pass.
 * @author Raymond
 * 
 */
public class SJNodeReplacer extends NodeVisitor
{
	private Node target;
	private Node subs;
	
	/**
	 * 
	 */
	public SJNodeReplacer(Node old, Node n)
	{
		this.target = old;
		this.subs = n;
	}

	public Node leave(Node parent, Node old, Node n, NodeVisitor v)
	{
		if (n == target)
		{
			System.out.println("0: " + n + " " + subs);
			
			return subs;
		}
		
		return n; 
	}
}
