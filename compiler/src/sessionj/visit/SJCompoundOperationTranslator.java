package sessionj.visit;

import polyglot.ast.*;
import polyglot.frontend.Job;
import polyglot.qq.QQ;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.UniqueID;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import static sessionj.SJConstants.*;
import sessionj.SJConstants;
import sessionj.ast.SJNodeFactory;
import sessionj.ast.sessops.basicops.SJRecurse;
import sessionj.ast.sessops.compoundops.*;
import sessionj.extension.sessops.SJSessionOperationExt;
import sessionj.types.SJTypeSystem;
import static sessionj.util.SJCompilerUtils.buildAndCheckTypes;
import static sessionj.util.SJCompilerUtils.getSJSessionOperationExt;
import sessionj.util.SJLabel;
import sessionj.util.SJTypeEncoder;

import java.util.*;

/**
 * 
 * @author Raymond
 *
 * Also translates basic recurse operation (and method calls for session recursions).
 *
 * Since this visitor adds new code but doesn't not always build type information for the new code immediately (it is done later when leaving an outer context scope), this visitor does not seem to integrate well with the SJSessionVisitor framework. 
 *
 * Furthermore, all SJSessionVisitor passes must come before this one: this translator destroys e.g. SJInbranch, so "automatic" session context management by the SJSessionVisitor framework won't work anymore.
 *
 */
public class SJCompoundOperationTranslator extends ContextVisitor 
//public class SJCompoundOperationTranslator extends SJSessionVisitor // Doesn't work because we translate some code (e.g. SJRecurse) but type information required by the SJSessionVisitor framework is not immediately available.  
{
	private static final String INWHILE_VAR = "_inwhile";
	private static int inwhileCounter = 1;
	
	private final SJTypeSystem sjts;
	private final SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();

	private final Stack<SJCompoundOperation> compounds = new Stack<SJCompoundOperation>();
    // Actually, only SJInbranch and SJRecursion.
    private final Stack<Collection<ClassMember>> fieldsToAdd = new Stack<Collection<ClassMember>>();

    public SJCompoundOperationTranslator(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
        sjts = (SJTypeSystem) ts;
    }

	protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException 
	{
		if (n instanceof SJInbranch || n instanceof SJRecursion)
		{
			compounds.push((SJCompoundOperation) n);
		} 
        else if (n instanceof ClassBody)
        {
            fieldsToAdd.push(new LinkedList<ClassMember>());
        }
		
		return this;
	}
    
	// Made public for the convenience of test programs.
	public Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		Node newNode = n;
        if (n instanceof SJCompoundOperation)
        {
            SJCompoundOperation so = (SJCompoundOperation) n;

            if (so instanceof SJInbranch)
            {
                compounds.pop();
                // We're only using this to tell if we're dealing with the
                // outermost inbranch/recursion for type checking purposes.

                newNode = translateSJInbranch((SJInbranch) so, createQQ(so));
            }
            else if (so instanceof SJRecursion)
            {
                compounds.pop();

                newNode = translateSJRecursion((SJRecursion) so, createQQ(so));
            }
            else if (so instanceof SJOutwhile)
            {
                newNode = translateSJOutwhile((SJOutwhile) so, createQQ(so));
            }
            else if (so instanceof SJInwhile)
            {
                newNode = translateSJInwhile((SJInwhile) so, createQQ(so));
            }
            else if (so instanceof SJOutInwhile)
            {
                newNode = translateSJOutinwhile((SJOutInwhile) so, createQQ(so));
            }
            else if (so instanceof SJTypecase)
            {
                newNode = ((SJTypecase) so).translate(createQQ(so), this, fieldsToAdd.peek());
            }
        }
        else if (n instanceof SJRecurse)
        {
            newNode = translateSJRecurse(parent, (SJRecurse) n, createQQ(n));
        }
        else if (n instanceof ClassBody && !fieldsToAdd.isEmpty())
        {
            Collection<ClassMember> toAdd = fieldsToAdd.pop();
            newNode = n;
            for (ClassMember generatedField : toAdd) {
                newNode = ((ClassBody) newNode).addMember(generatedField);
            }
        }

        if (shouldBuildAndCheckTypes(n))
        //if (shouldBuildAndCheckTypes(newNode)) // Don't use newNode here, because it can be translated into something different than n.
        {
            newNode = buildAndCheckTypes(this, newNode);
        }

        return newNode;
    }

    private QQ createQQ(Node node) {
        return new QQ(sjts.extensionInfo(), node.position());
    }

    private Expr buildNewArray(Position pos, List contents) {
        NewArray na = sjnf.makeSocketsArray(pos, contents.size());

        ArrayInit ai = sjnf.ArrayInit(pos, contents);
        na = na.init(ai)
               .dims(Collections.emptyList())
               .additionalDims(1);
        return na;
    }

    private Node translateSJOutwhile(SJOutwhile outwhile, QQ qq) {
        String unique = UniqueID.newID("loopCond");
        boolean isInterruptible = outwhile.isInterruptible();
        BooleanLit interruptible = new BooleanLit_c(outwhile.position(), isInterruptible);
        //RAY
        List targets = outwhile.targets();
        String translation;
        Object[] mapping;
        if (targets.size() == 1)
        {
        	if (isInterruptible)
        	{
	        	translation = "{ sessionj.runtime.net.LoopCondition %s = " 
	        		          + "sessionj.runtime.net.SJRuntime.negotiateOutsync(%E, %s);" 
	        		          + " while (%s.call(%E)) %S }";
	        	mapping = new Object[] { unique, interruptible, ((Local) targets.get(0)).name(), unique, outwhile.cond(), outwhile.body()};
        	}
        	else
        	{
        		translation = "{ sessionj.runtime.net.SJRuntime.negotiateOutsync(%E, %s);" 
		                    + " while (sessionj.runtime.net.SJRuntime.outsync(%E, %s)) %S }";
        		String sockName = ((Local) targets.get(0)).name();
	mapping = new Object[] { interruptible, sockName, outwhile.cond(), sockName, outwhile.body()};
        	}
        }
        else
        {
	        Expr sockArray = buildNewArray(outwhile.position(), targets);
	        translation = "{ sessionj.runtime.net.LoopCondition %s = " +
	      	"sessionj.runtime.net.SJRuntime.negotiateOutsync(%E, %E);" +
	      	" while (%s.call(%E)) %S }";
	        mapping = new Object[] { unique, interruptible, sockArray, unique, outwhile.cond(), outwhile.body()};
	        /*
	        BooleanLit interruptible = new BooleanLit_c(outwhile.position(), outwhile.isInterruptible());
	        return qq.parseStmt(
	"{ sessionj.runtime.net.LoopCondition %s = " +
	"sessionj.runtime.net.SJRuntime.negotiateOutsync(%E, %E);" +
	" while (%s.call(%E)) %S }",
	                unique,
	                interruptible, sockArray,
	                unique, outwhile.cond(), outwhile.body()
	        );*/
        }  
        return qq.parseStmt(translation, mapping);
      //YAR        
    }

    private Node translateSJInwhile(SJInwhile inwhile, QQ qq) {
    		List targets = inwhile.targets();
   
        /*return qq.parseStmt(
        		"{ sessionj.runtime.net.SJRuntime.negotiateNormalInwhile(%E);" +
        		" while (sessionj.runtime.net.SJRuntime.insync(%E)) %S }",
        		                sockArray, sockArray, inwhile.body()
        		        );*/
        
        //RAY
        String translation;
        Object[] mapping;
	String target = ((SJInwhile_c)inwhile).arguments().get(0).toString();	//<By MQ>
        if (targets.size() == 1)
        {
	    translation = "{ sessionj.runtime.net.SJRuntime.negotiateNormalInwhile(" + target + ", %s);" //<By MQ>
		+ " while (sessionj.runtime.net.SJRuntime.insync(" + target + ", %s)) %S }"; //<By MQ>
		//+ "\n" + "%s.flush();"; //<By MQ> to flush sends after inwhile
        	String sockName = ((Local) targets.get(0)).name();
	    return qq.parseStmt(translation, new Object[] { sockName, sockName, inwhile.body()/*, sockName*/ }); //<By MQ>
        }
        else
        {
          Expr sockArray = buildNewArray(inwhile.position(), targets);
          String tmpVarName = SJConstants.SJ_TMP_LOCAL + INWHILE_VAR + (inwhileCounter++);        	
  				translation = "{ sessionj.runtime.net.SJSocket[] %s = %E;"
				    + " sessionj.runtime.net.SJRuntime.negotiateNormalInwhile(" + target + ", %s);" //<By MQ>
				    + " while (sessionj.runtime.net.SJRuntime.insync(" + target + ", %s)) %S }"; //<By MQ>
					mapping = new Object[] { tmpVarName, sockArray, tmpVarName, tmpVarName, inwhile.body() };
        }
	return qq.parseStmt(translation, mapping);        
        //YAR
    }

    private Node translateSJOutinwhile(SJOutInwhile outinwhile, QQ qq) {
    		List sources = outinwhile.insyncSources();
    		List targets = outinwhile.outsyncTargets();

    		String loopCond = UniqueID.newID("loopCond");
        String peerInterruptible = UniqueID.newID("peerInterruptible");

        List<Object> subst = new LinkedList<Object>();
        String code;
        
        // FIXME: this should be better factored. here, should treat sources and targets separately. but also should integrate better with the same "optimisations" in the translation of in/outwhile
        if (sources.size() == 1 && targets.size() == 1)  
        {
        	String sourceName = ((Local) sources.get(0)).name();
        	String targetName = ((Local) targets.get(0)).name(); 
        	
	        code = "{ sessionj.runtime.net.SJRuntime.negotiateOutsync(false, %s); ";
	        subst.add(targetName);
	        if (outinwhile.hasCondition()) 
	        {
	          /*code += "boolean %s = ";
	          subst.add(peerInterruptible);*/
	        	throw new RuntimeException("[SJCompoundOperation] TODO.");
	        }
	        code += "sessionj.runtime.net.SJRuntime.";
	        if (outinwhile.hasCondition()) 
	        {
	        	//code += "negotiateInterruptingInwhile" 
	        	throw new RuntimeException("[SJCompoundOperation] TODO.");
	        }
	        else
	        {
	        	code += "negotiateNormalInwhile";
	        }
	        code += "(%s); while(sessionj.runtime.net.SJRuntime.outsync(";
	        subst.add(sourceName);
	        
	        if (outinwhile.hasCondition()) {
	            /*code += "interruptingInsync(%E, %s, %E)";
	            subst.add(outinwhile.cond());
	            subst.add(peerInterruptible);
	            subst.add(sourcesArray);*/
	        	throw new RuntimeException("[SJCompoundOperation] TODO.");
	        } else {
	            code += "sessionj.runtime.net.SJRuntime.insync(%s)";
	            subst.add(sourceName);
	        }
	        code += ", %s)) %S  }";
		code += "\n" + "%s.flush();"; //<By MQ> to flush sends after outwhile
	        subst.add(targetName);
	        subst.add(outinwhile.body());      
	        subst.add(targetName);        //<By MQ>
        }
        else
        {
          Expr sourcesArray = buildNewArray(outinwhile.position(), sources);  // inwhile sockets
          Expr targetsArray = buildNewArray(outinwhile.position(), targets); // outwhile sockets        	
        	
	        subst = new LinkedList<Object>(Arrays.asList(
	            loopCond, targetsArray
	        ));
	        code =
	            "{ sessionj.runtime.net.LoopCondition %s = " +
	            "sessionj.runtime.net.SJRuntime.negotiateOutsync(false, %E); ";
	        if (outinwhile.hasCondition()) {
	            code += "boolean %s = ";
	            subst.add(peerInterruptible);              
	        }
	        code += "sessionj.runtime.net.SJRuntime.";
	        code += outinwhile.hasCondition() ?
	            "negotiateInterruptingInwhile" : "negotiateNormalInwhile";
	        code += "(%E); while(%s.call(sessionj.runtime.net.SJRuntime.";
	
	        subst.add(sourcesArray);
	        subst.add(loopCond);
	        
	        if (outinwhile.hasCondition()) {
	            code += "interruptingInsync(%E, %s, %E)";
	            subst.add(outinwhile.cond());
	            subst.add(peerInterruptible);
	            subst.add(sourcesArray);
	        } else {
	            code += "insync(%E)";
	            subst.add(sourcesArray);
	        }
	        code += ")) %S  }";
	        subst.add(outinwhile.body());
        }

        return qq.parseStmt(code, subst);
    }

    private Node translateSJRecurse(Node parent, SJRecurse r, QQ qq) {
		if (!(parent instanceof Eval))
		{
			throw new RuntimeException("[SJCompoundOperationTranslator] Shouldn't get here.");			
		}

        String translation = "";
		List<Object> mapping = new LinkedList<Object>();
		
		translation += "%s = %E";
		mapping.add(getRecursionBooleanName(getSJSessionOperationExt(r).targetNames(), r.label()));
		mapping.add(r);

        return qq.parseExpr(translation, mapping.toArray());
	}
	
	private Node translateSJInbranch(SJInbranch ib, QQ qq) {
        StringBuilder translation = new StringBuilder("{ ");
		Collection<Object> mapping = new LinkedList<Object>();
		
		String labVar = UniqueID.newID(SJ_INBRANCH_LABEL_FIELD_PREFIX);
		
		translation.append("%T %s = %E; ");
		mapping.add(qq.parseType(SJ_LABEL_CLASS));
		mapping.add(labVar);
		mapping.add(ib.inlabel());
		
		for (Iterator<SJInbranchCase> i = ib.branchCases().iterator(); i.hasNext(); )
		{
			SJInbranchCase ibc = i.next();
			
			translation.append("if (%s.equals(%E)) { %LS } ");
			mapping.add(labVar);
			mapping.add(sjnf.StringLit(ib.position(), ibc.label().labelValue()));
			mapping.add(ibc.statements());
			
			if (i.hasNext())
			{
				translation.append("else ");
			}
		}
		
		translation.append("else { throw new SJIOException(\"Unexpected inbranch label: \" + %s); }");
		mapping.add(labVar);
		
		//FIXME: need a final else case to better handle, if runtime monitoring is disabled, non-sj-compatibility mode and in case of malicious peers.
		
		translation.append('}');

        return qq.parseStmt(translation.toString(), mapping.toArray());
	}

	//FIXME: does not integrate with recursive session method calls: recursionEnter/Exit and also recurse do not match the control flow of recursive calls, and hence runtime type monitoring does not work.
    private Node translateSJRecursion(SJRecursion r, QQ qq)
    // recursionEnter inserted by node factory, but translation is finished here..
	{
		SJSessionOperationExt soe = getSJSessionOperationExt(r);
		
		Position pos = r.position();

        Collection<Object> mapping = new LinkedList<Object>();
		
		String bname = getRecursionBooleanName(soe.targetNames(), r.label());

        mapping.add(bname);
		mapping.add(bname);

        String translation = "for (boolean %s = true; %s; ) { }";
        For f = (For) qq.parseStmt(translation, mapping.toArray());
		
		mapping.clear();
		
		r = (SJRecursion) r.inits(f.inits());
		r = (SJRecursion) r.cond(f.cond());
		
		List stmts = new LinkedList();
		
		stmts.addAll(r.body().statements()); 
		
		translation = "%s = %E;";
		mapping.add(bname);
		mapping.add(((Eval) stmts.remove(0)).expr()); // Factor out constant.
		
		Eval e = (Eval) qq.parseStmt(translation, mapping.toArray());
		
		stmts.add(0, e);
		
		r = (SJRecursion) r.body(sjnf.Block(pos, stmts));
	
		/*// Appending the recursion-exit hook. // Disabled to support delegation from within recursion scopes (socket will be null on recursion-exit). 
		List<Local> targets = new LinkedList<Local>(); // FIXME: should be SJLocalSockets.
		
		for (String sjname : soe.targetNames()) // Unicast optimisation for SJRecursionExit is done within the NodeFactory method - this pass comes after SJUnicastOptimiser.
		{
			targets.add(sjnf.Local(pos, sjnf.Id(pos, sjname))); // Would it be bad to instead alias the recursionEnter targets? 
		}

		SJRecursionExit re = sjnf.SJRecursionExit(pos, targets); // Problem: the sockets argument array is not yet filled (for other (internal) basic operations, this was done earlier by SJSessionOperationParser)...								
		
		re = (SJRecursionExit) SJVariableParser.parseSJSessionOperation(this, re); // ...Current fix: use those routines form those earlier passes.   
		re = (SJRecursionExit) SJSessionOperationParser.fixSJBasicOperationArguments(this, re);*/
		
    //return sjnf.Block(pos, r, sjnf.Eval(pos, re));
		return sjnf.Block(pos, r);
	}
	
	private boolean shouldBuildAndCheckTypes(Object node) 
		{
		  if (node instanceof SJRecurse) 
		  {
		  	return false;
		  }
		    // SJRecurse: Can't build the types now because the assignment target variable is not in the context
		    //  - but it will be built when we translate the outer(most) recursion statement.
		  /*else if (node instanceof SJInbranch || node instanceof SJRecursion)
		  {
		  	return compounds.isEmpty();
		    // (Re-)building types might erase previously built SJ type information.
		    // Maybe we don't need to rebuild types in translation phase.
		    // Or maybe no important SJ type information is lost
		    // (e.g. protocol fields, method signatures, etc.).
		    // Need to build types in one go because cannot build types for e.g. the assignment expression
		    // separately from the newly inserted variable declaration for the assignment target.
		  }*/	    
		  else // RAY: not sure if this is right. // It was wrong: we still need to build types for outbranch in case there is a translated recurse or other constructs within.
		  {
		  	//return node instanceof SJCompoundOperation;
		  	if (node instanceof SJCompoundOperation)
		  	{
		  		//return (!(node instanceof SJOutbranch)) && compounds.isEmpty(); // outbranch is the only compound operation we didn't translate here (and it's not recorded in the compounds stack). // But there could be other constructs inside.
		  		return compounds.isEmpty(); // 
		  	}
		  	else // Ignore non compound operations.
		  	{
		  		return false;
		  	}
		  }
		}

	private String getRecursionBooleanName(Iterable<String> sjnames, SJLabel lab)
	{
		StringBuilder bname = new StringBuilder(SJ_RECURSION_PREFIX);
		
		for (String sjname : sjnames)
		{
			bname.append(sjname).append('_'); 
		}
		
		return bname + lab.labelValue();		
	}
}
