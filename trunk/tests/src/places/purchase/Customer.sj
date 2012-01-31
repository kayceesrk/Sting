//$ bin/sessionjc -cp tests/classes/ tests/src/places/purchase/Customer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.purchase.Customer s s localhost 9999

package places.purchase;

import java.net.*;
import java.util.*;
import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/**
 * Customer of an online shopping company.
 * 
 * @author Nuno Alves
 *
 */
class Customer {
	
	final noalias protocol options {
			!{
				PURCHASE: ![!<Product>.?(int)]*,
				BASKET: ,
				OTHER:
			} 
	}
	
	final noalias protocol customerToVendor {
		cbegin.
			?(List).
			![@(options)]*.
			!{
				CHECKOUT: !<CreditCard>.?(Receipt),
				EXIT:
			}
	}
	
	private String setups;
	private String transports;
	private int port_a;
	private String addr_a;
	
	private List products;
	private Map basket;
	private Scanner in;
	
	private CreditCard CARD = new CreditCard("Mike Yoon", "8798 2839 2934 2940", "293");

	public Customer(String setups, String transports, String addr_a, int port_a) {
		this.setups = setups;
		this.transports = transports;
		this.port_a = port_a;
		this.addr_a = addr_a;
		
		products = new LinkedList();
		basket = new HashMap();
		in = new Scanner(System.in);
	}
	
	public void run() throws Exception {
		final noalias SJService c_cs = SJService.create(customerToVendor, addr_a, port_a);
		final noalias SJSocket s_cs;
		
		try (s_cs) {
			s_cs = c_cs.request(SJTransportUtils.createSJSessionParameters(setups, transports));
			
			products = (List) s_cs.receive();
			
			DisplayMenu();
			int command = in.nextInt();
			boolean checkout = false;
			
			s_cs.outwhile (command != 3 && !checkout) {
						
				if (command == 1) {
					s_cs.outbranch(PURCHASE) {
						//Purchase(s_cs);
						DisplayPurchase();
						
						int command2 = in.nextInt();
						s_cs.outwhile (command2 < products.size()+1 && command2 > 0) {

							Product p = (Product) products.get(command2-1);
							
							// Sends product ID
							s_cs.send(p);
							
							Integer val = (Integer) basket.get(p);
							if (val == null) {
								basket.put(p, new Integer(1));
							}
							else {
								basket.put(p, new Integer(val.intValue() + 1));
							}
							System.out.println("Added to basket: " + p);
							
							System.out.println("Total: " + s_cs.receiveInt());

							System.out.println("Press Enter to continue..");
							try {
								System.in.read();
							} catch (IOException e) {
								System.out.println("Input error!");
							}
							
							DisplayPurchase();
							command2 = in.nextInt();
						}
						
						if (command2 > products.size()+1 || command2 <= 0) {
							System.out.println("Invalid option.. Press Enter to return to main menu..");
							try {
								System.in.read();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				else if (command == 2) {
					s_cs.outbranch(BASKET) {
						System.out.println("Case 2");
						checkout = Basket();
					}
				}
				else {
					s_cs.outbranch(OTHER) {
						System.out.println("Invalid option");
					}
				}
							
				if (!checkout) {
					DisplayMenu();
					command = in.nextInt();
				}
			}
			
			if (checkout) {
				s_cs.outbranch(CHECKOUT) {					
					// Sends card details
					s_cs.send(CARD);
					
					System.out.println("Receipt:\n " + s_cs.receive());
					
					System.out.println("Thank you for shopping with us..");
				}
			}
			else {
				s_cs.outbranch(EXIT) {
					System.out.println("Hope to see you soon!");
				}
			}
			
		}
		finally	{
			
		}
	}
	
	private boolean Basket() {
		Iterator it = basket.keySet().iterator();
		
		ClearScreen();
		System.out.println("--------------");
		System.out.println("Current Basket");
		System.out.println("--------------");

		if (basket.isEmpty()) {
			System.out.println("EMPTY!");
			System.out.println("Press Enter to return to main menu");
			try {
				System.in.read();
			} catch (IOException e) {
				System.out.println("Input error!");
			}
		}
		else {
			while(it.hasNext()) {
				Product p = (Product) it.next();
				System.out.println(basket.get(p) + " " + p);
			}
			
			System.out.println("Proceed to checkout? (y/n)");
			String cont = in.next();
			if(cont.equals("y"))
				return true;
		}
		
		return false;
	}

	/*private void Purchase(final noalias !<Product>.!<Integer>.?(Double) s_cs) {
		
		DisplayPurchase();
						
		int command2 = in.nextInt();
		s_cs.outwhile (command2 < products.size()+1 && command2 > 0) {
			
			System.out.print("Insert quantity: ");
			
			int qty = in.nextInt();
			Product p = (Product) products.get(command2-1);
			
			// Sends product and quantity
			s_cs.send(p);
			s_cs.send(new Integer(qty));
			
			Integer val = (Integer) basket.get(p);
			if (val == null) {
				if (qty>0) {
					basket.put(p, new Integer(qty));
					System.out.println("Added to basket: " + p);
				}
			}
			else {
				int newqty = qty + val.intValue();
				if (newqty > 0) {
					basket.put(p, new Integer(newqty));
					System.out.println("Modified from basket: " + p);
				}
				else {
					basket.remove(p);
					System.out.println("Removed from basket: " + p);
				}
			}
			
			System.out.println("Current total: " + s_cs.receive());

			System.out.println("Press Enter to continue..");
			try {
				System.in.read();
			} catch (IOException e) {
				System.out.println("Input error!");
			}
			
			DisplayPurchase();
			command2 = in.nextInt();
		}
		
		if (command2 > products.size()+1 || command2 <= 0) {
			System.out.println("Invalid option.. Press Enter to return to main menu..");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	private void DisplayPurchase() {
		Iterator it = products.iterator();
		int counter = 1;

		ClearScreen();
		System.out.println("Choose one of the products available:");

		while(it.hasNext())
			System.out.println(counter++ + " - " + it.next());

		System.out.println(counter + " - Exit to main menu");		
	}
	
	private void DisplayMenu() {
		ClearScreen();
		System.out.println("Welcome to Online Shopping");
		System.out.println("--------------------------");
		System.out.println("Choose one of the following options:");
		System.out.println("1 - Purchase products");
		System.out.println("2 - View current basket");
		System.out.println("3 - Exit");
	}
	
	private void ClearScreen() {
		String clearScreenCommand = null;
		if( System.getProperty( "os.name" ).startsWith( "Window" ) )
			clearScreenCommand = "cls";
		else
			clearScreenCommand = "clear";

		try {
			Runtime.getRuntime().exec( clearScreenCommand );
		} catch (Exception e) {
			for (int i=0; i<100; i++)
				System.out.print("\n");
		}
	}
	
	public static void main(String[] args) throws Exception	{
		String setups = args[0];
		String transports = args[1];
		
		String host_a = args[2];
		int port_a = Integer.parseInt(args[3]);
		
		new Customer(setups, transports, host_a, port_a).run();
	}
}
