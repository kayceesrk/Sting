/**
 * 
 */
package sessionj.visit.noalias;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.types.*;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.chanops.SJRequest;
import sessionj.ast.createops.SJCreateOperation;
import sessionj.ast.servops.SJAccept;
import sessionj.ast.selectorops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessvars.SJLocalSocket;
import sessionj.extension.SJExtFactory;
import sessionj.extension.noalias.SJNoAliasExprExt;
import sessionj.extension.noalias.SJNoAliasVariablesExt;
import sessionj.types.SJTypeSystem;
import sessionj.types.noalias.SJNoAliasReferenceType;
import sessionj.types.typeobjects.*;
import static sessionj.util.SJCompilerUtils.*;

import java.util.*;

/**
 * @author Raymond
 *
 */
public class SJNoAliasExprBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
    private static final boolean DEBUG = false;
	
	public SJNoAliasExprBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}
    
    private static void debug(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

	// This pass is for checks that require field and method declarations from all classes to be already checked.
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof Expr)
		{			
			if (n instanceof Variable)
			{
				if (n instanceof NamedVariable)
				{
					if (n instanceof Field) 
					{
						n = buildField((Field) n);
					}
					else //if (n instanceof Local)
					{
						n = buildLocal((Local) n);
					}
				}
				else if (n instanceof ArrayAccess)
				{
					n = buildArrayAccess((ArrayAccess) n);
				}
			}
			else if (n instanceof ProcedureCall)
			{
				if (n instanceof New)  
				{
					n = buildNew((New) n);
				}
				else //if (n instanceof Call)
				{					
					// Basically, checking here for SessionSocketCreator operations - need to ensure they have noalias return types.
					if (n instanceof SJCreateOperation) // Do here, or in SJCreateOperationParser?
					{
						n = buildSJCreateOperation((SJCreateOperation) n);
					}
					else if (n instanceof SJRequest)
					{
						n = buildSJRequest((SJRequest) n);
					}
					else if (n instanceof SJAccept)
					{
						n = buildSJAccept((SJAccept) n);
					}					
					else if (n instanceof SJReceive) // Do here, or in SJSessionOperationParser?
					{
						n = buildSJReceive((SJReceive) n); 
					}
					else if (n instanceof SJSelectSession)
					{
						n = buildSJSelectSession((SJSelectSession) n);
					}
					else
					{
						n = buildCall((Call) n);
					}
				}			
			}
			else if (n instanceof Lit)
			{		
				n = buildLit((Lit) n);			
			}
			else if (n instanceof Assign)
			{
				n = buildAssign((Assign) n);
			}
			else if (n instanceof Conditional) // Done in next noalias type building pass.
			{
				n = buildConditional((Conditional) n);
			}
			else if (n instanceof Cast)
			{
				n = buildCast((Cast) n);
			}
			else if (n instanceof Special)
			{
				n = buildSpecial((Special) n);
			}
			else if (n instanceof Binary)
			{
				n = buildBinary((Binary) n);
			}
			else if (n instanceof Unary)
			{
				n = buildUnary((Unary) n);
			}
			else if (n instanceof NewArray)
			{
				n = buildNewArray((NewArray) n);
			}
			else if (n instanceof ArrayInit)
			{
				n = buildArrayInit((ArrayInit) n);
			}
			else if (n instanceof Instanceof)
			{
				n = buildInstanceof((Instanceof) n);
			}
			else 
			{
				throw new SemanticException("[SJNoAliasExprBuilder] Expr not supported yet: " + n);
			}			
		}
		else if (n instanceof ConstructorCall)
		{
			n = buildConstructorCall((ConstructorCall) n);
		}
		else if (n instanceof Return)
		{
			n = buildReturn((Return) n);
		}
		
		return n;
	}
	
	private Node buildField(Field f) throws SemanticException
	{								
		/*if (parent instanceof ArrayAccess)
		{
			return f;
		}*/
		
		Type t = f.target().type();
		
		boolean isNoAlias = false;
		boolean isFinal = false;		
		
		if (t instanceof ParsedClassType)
		{
			ReferenceType pct = (ReferenceType) t;
	
			if (pct instanceof SJParsedClassType)
			{				
				SJFieldInstance sjfi = (SJFieldInstance) sjts.findField(pct, f.name(), context.currentClass());							
				
				isNoAlias = sjfi.isNoAlias();
				isFinal = sjfi.isFinal(); 
				
				f = f.fieldInstance(sjfi);							
			}
			else
			{								
				// FIXME: could still set isFinal, even though it's not relevant unless isNoAlias is true.
				
				debug("[SJNoAliasExprBuilder] Warning! noalias type building skipped (defaulted to non-noalias) for field: " + f);
			}					
		}
		else if (t instanceof ArrayType) // Should be length property access, which is an int type, so leave as non-noalias like other primitives.
		{						
			isNoAlias = true; 
		}
		else if (t instanceof PrimitiveType) // Can't be void. // No need to look up the field instance (regardless of whether it is a SJFieldInstance set by SJNoAliasTypeBuilder). 
		{
			isNoAlias = true;
		}
		
		/*if (!isNoAlias) //Not needed given all fields must now be noalias. 
		{
			if(isNoAlias(f.target()))
			{
				//isNoAlias = isFinal = true; // FIXME: this is should be for assign from, arg. pass, etc. of field... 
				isNoAlias = true; // ...this is for assign to field.
			}
		}*/
		
		f = (Field) setSJNoAliasExprExt(sjef, f, isNoAlias, isFinal, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		
		if (isNoAlias)
		{
			List<Field> fields = new LinkedList<Field>();
			
			fields.add(f);
			
			((SJNoAliasVariablesExt) f.ext()).addFields(fields); // Make an addField for convenience?
		}
		
		return f;
	}	

	private Node buildLocal(Local l) throws SemanticException
	{			
		/*if (parent instanceof ArrayAccess)
		{
			return l;
		}*/		
		
		SJLocalInstance sjli = (SJLocalInstance) context().findLocal(l.name());					
		boolean isNoAlias = sjli.isNoAlias();
				
		l = l.localInstance(sjli);				
		l = (Local) setSJNoAliasExprExt(sjef, l, isNoAlias, sjli.isFinal(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		
		if (isNoAlias)
		{
			List<Local> locals = new LinkedList<Local>();
			
			locals.add(l);
			
			((SJNoAliasVariablesExt) l.ext()).addLocals(locals);
		}		
		
		return l;
	}
	
	private ArrayAccess buildArrayAccess(ArrayAccess aa) {
		SJNoAliasExprExt naee = getSJNoAliasExprExt(aa.array());

        boolean isNoAlias = aa.type().isPrimitive() || naee.isNoAlias();

        //aa = (ArrayAccess) setSJNoAliasExprExt(sjef, aa, isNoAlias, naee.isFinal(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		aa = (ArrayAccess) setSJNoAliasExprExt(sjef, aa, isNoAlias, false, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		
		if (isNoAlias)
		{
			List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
									
			arrayAccesses.add(aa);
			
			((SJNoAliasVariablesExt) aa.ext()).addArrayAccesses(arrayAccesses);
		}
		
		return aa;
	}
	
	private New buildNew(New n) throws SemanticException
	{						
		ParsedClassType pct = (ParsedClassType) n.objectType().type();
		
		boolean isNoAlias = true;
		
		if (pct instanceof SJParsedClassType)
		{
			List formalTypes = n.constructorInstance().formalTypes();		
			SJConstructorInstance sjci = (SJConstructorInstance) sjts.findConstructor(pct, formalTypes, context().currentClass());				
			
			if (((SJParsedClassType) pct).noAliasThroughThis() && sjci.noAliasThroughThis())
			{
                for (Object o : n.arguments()) {
                    if (!isNoAlias((Expr) o)) {
                        isNoAlias = false;

                        break;
                    }
                }
			}
			else
			{
				isNoAlias = false;
			}
			
			n = n.constructorInstance(sjci);			
		}		
		else
		{
			debug("[SJNoAliasExprBuilder] Warning! noalias type building skipped (defaulted to noalias) for: " + n);			
		}
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();

        for (Object o : n.arguments()) {
            SJNoAliasExprExt naee = getSJNoAliasExprExt((Expr) o);

            fields.addAll(naee.fields());
            locals.addAll(naee.locals());
            arrayAccesses.addAll(naee.arrayAccesses());
        }
		
		n = (New) setSJNoAliasExprExt(sjef, n, isNoAlias, false, fields, locals, arrayAccesses);	
		
		return n;
	}
	
	private SJCreateOperation buildSJCreateOperation(SJCreateOperation co) throws SemanticException
	{				
		co = (SJCreateOperation) buildCall(co); // Although no need to check target.
		co = (SJCreateOperation) setNoAliasReturn(co); 

		return co;
	}
	
	private SJRequest buildSJRequest(SJRequest r) throws SemanticException
	{
		r = (SJRequest) buildCall(r); 
		r = (SJRequest) setNoAliasReturn(r); 
		
		return r;
	}

	private SJAccept buildSJAccept(SJAccept a) throws SemanticException
	{
		a = (SJAccept) buildCall(a); 
		a = (SJAccept) setNoAliasReturn(a); 
		
		return a;
	}

	private SJSelectSession buildSJSelectSession(SJSelectSession ss) throws SemanticException
	{
		ss = (SJSelectSession) buildCall(ss); 
		ss = (SJSelectSession) setNoAliasReturn(ss); 
		
		return ss;
	}
	
	private SJReceive buildSJReceive(SJReceive r) throws SemanticException
	{
		r = (SJReceive) buildCall(r); // Although no need to check target.
		r = (SJReceive) setNoAliasReturn(r);
		
		return r;
	}
	
	private Call buildCall(Call c) throws SemanticException // This should be enough to support session type checking of method calls as well (i.e. just need the SJMethodInstances from the Calls). Similarly for ConstructorCalls. // Maybe a separate pass for type building session expressions should be created. 
	{
		Receiver target = c.target();
		
		Type targetType = target.type();
		
		if (targetType instanceof ArrayType_c) // FIXME: handle method calls to array objects. Currently hacked in for wait/notify calls. 
		{
			c = (Call) setSJNoAliasExprExt(sjef, c, false, false, new LinkedList(), new LinkedList(), new LinkedList()); // FIXME: na-final return types currently not supported.
			
			return c;  
		}
		
		ParsedClassType pct = (ParsedClassType) targetType;

		List argsList = c.arguments();
		MethodInstance mi = sjts.findMethod(pct, c.name(), getArgumentTypes(argsList), context().currentClass());
		Type t = mi.returnType();
		
		boolean isNoAlias = false;	
		boolean isFinal = false;
		
		if (t.isPrimitive() && !t.isVoid())
		{
			isNoAlias = true;

			if (pct instanceof SJParsedClassType && mi instanceof SJMethodInstance)
			{			
				c = c.methodInstance(mi);
			}			
		}
		else
		{
			if (pct instanceof SJParsedClassType)
			{					
				if (mi instanceof SJMethodInstance)
				{			
					SJMethodInstance sjmi = (SJMethodInstance) mi;		 	
									
					isNoAlias = sjmi.noAliasReturnType() instanceof SJNoAliasReferenceType;
					
					c = c.methodInstance(sjmi); // Could this be skipped if a barrier was inserted after this pass and before noalias type checking? (Similarly for e.g. locals, fields, constructors, ... ?) // Probably not, this is information recorded on the AST nodes, not auto-built by each context pass.
				}
				else // For anonymous classes, the class types will be SJParsedClassType, but calls to an inherited, regular Java method declaration (i.e. not visited by sessionjc) will have regular MethodInstance.
				{
					debug("[SJNoAliasExprBulder] Warning! noalias type building skipped (defaulted to non-noalias return type) for method call: " + c);				
				}
			}
			else
			{
				debug("[SJNoAliasExprBuilder] Warning! noalias type building skipped (defaulted to non-noalias return type) for method call: " + c);
			}		
		}
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
		
        for (Object anArgsList : argsList) {
            SJNoAliasExprExt naee = getSJNoAliasExprExt((Expr) anArgsList);

            fields.addAll(naee.fields());
            locals.addAll(naee.locals());
            arrayAccesses.addAll(naee.arrayAccesses());
        }
        
		if (target instanceof Expr)
		{		
			Expr e = (Expr) c.target();
			SJNoAliasExprExt naee = getSJNoAliasExprExt(e);
			
			Set<Variable> vars = new HashSet<Variable>();			
			findPossibleCallTargets(e, vars);	// We exclude the possible call targets, but they are implicitly re-included if the also passed as arguments. 					
			
			/*fields.addAll(naee.fields());
			locals.addAll(naee.locals());
			arrayAccesses.addAll(naee.arrayAccesses());*/
			
			for (Field f : naee.fields()) if (!vars.contains(f)) fields.add(f);				
			for (Local l : naee.locals()) if (!vars.contains(l)) locals.add(l);
			for (ArrayAccess aa : naee.arrayAccesses()) if (!vars.contains(aa)) arrayAccesses.add(aa);
		}

		removeNonFinalSocketArguments(c, locals);
		
		if (!isNoAlias)
		{
			if (isNoAlias(target))
			{
				//isNoAlias = isFinal = true; // FIXME: too restrictive, can't use SJ factory methods.
			}
		}
				
		c = (Call) setSJNoAliasExprExt(sjef, c, isNoAlias, isFinal, fields, locals, arrayAccesses); // FIXME: na-final return types currently not supported.
		
		return c;
	}

    private void removeNonFinalSocketArguments(Call c, List<Local> locals) {
        List targets = null;

        //if (c instanceof SJSessionOperation) // Needed since session sockets don't need to be final anymore (and because session (edit: not session, but basic - not compound because c is a Call) operations are already translated to static SJRuntime calls).
        if (c instanceof SJBasicOperation)
        {
            targets = ((SJBasicOperation)c).targets(); // Unicast optimisation of session operations comes later.
        }
        /*else if (c instanceof SJSpawn) // Incorrect: we want spawned sessions to become null.
{
//targets = c.arguments(); // Type checking will prevent these from being final.
}*/

        if (targets != null)
        {
            for (Object o : targets)
            {
                SJLocalSocket s = (SJLocalSocket) o;
                
                if (!s.localInstance().flags().isFinal() && locals.contains(s))
                // Requires that the SJLocalSocket found in targets and the one in locals be equals.
                // Had to implement equals in SJLocalSocket_c, as parent classes come from Polyglot.
                // (the target of the session operation) and assumes that delegating a session over itself is illegal.
                {
                    locals.remove(s);
                }
            }
        }
    }

  private Node buildLit(Lit l) throws SemanticException
	{
		if (l instanceof NullLit || l instanceof StringLit || l instanceof BooleanLit || l instanceof NumLit || l instanceof FloatLit || l instanceof ClassLit)				
		{			
			l = (Lit) setSJNoAliasExprExt(sjef, l, true, false, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		}
		else
		{
			throw new SemanticException("[SJNoAliasExpreBuilder] Lit not supported: " + l);
		}
		
		return l;
	}
	
	private Assign buildAssign(Assign a) {
		Expr left = a.left();		
		Expr right = a.right();
		
		SJNoAliasExprExt naee1 = getSJNoAliasExprExt(left);
		SJNoAliasExprExt naee2 = getSJNoAliasExprExt(right);
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
		
		fields.addAll(naee1.fields());
		fields.addAll(naee2.fields());		
		locals.addAll(naee1.locals());
		locals.addAll(naee2.locals());
		arrayAccesses.addAll(naee1.arrayAccesses());
		arrayAccesses.addAll(naee2.arrayAccesses());
		
		a = (Assign) setSJNoAliasExprExt(sjef, a, isNoAlias(left), isFinal(left), fields, locals, arrayAccesses);	
		
		return a;
	}
	
	private Node buildConditional(Conditional c) {
		Expr consequent = c.consequent();
		Expr alternative = c.alternative();

		boolean isNoAlias = isNoAlias(consequent) && isNoAlias(alternative);
		boolean isFinal = isFinal(consequent) || isFinal(alternative);
		
		SJNoAliasExprExt naee1 = getSJNoAliasExprExt(c.cond());
		SJNoAliasExprExt naee2 = getSJNoAliasExprExt(consequent);
		SJNoAliasExprExt naee3 = getSJNoAliasExprExt(alternative);
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
		
		fields.addAll(naee1.fields());
		fields.addAll(naee2.fields());
		fields.addAll(naee3.fields());
		locals.addAll(naee1.locals());
		locals.addAll(naee2.locals());
		locals.addAll(naee3.locals());
		arrayAccesses.addAll(naee1.arrayAccesses());
		arrayAccesses.addAll(naee2.arrayAccesses());
		arrayAccesses.addAll(naee3.arrayAccesses());		
		
		c = (Conditional) setSJNoAliasExprExt(sjef, c, isNoAlias, isFinal, fields, locals, arrayAccesses);
		
		return c;
	}
	
	private Cast buildCast(Cast c) throws SemanticException
	{
		if (isNoAlias(c.castType()))
		{
			throw new SemanticException("[SJNoAliasExprBuilder] noalias type casts not permitted: " + c);
		}
		
		Expr e = c.expr();
		SJNoAliasExprExt naee = getSJNoAliasExprExt(e);
			
		c = (Cast) setSJNoAliasExprExt(sjef, c, isNoAlias(e), isFinal(e), naee.fields(), naee.locals(), naee.arrayAccesses());
		
		return c;
	}
	
	private ConstructorCall buildConstructorCall(ConstructorCall cc) throws SemanticException
	{
		ParsedClassType pct = (ParsedClassType) cc.constructorInstance().container();
		
		if (pct instanceof SJParsedClassType)
		{		
			SJConstructorInstance sjci = (SJConstructorInstance) sjts.findConstructor(pct, getArgumentTypes(cc.arguments()), context().currentClass());		 	
			
			cc = cc.constructorInstance(sjci); // Could this be skipped if a barrier was inserted after this pass and before noalias type checking? (Similarly for e.g. locals, fields, constructors, ... ?) // Probably not, this is information recorded on the AST nodes, not auto-built by each context pass.
		}
		else
		{
			debug("[SJNoAliasExprBuilder] Warning! noalias type building skipped for constructor call (" + pct + "): " + cc);
		}		
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();

        for (Object o : cc.arguments()) {
            SJNoAliasExprExt naee = getSJNoAliasExprExt((Expr) o);

            fields.addAll(naee.fields());
            locals.addAll(naee.locals());
            arrayAccesses.addAll(naee.arrayAccesses());
        }
		
		// Could check that fields is empty here instead of in JSNoAliasTypeChecker.
		cc = (ConstructorCall) setSJNoAliasVariablesExt(sjef, cc, fields, locals, arrayAccesses);  
		
		return cc;
	}	
	
	private Return buildReturn(Return r)
	{
		Expr e = r.expr();
				
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
		
		if (e != null)
		{
			SJNoAliasExprExt naee = getSJNoAliasExprExt(e);
					
			fields.addAll(naee.fields());
			locals.addAll(naee.locals());
			arrayAccesses.addAll(naee.arrayAccesses());
		}
		
		r = (Return) setSJNoAliasVariablesExt(sjef, r, fields, locals, arrayAccesses);
		
		return r;
	}
	
	private Special buildSpecial(Special s)
	{
		s = (Special) setSJNoAliasExprExt(sjef, s, false, false, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		
		return s;
	}
	
	private Binary buildBinary(Binary b) throws SemanticException // Binary.EQ and Binary.ADD should be the only operators on non-primitive types; and Binary.ADD is the only that does not return a primitive (can return a String).
	{
		boolean isNoAlias = true; // FIXME: currently, works a bit hackily for toString (i.e. implicit toString OK because not checked, but explicit toString to regular Java class is non-noalias - because it may well be aliased, although for immutable String, shouldn't matter).
		boolean isFinal = false;		
		
		Set<Variable> vars = new HashSet<Variable>();
		
		findAssigned(b.left(), vars);
		findAssigned(b.right(), vars);		
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();		
		
		for (Variable v : vars)
		{
			if (v instanceof Field)
			{
				fields.add((Field) v);
			}
			else if (v instanceof Local)
			{
				locals.add((Local) v);
			}
			if (v instanceof ArrayAccess)
			{
				arrayAccesses.add((ArrayAccess) v);
			}
		}		
		
		b = (Binary) setSJNoAliasExprExt(sjef, b, isNoAlias, isFinal, fields, locals, arrayAccesses);
		
		return b;
	}

	private Unary buildUnary(Unary u)
	{
		SJNoAliasExprExt naee = getSJNoAliasExprExt(u.expr());
		
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();		
		
		fields.addAll(naee.fields());
		locals.addAll(naee.locals());
		arrayAccesses.addAll(naee.arrayAccesses());		
		
		u = (Unary) setSJNoAliasExprExt(sjef, u, naee.isNoAlias(), naee.isFinal(), fields, locals, arrayAccesses);
		
		return u;
	}
	
	private NewArray buildNewArray(NewArray na)
	{			
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();
				
		ArrayInit ai = na.init();
		
		if (ai != null)
		{
			SJNoAliasExprExt naee = getSJNoAliasExprExt(ai);
			
			fields.addAll(naee.fields());
			locals.addAll(naee.locals());
			arrayAccesses.addAll(naee.arrayAccesses());
		}
										
		na = (NewArray) setSJNoAliasExprExt(sjef, na, true, false, fields, locals, arrayAccesses); // NewArrays are implicitly noalias in the same way that News (for classes without bad use of this) are?
		
		return na;
	}
	
	private Call setNoAliasReturn(Call c) // For SJ create etc. operations. // I.e. SessionSocketCreates should have noalias return types.
	{
		SJNoAliasExprExt naee = getSJNoAliasExprExt(c);
		
		if (!naee.isNoAlias()) // SJ libraries separately compiled by non-SJ compiler, the general case. 
		{
			c = (Call) setSJNoAliasExprExt(sjef, c, true, naee.isFinal(), naee.fields(), naee.locals(), naee.arrayAccesses());
		}
		
		return c;
	}
	
	private ArrayInit buildArrayInit(ArrayInit ai)
	{
		List<Field> fields = new LinkedList<Field>();
		List<Local> locals = new LinkedList<Local>();
		List<ArrayAccess> arrayAccesses = new LinkedList<ArrayAccess>();

        for (Object o : ai.elements()) {
            SJNoAliasExprExt naee = getSJNoAliasExprExt((Expr) o);

            fields.addAll(naee.fields());
            locals.addAll(naee.locals());
            arrayAccesses.addAll(naee.arrayAccesses());
        }
		
		ai = (ArrayInit) setSJNoAliasExprExt(sjef, ai, true, false, fields, locals, arrayAccesses); // ArrayInits are implicitly noalias in the same way that Lits are?
		
		return ai;
	}	
	
	private Instanceof buildInstanceof(Instanceof i) {
		Expr e = i.expr();		
		SJNoAliasExprExt naee = getSJNoAliasExprExt(e);
		
		i = (Instanceof) setSJNoAliasExprExt(sjef, i, isNoAlias(e), isFinal(e), naee.fields(), naee.locals(), naee.arrayAccesses());	

		return i;
	}
	
	private void findPossibleCallTargets(Expr target, Set<Variable> targets) throws SemanticException
	{
		if (target == null)
		{
			return;
		}
		
		if (target instanceof Variable)
		{
			targets.add((Variable) target); 
		}
		else if (target instanceof Assign)
		{
			targets.add((Variable) ((Assign) target).left()); 
		}
		else if (target instanceof Conditional)
		{
			Conditional c = (Conditional) target; // Not the cond.
			
			findPossibleCallTargets(c.consequent(), targets);
			findPossibleCallTargets(c.alternative(), targets);
		}
		else if (target instanceof Cast) 
		{
			findPossibleCallTargets(((Cast) target).expr(), targets);			
		}
		else if (!(target instanceof ProcedureCall || target instanceof Special)) 
		{
			throw new SemanticException("[SJNoAliasExprBuilder] Call target not yet supported: " + target);
		}
    }
	
	private void findAssigned(Expr e, Set<Variable> vars) throws SemanticException
	{
		if (e == null)
		{
			return;
		}
		
		if (e instanceof Assign)
		{
			SJNoAliasExprExt naee = getSJNoAliasExprExt(((Assign) e).right());
			
			vars.addAll(naee.fields());
			vars.addAll(naee.locals());
			vars.addAll(naee.arrayAccesses());
		}
		else if (e instanceof Conditional)
		{
			Conditional c = (Conditional) e; // Not the cond.
			
			findAssigned(c.cond(), vars);
			findAssigned(c.consequent(), vars);
			findAssigned(c.alternative(), vars);
		}
		else if (e instanceof Cast) 
		{
			findAssigned(((Cast) e).expr(), vars);			
		}
		else if (e instanceof Unary) 
		{
			findAssigned(((Unary) e).expr(), vars);			
		}
		else if (e instanceof ProcedureCall || e instanceof Binary)
		{
			SJNoAliasExprExt naee = getSJNoAliasExprExt(e);
			
			vars.addAll(naee.fields());
			vars.addAll(naee.locals());
			vars.addAll(naee.arrayAccesses());			
		}
		else if (!(e instanceof Variable || e instanceof Special || e instanceof Lit)) 
		{
			throw new SemanticException("[SJNoAliasExprBuilder] Expr not yet supported: " + e);
		}
	}

    public static Node setSJNoAliasVariablesExt(SJExtFactory sjef, Node n, List<Field> fields, List<Local> locals, List<ArrayAccess> arrayAccesses)
    {
        SJNoAliasVariablesExt nave = sjef.SJNoAliasVariablesExt();

        nave.addFields(fields);
        nave.addLocals(locals);
        nave.addArrayAccesses(arrayAccesses);

        return n.ext(1, nave);
    }

    public static Node setSJNoAliasExprExt(SJExtFactory sjef, Node n, boolean isNoAlias, boolean isFinal, List<Field> fields, List<Local> locals, List<ArrayAccess> arrayAccesses)
    {
        SJNoAliasExprExt naee = sjef.SJNoAliasExprExt(isNoAlias, isFinal);

        naee.addFields(fields);
        naee.addLocals(locals);
        naee.addArrayAccesses(arrayAccesses);

        return n.ext(1, naee);
    }
}
