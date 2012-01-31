package sessionj.runtime.transport.udp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Random;

public class SJUDP extends AbstractSJTransport {
       
    public static final String 
       TRANSPORT_NAME = 
       "sessionj.runtime.transport.SJUDP";

    private static final int LOWER_PORT_LIMIT = 1024; 
    private static final int PORT_RANGE = 10000;

    private static final int INITIAL_MESSAGE_SIZE = 8; 
    private final DatagramPacket request
       = new DatagramPacket(new byte [INITIAL_MESSAGE_SIZE],
        INITIAL_MESSAGE_SIZE);
    private final DatagramPacket answer
       = new DatagramPacket(new byte [INITIAL_MESSAGE_SIZE],
        INITIAL_MESSAGE_SIZE);

    public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) 
       throws SJIOException
    {
        return new SJUDPAcceptor(port, this);
    }
       
    public SJConnection connect(String s, int i) throws SJIOException 
    {
    	try 
    	{
        // create a channel 
        DatagramChannel datagramChannel = DatagramChannel.open();

        //RAY
        //DatagramSocket localsocket = datagramChannel.socket();        
        DatagramSocket localsocket = new DatagramSocket(getFreePort());
        //YAR
        
        // set up remote/local addresses / ports
        //InetAddress lia = localsocket.getLocalAddress();
        InetAddress lia = InetAddress.getLocalHost();
        int lport = localsocket.getLocalPort();
        
        InetAddress ia = InetAddress.getByName(s); 
        int port = i;
        
        // set up the request packet
        byte [] data = request.getData();
        Converter.intToByteArray(Converter.inetAddressToInt(lia), data, 0);
        Converter.intToByteArray(lport, data, 4);
        
        System.out.println("a1: " + ia + ", " + port);
        System.out.println("a2: " + lia + ", " + lport);
        
        request.setAddress(ia);
        request.setPort(port);
        
        // send        
        System.out.println("b: " + request);
        
        localsocket.send(request);
        
        // receive        
        System.out.println("c: ");
        
        localsocket.receive(answer);

        // parse it        
        System.out.println("d: ");
        
        data = this.answer.getData();
        
        System.out.println("e: ");
        
        int IPaddress = Converter.byteArrayToInt(data, 0);
        int UDPport = Converter.byteArrayToInt(data, 4);
        
        System.out.println("e1: " + Converter.intToInetAddress(IPaddress) + " " + UDPport);
        
        // create the address
        InetAddress inet_IPaddress = Converter.intToInetAddress(IPaddress);
        InetSocketAddress sa = new InetSocketAddress(inet_IPaddress, UDPport);
        
        // now connect it (this is done locally)        
				System.out.println("f: ");                
        				
				//RAY
				localsocket.close();
				
				//datagramChannel.socket().bind(new InetSocketAddress("localhost", lport));
				//datagramChannel.socket().bind(new InetSocketAddress("192.168.1.66", lport));
				datagramChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), lport));
				datagramChannel.connect(sa);
        
				System.out.println("g: ");				
				
				byte[] ba = new byte[4];
				Converter.intToByteArray(1234, ba, 0);
				ByteBuffer bb = ByteBuffer.wrap(ba);
								
				DatagramSocket foo = datagramChannel.socket();
				
				//System.out.println("g1: " + foo.getLocalPort() + ", " + foo.getPort());
				System.out.println("g1: " + foo.getLocalAddress() + ", " + foo.getInetAddress());
				
				//datagramChannel.write(bb);
				foo.send(new DatagramPacket(ba, 4));
				//YAR
				
        return new SJUDPConnection(datagramChannel, this); 
	    } 
	    catch (IOException ioe) 
	    {
	    	throw new SJIOException(ioe);
	    }
    }

    public boolean portInUse (int port) {
       DatagramSocket ss = null; 
       try {
           ss = new DatagramSocket(port);
       }
       catch (Exception ioe) {
           return true;
       }
       finally {
           if (ss != null) {
              try {
                  ss.close();
              }
              catch (Exception ioe) { }                                   
           }
       }
       return false;
    }

    public int sessionPortToSetupPort(int i) {
       return -1;
    }

    public String sessionHostToNegotiationHost(String s) {
       return null;
    }
       
    public int getFreePort() throws SJIOException{
       int start = new Random().nextInt(PORT_RANGE);
       int seed = start + 1;
       int finalport = 0;
       for (int port = seed % PORT_RANGE; 
            port != start; 
            port = seed++ % PORT_RANGE) 
           {  
      	 			//System.out.println("bar: " + port);
      	 
              if (!portInUse(port + LOWER_PORT_LIMIT)) {
                  finalport = port + LOWER_PORT_LIMIT;
                  //port = start;
                  break;
              } 
           }
       return finalport;
    }
       
    public String getTransportName () {
       return TRANSPORT_NAME;
    }

}