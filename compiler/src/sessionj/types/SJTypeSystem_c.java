package sessionj.types;

import polyglot.frontend.Source;
import polyglot.types.*;
import sessionj.types.noalias.*;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import sessionj.util.SJLabel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import polyglot.ast.Id; //<By MQ>

public class SJTypeSystem_c extends TypeSystem_c implements SJTypeSystem 
{
	public SJCBeginType SJCBeginType()
	{
		return new SJCBeginType_c(this);
	}
	
        public SJSBeginType SJSBeginType(String target) //<By MQ> Added target
        {
	        return new SJSBeginType_c(this, target); //<By MQ>
	}

        public SJSendType SJSendType(String target, Type messageType) throws SemanticException  //<By MQ>
	{
	        return new SJSendType_c(this, target, messageType); //<By MQ> MQTODO: is it appropriate to place null for ID?
	}

        public SJReceiveType SJReceiveType(String target, Type messageType) throws SemanticException //<By MQ>
	{
	        return new SJReceiveType_c(this, target, messageType); //<By MQ>
	}

	public SJOutbranchType SJOutbranchType()
	{
		return new SJOutbranchType_c(this);
	}

	public SJOutbranchType SJOutbranchType(boolean isDependentlyTyped)
	{
		return new SJOutbranchType_c(this, isDependentlyTyped);
	}

	@Override
	public boolean typeEquals(Type type1, Type type2) {
		// Removed the assert_ calls as this is a hotspot when using typecase
		return type1.typeEqualsImpl(type2);
	}

        public SJInbranchType SJInbranchType(String target) //<By MQ> Added target
	{
	    return new SJInbranchType_c(this, target); //<By MQ> Added target
	}

        public SJInbranchType SJInbranchType(String target, boolean isDependentlyTyped) //<By MQ> Added target
	{
	    return new SJInbranchType_c(this, target, isDependentlyTyped); //<By MQ> Added target
	}
	
	public SJOutwhileType SJOutwhileType()
	{
		return new SJOutwhileType_c(this);
	}

    public SJSessionType SJOutwhileType(SJSessionType body) {
        return SJOutwhileType().body(body);
    }

 //<By MQ> Added target
    public SJInwhileType SJInwhileType(String target)
	{
	    return new SJInwhileType_c(this, target);
	}

    public SJInwhileType SJInwhileType(SJSessionType body, String target) {
        return SJInwhileType(target).body(body);
    }
 //<By MQ> Added target
    public SJRecursionType SJRecursionType(SJLabel lab)
	{
		return new SJRecursionType_c(this, lab);
	}

	public SJRecurseType SJRecurseType(SJLabel lab)
	{
		return new SJRecurseType_c(this, lab);
	}

	public SJUnknownType SJUnknownType()
	{
		return new SJUnknownType(this);
	}	

	public SJDelegatedType SJDelegatedType(SJSessionType st)
	{
		return new SJDelegatedType(this, st);
	}

    /*public SJSetType SJSetType(List<SJSessionType_c> members) {
        return new SJSetType_c(this, members);
    }*/
	
	public SJSetType SJSetType(List<SJSessionType> members) {
    return new SJSetType_c(this, members);
	}

    // Adapted from the Coffer example.
  public ParsedClassType createClassType(LazyClassInitializer init, 
      Source fromSource) 
	{
  	/*if (init instanceof DeserializedClassInitializer)
  	{
  		System.out.println("4: " + init); 
  	}*/
  	
		if (!init.fromClassFile()) // The classes from the source files being compiled right now.
		{
			return SJParsedClassType(init, fromSource);
		}
		else // Precompiled classes (both SJ and plain Java).  
		{	
			return super.createClassType(init, fromSource);
		}
	}		
  
	public SJParsedClassType SJParsedClassType(LazyClassInitializer init, Source fromSource)
	{
		return new SJParsedClassType_c(this, init, fromSource);
	}
  
  public SJFieldInstance SJFieldInstance(FieldInstance fi, boolean isNoAlias, boolean isFinal)
  {
		return new SJFieldInstance_c(fi, isNoAlias, isFinal);
	}  
  
	public SJConstructorInstance SJConstructorInstance(ConstructorInstance ci)
	{
		return new SJConstructorInstance_c(ci);
	}
	
	public SJMethodInstance SJMethodInstance(MethodInstance mi)
	{
		return new SJMethodInstance_c(mi);
	}
	
	public SJLocalInstance SJLocalInstance(LocalInstance li, boolean isNoAlias, boolean isFinal)
	{
		return new SJLocalInstance_c(li, isNoAlias, isFinal);
	}
	
	public SJNoAliasReferenceType SJNoAliasReferenceType(ReferenceType rt)
	{
		return new SJNoAliasReferenceType_c(rt);
	}

	public SJNoAliasFinalReferenceType SJNoAliasFinalReferenceType(ReferenceType rt, boolean isFinal)
	{
		return new SJNoAliasFinalReferenceType_c(rt, isFinal);
	}

	public SJFieldProtocolInstance SJFieldProtocolInstance(SJFieldInstance fi, SJSessionType st, String sjname)
	{
		return new SJFieldProtocolInstance_c(fi, st, sjname);
	}

	public SJLocalProtocolInstance SJLocalProtocolInstance(SJLocalInstance li, SJSessionType st, String sjname)
	{
		return new SJLocalProtocolInstance_c(li, st, sjname);
	}

	public SJLocalChannelInstance SJLocalChannelInstance(SJLocalInstance ci, SJSessionType st, String sjname)
	{
		return new SJLocalChannelInstance_c(ci, st, sjname);
	}
	
	public SJLocalSocketInstance SJLocalSocketInstance(SJLocalInstance si, SJSessionType st, String sjname)
	{
		return new SJLocalSocketInstance_c(si, st, sjname);
	}	
	
	public SJLocalServerInstance SJLocalServerInstance(SJLocalInstance ci, SJSessionType st, String sjname)
	{
		return new SJLocalServerInstance_c(ci, st, sjname);
	}

	public SJLocalSelectorInstance SJLocalSelectorInstance(SJLocalInstance ci, SJSessionType st, String sjname)
	{
		return new SJLocalSelectorInstance_c(ci, st, sjname);
	}
	
	public boolean wellFormedRecursions(SJSessionType st)
	{
		try
		{
			wellFormedRecursionsAux(st, new HashSet<SJLabel>(), new HashSet<SJLabel>()); // Would be enough to just use a hasRecurse boolean? (As in the original implementation.)  
		}
		catch (SemanticException se)
		{
			se.printStackTrace();
			
			return false;
		}
		
		return true;
	}

	// Recursively visits each element of a session type. Collects recursion scope labels and checks that all recurse are bound. Also checks that an element with a recurse has no child at any lexical level up to the outermost recursion scope (only tail recursion permitted).  
	private void wellFormedRecursionsAux(SJSessionType st, Set<SJLabel> labs, Set<SJLabel> jumps) throws SemanticException
	{
		if (st == null) // For empty branch cases and loop bodies.
		{
			return;
		}

		if (st instanceof SJRecurseType)
		{
			SJRecurseType sjrt = (SJRecurseType) st;
			SJLabel lab = sjrt.label();

			if (!labs.contains(lab))
			{
				throw new SemanticException("[SJTypeSystem_c] Recurse label not bound: " + sjrt);
			}

			jumps.add(lab);
		}
		else if (st instanceof SJLoopType) // Compound types.
		{
			if (st instanceof SJRecursionType)
			{
				SJRecursionType sjrt = (SJRecursionType) st;
				SJLabel lab = sjrt.label(); 
				
				if (labs.contains(lab))
				{
					throw new SemanticException("[SJTypeSystem_c] Label already in scope: " + lab);
				}
				
				labs.add(lab);
	
				wellFormedRecursionsAux(sjrt.body(), labs, jumps);
				
				labs.remove(lab);
				jumps.remove(lab);
			}
			else 
			{
				wellFormedRecursionsAux(((SJLoopType) st).body(), new HashSet<SJLabel>(), jumps); // Empty labs set, so can't jump out.			
			}
		} 
		else if (st instanceof SJBranchType)
		{
			SJBranchType sjobt = (SJBranchType) st;
			
			Set<SJLabel> next = new HashSet<SJLabel>();
			
			for (SJLabel lab : sjobt.labelSet())
			{
				Set<SJLabel> current = new HashSet<SJLabel>();
				
				current.addAll(jumps);				
				
				wellFormedRecursionsAux(sjobt.branchCase(lab), labs, current); 
				
				next.addAll(current);
			}
			
			jumps.addAll(next);
		}
		
		if (st.child() != null) // Handles basic types. 
		{
			if (!jumps.isEmpty())
			{
				throw new SemanticException("[SJTypeSystem_c] Only tail recursion is permitted [2]: " + st);
			}

			wellFormedRecursionsAux(st.child(), labs, jumps); // jumps is empty.
		}

		return;
	}
	
	// Relaxing the inherited protected access (needed for overriding pretty printing). 
	public static String listToString(List l)
	{
		return TypeSystem_c.listToString(l);
	}
    //<By MQ> My added definitions
        public SJFieldGProtocolInstance SJFieldGProtocolInstance(SJFieldInstance fi, SJSessionType st, String sjname)
	{
		return new SJFieldGProtocolInstance_c(fi, st, sjname);
	}

	public SJLocalGProtocolInstance SJLocalGProtocolInstance(SJLocalInstance li, SJSessionType st, String sjname)
	{
		return new SJLocalGProtocolInstance_c(li, st, sjname);
	}
        public SJParticipantsType SJParticipantsType()
	{
		return new SJParticipantsType_c(this);
	}
	public SJGMsgType SJGMsgType(Type messageType) throws SemanticException
	{
		return new SJGMsgType_c(this, messageType);
	}
	public SJGBranchType SJGBranchType()
	{
		return new SJGBranchType_c(this);
	}

	public SJGBranchType SJGBranchType(boolean isDependentlyTyped)
	{
		return new SJGBranchType_c(this, isDependentlyTyped);
	}
    public SJGLoopType SJGLoopType()
	{
		return new SJGLoopType_c(this);
	}

    public SJGLoopType SJGLoopType(SJSessionType body) {
        return SJGLoopType().body(body);
    }
	public SJGBeginType SJGBeginType()
	{
		return new SJGBeginType_c(this);
	}

    //</By MQ>
}
