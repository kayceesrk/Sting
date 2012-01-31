package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.SJSpawn;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessops.compoundops.SJOutbranch;
import sessionj.ast.sesstry.SJTry;
import sessionj.ast.sessvars.*;
import sessionj.util.SJLabel;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;

import java.util.LinkedList;
import java.util.List;

public class SJVariableParser extends ContextVisitor
{
    private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

	public SJVariableParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof Variable)
		{
			if (n instanceof Field)
			{
				n = parseSJField((Field) n);
			}
			else if (n instanceof Local)
			{
				n = parseSJLocal(this, (Local) n);
			}
			else //if (n instanceof ArrayAccess)
			{
				n = parseSJArrayAccess((ArrayAccess) n);				
			}		
		}
		else if (n instanceof SJTry) // Should be SJAmbiguousTry.
		{
			n = parseSJTry((SJTry) n);
		}
		else if (n instanceof SJSessionOperation)
		{
			n = parseSJSessionOperation(this, (SJSessionOperation) n);
			
			/*if (n instanceof SJOutbranch)
			{
				SJOutbranch ob = (SJOutbranch) n;
				
				if (ob.isDependentlyTyped())
				{
					n = fixDependentlyTypedBranch(ob);
				}
			}*/
		}
		else if (n instanceof SJSpawn)
		{
			n = parseSJSpawn((SJSpawn) n);
		}

		return n;
	}
	
	/*private Node fixDependentlyTypedBranch(SJOutbranch ob)
	{
		SJLabel lab = ob.label();
		
		System.out.println("a: " + lab.getId());
		
		return ob;
	}*/

	private Field parseSJField(Field f) throws SemanticException // Doesn't attach extension objects (SJVariables are not SJTypeable).
	{
		Type t = f.type();
		
		if (t.isSubtype(SJ_ABSTRACT_CHANNEL_TYPE))
		{
		        throw new SemanticException("[SJVariableParser] Session-typed fields not yet supported: " + f);
		}
		
		return f;
	}		
	
	private static final Local parseSJLocal(ContextVisitor cv, Local l) throws SemanticException // Doesn't attach extension objects (SJVariables are not SJTypeable).
	{
		SJNodeFactory sjnf = (SJNodeFactory) cv.nodeFactory();
		
		SJVariable v = null;
		
		if (l.type().isSubtype(SJ_CHANNEL_TYPE))
		{
			v = sjnf.SJLocalChannel(l.position(), l.id());
		}
		else if (l.type().isSubtype(SJ_SOCKET_INTERFACE_TYPE))
		{
			v = sjnf.SJLocalSocket(l.position(), l.id());
		}		
		else if (l.type().isSubtype(SJ_SERVER_INTERFACE_TYPE))
		{
			v = sjnf.SJLocalServer(l.position(), l.id());
		}
		else if (l.type().isSubtype(SJ_SELECTOR_INTERFACE_TYPE))
		{
			v = sjnf.SJLocalSelector(l.position(), l.id());
		}
		 
		if (v != null)
		{
			//l = (Local) buildAndCheckTypes(this, v); // Instead could just reassign the existing type objects of `l' to `v'?
			l = (Local) buildAndCheckTypes(cv, v);
		}
		
		return l;
	}	
	
	private ArrayAccess parseSJArrayAccess(ArrayAccess aa) throws SemanticException
	{
		Type t = aa.type();
		
		if (t.isSubtype(SJ_ABSTRACT_CHANNEL_TYPE))
		{
			throw new SemanticException("[SJVariableParser] Session-typed array accesses not yet supported: " + aa);
		}
		
		return aa;
	}		
	
	// Session operation targets currently not visited by base passes.
	private SJTry parseSJTry(SJTry st) throws SemanticException
	{										
		return st.targets(parseSJVariableList(this, st.targets(), false));
	}
		
	// Currently duplicated with SJTry (could make SJTry a SJNamed).
	//RAY: was an instance method, but now refactored to make static because later passes, specifically translations that create basic operations, may need this (e.g. SJCompoundOperationTranslator for recursion-exit operations).
	//private SJSessionOperation parseSJSessionOperation(SJSessionOperation so) throws SemanticException
	protected static final SJSessionOperation parseSJSessionOperation(ContextVisitor cv, SJSessionOperation so) throws SemanticException
	{
		//return so.targets(parseSJVariableList(this, so.targets(), false));
		return so.targets(parseSJVariableList(cv, so.targets(), false));
	}	
	
	private SJSpawn parseSJSpawn(SJSpawn s) throws SemanticException // Based on parseSJSessionOperation.
	{
		return s.targets(parseSJVariableList(this, s.targets(), true));		
	}	
	
	// Should check no repeated sockets for session-try and session operations. // Now also optionally does channels (for SJSpawn).
	//public static final List<SJVariable> parseSJVariableList(List l, boolean channelsAllowed) throws SemanticException
	private static final List<SJVariable> parseSJVariableList(ContextVisitor cv, List l, boolean channelsAllowed) throws SemanticException
	{
		List<SJVariable> targets = new LinkedList<SJVariable>();

        for (Object aL : l) {
            //Receiver r = (Receiver) buildAndCheckTypes(this, (Node) aL); // Runs AmbiguityRemover.
        	Receiver r = (Receiver) buildAndCheckTypes(cv, (Node) aL); // Runs AmbiguityRemover.

            Type t = r.type();

            if (r instanceof Local) {
                if (t.isSubtype(SJ_SOCKET_INTERFACE_TYPE) || t.isSubtype(SJ_SERVER_INTERFACE_TYPE)) {
                    //targets.add((SJVariable) parseSJLocal((Local) r));
                    targets.add((SJVariable) parseSJLocal(cv, (Local) r));
                } else if (channelsAllowed && t.isSubtype(SJ_CHANNEL_TYPE)) {
                    targets.add((SJChannelVariable) parseSJLocal(cv, (Local) r));                    
                } 
                else if (t.isSubtype(SJ_SELECTOR_INTERFACE_TYPE))
                {
                	targets.add((SJSelectorVariable) parseSJLocal(cv, (Local) r));
                }
                else {
                    throw new SemanticException("[SJVariableParser] Expected session socket or server variable, not: " + r);
                }
            } else {
                throw new SemanticException("[SJVariableParser] Expected local variable, not: " + r);
            }
        }
		
		return targets;
	}	
}
