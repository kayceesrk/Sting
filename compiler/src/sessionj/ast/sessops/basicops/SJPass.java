package sessionj.ast.sessops.basicops;

import polyglot.ast.Expr;
import polyglot.ast.StringLit;

/**
 * 
 * @author Raymond
 *
 * Pass takes a noalias argument; send takes an ordinary argument (and noalias); copy takes only na-final.
 * 
 * So send and pass can be used for all noalias messages and session delegation. Copy can be used for shared-channel (service) passing.
 *
 * SJPass is the super type of both SJSend and SJCopy: noalias messages can be accepted by both (standard contravariance, noalias is a subtype of both ordinary and na-final), but the latter two cannot accept each other's message types. 
 *
 */
public interface SJPass extends SJBasicOperation
{
    Expr encodedSessionType();
    /** The real argument of the call (data or session channel) */
    Expr argument();

    SJPass addEncodedArg(StringLit encoded);
}
