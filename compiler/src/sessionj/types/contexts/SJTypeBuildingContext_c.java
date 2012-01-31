/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import polyglot.types.*;
import polyglot.visit.ContextVisitor;

import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sesstry.*;
import sessionj.ast.sessvars.*;
import sessionj.types.*;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import sessionj.util.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 * SJSessionTypeChecker uses this context manager to complete type building, i.e. we do some type checking in here. Subsequent passes should just use SJContext_c (probably via SJSessionVisitor), which should involve much less "calculation" and more simple lookups.
 *
 */
public class SJTypeBuildingContext_c extends SJContext_c implements SJTypeBuildingContext   
{
	private final SJTypeSystem sjts;
	
	public SJTypeBuildingContext_c(ContextVisitor cv, SJTypeSystem sjts)
	{
		super(cv, sjts);
		
		this.sjts = sjts;
	}
	
	public void advanceSession(String sjname, SJSessionType st) throws SemanticException // st should be single type object.
	{
		if (currentContext() instanceof SJBranchContext)
        // Hacky? This is here because branch contexts are a kind of meta context,
        // should never be updated by session implementations directly. Prevents inline if-statements.
		{
			throw new SemanticException("[SJTypeBuildingContext_c] Unsupported branch context for session implementation: " + sjname);			
		}
				
		if (st != null && st.child() != null)
		{
			throw new RuntimeException("[SJContent_c] Shouldn't get in here: " + st);
		}
		
		super.advanceSession(sjname, st);
	}
	
	public SJSessionType delegateSession(String sjname) throws SemanticException // Could be called completeSession (also used for session method argument passing).
	{
		SJSessionType remaining = sessionRemaining(sjname);
		
		if (remaining == null)
		{
			throw new SemanticException("[SJTypeBuildingContext_c] Cannot delegate a completed session: " + sjname);
		}
		
		//SJSessionType bar = remaining; // Will be the "difference" between remaining and innermostRemaining.
		
		//for (SJSessionType st = innermostRemaining; st != null; st = st.child())
		{
			/*if (expectedSessionOperation(sjname) == null) // There's a similar routine in popContextElement. We're basically "carrying over" the remainder of the complete session implementation to the outer scopes.
			{
				SJSessionType foo = sessionImplemented(sjname);
				
				setSessionImplemented(sjname, (foo == null) ? st : foo.append(st)); // Doesn't work: implementation that should be carried over outside of a compound operation, e.g. outbranch, gets sucked inside the operation.
				
				break;
			}*/
			
			//advanceSession(sjname, st.nodeClone());
			
			//bar = bar.child();
		}
		
		// FIXME: if remaining is a recurse type, unfold it back to the recursion type. // Partly done in sessionRemaining.
		
		SJSessionType foo = sessionImplemented(sjname);		
		SJSessionType bar = sjts.SJDelegatedType(remaining);
		
		setSessionActive(sjname, null);
		setSessionImplemented(sjname, foo == null ? bar : foo.append(bar));
		
		/*for (int i = contexts().size() - 2; i >= 0; i--) // Doesn't work for inner nested branches.
		{
			SJContextElement ce = contexts().get(i);
			
			if (!ce.sessionActive(sjname))
			{			
				ce.setImplementedOverflow(sjname, bar);
				
				break;
			}
		}*/
		
		/*int i = contexts().size() - 1; // Also tried to manually sort out the context stack here for the delegated session, but it didn't work. 
		
		SJContextElement ce = contexts().get(i--);
		
		SJSessionType active = ce.getActive(sjname);
		SJSessionType foo = null;
		
		for (SJSessionType st = remaining; st != null; st = st.child()) // Hacky? Alternative would be to propagate a "delegated" signal down the context stack (through to outer scopes).			
		{
			while (active == null)
			{
				ce = contexts().get(i--); 
				
				active = ce.getActive(sjname);
			}
			
			if (ce == currentContext())
			{
				advanceSession(sjname, st.nodeClone());
				
				foo = st.nodeClone();
			}
			else
			{
				//SJSessionType active = ce.getActive(sjname); // Duplicated from advanceSession.			
				SJSessionType implemented = ce.getImplemented(sjname);

				System.out.println("c: " + sjname + ", " + active + ", " + implemented + ", " + st);
				
				ce.setActive(sjname, active.child());
				ce.setImplemented(sjname, (implemented == null) ? st : implemented.append(st));
			}
		}*/
				
		return remaining;
	}
	
	public void recurseSessions(List<String> sjnames) throws SemanticException
	{
		for (SJContextElement ce : contexts())
		{
			if (ce instanceof SJSessionRecursionContext)
			{
				List<String> targets = ((SJSessionRecursionContext) ce).targets();
				
				for (String sjname : sjnames)
				{
					if (targets.contains(sjname))
					{
						if (!targets.equals(sjnames))
						{
							throw new SemanticException("[SJTypeBuildingContext_c] recurse does not match recursion target: " + targets);				
						}
						
						return;
					}
				}
			}
		}				
	}
	
	/*public SJSessionType expectedSessionOperation(String sjname)
	{	
		return currentContext().getActive(sjname);
	}
	
	public SJSessionType sessionImplemented(String sjname)
	{
		return currentContext().getImplemented(sjname);
	}*/
	
	// If anything gets added/changed here due to new session operations/constructs, don't forget to update the super routine as well!
	public SJSessionType sessionRemaining(String sjname) throws SemanticException  
	{
		SJSessionType remaining = null;
		SJSessionType innermostRemaining = null; // What's remaining in the current scope (excludes what's remaining outside, e.g. continuations).
		
		boolean enterWhenCase = false; // HACK: we "reset" the algorithm when we enter a typecase - typecases currently cannot have continuations for the target session. The result should end up the same as expectedSessionOperation. 
		
		Map<SJLabel, SJRecursionType> recursionScopes = new HashMap<SJLabel, SJRecursionType>();
		
		for (SJContextElement ce : contexts()) // Starts from bottom of the stack (outermost context). We take the maximum possible active type (remaining session to be implemented) from the outermost scope, and take away the bits we've found have implemented as we move through the inner scopes. Remember, nesting of active types should only come from the first elements at each level being an open branch scope - delegation within loops not allowed.
		//for (int i = 0; i < contexts().size(); i++)
		{		
			//SJContextElement ce = contexts().get(i);
			
			if (remaining == null || enterWhenCase)
			{
				if (ce.sessionActive(sjname))
				{	
					remaining = ce.getActive(sjname);
					
					/*if (remaining != null)
					{
						remaining = remaining.getCanonicalForm();
					}*/
					
					innermostRemaining = remaining;
					
					if (enterWhenCase)
					{
						enterWhenCase = false;
					}
				}
			}
			else
			{ 
				if (ce instanceof SJLoopContext)
				{
					//RAY
					if (ce instanceof SJSessionRecursionContext) // FIXME: not sure this is correct at all.
					{
						//SJRecursionType rt = (SJRecursionType) sjts.SJRecursionType(src.lab()).body(src.getSession(sjname)); // Reconstruct the session type for the session on entering the recursion scope.  
						SJRecursionType rt = (SJRecursionType) innermostRemaining; 
						
						recursionScopes.put(rt.label(), rt);
						
						SJSessionType child = remaining.child();
						
						remaining = ((SJRecursionType) remaining).body(); 
						innermostRemaining = remaining;
						
						remaining = remaining.append(child);
					}
					else
					//YAR
					{
						throw new SemanticException("[SJTypeBuildingContext_c] Cannot delegate session within loop context: " + sjname);
					}						
				}
				else if (ce instanceof SJBranchCaseContext)
				{
					if (!((SJBranchCaseContext) ce).isTerminal(sjname)) // Currently a bit of a work around. Can we avoid this restriction?
					{
						throw new SemanticException("[SJTypeBuildingContext_c] Cannot delegate session within non-terminal branch case: " + sjname);
					}
					
					/*for (int j = i - 1; j >= 0; j--) // Doesn't work: manually updating the context stack doesn't work, we will damage or remove information that might be needed for subsequent branch cases. The best bet seems to be propagating an explicit delegated/completed signal. For now, using an implemented "overflow" hack.
					{
						SJContextElement foo = contexts().get(j);
						
						if (foo.sessionActive(sjname)) // HACK: we're just erasing the remainder of the active session past the first branch element. It was hard to carry this information over for the existing context pop mechanism to use (it gets sucked inside the implementation of the branch instead of following the branch), and hard to update those outer contexts right now (we need to do it after the context pop update).
						{
							foo.setActive(sjname, foo.getActive(sjname).nodeClone()); // Will redundantly go up the context stack for each nested branch scope. 
						}						
					}*/
					
					SJSessionType child = remaining.child();
					
					remaining = ((SJBranchType) remaining).branchCase(((SJBranchCaseContext) ce).label()); 
					innermostRemaining = remaining;
					
					remaining = remaining.append(child); // FIXME: gets the full remainder of the session for completion. But will break the advanceSession routine below if we're currently inside an inner scope that only has a fragment of the remainder as the active type, e.g. if we're delegating a session from within a branch on that session and there are operations after the branch. 
				}
				else if (ce instanceof SJTypecaseContext) // FIXME: typecase contexts currently cannot have continuations for the target session (unlike the above branch contexts).
				{
					enterWhenCase = true; // FIXME: is this OK? How about nested typecase. And should typecase be allowed to have continuations in the future?
				}
				else
				{					
					SJSessionType implemented = ce.getImplemented(sjname);
					
					/*if (implemented != null)
					{
						implemented = implemented.getCanonicalForm();
					}*/
					
					for ( ; implemented != null; implemented = implemented.child())
					{
						remaining = remaining.child();
						innermostRemaining = innermostRemaining.child();
					}					
				}
			}
		}

		//RAY
		remaining = substituteTypeVariables(remaining, recursionScopes);
		//YAR
		
		return remaining;
	}
	
    // CAREFUL: This function modifies its map argument. Make sure a copy is passed in.
	// Can this be put back into the SJLoopType unfold routine? (That routine is currently unused.)
	public static SJSessionType substituteTypeVariables(SJSessionType st, Map<SJLabel, SJRecursionType> map) // Made public as a hack for SJStateManager_c.
	{
		if (st == null) // Cannot delegate finished sessions (remaining is null), but this is needed for e.g. empty branch cases and loop bodies. 
		{
			return null;
		}
		
		SJTypeSystem sjts = st.typeSystem();
		
		SJSessionType child = st.child();
		
		if (st instanceof SJBranchType)
		{
			SJBranchType bt = (SJBranchType) st;
			//<By MQ>			
			SJBranchType nbt = bt instanceof SJInbranchType ? sjts.SJInbranchType(((SJInbranchType_c)st).target()) : sjts.SJOutbranchType();
			//</By MQ>
			for (SJLabel lab : bt.labelSet())
			{
				nbt = nbt.branchCase(lab, substituteTypeVariables(bt.branchCase(lab), map));
			}
			
			st = nbt;
		}
		else if (st instanceof SJRecursionType) // FIXME: we only want to do this for unbound type variables, but is this a correct way to do it? Not actually sure this is needed: only labels for recursion scopes that have been entered to calculate the remaining session type have been recorded (by remainingSession).
		{
			map.remove(((SJRecursionType) st).label());
		}
		else if (st instanceof SJRecurseType) 
		{
			SJLabel lab = ((SJRecurseType) st).label();
			
			if (map.containsKey(lab)) // FIXME: won't work with label values reused by nested recursion scopes (but not sure if this is allowed anyway).  
			{
				st = map.get(lab).copy();
			}
		}
		
		if (child != null)
		{
			st = st.child(substituteTypeVariables(child, map));
		}
		
		return st;
	}
	
	/*public boolean serviceInScope(String sjname)
	{
		return currentContext().serviceInScope(sjname);
	}
	
	public boolean serviceOpen(String sjname)
	{
		return currentContext().serviceOpen(sjname);
	}
	
	public boolean sessionInScope(String sjname)
	{
		return currentContext().sessionInScope(sjname);
	}
	
	public boolean sessionActive(String sjname)
	{
		return currentContext().sessionActive(sjname);
	}
	
	//public void pushCode();
	
	public void pushBlock()
	{
		pushContextElement(new SJContextElement_c(currentContext()));
	}
	
	public void pushBranch()
	{
		pushContextElement(new SJBranchContext_c(currentContext()));
	}
	
	public void pushLoop()
	{	
		SJLoopContext lc = new SJLoopContext_c(currentContext());
		
		//l.clearSockets(); // Prevent session being reopened whilst it could already be open.
		lc.clearSessions();	
		
		pushContextElement(lc);
	}
	
	public void pushTry()
	{
		SJTryContext tc = new SJTryContext_c(currentContext());
		
		tc.clearSessions();
		
		pushContextElement(tc);
	}
	
	public void pushMethodBody(MethodDecl md) throws SemanticException
	{
		MethodInstance mi = md.methodInstance(); 
		
		if (mi instanceof SJMethodInstance)
		{	
			SJMethodInstance sjmi = (SJMethodInstance) mi;
			
			Iterator i = md.formals().iterator();				
			
			for (Type t : sjmi.sessionFormalTypes())
			{
				Formal f = (Formal) i.next();
				
				if (t instanceof SJSessionType)
				{
					String sjname = ((SJFormal) f).name();
					
					if (f.flags().isFinal())
					{												
						openSession(sjname, (SJSessionType) t);
					}
					else
					{							
						setSessionActive(sjname, (SJSessionType) t);
						// Maybe should set session implemented as well (to null).
					}
				}
			}
		}
	}*/
	
	public void pushSJSessionTry(SJSessionTry st) throws SemanticException
	{
		SJSessionTryContext tc = new SJSessionTryContext_c(currentContext());
		
		//tc.clearSockets(); // Prevent session being reopened whilst it could already be open. // Instead check if session is alive. 
		tc.clearSessions();
		
		List<String> sjnames = new LinkedList<String>();
		
		for (Iterator i = st.targets().iterator(); i.hasNext(); )
		{
			//String sjname = getSocket(((SJSocketVariable) i.next()).sjname()).sjname(); // Checks that the socket is in scope.
			String sjname = ((SJSocketVariable) i.next()).sjname();
			
			for (int v = contexts().size() - 1; v >= 0; v--)
			{
				SJContextElement ce = contexts().get(v);
	
				if (ce.sessionActive(sjname)) // "Pushing" a session into an inner scope.
				{				
					if (ce.sessionInScope(sjname))
					{
						tc.setSession(sjname, ce.getActive(sjname));
						
						break;
					}					
					else if (ce instanceof SJLoopContext)
					{
						throw new SemanticException("[SJTypeBuildingContext_c] Cannot re-enter session context within a loop context: " + sjname);
					}
					else
					{
						try
						{
							findSocket(sjname); // Hacky? We're using the sockets to identify whether the session is one that has already been opened and has been passsed as a method argument. Will also use this to work out whether a session parameter needs to be closed or not (i.e. if it's final).
						}
						catch (SemanticException se) // noalias session method parameters.						 
						{
							tc.setSession(sjname, ce.getActive(sjname));
							
							break;
						}
					}
				}											
				else
				{
					SJNamedInstance ni = getSocket(sjname); // Socket is in context.
					
					tc.setSession(sjname, ni.sessionType()); // Should be SJUnknownType.
					
					break;
				}
			}
			
			sjnames.add(sjname);
		}
		
		tc.setSessions(sjnames);
		
		pushContextElement(tc);
	}
	
	public void pushSJServerTry(SJServerTry st) throws SemanticException
	{
		SJServerTryContext tc = new SJServerTryContext_c(currentContext());
		 
		tc.clearSessions(); // No need to clear services.
		
		List<String> sjnames = new LinkedList<String>();

        for (Object o : st.targets()) {
            String sjname = ((SJVariable) o).sjname();

            if (tc.serviceInScope(sjname)) {
                throw new SemanticException("[SJTypeBuildingContext_c] server-try already declared: " + sjname);
            }

            tc.setService(sjname, getServer(sjname).sessionType()); // Should be SJUnknownType.

            sjnames.add(sjname);
        }
		
		tc.setServers(sjnames);
		
		pushContextElement(tc);		
	}
	
    public void pushSJBranchOperation(SJBranchOperation b) throws SemanticException // SJInbranch.
	{
		assert !(b instanceof SJOutbranch) : "Should call pushSJBranchCase instead";
        
		SJContextElement current = currentContext();
		
		List<String> sjnames = getTargetNames(b);

        assert sjnames.size() == 1 : "Session branch operations should only have a single target.";
		for (String sjname : sjnames)
		{
			current.checkActiveSessionStartsWith
                (sjname, SJInbranchType.class, "[SJTypeBuildingContext_c] found inbranch, but expected: ");
            // Maybe better to explicitly check session is active (open) as well.
		}
		
		pushContextElement(new SJSessionBranchContext_c(current, b, sjnames));
	}

    public void pushSJBranchCase(SJBranchCase bc) throws SemanticException
	{
		SJContextElement current = currentContext();				
		SJLabel lab = bc.label();
		
		if (bc instanceof SJOutbranch)
		{
			SJOutbranch ob = (SJOutbranch) bc;
			
			pushContextElement(new SJOutbranchContext_c(current, ob, getTargetNames(ob), lab));
			
			for (String sjname : getTargetNames(ob))
			{
				SJSessionType st = current.getActive(sjname);

                current.checkActiveSessionStartsWith
                    (sjname, SJOutbranchType.class, "[SJTypeBuildingContext_c] found outbranch, but expected: ");
				
				SJBranchType obt = (SJBranchType) st;
				
				if (!obt.hasCase(lab))
				{
					throw new SemanticException("[SJTypeBuildingContext_c] unexpected label: " + lab);
				}
				
				openSession(sjname, obt.branchCase(lab));
				
				if (st.child() == null)
				{
					((SJBranchCaseContext) currentContext()).addTerminal(sjname);
				}			
			}
		}
		else //if (bc instanceof SJInbranchCase)
		{
			pushContextElement(new SJBranchCaseContext_c(current, lab)); // FIXME: to fit the current context pop+check algorithm, we need to push a session context (as for outbranches) here - otherwise incomplete (sub)sessions are not caught (this is what currently happens). // Currently, hacked a fix into the algorithm for now (similarly for typecase cases). 			
			
			for (String sjname : ((SJSessionContext) current).targets()) // Should only be a single target.
			{
				SJSessionType st = current.getActive(sjname);

                current.checkActiveSessionStartsWith
                    (sjname, SJInbranchType.class, "[SJTypeBuildingContext_c] found inbranch, but expected: ");
				
				SJInbranchType ibt = (SJInbranchType) st;
				
				if (!ibt.hasCase(lab))
				{
					throw new SemanticException("[SJTypeBuildingContext_c] unexpected label: " + lab);
				}				
				
				openSession(sjname, ibt.branchCase(lab));
				
				if (st.child() == null)
				{
					((SJBranchCaseContext) currentContext()).addTerminal(sjname);
				}
			}			
		} 			
	}
	
	public void pushSJWhile(SJWhile w) throws SemanticException
	{
		SJContextElement current = currentContext();
		List<String> sjnames = getSJSessionOperationExt(w).targetNames();
		SJSessionLoopContext slc = new SJSessionLoopContext_c(current, w, sjnames);
		
		slc.clearSessions();
		
		pushContextElement(slc);
		
		for (String sjname : sjnames)
		{
			SJSessionType st = current.getActive(sjname);
			
			if (w instanceof SJOutwhile) 
			{
				if (st == null || !st.startsWith(SJOutwhileType.class))
				{
					throw new SemanticException("[SJTypeBuildingContext_c] found outwhile, but expected: " + st);
				}
			}
			else if (w instanceof SJOutInwhile)
			{
				Collection<String> sourceNames = new LinkedList<String>();
                for (Object r : ((SJOutInwhile) w).insyncSources())
                    sourceNames.add(((SJVariable) r).sjname());
                
                if (sourceNames.contains(sjname))
				{
					if (!st.startsWith(SJInwhileType.class))
					{
						throw new SemanticException("[SJTypeBuildingContext_c] found inwhile, but expected: " + st);
					} 
					
				}
				else
				{
					if (!st.startsWith(SJOutwhileType.class))
					{
						throw new SemanticException("[SJTypeBuildingContext_c] found outwhile, but expected: " + st);
					}					
				}
			}
			else //if (w instanceof SJInwhile)
			{
				if (st == null || !st.startsWith(SJInwhileType.class))
				{
					throw new SemanticException("[SJTypeBuildingContext_c] found inwhile, but expected: " + st);
				}
			}
			
			openSession(sjname, ((SJLoopType) st).body());
		}
	}
	
	public void pushSJRecursion(SJRecursion r) throws SemanticException
	{
		SJContextElement current = currentContext();
		List<String> sjnames = getSJSessionOperationExt(r).targetNames();
		
		SJContextElement slc = new SJSessionRecursionContext_c(current, r, sjnames);
		
		slc.clearSessions();
		
		pushContextElement(slc);
		
		for (String sjname : sjnames)
		{
			SJSessionType st = current.getActive(sjname);
			
			if (!st.startsWith(SJRecursionType.class))
			{
				throw new SemanticException("[SJTypeBuildingContext_c] found recursion, but expected: " + st);
			}
			
			SJLabel lab = r.label();
			
			if (!lab.equals(((SJRecursionType) st).label()))
			{
				throw new SemanticException("[SJTypeBuildingContext_c] unexpected recursion label: " + lab);
			}
			
			openSession(sjname, ((SJLoopType) st).body());
		}
	}
	
	public void pushSJWhen(SJWhen when) throws SemanticException 
	{
    SJTypecaseContext current = (SJTypecaseContext) currentContext();

    /*SJSetType set = current.getActiveSetType(); // Active type does not need to explicitly be a set type to use a typecase: all session types are set types implicitly.         
    SJSessionType selected = when.selectMatching(set);
    // TODO when this fails, new context is not pushed, so next when will
    // blow. Look at bypass() methods in visitor

    if (selected instanceof SJSetType)
    {
    	SJSetType st = (SJSetType) selected;
    	
    	if (st.isSingleton()) 
    	{
    		selected = st.getSingletonMember();
    	}
    }*/
    
    SJSessionType outer = current.getActive(current.getSessionName()).getCanonicalForm();
    SJSessionType selected;
    
    if (outer instanceof SJSetType)
    {
    	selected = when.selectMatching(((SJSetType) outer).getFlattenedForm()).getCanonicalForm();
    }
    else // Could be a (flattened) singleton typecase...
    {
    	SJSessionType wt = when.type().getCanonicalForm(); // ...so still need to get canonical form. 
    	
    	//if (!outer.isSubtype(wt)) 
    	if (!outer.typeEquals(wt)) // TODO: treat subtyping.
    	{
    		throw new SemanticException("[SJContext_c] Expected type " + outer + " incompatible with when-case: " + wt);
    	}
    	
    	selected = wt;
    }
 
    SJContextElement whenContext = new SJContextElement_c(current); // FIXME: should be pushing a SJBranchCaseContext like inbranch cases and outbranch. 
    whenContext.setActive(current.sjname, selected);
    pushContextElement(whenContext);
	}	
	
	public SJContextElement pop() throws SemanticException
	{
		return popContextElement();				
	}
	
	public void pushContextElement(SJContextElement ce)
	{
		contexts().push(ce);		
	}
	
	private boolean subsumeBranchSession(SJContextElement foo, SJContextElement bar, String sjname) throws SemanticException
	{
		boolean hasSessionImplementations = false;
		
		SJSessionType fimplemented = foo.getImplemented(sjname);
		SJSessionType bimplemented = bar.getImplemented(sjname);
		
		if (fimplemented == null)
		{
			if (bimplemented != null)
			{
				throw new SemanticException("[SJTypeBuildingContext_c] Incompatible branch: " + sjname);
			}
		}
		else
		{
			if (bimplemented == null) 
			{
				throw new SemanticException("[SJTypeBuildingContext_c] Incompatible branch: " + sjname);
			}
			else
			{								
				foo.setImplemented(sjname, fimplemented.subsume(bimplemented));
				
				hasSessionImplementations = true;
			}						
		}
		
		return hasSessionImplementations;
	}
	
	public SJContextElement popContextElement() throws SemanticException
	{
		SJContextElement poppedContext = contexts().pop();

    popBranchContext(poppedContext);

    // Until here, just sorting out popped branch (and inbranch) contexts (i.e. the branch case contexts ces for branch context poppedContext).
		
		if (!contexts().isEmpty())
		{
			SJContextElement current = currentContext();

			if (current instanceof SJBranchContext) // Now sorting out popped branch case contexts (ces merged into poppedContext) for the current branch context.
            // FIXME: sort out channels as well.
            // (Now not necessary? Since channels must be na-final, so cannot assign different
            // values across branches, or even use after being received in a branch - but this may be too restrictive).  
			{
        pullUpBranchContext(poppedContext, current);
			}
			else
			{			
				for (String sjname : poppedContext.channelSet())
				{
					SJSessionType st = poppedContext.getChannel(sjname).sessionType();
					SJLocalChannelInstance ni = (SJLocalChannelInstance) current.getChannel(sjname);
					
					if (ni != null && ni.sessionType() instanceof SJUnknownType)
					{
						current.setChannel(sjts.SJLocalChannelInstance(ni, st, sjname));
					}
				}
				
				for (String sjname : poppedContext.activeSessions())
				{				
					SJSessionType implemented = poppedContext.getImplemented(sjname);
					
					if (current.sessionActive(sjname))
          // Can be not in scope but still active for e.g. try (s) { try { try (s) { ... 
					{
						if (poppedContext instanceof SJSessionTryContext) // FIXME: make SJSessionTryContext a SJSessionContext.
						{
              handleSessionTryContext(poppedContext, sjname);
            }
						
						if (poppedContext instanceof SJSessionContext)
						{																				
							SJSessionContext sc = (SJSessionContext) poppedContext;
							
							if (sc.targets().contains(sjname)) // Need to build back the type structure that was stripped when the compound operation context was entered.
							{							
								SJSessionType expected = poppedContext.getActive(sjname);							
																 
								// FIXME: should treate typecase contexts just like branch contexts. 
								// FIXME: when branches in a typecase do not properly advance session types of sockets other than the one the typecase is acting on. 
								// Need to handle those the same way as branch contexts.
								//if (expected != null && isNotTypecaseSet(poppedContext, expected))
				        if (expected != null && !(poppedContext instanceof SJTypecaseContext)) // Maybe can be better factored out with above check for branch contexts and below check.
								{
                  // No need to check for delegated types here because delegation within a branch must be terminal (and delegateSession clears the active type).
                  throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [4], expected: " + expected);
                }

                if (poppedContext instanceof SJSessionBranchContext)
								{
									// SJInbranch (and SJInbranchCase) done above with branch merging.
								}
								else if (poppedContext instanceof SJOutbranchContext)
								{
                  implemented = handleOutbranchContext(poppedContext, implemented);
		  								}
								else if (poppedContext instanceof SJSessionLoopContext)
								{
                  implemented = handleSessionLoopContext(sjname, implemented, ((SJSessionContext) poppedContext).node());
                }
                else if (poppedContext instanceof SJTypecaseContext) // FIXME: should treate typecase contexts just like branch contexts.
                {
                  //implemented = ((SJTypecaseContext) poppedContext).getActiveSetType();
                	
                	SJTypecaseContext tc = (SJTypecaseContext) poppedContext; 
                	
                	implemented = tc.getActive(tc.getSessionName());
                	
                	if (!(implemented instanceof SJSetType)) // FIXME: ideally, should not need to do this. We should work using the canonical forms of session types.
                	{
                		List<SJSessionType> members = new LinkedList<SJSessionType>();
                		
                		members.add(implemented);
                		
                		implemented = sjts.SJSetType(members);
                	}
                }
								else 
								{
									throw new SemanticException("[SJTypeBuildingContext_c] Session context not yet supported: " + poppedContext);
								}
								
								poppedContext.setImplemented(sjname, implemented);
                // So that the returned context contains the right session type,
                // used in SJSessionTypeChecker.leaveCall   
							}				
						} 
            else if (current instanceof SJTypecaseContext) // poppedContext is currently a SJContextElement pushed for a when-case.
            {            	            	
              // FIXME: we're currently ignoring implementations on non-target sessions within the typecase. Should be treated like a branch. 
              // Currently we just check that the specified type for each when-branch is implemented correctly and we don't bother to record the actual implementation type.
            	
            	checkExpectedIsNull(poppedContext, sjname); // Since the poppedContext for the when-case is not a SJSessionContext, we need to check for session completion within the popped context manually. Similar to the hack in pullUpBranchContext for inbranches (since inbranch case contexts are also currently not session contexts).     	
            	
              implemented = null; // TODO: hack, to avoid advance session as long as we've not exited the typecase.
              
              // FIXME: should avoid needing manual expected-is-null checks for when-cases (and inbranch-cases).
            } 
						
						advanceSessionWithImplementedPart(sjname, implemented);
          }
					else // Session from popped context is not active in current context - the session must have been completed.  
					{						
						checkExpectedIsNull(poppedContext, sjname);
					}
				}			
			}		
		}
		
		return poppedContext;
	}

	private void checkExpectedIsNull(SJContextElement poppedContext, String sjname) throws SemanticException
	{
		SJSessionType expected = poppedContext.getActive(sjname);
		/*SJSessionType overflow = currentContext().getImplementedOverflow(sjname);
		
		while (expected != null && overflow != null)
		{
			expected = expected.child();
			overflow = overflow.child();
		}												
		
		if (overflow != null)
		{
			throw new SJRuntimeException("[SJTypeBuildingContext_c] Shouldn't get here: " + overflow);
		}
		
		currentContext().removeImplementedOverflow(sjname);*/ 
		
		if (expected != null)
		{
			// No need to check for delegated types here because delegation within a branch must be terminal (and delegateSession clears the active type).
		  throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [5], expected: " + expected);
		}
	}

    private boolean isNotTypecaseSet(SJContextElement poppedContext, SJSessionType expected) {
        return !(poppedContext instanceof SJTypecaseContext && expected instanceof SJSetType);
    }

    private void pullUpBranchContext(SJContextElement ce, SJContextElement current) throws SemanticException {
        for (String sjname : ce.activeSessions())
        {
            if (!current.sessionActive(sjname)) // Session was opened within the branch. // FIXME: make SJSessionTryContext a SJSessionContext.
            {
                SJSessionType expected = ce.getActive(sjname);

                if (expected != null) // Could use checkExpectedIsNull, but the error messages are currently distinguished for ease of compiler debugging.
                {
                    throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [1], expected: " + expected);
                }
            }
            
            if (ce instanceof SJSessionContext && ((SJSessionContext) ce).targets().contains(sjname)
            		|| current instanceof SJSessionBranchContext) // FIXME: added this hack for inbranch cases. Is it OK?
            {
                SJSessionType expected = ce.getActive(sjname);

                if (expected != null)
                {
                    throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [2], expected: " + expected);
                }
            }

            if (ce instanceof SJSessionTryContext && ((SJSessionTryContext) ce).getSessions().contains(sjname)) // Can be factored out if session-try contexts are unified with regular session contexts.
            {
                SJSessionType expected = ce.getActive(sjname);

                if (expected != null)
                {
                    throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [3], expected: " + expected);
                }
            }
        }

        ((SJBranchContext) current).addBranch(ce);
    }

    private void advanceSessionWithImplementedPart(String sjname, SJSessionType implemented) throws SemanticException {
    	
    		if (implemented != null)
    		{
    			implemented = implemented.getCanonicalForm(); // HACK: found that set types can pop up here.
    		}
    	
        while (implemented != null) {
            /*if (expectedSessionOperation(sjname) == null) // Hacky? Early session completion (e.g. delegation, method passing) will give the full remaining type as implemented.
            {
                SJSessionType foo = sessionImplemented(sjname);

                setSessionImplemented(sjname, (foo == null) ? implemented : foo.append(implemented)); // Doesn't work: see delegateSession.

                break;
            }
            else*/
            advanceSession(sjname, implemented.nodeClone()); // Already type checked within the inner block. 		
            implemented = implemented.child();
        }
    }

    private SJSessionType handleOutbranchContext(SJContextElement ce, SJSessionType implemented) {
        SJSessionType implementedOrig = implemented;

        implemented = sjts.SJOutbranchType().branchCase(((SJOutbranchContext) ce).label(), implemented);

        if (implementedOrig != null && implementedOrig.getLeaf() instanceof SJDelegatedType)
        {
            implemented = sjts.SJDelegatedType(implemented); // Strictly speaking, that is not the type we are delegating - but it is the type implemented. Needs to be this way for branch subsumption.
        }
        return implemented;
    }

    private SJSessionType handleSessionLoopContext(String sjname, SJSessionType implemented, SJCompoundOperation co) {
        if (co instanceof SJWhile) // FIXME: should really differentiate the contexts instead (as for SJOutbranch).
        {
            if (co instanceof SJOutwhile)
            {
                implemented = sjts.SJOutwhileType(implemented);
            }
            else if (co instanceof SJOutInwhile) // FIXME: hacky.
            {
                if (((SJVariable) ((SJOutInwhile) co).targets().get(0)).sjname().equals(sjname))
                {
                    implemented = sjts.SJInwhileType(implemented, "MQUnknown"); //<By MQ> OutInWhile not yet supported
                }
                else
                {
                    implemented = sjts.SJOutwhileType(implemented);
                }
            }
            else
            {
                implemented = sjts.SJInwhileType(implemented, ((SJInwhile_c)co).arguments().get(0).toString().replace("\"", "")); //<By MQ> MQTODO: We need the target from the protocol, not from the implementation. FIX THIS
            }
        }
        else //if (co instanceof SJRecursion)
        {
            implemented = sjts.SJRecursionType(((SJRecursion) co).label()).body(implemented);
        }
        return implemented;
    }

    private void handleSessionTryContext(SJContextElement ce, String sjname) throws SemanticException {
        if (((SJSessionTryContext) ce).getSessions().contains(sjname))
        {
            SJSessionType expected = ce.getActive(sjname);

            if (expected != null) // Maybe can be better factored out with above check for branch contexts and below check.
            {
                //if (!(implemented != null && implemented.getLeaf() instanceof SJDelegatedType)) // Not needed now because delegation within a branch must be terminal (and delegateSession clears the active type).
                {
                    throw new SemanticException("[SJTypeBuildingContext_c] Session " + sjname + " incomplete [6], expected: " + expected);
                }
            }
        }
    }

    private void popBranchContext(SJContextElement ce) throws SemanticException {
        if (ce instanceof SJBranchContext) // Merge branches. (Does not include SJOutbranch, but does include If.)
        {
            List<SJContextElement> ces = ((SJBranchContext) ce).branches();

            if (ces.isEmpty())
            // Hacky? This means we're popping an inline if-statement. Inline loop statements are checked OK
            // because of their session context properties - no operations on outer sessions allowed by typing.
            {
                // Nothing to do. ce should not contain any session implementations -
                // should be prevented by advanceSession (maybe not the best place to do that).
            }
            else
            {
                Map<String, SJSessionType> inbranches = new HashMap<String, SJSessionType>();
                Set<String> delegated = null;

                if (ce instanceof SJSessionBranchContext)
                {
                    delegated = checkBranches(ce, ces, inbranches);
                    
                    for (String m : delegated) // Should this be in here? Or outside with a null-guard?
                    {
                        inbranches.put(m, sjts.SJDelegatedType(inbranches.get(m)));
                    }
                }
                else
                {
                	// Just a SJBranchContext, but not a SJSessionBranchContext. Do we need to do anything here?
                }
                
                SJContextElement firstBranch = ces.remove(0); // Going to use the first one to hold subsumption results.

                boolean hasSessionImplementations = false;

                for (String sjname : firstBranch.activeSessions())
                {
                    SJSessionType orig = ce.getImplemented(sjname);
                    SJSessionType inner = firstBranch.getImplemented(sjname);

                    if (orig == null)
                    {
                        if (inner != null) // Both can be null for e.g. if-statements that contains no session code.
                        {
                            hasSessionImplementations = true;
                        }
                    }
                    else
                    {
                        if (inner == null) {
                            throw new RuntimeException("[SJTypeBuildingContext_c] Shouldn't get in here.");
                        }

                        if (!orig.typeEquals(inner))
                        {
                            hasSessionImplementations = true;
                        }

                        break;
                    }
                }

                for (SJContextElement branchContext : ces)
                {
                    Set<String> fset = firstBranch.activeSessions();
                    Set<String> bset = branchContext.activeSessions();

                    for (String sjname : fset)
                    {
                        if (bset.contains(sjname))
                        {
                            boolean res = subsumeBranchSession(firstBranch, branchContext, sjname);

                            if (!hasSessionImplementations)
                            {
                                hasSessionImplementations = res;
                            }
                        }
                    }
                }

                for (String sjname : firstBranch.activeSessions())
                {
                    SJSessionType st = firstBranch.getImplemented(sjname);

                    while (st != null) {
                        SJSessionType branchBodyType = st.nodeClone();

                        SJSessionType active = ce.getActive(sjname);
                        SJSessionType implemented = ce.getImplemented(sjname);

                        ce.setActive(sjname, active == null ? null : active.child());
                        ce.setImplemented(sjname, implemented == null ? branchBodyType : implemented.append(branchBodyType));
                        st = st.child();
                    }
                }

                if (ce instanceof SJSessionBranchContext)
                {
                    for (String sjname : inbranches.keySet()) // Should be a single target.
                    {
                        SJSessionType active = ce.getActive(sjname);
                        SJSessionType implemented = ce.getImplemented(sjname);

                        SJSessionType st = inbranches.get(sjname); // Should be a single SJInbranchType node.

                        ce.setActive(sjname, active == null ? null : active.child());
                        ce.setImplemented(sjname, implemented == null ? st : implemented.append(st));
                    }
                }

                ((SJBranchContext) ce).setHasSessionImplementations(hasSessionImplementations); // If ces is empty, then session implementations would not be possible, so this is correctly false by default.
            }
        }
    }

    private Set<String> checkBranches(SJContextElement ce, List<SJContextElement> ces, Map<String, SJSessionType> inbranches) throws SemanticException {
        Set<String> delegated = new HashSet<String>();
        for (SJContextElement branchContext : ces)
        {
            for (String sjname : ((SJSessionContext) ce).targets()) // Should only be a single target.
            {
                SJLabel lab = ((SJBranchCaseContext) branchContext).label();
                SJSessionType implemented = branchContext.getImplemented(sjname);

                if (implemented instanceof SJDelegatedType) // For outbranch, this is taken care of by branch case subsumption.
                {
                    delegated.add(sjname);
                }

                SJInbranchType ibt;

                if (inbranches.containsKey(sjname))
                {
                    ibt = (SJInbranchType) inbranches.get(sjname);

                    if (ibt.hasCase(lab)) // Is this already checked earlier? If not, should it be checked here?
                    {
                        throw new SemanticException("[SJTypeBuildingContext_c] repeated session branch case: " + lab);
                    }
                    else
                    {
                        ibt = ibt.branchCase(lab, implemented);
                    }
                }
                else
                {
		    //<By MQ>
		    if(!(ce.getActive(sjname) instanceof SJInbranchType))
		    {
			throw new SemanticException("[SJTypeBuildingContext_c]: shouldn't get here.");
		    }
		    String target = ((SJInbranchType_c)ce.getActive(sjname)).target();
                    ibt = sjts.SJInbranchType(target).branchCase(lab, implemented);
		    //</By MQ>
                }

                inbranches.put(sjname, ibt);
                branchContext.removeSession(sjname); // For inbranch branches, we need to merge the cases - cannot use subsume (the opposite).
            }
        }
        return delegated;
    }

    public void checkSessionsCompleted() throws SemanticException // Maybe can be factored out with the routine in popContextElement. // Currently unused.
	{
		SJContextElement ce = currentContext();
		
		for (String sjname : ce.activeSessions()) // Duplicated from popContextElement.
		{
			checkExpectedIsNull(ce, sjname);
		}
	}
	
	public boolean inSJBranchCaseContext()
	{
		for (SJContextElement ce : contexts()) // Does it matter which order we go through the context stack?
		{
			if (ce instanceof SJBranchCaseContext)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean inSJSessionLoopContext()
	{
		for (SJContextElement ce : contexts())
		{
			if (ce instanceof SJSessionLoopContext)
			{
				return true;
			}
		}
		
		return false;
	}
}
