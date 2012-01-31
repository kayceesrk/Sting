/*

This is an experimental UDP implementation for SJ ATI with
minimal functionality. The basic design idea is to have a
minimal subset of TCP-like mechanisms to serve our
purpose. The formal criteria we want is the lack of
deadlock and error recovery for message loss.  For this
purpose we include:

(1) Use positive cumulative acks, extended with their 
    NAK-like usage for a blocking read as well as 
    periodical ack trade.
(2) Retransmission based on repeated acks: due to 
    blocing read, no timeout needed.
(3) Two ends trade acks and latest acks (simplifed
    form of ack-acks) for liveness.

For simplicity it has:

--- *No* flow control (either end-to-end or global) 
--- *No* resiliency against message corruption/tampering
--- *Not* DOS resistant 

Another basic feature is sequentiality: there is at most
one reader and one writer, and a helper thread never runs
in parallel by coordination. This is a deliberate design
choice for simplicity and efficiency, as well as what SJ
needs (though it is not hard to add concurrent read and 
write as well as multiple helper threads.

Key Requirements for connection:

(1) No deadlock under sequential usage.
(2) Works with non-trivial losses as far as packets 
are moving and both-ends are alive.

More detailed spec follows. Below we use "read" as an
action for the SL to read a message from TL: and "receive"
for the TL to receive a message from a connection.
Similarly "write" is an action of the SL, "send" is an
action of the TL.

(a) Packets of fixed size (1500 bytes), when sent
truncated.

(b) 32 bits cyclic sequence numbers (with none for acks),
32 bits give us 4 billion packets, not so bad for
session-typed traffic.

(c) Sent packets stored in a cyclic buffer (1024 slots, 
default), similarly received packets. 

(d) Each "sent" slot associated with if it's acked or
not. Each "received" slot is associated with if its gotten
by the application or not (in fact the latter is carried
out by a delimiter). A slot becomes reusable when it is
acked (for a send slot) or it is read (for a receive slot).

In other words, all of the sent packets which have not yet
been acked should be in the buffer.

(e) We have a helper thread: the purpose of this thread is
to check acks and do, when finding repeated acks, a
re-transmission, as well as to periodically trade acks and
latest ack info, the latter allowing it to stop the ack
trading (since the acks for all sent messages have been
received).

(f) "writeBytes": send the data in one or more packets as
well as saving them for retransmission.

(g) "readBytes": read the data from the buffer, if not
enough receive and if it is not enough after 10 times then
start NAKing.

(h) The read/writeBytes never run in parallel with the
helper thread: they let it sleep before starting.

(i) To allow the receiver to send NAKs when it is expecting
but not receiving the data of required size, which in turn
allows the sender to re-transmit, it cannot block when
receiving when it is reading the data: so we use
nonblocking DatagramChannel.

NB: In fact in the present SJ session, no ack for its
previous send can come while receiving, but the code is
made to be able to cope with such interleaving.

*/

package sessionj.runtime.transport.udp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.SJTransport;
import sessionj.runtime.transport.AbstractSJConnection;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class SJUDPConnection extends AbstractSJConnection implements Runnable
{

    private final DatagramChannel datagramChannel;
    private final SocketAddress socketAddress;
    private final DatagramSocket socket;

    private boolean closing = false;

    // two circular buffers of send/receive packets
    // A packet has header and data
    // The header has three fields
    // *** 0th: packet sequence number, int: hence 4 bytes. 
    //          -1: ack, -2: ack-of-latest-ack
    // *** 1st: datalength, int.
    // *** 2nd: (a) "next byte" for ack 
    //          (b) the maximum "next byte" the sender received
    //              so far, for ack-ack
    // NB: (1) ack and ack-ack only use a header
    //     (2) short messages are truncated when sending
    //         but always stored in a 1500 bytes slot
    private static final int MAX_SLOT = 1024; 
    private static final int DATA_SIZE = 1500;
    private static final int HEADER_SIZE = 12; // 4bytes (for int) x 3
    private static final int PACKET_SIZE = DATA_SIZE + HEADER_SIZE;
    private static final int RECEIVE_INTERVAL = 1; // in ms

    // The send/receive packets which we reuse.
    private byte [] receivedPacket = new byte[PACKET_SIZE];
    // The ack and ack-ack packet which we reuse.
    private final byte [] ackPacket = new byte[HEADER_SIZE];
    // reused header
    private final int [] header = new int[HEADER_SIZE /4]; 

    // buffer for sending
    private final byte [][] sbuf = new byte[PACKET_SIZE][MAX_SLOT];
    private final boolean [] sAcked = new boolean[MAX_SLOT];

    // buffer for receiving
    private byte [][] rbuf = new byte[PACKET_SIZE][MAX_SLOT];
    private final boolean [] rFilled = new boolean[MAX_SLOT];

    // The two cursors for sending buffer:
    //   (1) nextSendingPacket: what it will send next
    //   (2) ackedUptoThis: up to here contiguously
    //       acked.
    // s.t. the invariant
    //     nextSendingPacket - ackedUptoThis < MAX_SLOT
    // is always obeyed.
    private int nextSendingPacket = 0;
    private int ackedUptoThis = -1; //

    // Since the sender and the receiver have the same
    // window size and since the receiver always acks
    // *after* it being read, the receiver never receives 
    // an out-of-range message. 
    // Thus we only use two cursors for reciving buffer:
    //    (1) nextMissingPacket: "next byte" in TCP. 
    //    (2) nextReadingPacket: what to be read next.
    //    (3) maxReceivedPacket
    // s.t. nextReadingPacket < nextMissingPacket < maxReceivedPacket
    private int nextMissingPacket = 0; 
    private int nextReadingPacket = -1; 
    private int maxReceivedPacket = -1; 

    // For an ack-ack we uses 
    //     (1)  maxNMP 
    //     (2)  ackTimes
    //     (3)  MAX_ACK_TIMES
    // (1) is the receiver's record that its ack is acked
    // (NMP is next missing packet).
    // (2)(3) are for: for MAX_ACK_TIMES of acks, an ack-ack 
    // will be sent.
    private int maxNMP = -1;
    private int ackTimes = 0;
    private static final int MAX_ACK_TIMES = 10;

    // Parameters for repeatedack 
    //   (1) ackRepeated reaches maxAckRepeated 
    //       then resend
    //   (2) ACK_CHECK_INTERVAL (ms) to check acks
    // NB: We can also send acks more sparingly
    private int ackRepeated = 0;
    private static final int maxAckRepeated = 3;

    // helper thread is stored here
    private final Thread helper;
    private static final long ACK_CHECK_INTERVAL = 100;

    // whether we wish to invoke a thread or not.
    private volatile boolean helperNeeded = false;

    public SJUDPConnection(DatagramChannel datagramChannel, SJTransport transport)
    {
        super(transport);
        // (1) all the initialisation above are performed.
        // (2) UDP channel (assuming it is already connected)
        this.datagramChannel = datagramChannel;
        this.socketAddress =
            datagramChannel.socket().getRemoteSocketAddress();
        // (3) initialise arrays.
        for (int i=0; i< MAX_SLOT; i++) {
            // no send packet acked yet.
            this.sAcked[i]=false;
            this.rFilled[i]=false;
        }
        // (5) run the helper thread (the helper "waits" immediately).
        this.helperNeeded = false;
        this.helper = new Thread(this);
        this.helper.start();

        //RAY
        this.socket = datagramChannel.socket();
        //YAR
    }

    public void disconnect() {
     try{
         // (1) disconnect the channel
         this.datagramChannel.disconnect();
         // (2) close the underlying socket
         this.socket.close();
         // (3) kill the thread by waking it up
         this.closing = true;
         this.notify();
         this.helper.join();
     }
     catch(Exception e) {
     }
    }

    // An AL message can be cut off into two or more.
    public void writeBytes(byte[] bs) throws SJIOException{
     try {
         // (1) let the helper sleep and wait until it does sleep.
         helperSleeps();
         // (2) send (and store) data
         int packetsNeeded = 
            (bs.length / this.DATA_SIZE) +
            (bs.length % this.DATA_SIZE == 0? 0: 1);
         for (int i=0; i<packetsNeeded; i++) {
            int offset = i*this.DATA_SIZE;
            int length = ((i<packetsNeeded-1)?
                        this.DATA_SIZE :
                        (bs.length % this.DATA_SIZE));
            sendDataPacket(bs, offset, length);
         }
         // (3) invoke helper thread.
         helperWakesUp();
     } catch (Exception e) {
         throw new SJIOException(e);
     }
    }

    public void readBytes(byte[] bs) throws SJIOException{
     try {
         // (1) let the helper sleep and wait until it does so.
         helperSleeps();
         // (2) receive and put data into bs
         int packetsNeeded = 
          (bs.length / this.DATA_SIZE) +
          (bs.length % this.DATA_SIZE == 0? 0: 1);
         for (int i=0; i<packetsNeeded; i++) {
          int offset = i*this.DATA_SIZE;
          int length = ((i<packetsNeeded-1)?
                     this.DATA_SIZE :
                     (bs.length % this.DATA_SIZE));
          if (this.nextReadingPacket < this.nextMissingPacket) {
              // Read from the rbuf at this.nextReadigPacket
              // It also incretment this.nextReadingPacket
              // and set rFilled[this.nextReadingPacket].
              copyFromReceivedBuffer(bs, offset, length);         
          } else {
              // receive a packet:
              receiveDataPacket();
              copyFromReceivedBuffer(bs, offset, length);         
              sendAck();
          }
         }
     } catch(Exception ioe) {
    	 
    	 //ioe.printStackTrace(); System.out.println();
    	 
         throw new SJIOException(ioe);
     }
    }

    // helper thread's main code
    //  (1) Initially sleeps
    //  (2) When writeBytes finishes it is awakened.
    //  (3) Never run in parallel with readBytes/writeBytes
    public void run() {
     try {
     // Below Thread.currentThread() is 
     // the same as this.helper
     // counter is needed for repetition
     int interval = 0;
     while (!this.closing) {
         // an inner loop, to process acks
         // as far as it exists. if not it 
         // waits with exponential backoff.
         int wait_interval = 1;
         boolean isProcessing = this.helperNeeded;
         while (isProcessing) { 
          // try to process an ack
          boolean received = receivePacket();
          if (received) {
              // intialise interval
              wait_interval = 1;
              // header
              readHeader(this.receivedPacket);
              if (isData()) {
               ;
              } else if (isAck()) {
               ;
              } else if (isAckAck()) {
               int ackack = getAckAckNumber();
               if (ackack > this.maxNMP)
                   this.maxNMP = ackack;
              }
          } else {
              // back off: starts from 2 ms.
              wait_interval=wait_interval*2;
              Thread.currentThread(). 
               sleep(wait_interval);
          }
          // if 10 times no receive, which
          // means no acks for a couple of 
          // seconds, sleep.
          if (!this.helperNeeded || wait_interval > 1024) {
              isProcessing = false;
          }
         }
         sendAck();
         // wait or sleep
         if (!this.helperNeeded) 
            this.wait();
         else {
            Thread.currentThread(). 
               sleep(this.ACK_CHECK_INTERVAL);
         }
     }
     } catch (Exception e) {
         ;
     }
    }

    // auxiliary methods
    public void flush() throws SJIOException{
       try{
           // do nothing
           ;
       }catch(Exception ioe){throw new SJIOException(ioe);}
    }

    // this.receivedPacket --> rbuf[slot] by pointer exchange.
    void saveReceivedPacket(int slot) {
     byte [] tmp = this.rbuf[slot];
     this.rbuf[slot] = this.receivedPacket;
     this.receivedPacket = tmp;
    }

    // auxiliary methods     
    public String getHostName(){
       return socket.getInetAddress().getHostName();
    }

    // auxiliary methods     
    public int getPort(){
       return socket.getPort();
    }

    /* From here, key function methods */

    // send part of bs: from offset to offset+length-1
    public void sendDataPacket(byte [] bs, int offset, int length)
       throws SJIOException
    {
       try {
           if (length>this.DATA_SIZE ||
              offset+length-1>bs.length)
               throw new SJIOException();
           // (1) needs one free slot in this.sbuf.
           while (sendBufferFull())
              receivePacket();
           // (2) copy it to this.sbuf, including
           //     the preparation of a header,
           //     and setting sAcked.
           copyToSendingBuffer(bs, offset, length);         
           // (3) prepare a byte buffer.
           ByteBuffer bb = ByteBuffer.
               wrap(this.sbuf[getSlot(this.nextSendingPacket)]);
           // (4) now really send 
           this.datagramChannel.send(bb,
                                     this.socketAddress);
           // (5) increment nextSendingPacket.
           this.nextSendingPacket++;
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }    

    // Repeats non-blocking receiption of messages
    // until a single data packet is received
    void receiveDataPacket() throws SJIOException 
    {
       try {
           // nonblocking receive.
           this.datagramChannel.configureBlocking(false);
           // loop until a data packet is received
           boolean toExit = false;
           while (!toExit) {
              boolean received = receivePacket();
              // assume this.header  is set.
              if (received && isData()) 
                  toExit = true;
              else if (!received) {
                  // sleep a bit then rapidly repeat 
              Thread.currentThread().sleep(this.RECEIVE_INTERVAL);
              }
           }
       } catch (Exception ioe) {
           throw new SJIOException(ioe);
       }
    }

    // Receive packet in a non-blocking way.
    // Does everything except acking: this is because
    // acking is better done after freeing a slot.
    boolean receivePacket() throws SJIOException 
    {
       try {
           // (1) receive
           SocketAddress res = 
              this.datagramChannel.
              receive(ByteBuffer.wrap(this.receivedPacket));
           // (3) received?
           boolean received = false;
           if (res!=null) { // received
              received = true;
              readHeader(this.receivedPacket);
              if (isData()||isAck()) {
                  recordAck(getAckNumber());
                  checkAndResend();
              }
              if (isData()) {
                  int seqnum = getSequenceNumber();
                  if (seqnum >= this.nextMissingPacket)  {
                     // the following is a pointer exchange
                     // so very fast
                     saveReceivedPacket(getSlot(seqnum));
                     // maxReceivedPacket can change
                     if (seqnum > this.maxReceivedPacket) 
                         this.maxReceivedPacket = seqnum;
                     // increment NMP zero or more
                     if (seqnum == this.nextMissingPacket) { 
                         this.nextMissingPacket++;
                         findNextMissingPacket();
                     }
                  }
              } else if (isAck()) {
                  // ack is already processed so only ack-ack
                  this.ackTimes++;
                  if (this.ackTimes >= this.MAX_ACK_TIMES) {
                     sendAckAck();
                     this.ackTimes=0;
                  }
              } else if (isAckAck()) {
                  // advance maxNMP
                  int ackack = getAckAckNumber();
                  if (ackack > this.maxNMP)
                     this.maxNMP = ackack;
              }
           }
           return received;
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }

    // copy to sbuf[nextReadingPacket] from a byte sequence
    // at an offset and frees that slot.
    void copyToSendingBuffer(byte[] bs, int offset, int length) throws SJIOException {
        // (1) slot and byte array we use.
        int slot = getSlot(this.nextSendingPacket);
        byte [] data = this.sbuf[slot]; 
        // (2) prepare the header (in this.header)
        setDataHeader(this.nextSendingPacket,
                      length,
                      this.nextMissingPacket);
        // (3) write the header.
        writeHeader(data);
        // (4) copy from the bs
        for (int i=0; i< length; i++) 
            data[this.HEADER_SIZE +i]=bs[offset+i];
        // (5) set the flag.
        this.sAcked[slot]=false;
    }

    // copy from rbuf[nextReadingPacket] to a byte sequence
    // at an offset and frees that slot.
    void copyFromReceivedBuffer(byte[] bs, int offset, int length) {
       // this is the slot to read from
       int slot = getSlot(this.nextReadingPacket);
       int start = this.HEADER_SIZE;
       byte [] data = this.rbuf[slot];
       for (int i=0; i<length; i++)
           bs[offset+i]=data[start+i];
       // the slot is free.
       this.rFilled[slot] = false;
       // now advance nextReadingPacket
       this.nextReadingPacket++;
    }
     
    void recordAck (int ackNum)
    {
       // mark they are acked
       for (int i = this.ackedUptoThis+1;
            i<=ackNum;
            i++) {
           this.sAcked[getSlot(i)]=true;
       }
       if (this.ackedUptoThis < ackNum) {
           this.ackedUptoThis = ackNum;
           this.ackRepeated = 0;
       } else {
           // the same ack as before
           this.ackRepeated++;
       }
    }

    void findNextMissingPacket()
    {
       // find the next missing packet: we may slide over already 
       // acked intermediate packets
       boolean finding = true;
       while (finding) {
           // the current one is max + 1?
           if (this.nextMissingPacket == this.maxReceivedPacket + 1) {
              // no interrim missing packet, so end
              finding = false;
           } else {
              // if it is filled then slide.
              if (this.rFilled[getSlot(this.nextMissingPacket)]) 
                  finding = false;
              else 
                  this.nextMissingPacket++;
           }
       }
    }
              
    void sendAck() throws SJIOException {
       try {
           if (this.maxNMP<this.nextSendingPacket) {
              setAckHeader(this.nextMissingPacket);
              writeHeader(this.ackPacket);
              this.datagramChannel.send(ByteBuffer.wrap(this.ackPacket),
                                     this.socketAddress);
           }
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }

    void sendAckAck() throws SJIOException {
       try {
           setAckAckHeader(this.maxNMP);
           writeHeader(this.ackPacket);
           this.datagramChannel.send(ByteBuffer.wrap(this.ackPacket),
                                  this.socketAddress);
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }

    public void checkAndResend() throws SJIOException {
       try {
           if (this.ackRepeated >= this.maxAckRepeated) {
              resend(this.nextMissingPacket);
              this.ackRepeated=0;
           }
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }

    public void resend(int seqnum) throws SJIOException {
       try {
           byte [] data = 
              this.sbuf[getSlot(seqnum)];
           readHeader(data);
           this.datagramChannel.send(ByteBuffer.wrap(data),
                                  this.socketAddress);
       } catch (Exception e) {
           throw new SJIOException(e);
       }
    }

    void readHeader (byte [] bs) throws SJIOException {
       for (int i=0; i<3; i++) 
           this.header[i] = 
              Converter.byteArrayToInt(bs, i*4);
    }

    void writeHeader (byte [] bs) throws SJIOException {
       for (int i=0; i<3; i++) 
           Converter.intToByteArray(this.header[i],bs,i*4);
    }

    int getSlot (int packetNumber) {
       return ((packetNumber / this.MAX_SLOT) - 1);
    }
           
    // no more sending
    boolean sendBufferFull () {
       return  (this.nextSendingPacket - this.ackedUptoThis 
               > 
               this.MAX_SLOT);
    }

    //RAY
    //public void helperSleeps() {
    public synchronized void helperSleeps() {
    //YAR
       helperNeeded = false;
       this.notify();
       while (this.helper.getState() != Thread.State.WAITING) {
           ;
       }
    }

    //RAY
    //public void helperWakesUp() {
    public synchronized void helperWakesUp() {
    //YAR
       this.helperNeeded = true;
       this.notify();
    }

    // max receivable packet (sender automatically does not
    // exceed this, as far as all are working fine)
    public int getMaxReceivablePacket() {
       return (this.nextReadingPacket + this.MAX_SLOT - 1);
    }

    // is this.header an ack?
    public boolean isAck() {
       return (this.header[0]==-1);
    }

    // is this.header an ack-ack?
    public boolean isAckAck() {
       return (this.header[0]==-2);
    }

    // is this.header a data?
    public boolean isData() {
       return (this.header[0]>=0);
    }

    // data size
    public int getDataSize() {
       return this.header[1];
    }

    // sequence Number
    public int getSequenceNumber() {
       return this.header[0];
    }

    // next byte in an ack
    public int getAckNumber() {
       return this.header[2];
    }

    // "I got your ack saying this"
    public int getAckAckNumber() {
       return this.header[2];
    }

    /* The following are part of the interface which 
       may not be frequently used
    */

    // writeByte and readByte uses writeBytes and readBytes
    // writeByte and readByte may not be used in practice.
    public void writeByte(byte b) throws SJIOException {
     try{
         byte [] ba = new byte[1];
         ba[0] = b;
         this.writeBytes(ba);
     } catch (Exception e) {
         throw new SJIOException(e);
     }
    }

    public byte readByte() throws SJIOException {
     try{
         byte [] ba = new byte[1];
         this.readBytes(ba);
         return ba[1];
     } catch (Exception e) {
         throw new SJIOException(e);
     }
    }

    public void setDataHeader(int seq, int len, int ack) {
     this.header[0]=seq;
     this.header[1]=len;
     this.header[2]=ack;
    }

    public void setAckHeader(int ack) {
     this.header[0]=-1;
     this.header[2]=ack;
    }

    public void setAckAckHeader(int ack) {
     // the number "ack" means 
     //    "Hi Receiver, I did get your ack 
     //     which contains this number (ack) 
     //     as the next packet number. 
     this.header[0]=-2;
     this.header[2]=ack;
    }

    public int getLocalPort() {
        return this.socket.getLocalPort();
    }
}    
