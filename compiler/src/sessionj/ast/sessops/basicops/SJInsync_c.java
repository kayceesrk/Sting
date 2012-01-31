package sessionj.ast.sessops.basicops;

import polyglot.ast.Expr;
import polyglot.ast.BooleanLit_c;
import polyglot.util.Position;
import static sessionj.SJConstants.SJ_SOCKET_INSYNC;
import sessionj.ast.SJNodeFactory;
import sessionj.util.SJCompilerUtils;

import java.util.LinkedList;
import java.util.List;

public class SJInsync_c extends SJBasicOperation_c implements SJInsync  
{	
	public SJInsync_c(SJNodeFactory nf, Position pos, Expr condition, List targets)
	{
        super(pos, nf, SJ_SOCKET_INSYNC,
                SJCompilerUtils.asLinkedList(prepareCondition(pos, condition)), targets);
	}

    private static Expr prepareCondition(Position pos, Expr condition) {
        if (condition == null) return new BooleanLit_c(pos, true);
        else return condition;
    }
}
