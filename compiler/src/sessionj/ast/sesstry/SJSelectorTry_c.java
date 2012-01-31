package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJSelectorTry_c extends SJTry_c implements SJSelectorTry
{
	public SJSelectorTry_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		super(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}

	public SJSelectorTry targets(List targets)
	{
		return (SJSelectorTry) super.targets(targets);
	}
}
