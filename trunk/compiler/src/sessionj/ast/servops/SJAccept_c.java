package sessionj.ast.servops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;
import sessionj.ast.sessops.basicops.SJBasicOperation;

public class SJAccept_c extends SJServerOperation_c implements SJAccept  
{	
	public SJAccept_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
