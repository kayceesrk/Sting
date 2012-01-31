package sessionj.extension.sessops;

import java.util.*;

import sessionj.extension.sesstypes.*;
import sessionj.types.sesstypes.*;

//If protocol declarations etc had extensions extended from this, this should be abstract.
public class SJSessionOperationExt_c extends SJTypeableExt_c implements SJSessionOperationExt
{
	private List<String> sjnames = new LinkedList<String>();

	public SJSessionOperationExt_c(SJSessionType st, List<String> sjnames) 
	{
		super(st);
		
		this.sjnames = sjnames;
	}

	public List<String> targetNames()
	{
		return sjnames;
	}
}
