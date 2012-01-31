//<By MQ> Added
package sessionj.ast.protocoldecls;

import polyglot.ast.*;
import polyglot.types.Flags;
import polyglot.util.Position;
import sessionj.ast.typenodes.SJTypeNode;

public class SJLocalGProtocolDecl_c extends LocalDecl_c implements SJLocalGProtocolDecl
{
	private SJTypeNode tn;
	
	public SJLocalGProtocolDecl_c(Position pos, Flags flags, TypeNode typeNode, Id name, Expr init, SJTypeNode tn)
	{
		super(pos, flags, typeNode, name, init);
		
		this.tn = tn;
	}

	public SJLocalGProtocolDecl init(Expr init)
	{
		return (SJLocalGProtocolDecl) super.init(init);
	}
	
	// Duplicated from SJFieldProtocolDecl_c.
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJLocalGProtocolDecl sessionType(SJTypeNode tn)
	{		
		this.tn = tn;
		
		return this;
	}
}
//</By MQ>