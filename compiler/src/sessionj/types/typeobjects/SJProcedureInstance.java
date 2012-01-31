/**
 * 
 */
package sessionj.types.typeobjects;

import java.util.*;

import polyglot.types.*;

/**
 * @author Raymond
 * 
 */
public interface SJProcedureInstance extends ProcedureInstance,
    SJMemberInstance
{
	// In common with SJNoAliasParsedClassType.
	public boolean noAliasThroughThis(); 
	public void setNoAliasThroughThis(boolean noAliasThroughThis);
	
	public List<Type> noAliasFormalTypes();
	public void setNoAliasFormalTypes(List<Type> noaliasFormalTypes); // Non-defensive setter.
	
	public List<Type> sessionFormalTypes();
	public void setSessionFormalTypes(List<Type> sessionFormalTypes);
}
