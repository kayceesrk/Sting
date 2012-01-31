package sessionj.ast.sessops.basicops;

import polyglot.ast.Expr;
import polyglot.ast.StringLit;
import polyglot.util.Position;
import sessionj.ast.SJNodeFactory;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class SJReceive_c extends SJBasicOperation_c implements SJReceive
{	
	public SJReceive_c(Position pos, SJNodeFactory nf, String name, List<Expr> arguments, List targets)
	{
		super(pos, nf, name, arguments, targets);
	}

    public SJReceive addEncodedArg(StringLit encoded) {
        List l = new ArrayList(arguments);
        l.add(0, encoded);
        return (SJReceive) arguments(l);
    }
}
