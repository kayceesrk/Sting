package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import static sessionj.SJConstants.SJ_SOCKET_COPY;
import sessionj.ast.SJNodeFactory;

import java.util.List;

public class SJCopy_c extends SJPass_c implements SJCopy
{	
	public SJCopy_c(Position pos, SJNodeFactory nf, List arguments, List targets)
	{
		super(pos, nf, SJ_SOCKET_COPY, arguments, targets);
	}
}
