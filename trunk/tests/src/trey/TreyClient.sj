//$ bin/sessionjc tests/src/trey/TreyClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/:. trey.TreyClient localhost

package trey;

import java.net.UnknownHostException;
import java.io.*;
import java.lang.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class TreyClient extends JFrame implements ActionListener{

	/* Client protocol is defined by a recursive send and receive behavior */
	private final noalias protocol _c {cbegin.rec X[ !<int>.!{LOST: , WIN: , CONT: ?(int).?{LOST: , WIN: , CONT: #X}}]} 

	/* Values read by the client frame and read by the server */
	private int pipe = -1;
	/* Lock */
	MyInteger lock = new MyInteger(0);

	/* Frame buttons */
	private JButton buttons[];
	/* Client and Server moves*/
	private int moves[];

	public TreyClient(String server){

		/* Create and set up the Client Socket*/
		final noalias SJService s_address = SJService.create(_c, server, 4444);
		noalias SJSocket theSocket;		

		try (theSocket){
		
			/* Set up the connection*/
			theSocket = s_address.request();

			/* Set up the array of moves */
	    		moves = new int[9];
	    		for(int i = 0; i < 9; i ++)	    	
	    			moves[i] = -1;
			
			/* Set up and start the frame */
			show_frame();

			int _receive = 0;

			theSocket.recursion(X){


				/* Send the abstraction of the button selected by client and checks for winner */
				synchronized(lock){
					try{
					while(lock.getInt() == 0)
						lock.wait();	
					}catch(InterruptedException ie){System.err.println("TreyClient: "+ie);}
				}	
				System.out.println("Client Sending: " +pipe);

				theSocket.send(pipe);
	
				if(check() == 0){
					
					theSocket.outbranch(LOST){}

				}
	

				else if(check() == 3 ) {
					
					theSocket.outbranch(WIN){}

				}
			
				else{

					theSocket.outbranch(CONT){	
						
						/* Receive the asbtraction of the button selected by server and checks for winner*/					   

						_receive = theSocket.receiveInt();
						System.out.println("Client Receiving: "+_receive);
						buttons[_receive].removeActionListener(this);
	    					buttons[_receive].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
						moves[_receive] = 1;							
						theSocket.inbranch(){

							case LOST: {notify(3);}

							case WIN: {notify(0);}
							
							case CONT: { 
									synchronized (lock) {
	    									lock.setInt(0);
	    								}
									theSocket.recurse(X);
								}
						
						}
					}
				}
			}
		}catch(SJIncompatibleSessionException ise){System.err.println("TreyClient: Non Dual Behavior: "+ise );
		}catch(SJIOException ioe){System.err.println("TreyClient: Communication Error: "+ioe);
		
		}finally{


		}
	}
	 
	 
	 public void show_frame() {
		
		/* Exit the application when the close button is clicked */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		//Create and set the frame
	    	setTitle("Client Trey");
	    	setLayout(new BorderLayout());
	    
	    	/* Set up the content pane */
	    	this.addComponentsToPane(this.getContentPane());
	    
	    	/* Starting the frame */
	    	this.pack();
	    	this.setVisible( true );
	 }
	 
	 public void addComponentsToPane(final Container pane){
		 
		 //Create and set the grid
		 GridLayout theGrid = new GridLayout(3,3,5,5); 		    
		 JPanel panel = new JPanel();
		 panel.setLayout(theGrid);
		 
		 buttons = new JButton[9];
		 
		 for(int i =0; i<9; i++){
				
			buttons[i] = new JButton(String.valueOf(i));
			buttons[i].setPreferredSize(new Dimension(130, 130));
			buttons[i].setActionCommand(String.valueOf(i));
			buttons[i].addActionListener(this);
			panel.add(buttons[i]);
		 }
		 pane.add(panel, BorderLayout.NORTH);
	 }
	 
	 public void actionPerformed(ActionEvent e){
	      	
	    int action = (Integer.decode(e.getActionCommand())).intValue();
	    	
	    
	    synchronized(lock){
	    	switch (action){
	    	
	    	case 0 : if(lock.getInt() == 0){
	    		
	        	buttons[0].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[0].removeActionListener(this);
			pipe = 0;	
	        	moves[0] = 0;
			lock.setInt(1);}
	        	break;
	    	case 1 : if(lock.getInt() == 0){
	    		
	        	buttons[1].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[1].removeActionListener(this);
			pipe = 1;
	        	moves[1] = 0;
			lock.setInt(1);}
	        	break;
	    	case 2 : if(lock.getInt() == 0){
	    		
	        	buttons[2].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[2].removeActionListener(this);	
			pipe = 2;
	        	moves[2] = 0;
			lock.setInt(1);}
	        	break;
	    	case 3 : if(lock.getInt() == 0){
	    		
	        	buttons[3].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[3].removeActionListener(this);
			pipe = 3;
	        	moves[3]=0;
			lock.setInt(1);}
	        	break;
	    	case 4 : if(lock.getInt() == 0){
	    		
	            	buttons[4].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[4].removeActionListener(this);
			pipe = 4;
	        	moves[4]=0;
			lock.setInt(1);}
	        	break;
	    	case 5 : if(lock.getInt() == 0){
	    		
	        	buttons[5].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[5].removeActionListener(this);
			pipe = 5;
	        	moves[5]=0;
			lock.setInt(1);}
	        	break;
	    	case 6 : if(lock.getInt() == 0){
	    		
	        	buttons[6].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[6].removeActionListener(this);
			pipe = 6;
	        	moves[6]=0;
			lock.setInt(1);}
	        	break;
	    	case 7 : if(lock.getInt() == 0){
	    
	        	buttons[7].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[7].removeActionListener(this);
			pipe = 7;
	        	moves[7]=0;
			lock.setInt(1);}
	        	break;
	    	case 8 : if(lock.getInt() == 0){
	    		
	        	buttons[8].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	        	buttons[8].removeActionListener(this);
			pipe = 8;
	        	moves[8]=0;
			lock.setInt(1);}
	        	break;
	    	default : break;
	    	}
		lock.notifyAll();

	    }
	 }
	 
	 public int check(){
		 
		 int sum = 0;
		 /*Horizontal check*/
		 for (int j = 0;j <= 6; j = j+3){
			 sum = 0;
			 for (int i = j; i <= j + 2; i ++){
				 if(moves[i] == -1){
					 sum = -1; break;}
				 else
					 sum +=moves[i];
			 }
			 if (sum == 0 || sum == 3){
				 notify(sum);
				 return sum;
			 }
		 }
		 /* Vertical check*/
		for(int j = 0; j < 3; j ++){
			
			sum = 0;
			for(int i = j; i <= j + 6; i = i + 3){
				
				if(moves[i] == -1)
					 {sum = -1; break;}
				 else
					 sum +=moves[i];
			}
			if (sum == 0 || sum == 3){
				 notify(sum);
				 return sum;
			 }
		}
		
		/*Diagonal check*/
		sum = 0;
		for(int i = 0; i <= 8; i = i + 4)
			if(moves[i] == -1)
				{sum = -1;break;}
			else
				sum += moves[i];
		
		if (sum == 0 || sum == 3){
			 notify(sum);
			 return sum;
		 }
		sum = 0;
		for(int i = 2; i <= 6; i = i + 2)
			if(moves[i] == -1)
				{sum = -1; break;}
			else
				sum += moves[i];
		
		if (sum == 0 || sum == 3){
			 notify(sum);
			 return sum;
		 }
		return sum;
	 }
	 
	 public void notify(int result){

		 if(result == 0)			 
			 JOptionPane.showMessageDialog(null, "YOU WON!!!", "Client Result", JOptionPane.INFORMATION_MESSAGE);
		 else
			 JOptionPane.showMessageDialog(null, "YOU LOST!!!", "Client Result", JOptionPane.INFORMATION_MESSAGE);
	 }
	

	public static void main( String [] args ){
		
		/* Creates and sets up connection and graphical frame */
	    	TreyClient frame = new TreyClient(args[0]);
	}
}

class MyInteger implements Serializable{

	private int n;
	public MyInteger(int n){
		
		this.n= n;
	}
	
	public void setInt(int m){
		
		n = m;
	}
	
	public int getInt(){
		
		return n;
	}
}