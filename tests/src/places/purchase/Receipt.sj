package places.purchase;

import java.io.Serializable;

class Receipt implements Serializable 
{
	private String name;
	private String card;

	public Receipt(String name, String card) 
	{
		this.name = name;
		this.card = card;
	}

	public String toString() 
	{
		return "Mr/Ms: " + name + "\n Credit Card: " + card;
	}
}
