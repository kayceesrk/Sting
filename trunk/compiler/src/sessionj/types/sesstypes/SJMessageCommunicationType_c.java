package sessionj.types.sesstypes;

import polyglot.types.*;
import polyglot.ast.*; //<By MQ>
import polyglot.util.Position; //<By MQ>

abstract public class SJMessageCommunicationType_c extends SJSessionType_c implements SJMessageCommunicationType
{
	private Type messageType; 

	//private long svuid = -1; // Factor out constant.
    //<By MQ>
    StackTraceElement[] elements;
    protected String target;
    public String target()
    {
	return target;
    }
    public void target(String target)
    {
	this.target = target;
    }
    //</By MQ>
	protected SJMessageCommunicationType_c(TypeSystem ts)
	{
		super(ts);
		elements = Thread.currentThread().getStackTrace(); //<By MQ>
	}
	
        public SJMessageCommunicationType_c(TypeSystem ts, String target,  Type messageType) throws SemanticException //<By MQ> added target
	{
		super(ts);

        this.messageType = messageType;
	this.target = target; //<By MQ>
	elements = Thread.currentThread().getStackTrace();
	if(target == null)
	{
	    System.out.println("target is null in constructor");
	}
    }
	
	public Type messageType()
	{
        Type t = messageType;
		
		return t instanceof SJSessionType ? ((SJSessionType) t).copy() : t;
	}

        public SJSessionType messageType(Type messageType) throws SemanticException
	{
		SJMessageCommunicationType mct = skeleton();
		if (messageType instanceof SJSessionType)
		{
			messageType = ((SJSessionType) messageType).copy(); // Only session type constructors cloned - pointer equality maintained for ordinary types.
		}
		((SJMessageCommunicationType_c) mct).messageType = messageType;
		return mct;
	}

	public boolean nodeWellFormed()
	{
        Type type = messageType;
		
		if (type instanceof SJSessionType)
		{
			if (type instanceof SJBeginType)
			{
				return ((SJSessionType) type).isWellFormed();
			}
			
			return ((SJSessionType) type).treeWellFormed();
		}
		else
		{
			return true;
		}
	}

	public SJSessionType nodeClone()
	{
		SJMessageCommunicationType mct = skeleton();
		/*if(target == null)
		  System.out.println("target is null\n\n");*/
		//<By MQ>
		//</By MQ>

		try
		{
		    mct = (SJMessageCommunicationType)mct.messageType(messageType); // Higher-order message types are copied by the setter method.
		    if(target != null)
			((SJMessageCommunicationType_c)mct).target = target; //new Id_c(target.position(), target.id()); //<By MQ>		
		    return mct;
		}
		catch (SemanticException se) // Not possible - any problems would have been raised when this object was orig. created.
		{
			throw new RuntimeException("[SJMessageCommunicationType_c] Shouldn't get in here.", se);
		}
	}
	
	public String nodeToString()
	{
        String message = messageType.toString(); // toString enough for messageType? or need to manually get full name?
	
	if(target == null)
	{
	    for (int i = 0; i < elements.length; i++)
		if(elements[i].toString().indexOf("session") != -1 && target == null)
		    System.out.println(elements[i]);
	}
	return target + ":" + messageCommunicationOpen() + message + messageCommunicationClose();//<By MQ>
	}

    abstract protected SJMessageCommunicationType skeleton(); 
	
	abstract protected String messageCommunicationOpen();
	abstract protected String messageCommunicationClose();
	
  public SJSessionType nodeCanonicalForm()  
  {

      Type mt = messageType();
  	
  	try
  	{
  		return mt instanceof SJSessionType ? messageType(((SJSessionType) mt).getCanonicalForm()) : this;
  	}
  	catch (SemanticException se)
  	{
  		throw new RuntimeException("[SJMessageCommunicationType_c] Shouldn't get in here: " + mt, se);
  	}
  }	
}
