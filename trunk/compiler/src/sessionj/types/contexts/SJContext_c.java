/**
 * 
 */
package sessionj.types.contexts;

import polyglot.ast.Formal;
import polyglot.ast.MethodDecl;
import polyglot.types.MethodInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.visit.ContextVisitor;
import sessionj.ast.sessops.compoundops.*;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sesstry.SJSelectorTry;
import sessionj.ast.sesstry.SJServerTry;
import sessionj.ast.sesstry.SJSessionTry;
import sessionj.ast.sessvars.SJSelectorVariable;
import sessionj.ast.sessvars.SJServerVariable;
import sessionj.ast.sessvars.SJSocketVariable;
import sessionj.ast.sessformals.SJFormal;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.SJLocalInstance;
import sessionj.types.typeobjects.SJMethodInstance;
import sessionj.types.typeobjects.SJNamedInstance;
import static sessionj.util.SJCompilerUtils.getSJSessionOperationExt;
import sessionj.util.SJLabel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Raymond
 *
 * Mostly based on (the old) SJTypeBuildingContext_c, but with type checking parts removed.
 *
 */
public class SJContext_c extends SJContext // Currently only used by SJAbstractSessionVisitor. 
{
	private final SJTypeSystem sjts;
	
	public SJContext_c(ContextVisitor cv, SJTypeSystem sjts)
	{
		super(cv);
		
		this.sjts = sjts;
	}
	
	public SJSessionType findProtocol(String sjname) throws SemanticException
	{
		return ((SJNamedInstance) visitorContext().findVariable(sjname)).sessionType();
	}
	
	public SJSessionType findChannel(String sjname) throws SemanticException
	{
		return getChannel(sjname).sessionType();
	}
		
	public SJSessionType findSocket(String sjname) throws SemanticException
	{		
		return getSocket(sjname).sessionType();
	}
	
	public SJSessionType findServer(String sjname) throws SemanticException
	{
		return getServer(sjname).sessionType();
	}

	public SJSessionType findSelector(String sjname) throws SemanticException
	{
		return getSelector(sjname).sessionType();
	}
	
	public void addChannel(SJNamedInstance ni)
	{
		currentContext().setChannel(ni); 
	}
		
	public void addSocket(SJNamedInstance ni)
	{
		currentContext().setSocket(ni); 
	}			
	
	public void addServer(SJNamedInstance ni)
	{
		currentContext().setServer(ni); 
	}
	
	public void addSelector(SJNamedInstance ni)
	{
		currentContext().setSelector(ni); 
	}
	
	public void addSession(SJNamedInstance ni)
	{
		currentContext().setSession(ni.sjname(), ni.sessionType());  
	}		
	
	public void openService(String sjname, SJSessionType st) throws SemanticException
	{
		currentContext().setService(sjname, st);
	}
	
	public void openSession(String sjname, SJSessionType st) throws SemanticException
	{					
		setSessionRequested(sjname, st);		
		setSessionActive(sjname, st);
		setSessionImplemented(sjname, null);				
	}
	
	public void advanceSession(String sjname, SJSessionType st) throws SemanticException
	{
		SJSessionType active = expectedSessionOperation(sjname);		
		SJSessionType implemented = sessionImplemented(sjname);
				  
		assert active != null : "Tried to advance a completed session on socket " + sjname + " with " + st;
		
		if (st != null && st.startsWith(SJDelegatedType.class))
        // Needed e.g. when popping a compound operation context, but the session is still active
        // - if it was delegated within the popped context, the single trailing SJDelegationType element
        // must be enough to clear the remaining active type. 
		{
			setSessionActive(sjname, null);
		}
		else
		{		
			setSessionActive(sjname, active.child());			
		}
		
		setSessionImplemented(sjname, implemented == null ? st : implemented.append(st));
	}
	
	public SJSessionType delegateSession(String sjname) throws SemanticException
	{
        SJSessionType implemented = sessionImplemented(sjname);
		SJSessionType remaining = sessionRemaining(sjname);		
		
		SJSessionType st = sjts.SJDelegatedType(remaining);
		
		setSessionActive(sjname, null);		
		setSessionImplemented(sjname, implemented == null ? st : implemented.append(st));
		
		return remaining;
	}
	
	public SJSessionType expectedSessionOperation(String sjname)
	{	
		return currentContext().getActive(sjname);
	}
	
	public SJSessionType sessionImplemented(String sjname)
	{
		return currentContext().getImplemented(sjname);
	}
	
	public SJSessionType sessionRemaining(String sjname) throws SemanticException
	{
		SJSessionType remaining = null;
		
		for (SJContextElement ce : contexts()) // Starts from bottom of the stack (outermost context). We take the maximum possible active type (remaining session to be implemented) from the outermost scope, and take away the bits we've found have implemented as we move through the inner scopes. Remember, nesting of active types should only come from the first elements at each level being an open branch scope - delegation within loops not allowed.
		{			
			if (remaining == null)
			{
				if (ce.sessionActive(sjname))
				{	
					remaining = ce.getActive(sjname);
				}
			}
			else
			{ 
				if (ce instanceof SJLoopContext) // We don't expect iteration types here: should have been already checked by previous type building passes.
				{
					if (ce instanceof SJSessionRecursionContext) 
					{
						SJSessionType child = remaining.child();
						
						remaining = ((SJRecursionType) remaining).body(); 
						
						remaining = remaining.append(child);
					}
				}
				else if (ce instanceof SJBranchCaseContext)
				{
					SJSessionType child = remaining.child();
					
					remaining = ((SJBranchType) remaining).branchCase(((SJBranchCaseContext) ce).label()); 					
					remaining = remaining.append(child);
                    // FIXME: gets the full remainder of the session for completion. But will break the advanceSession
                    // routine below if we're currently inside an inner scope that only has a fragment
                    // of the remainder as the active type, e.g. if we're delegating a session from within
                    // a branch on that session and there are operations after the branch. 
				}

				for (SJSessionType implemented = ce.getImplemented(sjname);
                     implemented != null;
                     implemented = implemented.child())
				{
					remaining = remaining.child();				
				}
			}
		}
		
		return remaining;
	}
	
	public boolean serviceInScope(String sjname)
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

	public boolean selectorInScope(String sjname)
	{
		return currentContext().selectorInScope(sjname);
	}
	
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
	// TODO: factor out duplicated code between this and SJSessionTypeChecker.checkAssign
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
					String sjname = f.name();
					SJLocalInstance li = (SJLocalInstance) f.localInstance();
					SJSessionType st = (SJSessionType) t;					
					
					if (f instanceof SJFormal && ((SJFormal) f).isSharedChannel())
					{
						addChannel(sjts.SJLocalChannelInstance(li, st, sjname));
					}
					else if (f instanceof SJFormal && ((SJFormal) f).isSession())
					{
						if (f.flags().isFinal())
						{												
							openSession(sjname, st);
						}
						else
						{							
							addSocket(sjts.SJLocalSocketInstance(li, st, sjname));
							
							setSessionActive(sjname, st);
							// Maybe should set session implemented as well (to null).
						}
					} else if (f instanceof SJFormal && ((SJFormal) f).isServer()) {
                        if (f.flags().isFinal()) {
                            openService(sjname, st);
                            // Replaces the SJUnknownType added at server-try enter.
                        } else {
                            addServer(sjts.SJLocalServerInstance(li, st, sjname));
                            // Replaces the SJUnknownType added by the LocalDecl (for the server socket).                        
                        }

                    }
				}
			}
		}
	}
	
	public void pushSJSessionTry(SJSessionTry st) throws SemanticException
	{
		SJSessionTryContext tc = new SJSessionTryContext_c(currentContext());
		
		tc.clearSessions();
		
		List<String> sjnames = new LinkedList<String>();

        for (Object o : st.targets()) {
            String sjname = ((SJSocketVariable) o).sjname();

            for (int v = contexts().size() - 1; v >= 0; v--) {
                SJContextElement ce = contexts().get(v);

                if (ce.sessionActive(sjname)) // "Pushing" a session into an inner scope.
                {
                    if (ce.sessionInScope(sjname)) {
                        tc.setSession(sjname, ce.getActive(sjname));

                        break;
                    } else {
                        try {
                            findSocket(sjname); // Hacky? We're using the sockets to identify whether the session is one that has already been opened and has been passsed as a method argument. Will also use this to work out whether a session parameter needs to be closed or not (i.e. if it's final).
                        }
                        catch (SemanticException se) // noalias session method parameters.
                        {
                            tc.setSession(sjname, ce.getActive(sjname));

                            break;
                        }
                    }
                } else {
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
		 
		tc.clearSessions(); // No need to clear services. // CHECKME: how about selectors-in-scope?
		
		List<String> sjnames = new LinkedList<String>();

        for (Object o : st.targets()) {
            String sjname = ((SJServerVariable) o).sjname();

            tc.setService(sjname, getServer(sjname).sessionType()); // Should be SJUnknownType.

            sjnames.add(sjname);
        }
		
		tc.setServers(sjnames);
		
		pushContextElement(tc);		
	}
	
	public void pushSJSelectorTry(SJSelectorTry st) throws SemanticException // Duplicated from pushSJServerTry.
	{
		SJSelectorTryContext tc = new SJSelectorTryContext_c(currentContext());
		 
		tc.clearSessions(); // No need to clear services. // CHECKME: how about selectors-in-scope?
		
		List<String> sjnames = new LinkedList<String>();

    for (Object o : st.targets()) 
    {
      String sjname = ((SJSelectorVariable) o).sjname();

      tc.setSelectorInScope(sjname, getSelector(sjname).sessionType()); // Unlike server variables, type should be known.

      sjnames.add(sjname);
    }
		
		tc.setSelectors(sjnames);
		
		pushContextElement(tc);		
	}
	
	public void pushSJBranchOperation(SJBranchOperation b) throws SemanticException // SJInbranch.
	{
		SJContextElement current = currentContext();
		
		List<String> sjnames = getSJSessionOperationExt(b).targetNames();
		
		for (String sjname : sjnames) // Should only be a single target.
		{
			SJSessionType st = current.getActive(sjname);				
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
			
			pushContextElement(new SJOutbranchContext_c(current, ob, getSJSessionOperationExt(ob).targetNames(), lab));
			
			for (String sjname : getSJSessionOperationExt(ob).targetNames())
			{
				SJSessionType st = current.getActive(sjname);
				SJOutbranchType obt = (SJOutbranchType) st;			
				
				openSession(sjname, obt.branchCase(lab));
				
				if (st.child() == null)
				{
					((SJBranchCaseContext) currentContext()).addTerminal(sjname);
				}			
			}
		}
		else //if (bc instanceof SJInbranchCase)
		{
			pushContextElement(new SJBranchCaseContext_c(current, lab));			
			
			for (String sjname : ((SJSessionContext) current).targets()) // Should only be a single target.
			{
				SJSessionType st = current.getActive(sjname);
				SJBranchType ibt = (SJBranchType) st;				
				
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
			
			openSession(sjname, ((SJLoopType) st).body());
		}
	}
	
	public void pushSJRecursion(SJRecursion r) throws SemanticException
	{
		SJContextElement current = currentContext();
		List<String> sjnames = getSJSessionOperationExt(r).targetNames();
		
		SJSessionLoopContext slc = new SJSessionRecursionContext_c(current, r, sjnames);
		
		slc.clearSessions();
		
		pushContextElement(slc);
		
		for (String sjname : sjnames)
		{
			SJSessionType st = current.getActive(sjname);

            openSession(sjname, ((SJRecursionType) st).body());
		}
	}

    protected List<String> getTargetNames(SJSessionOperation op) {
        return getSJSessionOperationExt(op).targetNames();
    }
    
    public void pushSJTypecase(SJTypecase typecase) throws SemanticException {
        SJContextElement current = currentContext();
        List<String> sjnames = getTargetNames(typecase);
        
        assert sjnames.size() == 1;

        String sjname = sjnames.get(0);        
        
        pushContextElement(new SJTypecaseContext(current, typecase, sjname));
    }

    public void pushSJWhen(SJWhen when) throws SemanticException {
        SJTypecaseContext current = (SJTypecaseContext) currentContext();

        SJSessionType outer = current.getActive(current.getSessionName()).getCanonicalForm();
        SJSessionType selected;
        
        if (outer instanceof SJSetType)
        {
        	selected = when.selectMatching(((SJSetType) outer).getFlattenedForm()).getCanonicalForm();
        }
        else // FIXME: are we supposed to do "type checking" here, according to the current design? Peer methods don't seem to?
        {
        	SJSessionType wt = when.type();
        	
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
		return contexts().pop();				
	}
	
	protected void pushContextElement(SJContextElement ce)
	{
		contexts().push(ce);		
	}

	public void setSessionRequested(String sjname, SJSessionType st)
	{
		currentContext().setSession(sjname, st); // Will replace the previous SJUnknownType entry for the session, recorded from the session-try.
	}
	
	public void setSessionActive(String sjname, SJSessionType st)
	{
		currentContext().setActive(sjname, st);
	}
	
	public void setSessionImplemented(String sjname, SJSessionType st)
	{
		currentContext().setImplemented(sjname, st);
	}
	
	public SJNamedInstance getChannel(String sjname) throws SemanticException
	{
		SJContextElement ce = currentContext();
		
		if (!ce.hasChannel(sjname))
		{
			throw new SemanticException("[SJContext_c] Channel not in context: " + sjname);
		}
		
		return ce.getChannel(sjname);
	}	
	
	public SJNamedInstance getSocket(String sjname) throws SemanticException
	{
		SJContextElement ce = currentContext();
		
		if (!ce.hasSocket(sjname))
		{
			throw new SemanticException("[SJContext_c] Socket not in context: " + sjname);
		}
		
		return ce.getSocket(sjname);
	}	
	
	public SJNamedInstance getServer(String sjname) throws SemanticException
	{
		SJContextElement ce = currentContext();
		
		if (!ce.hasServer(sjname))
		{
			throw new SemanticException("[SJContext_c] Server not in context: " + sjname);
		}
		
		return ce.getServer(sjname);		
	}
	
	public SJNamedInstance getSelector(String sjname) throws SemanticException
	{
		SJContextElement ce = currentContext();
		
		if (!ce.hasSelector(sjname))
		{
			throw new SemanticException("[SJContext_c] Selector not in context: " + sjname);
		}
		
		return ce.getSelector(sjname);		
	}	
}
