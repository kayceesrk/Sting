/**
 * 
 */
package sessionj.runtime.session;

import sessionj.SJConstants;
import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.SJConnection;

import java.io.EOFException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Raymond
 *
 * Intended to be an easier to use and more convenient interface than the full SJSerializer. The serializer should wrap this formatter around the underlying I/O streams.
 *
 * For "simple" protocols, this component may not need to be stateful; but for many protocols, we probably need to implement a state machine here. For that purpose, message formatters are like a localised version of the protocol: the messages received by writeMessage should follow the dual protocol to the messages returned by readNextMessage. But the formatter is an object: need object-based session types to control this.
 * 
 * A less centralised option is to move formatting/parsing operations into each message type. The SJ Runtime can use session type monitoring to work out which message to expect and use the appropriate deserialization/parsing routine. Need to be a bit careful for inbranches, and with subtyping (if supported, need to be able to decode a subtype message using the supertype routine).   
 *
 * FIXME: need to really make clear the contracts (pre and post conditions, wrt. arguments and fields) of each method, particularly parseMessage, since that's the one the user must implement.
 * 
 */
abstract public class SJCustomMessageFormatter 
{		
	private SJConnection conn;
	
	private ByteBuffer bb; 
	
	{
		 bb = ByteBuffer.allocate(SJConstants.CUSTOM_MESSAGE_FORMATTER_INIT_BUFFER_SIZE); // Size should be at least greater than 0. // Called by SJCustomSerializer in a "sequentialised" way, i.e. no race conditions.
		 bb.flip();
	}
	
	//private LinkedList<Object> history = new LinkedList<Object>(); // Design not finished: no memory management considered yet for long running sessions, i.e. cannot keep all history forever. 
	
	// Maybe a bit weird for formatMessage to use a byte[] but parseMessage to use a ByteBuffer.
	abstract public byte[] formatMessage(Object o) throws SJIOException; // Maybe we should use e.g. SJCustomMessage (subclasses) rather than Object. SJCustomMessage could also offer message-specific formatting operations.
	abstract public Object parseMessage(ByteBuffer bb, boolean eof) throws SJIOException; // Instead of the eof flag, we could append a -1 to the bb. // Pre: bb should already be flipped, i.e. ready for (relative) "getting". Also has to be non-blocking (for readNextMessage to work as intended). // FIXME: this is maybe not a good interface for the user.
	// FIXME: at the moment, branch labels are still implicitly coupled to Strings in the upper layers (i.e. in the compiler), so parseMessage has to return labels as Strings (for label comparison to work). But later, should generalise to support arbitrary objects as labels.	
	
	protected final void bindConnection(SJConnection conn) // Called by SJCustomSerializer.
	{
		this.conn = conn;
	}
	
	/*public List<Object> getHistory()
	{
		return Collections.unmodifiableList(history); 
	}
	
	public Object lastMessageSent()
	{
		return history.peek();
	}*/
	
	protected final void writeMessage(Object o) throws SJIOException
	{
		conn.writeBytes(formatMessage(o));
		conn.flush();
		
		//history.push(o); // Not compatible with noalias reference passing.
	}
	
	// Pre: bb should be ready for reading.
	//public final SJMessage readNextMessage() throws SJIOException	
	protected final Object readNextMessage() throws SJIOException // FIXME: would be better if could implement using arrived.
	{		
		Object o = null;
		
		//f (bb.position() > 0)
		if (bb.remaining() > 0)
		{
			//o = parseMessage(getFlippedReadOnlyByteBuffer(bb), false);
			o = parseMessage(bb, false);
			
			if (o != null)
			{
				return o;
			}
		}
		else if (bb.position() == bb.limit() && bb.position() > 0) // Nothing to read (but user didn't call restoreReadButUnusedData) - not needed for correctness, just being efficient with space.
		{
			bb.clear();
		}
		else
		{
			unflipBuffer(bb);
		}
			
		try
		{
			//unflipBuffer(bb);
			bb.put(conn.readByte()); // Need at least one byte for a message. This gives the guarantee that parseMessage will not be called with empty arrays.
							
			//byte[] bs = copyByteBufferContents(bb);		
			
			//for (o = parseMessage(bs); o == null; o = parseMessage(bs)) // Assuming parseMessage returns null if parsing unsuccessful. (But what if we want to communicate a null?)
			//for (o = parseMessage(getFlippedReadOnlyByteBuffer(bb), false); o == null; o = parseMessage(getFlippedReadOnlyByteBuffer(bb), false)) // Assuming parseMessage returns null if parsing unsuccessful. (But what if we want to communicate a null?)
			for (o = parseMessage((ByteBuffer) bb.flip(), false); o == null; o = parseMessage((ByteBuffer) bb.flip(), false)) // Assuming parseMessage returns null if parsing unsuccessful. (But what if we want to communicate a null?)
			{		
				unflipBuffer(bb);
				
				if (bb.position() == bb.limit()) // Cannot be greater than.
				{
					bb.flip();
					
					ByteBuffer bbb = ByteBuffer.allocate(bb.capacity() * 2); // Crude reallocation scheme. Buffer never shrinks back.
					
					bbb.put(bb);
					
					bb = bbb;
				}
				
				bb.put(conn.readByte()); 
							
				//bs = copyByteBufferContents(bb);
			}
		}
		catch(SJIOException ioe)
		{
			if (ioe.getCause() instanceof EOFException)
			{	
				//o = parseMessage(getFlippedReadOnlyByteBuffer(bb), true); // "Last chance" to parse the message.
				o = parseMessage((ByteBuffer) bb.flip(), true); 
				
				if (o == null)
				{
					throw ioe;
				}
			}
		}
		finally
		{			

		}
		
		return o;
	}
	
	/*private static final ByteBuffer getFlippedReadOnlyByteBuffer(ByteBuffer bb)
	{
		ByteBuffer fbb = bb.asReadOnlyBuffer();
		
		fbb.flip();
		
		return fbb;
	}*/	

	private static void unflipBuffer(Buffer b)
	{
		int lim = b.limit();
		
		b.limit(b.capacity());
		b.position(lim);
	}
	
	protected static void restoreReadButUnusedData(ByteBuffer bb, int consumed) // Leaves buffer in reading mode.
	{
		bb.position(consumed);
		bb.compact();
		bb.flip();
	}
	
	/*private static final byte[] copyByteBufferContents(ByteBuffer bb)
	{
		bb.flip();
		
		byte[] bs = byteBufferToByteArray(bb);
		
		unflipByteBuffer(bb);
		
		return bs;
	}	
	
	// Pre: bb should already be flipped, i.e. ready for (relative) "getting".
	private static final byte[] byteBufferToByteArray(ByteBuffer bb)
	{
		byte[] bs = new byte[bb.limit()];
		
		bb.get(bs, 0, bs.length);		
		
		return bs;
	}

	private static final void unflipByteBuffer(ByteBuffer bb)
	{
		bb.position(bb.limit()); 
		bb.limit(bb.capacity());
	}*/
	
	/*class SJByteArray // Would like to avoid creating creating lots of new byte arrays all the time (as we do in byteBufferToByteArray), but also don't want to much work to be moved into parseMessage (e.g. we could give the ByteBuffer directly, but the user would have to handle it very carefully). 
	{
		public final int capacity;
		private int position = 0;
		
		private final byte[] bs; 
		
		public SJByteArray(int capacity)
		{
			this.capacity = capacity;
			this.bs = new byte[capacity];
		}
		
		public void read(ByteBuffer bb)
		{
			bb.get(bs, 0, bb.limit());
			
			position = bb.limit();
		}
		
		public byte[] ...
		
		public int capacity()
		{
			return capacity;
		}
		
		public int position()
		{
			return position;
		}
	}*/	
}
