package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.QQ;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sesstry.SJSelectorTry;
import sessionj.ast.sesstry.SJServerTry;
import sessionj.ast.sesstry.SJSessionTry;
import sessionj.ast.sesstry.SJTry;
import sessionj.ast.sessvars.SJLocalSocket;
import sessionj.ast.sessvars.SJSelectorVariable;
import sessionj.ast.sessvars.SJServerVariable;
import sessionj.types.SJTypeSystem;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Raymond
 *
 * Removes the final from socket/server declarations and inserts the required close operations in the finally block.
 * 
 */
public class SJSessionTryTranslator extends SJSessionVisitor
//public class SJSessionTryTranslator extends ContextVisitor
{
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

	//private SJContext sjcontext = new SJContext_c(this);
	
	public SJSessionTryTranslator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	//protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	protected Node sjLeaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof LocalDecl) // Might be better to make SJAbstractSocket/ServerDecl.
		{
			LocalDecl ld = (LocalDecl) n;
			
			if (ld.declType().isSubtype(SJ_SOCKET_INTERFACE_TYPE))
			{
				n = translateSJSocketDecl(ld);
			}
			else if (ld.declType().isSubtype(SJ_SERVER_INTERFACE_TYPE))
			{
				n = translateSJServerDecl(ld);
			}
		}
		else if (n instanceof SJTry)
		{
			if (n instanceof SJSessionTry)			
			{
				n = translateSJSessionTry((SJSessionTry) n);
			}
			else if (n instanceof SJServerTry)
			{
				n = translateSJServerTry((SJServerTry) n);
			}
			else if (n instanceof SJSelectorTry)
			{
				n = translateSJSelectorTry((SJSelectorTry) n);
			}
			else
			{
				throw new SemanticException("[SJSessionTryTranslator] ");
			}
		}

		return n;
	}

	private LocalDecl translateSJSocketDecl(LocalDecl ld)
	{
		return removeFinalFlagAndNullInitialise(ld);
	}
	
	private LocalDecl translateSJServerDecl(LocalDecl ld)
	{
		return removeFinalFlagAndNullInitialise(ld);
	}
	
	private SJSessionTry translateSJSessionTry(SJSessionTry st) throws SemanticException
	{
		List<Local> sockets = new LinkedList<Local>();
        // Not bothering to make SJLocalSockets at this stage of compilation. And no need to build SJ type information.
        // Now, can be needed since we have the SJSessionVisitor framework.
        // But for the close operations, maybe not needed.

        for (Object o : st.targets()) {
            SJLocalSocket ls = (SJLocalSocket) o;

            try {
                sjcontext.findSocket(ls.sjname());

                sockets.add(sjnf.Local(ls.position(), ls.id()));
                // noalias session method parameters are registered as sockets
                // when the MethodBody context is pushed.
            }
            catch (SemanticException se) {
                // Session is a na-final session method parameter.
                // Even if the argument actually passed is noalias, we don't need to close here because
                // the argument isn't nulled at the Call, so the original close will be invoked.
            }
        }

		if (!sockets.isEmpty())
		{
			Position pos = st.position();		
			QQ qq = new QQ(sjts.extensionInfo(), pos);
			
			String translation = "";
			List<Object> mapping = new LinkedList<Object>();				
			
			Expr x;

			if (sockets.size() == 1)
			{
				x = sockets.get(0);
			}
			else
			{
				x = sjnf.NewArray(pos, sjnf.CanonicalTypeNode(pos, SJ_SOCKET_INTERFACE_TYPE), 1, sjnf.ArrayInit(pos, sockets));
			}

			translation += "%T.close(%E);"; // Could make close an SJInternalOperation.
			mapping.add(sjnf.CanonicalTypeNode(pos, SJ_RUNTIME_TYPE));
			mapping.add(x);
			
			Eval e = (Eval) qq.parseStmt(translation, mapping);
			e = (Eval) buildAndCheckTypes(this, e);
			
			st = (SJSessionTry) appendToFinally(st, e);
		}
		
		return st;
	}
	
	private SJServerTry translateSJServerTry(SJServerTry st) throws SemanticException
	{
		Position pos = st.position();		
		QQ qq = new QQ(sjts.extensionInfo(), pos);
		
		String translation = "{";
		List<Object> mapping = new LinkedList<Object>();		

        for (Object o : st.targets()) {
            SJServerVariable sv = (SJServerVariable) o;

            translation += "if (%E != null) %E.close();";
            mapping.add(sv);
            mapping.add(sv);

        }
        translation += "}";
        Block b = (Block) qq.parseStmt(translation, mapping);
        b = (Block) buildAndCheckTypes(this, b);

        st = (SJServerTry) appendToFinally(st, b);

		return st;
	}
	
	private SJSelectorTry translateSJSelectorTry(SJSelectorTry st) throws SemanticException // Duplicated from translateSJServerTry
	{
		Position pos = st.position();		
		QQ qq = new QQ(sjts.extensionInfo(), pos);
		
		String translation = "";
		List<Object> mapping = new LinkedList<Object>();		
		
		SJSelectorVariable sv = (SJSelectorVariable) st.targets().get(0); // Factor out constant. // Currently, only a single server is permitted per server-try.
		
		translation += "if (%E != null) %E.close();";
		mapping.add(sv);
		mapping.add(sv);
			
		If i = (If) qq.parseStmt(translation, mapping);
		i = (If) buildAndCheckTypes(this, i);
		
		st = (SJSelectorTry) appendToFinally(st, i);
		
		return st;
	}
	
	private LocalDecl removeFinalFlagAndNullInitialise(LocalDecl ld)
	{
		Flags flags = ld.flags().clearFinal();
		
		ld = ld.flags(flags);
		ld = ld.localInstance(ld.localInstance().flags(flags)); // Should be SJLocalSocketInstance.
		
		ld = ld.init(sjnf.NullLit(ld.position())); // Didn't bother to build types for the NullLit.
		
		return ld;
	}
	
	private SJTry appendToFinally(SJTry st, Stmt s) // Does it matter if we prepend/append?
	{
		Block b = st.finallyBlock();
		
		if (b == null)
		{
			b = sjnf.Block(st.position(), s);
		}
		else
		{
			b = b.append(s); 
		}
		
		st = (SJTry) st.finallyBlock(b);
		
		return st;
	}	
}
