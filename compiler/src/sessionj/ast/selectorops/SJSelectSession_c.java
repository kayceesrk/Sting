package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJSelectSession_c extends SJSelectorOperation_c implements SJSelectSession  
{	
	public SJSelectSession_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
