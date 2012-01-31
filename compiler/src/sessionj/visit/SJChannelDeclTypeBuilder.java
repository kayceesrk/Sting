/**
 * 
 */
package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.*;
import sessionj.ast.createops.*;
import sessionj.ast.protocoldecls.*;
import sessionj.ast.sessvars.*;
import sessionj.ast.sesscasts.SJChannelCast;
import sessionj.ast.sesscasts.SJSessionCast;
import sessionj.ast.sessops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.extension.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.contexts.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.*;
import sessionj.types.noalias.*;
import sessionj.util.noalias.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 */
public class SJChannelDeclTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJChannelDeclTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException
	{
		return this;
	}
	
	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{				
		if (n instanceof VarInit)
		{
			if (n instanceof FieldDecl)
			{
				n = buildFieldDecl((FieldDecl) n);
			}
			else //if (n instanceof LocalDecl)
			{
				n = buildLocalDecl((LocalDecl) n);
			}
		}		
		else if (n instanceof SJChannelCreate)
		{
			n = buildSJChannelCreate((SJChannelCreate) n);
		}
		
		return n;
	}
	
	private FieldDecl buildFieldDecl(FieldDecl fd) throws SemanticException
	{	
		Type t = fd.declType();
		
		if (t.isSubtype(SJ_CHANNEL_TYPE)) // Could make a SJChannelDecl.
		{
			throw new SemanticException("[SJChannelDeclTypeBuilder] Field type not supported: " + t);
		}
		
		return fd;
	}
	
	private LocalDecl buildLocalDecl(LocalDecl ld) throws SemanticException
	{	
		Type t = ld.declType();
		SJLocalInstance li = (SJLocalInstance) ld.localInstance();
		SJNamedInstance ni = null; 
		
		if (t.isSubtype(SJ_CHANNEL_TYPE)) 
		{
			Expr init = ld.init();
			SJSessionType st = null;
			String sjname = ld.name();
									
			/*if (init == null || init instanceof NullLit) // Currently, channels must be na-final. Maybe this should be relaxed (null initialiser permitted, but not NullLit) so that channels can be received within a branch scope but used after the branch (not just within).   
			{
				st = sjts.SJUnknownType();
			}
			else */if (init instanceof SJChannelCreate)
			{			
 				st = getSessionType(init);				
			}
			else if (init instanceof SJChannelCast)
			{
				//st = ((SJChannelCast) init).();
				st = getSessionType(init);
			}
			else
			{
				throw new SemanticException("[SJChannelDeclTypeBuilder] Unexpected channel variable initializer: " + init);
			}		
			
			ld = ld.localInstance(sjts.SJLocalChannelInstance(li, st, sjname));
			ld = (LocalDecl) setSJNamedExt(sjef, ld, st, sjname);			
		}
		
		return ld;
	}
	
	private SJChannelCreate buildSJChannelCreate(SJChannelCreate cc) throws SemanticException
	{		
		NamedVariable nv = (NamedVariable) cc.arguments().get(0); // Factor out constant. // Or could be recorded in a SJChannelDecl. 
		String pname = nv.name(); // FIXME: should parse and create SJProtocolVariables.
				
		SJNamedInstance ni = null;
		
		if (nv instanceof Field)
		{		
			ParsedClassType pct = (ParsedClassType) ((Field) nv).target().type();
			
			if (pct instanceof SJParsedClassType)
			{	
				ni = (SJFieldProtocolInstance) sjts.findField(pct, pname, context.currentClass()); 
			}
			else
			{
				throw new RuntimeException("[SJChannelDeclTypeBuilder] Shouldn't get in here.");
			}
		}
		else //if (nv instanceof Local)
		{
			ni = (SJNamedInstance) context().findLocal(pname);
		}								
	
		cc = (SJChannelCreate) setSJTypeableExt(sjef, cc, ni.sessionType());
		
		return cc;
	}
}
