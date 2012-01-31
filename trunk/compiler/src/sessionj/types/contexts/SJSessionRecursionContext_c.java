/**
 * 
 */
package sessionj.types.contexts;

import java.util.List;

import sessionj.ast.sessops.compoundops.*;
import sessionj.util.SJLabel;

/**
 * @author Raymond
 *
 */
public class SJSessionRecursionContext_c extends SJSessionLoopContext_c implements
    SJSessionRecursionContext
{
	/**
	 * 
	 */
	public SJSessionRecursionContext_c(SJContextElement ce, SJRecursion r, List<String> targets)
	{
		super(ce, r, targets);
	}
	
	public SJRecursion node()
	{
		return (SJRecursion) super.node();
	}
	
	public SJLabel lab()
	{
		return node().label();
	}
}
