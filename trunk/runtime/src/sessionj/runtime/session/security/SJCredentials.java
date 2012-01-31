package sessionj.runtime.session.security;

import java.io.Serializable;

public class SJCredentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user;
	private String pwd;
	private String index;
	private byte salt[];
	
	public SJCredentials(String user, String pwd, String index, byte salt[]) {
		this.user = user;
		this.pwd = pwd;
		this.index = index;
		this.salt = salt;
	}
	
	public SJCredentials() {
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

}
