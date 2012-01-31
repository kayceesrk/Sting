package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import static sessionj.SJConstants.SJ_SOCKET_RECURSE;
import sessionj.ast.SJNodeFactory;
import sessionj.util.SJLabel;

import java.util.List;

public class SJRecurse_c extends SJBasicOperation_c implements SJRecurse 
{	
	private SJLabel lab;
	
	public SJRecurse_c(Position pos, SJNodeFactory nf, List arguments, List targets, SJLabel lab)
	{
		super(pos, nf, SJ_SOCKET_RECURSE, arguments, targets);
		
		this.lab = lab;
	}
	
	public SJLabel label()
	{
		return lab;
	}
}
