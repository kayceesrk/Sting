package sessionj.ast.createops;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;

/**
 * 
 * @author Raymond
 *
 */
public class SJSelectorCreate_c extends SJCreateOperation_c implements SJSelectorCreate
{
	public SJSelectorCreate_c(Position pos, Receiver target, Id name, List arguments)
	{
		super(pos, target, name, arguments);
	}
}
