package sessionj.types.sesstypes;

import polyglot.types.*;
import static sessionj.SJConstants.SJ_STRING_SEPARATOR;
import static sessionj.SJConstants.SJ_VERSION;
import sessionj.types.typeobjects.SJType_c;

abstract public class SJSessionType_c extends SJType_c implements SJSessionType // Doesn't inherit any code from SJTypeObject_c (no new code there anyway*).
{
    protected enum NodeComparison {
        EQUALS {
            boolean apply(SJSessionType us, SJSessionType them) {
                return us.typeEquals(them);
            }},
        SUBTYPE {
            boolean apply(SJSessionType us, SJSessionType them) {
                return us.isSubtype(them);
            }},
        DUALTYPE {
            boolean apply(SJSessionType us, SJSessionType them) {
                return us.isDualtype(them);
            }};
        abstract boolean apply(SJSessionType us, SJSessionType them);
    }
	
	private static final long serialVersionUID = SJ_VERSION;

	private SJSessionType child;   
    // Important: serialization will loop if not transient
    @SuppressWarnings({"InstanceVariableMayNotBeInitialized"})
    private transient SJSessionType canonicalForm;
    
	protected SJSessionType_c(TypeSystem ts)
	{
		super(ts);
    }

	public SJSessionType child()
	{
		return child;
	}

	public SJSessionType child(SJSessionType child) // Immutable.
	{		
		SJSessionType st = nodeClone(); // Don't need full treeCopy (would loop forever anyway).
		
		if (child != null)
		{
            ((SJSessionType_c) st).setChild(child);
		}
		
		return st; 
	}

	public SJSessionType append(SJSessionType st)
	{
		if (st != null) // Should this be removed? 
		{
			SJSessionType copy = copy();
			
			((SJSessionType_c) ((SJSessionType_c) copy).leaf()).setChild(st);
			
			return copy; // child clones the child.
		}

		return this; 
	}
	
	public boolean isSubtypeImpl(Type potentialSupertype) // No need to override if descendsFrom was implemented properly (default implementation uses typeEquals and descendsFrom).
	{
		return potentialSupertype instanceof SJSessionType && treeSubtype((SJSessionType) potentialSupertype); // Equality is already built into node/treeSubtype.
	}
	
	public boolean typeEqualsImpl(Type t) // Unlike regular Polyglot types, pointer equality is not maintained for equal session types.
	{
		return t instanceof SJSessionType && treeEquals((SJSessionType) t);
	}

	public boolean descendsFromImpl(Type t) 
	{
		return isSubtype(t); // FIXME: make strict descendsFrom? (by modifying node/treeSubtype?) // If so, then isSubtypeImpl needs to check for subtyping between each pairs of type elements (not just the equals or descendsFrom over the whole).
	}

	public boolean isDualtype(Type t)
	{
		return t instanceof SJSessionType && treeDualtype((SJSessionType) t);
	}
	
	public SJSessionType subsume(SJSessionType st) throws SemanticException
	{
		if (st instanceof SJDelegatedType)
		{
			return typeSystem().SJDelegatedType(subsume(((SJDelegatedType) st).getDelegatedType()));
		}
		
		return treeSubsume(st);
	}
	
	public boolean isWellFormed()
	{
        // Not well-formed by default: only types starting with either cbegin or sbegin can be well-formed.
        return false;
	}
	
	public boolean treeEquals(SJSessionType tree)
	{
        SJSessionType ours = child;
        SJSessionType theirs = ((SJSessionType_c) tree).child;
		
		return nodeEquals(tree) && (ours == null ? theirs == null : ours.typeEquals(theirs));
	}

	public boolean treeSubtype(SJSessionType potentialSupertype)
	{
        SJSessionType ours = child;
        SJSessionType theirs = ((SJSessionType_c) potentialSupertype).child;
		
		return nodeSubtype(potentialSupertype) && (ours == null ? theirs == null : ours.isSubtype(theirs));
	}

	public boolean treeDualtype(SJSessionType tree)
	{
        SJSessionType ours = child;
        SJSessionType theirs = ((SJSessionType_c) tree).child;	
		
		return nodeDualtype(tree) && (ours == null ? theirs == null : ours.isDualtype(theirs));
	}

	public SJSessionType treeSubsume(SJSessionType tree) throws SemanticException
	{
        SJSessionType ours = child;
        SJSessionType theirs = ((SJSessionType_c) tree).child;

		if (ours == null)
		{
			if (theirs != null)
			{
				throw new SemanticException("[SJSessionType_c] Not subsumable: " + this + ", " + tree);
			}

			return nodeSubsume(tree);
		}
        if (theirs == null)
        {
            throw new SemanticException("[SJSessionType_c] Not subsumable: " + this + ", " + tree);
        }

        return nodeSubsume(tree).child(ours.subsume(theirs)); // nodeSubsume returns a copy.
    }

	public boolean treeWellFormed()
	{
		if (!nodeWellFormed())
		{
			return false;
		}

        SJSessionType st = child;
		
		return st == null || st.treeWellFormed();
	}

	public boolean nodeEquals(SJSessionType st)
	{
		SJSessionType_c us = (SJSessionType_c) getCanonicalForm();
		SJSessionType_c them = (SJSessionType_c) st.getCanonicalForm();

		return us.eligibleForEquals(them) && us.compareNode(NodeComparison.EQUALS, them);
	}
	
	public boolean nodeSubtype(SJSessionType potentialSupertype)
	{
    //SJSessionType candidate = potentialSupertype.supertypeCandidate(this);
		//return eligibleForSubtype(candidate) && compareNode(NodeComparison.SUBTYPE, candidate);
		
		SJSessionType_c us = (SJSessionType_c) getCanonicalForm();
		SJSessionType_c them = (SJSessionType_c) potentialSupertype.getCanonicalForm();

    SJSessionType candidate = them.supertypeCandidate(us);
		return us.eligibleForSubtype(candidate) && us.compareNode(NodeComparison.SUBTYPE, candidate);
	}
	
	public boolean nodeDualtype(SJSessionType st)
    // Should check message SVUID values?
	{
		SJSessionType_c us = (SJSessionType_c) getCanonicalForm();
		SJSessionType_c them = (SJSessionType_c) st.getCanonicalForm();
		
		return us.eligibleForDualtype(them) && us.compareNode(NodeComparison.DUALTYPE, them);
	}
	
	abstract protected boolean eligibleForEquals(SJSessionType st);
	abstract protected boolean eligibleForSubtype(SJSessionType st); 
	abstract protected boolean eligibleForDualtype(SJSessionType st);
	
	abstract protected boolean compareNode(NodeComparison o, SJSessionType st);

    public boolean startsWithOutput() {
        return false;
    }

    public SJSessionType treeClone()
	{
        SJSessionType st = child;
		
		return st == null ? nodeClone() : nodeClone().child(st.treeClone());
	}
	
	public SJSessionType clone()
	{
		return copy();
	}
	
	public SJSessionType copy()
	{
		return treeClone();
	}
	
	public String toString()
	{
		return treeToString();
	}

	public String treeToString()
	{
        SJSessionType st = child; 
		
		return nodeToString() + (st == null ? "" : SJ_STRING_SEPARATOR + st.treeToString());
	}

	public String translate(Resolver c)
    // FIXME: hacked. Should just call the super method and wrap the appropriate session type constructor symbols around it.
	{
		return null;
	}

	public boolean equalsImpl(TypeObject to) // Used instead of standard Object equals.
	{
		if (to instanceof SJSessionType)
		{
			return typeEquals((Type) to);
		}
		else
		{
			return false;
		}
	}

    protected void setChild(SJSessionType st)
	{
		this.child = st;
	}	
	
	private SJSessionType leaf()
	{
		if (child == null)
		{
			return this;
		}
		else
		{
			return ((SJSessionType_c) child).leaf(); // No defensive copy, so private.
		}
	}
	
	/*public int hashCode()
	{
		return toString().hashCode(); // Is this an acceptable design?
	}*/
	
	public SJSessionType getLeaf()
	{
		return leaf();
	}

    public boolean startsWith(Class<? extends SJSessionType> aClass) {
        return aClass.isInstance(this);
    }

    public SJSessionType nodeDual() throws SemanticException {
        throw new SemanticException("[SJSessionType] This session type: "
            + this + " does not admit a dual");
    }

    /**
     * By default, no change for the isSubtype comparison.
     * @param potentialSubtype
     * @return
     */
    public SJSessionType supertypeCandidate(SJSessionType potentialSubtype) {
        return this;
    }
    
  final public SJSessionType getCanonicalForm()  
  {
      if (canonicalForm == null) {
          canonicalForm = treeCanonicalForm();
      }
      return canonicalForm;
  }
  
  public SJSessionType nodeCanonicalForm() // FIXME: still need to override in most types, e.g. branch types. Currently only overridden by SJSetType_c and SJMessageCommunication_c.
  {
  	return this;
  }
    
	final public SJSessionType treeCanonicalForm()
	{
		SJSessionType n = nodeCanonicalForm();
  	SJSessionType c = child();		
		
  	return c == null ?  n : n.child(c.getCanonicalForm());
	}
	
  public boolean canImplement(SJSessionType st)
  {
  	return treeCanImplement(st);
  }
  
  public boolean nodeCanImplement(SJSessionType st) // TODO: should be overridden by all concrete classes.
  {
    throw new RuntimeException("[SJSessionType_c] nodeCanImplemented on " + this + " not yet suppported: " + st);
  }
  
  public boolean treeCanImplement(SJSessionType st)
  {
		SJSessionType ours = child(); // This basic structure (check if child is null, etc.) is used by a bunch of routines.
		SJSessionType theirs = st.child();
		
		return nodeCanImplement(st) && (ours == null ? theirs == null : ours.canImplement(theirs));
  }
}
