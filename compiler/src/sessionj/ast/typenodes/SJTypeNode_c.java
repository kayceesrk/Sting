package sessionj.ast.typenodes;

import polyglot.ast.TypeNode_c;
import polyglot.ast.TypeNode;
import polyglot.util.*;
import polyglot.visit.PrettyPrinter;
import polyglot.types.Type;

import sessionj.types.sesstypes.*;

import static sessionj.SJConstants.*;

public abstract class SJTypeNode_c extends TypeNode_c implements SJTypeNode
{
	private SJTypeNode child;

	public SJTypeNode_c(Position pos)
	{
		super(pos);
	}

	public SJTypeNode child()
	{
		return child;
	}

	public SJTypeNode child(SJTypeNode child)
	{
		this.child = child;

		return this;
	}
	
	public SJSessionType type()
	{
		SJSessionType st = (SJSessionType) super.type();
		
		if (st != null) // To support SJBranchCaseNode.
		{
			st = st.copy(); // The parent method is just a getter for the type field: the type is set to be the session type. This overriding routine uses the parent getters to get the type of the whole tree. 		
		}
		
		SJTypeNode child = child();
	
		if (child != null) // For SJBranchCaseNode, always null.
		{	
			SJSessionType childType = child.type().copy();

			//if (this instanceof SJProtocolNode) // Because protocol nodes represent full SJSessionTypes, not just a single node.
            st = st.append(childType);
            /*else // This has become an optimisation.
               {
                   st = st.child(childType);
               }*/
		}

		return st;
	}

    public SJTypeNode type(Type st)
	{
		return (SJTypeNode) super.type
            (st == null ? null : (Type) st.copy());
	}

	public String toString()
	{
		return treeToString();
	}

	public String treeToString()
	{
		return nodeToString() + (child() == null ? "" : SJ_STRING_SEPARATOR + child().treeToString());
	}

	// Cannot call type().toString as (session) types may not have been built yet.
	public void prettyPrint(CodeWriter w, PrettyPrinter tr)
	{
		w.write(toString());
    }
}
