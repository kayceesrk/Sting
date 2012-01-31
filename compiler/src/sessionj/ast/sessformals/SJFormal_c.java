package sessionj.ast.sessformals;

import polyglot.ast.Formal_c;
import polyglot.ast.Id;
import polyglot.ast.NodeFactory;
import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.util.Position;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.SJConstants;

public class SJFormal_c extends Formal_c implements SJFormal 
{
	private SJTypeNode tn; 
	private final Type interfaceType;
    
	public SJFormal_c(NodeFactory nf, Type interfaceType, Position pos, Flags flags, Id name, SJTypeNode tn)
	{
		super(pos, flags, nf.CanonicalTypeNode(pos, interfaceType), name);
	    this.interfaceType = interfaceType;	
		this.tn = tn;
	}
	
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJFormal sessionType(SJTypeNode tn)
	{
		this.tn = tn;
		
		return this;
	}

    public boolean isSession() {
        return interfaceType.equals(SJConstants.SJ_SOCKET_INTERFACE_TYPE);
    }

    public boolean isSharedChannel() {
        return interfaceType.equals(SJConstants.SJ_CHANNEL_TYPE);
    }

    public boolean isServer() {
        return interfaceType.equals(SJConstants.SJ_SERVER_INTERFACE_TYPE);
    }
}
