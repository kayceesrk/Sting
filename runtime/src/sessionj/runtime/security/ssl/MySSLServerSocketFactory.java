package sessionj.runtime.security.ssl;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.net.ssl.*;

import sessionj.runtime.*;
import sessionj.runtime.security.Constants;
import sessionj.runtime.session.security.*;


/**
 * Implements a customised SSLServerSocketFactory
 * @author Nuno Alves
 *
 */
public class MySSLServerSocketFactory extends SSLServerSocketFactory implements Serializable {

	private static final long serialVersionUID = 1L;
	protected SSLServerSocketFactory ssf;
	
	
	/**
	 * Creates a Factory that produces customised SSLSockets.
	 * A SSLContext is created, so the user does not have to bother with the properties of SSL,
	 * being everything done automatically.
	 */
	public MySSLServerSocketFactory(){
		
		try {
		    KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
		    KeyStore serverStore = KeyStore.getInstance("JKS");
		
		    // Loads keystore
		    serverStore.load(new FileInputStream(Constants.DEFAULT_KEYSTORE), "password".toCharArray());
		    mgrFact.init(serverStore, "password".toCharArray());
		
		    // create a context and initialises it
		    SSLContext sslContext = SSLContext.getInstance("TLS");
		    sslContext.init(mgrFact.getKeyManagers(), null, null);
			
			ssf = (SSLServerSocketFactory)sslContext.getServerSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Most of the methods are delegated to an instance of a normal SSLServerSocketFactory.
	 * Only the methods createServerSocket are changed to deliver a customised type of SSLSocket.
	 * 
	 * @param ssf SSLServerSocketFactory passed to delegate the most of the methods.
	 */
	public MySSLServerSocketFactory(SSLServerSocketFactory ssf) {
		this.ssf = ssf;
	}
	
	
	/**
	 * Redefinition of the method getDefault
	 */
	public static synchronized SSLServerSocketFactory getDefault() {
		return new MySSLServerSocketFactory((SSLServerSocketFactory)SSLServerSocketFactory.getDefault());
	}
	
	/**
	 * Delegated Method
	 */
	@Override
	public String[] getDefaultCipherSuites() {
		return ssf.getDefaultCipherSuites(); 
	}

	/**
	 * Delegated Method
	 */
	@Override
	public String[] getSupportedCipherSuites() {
		return ssf.getSupportedCipherSuites();
	}
	
	
	/**
	 * Creates a new customised SSL server socket bound to the given port.
	 */
	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		SSLServerSocket s = (SSLServerSocket)ssf.createServerSocket(port);
		//s.setWantClientAuth(true);
		return new MySSLServerSocket(s);
	}

	
	/**
	 * Creates a server socket and binds it to the specified local port number, with the specified backlog.
	 */
	@Override
	public ServerSocket createServerSocket(int port, int backlog)
	throws IOException {
		return new MySSLServerSocket((SSLServerSocket)ssf.createServerSocket(port, backlog));
	}

	
	/**
	 * Create a server socket with the specified port, backlog, and local IP address to bind.
	 */
	@Override
	public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress)
	throws IOException {
		return new MySSLServerSocket((SSLServerSocket)ssf.createServerSocket(port, backlog, ifAddress));
	}

}
