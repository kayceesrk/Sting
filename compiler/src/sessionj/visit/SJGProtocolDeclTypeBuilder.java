//<By MQ> Added
/**
 * 
 */
package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.visit.*;

import sessionj.ast.*;
import sessionj.ast.protocoldecls.*;
import sessionj.ast.sesscasts.SJAmbiguousCast;
import sessionj.ast.sesscasts.SJSessionTypeCast;
import sessionj.ast.sessformals.SJFormal;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.extension.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;
import sessionj.visit.noalias.SJNoAliasExprBuilder;

/**
 * @author Raymond
 *
 * Also does SJSessionTypeCasts and SJFormals.
 *
 */
public class SJGProtocolDeclTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJGProtocolDeclTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
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
		else if (n instanceof SJSessionTypeCast)
		{
			n = buildSJSessionTypeCast((SJSessionTypeCast) n);
		}
		else if (n instanceof Formal)
		{
			n = buildFormal((Formal) n);
		}
		
		return n;
	}
	
	private Node buildFieldDecl(FieldDecl fd) throws SemanticException
	{	
		Type t = fd.declType();
		
		if (t.isSubtype(SJ_GPROTOCOL_TYPE))
		{
			if (!(fd instanceof SJFieldGProtocolDecl))
			{
				throw new SemanticException("[SJGProtocolDeclTypeBuilder] GProtocols may only be declared using the gprotocol keyword: " + fd);
			}

			SJTypeNode tn = disambiguateSJTypeNode(this, ((SJGProtocolDecl) fd).sessionType());
			SJSessionType st = tn.type();
			String sjname = fd.name(); // Should match that given by SJVariable.sjname.
			
			SJFieldInstance fi = (SJFieldInstance) fd.fieldInstance();
			SJFieldGProtocolInstance fpi = sjts.SJFieldGProtocolInstance((SJFieldInstance) fd.fieldInstance(), st, sjname);
			
			fpi.setConstantValue(fi.constantValue()); // Currently, constant checker not run on custom nodes/type objects. (Previously done by SJNoAliasTypeBuilder.)
			
			fd = fd.fieldInstance(fpi);
			fd = (FieldDecl) setSJGProtocolDeclExt((SJGProtocolDecl) fd, tn, sjname);			
			
			updateSJFieldInstance(fi, fpi);
		}
		
		return fd;
	}
	
	private Node buildLocalDecl(LocalDecl ld) throws SemanticException
	{	
		Type t = ld.declType();
		SJLocalInstance li = (SJLocalInstance) ld.localInstance();
		//SJNamedInstance ni = null; 
		
		if (t.isSubtype(SJ_GPROTOCOL_TYPE)) // Mostly the same as for LocalDecl.
		{
			if (!(ld instanceof SJLocalGProtocolDecl))
			{
				throw new SemanticException("[SJGProtocolDeclTypeBuilder] GProtocols may only be declared using the gprotocol keyword: " + ld);
			}
	
			SJTypeNode tn = disambiguateSJTypeNode(this, ((SJGProtocolDecl) ld).sessionType());
			SJSessionType st = tn.type();
			String sjname = ld.name(); // Should match that given by SJVariable.sjname.
			
			ld = ld.localInstance(sjts.SJLocalGProtocolInstance(li, st, sjname));
			ld = (LocalDecl) setSJGProtocolDeclExt((SJGProtocolDecl) ld, tn, sjname);
		}
		
		return ld;
	}
	
	private SJGProtocolDecl setSJGProtocolDeclExt(SJGProtocolDecl pd, SJTypeNode tn, String sjname) {
		pd = pd.sessionType(tn);
		pd = (SJGProtocolDecl) setSJNamedExt(sjef, pd, tn.type(), sjname);			
		
		return pd;
	}
	
	private Node buildSJSessionTypeCast(SJSessionTypeCast stc) throws SemanticException  
	{
		SJTypeNode tn = disambiguateSJTypeNode(this, stc.sessionType());
		SJSessionType st = tn.type();
		
		if (stc instanceof SJAmbiguousCast)
		{
			SJNoAliasExprExt naee = getSJNoAliasExprExt(stc);
			
			Position pos = stc.position();
			Expr e = stc.expr();
			
			if (st instanceof SJBeginType)
			{
				stc = sjnf.SJChannelCast(pos, e, tn);
			}
			else
			{
				stc = sjnf.SJSessionCast(pos, e, tn);
			}
			
			stc = (SJSessionTypeCast) buildAndCheckTypes(this, stc);
			stc = (SJSessionTypeCast) SJNoAliasExprBuilder.setSJNoAliasExprExt(sjef, stc, naee.isNoAlias(), naee.isFinal(), naee.fields(), naee.locals(), naee.arrayAccesses());
		}
		else
		{
			stc = stc.sessionType(tn);
		}
				
		stc = (SJSessionTypeCast) setSJTypeableExt(sjef, stc, st);
		
		return stc;
	}
	
	private Node buildFormal(Formal f) throws SemanticException // Based on buildLocalDecl.
	{
		Type t = f.declType();
		SJLocalInstance li = (SJLocalInstance) f.localInstance(); 
		
		if (t.isSubtype(SJ_ABSTRACT_CHANNEL_TYPE)) 
		{
			if (!(f instanceof SJFormal))
			{
				throw new SemanticException("[SJGProtocolDeclTypeBuilder] Session socket parameters should be declared by their session type: " + f);
			}
	
			SJTypeNode tn = disambiguateSJTypeNode(this, ((SJFormal) f).sessionType());
			SJSessionType st = tn.type();
			String sjname = f.name(); // Should match that given by SJVariable.sjname.
			
			f = f.localInstance(sjts.SJLocalGProtocolInstance(li, st, sjname));
			f = setSJFormalExt((SJFormal) f, tn, sjname);			
		}
		
		return f;
	}
	
	private Formal setSJFormalExt(SJFormal f, SJTypeNode tn, String sjname)  // Based on setSJGProtocolDeclExt
	{
		f = f.sessionType(tn);
		f = (SJFormal) setSJNamedExt(sjef, f, tn.type(), sjname);			
		
		return f;
	}	
}
//</By MQ>