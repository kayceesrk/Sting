package places.purchase;

import java.io.Serializable;

class CreditCard implements Serializable 
{
	private String name;
	private String number;
	private String secCode;

	public CreditCard(String name, String number, String secCode) 
	{
		this.name = name;
		this.number = number;
		this.secCode = secCode;
	}

	public String toString() 
	{
		return number;
	}
	
	public String getName() 
	{
		return name;
	}
}
