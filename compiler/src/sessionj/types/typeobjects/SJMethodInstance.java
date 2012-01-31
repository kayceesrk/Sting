/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.types.*;

/**
 * @author Raymond
 * 
 */
public interface SJMethodInstance extends MethodInstance,
    SJProcedureInstance
{
	public Type noAliasReturnType();
	public SJMethodInstance noAliasReturnType(Type returnType);
	
	// Session method return type not supported yet.
}
