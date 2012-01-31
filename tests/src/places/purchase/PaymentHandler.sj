//$ bin/sessionjc -cp tests/classes/ tests/src/places/purchase/PaymentHandler.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.purchase.PaymentHandler s s 7777

package places.purchase;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/**
 * Payment Handler of an online shopping company
 * 
 * @author Nuno Alves
 *
 */
class PaymentHandler {
	
	final noalias protocol handlerToCustomer { ?(CreditCard).!<Receipt> }
	final noalias protocol handlerToVendor { sbegin.?(@(handlerToCustomer)) }
	
	private String setups;
	private String transports;
	private int port;
	
	public PaymentHandler(String setups, String transports, int port) {
		this.setups = setups;
		this.transports = transports;
		this.port = port;
	}
	

	public void run() throws Exception {
		final noalias SJServerSocket ss_ps;
		
		try (ss_ps) {
			ss_ps = SJServerSocketImpl.create(handlerToVendor, port, SJTransportUtils.createSJSessionParameters(setups, transports)); 		
		
			while (true) {
				final noalias SJSocket s_ps;		
				final noalias SJSocket s_pc;
				
				try (s_pc, s_ps) {
					s_ps = ss_ps.accept();				

					s_pc = (@(handlerToCustomer)) s_ps.receive(s_ps.getParameters());
	
					CreditCard card = (CreditCard) s_pc.receive();
					
					System.out.println("Storing customer card information: " + card);
					
					Receipt receipt = new Receipt(card.getName(), card.toString());
					
					System.out.println("Receipt sent");
					
					s_pc.send(receipt);
				}				
				finally {
	
				}			
			}
		}
		finally	{
			
		}
	}

	public static void main(String[] args) throws Exception	{
		String setups = args[0];
		String transports = args[1];
		
		int port_s = Integer.parseInt(args[2]);
		
		new PaymentHandler(setups, transports, port_s).run();
	}		
}
