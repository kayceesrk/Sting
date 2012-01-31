//<By MQ> Added.
package sessionj.ast.protocoldecls;

import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import sessionj.ast.typenodes.SJTypeNode;

public interface SJFieldGProtocolDecl extends FieldDecl, SJGProtocolDecl
{
	SJFieldGProtocolDecl init(Expr init);	
}
//</By MQ>