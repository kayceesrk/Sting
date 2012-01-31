/**
 * 
 */
package sessionj.visit.noalias;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessvars.SJLocalSocket;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.noalias.SJNoAliasFinalReferenceType;
import sessionj.types.typeobjects.*;
import sessionj.util.noalias.SJNoAliasVariableRenamer;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 * 
 * FIXME: translation for Conditionals is too conservative.
 *
 */
public class SJNoAliasTranslator extends ContextVisitor
{
	SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	
	//private Stack<Block> code = new Stack<Block>();	
	private Stack<HashMap<Stmt, TranslatedStmt>> replacements = new Stack<HashMap<Stmt, TranslatedStmt>>(); 
	
	/**
	 * @param job
	 * @param ts
	 * @param nf
	 */
	public SJNoAliasTranslator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	public SJNoAliasTranslator enterCall(Node parent, Node n) throws SemanticException
	{
		if (n instanceof Block)
		{
			//code.push((Block) n); // No simple way to do this (no common class).
			
			replacements.push(new HashMap<Stmt, TranslatedStmt>());
		}
		
		// FIXME: doesn't handle singly nested statements? e.g. while (...) while (...) { ... }
		
		return this;
	}
	
	public Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if (n instanceof Block)
		{					
			HashMap<Stmt, TranslatedStmt> stmts = replacements.pop();
			
			for (Stmt s1 : stmts.keySet())
			{
				TranslatedStmt ts = stmts.get(s1);
				Stmt replace = ts.getReplace();
				Stmt insert = ts.getInsert(); 
				
				if (insert == null)
				{
					n = replaceStmt((Block) n, s1, replace);
				}
				else
				{
					n = insertStmtAfterStmt((Block) n, s1, replace, insert);
				}
			}						
		}	
		else if (n instanceof VarInit) 
		{
            // No translation needed (or possible) for FieldDecls.
			if (n instanceof LocalDecl)
			{
				n = translateLocalDecl((LocalDecl) n);
			}
		}
		else if (n instanceof Assign)
		{
			n = translateAssign(parent, (Assign) n);
		}
		else if (n instanceof ProcedureCall)
		{
			if (n instanceof ConstructorCall)
			{
				// No translation needed: cannot pass noalias fields in a ConstructorCall, and locals not possible (this/super must be first statement in a ConstructorDecl).
			}
			else //if (n instanceof Call || n instanceof New)
			{		
				n = translateProcedureCall(parent, (ProcedureCall) n);
			}
		}
		else if (n instanceof Return)
		{
			n = translateReturn((Return) n);
		}
		
		return n;
	}
	
	private Node translateLocalDecl(LocalDecl ld) throws SemanticException
	{					
		if (ld.declType().isPrimitive())
		{
			return ld;
		}
		
		Expr init = ld.init();		

		if (init == null)
		{
			return ld;
		}
		
		if (isNoAlias(init))
		{		 									
			if (!supportedInit(init))
			{
				throw new SemanticException("[SJNoAliasTranslator] noalias translation of LocalDecl not yet supported for initialisation by: " + init);
			}
		}

		Set<Variable> vars = removeFinalVariables(setifyVariables(getSJNoAliasVariablesExt(init)));

		if (!vars.isEmpty())
		{
			QQ qq = new QQ(sjts.extensionInfo(), ld.position());
			Collection<Object> mapping = new LinkedList<Object>();			

			String translation = "{ ";

			translation += nullOutTheVariables(qq, vars, mapping);

			translation += "%s = %E; ";

			mapping.add(ld.name());
			mapping.add(renameNoAliasVariables(init, vars));

			translation += "}";

			Stmt s = qq.parseStmt(translation, mapping.toArray());
			s = (Stmt) buildAndCheckTypes(this, s);

			LocalDecl foo = ld.init(null);
			foo = foo.type(ld.type());
			foo = foo.localInstance(ld.localInstance());

			//ld = (LocalDecl) insertStmtAfterStmt(ld, foo, s);

			replacements.peek().put(ld, new TranslatedStmt(foo, s));
		}

		return ld;
	}

    private boolean supportedInit(Object init) {
        return init instanceof Variable || init instanceof ProcedureCall || init instanceof Lit
            || init instanceof Assign || init instanceof Conditional || init instanceof Cast
            || init instanceof NewArray || init instanceof Binary;
    }

	private Node translateAssign(Node parent, Assign a) throws SemanticException
	{
		if (a.type().isPrimitive())
		{
			return a;
		}		
		
		Variable left = (Variable) a.left();
		Expr right = a.right();
				
		if (parent instanceof Eval)
		{			
			if (isNoAlias(right))
			{		 									
				if (!supportedInit(right))
				{	
					throw new SemanticException("[SJNoAliasTranslator] noalias translation not yet supported for assign of: " + right);
				}								
			}					
			
			Set<Variable> vars = removeFinalVariables(setifyVariables(getSJNoAliasVariablesExt(right)));		
			
			if (!vars.isEmpty()) 
			{		
				QQ qq = new QQ(sjts.extensionInfo(), a.position());		 
				List<Object> mapping = new LinkedList<Object>();			
				
				String translation = "{ ";
				
				translation += nullOutTheVariables(qq, vars, mapping); 
				
				translation += "%E = %E; "; 
				
				mapping.add(left);
				mapping.add(renameNoAliasVariables(right, vars));
				
				translation += "}";
				
				Stmt s = qq.parseStmt(translation, mapping.toArray());
				
				s = (Stmt) buildAndCheckTypes(this, s);
				//s = replaceStmt((Eval) parent, s);
				
				replacements.peek().put((Eval) parent, new TranslatedStmt(s));
			}
		}
		
		return a;
	}

	private ProcedureCall translateProcedureCall(Node parent, ProcedureCall pc) throws SemanticException
	{				
		if (parent instanceof Eval) // OK because e.g. (T) a.m(...); is not a permitted statement (similarly for Conditional, etc.). 
		{		
			Set<Variable> vars = removeFinalParameters(pc, removeFinalVariables(setifyVariables(getSJNoAliasVariablesExt(pc)))); 				
			
			if (!vars.isEmpty())
			{				
				QQ qq = new QQ(sjts.extensionInfo(), pc.position());
				
				String translation = "{ ";
				List<Object> mapping = new LinkedList<Object>();			
				
				translation += nullOutTheVariables(qq, vars, mapping);
				
				translation += "%E; ";					
				mapping.add(renameNoAliasVariables((Expr) pc, vars));					
				
				translation += "}";
				Stmt s = qq.parseStmt(translation, mapping.toArray());			
				
				s = (Stmt) buildAndCheckTypes(this, s);
				//s = replaceStmt((Eval) parent, s);
				
				replacements.peek().put((Stmt) parent, new TranslatedStmt(s));
			}
		}
		
		return pc;
	}
	
	private Node translateReturn(Return r) throws SemanticException
	{		
		Expr e = r.expr();
		
		if (e == null || e.type().isPrimitive())
		{
			return r;
		}
		
		//Set<Variable> vars = removeFinalVariables(setifyVariables(getSJNoAliasVariablesExt(r), true)); // Locals don't matter. // Wrong: locals can still be accessed after a return from a finally block, e.g. session sockets (don't want to close returned a session that was established within the method - let it become null).
		Set<Variable> vars = removeFinalVariables(setifyVariables(getSJNoAliasVariablesExt(r), false));
		
		if (!vars.isEmpty()) // Mostly the same as for Assign and ProcedureCall.
		{		
			QQ qq = new QQ(sjts.extensionInfo(), r.position());		 
			LinkedList<Object> mapping = new LinkedList<Object>();			
			
			String translation = "{ ";
			
			translation += nullOutTheVariables(qq, vars, mapping); 
			
			translation += "return %E; "; // Apart from here, the same as for Assign and ProcedureCall.
			
			mapping.add(renameNoAliasVariables(r.expr(), vars));
			
			translation += "}";
			
			Stmt s = qq.parseStmt(translation, mapping.toArray());
			
			s = (Stmt) buildAndCheckTypes(this, s);
			//s = replaceStmt(r, s);
			
			replacements.peek().put(r, new TranslatedStmt(s));
		}	
		
		return r;
	}
	
	private Expr renameNoAliasVariables(Expr e, Set<Variable> vars)
	{
		SJNoAliasVariableRenamer navr = new SJNoAliasVariableRenamer(job, this, vars);		
			
		e = (Expr) e.visit(navr);	
		
		return e;
	}
	
	private String nullOutTheVariables(QQ qq, Iterable<Variable> vars, Collection<Object> mapping) throws SemanticException
	{								
		StringBuilder translation = new StringBuilder();

        for (Variable v : vars)
		{						
			Type t = v.type();
			String tname;
			
			if (t instanceof ArrayType)
			{
				tname = ((Named) ((ArrayType) t).base()).fullName();
				
				if (!(v instanceof ArrayAccess))
				{
					tname += "[]";
				}						
			}
			else
			{
				tname = ((Named) t).fullName();
			}
			
			TypeNode tn = qq.parseType(tname);			 			
			tn = (TypeNode) buildAndCheckTypes(this, tn);
			
			String vname = SJNoAliasVariableRenamer.renameNoAliasVariable(v);
			
			translation.append("%T %s = null; ");
			mapping.add(tn);
			mapping.add(vname);
		}

        int fields = 0;
        for (Variable v : vars)
		{
			if (v instanceof Field || v instanceof ArrayAccess && ((ArrayAccess) v).array() instanceof Field)
			{				
				translation.append("if (%E != null) { synchronized (%E) { ");
				mapping.add(v);
				mapping.add(v);
				
				fields++;
			}
		}
		
		for (Variable v : vars)
		{								
			translation.append("%s = %E; ");
			mapping.add(SJNoAliasVariableRenamer.renameNoAliasVariable(v));
			mapping.add(v);
		}
		
		for (Variable v : vars)
		{
			translation.append("%E = ");
			mapping.add(v);
		}
		
		translation.append("null; ");

        while (fields > 0) {
			translation.append("} } ");
            fields--;
        }

        return translation.toString();
	}
	
	private Set<Variable> removeFinalParameters(ProcedureCall pc, Set<Variable> vars) throws SemanticException
	{
		ProcedureInstance pi = pc.procedureInstance();

		List<Variable> foo = new LinkedList<Variable>();
		
		if (pi instanceof SJProcedureInstance)
		{						
			Iterator i = pc.arguments().iterator();
			
			for (Type t : ((SJProcedureInstance) pi).noAliasFormalTypes())
			{
				Expr arg = (Expr) i.next();
				
				if (pc instanceof SJSpawn) // The generated target (spawn method) has implicitly final parameters, but spawn is a "delegation" operation.
				{
					if (!(arg instanceof SJLocalSocket))
					{
						if ((t instanceof SJNoAliasFinalReferenceType) && ((SJNoAliasFinalReferenceType) t).isFinal())
						{
							findVariableArguments(arg, foo);
						}
					}
				}
				else
				{
					if ((t instanceof SJNoAliasFinalReferenceType) && ((SJNoAliasFinalReferenceType) t).isFinal())
					{
						findVariableArguments(arg, foo);
					}
				}
			}
		}
		else if (pc instanceof SJCopy) // To support SJ libraries implemented in regular Java.
		{
			Expr arg = (Expr) ((SJCopy) pc).arguments().get(1);
			
			if (arg instanceof Variable)
			{
				foo.add((Variable) arg); // Factor out constant.
			}
			else if (!(arg instanceof Lit))
			{
				throw new RuntimeException("[SJNoAliasTranslator] Shouldn't get here: " + arg);
			}
		}

		//vars.removeAll(foo); // Pointer equality for the locals has been broken somewhere between SJNoAliasExprBuilder and here. But better to use the type object comparison anyway.
		
		for (Variable v1 : foo)
		{
			Variable remove = null;
			
			for (Variable v2 : vars)
			{
				if (((NamedVariable) v1).varInstance().equals(((NamedVariable) v2).varInstance()))
				{
					remove = v2;
					
					break;
				}
			}
			
			if (remove != null) 
			{
				vars.remove(remove);
			}
		}
		
		return vars;
	}
	
	private void findVariableArguments(Expr arg, List<Variable> vars) throws SemanticException
	{
		if (arg instanceof Variable)
		{
			vars.add((Variable) arg);
		}
		else if (arg instanceof Assign)
		{
			findVariableArguments(((Assign) arg).left(), vars);
		}
		else if (arg instanceof Conditional)
		{
			findVariableArguments(((Conditional) arg).consequent(), vars);
			findVariableArguments(((Conditional) arg).alternative(), vars);
		}
		else if (arg instanceof Cast)
		{
			findVariableArguments(((Cast) arg).expr(), vars); 
		}
		else if (!(arg instanceof ProcedureCall || arg instanceof Special || arg instanceof Lit || arg instanceof Binary || arg instanceof Unary))
		{
			throw new SemanticException("[SJNoAliasTranslator] Argument expression not yet supported: " + arg);
		}
    }
	
	private Set<Variable> removeFinalVariables(Set<Variable> vars)
	{
		Set<Variable> foo = new HashSet<Variable>();
		
		for (Variable v : vars)
		{
			if (!v.flags().isFinal()) // na-final variables don't need translating: cannot assign from and can only be passed as na-final parameters.
			{
				foo.add(v);
			}
		}
		
		return foo;
	}
	
	private Set<Variable> setifyVariables(SJNoAliasVariablesExt nave)
	{
		return setifyVariables(nave, false);
	}
	
	private Set<Variable> setifyVariables(SJNoAliasVariablesExt nave, boolean noLocals)
	{
		Set<Variable> vars = new HashSet<Variable>();				
		Set<String> seen = new HashSet<String>(); // Should instead use VarInstance type objects?
		
		for(Field f : nave.fields())
		{
			String fname = f.toString();
			
			if (!(f.type().isPrimitive() || seen.contains(fname))) 
			{
				vars.add(f);
				seen.add(fname);
			}
		}
		
		if (!noLocals)
		{
			for(Local l : nave.locals())
			{
				String lname = l.toString();
				
				if (!(l.type().isPrimitive() || seen.contains(lname)))
				{
					vars.add(l);
					seen.add(lname);
				}
			}
		}
		
		for(ArrayAccess aa : nave.arrayAccesses())
		{
			if (!noLocals || aa.array() instanceof Field)
			{
				String aaname = aa.toString();
				
				if (!(aa.type().isPrimitive() || seen.contains(aaname)))
				{
					vars.add(aa);
					seen.add(aaname);
				}
			}
		}
		
		return vars;
	}
	
	private Stmt replaceStmt(Block b, Stmt s1, Stmt s2)
	{
		return insertStmtAfterStmt(b, s1, s2, null);
	}
	
	// Replaces s1 with s2, and inserts s3 after if s3 is not null.
	private Stmt insertStmtAfterStmt(Block b, Stmt s1, Stmt s2, Stmt s3) 
	{		
		//Block b = code.pop();
		List<Stmt> ss = new LinkedList<Stmt>();

        for (Object o : b.statements()) {
            Stmt s = (Stmt) o;

            if (s == s1) {
                ss.add(s2);

                if (s3 != null) {
                    ss.add(s3);
                }
            } else {
                ss.add(s);
            }
        }
				
		//b = sjnf.Block(b.position(), ss); // ERROR: wrong to force all Block subtypes to become exactly a Block (e.g. SJWhen).
     b = b.statements(ss);
		//b = (Block) buildAndCheckTypes(job(), this, b); // Not needed, types already built for newly inserted Stmts.
		
		return b;
	}
}

class TranslatedStmt
{
	private Stmt replace;
	private Stmt insert;

	TranslatedStmt(Stmt replace)
	{
		this.replace = replace;
	}
	
	TranslatedStmt(Stmt replace, Stmt insert)
	{
		this.replace = replace;
		this.insert = insert;
	}
	
	public Stmt getReplace()
	{
		return replace;
	}
	
	public Stmt getInsert()
	{
		return insert;
	}	
}
