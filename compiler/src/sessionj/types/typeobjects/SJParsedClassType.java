/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.ParsedClassType;

/**
 * @author Raymond
 * 
 */
public interface SJParsedClassType extends ParsedClassType, SJTypeObject
{
	// These are ok because this is a static property on the class, i.e. won't be broken by the fact that type objects are shared singletons (related by pointer equality).
	public boolean noAliasThroughThis();
	public void setNoAliasThroughThis(boolean noAliasThroughThis);
}
