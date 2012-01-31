package sessionj.types.sesstypes;

import polyglot.types.*;

abstract public class SJBeginType_c extends SJSessionType_c implements SJBeginType
{
	protected SJBeginType_c(TypeSystem ts)
	{
		super(ts);
	}

	public boolean nodeWellFormed() 
	{
		return false;
        // begin as a session type element other than initial prefix is bad;
        // the primary isWellFormed routine in SJSessionType_c handles the prefix separately.
	}

    @Override
    public boolean isWellFormed() {
        if (typeSystem().wellFormedRecursions(this)) {
            SJSessionType st = child();
            return st == null || st.treeWellFormed();
        }
        return false;
    }
}
