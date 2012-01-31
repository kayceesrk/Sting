package sessionj.extension.sesstypes;

import polyglot.ast.Ext_c;

import sessionj.types.sesstypes.*;

public class SJTypeableExt_c extends Ext_c implements SJTypeableExt
{
	private SJSessionType st;

	public SJTypeableExt_c(SJSessionType st)
	{
		this.st = st; // Should clone? // No? SJSessionType is immutable.
	}

	public SJSessionType sessionType()
	{
		return st;
	}
}
