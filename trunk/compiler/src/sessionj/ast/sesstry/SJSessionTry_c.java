package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJSessionTry_c extends SJTry_c implements SJSessionTry
{
	public SJSessionTry_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		super(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}

	public SJSessionTry targets(List targets)
	{
		return (SJSessionTry) super.targets(targets);
	}
}
