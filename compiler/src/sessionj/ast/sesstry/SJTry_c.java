package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

abstract public class SJTry_c extends Try_c implements SJTry
{
	private List targets; 

	public SJTry_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		super(pos, tryBlock, catchBlocks, finallyBlock);

		this.targets = targets;
	}

	public List targets()
	{
		return targets;
	}

	public SJTry targets(List targets)
	{
		this.targets = targets;

		return this;
	}
}
