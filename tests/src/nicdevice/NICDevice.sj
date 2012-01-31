//$ bin/sessionjc tests/src/nicdevice/NICDevice.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ nicdevice.NICDevice

package nicdevice;

import java.io.Serializable;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class NICDevice 
{
	private final noalias protocol NicEventBody 
	{ 
		rec READY[
			!<NicEvent>
			.?(AckEvent)
			.#READY
		] 
	}
	
	private final noalias protocol NicEvent 
	{ 
		cbegin
		.@(NicEventBody)
	}

	private final noalias protocol IOConfigureBegin
	{
		rec IO_CONFIGURE_BEGIN[            
			?(@(NicEvent)) // RegisterForEvents
			.?(SetParameters)
			.!{                     
				InvalidParameters: 
					!<InvalidParameters> // They have choice based on message type: we don't, so we have to encode using branch.
					.#IO_CONFIGURE_BEGIN,
				Success:
					?{                     
						StartIO: ,
						ConfigureIO: 
							#IO_CONFIGURE_BEGIN
					}				
			}			
		]	
	}

	private final noalias protocol IORunning
	{
		rec IO_RUNNING[		               
			?{
				PacketForReceive: 
					?(PacketForReceive)
					.!{
						Success: , 
						BadPacketSize: 
							!<BadPacketSize>
					}
					.#IO_RUNNING,
				GetReceivedPacket: 
					!{
						ReceivedPacket: 
							!<ReceivedPacket>, 
						NoPacket: 
					}
					.#IO_RUNNING
			}
		]	
	}

	private final noalias protocol NicDevice
	{
		cbegin 
		.!<DeviceInfo>             
		.@(IOConfigureBegin)
		.@(IORunning)
	}
	
	public NICDevice(String server, int port) throws Exception
	{			
		final noalias SJService c = SJService.create(NicDevice, server, port);
		
		final noalias SJSocket s;

		try (s)
		{		
			s = c.request(); // More realistic to be a server socket?
					
			s.send(new DeviceInfo());		
		
			meth1(s);
			meth2(s);
		}
		finally
		{

		}
	}	

	private void meth1(final noalias @(IOConfigureBegin) s) throws ClassNotFoundException, SJIOException
	{	
		s.recursion(IO_CONFIGURE_BEGIN)
		{
			final noalias SJService addr = (cbegin.@(NicEventBody)) s.receive();

			// Use the server-address to open a NicEvent session with the client. This session would probably be spawned to run in a separate thread.

			SetParameters sp = (SetParameters) s.receive();

			if (true) // Some condition on rfe and sp.
			{
				s.outbranch(InvalidParameters)
				{
					s.send(new InvalidParameters());

					s.recurse(IO_CONFIGURE_BEGIN);
				}
			}
			else
			{
				s.outbranch(Success)
				{
					s.inbranch()
					{
						case StartIO:
						{

						}
						case ConfigureIO:
						{
							s.recurse(IO_CONFIGURE_BEGIN);
						}
					}
				}				
			}								
		}
	}
	
	private void meth2(final noalias @(IORunning) s) throws ClassNotFoundException, SJIOException
	{
		s.recursion(IO_RUNNING)
		{
			s.inbranch()
			{
				case PacketForReceive:
				{
					PacketForReceive pack = (PacketForReceive) s.receive();

					if (true) // Check the received pack buffer is the correct size.
					{
						s.outbranch(Success)
						{

						}
					}
					else
					{
						s.outbranch(BadPacketSize)
						{
							s.send(new BadPacketSize());	
						}
					}

					s.recurse(IO_RUNNING);
				}
				case GetReceivedPacket:
				{
					if (true) // Check there is an available packet for receive.
					{
						s.outbranch(ReceivedPacket)
						{
							s.send(new ReceivedPacket());
						}
					}
					else
					{
						s.outbranch(NoPacket)
						{

						}
					}

					s.recurse(IO_RUNNING);
				}
			}
		}
	}
	
	private class DeviceInfo implements Serializable { }
	private class SetParameters implements Serializable { }
	private class InvalidParameters implements Serializable { }
	private class PacketForReceive implements Serializable { }
	private class BadPacketSize implements Serializable { }
	private class ReceivedPacket implements Serializable { }

	private class NicEvent implements Serializable { }
	private class AckEvent implements Serializable { }
}
