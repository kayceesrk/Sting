/**
 * 
 */
package sessionj.types.typeobjects;

import polyglot.frontend.*;
import polyglot.types.*;
import polyglot.visit.TypeBuilder;

import static sessionj.util.SJCompilerUtils.*;

/**
 * @author Raymond
 *
 */
public class SJParsedClassType_c extends ParsedClassType_c implements
    SJParsedClassType
{
	private boolean noAliasThroughThis = false;

	public SJParsedClassType_c(TypeSystem ts, LazyClassInitializer lci, Source s)
	{
		super(ts, lci, s);
	}
	
	/*public Job job()
	{  	  
		if (job == null)
		{
			//System.out.println("1b: " + this.initializer().isTypeObjectInitialized());	
		}
		
  	return super.job();		
	}
	
  public void setJob(Job job) 
  {  	
  	super.setJob(job);
  }*/
	
	public boolean noAliasThroughThis()
	{
		return noAliasThroughThis;	
	}

	public void setNoAliasThroughThis(boolean noAliasThroughThis)
	{
		this.noAliasThroughThis = noAliasThroughThis;	
	}
}
