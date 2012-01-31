/**
 * 
 */
package sessionj.ast.noalias;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;

import sessionj.SJConstants;
import sessionj.ast.*;
import sessionj.extension.SJExtFactory;
import sessionj.extension.noalias.*;
import sessionj.types.*;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 * 
 * Duplicated from SJNoAliasArrayTypeNode.
 * 
 */
public class SJNoAliasCanonicalTypeNode_c extends CanonicalTypeNode_c implements
    SJNoAliasCanonicalTypeNode
{
	/**
	 * @param pos
	 */
	public SJNoAliasCanonicalTypeNode_c(Position pos, CanonicalTypeNode ctn)
	{
		super(pos, ctn.type());
	}

	public Node disambiguate(AmbiguityRemover sc) throws SemanticException
	{
		SJTypeSystem sjts = (SJTypeSystem) sc.typeSystem();
		SJExtFactory sjef = ((SJNodeFactory) sc.nodeFactory()).extFactory();
		
		TypeNode tn = (TypeNode) super.disambiguate(sc);			
		tn = (TypeNode) setSJNoAliasExt(sjef, tn, true); // It's better to keep noalias "marking" separate from the general type system. More difficult to make ClassTypes (by modifying the TypeSystem) for both noalias and non-noalias, and noalias is not really a property of the type itself, but of references that use it. 
		//tn = tn.type(sjts.SJNoAliasReferenceType((ReferenceType) tn.type()));
		
		return tn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polyglot.ast.TypeNode_c#prettyPrint(polyglot.util.CodeWriter,
	 * polyglot.visit.PrettyPrinter)
	 */
	@Override
	public void prettyPrint(CodeWriter w, PrettyPrinter tr)
	{
		w.write(SJConstants.SJ_KEYWORD_NOALIAS + " ");		
		super.prettyPrint(w, tr);
	}
}
