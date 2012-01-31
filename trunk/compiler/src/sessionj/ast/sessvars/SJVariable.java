package sessionj.ast.sessvars;

import polyglot.ast.NamedVariable;

public interface SJVariable extends NamedVariable//, SJNamed // SJTypeable component unused.
// Actually, session type information is never built for SJVariables (extension object is not attached).
{
	String sjname(); // This should match the SJ name parsed from the variable declarations.
    boolean isFinal();
}
