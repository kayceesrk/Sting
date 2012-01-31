package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;
import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import sessionj.ast.typenodes.SJTypeNode;
import sessionj.extension.SJExtFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.contexts.SJContextElement;
import sessionj.types.contexts.SJContextInterface;
import sessionj.types.contexts.SJTypeBuildingContext;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.sesstypes.SJSetType;
import sessionj.util.SJCompilerUtils;

import java.util.List;

/**
 * Not quite a statement, but the switch cases {@link polyglot.ast.Case}
 * are also considered to be statements.
 */
public class SJWhen_c extends Block_c implements SJWhen {
    private final SJTypeNode typeNode;

    public SJWhen_c(Position pos, SJTypeNode typeNode, List<Stmt> body) {
        super(pos, body);
        this.typeNode = typeNode;
    }

    @Override
    public String toString() {
        return "when (" + typeNode + ") " + super.toString();
    }
    
    @Override
    public Object copy() 
    {
  		SJWhen_c newThis = (SJWhen_c) super.copy(); // Polyglot copy uses clone.
  		
  		return newThis;
    }

    public SJContextElement leaveSJContext(SJContextInterface sjcontext) throws SemanticException {
        return sjcontext.pop();
    }

    public void enterSJContext(SJContextInterface sjcontext) throws SemanticException {
        sjcontext.pushSJWhen(this);
    }

    public SJSessionType selectMatching(SJSetType set) throws SemanticException {    	
        if (set.contains(type())) return type();
        else throw new SemanticException("Expected a set type containing " + typeNode + " but got " + set);
    }

    public SJSessionType type() {
        assert typeNode.type() != null;
        return typeNode.type();
    }

    public Node buildType(ContextVisitor cv, SJTypeSystem sjts, SJExtFactory sjef) throws SemanticException {
        SJTypeNode newTypeNode = SJCompilerUtils.disambiguateSJTypeNode(cv, typeNode);
        Node updated = new SJWhen_c(position, newTypeNode, statements);
        if (ext() != null) {
            updated = updated.ext((Ext) ext.copy());
        }
        if (del != null) { // the del() method returns this if del is null, so use field to check
            updated = updated.del((JL) del().copy());
        }
        return updated;
    }
}
