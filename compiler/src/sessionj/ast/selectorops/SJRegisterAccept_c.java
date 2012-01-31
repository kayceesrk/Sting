package sessionj.ast.selectorops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJRegisterAccept_c extends SJSelectorOperation_c implements SJRegisterAccept  
{	
	public SJRegisterAccept_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
