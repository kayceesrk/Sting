package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import static sessionj.SJConstants.SJ_SOCKET_OUTLABEL;
import sessionj.ast.SJNodeFactory;

import java.util.List;

public class SJOutlabel_c extends SJBasicOperation_c implements SJOutlabel  
{	
	public SJOutlabel_c(Position pos, SJNodeFactory nf, List arguments, List targets)
	{
		super(pos, nf, SJ_SOCKET_OUTLABEL, arguments, targets);
	}
}
