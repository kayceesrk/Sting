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
import sessionj.ast.sessops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.extension.*;
import sessionj.types.*;
import sessionj.types.contexts.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.*;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 * @deprecated Divided into separate Protocol/Channel/SocketDecl type building passes.
 *
 */
public class SJCreateOperationTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	private SJBasicContext sjcontext = new SJBasicContext();
	
	/**
	 * 
	 */
	public SJCreateOperationTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException
	{
		if (n instanceof ClassDecl)
		{
			sjcontext.pushClassDecl();
		}
		else if (n instanceof Block)
		{
			sjcontext.pushBlock();
		}
		
		return this;
	}
	
	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassDecl || n instanceof Block)
		{
			sjcontext.pop();
		}
		
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
		else if (n instanceof SJCreateOperation)
		{
			n = buildSJCreateOperation((SJCreateOperation) n);
		}
		else if (n instanceof SJSessionOperation)
		{
			if (n instanceof SJBasicOperation)
			{
				//n = buildSJBasicOperation((SJBasicOperation) n);
			}
		}	
		
		return n;
	}
	
	private FieldDecl buildFieldDecl(FieldDecl fd) throws SemanticException
	{	
		Type t = fd.declType();
		
		if (t.isSubtype(SJ_PROTOCOL_TYPE))
		{
			if (!(fd instanceof SJFieldProtocolDecl))
			{
				throw new SemanticException("[SJCreateOperationTypeBuilder] Protocols may only be declared using the protocol keyword: " + fd);
			}

			fd = (SJFieldProtocolDecl) setSJProtocolDeclExt((SJFieldProtocolDecl) fd);
			
			SJFieldProtocolInstance fpi = sjts.SJFieldProtocolInstance((SJFieldInstance) fd.fieldInstance(), getSessionType(fd), getSJName(fd));
			
			fd = fd.fieldInstance(fpi);
			
			sjcontext.setProtocol(fpi);
		}
		//<By MQ> Added to support gprotocol
		else if (t.isSubtype(SJ_GPROTOCOL_TYPE))
		{
			if (!(fd instanceof SJFieldGProtocolDecl))
			{
				throw new SemanticException("[SJCreateOperationTypeBuilder] GProtocols may only be declared using the gprotocol keyword: " + fd);
			}

			fd = (SJFieldGProtocolDecl) setSJGProtocolDeclExt((SJFieldGProtocolDecl) fd);
			
			SJFieldGProtocolInstance fpi = sjts.SJFieldGProtocolInstance((SJFieldInstance) fd.fieldInstance(), getSessionType(fd), getSJName(fd));
			
			fd = fd.fieldInstance(fpi);
			
			sjcontext.setGProtocol(fpi);
		}
		//</By MQ>
		else if (t.isSubtype(SJ_CHANNEL_TYPE) || t.isSubtype(SJ_SOCKET_INTERFACE_TYPE)) // Make a common supertype?
		{
			throw new SemanticException("[SJCreateOperationTypeBuilder] Field type not supported: " + t);
		}
		
		return fd;
	}
	
	private LocalDecl buildLocalDecl(LocalDecl ld) throws SemanticException
	{	
		Type t = ld.declType();
		SJLocalInstance li = (SJLocalInstance) ld.localInstance();
		SJNamedInstance ni = null; 
		
		if (t.isSubtype(SJ_PROTOCOL_TYPE)) // Mostly the same as for LocalDecl.
		{
			if (!(ld instanceof SJLocalProtocolDecl))
			{
				throw new SemanticException("[SJCreateOperationTypeBuilder] Protocols may only be declared using the protocol keyword: " + ld);
			}
	
			ld = (SJLocalProtocolDecl) setSJProtocolDeclExt((SJLocalProtocolDecl) ld);
			
			ni = sjts.SJLocalProtocolInstance(li, getSessionType(ld), getSJName(ld));
			
			sjcontext.setProtocol(ni);			
		}
		//<By MQ> Added to support gprotocol
		else if (t.isSubtype(SJ_GPROTOCOL_TYPE)) // Mostly the same as for LocalDecl.
		{
			if (!(ld instanceof SJLocalGProtocolDecl))
			{
				throw new SemanticException("[SJCreateOperationTypeBuilder] GProtocols may only be declared using the gprotocol keyword: " + ld);
			}
	
			ld = (SJLocalGProtocolDecl) setSJGProtocolDeclExt((SJLocalGProtocolDecl) ld);
			
			ni = sjts.SJLocalGProtocolInstance(li, getSessionType(ld), getSJName(ld));
			
			sjcontext.setGProtocol(ni);			
		}
		//</By MQ>
		else if (t.isSubtype(SJ_CHANNEL_TYPE) || t.isSubtype(SJ_SOCKET_INTERFACE_TYPE)) 
		{
			Expr init = ld.init();
			SJSessionType st = null;
			String sjname = ld.name();
									
			if (init == null || init instanceof NullLit)
			{
				st = sjts.SJUnknownType();
			}
			else if (init instanceof SJChannelCreate || init instanceof SJSocketCreate)
			{			
 				st = getSessionType(init);
			}
			else
			{
				throw new SemanticException("[SJCreateOperationTypeBuilder] Unexpected channel variable initializer: " + init);
			}		
			
			ld = (LocalDecl) setSJNamedExt(sjef, ld, st, sjname);
			
			if (t.isSubtype(SJ_CHANNEL_TYPE))
			{
				ni = sjts.SJLocalChannelInstance(li, st, sjname);
				
				sjcontext.setChannel(ni);
			}
			else if (t.isSubtype(SJ_SOCKET_INTERFACE_TYPE))
			{
				ni = sjts.SJLocalSocketInstance(li, st, sjname);
				
				sjcontext.setSocket(ni);
			} 
		}		
		if (ni != null)
		{
			ld = ld.localInstance((LocalInstance) ni);
			
			System.out.println("b: " + ni.sjname() + " " + ni.sessionType());
		}
		
		return ld;
	}
	
	private SJCreateOperation buildSJCreateOperation(SJCreateOperation co) throws SemanticException
	{		
		NamedVariable nv = (NamedVariable) co.arguments().get(0); // Factor out constant.
		SJSessionType st = null;
		
		if (co instanceof SJChannelCreate)     //MQTODO: will gprotocol need something like this?
		{			
			st = sjcontext.findProtocol(nv.name()); // FIXME: should parse and create SJProtocolVariables.			
		}
		else //if (co instanceof SJSocketCreate)
		{
			st = sjcontext.findChannel(((SJChannelVariable) nv).sjname()); 
		}

		co = (SJCreateOperation) setSJTypeableExt(sjef, co, st);
		
		return co;
	}
	
	private SJProtocolDecl setSJProtocolDeclExt(SJProtocolDecl pd) throws SemanticException
	{
		SJTypeNode tn = disambiguateSJTypeNode(this, pd.sessionType());
		SJSessionType st = tn.type();
		String sjname; // Should match that given by SJVariable.sjname.
		
		if (pd instanceof FieldDecl) 
		{
			sjname = ((FieldDecl) pd).name();  
		}
		else //if (pd instanceof LocalDecl)
		{
			sjname = ((LocalDecl) pd).name();
		}
		
		pd = pd.sessionType(tn);
		pd = (SJProtocolDecl) setSJNamedExt(sjef, pd, st, sjname);			
		
		return pd;
	}

    //<By MQ> Added to support gprotocol definitions
            private SJGProtocolDecl setSJGProtocolDeclExt(SJGProtocolDecl pd) throws SemanticException
	    {
		SJTypeNode tn = disambiguateSJTypeNode(this, pd.sessionType());
		SJSessionType st = tn.type();
		String sjname; // Should match that given by SJVariable.sjname.
		
		if (pd instanceof FieldDecl) 
		{
			sjname = ((FieldDecl) pd).name();  
		}
		else //if (pd instanceof LocalDecl)
		{
			sjname = ((LocalDecl) pd).name();
		}
		
		pd = pd.sessionType(tn);
		pd = (SJGProtocolDecl) setSJNamedExt(sjef, pd, st, sjname);			
		
		return pd;
	    } 
    //</By MQ>

    public static String getSJName(Node n)
    {
        return getSJNamedExt(n).sjname();
    }
}

/**
 * 
 * @author Raymond
 *
 * @deprecated Only needed by SJBasicContext, which is also deprecated.
 *
 */
class SJBasicContextElement extends SJContextElement_c // The basic version is actually adding more...
{
	private HashMap<String, SJNamedInstance> protocols = new HashMap<String, SJNamedInstance>();
        private HashMap<String, SJNamedInstance> gprotocols = new HashMap<String, SJNamedInstance>(); //<By MQ> list of gprotocols
	
	public SJBasicContextElement()
	{

	}
	
	public SJBasicContextElement(SJContextElement ce)
	{
		super(ce);
		
		this.protocols.putAll(((SJBasicContextElement) ce).protocols);		
		this.gprotocols.putAll(((SJBasicContextElement) ce).gprotocols);		//<By MQ> added to support gprotocols
	}	
	
	public SJNamedInstance getProtocol(String sjname)
	{
		return protocols.get(sjname); 
	}
	
	public void setProtocol(SJNamedInstance ni)
	{
		protocols.put(ni.sjname(), ni);
	}
	
	public boolean hasProtocol(String sjname)
	{
		return protocols.keySet().contains(sjname); 
	}
    //<By MQ> methods for supporting gprotocols
        public SJNamedInstance getGProtocol(String sjname)
	{
		return gprotocols.get(sjname); 
	}
	
	public void setGProtocol(SJNamedInstance ni)
	{
		gprotocols.put(ni.sjname(), ni);
	}
	
	public boolean hasGProtocol(String sjname)
	{
		return gprotocols.keySet().contains(sjname); 
	}
    //</By MQ>
}

/**
 * 
 * @author Raymond
 *
 * @deprecated Only needed by SJCreateOperationTypeBuilder, which is also deprecated. 
 */
class SJBasicContext // A primitive version of SJContext. // Unify?
{
	private Stack<SJBasicContextElement> contexts = new Stack<SJBasicContextElement>();
	
	public SJBasicContextElement currentContext()
	{
		return contexts.peek();
	}			
	
	public SJSessionType findProtocol(String sjname) throws SemanticException
	{
		SJBasicContextElement ce = currentContext();
		
		if (!ce.hasProtocol(sjname))
		{
			throw new SemanticException("[SJBasicContext_c] Protocol not in context: " + sjname);
		}
		
		return ce.getProtocol(sjname).sessionType();
	}
    //<By MQ> Added to support gprotocols
	public SJSessionType findGProtocol(String sjname) throws SemanticException
	{
		SJBasicContextElement ce = currentContext();
		
		if (!ce.hasGProtocol(sjname))
		{
			throw new SemanticException("[SJBasicContext_c] GProtocol not in context: " + sjname);
		}
		
		return ce.getGProtocol(sjname).sessionType();
	}
    
    //</By MQ>
	public SJSessionType findChannel(String sjname) throws SemanticException
	{
		SJBasicContextElement ce = currentContext();
		
		if (!ce.hasChannel(sjname))
		{
			throw new SemanticException("[SJBasicContext_c] Channel not in context: " + sjname);
		}
		
		return ce.getChannel(sjname).sessionType();
	}

	public void setGProtocol(SJNamedInstance ni)
	{
		currentContext().setGProtocol(ni); 
	}

    //</By MQ>		
	public SJSessionType findSocket(String sjname) throws SemanticException
	{
		SJBasicContextElement ce = currentContext();
		
		if (!ce.hasSocket(sjname))
		{
			throw new SemanticException("[SJBasicContext_c] Socket not in context: " + sjname);
		}
		
		return ce.getSocket(sjname).sessionType();
	}
	
	public void setProtocol(SJNamedInstance ni)
	{
		currentContext().setProtocol(ni); 
	}
    	
	public void setChannel(SJNamedInstance ni)
	{
		currentContext().setChannel(ni); 
	}
		
	public void setSocket(SJNamedInstance ni)
	{
		currentContext().setSocket(ni); 
	}	
	
	public void pushClassDecl()
	{
		SJBasicContextElement ce;
		
		if (contexts.isEmpty())
		{
			ce = new SJBasicContextElement();
		}
		else
		{
			ce = new SJBasicContextElement(currentContext());
		}
		
		contexts.push(ce);
	}
	
	public void pushBlock()
	{
		contexts.push(new SJBasicContextElement(currentContext()));
	}
	
	public void pop()
	{
		SJBasicContextElement ce = contexts.pop();
		
		if (!contexts.isEmpty())
		{
			SJBasicContextElement current = currentContext();
			
			// Protocol and channel types do not change.
			
			for (String sjname : current.socketSet())
			{				
				if (ce.hasSocket(sjname))
				{
					current.setSocket(ce.getSocket(sjname)); // Replaces existing entry.
				}
			}			
		}
	}	
}
