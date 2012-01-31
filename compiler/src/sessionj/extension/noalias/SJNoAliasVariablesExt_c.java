/**
 * 
 */
package sessionj.extension.noalias;

import java.util.*;

import polyglot.ast.*;

import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 * For ConstructorCall and Return.
 * 
 */
public class SJNoAliasVariablesExt_c extends Ext_c implements SJNoAliasVariablesExt
{
	public List<Field> fields = new LinkedList<Field>();
	public List<Local> locals = new LinkedList<Local>();
	public List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
	
	public SJNoAliasVariablesExt_c()
	{
		super();
	}	

	public List<Field> fields()
	{
		return fields;
	}
	
	public List<Local> locals()
	{
		return locals;
	}

	public List<ArrayAccess> arrayAccesses()
	{
		return arrayAccesses;
	}
	
	public void addFields(List<Field> fields)
	{
		this.fields.addAll(fields);
	}
	
	public void addLocals(List<Local> locals)
	{
		this.locals.addAll(locals);
	}
	
	public void addArrayAccesses(List<ArrayAccess> arrayAccesses)
	{
		this.arrayAccesses.addAll(arrayAccesses);
	}		
}
