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
import sessionj.ast.chanops.*;
import sessionj.ast.sessops.*;
import sessionj.ast.servops.*;
import sessionj.ast.selectorops.*;
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
 * Very similar to SJChannelDeclTypeBuilder.
 *
 * Also builds types for session socket initialisers: SJRequest, SJAccept, SJSelect, etc. // I.e. should be SessionSocketCreateors. 
 *
 */
public class SJSocketDeclTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJSocketDeclTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
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
		/*else if (n instanceof SJSocketCreate)
		{
			n = buildSJSocketCreate((SJSocketCreate) n);
		}*/
		else if (n instanceof SJRequest) // Although type information not needed here, eventually used by SJSessionTypeChecker. 
		{
			n = buildSJRequest((SJRequest) n);
		}
		else if (n instanceof SJAccept)
		{
			n = buildSJAccept((SJAccept) n);
		}
		else if (n instanceof SJSelect)
		{
			n = buildSJSelect((SJSelect) n);
		}
		
		return n;
	}
	
	private FieldDecl buildFieldDecl(FieldDecl fd) throws SemanticException
	{	
		Type t = fd.declType();
		
		if (t.isSubtype(SJ_SOCKET_INTERFACE_TYPE)) 
		{
		        throw new SemanticException("[SJSocketDeclTypeBuilder] Field type not supported: " + t);
		}
		
		return fd;
	}
	
	private LocalDecl buildLocalDecl(LocalDecl ld) throws SemanticException
	{	
		Type t = ld.declType();
		SJLocalInstance li = (SJLocalInstance) ld.localInstance();
		SJNamedInstance ni = null; 
		
		if (t.isSubtype(SJ_SOCKET_INTERFACE_TYPE)) 
		{
			Expr init = ld.init();
			SJSessionType st = null;
			String sjname = ld.name();
									
			if (init == null || init instanceof NullLit)
			{
				st = sjts.SJUnknownType();
			}
			/*else if (init instanceof SJSocketCreate) // Deprecated.
			{			
 				st = getSessionType(init);				
			}*/
			/*else if (init instanceof SJRequest) // FIXME: actually, isn't possible - socket must be declared before the session-try, but the request can only occur inside the session-try.
			{
				st = getSessionType(init);
			}*/			
			else
			{
				throw new SemanticException("[SJSocketDeclTypeBuilder] Unexpected channel variable initializer: " + init);
			}		
			
			ld = ld.localInstance(sjts.SJLocalSocketInstance(li, st, sjname));
			ld = (LocalDecl) setSJNamedExt(sjef, ld, st, sjname);
		}
		
		return ld;
	}
	
	/*private SJSocketCreate buildSJSocketCreate(SJSocketCreate cc) throws SemanticException
	{		
		Expr arg = (Expr) cc.arguments().get(0); // Factor out constant. 				
		
		SJSessionType st = null;
		
		if (arg instanceof SJChannelVariable)
		{
			SJChannelVariable cv = (SJChannelVariable) arg;
			String cname = cv.sjname();
		
			SJNamedInstance ni = null;
			
			if (cv instanceof Field)
			{		
				// Not permitted by SJVariableParser.
			}
			else //if (cv instanceof Local)
			{
				ni = (SJNamedInstance) context().findLocal(cname);
			}
			
			st = ni.sessionType();
		}
		else if (arg instanceof SJChannelCreate)
		{
			st = getSessionType(arg);  
		}
		else
		{
			throw new SemanticException("[SJSocketDeclTypeBuilder] Unsupported channel expression: " + arg);
		}
	
		cc = (SJSocketCreate) setSJTypeableExt(sjef, cc, st);
		
		return cc;
	}*/
	
	private SJRequest buildSJRequest(SJRequest r) throws SemanticException
	{		
		Receiver target = r.target();  				
		
		SJSessionType st = null;
		String sjname = null;
		
		if (target instanceof SJChannelVariable)
		{
			SJChannelVariable cv = (SJChannelVariable) target;
			String cname = cv.sjname();
		
			SJNamedInstance ni = null;
			
			if (cv instanceof Field)
			{		
				throw new RuntimeException("[SJSocketDeclTypeBuilder] Shouldn't get here."); // Not permitted by SJVariableParser.
			}
			else //if (cv instanceof Local)
			{
				ni = (SJNamedInstance) context().findLocal(cname);
			}
			
			st = ni.sessionType();
			sjname = cv.sjname();
			
			//target = ((Local) target).localInstance(sjts.SJLocalSocketInstance((SJLocalInstance) ((Local) target).localInstance(), st, sjname)); // Not necessary? Instance type objects only useful for static declaration-related information. Actually, instance type objects should be the same for all references to a variable.  
			target = (SJChannelVariable) setSJNamedExt(sjef, target, st, sjname);
		}
		else if (target instanceof SJChannelCreate)  
		{			
			st = getSessionType(target);  
			sjname = "[anonymous server-address]"; // FIXME: factor out constant.
			
			target = (SJChannelCreate) setSJTypeableExt(sjef, target, st);
		}
		else
		{
			throw new SemanticException("[SJSocketDeclTypeBuilder] Unsupported channel expression: " + target);
		}
	
		r = (SJRequest) r.target(target);
		r = (SJRequest) setSJNamedExt(sjef, r, st, sjname); // Should set the "second" extension object (the "first" should be for noalias typing).  
		
		return r;
	}	
	
	private SJAccept buildSJAccept(SJAccept a) throws SemanticException // FIXME: try to factor stuff out with above buildSJRequest.
	{		
		Receiver target = a.target();  				
		
		SJSessionType st = null;
		String sjname = null;
		
		if (target instanceof SJServerVariable)
		{
			SJServerVariable sv = (SJServerVariable) target;
			String sname = sv.sjname();
		
			SJNamedInstance ni = null;
			
			if (sv instanceof Field)
			{		
				throw new RuntimeException("[SJSocketDeclTypeBuilder] Shouldn't get here."); // Not permitted by SJVariableParser.
			}
			else //if (sv instanceof Local)
			{
				ni = (SJNamedInstance) context().findLocal(sname);
			}
			
			st = ni.sessionType();
			sjname = sv.sjname();
			
			//target = ((Local) target).localInstance(sjts.SJLocalSocketInstance((SJLocalInstance) ((Local) target).localInstance(), st, sjname)); // Not necessary? Instance type objects only useful for static declaration-related information. Actually, instance type objects should be the same for all references to a variable.  
			target = (SJServerVariable) setSJNamedExt(sjef, target, st, sjname);
		}		
		else
		{
			throw new SemanticException("[SJSocketDeclTypeBuilder] Unsupported server expression: " + target);
		}
	
		a = (SJAccept) a.target(target);
		a = (SJAccept) setSJNamedExt(sjef, a, st, sjname); // Should set the "second" extension object (the "first" should be for noalias typing).
		
		return a;
	}	
	
	private SJSelect buildSJSelect(SJSelect s) throws SemanticException // FIXME: try to factor stuff out with above.
	{				
		Receiver target = s.target();  				
		
		SJSessionType st = null;
		String sjname = null;
		
		if (target instanceof SJSelectorVariable)
		{
			SJSelectorVariable sv = (SJSelectorVariable) target;
			String sname = sv.sjname();
		
			SJNamedInstance ni = null;
			
			if (sv instanceof Field)
			{		
				throw new RuntimeException("[SJSocketDeclTypeBuilder] Shouldn't get here."); // Not currently permitted by SJVariableParser.
			}
			else //if (sv instanceof Local)
			{
				ni = (SJNamedInstance) context().findLocal(sname);
			}
			
			st = ni.sessionType();
			sjname = sv.sjname();
			  
			target = (SJSelectorVariable) setSJNamedExt(sjef, target, st, sjname);
		}		
		else
		{
			throw new SemanticException("[SJSocketDeclTypeBuilder] Unsupported server expression: " + target);
		}
	
		s = (SJSelect) s.target(target);
		s = (SJSelect) setSJNamedExt(sjef, s, st, sjname); // Should set the "second" extension object (the "first" should be for noalias typing).
		
		return s;
	}	
}
