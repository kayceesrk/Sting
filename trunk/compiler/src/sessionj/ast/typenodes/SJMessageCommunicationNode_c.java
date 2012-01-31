package sessionj.ast.typenodes;

import polyglot.ast.TypeNode;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJMessageCommunicationType;
import sessionj.util.SJCompilerUtils;

abstract public class SJMessageCommunicationNode_c extends SJTypeNode_c implements SJMessageCommunicationNode
{
	private TypeNode messageType;

	protected SJMessageCommunicationNode_c(Position pos, TypeNode messageType)
	{
		super(pos);

		this.messageType = messageType;
	}

	public TypeNode messageType()
	{
		return messageType;
	}

	protected void setMessageType(TypeNode messageType)
	{
		this.messageType = messageType; // Unlike polyglot type nodes, no defensive copy (objects are mutable).
	}

    protected abstract SJMessageCommunicationType createType(SJTypeSystem ts, Type messageType) throws SemanticException;

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) throws SemanticException {
        TypeNode messageTypeNode = messageType();

        if (messageTypeNode instanceof SJTypeNode)
        {
            messageTypeNode = SJCompilerUtils.disambiguateSJTypeNode(cv, (SJTypeNode) messageTypeNode);
        }
        else
        {
            messageTypeNode = (TypeNode) SJCompilerUtils.buildAndCheckTypes(cv, messageTypeNode);
        }
        setMessageType(messageTypeNode);
        return type(createType(sjts, messageTypeNode.type()));
    }
}
