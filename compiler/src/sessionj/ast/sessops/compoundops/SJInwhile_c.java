package sessionj.ast.sessops.compoundops;

import java.util.*;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJInwhile_c extends SJWhile_c implements SJInwhile
{
    //<By MQ> Added args to support inwhile target
    protected List arguments;
    public List arguments()
    {
	return arguments;
    }
    public SJInwhile_c(Position pos, Stmt body, List targets, List arguments)
	{
        // The condition will be replaced in SJCompoundOperationTranslator.
	    super(pos, dummyCond(pos), body, targets);	    
	    this.arguments = arguments;
	}
    //</By MQ>
    
    private static Expr dummyCond(Position pos) {
        return new BooleanLit_c(pos, false);
    }


	public SJInwhile cond(Expr cond)
	{
		return (SJInwhile) super.cond(cond);
	}

	public SJInwhile body(Stmt body)
	{
		return (SJInwhile) super.body(body);
	}
}
