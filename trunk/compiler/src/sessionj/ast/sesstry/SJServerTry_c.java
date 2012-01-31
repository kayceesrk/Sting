package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJServerTry_c extends SJTry_c implements SJServerTry
{
	public SJServerTry_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		super(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}

	public SJServerTry targets(List targets)
	{
		return (SJServerTry) super.targets(targets);
	}
}
