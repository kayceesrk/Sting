package sessionj.runtime.session;

import sessionj.types.sesstypes.SJRecursionType;
import sessionj.util.SJLabel;
import sessionj.runtime.session.contexts.SJRecursionContext;

import java.util.HashMap;
import java.util.Map;

public class TypeVariableScope {
    /**
     * Invariant: labels.containsKey(X) ==> !labels.get(X).isEmpty()
     */
    //private final Map<SJLabel, Stack<SJRecursionType>> labels = new HashMap<SJLabel, Stack<SJRecursionType>>();
    // For now, assuming no masking of recursion variables.
    private final Map<SJLabel, SJRecursionType> labels = new HashMap<SJLabel, SJRecursionType>();

    public Map<SJLabel, SJRecursionType> inScope() {
	    /*
        Map<SJLabel, SJRecursionType> result = new HashMap<SJLabel, SJRecursionType>();
        for (Map.Entry<SJLabel, Stack<SJRecursionType>> e : labels.entrySet()) {
            result.put(e.getKey(), e.getValue().peek());
        }
        return result;
        */
	    // For now, assuming no masking of recursion variables.
	    return new HashMap<SJLabel, SJRecursionType>(labels);
    }

    public void enterScope(SJLabel sjLabel, SJRecursionType rt) {
	    /*
        Stack<SJRecursionType> s = labels.get(sjLabel);
        if (s == null) {
            s = new Stack<SJRecursionType>();
            labels.put(sjLabel, s);
        }
        s.push(rt);
        */
	    // For now, assuming no masking of recursion variables.
	    labels.put(sjLabel, rt);
    }

    public void exitScope(SJLabel lab) {
	    /*
        Stack<SJRecursionType> stack = labels.get(lab);
        stack.pop();
        if (stack.isEmpty()) labels.remove(lab);
        */
	    // For now, assuming no masking of recursion variables.
	    labels.remove(lab);
    }
	
	public void exitScope(SJRecursionContext context) {
		exitScope(context.label());
	}

    public boolean alreadyEntered(SJLabel label) {
	    // For now, assuming no masking of recursion variables.
        //return labels.get(label) != null;
	    return labels.containsKey(label);
    }

	@Override
	public String toString() {
		return "TypeVariableScope{" + labels + '}';
	}
}
