package sessionj.ast.sessops.basicops;

import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.NewArray;
import sessionj.ast.sessops.SJSessionOperation;

import java.util.List;

/**
 * 
 * @author Raymond
 *
 * Basic operations are Calls.
 *
 */
public interface SJBasicOperation extends Call, SJSessionOperation
{
	SJBasicOperation targets(List targets);
    
    List<Expr> realArgs();

    NewArray dummyArray();
}
