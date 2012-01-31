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
 */
public interface SJNoAliasFinalReferenceType extends SJNoAliasReferenceType
{
	public ReferenceType type();
	public boolean isFinal();
}
