//$ bin/sessionjc -cp tests/classes/ tests/src/places/purchase/Vendor.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.purchase.Vendor s s 8888 localhost 7777

package places.purchase;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/**
 * Shop of an online shopping company. After the purchase is complete, 
 * it delegates the session to the Payments department.
 * 
 * @author Nuno Alves
 *
 */
class Vendor {
	private static final String PRODUCTS_DATABASE = "c:\\cygwin\\home\\Raymond\\code\\java\\eclipse\\sessionj-hg\\tests\\src\\places\\purchase\\products.txt"; // Just a text file.
	
	private List products;
	private Map basket;
	private int total;
	
	private String setups;
	private String transports;
	private int port_a;
	private String addr_s;
	private int port_s;
	
	//private CreditCard dummy;
	
	final noalias protocol options {
		?{
			PURCHASE: ?[?(Product).!<int>]*,
			BASKET: ,
			OTHER:
		} 
	}
	
	final noalias protocol vendorToCustomer { 
		sbegin.
			!<List>.
			?[@(options)]*.
			?{
				CHECKOUT: ?(CreditCard).!<Receipt>,
				EXIT:
			}
	}
	
	final noalias protocol vendorToHandler { cbegin.!<?(CreditCard).!<Receipt> > }
	
	public Vendor(String setups, String transports, int port_a, String addr_s, int port_s) {
		this.setups = setups;
		this.transports = transports;
		this.port_a = port_a;
		this.addr_s = addr_s;
		this.port_s = port_s;
		
		products = new LinkedList();
		basket = new HashMap();
		readFile();
	}
	
	public void run() throws Exception {
		
		final noalias SJServerSocket ss_sc;
		
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(setups, transports);
		
		try (ss_sc) {
			ss_sc = SJServerSocketImpl.create(vendorToCustomer, port_a, params);
			
			while (true) {
				noalias SJSocket s_sc;
	
				try (s_sc) {
					s_sc = ss_sc.accept();
					
					basket.clear();
					total = 0;
					
					s_sc.send(products);
					
					s_sc.inwhile() {
						
						s_sc.inbranch() {
							case PURCHASE: 
							{
								s_sc.inwhile() {
									Product p = (Product)s_sc.receive();
									
									System.out.println("Requested product: " + p);
									
									int qty = 0;
									
									Integer val = (Integer)basket.get(p);
									if (val != null)
										qty = val.intValue();									
									
									basket.put(p, new Integer(qty));
									total += p.getPrice();
									
									s_sc.send(total);									
								}								
							}
							case BASKET: 
							{
								//System.out.println("BASKET");
							}
							case OTHER: 
							{
								//System.out.println("OTHER");
							}
						}
						
					}
					
					s_sc.inbranch() {
						case CHECKOUT: 
						{
							System.out.println("CHECKOUT");
							
							final noalias SJService c_sp = SJService.create(vendorToHandler, addr_s, port_s);
							final noalias SJSocket s_sp;

							try (s_sp) {
								s_sp = c_sp.request(params);
								
								System.out.println("Delegating to Payment Handler...");
								
								s_sp.pass(s_sc); // Delegation session to Payments. 
							}		
							finally { 
							
							}
						}
						case EXIT: 
						{
							System.out.println("EXIT");
						}
					}

				}		
				finally { 
					
				}
			}
		}
		finally	{
			
		}		
	}
	
	private void readFile() {
		File file = new File(PRODUCTS_DATABASE);
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;
		
	    try {
	        fis = new FileInputStream(file);
	        bis = new BufferedInputStream(fis);
	        dis = new DataInputStream(bis);

	        while (dis.available() != 0) {
	        	String line = dis.readLine();
	        	
	        	String[] split = line.split("\\s"); // Splits the string by white spaces
	        	String product = "";
	        	
	            for (int x=0; x<split.length-1; x++)
	            	product = product.concat(split[x] + " ");
	            
	            product = product.substring(0, product.length()-1); // Removes the last white space
	        	double price = Double.parseDouble(split[split.length-1]);
	        	
	        	products.add(new Product(product, price));
	        }

	        fis.close();
	        bis.close();
	        dis.close();

	      } catch (FileNotFoundException e) {
	    	  e.printStackTrace();
	      } catch (IOException e) {
	    	  e.printStackTrace();
	      }
	      
	}
	
	public static void main(String[] args) throws Exception {
		String setups = args[0];
		String transports = args[1];
		
		int port_a = Integer.parseInt(args[2]);
		String host_s = args[3];
		int port_s = Integer.parseInt(args[4]);
		
		// Include file as argument
		
		new Vendor(setups, transports, port_a, host_s, port_s).run();
	}	
}
