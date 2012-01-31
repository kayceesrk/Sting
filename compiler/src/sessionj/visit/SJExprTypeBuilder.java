/**
 * 
 */
package sessionj.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.SJNodeFactory;
import sessionj.ast.chanops.*;
import sessionj.ast.createops.*;
import sessionj.ast.sessops.*;
import sessionj.ast.sessops.basicops.*;
import sessionj.ast.sessvars.SJLocalSocket;
import sessionj.ast.servops.*;
import sessionj.extension.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.typeobjects.*;
import sessionj.types.noalias.*;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 * @deprecated
 *
 */
public class SJExprTypeBuilder extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJExprTypeBuilder(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		
		return n;
	}	
}
