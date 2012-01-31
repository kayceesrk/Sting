/**
 * 
 */
package sessionj.types.typeobjects;

import java.util.List;

import polyglot.types.*;
import polyglot.util.Position;

/**
 * @author Raymond
 *
 */
public class SJConstructorInstance_c extends ConstructorInstance_c
    implements SJConstructorInstance
{
	private boolean noAliasThroughThis = false;
	
	private List<Type> noAliasFormalTypes;
	
	private List<Type> sessionFormalTypes;
	
	/**
	 * 
	 */
	public SJConstructorInstance_c()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param 
	 * @param excTypes
	 */
	public SJConstructorInstance_c(ConstructorInstance ci)
	{
		super(ci.typeSystem(), ci.position(), (ClassType) ci.container(), ci.flags(), ci.formalTypes(), ci.throwTypes());
	}

	/*public SJNoAliasConstructorInstance_c(TypeSystem ts, Position pos, ClassType container, Flags flags, java.util.List argTypes, java.util.List excTypes)
	{
		super(ts, pos, container, flags, argTypes, excTypes);
	}*/
	
	public boolean noAliasThroughThis()
	{
		return noAliasThroughThis;
	}

	public void setNoAliasThroughThis(boolean noAliasThroughThis)
	{
		this.noAliasThroughThis = noAliasThroughThis;	
	}

	public List<Type> noAliasFormalTypes()
	{
		return noAliasFormalTypes;
	}
	
	public void setNoAliasFormalTypes(List<Type> noaliasFormalTypes)
	{
		this.noAliasFormalTypes = noaliasFormalTypes;
	}
	
	public List<Type> sessionFormalTypes()
	{
		return sessionFormalTypes;
	}
	
	public void setSessionFormalTypes(List<Type> sessionFormalTypes)
	{
		this.sessionFormalTypes = sessionFormalTypes;
	}		
}
