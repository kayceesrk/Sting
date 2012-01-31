/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.ast.sessops.compoundops.*;
import sessionj.util.SJLabel;

/**
 * @author Raymond
 *
 */
public interface SJSessionRecursionContext extends SJSessionLoopContext
{
	public SJRecursion node();
	public SJLabel lab(); 
}
