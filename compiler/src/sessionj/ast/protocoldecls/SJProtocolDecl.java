package sessionj.ast.protocoldecls;

import polyglot.ast.VarInit;

import sessionj.ast.*;
import sessionj.ast.typenodes.*;

/**
 * 
 * @author Raymond
 *
 * Maybe make a SJVarDecl base class (SJFormal, etc. would also be one). Analogous to SJVariable.
 * 
 */
public interface SJProtocolDecl extends VarInit, SJNamed
{
	public SJTypeNode sessionType();
	public SJProtocolDecl sessionType(SJTypeNode tn);
}
