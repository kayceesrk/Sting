/**
 * 
 */
package sessionj.extension.noalias;

import java.util.*;

import polyglot.ast.Ext;
import polyglot.types.*;

/**
 * @author Raymond
 * 
 * All expressions plus procedure and variable declarations have a SJNoAliasExt as the first extension object.
 *
 */
public interface SJNoAliasExt extends Ext
{	
	public boolean isNoAlias();
}
