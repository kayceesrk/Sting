package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

abstract public class SJSelectorOperation_c extends Call_c implements SJSelectorOperation
{	
	public SJSelectorOperation_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
