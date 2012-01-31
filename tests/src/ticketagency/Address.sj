package ticketagency;

import java.io.Serializable;

class Address implements Serializable 
{
	private String address;

	public Address(String address) 
	{
		this.address = address;
	}

	public String toString() 
	{
		return address;
	}
}
