package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

abstract public class SJRegister_c extends SJSelectorOperation_c implements SJRegister  
{	
	public SJRegister_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
