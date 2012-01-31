package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.sesstypes.SJSessionType_c;
import sessionj.util.SJCompilerUtils;

public class SJSetTypeNode_c extends SJTypeNode_c implements SJSetTypeNode {
    private List<SJTypeNode> elements;

    public SJSetTypeNode_c(Position pos, List<SJTypeNode> elements) {
        super(pos);
        assert !elements.isEmpty();
        setElements(elements);
    }

    private void setElements(List<SJTypeNode> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }

    public String nodeToString() {
        StringBuilder builder = new StringBuilder("{ ");
        for (SJTypeNode n : elements) builder.append(n).append(", ");
        builder.replace(builder.length()-2, builder.length(), " }");
        return builder.toString();
    }

    public boolean isSingleton() {
        return elements.size() == 1;
    }

    public SJTypeNode get(int index) {
        return elements.get(index);
    }

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts)
        throws SemanticException
    {
        List<SJTypeNode> disambElem = new LinkedList<SJTypeNode>();
        //List<SJSessionType_c> elemTypes = new LinkedList<SJSessionType_c>();
        List<SJSessionType> elemTypes = new LinkedList<SJSessionType>();
        for (SJTypeNode elem : elements) {
            SJTypeNode typeNode = SJCompilerUtils.disambiguateSJTypeNode(cv, elem);
            disambElem.add(typeNode);
            elemTypes.add((SJSessionType_c) typeNode.type());
        }
        setElements(disambElem);
        return type(sjts.SJSetType(elemTypes));
    }

    @Override // for breakpoints only, to be deleted
    public SJSessionType type() {
        return super.type();
    }
}
