/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import sessionj.util.SJLabel;

/**
 * @author Raymond
 *
 */
public class SJBranchCaseContext_c extends SJContextElement_c implements SJBranchCaseContext
{
	private SJLabel lab;

	private HashSet terminals = new HashSet<String>();
	
	public SJBranchCaseContext_c()
	{
		
	}
	
	public SJBranchCaseContext_c(SJContextElement ce, SJLabel lab)
	{
		super(ce);	
		
		this.lab = lab;
	}
	
	public SJLabel label()
	{		
		return lab;
	}
	
	public void addTerminal(String sjname)
	{
		terminals.add(sjname);
	}
	
	public boolean isTerminal(String sjname)
	{
		return terminals.contains(sjname);
	}
}
