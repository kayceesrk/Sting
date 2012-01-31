package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.*;
import polyglot.types.*;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

import sessionj.ast.*;
import sessionj.ast.sessformals.*;
import sessionj.ast.sessvars.*;
import sessionj.types.SJTypeSystem;

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
public class SJThreadParser extends ContextVisitor
{
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	//private SJExtFactory sjef = ((SJNodeFactory) nodeFactory()).extFactory();
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();

	private Stack<MethodDecl> spawns = new Stack<MethodDecl>(); // This way still does not work for anonymous inner SJSpawn subclasses. Polyglot bug?
	
	public SJThreadParser(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}
	
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassDecl)
		{	
			n = parseClassDecl((ClassDecl) n);
		}
		else if (n instanceof New)
		{
			New w = (New) n;
			
			if (w.body() != null && w.anonType().isSubtype(SJ_THREAD_TYPE))
			{
				//n = w.body(parseClassBody(w.body()/*, w.anonType()*/));
				
				throw new SemanticException("[SJThreadParser] Anonymous inner SJThread subclasses not supported yet: " + w);
			}
		}
		else if (n instanceof MethodDecl)
		{
			n = parseMethodDecl(parent, (MethodDecl) n);
		}

		return n;
	}	
	
	private ClassDecl parseClassDecl(ClassDecl cd) throws SemanticException
	{
		ParsedClassType pct = cd.type(); // SJParsedClassType?
		
		if (pct.isSubtype(SJ_THREAD_TYPE))
		{	
			cd = cd.body(parseClassBody((ClassBody) cd.body()/*, pct*/));
		}
		
		return cd;
	}
	
	private ClassBody parseClassBody(ClassBody body/*, ParsedClassType pct*/) throws SemanticException
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
		}*/
		
		MethodDecl md = spawns.pop();
		
		List<ClassMember> membs = new LinkedList<ClassMember>();
		
		membs.addAll(body.members());
		membs.add(md);
		
		body = body.members(membs);
		
		//pct.addMethod(md.methodInstance()); // Doesn't seem to be needed. Type checking the method seems to have already updated the SJThread class type.
		
		return body;
	}
	
	private MethodDecl parseMethodDecl(Node parent, MethodDecl md) throws SemanticException
	{
		ReferenceType rt = md.methodInstance().container();
		
		if (rt.isSubtype(SJ_THREAD_TYPE))
		{
			if (md.name().equals(SJ_THREAD_RUN))
			{
				List<SJFormal> fs = new LinkedList<SJFormal>();

                for (Object o : md.formals()) {
                    Formal f = (Formal) o;

                    if (f instanceof SJFormal && ((SJFormal) f).isSharedChannel()) {
                        if (!f.flags().isFinal()) // Currently, channels must be na-final.
                        {
                            throw new SemanticException("[SJThreadParser] SJThread srun channel parameters must be na-final: " + f);
                        }
                    } else if (f instanceof SJFormal && ((SJFormal) f).isSession()) // No way for spawn to pass non-session arguments to the target method.
                    {
                        if (f.flags().isFinal()) // To ensure the sessions are closed by SJSessionTryTranslator, although we could allow final parameters as an exception in this case.
                        {
                            throw new SemanticException("[SJThreadParser] SJThread srun session parameters cannot be final: " + f);
                        }
                    } else {
                        throw new SemanticException("[SJThreadParser] Only channel and session socket parameters permitted for session thread run method: " + f);
                    }

                    fs.add((SJFormal) f.flags(f.flags().Final())); // Returns a copy of the original Formal?
                }
				
				//Position pos = ((ClassMember) members.get(members.size() - 1)).position();
				Position pos = Position.compilerGenerated();
				QQ qq = new QQ(sjts.extensionInfo(), pos);
				
				String translation = "";
				List<Object> mapping = new LinkedList<Object>();			

				translation += "{";
				
				/*translation += 	"final %T %s = this;"; // Needed if SJ_THREAD_RUN is "run", but doesn't work for anonymous inner classes.
				
				mapping.add(sjnf.CanonicalTypeNode(pos, pct));
				mapping.add(SJ_THREAD_THIS);*/
				
				translation += 	"new Thread()"; // FIXME: if the user wants to do any thread related operations, we can keep a reference to this Thread subclass in the SJThread and provide an object delegation interface. 
				translation += 	"{";
				translation += 		"public void run()";
				translation += 		"{";
				
				/*translation += 		" %E." + SJ_THREAD_RUN + "("; // See above comment.
				mapping.add(sjnf.Local(pos, sjnf.Id(pos, SJ_THREAD_THIS)));*/
				
				translation += 		SJ_THREAD_RUN + "(";
				
				for (SJFormal f : fs)
				{		
					SJVariable v;
					
					if (f.isSharedChannel())
					{
						v = (SJLocalChannel) sjnf.SJLocalChannel(pos, f.id()).localInstance(f.localInstance());
					}
					else
					{
						v = (SJLocalSocket) sjnf.SJLocalSocket(pos, f.id()).localInstance(f.localInstance());
					}
					
					translation += 		"%E, ";				
					mapping.add(v);
				}
			
				translation = translation.substring(0, translation.length() - 2);
				
				translation += 		");";
				translation += 		"}";
				translation += 	"}.start();";
				
				translation += "return this;";
				
				translation += "}";
				
				Block b = (Block) qq.parseStmt(translation, mapping);
				
				//MethodDecl spawn = sjnf.MethodDecl(pos, Flags.PUBLIC, sjnf.CanonicalTypeNode(pos, sjts.Void()), sjnf.Id(pos, SJ_THREAD_SPAWN), fs, Collections.EMPTY_LIST, b);
				MethodDecl spawn = sjnf.MethodDecl(pos, Flags.PUBLIC, sjnf.CanonicalTypeNode(pos, SJ_THREAD_TYPE), sjnf.Id(pos, SJ_THREAD_SPAWN), fs, Collections.EMPTY_LIST, b);
				
				/*Context c = this.context().pushClass((SJParsedClassType) rt, (ClassType) rt); // Because we're in the leaveCall, the context for the ClassDecl we're leaving has already been popped?
				//Context c = this.context().pushClass(pct, ts.staticTarget(type).toClass()); // Do we need to do it like this? (Copied from ClassDecl_c, although it's not done like this for anonymous inner classes - see New_c.)
				
				spawn = (MethodDecl) buildAndCheckTypes(job(), this.context(c), md2); // Seems to correctly update the container thread class type as well (i.e. records the new spawn method).*/
				
				spawn = (MethodDecl) buildAndCheckTypes(this, spawn);				
				
				spawns.push(spawn);
			}
		}
		
		return md;
	}
	
	/*//Doesn't work for anonymous inner SJThread subclasses. But the new way still does not work. This one might be better.
	private ClassBody parseClassBody(ClassBody body, ParsedClassType pct) throws SemanticException
	{						
		List members = body.members();
		
		List runFormals = null;
		
		for (Iterator i = members.iterator(); i.hasNext(); )
		{
			ClassMember cm = (ClassMember) i.next();
			
			if (cm instanceof MethodDecl)
			{
				MethodDecl md = (MethodDecl) cm;
				
				if (md.name().equals(SJ_THREAD_RUN))
				{
					if (runFormals != null)
					{
						throw new SemanticException("[SJThreadParser] Multiple run methods not permitted: " + runFormals);
					}
					
					runFormals = md.formals();				
				}
			}
		}
		
		if (runFormals == null)
		{
			throw new SemanticException("[SJThreadParser] No srun method found.");
		}
		
		/*for (SJFormal f : (SJFormal) funFormals)
		{
			// Only allow session parameters to the run method?
		}*
		
		List<SJFormal> fs = new LinkedList<SJFormal>();
		
		for (Iterator i = runFormals.iterator(); i.hasNext(); )
		{
			Formal f = (Formal) i.next();
			
			if (!(f instanceof SJFormal)) // No way for spawn to pass non-session arguments to the target method.
			{
				throw new SemanticException("[SJThreadParser] Only session socket parameters permitted for session thread run method: " + f);
			}
			
			if (f.flags().isFinal()) // To ensure the sessions are closed by SJSessionTryTranslator, although we could allow final parameters as an exception in this case. 
			{
				throw new SemanticException("[SJThreadParser] Session thread parameters cannot be final: " + f);
			}
			
			fs.add((SJFormal) f.flags(f.flags().Final())); // Returns a copy of the original Formal?
		}
		
		//Position pos = ((ClassMember) members.get(members.size() - 1)).position();
		Position pos = Position.compilerGenerated();
		QQ qq = new QQ(sjts.extensionInfo(), pos);
		
		String translation = "";
		List<Object> mapping = new LinkedList<Object>();			

		translation += "{";
		
		/*translation += 	"final %T %s = this;"; // Needed if SJ_THREAD_RUN is "run", but doesn't work for anonymous inner classes.
		
		mapping.add(sjnf.CanonicalTypeNode(pos, pct));
		mapping.add(SJ_THREAD_THIS);*
		
		translation += 	"new Thread()";
		translation += 	"{";
		translation += 		"public void run()";
		translation += 		"{";
		
		/*translation += 		" %E." + SJ_THREAD_RUN + "("; // See above comment.
		mapping.add(sjnf.Local(pos, sjnf.Id(pos, SJ_THREAD_THIS)));*
		
		translation += 		SJ_THREAD_RUN + "(";
		
		for (SJFormal f : fs)
		{				
			SJLocalSocket s = (SJLocalSocket) sjnf.SJLocalSocket(pos, f.id()).localInstance(f.localInstance());
			
			translation += 		"%E, ";				
			mapping.add(s);
		}
	
		translation = translation.substring(0, translation.length() - 2);
		
		translation += 		");";
		translation += 		"}";
		translation += 	"}.start();";
		translation += "}";
		
		Block b = (Block) qq.parseStmt(translation, mapping);
		
		MethodDecl md = sjnf.MethodDecl(pos, Flags.PUBLIC, sjnf.CanonicalTypeNode(pos, sjts.Void()), sjnf.Id(pos, SJ_THREAD_SPAWN), fs, Collections.EMPTY_LIST, b);
		
		Context c = this.context().pushClass(pct, pct); // Because we're in the leaveCall, the context for the ClassDecl we're leaving has already been popped?
		// Context c = this.context().pushClass(pct, ts.staticTarget(type).toClass()); // Do we need to do it like this? (Copied from ClassDecl_c, although it's not done like this for anonymous inner classes - see New_c.)
		
		md = (MethodDecl) buildAndCheckTypes(job(), this.context(c), md); // Seems to correctly update the container thread class type as well (i.e. records the new spawn method).
		//md = (MethodDecl) buildAndCheckTypes(job(), this, md);
		
		List<ClassMember> membs = new LinkedList<ClassMember>();
		
		membs.addAll(body.members());
		membs.add(md);
		
		body = body.members(membs);
		
		return body;
	}*/
}
