package sessionj.ast.chanops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.sessvars.SJSocketVariable;

abstract public class SJChannelOperation_c extends Call_c implements SJChannelOperation
{	
	public SJChannelOperation_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
