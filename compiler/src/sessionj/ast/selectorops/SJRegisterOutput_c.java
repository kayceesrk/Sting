package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJRegisterOutput_c extends SJSelectorOperation_c implements SJRegisterOutput  
{	
	public SJRegisterOutput_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
