package sessionj.extension.sesstypes;

import sessionj.types.sesstypes.*;

//If protocol declarations etc had extensions extended from this, this should be abstract.
public class SJNamedExt_c extends SJTypeableExt_c implements SJNamedExt
{
	private String sjname;

	public SJNamedExt_c(SJSessionType st, String sjname) 
	{
		super(st);
		
		this.sjname = sjname;
	}

	public String sjname()
	{
		return sjname;
	}
}
