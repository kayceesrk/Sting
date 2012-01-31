package sessionj.runtime.security.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * Implements a customised socket that supports the SRP protocol
 * 
 * @author Nuno Alves
 *
 */
public class MySSLSocket extends SSLSocket {

	private SSLSocket s;
	
	/**
	 * Creates a customised SSLSocket that delegates most of the methods to the given SSLSocket.
	 * The methods getOuputStream and getInputStream can be redefined to cipher the information.
	 * @param s SSLSocket passed to delegate the most of the methods.
	 */
	public MySSLSocket(SSLSocket s) {
		this.s = s;
	}


	/**
	 * Delegated Method.
	 */
	@Override
	public void addHandshakeCompletedListener(HandshakeCompletedListener arg0) {
		s.addHandshakeCompletedListener(arg0);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public boolean getEnableSessionCreation() {
		return s.getEnableSessionCreation();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public String[] getEnabledCipherSuites() {
		return s.getEnabledCipherSuites();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public String[] getEnabledProtocols() {
		return s.getEnabledProtocols();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public boolean getNeedClientAuth() {
		return s.getNeedClientAuth();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public SSLSession getSession() {
		return s.getSession();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public String[] getSupportedCipherSuites() {
		return s.getSupportedCipherSuites();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public String[] getSupportedProtocols() {
		return s.getSupportedProtocols();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public boolean getUseClientMode() {
		return s.getUseClientMode();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public boolean getWantClientAuth() {
		return s.getWantClientAuth();
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void removeHandshakeCompletedListener(HandshakeCompletedListener listener) {
		s.removeHandshakeCompletedListener(listener);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setEnableSessionCreation(boolean flag) {
		s.setEnableSessionCreation(flag);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setEnabledCipherSuites(String[] suites) {
		s.setEnabledCipherSuites(suites);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setEnabledProtocols(String[] protocols) {
		s.setEnabledProtocols(protocols);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setNeedClientAuth(boolean need) {
		s.setNeedClientAuth(need);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setUseClientMode(boolean mode) {
		s.setUseClientMode(mode);
		
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void setWantClientAuth(boolean want) {
		s.setWantClientAuth(want);
	}

	/**
	 * Delegated Method.
	 */
	@Override
	public void startHandshake() throws IOException {
		s.startHandshake();
	}

	/**
	 * Delegated Method
	 */
	@Override
	public OutputStream getOutputStream() throws IOException{
		// Can cipher the outputstream (future work)
		return s.getOutputStream();
	}

	/**
	 * Delegated Method
	 */
	@Override
	public InputStream getInputStream() throws IOException{
		// Can decipher the inputstream (future work)
		return s.getInputStream();
	}
	
	/**
	 * Delegated Method
	 */
	@Override
	public String toString() {
		return s.toString();
	}
	
	/**
	 * Delegated Method
	 */
	@Override
	 public InetAddress getInetAddress() {
		return s.getInetAddress();
	}
	
	/**
	 * Delegated Method
	 */
	public void close() throws IOException {
		s.close();
	}
	
}
