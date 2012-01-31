package sessionj.runtime.transport.http;

import java.io.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.nio.channels.SelectableChannel;
import javax.net.ssl.*;
import javax.net.ServerSocketFactory;

import sessionj.runtime.*;
import sessionj.runtime.transport.*;

public class SJHTTPSAcceptor extends AbstractWithTransport implements SJConnectionAcceptor {

    private final SSLServerSocket sss;

    public SJHTTPSAcceptor(int port, SJTransport transport) throws SJIOException {
	    super(transport);

        try {
            KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
            KeyStore serverStore = KeyStore.getInstance("JKS");

            // Loads keystore
            String DEFAULT_KEYSTORE = "C:\\cygwin\\home\\Raymond\\code\\java\\eclipse\\sessionj-cvs\\serverKeystore";
            serverStore.load(new FileInputStream(DEFAULT_KEYSTORE), "password".toCharArray());
            mgrFact.init(serverStore, "password".toCharArray());

            // create a context and initialises it
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(mgrFact.getKeyManagers(), null, null);

        } catch (NoSuchAlgorithmException e) {
            throw new SJIOException(e);
        } catch (Exception e) {
            throw new SJIOException(e);
        }

        try {
            ServerSocketFactory sslSocketFactory = SSLServerSocketFactory.getDefault();

            sss = (SSLServerSocket) sslSocketFactory.createServerSocket(port); // Didn't bother to explicitly check portInUse.
        }
        catch (IOException ioe) {
            throw new SJIOException(ioe);
        }
    }

    public SJConnection accept() throws SJIOException {

        try {
            if (sss == null) {
                throw new SJIOException('[' + getTransportName() + "] Connection acceptor not open.");
            }

            SSLSocket s = (SSLSocket) sss.accept();

            return new SJHTTPSConnection(s, s.getInputStream(), s.getOutputStream(), getTransport());
        }
        catch (IOException ioe) {
            throw new SJIOException(ioe);
        }

    }

    public SelectableChannel acceptSelectableChannel() {
        throw new UnsupportedOperationException("TODO");
    }

    public void close() {

        try {
            if (sss != null) {
                sss.close();
            }
        }
        catch (IOException ignored) {
        }

    }

    public boolean interruptToClose() {

        return false;
    }

    public boolean isClosed() {

        return sss.isClosed();
    }

}
