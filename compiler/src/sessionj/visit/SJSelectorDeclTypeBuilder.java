/**
 * 
 */
package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.*;
import sessionj.ast.createops.*;
import sessionj.extension.*;
import sessionj.types.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 *	Similar to SJChannelDeclTypebuilder.
 *
 */
public class SJSelectorDeclTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJSelectorDeclTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
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
		else if (n instanceof SJSelectorCreate)
		{
			n = buildSJSelectorCreate((SJSelectorCreate) n); // We don't currently check that all created selectors are assigned to a variable (shouldn't be needed). 
		}
		
		return n;
	}
	
	private FieldDecl buildFieldDecl(FieldDecl fd) throws SemanticException
	{	
		Type t = fd.declType();
		
		if (t.isSubtype(SJ_SELECTOR_INTERFACE_TYPE)) 
		{
			throw new SemanticException("[SJSelectorDeclTypeBuilder] Field type not supported: " + t);
		}
		
		return fd;
	}
	
	private LocalDecl buildLocalDecl(LocalDecl ld) throws SemanticException
	{	
		Type t = ld.declType();
		SJLocalInstance li = (SJLocalInstance) ld.localInstance();

    if (t.isSubtype(SJ_SELECTOR_INTERFACE_TYPE))
		{    	
			Expr init = ld.init();
			
			SJSessionType st = null;
			String sjname = ld.name();
									
			if (init instanceof SJSelectorCreate) // Bit of a discrepancy between selectors and servers, etc. Selector must currently be assigned at the variable declaration (like channels and unlike servers), but must still be used inside a try. Because a selector is not "active" until at least one session has been registered, which can only happen inside the try. 
			{			
 				st = getSessionType(init);				
			}
			else
			{
				throw new SemanticException("[SJSelectorDeclTypeBuilder] Unexpected selector variable initializer: " + init.getClass());
			}		
			
			ld = ld.localInstance(sjts.SJLocalSelectorInstance(li, st, sjname));
			ld = (LocalDecl) setSJNamedExt(sjef, ld, st, sjname);			
		}
		
		return ld;
	}
	
	private SJSelectorCreate buildSJSelectorCreate(SJSelectorCreate sc) throws SemanticException
	{		
		NamedVariable nv = (NamedVariable) sc.arguments().get(0); // Factor out constant.  
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
				throw new RuntimeException("[SJSelectorDeclTypeBuilder] Shouldn't get in here.");
			}
		}
		else //if (nv instanceof Local)
		{
			ni = (SJNamedInstance) context().findLocal(pname);
		}								
	
		sc = (SJSelectorCreate) setSJTypeableExt(sjef, sc, ni.sessionType());
		
		return sc;
	}
}
