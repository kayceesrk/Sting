package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

abstract public class SJSelect_c extends SJSelectorOperation_c implements SJSelect  
{	
	public SJSelect_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
