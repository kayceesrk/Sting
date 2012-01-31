package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.Type;

/**
 * 
 * @author Raymond
 *
 */
public interface SJMessageCommunicationType extends SJSessionType
{
	public Type messageType();
	public SJSessionType messageType(Type messageType) throws SemanticException;
}
