package sessionj.ast.protocoldecls;

import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import sessionj.ast.typenodes.SJTypeNode;

public interface SJLocalProtocolDecl extends LocalDecl, SJProtocolDecl
{
	public SJLocalProtocolDecl init(Expr init);	
}
