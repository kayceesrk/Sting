/**
 * 
 */
package sessionj.types.typeobjects;

import java.util.*;

import polyglot.types.*;
import polyglot.util.Position;

import sessionj.types.*;
import sessionj.types.noalias.*;
import sessionj.types.sesstypes.SJSessionType;

import static sessionj.SJConstants.*;

/**
 * @author Raymond
 * 
 */
public class SJMethodInstance_c extends MethodInstance_c implements
    SJMethodInstance
{
	private boolean noAliasThroughThis = false; // This field and its getter duplicated in SJNoAliasConstructorInstance.
	
	private List<Type> noaliasFormalTypes;
	private Type noAliasReturnType; 
	
	private List<Type> sessionFormalTypes;
	
	/**
	 * 
	 */
	public SJMethodInstance_c()
	{

	}

	/**
	 * @param mi
	 */
	public SJMethodInstance_c(MethodInstance mi)
	{
		super(mi.typeSystem(), mi.position(), mi.container(), mi.flags(), mi.returnType(), mi.name(), mi.formalTypes(), mi.throwTypes());
	}
	
	/*public SJNoAliasMethodInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, Type returnType, java.lang.String name, java.util.List argTypes, java.util.List excTypes)
	{
		super(ts, pos, container, flags, returnType, name, argTypes, excTypes);
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
		return noaliasFormalTypes;
	}
	
	public void setNoAliasFormalTypes(List<Type> noaliasFormalTypes)
	{
		this.noaliasFormalTypes = noaliasFormalTypes;
	}
	
	public List<Type> sessionFormalTypes()
	{
		return sessionFormalTypes;
	}
	
	public void setSessionFormalTypes(List<Type> sessionFormalTypes)
	{
		this.sessionFormalTypes = sessionFormalTypes;
	}	
	
	public Type noAliasReturnType()
	{
		return noAliasReturnType;
	}
	
	public SJMethodInstance noAliasReturnType(Type returnType)
	{
		this.noAliasReturnType = returnType;
		
		return this;
	}	
	
	// Note: calls to overloaded methods are statically determined, even when the methods are overloaded through parameter subtyping. This also applies to methods that take both session and ordinary parameters (can be overloaded through the ordinary parameters). But overloading through only session type parameters not supported - session types erased to SJSocket types.     	
	// Relying on preceding Polyglot base passes to check standard method overriding rules before the SJ passes take over (the SJ method instance objects replace the originals).
	public boolean canOverrideImpl(MethodInstance mi, boolean quiet) throws SemanticException // If we can override mi.
	{	
		//boolean res = super.canOverrideImpl(mi, quiet);
		boolean res = true;
		
		//if (res)
		{
			if (mi instanceof SJMethodInstance)
			{
				SJMethodInstance sjmi = (SJMethodInstance) mi;
				
				Iterator<Type> nafi = sjmi.noAliasFormalTypes().iterator();				
				
				for (Type ours : noAliasFormalTypes()) 
				{
					Type theirs = nafi.next();
					
					if ((ours instanceof SJNoAliasReferenceType) && !(theirs instanceof SJNoAliasReferenceType)) 
					{
						res = false; // Covariant parameters not OK.
						
						break;
					}
					
					if ((theirs instanceof SJNoAliasFinalReferenceType && ((SJNoAliasFinalReferenceType) theirs).isFinal()) && !(ours instanceof SJNoAliasFinalReferenceType && ((SJNoAliasFinalReferenceType) ours).isFinal()))
					{
						res = false;
						
						break;
					}
				}
				
				if (res)
				{
					Type theirrt = sjmi.noAliasReturnType();
					
					if ((theirrt instanceof SJNoAliasReferenceType) && !(noAliasReturnType() instanceof SJNoAliasReferenceType))
					{
						res = false; // Contravariant return not OK.
					}
				}
				
				if (res) // No incompatibility found yet (in noalias aspect).
				{
					// FIXME: check session types as well.
					
					Iterator<Type> sfi = sjmi.sessionFormalTypes().iterator();
					
					for (Type ours : sessionFormalTypes()) 
					{
						Type theirs = sfi.next();
						
						if (ours instanceof SJSessionType)
						{
							if (!(theirs instanceof SJSessionType && theirs.isSubtype(ours))) 
							{
								res = false; 
								
								break;
							}
						}
					}
				}
				
				//FIXME: session method return types not supported yet.
			}
			else // Overriding a standard Java method...   
			{
				for (Type t : noAliasFormalTypes()) 
				{
					if (t instanceof SJNoAliasReferenceType) // ...cannot specify noalias parameters.  
					{
						res = false; 
					}
				}
				
				// FIXME: check session types as well.
				
				for (Type t : sessionFormalTypes)
				{
					if (t instanceof SJSessionType)
					{
						res = false;
						
						break;
					}
				}
			}
		}				
						
		if (quiet)
		{
			return res;
		}
		else
		{
			if (res)
			{
				return true;
			}
			else
			{
				throw new SemanticException("[SJMethodInstance] " + this + " cannot override: " + mi);
			}
		}		
	}
	
	// Adapted from MethodInstance to handle noalias return types (parameter types done implicitly).
	public String toString() 
	{
		String s = designator() + " " + flags.translate();
		
		s += " " + noAliasReturnType() + " " + container() + "." + signature();
		
		if (! throwTypes.isEmpty()) 
		{
			s += " throws " + SJTypeSystem_c.listToString(throwTypes());
		}
	
		return s;
	}
	
	/*public String signature() 
	{
		return name + "(" + SJTypeSystem_c.listToString(noAliasFormalTypes()) + ")";
	}*/	
}
