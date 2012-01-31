// This acceptor uses UDP for initial connection 
// which is most natural
// 
// An acceptor is asked to establish a connection
// with the following format of the request:
//       (1) initial four bytes: IP address
//       (2) the next four bytes: Port address
// and it returns with the newly created UDP 
// datagram address

package sessionj.runtime.transport.udp;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;

import sessionj.runtime.*;

import sessionj.runtime.transport.*;

public class SJUDPAcceptor extends AbstractWithTransport implements SJConnectionAcceptor{
       
    private final DatagramSocket ss;
    private static final int INITIAL_MESSAGE_SIZE = 8; 
    private final DatagramPacket request
       = new DatagramPacket(new byte [INITIAL_MESSAGE_SIZE],
        INITIAL_MESSAGE_SIZE);

    public SJUDPAcceptor(int port, SJTransport transport) throws SJIOException
    {
	    super(transport);
        try {      	 
           ss = new DatagramSocket(port);
        } catch (IOException ioe) {
           throw new SJIOException(ioe);
        }
    }
       
    public SJConnection accept() throws SJIOException{
       try {
           if (ss == null) {
              throw new SJIOException('[' + 
                                   getTransportName() +
	                               ']' +
                                   "Connection acceptor" +
                                   "not open.");
           }
           // get the request
	       ss.receive(request); 
           
           System.out.println("1: " + request);
           
           // parse it
           byte [] data = request.getData();
           int IPaddress = Converter.byteArrayToInt(data, 0);
           int UDPport = Converter.byteArrayToInt(data, 4);           
           // create the address
           InetAddress inet_IPaddress = Converter.intToInetAddress(IPaddress);
           
           System.out.println("1a: " + inet_IPaddress + ", " + UDPport);
           
           /*inet_IPaddress = InetAddress.getLocalHost();           
           System.out.println("1b: " + inet_IPaddress + ", " + UDPport);*/           
           
           SocketAddress sa = new InetSocketAddress(inet_IPaddress, UDPport);
           
           // create a channel 
           DatagramChannel datagramChannel = DatagramChannel.open();
           
           // now connect it (this is done locally)           
           System.out.println("2: ");
           
           datagramChannel.connect(sa);
                      
           System.out.println("3: ");
           
           DatagramSocket localsocket = datagramChannel.socket();
           
           // take its local address 
           InetSocketAddress lsa = (InetSocketAddress) localsocket.getLocalSocketAddress();           
           int localAddress = Converter.inetAddressToInt(lsa.getAddress());
           int localPort = lsa.getPort();
           
           // now send it back using the newly created channel
           // (reuses data above)
           Converter.intToByteArray(localAddress, data, 0);
           Converter.intToByteArray(localPort, data, 4);
           
           System.out.println("3a: " + lsa.getAddress() + ' ' + localPort);
           
           //datagramChannel.send(ByteBuffer.wrap(data), lsa);
           datagramChannel.send(ByteBuffer.wrap(data), sa);
           
           System.out.println("4: ");
           
           //RAY
   					byte[] ba = new byte[4];
   					ByteBuffer bb = ByteBuffer.wrap(ba);
   					
   					DatagramSocket foo = datagramChannel.socket();
   					
   					//System.out.println("4a: " + foo.getLocalPort() + ", " + foo.getPort());
   					System.out.println("4a: " + foo.getLocalAddress() + ", " + foo.getInetAddress());
   					
   					//datagramChannel.read(bb);

   					//System.out.println("4b: " + Converter.byteArrayToInt(bb.array(), 0));
   					
   					DatagramPacket dp = new DatagramPacket(new byte[4], 4);
   					foo.receive(dp);
   					
   					System.out.println("4b: " + Converter.byteArrayToInt(dp.getData(), 0));
           //YAR
           
           // can be 3-way but for simplicity just this
           // now return the connection
           return new SJUDPConnection(datagramChannel, getTransport());
       }
       catch (IOException ioe) {
           throw new SJIOException(ioe);
       }
    }

    public SelectableChannel acceptSelectableChannel() {
        throw new UnsupportedOperationException("TODO");
    }

    public void close(){
       try { 
           if (ss != null) {
              ss.close(); 
           }
       }
       catch (Exception ignored) { }
    }
       
    public boolean interruptToClose() {
       return false;
    }
    
    public boolean isClosed() {
       return ss.isClosed();
    }
       
    public String getTransportName(){
       return SJUDP.TRANSPORT_NAME;
    }
}
