package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.session.OngoingRead;
import sessionj.runtime.session.SJCustomMessageFormatter;
import sessionj.runtime.session.SJDeserializer;
import sessionj.runtime.util.SJRuntimeUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

public class CustomMessageFormatterFactory implements SJDeserializer { // Possibly more fitting to runtime.session subpackage.
    private static final Logger log = SJRuntimeUtils.getLogger(CustomMessageFormatterFactory.class);
    private final MessageFormatterOngoingRead messageFormatterOngoingRead;
	private static final byte[] BYTE = {};

	public CustomMessageFormatterFactory(SJCustomMessageFormatter cmf) {
        messageFormatterOngoingRead = new MessageFormatterOngoingRead(cmf);
    }
    
    public OngoingRead newOngoingRead() throws SJIOException {
        // Need to reuse the same message formatter
        return messageFormatterOngoingRead;
    }

	//RAY: modified to cache the data if parse unsuccessful, and also any left over (unconsumed) after a successful parse. 
	private class MessageFormatterOngoingRead implements OngoingRead 
	{
		private final SJCustomMessageFormatter messageFormatter;
		
		private Object parsed = null;
		
		private MessageFormatterOngoingRead(SJCustomMessageFormatter messageFormatter) 
		{
			this.messageFormatter = messageFormatter;
		}
		
		private byte[] consumed = BYTE; // A cache of the data from which a message is parsed. This *cannot* be obtained by "re-formatting" the message returned from parsing.
		private byte[] unconsumed = BYTE; // Holds the data that was not consumed the previous time we tried parsing. The issue is that SelectingThread needs all data to be read from the ByteBuffer argument before it will proceed: so we do so and cache it here. For this purpose, SelectingThread.consumeBytesRead is modified to use the same OngingRead all the time, otherwise, again, this unconsumed data will be lost (unless we do something like copy the unconsumed data from the old OngoingRead when we make the new one).   
		
		public void updatePendingInput(ByteBuffer newBytes, boolean eof) throws SJIOException 
		{
			byte[] allBytesArray = Arrays.copyOf(unconsumed, unconsumed.length + newBytes.remaining());
			
			newBytes.get(allBytesArray, unconsumed.length, newBytes.remaining()); // At this point, consumed holds the previously unconsumed data plus the new data just passed in. Also, newBytes' position will be at its limit.
		      		
			log.finest("Consuming input, messageFormatter = " + messageFormatter);
			
			ByteBuffer allBytes = ByteBuffer.wrap(Arrays.copyOf(allBytesArray, allBytesArray.length));
			
			parsed = messageFormatter.parseMessage(allBytes, eof);
			
			if (parsed != null)
			{
				int remaining = allBytes.remaining();
				int pos = newBytes.position(); // Should be at the limit. Gives how many new bytes of data were passed in this time.
			
				consumed = Arrays.copyOf(allBytesArray, allBytesArray.length - remaining);
				
				/*if (remaining >= pos) // If a message was successfully parsed purely out of the unconsumed bytes from last time. // Should be impossible: in that case, we would have parsed it last time.
				{
					newBytes.position(0);
				
					byte[] tmp = new byte[quux.length - remaining - pos];
					
					System.arraycopy(quux, unconsumed.length, tmp, 0, tmp.length);
					
					unconsumed = tmp;
				}
				else*/
				{
					newBytes.position(pos - remaining);
					
					unconsumed = BYTE;
				}
			}
			else
			{
				unconsumed = allBytesArray;
			}
			
			log.finer("Consumed input, parsed = " + parsed);
		}
		
		public boolean finished() 
		{
			return parsed != null;
		}
		
		public ByteBuffer getCompleteInput() throws SJIOException 
		{
			if (parsed != null) 
			{
				//return ByteBuffer.wrap(messageFormatter.formatMessage(parsed)); // Bad: parseMessage and formatMessage are not inverse operations.
				
				ByteBuffer bb = ByteBuffer.wrap(consumed);
			
				parsed = null;
				
				return bb;            	
			}
			else
			{
				return null;
			}
		}
  }
	//YAR
    
    /*private class MessageFormatterOngoingRead implements OngoingRead {
      private final SJCustomMessageFormatter messageFormatter;
      private Object parsed = null;
      private MessageFormatterOngoingRead(SJCustomMessageFormatter messageFormatter) {
          this.messageFormatter = messageFormatter;
      }

      @Override
      public void updatePendingInput(ByteBuffer bytes, boolean eof) throws SJIOException {
          log.finest("Consuming input, messageFormatter = " + messageFormatter);
          parsed = messageFormatter.parseMessage(bytes, eof);
          log.finer("Consumed input, parsed = " + parsed);
      }

      @Override
      public boolean finished() {
          return parsed != null;
      }

      @Override
      public ByteBuffer getCompleteInput() throws SJIOException {
          // FIXME: ugly hack
          if (parsed != null)
              return ByteBuffer.wrap(messageFormatter.formatMessage(parsed));
          else
              return null;
      }
  }*/   
}
