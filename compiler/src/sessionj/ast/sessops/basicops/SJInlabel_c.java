package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import static sessionj.SJConstants.SJ_SOCKET_INLABEL;
import sessionj.ast.SJNodeFactory;

import java.util.List;

public class SJInlabel_c extends SJBasicOperation_c implements SJInlabel  
{	
	public SJInlabel_c(Position pos, SJNodeFactory nf, List arguments, List targets)
	{
		super(pos, nf, SJ_SOCKET_INLABEL, arguments, targets);
	}
}
