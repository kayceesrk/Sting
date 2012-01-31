/**
 * 
 */
package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sessformals.SJFormal;
import sessionj.ast.typenodes.SJProtocolNode;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.types.sesstypes.SJCBeginType;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.SJProcedureInstance;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Raymond
 * 
 * Currently, only session method parameters are supported. Simply records the session parameter information.
 *  
 */
public class SJSessionMethodTypeBuilder extends ContextVisitor
{
    private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

    /**
	 * 
	 */
	public SJSessionMethodTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException
	{		
		return this;
	}
	
	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{				
		if (n instanceof ProcedureDecl)
		{
			n = buildProcedureDecl((ProcedureDecl) n);
		}
		
		return n;
	}
	
	private ProcedureDecl buildProcedureDecl(ProcedureDecl pd) throws SemanticException
	{
		List formals = pd.formals();
		List<Type> sft = new LinkedList<Type>();

        for (Object formal : formals) {
            Formal f = (Formal) formal;
            Type t = f.type().type();

            if (f instanceof SJFormal) // Could check here for channel parameters declared using protocol references, but it's a bit late.
            {
                SJTypeNode tn = ((SJFormal) f).sessionType();
                SJSessionType st = tn.type();

                if (tn instanceof SJProtocolNode && st instanceof SJCBeginType) // Currently, they are incorrectly parsed as SJSocket types. However, if a (otherwise correct) call is actually made to this method, this check is too late - base type checking will have failed.
                {
                    throw new SemanticException("[SJMethodTypeBuilder] Protocol reference for channel type parameters not yet supported: " + f);
                }

                sft.add(st);
            } else {
                sft.add(t);
            }
        }
		
		// FIXME: session method return types not supported yet.
		
		((SJProcedureInstance) pd.procedureInstance()).setSessionFormalTypes(sft); // SJNoAliasTypeBuilder has already converted the procedure instance object to a SJProcedureInstance (with noalias information).
		
		return pd;
	}
}
