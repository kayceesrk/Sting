package sessionj.del;

import java.util.*;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.sessops.*;
import sessionj.util.*;

/**
 * 
 * @author Raymond
 * @deprecated
 * 
 * Was needed to bypass a type checking problem (by using dummy type information for the call arguments), but now fixed by inserting a compilation barrier.
 * 
 */
public class SJSpawnDel_c extends JL_c implements SJSpawnDel
{
	public Node typeCheck(TypeChecker tc) throws SemanticException
	{
		/*SJTypeBuilder sjtb = new SJTypeBuilder(tc.job(), tc.typeSystem(), tc.nodeFactory());
		Context context = tc.context();

		SJSpawn sjs = (SJSpawn) node();

		/*New target = sjs.target();

		List sockets = target.arguments();
		List visited = new ArrayList();

		for (Object socket : sockets)
		{
			visited.add(sjtb.buildTypes(context, (Receiver) socket));
		}

		sjs = (SJSpawn) sjs.target((New) target.arguments(visited));*

		List<Receiver> socketArgs = SJCompilerUtils.getSJSpawnArgs(sjs);
		List<Receiver> visited = new ArrayList<Receiver>();

		for (Object socket : socketArgs)
		{
			visited.add((Receiver) sjtb.buildTypes(context, (Receiver) socket));
		}

		sjs = SJCompilerUtils.setSJSpawnArgs(sjs, visited);*/

		return node().typeCheck(tc); // Still typecheck node as originally intended.
		//return node();
		//return original(tc, (Call_c) node());
	}
	
	/*private Call original(TypeChecker tc, Call_c call) throws SemanticException
	{
		TypeSystem ts = tc.typeSystem();
	  Context c = tc.context();
	
	  List argTypes = new ArrayList(call.arguments().size());
	
	  /*for (Iterator i = call.arguments().iterator(); i.hasNext(); ) {
	      Expr e = (Expr) i.next();
	      if (! e.type().isCanonical()) {
	          return call;
	      }
	      argTypes.add(e.type());
	  }*/
	
	  /*if (call.target() == null) {
	      return call.typeCheckNullTarget(tc, argTypes);
	  }*
	  
	  if (! call.target().type().isCanonical()) {
	      return call;
	  }
	  
	  ReferenceType targetType = findTargetType(call);
	  MethodInstance mi = ts.findMethod(targetType, 
	                                    call.id().id(), 
	                                    argTypes, 
	                                    c.currentClass());
	  
	  /* This call is in a static context if and only if
	   * the target (possibly implicit) is a type node.
	   *
	  boolean staticContext = (call.target() instanceof TypeNode);
	
	  if (staticContext && !mi.flags().isStatic()) {
	      throw new SemanticException("Cannot call non-static method " + call.id().id()
	                            + " of " + call.target().type() + " in static "
	                            + "context.", call.position());
	  }
	
	  // If the target is super, but the method is abstract, then complain.
	  if (call.target() instanceof Special && 
	      ((Special)call.target()).kind() == Special.SUPER &&
	      mi.flags().isAbstract()) {
	          throw new SemanticException("Cannot call an abstract method " +
	                         "of the super class", call.position());            
	  }
	
	  Call_c call2 = (Call_c)call.methodInstance(mi).type(mi.returnType());
	
	  // If we found a method, the call must type check, so no need to check
	  // the arguments here.
	  //call2.checkConsistency(c);
	
	  return call2;
	}
	
	protected ReferenceType findTargetType(Call_c call) throws SemanticException {
		Receiver target = call.target();
		Id name = call.id();
		
    Type t = target.type();
    if (t.isReference()) {
        return t.toReference();
    } else {
        // trying to invoke a method on a non-reference type.
        // let's pull out an appropriate error message.
        if (target instanceof Expr) {
            throw new SemanticException("Cannot invoke method \"" + name + "\" on "
                                + "an expression of non-reference type "
                                + t + ".", target.position());
        }
        else if (target instanceof TypeNode) {
            throw new SemanticException("Cannot invoke static method \"" + name
                                        + "\" on non-reference type " + t + ".",
                                        target.position());
        }            
        throw new SemanticException("Cannot invoke method \"" + name
                                    + "\" on non-reference type " + t + ".",
                                    target.position());
    }
	}*/	
}
