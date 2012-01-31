//<By MQ> Added
package sessionj.ast.protocoldecls;

import polyglot.ast.*;
import polyglot.types.Flags;
import polyglot.util.Position;
import sessionj.ast.typenodes.SJTypeNode;

public class SJFieldGProtocolDecl_c extends FieldDecl_c implements SJFieldGProtocolDecl
{
	private SJTypeNode tn;
	
	public SJFieldGProtocolDecl_c(Position pos, Flags flags, TypeNode typeNode, Id name, Expr init, SJTypeNode tn)
	{
		super(pos, flags, typeNode, name, init);
		
		this.tn = tn;
	}

	public SJFieldGProtocolDecl init(Expr init)
	{
		return (SJFieldGProtocolDecl) super.init(init);
	}
	
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJFieldGProtocolDecl sessionType(SJTypeNode tn)
	{		
		this.tn = tn;
		
		return this;
	}	
}
//</By MQ>