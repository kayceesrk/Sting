package sessionj.ast.protocoldecls;

import polyglot.ast.*;
import polyglot.types.Flags;
import polyglot.util.Position;
import sessionj.ast.typenodes.SJTypeNode;

public class SJLocalProtocolDecl_c extends LocalDecl_c implements SJLocalProtocolDecl
{
	private SJTypeNode tn;
	
	public SJLocalProtocolDecl_c(Position pos, Flags flags, TypeNode typeNode, Id name, Expr init, SJTypeNode tn)
	{
		super(pos, flags, typeNode, name, init);
		
		this.tn = tn;
	}

	public SJLocalProtocolDecl init(Expr init)
	{
		return (SJLocalProtocolDecl) super.init(init);
	}
	
	// Duplicated from SJFieldProtocolDecl_c.
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJLocalProtocolDecl sessionType(SJTypeNode tn)
	{		
		this.tn = tn;
		
		return this;
	}
}
