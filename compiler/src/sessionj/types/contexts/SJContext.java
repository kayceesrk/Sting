/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import polyglot.ast.ClassDecl;
import polyglot.ast.MethodDecl;
import polyglot.types.*;
import polyglot.visit.ContextVisitor;

import sessionj.ast.*;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sesstry.*;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 */
abstract public class SJContext implements SJContextInterface
{
	private ContextVisitor cv; // This relies on the same visitor being used for the whole traversal, i.e. enterCall should return `this'. // Otherwise, if the visitor is replaced (cloned), can assign over the session context and update this field. 
	
	private Stack<SJContextElement> contexts = new Stack<SJContextElement>();
	
	public SJContext(ContextVisitor cv)
	{
		this.cv = cv;
	}
	
	public SJContextElement currentContext()
	{
		return contexts.isEmpty() ? null : contexts.peek();
	}
	
	protected Stack<SJContextElement> contexts()
	{
		return contexts;
	}

	public ContextVisitor getVisitor()
	{
		return cv;
	}
	
	public void setVisitor(ContextVisitor cv)
	{
		this.cv = cv;
	}
	
	protected Context visitorContext()
	{
		return getVisitor().context();
	}
}
