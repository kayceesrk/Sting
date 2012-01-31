package sessionj.runtime.security.ssl;

import java.io.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import sessionj.runtime.security.crypt.Util;
import sessionj.runtime.security.srp.NoSuchUserException;
import sessionj.runtime.security.srp.PasswordFile;
import sessionj.runtime.security.srp.SRPServer;

/**
 * Creates a Custom SSLServerSocket which redefines the accept method to support the SRP protocol. 
 * 
 * @author Nuno Alves
 *
 */
public class MySSLServerSocket extends SSLServerSocket implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private SSLServerSocket s;
	

	/**
	 * Creates an instance of a Customised SSLServerSocket.
	 * Most of the methods are delegated to an instance of a normal SSLServerSocket.
	 * Only the method accept is changed to deliver a customised type of SSLServerSocket.
	 * 
	 * @param s SSLServerSocket passed to delegate the most of the methods.
	 * @throws IOException
	 */
	public MySSLServerSocket(SSLServerSocket s) throws IOException{
		this.s = s;
	}
	
	/**
	 * Delegated Method
	 */
	@Override
	public boolean getEnableSessionCreation() {
		return s.getEnableSessionCreation();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public String[] getEnabledCipherSuites() {
		return s.getEnabledCipherSuites();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public String[] getEnabledProtocols() {
		return s.getEnabledProtocols();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public boolean getNeedClientAuth() {
		return s.getNeedClientAuth();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public String[] getSupportedCipherSuites() {
		return s.getSupportedCipherSuites();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public String[] getSupportedProtocols() {
		return s.getSupportedProtocols();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public boolean getUseClientMode() {
		return s.getUseClientMode();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public boolean getWantClientAuth() {
		return s.getWantClientAuth();
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setEnableSessionCreation(boolean flag) {
		s.setEnableSessionCreation(flag);
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setEnabledCipherSuites(String[] suites) {
		s.setEnabledCipherSuites(suites);
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setEnabledProtocols(String[] protocols) {
		s.setEnabledProtocols(protocols);
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setNeedClientAuth(boolean need) {
		s.setNeedClientAuth(need);
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setUseClientMode(boolean mode) {
		s.setUseClientMode(mode);
	}
	/**
	 * Delegated Method
	 */
	@Override
	public void setWantClientAuth(boolean want) {
		s.setWantClientAuth(want);
	}
	
	/**
	 * Delegated Method
	 */
	public void close() throws IOException {
		s.close();
	}
	
	/**
	 * Returns a Socket customised for SRP.
	 */
	@Override
	public SSLSocket accept() throws IOException {
		
		SSLSocket m = (SSLSocket)s.accept();
		
		try{
			InputStream is = m.getInputStream();
			InputStreamReader inputstreamreader = new InputStreamReader(is);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            
            OutputStream os = m.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(os);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
            
            String user = null;
            SRPServer serv = null;
            try {
                PasswordFile pf;
              	pf = new PasswordFile();
              	
              	// Reads username
                user = bufferedreader.readLine();
                serv = new SRPServer(user, pf);
            }
            catch(FileNotFoundException e) {
                System.err.println("Password file not found");
                System.exit(1);
            }
            catch(NoSuchUserException e) {
                System.err.println("User " + user + " unknown");
                System.exit(1);
            }
            catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            
            // Sends n, g and salt to client
            bufferedwriter.write(Util.tob64(serv.modulus()) + '\n'); // n
            bufferedwriter.flush();
            bufferedwriter.write(Util.tob64(serv.generator()) + '\n'); // g
            bufferedwriter.flush();
            bufferedwriter.write(Util.tob64(serv.salt()) + '\n'); // salt
            bufferedwriter.flush();

            byte[] ex = serv.generateExponential();
            
            // Receives A from client
            String astr = bufferedreader.readLine();
            
            // Sends B to client
            bufferedwriter.write(Util.tob64(ex) + '\n');
            bufferedwriter.flush();
            
            // Calculates session key
            byte[] key = serv.getSessionKey(Util.fromb64(astr));
            //System.out.println("Session key: " + Util.tohex(key));
            
            // Receives response from client
            String resp = bufferedreader.readLine();

            if(serv.verify(Util.fromhex(resp))) {
            	System.out.println("Authentication successful.");
            	bufferedwriter.write("OK" + '\n');
                bufferedwriter.flush();
            }
            else {
            	System.out.println("Authentication failed.");
            	bufferedwriter.write("ERROR" + '\n');
                bufferedwriter.flush();
            	throw new InvalidPasswordException("Authentication Failed [SRP]: Wrong password");
            }
          
	        return new MySSLSocket(m);
	        
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}