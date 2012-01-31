//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/example/Protocols.sj -d tests/classes/

/**
 *
 */

package pldi.example;

import java.math.*;

import sessionj.runtime.*;

/**
 *
 * @author Raymond
 *
 */
public class Protocols
{
	public static final noalias protocol p_service { sbegin.?(NoAliasLinkedList).!<Integer> }
	public static final noalias protocol p_worker { sbegin.?(NoAliasLinkedList).!<Integer> }
}
