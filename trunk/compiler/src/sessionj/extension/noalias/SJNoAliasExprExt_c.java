/**
 * 
 */
package sessionj.extension.noalias;

import java.util.*;

import polyglot.ast.*;

import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 */
public class SJNoAliasExprExt_c extends SJNoAliasVariablesExt_c implements SJNoAliasExprExt // Chose to extend SJNoAliasVariablesExt_c simply because this inherits more code.
{
	private boolean isNoAlias; // Duplicated from SJNoAliasExt_c.
	private boolean isFinal;
	
	public SJNoAliasExprExt_c(boolean isNoAlias, boolean isFinal)
	{
		super();
		
		this.isNoAlias = isNoAlias;
		this.isFinal = isFinal;
	}	
	
	public boolean isNoAlias()
	{
		return isNoAlias;
	}
	
	public boolean isFinal()
	{
		return isFinal;
	}
}
