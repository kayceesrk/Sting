/**
 * 
 */
package sessionj.extension.noalias;

import java.util.List;

import polyglot.ast.*;

import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 * Records the noalias variables that need to be nulled out due to e.g. Assign, VarDecl and ProcedureCall.
 *
 */
public interface SJNoAliasVariablesExt extends Ext
{
	// Aliases the original type objects, and is not updated if the originals are modified/replaced.
	// Maintain List (i.e. not Set) for repeated procedure-call argument checking.
	public List<Field> fields();
	public List<Local> locals();
	public List<ArrayAccess> arrayAccesses();
	
	public void addFields(List<Field> fields);  
	public void addLocals(List<Local> locals);
	public void addArrayAccesses(List<ArrayAccess> arrayAccesses);
}
