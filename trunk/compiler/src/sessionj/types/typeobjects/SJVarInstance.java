/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.VarInstance;

/**
 * @author Raymond
 * 
 */
public interface SJVarInstance extends VarInstance, SJTypeObject
{
	public boolean isNoAlias();
	public boolean isFinal();
}
