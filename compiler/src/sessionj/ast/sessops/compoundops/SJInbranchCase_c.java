package sessionj.ast.sessops.compoundops;

import java.util.List;

import polyglot.ast.*;
import polyglot.types.Context;
import polyglot.util.Position;

import sessionj.util.SJLabel;

public class SJInbranchCase_c extends Block_c implements SJInbranchCase
{
	private SJLabel lab;

	private boolean isDependentlyTyped;	
	
	public SJInbranchCase_c(Position pos, List statements, SJLabel lab)
	{
		this(pos, statements, lab, false);
	}

	public SJInbranchCase_c(Position pos, List statements, SJLabel lab, boolean isDependentlyTyped)
	{
		super(pos, statements);

		this.lab = lab;
		this.isDependentlyTyped = isDependentlyTyped;
	}
	
	public Context enterScope(Context c)
	{
		return c; // Push block?
	}

	public SJLabel label()
	{
		return lab;
	}
	
	public boolean isDependentlyTyped()
	{
		return isDependentlyTyped;
	}
}
