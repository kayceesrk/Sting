package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.Type;
import sessionj.types.typeobjects.SJType;

/**
 * A SJSessionType holds the information of a session type element.
 */
public interface SJSessionType extends SJType 
{	
	SJSessionType child();
	SJSessionType child(SJSessionType child);

	SJSessionType append(SJSessionType st);

	// Take Type argument to safely override behaviour for session types. 
	//public boolean isSubtype(Type t); // Reuse inherited behaviour (check typeEquals and descendsFrom), no need to override. 
    boolean isDualtype(Type t);
	SJSessionType subsume(SJSessionType t) throws SemanticException;
	boolean isWellFormed();

	boolean treeWellFormed();

	boolean nodeSubtype(SJSessionType st); // this is a subtype of st. // Should check message SVUID values?
	SJSessionType nodeSubsume(SJSessionType st) throws SemanticException;
	boolean nodeWellFormed();

	SJSessionType treeClone();
	SJSessionType nodeClone();

	SJSessionType clone();
	SJSessionType copy(); // Does a treeClone.


    // TODO: this is duplicated between typenode and type classes, find a way to sort it out
    String treeToString();
	String nodeToString();
	
	SJSessionType getLeaf(); // Doesn't return a defensive copy.

    // A first step to remove the instanceof checks in SJSessionTypeChecker and friends.
    boolean startsWith(Class<? extends SJSessionType> aClass);
    // TODO: void checkStartsWith throws SemanticException, to factor out
    // all the scattered throw SemanticException

    SJSessionType nodeDual() throws SemanticException;

    /**
     * Gives a chance to the parameter of the isSubtype call to present itself as an
     * alternative type for the subtype comparison. Introduced for session set types
     */
    SJSessionType supertypeCandidate(SJSessionType potentialSubtype);
    
    SJSessionType getCanonicalForm(); // Basically for flattening set types and simplifying singletons.
    SJSessionType treeCanonicalForm(); // Maybe these shouldn't be public.
    SJSessionType nodeCanonicalForm();     
    
    // TODO: refactor type checking routines to use these.
    boolean canImplement(SJSessionType st); // Separate to subtyping because our subtyping is not based on subsumption (it is based on I/O safety). But this routine should be generally implemented in terms of subtyping.
    boolean treeCanImplement(SJSessionType st);
    boolean nodeCanImplement(SJSessionType st);

    /* Output type nodes correspond to non-blocking actions */
    boolean startsWithOutput();
}
