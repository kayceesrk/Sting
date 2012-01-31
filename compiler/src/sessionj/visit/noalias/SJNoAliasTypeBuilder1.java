/**
 * 
 */
package sessionj.visit.noalias;

import java.util.*;

import polyglot.ast.*;
import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.*;

import sessionj.ast.SJNodeFactory;
import sessionj.extension.*;
import sessionj.extension.noalias.*;
import sessionj.types.*;
import sessionj.types.typeobjects.*;
import sessionj.types.noalias.*;
import sessionj.util.noalias.*;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 * @deprecated
 */
public class SJNoAliasTypeBuilder1 extends ContextVisitor
{	
	private SJTypeSystem sjts = (SJTypeSystem) typeSystem();
	private SJNodeFactory sjnf = (SJNodeFactory) nodeFactory();
	private SJExtFactory sjef = sjnf.extFactory();
	
	/**
	 * 
	 */
	public SJNoAliasTypeBuilder1(Job job, TypeSystem ts, NodeFactory nf)
	{
		super(job, ts, nf);
	}

	protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException
	{		
		if (n instanceof ClassMember)
		{
			if (n instanceof FieldDecl)
			{
				n = buildFieldDecl((FieldDecl) n);
			}
		}
		else if (n instanceof VarDecl)
		{
			n = buildVarDecl((VarDecl) n);
		}		
		
		return n;
	}
	
	private FieldDecl buildFieldDecl(FieldDecl fd) throws SemanticException
	{		
		TypeNode tn = fd.type();
		
		boolean isNoAlias = false;
		boolean isFinal = false; // final noalias primitive variables are treated just as noalias, not na-final (since they are passed by value).
						
		if (tn.type().isPrimitive()) // Can't be void.
		{
			isNoAlias = true;
		}
		else
		{
			isNoAlias = isNoAlias(tn);
			isFinal = fd.flags().isFinal();
		}
		
		//boolean isNoAlias = fd.type().type() instanceof SJNoAliasReferenceType;
		//boolean isFinal = fd.flags().isFinal(); 
		
		FieldInstance fi = fd.fieldInstance();
		SJFieldInstance sjfi = sjts.SJFieldInstance(fd.fieldInstance(), isNoAlias, isFinal);						
		
		sjfi.setConstantValue(fi.constantValue()); // Currently, constant checker not run on custom nodes/type objects.
		
		fd = fd.fieldInstance(sjfi);
		fd = (FieldDecl) setSJNoAliasFinalExt(sjef, fd, isNoAlias, isFinal); // Needs to be done for all Expr.
		
		updateSJFieldInstance(fi, sjfi); // Needs to be done for all ClassMember except ClassDecl (latter links to shared, mutable ParsedClassType).								
		
		return fd;
	}	
	
	private VarDecl buildVarDecl(VarDecl vd) throws SemanticException
	{
		TypeNode tn = vd.type();
		
		boolean isNoAlias = false;
		boolean isFinal = false; // final noalias primitive variables are treated just as noalias, not na-final (since they are passed by value).  
		
		if (tn.type().isPrimitive()) // Can't be void.
		{
			isNoAlias = true;
		}
		else
		{
			isNoAlias = isNoAlias(tn);
			isFinal = vd.flags().isFinal();
		}		
			
		//boolean isNoAliasType = vd.type().type() instanceof SJNoAliasReferenceType;
		//boolean isFinal = vd.flags().isFinal();
		
		SJLocalInstance sjli = sjts.SJLocalInstance(vd.localInstance(), isNoAlias, isFinal);		
		
		if (vd instanceof LocalDecl) 
		{
			vd = ((LocalDecl) vd).localInstance(sjli);				
		}
		else //if (vd instanceof Formal)
		{
			vd = ((Formal) vd).localInstance(sjli);
		}		
		
		vd = (VarDecl) setSJNoAliasFinalExt(sjef, vd, isNoAlias, isFinal);		
		
		return vd;
	}
}
