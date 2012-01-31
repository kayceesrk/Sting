package sessionj.runtime.transport.http;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.AbstractSJConnection;
import sessionj.runtime.transport.SJTransport;
import static sessionj.runtime.util.SJRuntimeUtils.closeStream;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class SJHTTPSConnection extends AbstractSJConnection {

    private final SSLSocket socket;

    private final DataOutputStream dos;
    private final DataInputStream dis;

    private final Vector msgSent;

    private boolean closed;

    private String status = "";
    private int numMsgSent = 0;

    private final Integer lock_sending = new Integer(0);
    private final Integer lock_status = new Integer(0);
    private final Integer lock_msgSent = new Integer(0);

    private final Thread sendingThread = new Thread() {

        public void run() {

            try {

                boolean running = true;

                //send only if the buffer is not empty
                synchronized (msgSent) {
                    while (msgSent.isEmpty()) {
                        if (closed == false) {
                            try {
                                msgSent.wait();
                            } catch (Exception e) {
                            }
                        } else {
                            running = false;
                            break;
                        }
                    }
                }

                while (running) {

                    //send a message

                    //System.out.println("Sending message...");

                    Object o = null;
                    synchronized (msgSent) {
                        o = msgSent.firstElement();
                    }
                    if (o == null)
                        throw new RuntimeException("Null Pointer");

                    if (o instanceof Byte) {
                        synchronized (lock_sending) {
                            dos.writeUTF("POST uri HTTP/1.1\n");
                            dos.writeUTF("Content-Length: " + 1);
                            dos.writeUTF("Content-Type: " + "message");
                            dos.writeUTF("\n");
                            //System.out.println("Sending byte");
                            byte b = ((Byte) o).byteValue();
                            dos.writeByte(b);
                        }
                    } else {
                        synchronized (lock_sending) {
                            dos.writeUTF("POST uri HTTP/1.1\n");
                            dos.writeUTF("Content-Length: " + ((byte[]) o).length);
                            dos.writeUTF("Content-Type: " + "message");
                            dos.writeUTF("\n");
                            //System.out.println("Sending bytes");
                            byte[] bs = new byte[((byte[]) o).length];
                            System.arraycopy((byte[]) o, 0, bs, 0, ((byte[]) o).length);
                            dos.write(bs, 0, bs.length);
                        }
                    }

                    //checking status of the message sent

                    synchronized (lock_status) {
                        while (status.compareTo("") == 0)
                            try {
                                lock_status.wait();
                            } catch (InterruptedException ie) {
                            }
                        if (status.compareTo("HTTP/1.1 200 OK\n") == 0)
                            status = "";
                        else
                            throw new RuntimeException("MESSAGE SENT NOT UNDERSTOOD.");
                    }
                    //System.out.println("Status Received");
                    synchronized (msgSent) {
                        msgSent.remove(0);

                        while (msgSent.isEmpty()) {

                            if (closed == false) {
                                try {
                                    msgSent.wait();
                                } catch (Exception e) {
                                }
                            } else {
                                running = false;
                                break;
                            }
                        }
                    }
                    //System.out.println("Message removed");
                }
                //System.out.println("Sending thread closed");
            } catch (IOException ioe) {
                //if(!closed)throw new RuntimeException(ioe);
                try {
                    closeStream(dos);
                    closeStream(dis);
                    socket.close();
                } catch (IOException ioes) {
                }
            }
        }
    };

    public SJHTTPSConnection(SSLSocket socket, InputStream is, OutputStream os, SJTransport transport) throws IOException {
        super(transport);
        this.socket = socket;

        socket.setTcpNoDelay(false);

        dos = new DataOutputStream(os);
        dis = new DataInputStream(is);

        msgSent = new Vector();
        closed = false;
        sendingThread.start();
    }

    public void disconnect() {// throws SJIOException;

        try {
            //System.out.println("Closing...");

            //wait until the status of each message sent is received
            closed = true;

            boolean stillMsgToSent;
            synchronized (lock_msgSent) {

                stillMsgToSent = numMsgSent > 0;
                //System.out.println("Msg still to receive status: "+numMsgSent);
            }
            while (stillMsgToSent) {
                String requestMethod = dis.readUTF();
                //System.out.println("Request Method: "+requestMethod);

                synchronized (lock_status) {
                    status = requestMethod;
                    lock_status.notify();
                }
                synchronized (lock_msgSent) {
                    numMsgSent--;
                    stillMsgToSent = numMsgSent > 0;
                    //System.out.println("Msg still to receive status: "+numMsgSent);
                }
            }
            //System.out.println("Main thread closing...");
            synchronized (msgSent) {
                //closed = true;
                msgSent.notify();
            }
            dos.flush();
            try {
                sendingThread.join();
            } catch (InterruptedException ie) {
            }

            closeStream(dos);
            closeStream(dis);
            socket.close();

        } catch (IOException ioe) {
        }
    }

    public void writeByte(byte b) throws SJIOException {

        synchronized (msgSent) {

            if (msgSent.isEmpty())
                msgSent.notify();

            msgSent.add(new Byte(b));

        }
        synchronized (lock_msgSent) {

            numMsgSent++;
        }
        //System.out.println("WriteByte");
    }

    public void writeBytes(byte[] bs) throws SJIOException {

        synchronized (msgSent) {

            if (msgSent.isEmpty())
                msgSent.notify();

            msgSent.add(bs);
        }
        synchronized (lock_msgSent) {

            numMsgSent++;
        }
        //System.out.println("WriteBytes");
    }

    public byte readByte() throws SJIOException {

        try {
            String requestMethod = dis.readUTF();
            //System.out.println("Request Method: "+requestMethod);

            while (requestMethod.compareTo("HTTP/1.1 200 OK\n") == 0 | requestMethod.compareTo("HTTP/1.1 400 Bad Request\n") == 0) {

                synchronized (lock_status) {
                    status = requestMethod;
                    lock_status.notify();
                }
                synchronized (lock_msgSent) {
                    numMsgSent--;
                }
                requestMethod = dis.readUTF();
            }

            StringTokenizer st = new StringTokenizer(requestMethod);

            String method = st.nextToken();

            if (method.compareTo("GET") == 0 | method.compareTo("HEAD") == 0 |
                method.compareTo("PUT") == 0 | method.compareTo("DELETE") == 0 |
                method.compareTo("TRACE") == 0 | method.compareTo("OPTIONS") == 0 |
                method.compareTo("CONNECT") == 0) {
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 Bad Request\n");
                }
                throw new SJIOException("readByte: BAD REQUEST METHOD: " + requestMethod);
            }

            if (method.compareTo("POST") == 0) {
                String token = dis.readUTF();
                String header = token;
                while (token.compareTo("\n") != 0) {
                    token = dis.readUTF();
                    header = header.concat("\n").concat(token);
                }

                //System.out.println("Header: "+header);
                byte b = dis.readByte();
                //System.out.println("Sending status");
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 200 OK\n");
                }
                return b;
            } else {
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 400 Bad Request\n");
                }
                throw new SJIOException("readByte: BAD REQUEST METHOD: " + requestMethod);
            }

        } catch (IOException ioe) {
            throw new SJIOException(ioe);
        }
    }

    public void readBytes(byte[] bs) throws SJIOException {

        try {
            String requestMethod = dis.readUTF();
            //System.out.println("Request Method: "+requestMethod);
            while (requestMethod.compareTo("HTTP/1.1 200 OK\n") == 0 | requestMethod.compareTo("HTTP/1.1 400 Bad Request\n") == 0) {

                synchronized (lock_status) {
                    status = requestMethod;
                    lock_status.notify();
                }
                synchronized (lock_msgSent) {
                    numMsgSent--;
                }
                requestMethod = dis.readUTF();
            }

            StringTokenizer st = new StringTokenizer(requestMethod);
            String method = st.nextToken();

            if (method.compareTo("GET") == 0 | method.compareTo("HEAD") == 0 |
                method.compareTo("PUT") == 0 | method.compareTo("DELETE") == 0 |
                method.compareTo("TRACE") == 0 | method.compareTo("OPTIONS") == 0 |
                method.compareTo("CONNECT") == 0) {
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 Bad Request\n");
                }
                throw new SJIOException("readBytes: BAD REQUEST METHOD: " + requestMethod);
            }

            if (method.compareTo("POST") == 0) {
                String token = dis.readUTF();
                String header = token;
                while (token.compareTo("\n") != 0) {
                    token = dis.readUTF();
                    header = header.concat("\n").concat(token);
                }
                //System.out.println("Header: "+header);
                dis.readFully(bs);
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 200 OK\n");
                }
            } else {
                synchronized (lock_sending) {
                    dos.writeUTF("HTTP/1.1 400 Bad Request\n");
                }
                throw new SJIOException("readBytes: BAD REQUEST METHOD: " + requestMethod);
            }
        } catch (IOException ioe) {
            throw new SJIOException(ioe);
        }

    }

    public void flush() throws SJIOException {

        try {
            dos.flush();
        } catch (IOException ioe) {
            throw new SJIOException(ioe);
        }
    }

    public String getHostName() {

        return socket.getInetAddress().getHostName();
    }

    public int getPort() {

        return socket.getPort();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }
}
