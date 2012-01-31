//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.java.event.server.Server false 8888

package ecoop.bmarks2.micro.java.event.server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import ecoop.bmarks2.micro.*;

public class Server extends ecoop.bmarks2.micro.Server 
{
	volatile private boolean run = true;
	volatile private boolean kill = false;
  volatile private boolean finished = false;
	
  private List keyUpdates = new LinkedList();
	
	private Selector sel;
  
	private int tid = 0;
	
  public Server(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception 
  {
    ServerSocketChannel ssc = null;    
    
    try 
    {
      sel = Selector.open();

      ssc = ServerSocketChannel.open();
      ssc.configureBlocking(false);
      ssc.socket().bind(new InetSocketAddress(getPort()));

      ssc.register(sel, SelectionKey.OP_ACCEPT);

      debugPrintln("[Server] Listening on: " + getPort());
      
      runSelectorLoop(sel);
    } 
    finally
    {
    	if (sel != null)
    	{
    		sel.close(); // Closes the channels inside it?
    	}
    }
  }
 
  public void kill() throws Exception
  {
  	int numClients = getNumClients(); // Unlike java.thread.Server, this will only tell us the number of LoadClients currently connected, not the number of TimerClients that had also previously connected. That's alright, the TimerClient (an individual) has already finished by now.
  	
  	this.kill = true;
  	
  	while (getNumClients() > 0);

  	this.run = false; // Can stop the selector loop after all LoadClients have quit.
  	
		sel.close(); // Break the selecting loop forcibly if needed.
		
		while (!this.finished);
  	
  	//debugPrintln("[Server] Finished running (" + numClients + " Clients joined).");
		System.out.println("[Server] Finished running (" + numClients + " Clients joined).");
  }  
  
  private void runSelectorLoop(Selector sel) throws Exception  
  {  	
  	try
  	{
	    while (this.run) 
	    {
	      sel.select();
	
	      processKeyUpdates(sel);
	      
	      for (Iterator keys = sel.selectedKeys().iterator(); keys.hasNext(); ) 
	      {
	        SelectionKey key = (SelectionKey) keys.next();
	        
	        keys.remove();
	
	        if (key.isValid())
	        {
	          if (key.isAcceptable()) 
	          {
	          	handleAccept(sel, key);
	          }
	          else if (key.isReadable()) 
	          {	            
	          	handleRead(sel, key);	          
	          }
						else if (key.isWritable())
						{
							handleWrite(key);
						}
						else
						{
							throw new RuntimeException("[Server] Unexpected key type: " + key);
						}	          
	        }
	      }
	    }
  	}
  	catch (ClosedSelectorException cse)
  	{
  		//cse.printStackTrace();
  	}
  	
  	this.finished = true;
  }

	private void handleAccept(Selector sel, SelectionKey key) throws Exception
	{
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		 
		SocketChannel sc = ssc.accept();
		 
		sc.configureBlocking(false);
		 
		/*Socket s = sc.socket();
		
		s.setTcpNoDelay(true); // Needed?*/ 
		 
		sc.register(sel, SelectionKey.OP_READ).attach(new Attachment(tid++));
		
		addClient();
	}

  private void handleRead(Selector sel, SelectionKey key) throws Exception
  {    
    read(key);
    
    doReceive(sel, key);
  }
  
  private void handleWrite(SelectionKey key) throws Exception  
  {
  	write(key);
  }

  private void doReceive(Selector sel, SelectionKey key) throws Exception
  {
    Attachment a = (Attachment) key.attachment();
    
  	ByteBuffer bb = (ByteBuffer) a.readBuffer.flip(); // Make buffer ready for reading.
    
    if (a.state == Attachment.GET_LABEL)
    {
    	if (bb.remaining() < 4)
	    {      
    		a.readBuffer.compact(); // Buffer is ready for writing again.
    		
	    	return;
	    }
    	
    	int i = parseInt(bb); // We get the buffer back in reading mode.

    	if (i == Common.REC)
    	{
    		a.state = Attachment.GET_HEADER;
    	}
    	else //if (i == Common.QUIT)
    	{
    		removeClient();
				
        key.cancel();
	      key.channel().close();
    		
				debugPrintln("[Server] Clients remaning: " + getNumClients());
				
				return;
    	}
    }
    
    if (a.state == Attachment.GET_HEADER)
    {
    	if (bb.remaining() < 4)
	    {      
    		a.readBuffer.compact(); // Buffer is ready for writing again.
    		
	    	return;
	    }
    	
    	a.numToRead = parseInt(bb);     	
    	a.state = Attachment.GET_MESSAGE;
    }
    
    if (a.state == Attachment.GET_MESSAGE)
    {
    	if (bb.remaining() < a.numToRead)
    	{
    		a.readBuffer.compact(); // Buffer is ready for writing again.
    		
    		return;
    	}
    	
    	byte[] bs = new byte[a.numToRead];
    	
    	bb.get(bs);
    	
    	ClientMessage cm = (ClientMessage) Common.deserializeObject(bs);
    	
    	debugPrintln("[Server] Received: " + cm);
      
      prepareSend(sel, key, new ServerMessage(cm.getServerMessageSize(), this.kill));
    	
    	a.resetReadState(); // Read buffer is ready for writing again.   	
    }
  }
  
  private void prepareSend(Selector sel, SelectionKey key, ServerMessage sm) throws Exception
  {
  	Attachment a = (Attachment) key.attachment();
  	
  	byte[] bs = Common.serializeObject(sm);  	
  	byte[] bs1 = new byte[bs.length + 4];
  	
  	ByteBuffer bb = ByteBuffer.allocate(bs1.length);
  	
  	bb.put(Common.serializeInt(bs.length));
  	bb.put(bs);
  	
  	bb.flip(); 
  	
    a.setWriteBuffer(bb);
            
    synchronized (keyUpdates)
    {
    	this.keyUpdates.add(new KeyUpdate((SocketChannel) key.channel(), KeyUpdate.CHANGE_OPS, SelectionKey.OP_WRITE));
    }
    
    sel.wakeup();
  }
  
	private static void read(SelectionKey key) throws IOException
	{
		SocketChannel sc = (SocketChannel) key.channel();

    Attachment a = (Attachment) key.attachment();		
		
		int numRead = sc.read(a.readBuffer);

		if (numRead == -1)
		{
			key.cancel();				
			sc.close();

			throw new RuntimeException("[Server] Connection failed: " + key);
		}
	}
	
	private /*static */void write(SelectionKey key) throws IOException
	{
		SocketChannel sc = (SocketChannel) key.channel();
		
		Attachment a = (Attachment) key.attachment();
		
		ByteBuffer bb = a.writeBuffer;
		
		while (bb.remaining() > 0) // Comment this line to get proper asynchronous writing; otherwise we will loop here until everything is written.
		{
			sc.write(bb); // Asynchronous writing: may not write everything or indeed anything.
		}

		if (bb.remaining() == 0)
		{
      if (isCounting()) 
      {
      	incrementCount(a.tid);
        
       	debugPrintln("[ServerThread] Current count:" + getCountTotal());		            
      }
			
			//bb.clear(); // Unnecessary.
			
			key.interestOps(SelectionKey.OP_READ); // Implicity set interest op. back to "default" read mode after writing has finished (don't bother going through keyUpdates).
		}			
  }

  private void processKeyUpdates(Selector sel) throws Exception
	{
		synchronized (this.keyUpdates)
		{
			Iterator updates = this.keyUpdates.iterator();
	
			while (updates.hasNext()) // Assumes that we won't change the interest op. more than once per channel per iteration of selector loop (we don't because protocol is lockstep receive and send).
			{
				KeyUpdate update = (KeyUpdate) updates.next();
	
				switch (update.getAction())
				{
					case KeyUpdate.CHANGE_OPS:
					{
						SelectionKey key = update.getSocketChannel().keyFor(sel);
	
						if (key != null) // Client can fail in between sending something to Server and when Server gets around to processing the event?
						{
							key.interestOps(update.getInterestOps());
						}
	
						break;
					}
					default:
					{
						throw new RuntimeException("[Server] Unexpected key update action: " + update.getAction());
					}
				}
			}
	
			this.keyUpdates.clear();
		}
	}   

  // Takes the buffer in reading mode; leaves the buffer ready for further reading.
  private static final int parseInt(ByteBuffer bb) throws Exception
  {
  	byte[] bs = new byte[4];
  	
  	bb.get(bs);  	
  	
  	return Common.deserializeInt(bs);  	
  }	
  
  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	int port = Integer.parseInt(args[1]);
    
  	new Server(debug, port).run();
  }	  
}
