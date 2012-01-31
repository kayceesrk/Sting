package sessionj.types.sesstypes;

import java.util.Collection;

import polyglot.types.SemanticException;

public interface SJSetType extends SJSessionType { 
    boolean contains(SJSessionType sessionType);
    boolean containsAllAndOnly(Collection<SJSessionType> types);

    Collection<SJSessionType> getMembers(); 
    SJSetType getFlattenedForm(); // Get a copy of the "flatten" form of this set type.
    
    boolean isSingleton();
    SJSessionType getSingletonMember() throws SemanticException;
}
