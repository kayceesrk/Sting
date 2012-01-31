package sessionj.ast.servops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.sessvars.SJSocketVariable;

abstract public class SJServerOperation_c extends Call_c implements SJServerOperation
{	
	public SJServerOperation_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
