package sessionj.types.sesstypes;

public interface SJOutwhileType extends SJLoopType // Maybe make a SJWhileType.
{
	public SJOutwhileType body(SJSessionType body);
}
