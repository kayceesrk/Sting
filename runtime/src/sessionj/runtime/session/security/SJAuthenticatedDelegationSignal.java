package sessionj.runtime.session.security;

import sessionj.SJConstants;

public class SJAuthenticatedDelegationSignal extends sessionj.runtime.session.SJDelegationSignal 
{
	private static final long serialVersionUID = SJConstants.SJ_VERSION;
	
	private String userName;
	private String pwd;
	
	public SJAuthenticatedDelegationSignal(String hostName, int port, String userName, String pwd)
	{
		super(hostName, port);
		
		this.userName = userName;
		this.pwd = pwd;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getPwd()
	{
		return pwd;
	}
	
	public String toString()
	{
		return super.toString() + ";" + userName + ":" + pwd; 
	}
}
