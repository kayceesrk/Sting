package sessionj.ast.createops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

/**
 * 
 * @author Raymond
 *
 * @deprecated
 *
 */
public class SJSocketCreate_c extends SJCreateOperation_c implements SJSocketCreate
{
	public SJSocketCreate_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
