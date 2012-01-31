/**
 * 
 */
package sessionj.extension.noalias;

import java.util.List;

import polyglot.ast.*;

import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 * Extension object for all expressions that can be (but are not necessarily) final?
 *
 */
public interface SJNoAliasFinalExt extends SJNoAliasExt
{
	public boolean isFinal();
}
