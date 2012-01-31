package sessionj.ast;

import polyglot.ast.Call;
import sessionj.types.sesstypes.SJSessionType;

import java.util.List;

/**
 * 
 * @author Raymond
 *
 * Not a SJBasicOperation, to simplify noalias type checking of session arguments against run parameters (SJBasicOperations use arrays to pass the session arguments to the SJ Runtime), but otherwise would be related to SJPass (as a "delagation" mechanism).
 *
 * Now not a SJSessionOperation, as these are currently singly typed operations. "Multicast" SJSpawn has multiple session types.
 * 
 * Now also different because channels are permitted targets.
 *
 */
public interface SJSpawn extends Call //, SJSessionOperation //, SJBasicOperation // Currently doesn't invoke the SJ Runtime (although this itself is not a problem to be a SJBasicOperation, but other passes that work on basic operations implicitly look for the socket arguments in the specific positions). 
{
	public List targets(); // Duplicated from SJSessionOperation.
	public SJSpawn targets(List targets);
	
	public List<String> sjnames(); // Duplicated from SJSessionOperationExt.
	public SJSpawn sjnames(List<String> sjnames);
	
	public List<SJSessionType> sessionTypes(); // FIXME: should be factored out into an extension object (for multiply types session operations).
	public SJSpawn sessionTypes(List<SJSessionType> sts);
}
