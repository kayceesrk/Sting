/**
 * 
 */
package sessionj.types.noalias;

import java.util.*;

import polyglot.types.*;
import polyglot.util.*;

import sessionj.types.*;

import static sessionj.SJConstants.*;

/**
 * 
 * @author Raymond
 * 
 */
public class SJNoAliasReferenceType_c extends ReferenceType_c implements
    SJNoAliasReferenceType
{
	protected transient SJTypeSystem sjts; 
	
	protected ReferenceType rt;

	public SJNoAliasReferenceType_c()
	{

	}
	
	public SJNoAliasReferenceType_c(ReferenceType rt)
	{
		this.rt = rt;
		this.sjts = (SJTypeSystem) rt.typeSystem();
		this.ts = this.sjts; // Some of the inherited methods refer to this field.
	}

	public SJTypeSystem typeSystem() 
	{
		return (SJTypeSystem) super.typeSystem();
	}
	
	public ReferenceType type()
	{
		return rt;		
	}
	
	// Following are wrapper methods customised to support noalias.
	public boolean descendsFromImpl(Type t)
	{
		return (t instanceof SJNoAliasReferenceType)
		    && type().descendsFrom(((SJNoAliasReferenceType) t).type());
	}

	public boolean typeEqualsImpl(Type t)
	{
		return (t instanceof SJNoAliasReferenceType)
		    && type().typeEquals(((SJNoAliasReferenceType) t).type());
	}

	public Type superType()
	{
		return sjts.SJNoAliasReferenceType((ReferenceType) type().superType());
	}

	public SJNoAliasReferenceType copy()
	{
		return sjts.SJNoAliasReferenceType((ReferenceType) type().copy());
	}
	
	public boolean equalsImpl(TypeObject t)
	{
		return (t instanceof SJNoAliasReferenceType) && typeEquals((SJNoAliasReferenceType) t);
	}	
	
	public String toString()
	{
		return SJ_KEYWORD_NOALIAS + " " + type().toString();
	}	
	
	// After here, just plain wrapper methods. Methods from the superclasses that are not overridden already have suitable default behaviour. 
	public FieldInstance fieldNamed(java.lang.String name)
	{
		return type().fieldNamed(name);
	}

	public List fields()
	{
		return type().fields();
	}

	public boolean hasMethodImpl(MethodInstance mi)
	{
		return type().hasMethodImpl(mi);
	}

	public List interfaces()
	{
		return type().interfaces();
	}

	public List members()
	{
		return type().members();
	}

	public List methods()
	{
		return type().methods();
	}

	public List methods(java.lang.String name, java.util.List argTypes)
	{
		return type().methods(name, argTypes);
	}

	public List methodsNamed(java.lang.String name)
	{
		return type().methodsNamed(name);
	}

	public ArrayType arrayOf()
	{
		throw new RuntimeException("Not done yet.");
	}

	public ArrayType arrayOf(int dims)
	{
		throw new RuntimeException("Not done yet.");
	}

	public boolean isArray()
	{
		return type().isArray();
	}

	public boolean isCanonical()
	{
		return type().isCanonical();
	}

	public boolean isCastValidImpl(Type toType)
	{
		return type().isCastValidImpl(toType);
	}

	public boolean isClass()
	{
		return type().isClass();
	}

	public boolean isComparable(Type t)
	{
		return type().isComparable(t);
	}

	public boolean isImplicitCastValidImpl(Type toType)
	{
		return type().isImplicitCastValidImpl(toType);
	}

	public boolean isNull()
	{
		return type().isNull();
	}

	public boolean isSubtypeImpl(Type t) // Uses descendsFrom and typeEquals.
	{
		return type().isSubtypeImpl(t);
	}

	public boolean isThrowable()
	{
		return type().isThrowable();
	}
	
	public boolean isUncheckedException()
	{
		return type().isUncheckedException();
	}

	public void print(CodeWriter w)
	{
		type().print(w);
	}

	public String translate(Resolver c)
	{
		return type().translate(c);
	}

	public Position position()
	{
		return type().position();
	}
}
