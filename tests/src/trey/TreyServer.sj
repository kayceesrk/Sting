//$ bin/sessionjc tests/src/trey/TreyServer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes:. trey.TreyServer

package trey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class TreyServer extends JFrame implements ActionListener
{
	private final noalias protocol _s 
	{
		sbegin
		.rec X[
		  ?(int)
		  .?{
		  	LOST: ,
		    WIN: ,
		    CONT: 
		    	!<int>.
		    	!{
		    		LOST: , 
		    		WIN: , 
		    		CONT: #X
		    	}		    	
		  } 
		]
	}

	private JButton buttons[];
	private int moves[];

	/* Values read by the server frame and read by the client */
	private int pipe = -1;

	/* Lock used to control the order of [receive-send] */
	private MyInteger lock = new MyInteger(1);

	public TreyServer()
	{
		final noalias SJServerSocket ss;
		noalias SJSocket theSocket;
		
		try (ss)
		{
			/* Create and set up the server socket*/
			ss = SJServerSocketImpl.create(_s, 4444);

			try (theSocket)
			{
				/* Accept the connection from client */
				theSocket = ss.accept();
			
				/* Set up the array of moves */
    		moves = new int[9];
    		for(int i = 0; i < 9; i ++)	    	
    			moves[i] = -1;
			
				/* Set up and start the frame */
				show_frame();
				
				int _receive;
	
				theSocket.recursion(X)
				{
					_receive = theSocket.receiveInt();
					System.out.println("Server Receiving: "+_receive);
					buttons[_receive].setIcon(new ImageIcon("./tests/src/trey/RedBall.gif"));
	  			buttons[_receive].removeActionListener(this);
	  			moves[_receive] = 0;
	
					theSocket.inbranch()
					{	
						case LOST: 
						{
							notify(0);
						}
						
						case WIN: 
						{
							notify(3);
						}
					
						case CONT: 
						{
							synchronized(lock)
							{
  							lock.setInt(0);
							}
							
							synchronized(lock)
							{
								try
								{
									while(lock.getInt() == 0) //Make sure that a button has been pressed by user				
										lock.wait();
								}
								catch (InterruptedException ie)
								{
									System.err.println("TreyServer: "+ie);
								}								
							}
						
							System.out.println("Server Sending: "+pipe);
							
							theSocket.send(pipe);
							
							if(check() == 3) 
							{
								theSocket.outbranch(LOST)
								{
									notify(3);
								}
							}
							else if (check() == 0) 
							{
								theSocket.outbranch(WIN)
								{
									notify(0);							
								}
							}
							else
							{
								theSocket.outbranch(CONT)
								{				
									theSocket.recurse(X);
								}
							}
						}
					}
				}
			}
			catch (SJIOException ioe)
			{
				System.err.println("TreyServer: Settig up Server Error "+ioe);
			}
			catch (SJIncompatibleSessionException ise)
			{
				System.err.println("TreyServer: Non Dual Behavior: "+ise );
			}
		}
		catch (SJIOException sioe)
		{
			System.err.println("TreyServer: Communication Error: "+sioe);		
		}
		finally
		{

		}
	}

	public void show_frame() {
		
		
		/* Exit the application when the close button is clicked */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		
		//Create and set the frame
	    	this.setTitle("Server Trey");
	    	setLayout(new BorderLayout());
	    
	    	/* Set up the content pane */
	    	this.addComponentsToPane(this.getContentPane());
	    	this.setLocation(600,0);
	    
	    
	    	/* Starting the frame */
	    	this.pack();
	    	this.setVisible( true );
	    	
	 }
	
	public void addComponentsToPane(final Container pane) {
		 
		 //Create and set the grid
		 GridLayout theGrid = new GridLayout(3,3,5,5); 		    
		 JPanel panel = new JPanel();
		 panel.setLayout(theGrid);
		 
		 /* Set up the buttons */
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
		    		
		        	buttons[0].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[0].removeActionListener(this);
		        	moves[0] = 1;
				pipe = 0;
				lock.setInt(1);}
		        	break;
		    	case 1 : if(lock.getInt() == 0){
		    		
		        	buttons[1].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[1].removeActionListener(this);
		        	moves[1] = 1;
		        	pipe = 1;
				lock.setInt(1);}
		        	break;
		    	case 2 : if(lock.getInt() == 0){
		    		
		        	buttons[2].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[2].removeActionListener(this);
		        	moves[2] = 1;
		        	pipe = 2;
				lock.setInt(1);}
		        	break;
		    	case 3 : if(lock.getInt() == 0){
		    		
		        	buttons[3].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[3].removeActionListener(this);
		        	moves[3] = 1;
		        	pipe = 3;
				lock.setInt(1);}
		        	break;
		    	case 4 : if(lock.getInt() == 0){
		    		
		            	buttons[4].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[4].removeActionListener(this);
		        	moves[4] = 1;
		        	pipe = 4;
				lock.setInt(1);}
		        	break;
		    	case 5 : if(lock.getInt() == 0){
		    		
		        	buttons[5].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[5].removeActionListener(this);
		        	moves[5] = 1;
		        	pipe = 5;
				lock.setInt(1);}
		        	break;
		    	case 6 : if(lock.getInt() == 0){
		    		
		        	buttons[6].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[6].removeActionListener(this);
		        	moves[6] = 1;
		        	pipe = 6;
				lock.setInt(1);}
		        	break;
		    	case 7 : if(lock.getInt() == 0){
		    		
		        	buttons[7].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[7].removeActionListener(this);
		        	moves[7] = 1;
		        	pipe = 7;
				lock.setInt(1);}
		        	break;
		    	case 8 : if(lock.getInt() == 0){
		    		
		        	buttons[8].setIcon(new ImageIcon("./tests/src/trey/BlackBall"));
		        	buttons[8].removeActionListener(this);
		        	moves[8] = 1;
		        	pipe = 8;
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
			 return sum;
		 }
		sum = 0;
		for(int i = 2; i <= 6; i = i + 2)
			if(moves[i] == -1)
				{sum = -1; break;}
			else
				sum += moves[i];
		
		if (sum == 0 || sum == 3){
			 return sum;
		 }

		return sum;
	 }

	 public void notify(int result) throws HeadlessException{
		 
		 if(result == 3)
			 
			 JOptionPane.showMessageDialog(null, "YOU WON!!!", "Server Result", JOptionPane.INFORMATION_MESSAGE);
		 else
			 JOptionPane.showMessageDialog(null, "YOU LOST!!!", "Server Result", JOptionPane.INFORMATION_MESSAGE);
	 }

	public static void main(String args[]){

		new TreyServer();
	}
}

class MyInteger implements Serializable
{
	private int n;
	
	public MyInteger(int n)
	{	
		this.n = n;
	}
	
	public void setInt(int m)
	{		
		n = m;
	}
	
	public int getInt()
	{	
		return n;
	}
}
