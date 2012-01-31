package sessionj.ast.protocoldecls;

import polyglot.ast.*;
import polyglot.types.Flags;
import polyglot.util.Position;
import sessionj.ast.typenodes.SJTypeNode;

public class SJFieldProtocolDecl_c extends FieldDecl_c implements SJFieldProtocolDecl
{
	private SJTypeNode tn;
	
	public SJFieldProtocolDecl_c(Position pos, Flags flags, TypeNode typeNode, Id name, Expr init, SJTypeNode tn)
	{
		super(pos, flags, typeNode, name, init);
		
		this.tn = tn;
	}

	public SJFieldProtocolDecl init(Expr init)
	{
		return (SJFieldProtocolDecl) super.init(init);
	}
	
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJFieldProtocolDecl sessionType(SJTypeNode tn)
	{		
		this.tn = tn;
		
		return this;
	}	
}
