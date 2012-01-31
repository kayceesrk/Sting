package sessionj.ast;

import java.util.List;

import polyglot.ast.*;
import polyglot.util.Position;
import sessionj.ast.sessops.basicops.SJBasicOperation;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJLabel;

public class SJSpawn_c extends Call_c implements SJSpawn 
{	
	private List targets; // Duplicated from SJBasicOperation.
	private List<String> sjnames; // Duplicated from SJSessionOperationExt_c.
	private List<SJSessionType> sts;
	
	public SJSpawn_c(Position pos, Receiver target, Id name, List arguments, List targets)
	{
		super(pos, target, name, arguments);
		
		this.targets = targets;
	}
	
	public List targets()
	{
		return targets; 
	}
	
	public SJSpawn targets(List targets)
	{
		this.targets = targets;
		
		return this;
	}	
	
	public List<String> sjnames()
	{
		return sjnames;
	}
	
	public SJSpawn sjnames(List<String> sjnames)
	{
		this.sjnames = sjnames;
		
		return this;
	}
	
	public List<SJSessionType> sessionTypes()
	{
		return sts;
	}
	
	public SJSpawn sessionTypes(List<SJSessionType> sts)
	{
		this.sts = sts;
		
		return this;
	}
}
