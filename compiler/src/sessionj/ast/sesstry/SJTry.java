package sessionj.ast.sesstry;

import java.util.List;

import polyglot.ast.*;

public interface SJTry extends Try // Probably can collapse all sj-trys into one entity. 
{
	public List targets(); // Duplicated from SJChannelOperation. // FIXME: unify (by moving to an extension object)?
	public SJTry targets(List targets);
}
