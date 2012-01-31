package sessionj.ast.createops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

abstract public class SJCreateOperation_c extends Call_c implements SJCreateOperation
{
	public SJCreateOperation_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
