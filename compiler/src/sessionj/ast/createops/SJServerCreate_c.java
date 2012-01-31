package sessionj.ast.createops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

/**
 * 
 * @author Raymond
 *
 */
public class SJServerCreate_c extends SJCreateOperation_c implements SJServerCreate
{
	public SJServerCreate_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
