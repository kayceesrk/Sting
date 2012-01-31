package sessionj.ast.sessops.basicops;

import polyglot.ast.*;
import polyglot.util.Position;
import static sessionj.SJConstants.SJ_RUNTIME_TYPE;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessvars.SJVariable;

import java.util.List;
import java.util.Collections;

/**
 * A call to a method of the SJ runtime.
 * The calls always take one to many SJSockets as their last arguments (varargs), but these
 * cannot be inserted straight away in the argument list because they need to be resolved from
 * Receivers to Exprs. We put in dummy null expressions to make the early Polyglot typechecking
 * work.
 * The visitor {@link sessionj.visit.SJSessionOperationParser} does the later replacing of nulls by SJSockets.
 */
abstract public class SJBasicOperation_c extends Call_c implements SJBasicOperation
{	
	private List targets; // Could override the relevant methods to perform type building and type checking (disambiguation not necessary?) via the base passes, but currently it is done manually in the "session operation parsing" pass.
	
	public SJBasicOperation_c(Position pos, SJNodeFactory nf, String name, List<Expr> arguments, List<Receiver> targets)
	{
		super(pos, nf.CanonicalTypeNode(pos, SJ_RUNTIME_TYPE),
                nf.Id(pos, name),
                addDummyTargets(nf, pos, arguments, targets.size())
        );
		
		this.targets = targets;
	}

    private static List<Expr> addDummyTargets(SJNodeFactory nf, Position pos, List<Expr> arguments, int tgtSize) {
        // Polyglot 2.4 does not support Java 5 varargs, so we need to
        // use NewArrays for the varargs parameters for the time being.
        arguments.add(nf.makeSocketsArray(pos,tgtSize));
        // Cannot add actual targets (sockets) to an ArrayInit until after disambiguation,
        // when the targets are resolved from Receivers to Exprs.
        return arguments;
    }

    public List targets()
	{
		return targets; 
	}
	
	public SJBasicOperation targets(List targets)
	{
		this.targets = targets;
		
		return this;
	}

    public List<Receiver> ambiguousTargets() {
        return Collections.unmodifiableList(targets);
    }

    public List<SJVariable> resolvedTargets() {
        return Collections.unmodifiableList(targets);
    }

    public SJSessionOperation resolvedTargets(List<SJVariable> resolved) {
        return targets(resolved);
    }

    public List<Expr> realArgs() {
        return arguments().subList(0, arguments().size()-1);
    }

    public NewArray dummyArray() {
        return (NewArray) arguments().get(arguments().size()-1);
    }

}
