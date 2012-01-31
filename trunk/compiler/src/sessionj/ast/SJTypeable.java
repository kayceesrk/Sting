package sessionj.ast;

import polyglot.ast.Node;

/**
 * An SJTypeable is "extended" by an SJTypeableExt, with session typing informaton recorded in an SJTypeObject.
 * 
 * Maybe generalise SJTypeable (or maybe a subclass) that records types for multiple, separate sessions - useful for compound operations (for the non-target sessions) and spawn. SJUniTypebale and SJMultiTypeable (and a subtype of the latter for target and non-target sessions). Or SJBasicTypeable and SJCompoundTypeable.
 */
public interface SJTypeable extends Node
{

}
