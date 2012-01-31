package sessionj.types.sesstypes;

public interface SJLoopType extends SJSessionType
{
	public SJSessionType body();
	public SJLoopType body(SJSessionType body);
	
	public SJSessionType unfold(); // TODO: override in concrete classes.
}
