/**
 * 
 */
package sessionj.extension.noalias;

import java.util.*;

import polyglot.ast.*;
import polyglot.types.*;

/**
 * @author Raymond
 *
 * For MethodDecl. 
 * 
 */
public class SJNoAliasExt_c extends Ext_c implements SJNoAliasExt
{
	private boolean isNoAlias;
	
	public SJNoAliasExt_c(boolean isNoAlias)
	{
		super();
		
		this.isNoAlias = isNoAlias;
	}
	
	/*public SJNoAliasExt_c(Ext ext)
	{
		super(ext);		
	}*/
	
	public boolean isNoAlias()
	{
		return isNoAlias;
	}
}
