package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;

import sessionj.util.SJLabel;

public interface SJRecursion extends For, SJLoopOperation // But does not need a SJSocketOperation.
{
	SJLabel label();
	SJRecursion label(SJLabel lab);
	
	Block body();
}
