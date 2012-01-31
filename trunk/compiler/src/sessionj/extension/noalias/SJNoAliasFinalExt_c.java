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
 * For Local/FieldDecl.
 * 
 */
public class SJNoAliasFinalExt_c extends SJNoAliasExt_c implements SJNoAliasFinalExt
{
	private boolean isFinal;
	
	public SJNoAliasFinalExt_c(boolean isNoAlias, boolean isFinal)
	{
		super(isNoAlias);
		
		this.isFinal = isFinal;
	}	
	
	public boolean isFinal()
	{
		return isFinal;
	}
}
