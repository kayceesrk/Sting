package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.*;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.visit.ErrorHandlingVisitor;
import polyglot.visit.NodeVisitor;

import sessionj.ast.*;
import sessionj.ast.createops.*;
import sessionj.ast.chanops.*;
import sessionj.ast.sessformals.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessvars.*;
import sessionj.extension.*;
import sessionj.types.SJTypeSystem;
import sessionj.types.typeobjects.SJParsedClassType;

import static sessionj.SJConstants.*;
import static sessionj.util.SJCompilerUtils.*;

/**
 * 
 * @author Raymond
 *
 * Similar to SJSessionOperationParser. SJSpawn operations parsed by SJSessionOperationParser. 
 * 
 * FIXME: these kind of "parsing" (translation) passes need to be moved before base type checking - cyclic compilation dependencies become a problem otherwise.
 * 
 */
public class SJThreadPreParser extends ErrorHandlingVisitor
{
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	//private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();

	
	public SJThreadPreParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}
	
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassDecl)
		{	
			n = parseClassDecl((ClassDecl) n);
		}
		/*else if (n instanceof New)
		{
			New w = (New) n;
			
			if (w.body() != null && w.anonType().isSubtype(SJ_THREAD_TYPE))
			{
				//n = w.body(parseClassBody(w.body()/*, w.anonType()*));
				
				throw new SemanticException("[SJThreadParser] Anonymous inner SJThread subclasses not supported yet: " + w);
			}
		}*/

		return n;
	}	
	
	private ClassDecl parseClassDecl(ClassDecl cd) throws SemanticException // To difficult to do anything before type checking, not enough information (e.g. superclasses, etc.) is available.
	{
		ParsedClassType pct = cd.type(); // SJParsedClassType?
		
		/*if (pct.isSubtype(SJ_THREAD_TYPE))
		{	
			cd = cd.body(parseClassBody((ClassBody) cd.body()/*, pct*));
		}*/
		
		return cd;
	}
	
	/*private ClassBody parseClassBody(ClassBody body/*, ParsedClassType pct) throws SemanticException
	{
		List members = body.members();
		
		boolean runFound = false;
		
		for (Iterator i = members.iterator(); i.hasNext(); )
		{
			ClassMember cm = (ClassMember) i.next();
			
			if (cm instanceof MethodDecl)
			{
				MethodDecl md = (MethodDecl) cm;
				
				if (md.name().equals(SJ_THREAD_RUN))
				{
					if (runFound)
					{
						throw new SemanticException("[SJThreadParser] Multiple run methods not permitted.");
					}
					
					runFound = true;				
				}
			}
		}
		
		if (!runFound)
		{
			throw new SemanticException("[SJThreadParser] No srun method found.");
		}
		
		/*if (spawns.isEmpty()) // Redundant after the above.
		{
			throw new SemanticException("[SJThreadParser] No run method found.");
		}
		
		MethodDecl md = spawns.pop();
		
		List<ClassMember> membs = new LinkedList<ClassMember>();
		
		membs.addAll(body.members());
		membs.add(md);
		
		body = body.members(membs);
		
		//pct.addMethod(md.methodInstance()); // Doesn't seem to be needed. Type checking the method seems to have already updated the SJThread class type.
		
		return body;
	}*/
}
