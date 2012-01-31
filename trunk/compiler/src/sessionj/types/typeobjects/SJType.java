/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.Type;
import sessionj.types.SJTypeSystem;

/**
 * @author Raymond
 *
 */
public interface SJType extends Type, SJTypeObject
{
	SJTypeSystem typeSystem();
}
