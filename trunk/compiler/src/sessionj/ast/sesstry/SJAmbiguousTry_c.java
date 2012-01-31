package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

public class SJAmbiguousTry_c extends SJTry_c implements SJAmbiguousTry
{
	public SJAmbiguousTry_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock, List targets)
	{
		super(pos, tryBlock, catchBlocks, finallyBlock, targets);
	}
}
