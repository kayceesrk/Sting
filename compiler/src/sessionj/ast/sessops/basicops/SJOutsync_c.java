package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import polyglot.ast.Expr;
import static sessionj.SJConstants.SJ_SOCKET_OUTSYNC;
import sessionj.ast.SJNodeFactory;
import sessionj.util.SJCompilerUtils;

import java.util.List;

public class SJOutsync_c extends SJBasicOperation_c implements SJOutsync  
{	
	public SJOutsync_c(SJNodeFactory nf, Position pos, Expr condition, List targets)
	{
        super(pos, nf, SJ_SOCKET_OUTSYNC,
                SJCompilerUtils.asLinkedList(condition), targets);
	}
}
