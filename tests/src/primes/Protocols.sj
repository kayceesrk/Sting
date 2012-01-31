/**
 * 
 */

package primes;

import java.math.*;

import sessionj.runtime.*;

/**
 * 
 * @author Raymond
 *
 */
public class Protocols
{	 
	public static final noalias protocol p_service { sbegin.?(Integer).!<Integer[]> }
	public static final noalias protocol p_worker { sbegin.?(Integer).?(Integer).?(Integer).!<Integer[]> }
}
