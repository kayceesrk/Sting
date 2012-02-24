//<By MQ> Added
package sessionj.visit;

import java.util.*;
import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.*;
import polyglot.util.InternalCompilerError;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessops.compoundops.*;


public class SJBatchingWithDataFlow extends DataFlow
{
    protected static class DataFlowItem extends Item
    {
	String operationName;

	public DataFlowItem(String on)
	{
	    operationName = on;
	}

	public int hashCode() 
	{
            return 4234;
        }

	public boolean equals(Object o)
	{
	    return false;
	}	
    }

    protected static class DefUseFinder extends NodeVisitor {
        protected Set def;
        protected Set use;

        public DefUseFinder(Set def, Set use) {
            this.def = def;
            this.use = use;
        }
        
        public Node override(Node parent, Node n) {
	    if (parent instanceof LocalAssign) {
		LocalAssign_c a = (LocalAssign_c) parent;
		if (n == a.left()) {
		    return n;
		}
	    }
                
	    return null;
        }

        public Node leave(Node old, Node n, NodeVisitor v) {
            if (n instanceof Local) {
                use.add(((Local_c)n).localInstance().name());
            } else if (n instanceof LocalAssign) {
                Expr left = ((LocalAssign_c)n).left();
                if (left instanceof Local) {
                    def.add(((Local_c)left).localInstance().name());
                }
            }

            return n;
        }
    }

	protected static class ReceiveStmtId
	{
	    public String id;
	    public Stmt stmt;
	    public ReceiveStmtId(String s, Stmt st)
	    {
		id = s;
		stmt = st;
	    }
	}
	
    public SJBatchingWithDataFlow(Job job, TypeSystem ts, NodeFactory nf) {
	super(job, ts, nf, 
	true /* forward analysis */, 
	true  /* perform dataflow on entry to CodeDecls */);	
    }

    protected NodeVisitor enterCall(Node n) throws SemanticException 
    {
	//debug("enterCall(): n of type " + n.getClass().getName());

        return this;
    }

    private Block optimizeBlock(Block b)
    {
	List<Stmt> statements = b.statements();
	List<ReceiveStmtId> delayedReceives = new LinkedList<ReceiveStmtId>();
	List<Stmt> newStatements = new LinkedList<Stmt>();
	boolean changed = false;
	for(Stmt st : statements)
	{
	    //debug("statement type " + st.getClass().getName() + "\n" + st);
	    //debug("This statement uses variables " + getUsed(st));
	    String id = isReceiveCall(st);
	    if(id != null)
	    {
		changed = true;
		//debug("found a receive call that is assigned to Id " + id);
		delayedReceives.add(new ReceiveStmtId(id, st));
	    }
	    else
	    {		
		if(st instanceof SJCompoundOperation)
		{
		    for(ReceiveStmtId recvStmt : delayedReceives)
		    {
			newStatements.add(recvStmt.stmt);
		    }
		    delayedReceives.clear();
		}
		else if(delayedReceives.size() != 0) //there are some delayed receives
		{
		    Set usedVars = getUsed(st);
		    int maxIndex = -1; // the maximum index of receive() that needs to be inserted before the next statement
		    for(int i = 0; i < delayedReceives.size(); i++)
		    {
			String receiveVar = delayedReceives.get(i).id;
			//debug("receiveVar " +  receiveVar);
			//debug("usedVars " + usedVars);
			if (usedVars.contains(receiveVar))
			    maxIndex = i;
		    }
		    for(int i = 0; i <= maxIndex; i++)
		    {
			//debug("found a statement that uses a receive() variable");
			newStatements.add(delayedReceives.remove(0).stmt);
		    }
		}
		newStatements.add(st);
	    }
	}
	
	for(ReceiveStmtId st : delayedReceives)
	{
	    newStatements.add(st.stmt);
	}
	b = b.statements(newStatements);
	//if(changed)
	//debug("Blcock: " + b);
	return b;
    }

    protected Set getUsed(Node n)
    {
	return getDefUse(n)[1];
    }

    protected Set[] getDefUse(Node n) 
    {
        final Set def = new HashSet();
        final Set use = new HashSet();

        if (n != null) {
            n.visit(createDefUseFinder(def, use));
        }

        return new Set[] {def, use};
    }

    protected NodeVisitor createDefUseFinder(Set def, Set use) 
    {
        return new DefUseFinder(def, use);
    }

    public String isReceiveCall(Node n)
    {
	if(n instanceof LocalDecl_c)
	{
	    Node init = ((LocalDecl_c)n).init();
	    if(init instanceof SJReceive_c)
	    {
		return ((LocalDecl_c)n).name();
	    }
	    else if(init instanceof Cast_c)
	    {
		Expr expr = ((Cast_c)init).expr();
		if(expr instanceof SJReceive_c)
		    return ((LocalDecl_c)n).name();
	    }
	}	
	else if(n instanceof Eval_c)
	{
	    Node n2 = ((Eval_c)n).expr();
	    if(n2 instanceof LocalAssign_c)
	    {
		Node right = ((LocalAssign_c)n2).right();
		if(right instanceof SJReceive_c)
		{
		    return ((LocalAssign_c)n2).left().toString();
		}
		else if(right instanceof Cast_c)
		{
		    Expr expr = ((Cast_c)right).expr();		    
		    if(expr instanceof SJReceive_c)
			return ((LocalAssign_c)n2).left().toString();
		}
	    }
	}
	return null;
	
    }

    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException 
    {    
        //debug("leaveCall() ");         
	if(n instanceof Block)
	{
	    return optimizeBlock((Block)n);
	}

        return super.leaveCall(old, n, v);
    }

    /*public void check(FlowGraph graph, Term n, Item inItem, Map outItems) throws SemanticException
    {
	System.out.println("Batching.check()");
        throw new InternalCompilerError("check() Should never be called.");
	}*/

    public void check(FlowGraph graph, Term n, boolean b, Item inItem, Map outItems) throws SemanticException
    {
        System.out.println("Batching.check()");
        //throw new InternalCompilerError("check() Should never be called.");
    }

    public Item confluence(List inItems, Term node, FlowGraph graph) 
    {
	System.out.println("Batching.confluence()");
        throw new InternalCompilerError("confluence() Should never be called.");
    }

    public Item confluence(List inItems, Term node, boolean entry, FlowGraph graph)
    {
	System.out.println("Batching.confluence()");
	for (Iterator i = inItems.iterator(); i.hasNext(); ) 
	{
	    i.next();
        }
	return new DataFlowItem("Confluence");
        //throw new InternalCompilerError("confluence() Should never be called.");
    }

    /*public Item createInitialItem(FlowGraph graph, Term node)
    {	
	System.out.println("Batching.createInitialItem1()");
	debug("graph: " + graph);
	debug("node: " + node);
	return new DataFlowItem("Unknown Operation");
	}*/

    public Item createInitialItem(FlowGraph graph, Term node, boolean b)
    {
	System.out.println("Batching.createInitialItem2()");
	//debug("graph: " + graph);
	//debug("node: " + node);
	return new DataFlowItem("Unknown Operation");
    }

    /*public Map flow(Item in, FlowGraph graph, Term n, Set succEdgeKeys)
    {
	debug("Batching.flow()");
	return itemToMap(in, succEdgeKeys);
	}*/

    public Map flow(Item in, FlowGraph graph, Term n, boolean entry, Set succEdgeKeys)
    {
	//debug("Batching.flow()");

	Map m = itemToMap(new DataFlowItem("Unknown2"), succEdgeKeys);
	if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_OTHER)) {
            m.put(FlowGraph.EDGE_KEY_OTHER, new DataFlowItem("Other"));
        }
        if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_TRUE)) {
            m.put(FlowGraph.EDGE_KEY_TRUE, new DataFlowItem("True"));
        }
        if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_FALSE)) {
            m.put(FlowGraph.EDGE_KEY_FALSE, new DataFlowItem("False"));
        }
	return itemToMap(new DataFlowItem("Unknown"), succEdgeKeys);
    }

    void debug(String msg)
    {
	System.out.println(msg);
    }

}