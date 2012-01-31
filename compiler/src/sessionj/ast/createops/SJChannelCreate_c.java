package sessionj.ast.createops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJChannelCreate_c extends SJCreateOperation_c implements SJChannelCreate
{
	public SJChannelCreate_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
