//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.java.event.server.Server false 8888 1

package ecoop.bmarks.java.event.server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import ecoop.bmarks.*;

public class Server 
{
	public static final int REC = 1;
	public static final int QUIT = 2;

	public static int signal = MyObject.NO_SIGNAL;
	public static boolean counting = false;
	public static boolean counted = false;
	
	private static boolean debug;
		
  private int port;
  private int numClients; // NB: a TimerClient counts as two clients.
  
  private long count = 0;  

  private List keyUpdates = new LinkedList();
  
  public Server(boolean debug, int port, int numClients) 
  {
  	Server.debug = debug;
  	
    this.port = port;
    this.numClients = numClients;
  }

  public void run() throws Exception 
  {
    Selector sel = null;
    
    ServerSocketChannel ssc = null;    
    
    try 
    {
      sel = Selector.open();

      ssc = ServerSocketChannel.open();
      ssc.configureBlocking(false);
      ssc.socket().bind(new InetSocketAddress(port));

      ssc.register(sel, SelectionKey.OP_ACCEPT);

      debugPrintln("[Server] Listening on: " + port);
      
      runSelectorLoop(sel);
    } 
    finally
    {
    	if (counted)
			{
				System.out.println("[Server] Total count: " + count);
			}    	
    	
    	if (sel != null)
    	{
    		sel.close(); // Closes the channels inside it?
    	}
    }
  }
  
  private void runSelectorLoop(Selector sel) throws Exception  
  {  	
    while (numClients > 0) 
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

	private void handleAccept(Selector sel, SelectionKey key) throws Exception
	{
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		 
		SocketChannel sc = ssc.accept();
		 
		sc.configureBlocking(false);
		 
		/*Socket s = sc.socket();
		
		s.setTcpNoDelay(true); // Needed?*/ 
		 
		sc.register(sel, SelectionKey.OP_READ).attach(new Attachment());
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

    	if (i == REC)
    	{
    		a.state = Attachment.GET_HEADER;
    	}
    	else //if (i == QUIT)
    	{
    		numClients--;
				
        key.cancel();
	      key.channel().close();
    		
				debugPrintln("[Server] Clients remaning: " + numClients);
				
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
    	
    	ClientMessage cm = (ClientMessage) deserializeObject(bs);
    	
    	debugPrintln("[Server] Received: " + cm);
      
      prepareSend(sel, key, new MyObject(signal, cm.getSize()));
    	
    	a.resetReadState(); // Read buffer is ready for writing again.   	
    }
  }
  
  private void prepareSend(Selector sel, SelectionKey key, MyObject mo) throws Exception
  {
  	Attachment a = (Attachment) key.attachment();
  	
  	byte[] bs = serializeObject(mo);  	
  	byte[] bs1 = new byte[bs.length + 4];
  	
  	ByteBuffer bb = ByteBuffer.allocate(bs1.length);
  	
  	bb.put(intToByteArray(bs.length));
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
      if (counting) 
      {
        count++;
        
        debugPrintln("[Server] Current count:" + count); // This is the only non-static part of this method.		            
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
  	
  	return byteArrayToInt(bs);  	
  }	
  
	public static int byteArrayToInt(byte[] bs) 
	{
		int i = 0;
		
		i += (bs[0] & 255) << 24;
		i += (bs[1] & 255) << 16;
		i += (bs[2] & 255) << 8;
		i += bs[3] & 255;
		
		return i;		
	}	
  
  public static final byte[] intToByteArray(int value) 
  {
    return new byte[] {
			(byte)(value >>> 24),
			(byte)(value >>> 16),
			(byte)(value >>> 8),
			(byte)value
		};
  }	
  
	public static final byte[] serializeObject(Object o) throws Exception
	{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		
		try 
		{
			baos = new ByteArrayOutputStream(); // Could optimise by reusing the byte array.
			oos = new ObjectOutputStream(baos);
			
			oos.writeObject(o);
			oos.flush();
			
			return baos.toByteArray();
		} 
		finally 
		{
			if (oos != null)
			{
				oos.close();
			}
			
			if (baos != null)
			{
				baos.close();
			}
		}		
	}
	
	public static final Object deserializeObject(byte[] bs) throws Exception
	{
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		
		try
		{
			bais = new ByteArrayInputStream(bs);
			ois = new ObjectInputStream(bais);
			
			return ois.readObject();
		}
		finally
		{
			if (bais != null)
			{
				bais.close();
			}
			
			if (ois != null)
			{
				ois.close();
			}
		}
	}	

  private static final void debugPrintln(String m)
  {
  	if (debug)
  	{
  		System.out.println(m);
  	}
  }
	
  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0]);
  	int port = Integer.parseInt(args[1]);
  	int numClients = Integer.parseInt(args[2]);
  	
    new Server(debug, port, numClients).run();
  }
}
