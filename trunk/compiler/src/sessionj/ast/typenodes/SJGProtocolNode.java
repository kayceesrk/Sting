//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;
import sessionj.types.SJTypeSystem;

/**
 * SJProtocolNode is a meta type object that refers to (the session type of) another protocol.
 */
public interface SJGProtocolNode extends SJTypeNode
{
	Receiver target();
	SJGProtocolNode target(Receiver target);
}
//</By MQ>