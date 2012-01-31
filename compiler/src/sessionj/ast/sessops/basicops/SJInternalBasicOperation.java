package sessionj.ast.sessops.basicops;

import sessionj.ast.sessops.SJInternalOperation;

/**
 * 
 * @author Raymond
 *
 * A bit contradictory - SJBasicOperations are SJTypeable, but SJInternalOperations skip type building and checking. The latter property has precedence. We use SJBasicOperation in the sense of invoking the SJ Runtime to do something.
 *
 */
public interface SJInternalBasicOperation extends SJBasicOperation, SJInternalOperation
{

}
