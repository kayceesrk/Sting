/**
 * 
 */
package sessionj.types.noalias;

import polyglot.types.*;

import sessionj.types.*;
import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 * Currently only used for method return types.
 *
 */
public interface SJNoAliasReferenceType extends ReferenceType, SJType
{
	public ReferenceType type();
}
