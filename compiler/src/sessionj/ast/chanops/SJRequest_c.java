package sessionj.ast.chanops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;
import sessionj.ast.sessops.basicops.SJBasicOperation;

public class SJRequest_c extends SJChannelOperation_c implements SJRequest  
{	
	public SJRequest_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
