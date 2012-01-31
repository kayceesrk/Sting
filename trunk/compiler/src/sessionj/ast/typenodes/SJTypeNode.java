package sessionj.ast.typenodes;

import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.visit.ContextVisitor;
import polyglot.frontend.Job;

import sessionj.types.sesstypes.*;
import sessionj.types.SJTypeSystem;

/**
 * A SJTypeNode is the internal compiler representation of a parsed session
 * type element. The SJTypeNodes are correspond to their SJType equivalents, but are less developed.
 * 
 * FIXME: rename to SJSessionTypeNode (to correspond with the type objects)?
 * 
 */
public interface SJTypeNode extends TypeNode
{
	SJTypeNode child();
	SJTypeNode child(SJTypeNode child);

	//public SJTypeNode leaf();
	
	SJSessionType type();

	String nodeToString(); // Could be protected?
	String treeToString();

    SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) throws SemanticException;
}
