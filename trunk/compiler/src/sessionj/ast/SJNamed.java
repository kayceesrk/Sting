package sessionj.ast;

/**
 * An SJNamed is an SJTypeable entity which has a session type name, i.e. a name required for session type checking purposes.
 */
public interface SJNamed extends SJTypeable // Separate name component from typeable? And make a NamedTypeable? (Only single inheritance is possible though.)
{
	// AST information (the name) recorded in extension object.
}
