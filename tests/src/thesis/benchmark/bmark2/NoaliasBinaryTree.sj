package thesis.benchmark.bmark2;

import java.io.Serializable;

public class NoaliasBinaryTree implements Serializable 
{
	private int senderId;
	private int messageId;
  private noalias NoaliasBinaryTree left;
  private noalias NoaliasBinaryTree right;
  
  public NoaliasBinaryTree(int senderId, int messageId)
  {
  	this.senderId = senderId;
    this.messageId = messageId;
  }
  
  public NoaliasBinaryTree(int senderId, int messageId, noalias NoaliasBinaryTree left, noalias NoaliasBinaryTree right) 
  {
    this(senderId, messageId);
    this.left = left;
    this.right = right;
  }

  public int getSenderId() 
  {
  	return senderId;
  }
  
  public int getMessageId() 
  {
  	return messageId;
  }
  
  public void incrementMessageId()
  {
  	messageId++;
  }
  
  public NoaliasBinaryTree getLeft()
  {
  	return left;
  }
  
  public NoaliasBinaryTree getRight()
  {
  	return right;
  }
  
  private String treeToString()
  {
  	return "(" + ((left == null) ? "L" : left.treeToString()) + ", " + ((right == null) ? "R" : right.treeToString()) + ")"; 
  }
  
  public String toString()
  {
  	return "NoaliasBinaryTree[sid=" + getSenderId() + ", mid=" + getMessageId() + ", tree=" + treeToString() + "]"; 
  }
  
  // Depth 1 is a single root node
  public static noalias NoaliasBinaryTree createDepth(int senderId, int messageId, int depth)
  {    	
  	noalias NoaliasBinaryTree left = null;
  	noalias NoaliasBinaryTree right = null;
  	
  	if (depth > 1)
  	{
  		left = createDepth(senderId, messageId, depth - 1);
  		right = createDepth(senderId, messageId, depth - 1);
  	}
  	  	
  	return new NoaliasBinaryTree(senderId, messageId, left, right);
  } 
}
