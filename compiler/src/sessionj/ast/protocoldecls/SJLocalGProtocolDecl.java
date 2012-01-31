//<By MQ> Added
package sessionj.ast.protocoldecls;

import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import sessionj.ast.typenodes.SJTypeNode;

public interface SJLocalGProtocolDecl extends LocalDecl, SJGProtocolDecl
{
	public SJLocalGProtocolDecl init(Expr init);	
}
//</By MQ>