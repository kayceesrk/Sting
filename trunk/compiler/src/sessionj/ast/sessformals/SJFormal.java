package sessionj.ast.sessformals;

import polyglot.ast.Formal;

import sessionj.ast.typenodes.SJTypeNode;

/**
 * 
 * @author Raymond
 *
 * Maybe should make a session equivalent of VarDecl, and unify with SJVariable.
 *
 */
public interface SJFormal extends Formal//, SJNamed // Like SJVariables, session information is not recorded (no extension object attached).
{
	SJTypeNode sessionType(); // Based on SJProtocolDecl. Make a common base class (SJVarDecl)? Like how base Polyglot Formals are VarDecls.
	SJFormal sessionType(SJTypeNode tn);
    boolean isSession();
    boolean isSharedChannel();
    boolean isServer();
}
