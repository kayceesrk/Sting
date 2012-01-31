package sessionj.runtime.security.ssl;

import java.io.*;
import java.net.*;
import java.security.KeyStore;

import javax.net.ssl.*;

import sessionj.runtime.security.Constants;
import sessionj.runtime.security.crypt.Util;
import sessionj.runtime.security.srp.SRPClient;

/**
 * Implements a SSLSocketFactory that produces sockets configured by the some automatised parameters.
 * 
 * @author Nuno Alves
 *
 */
public class MySSLSocketFactory extends SSLSocketFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	SSLSocketFactory ssf;
	private String userName;
	private String pwd;


	/**
	 * Creates a factory that produces SSLSockets configured by the some parameters
	 * Note that username and pwd are only defined in delegation cases
	 * @param user username
	 * @param pwd password
	 * 
	 */
	public MySSLSocketFactory(String user, String pwd) {
		this.userName = user;
		this.pwd = pwd;
		
		try {
			 // set up a trust manager so we can recognize the server
		    TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
		    KeyStore            trustStore = KeyStore.getInstance("JKS");
		
		    trustStore.load(new FileInputStream(Constants.DEFAULT_KEYSTORE), "password".toCharArray());
		    trustFact.init(trustStore);
		
		    // create a context and set up a socket factory
		    SSLContext sslContext = SSLContext.getInstance("TLS");
		    sslContext.init(null, trustFact.getTrustManagers(), null);
		    ssf = (SSLSocketFactory)sslContext.getSocketFactory();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Most of the methods are delegated to an instance of a normal SSLSocketFactory.
	 * Only the methods createServerSocket are changed to deliver a customised type of SSLSocket.
	 * 
	 * @param ssf SSLSocketFactory passed to delegate the most of the methods.
	 */
	public MySSLSocketFactory(SSLSocketFactory ssf) {
		this.ssf = ssf;
	}
	
	
	private SSLSocket performSRP(SSLSocket s) {
		
		try {
			InputStream is = s.getInputStream();
			InputStreamReader inputstreamreader = new InputStreamReader(is);
	        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
	        
	        OutputStream os = s.getOutputStream();
	        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(os);
	        BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
	        
	        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	        
            // Reads username from console and sends to server if not already defined
	        // otherwise sends the previously defined username
            if (userName == null) {
                System.out.println("Enter Username: ");
                System.out.flush();
                userName = input.readLine();
            }
            	
            bufferedwriter.write(userName + '\n');
            bufferedwriter.flush();

            // Receives n, g and salt from server
            String str;
            str = bufferedreader.readLine(); //n
            if (str == null) {
            	System.out.println("[Client] Server disconnected for not recognising username.");
            	return null;
            }
            byte[] n = Util.fromb64(str);
            str = bufferedreader.readLine(); //g
            byte[] g = Util.fromb64(str);
            str = bufferedreader.readLine(); //salt
            byte[] salt = Util.fromb64(str);
            
            SRPClient cli = new SRPClient(userName, n, g, salt);
            
            // Sends A to server
            byte[] ex = cli.generateExponential();
            bufferedwriter.write(Util.tob64(ex) + '\n');
            bufferedwriter.flush();
            
            // Receives B from server
            str = bufferedreader.readLine();
            
            // Reads password from user
            if (pwd == null) {
	            System.out.println("Enter Password: ");
	            System.out.flush();
	            pwd = input.readLine();
            }
            
            cli.inputPassword(pwd);
            
            byte[] key = cli.getSessionKey(Util.fromb64(str));
            //System.out.println("Session key: " + Util.tohex(key));

            // Sends response to server
            bufferedwriter.write(Util.tohex(cli.response()) + '\n');
            bufferedwriter.flush();
            
            // Receives confirmation of everything
            str = bufferedreader.readLine();
            
            if (!str.equals("OK"))
            	throw new InvalidPasswordException("Authentication Failed [SRP]: Wrong password");
            
			return new MySSLSocket(s);
		
		} catch(IOException e) {
			System.out.println("[Client] IO Error in SRP");
			return null;
		} catch(InvalidPasswordException e) {
			System.out.println("[Client] Authentication Failed in SRP: Wrong Password");
			return null;
		}
	
	}

	
	/**
	 * Returns the default SSLSocketFactory
	 */
	public static synchronized SSLSocketFactory getDefault() {
		return new MySSLSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault());
	}


	/**
	 * Creates a new socket with the SRP protocol enabled.
	 */
	@Override
	public Socket createSocket(Socket soc, String host, int port, boolean autoClose)
	throws IOException {
		SSLSocket s = (SSLSocket)ssf.createSocket(soc, host, port, autoClose);
		return performSRP(s);
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
	 * Creates a new socket with the SRP protocol enabled.
	 */
	@Override
	public Socket createSocket(String host, int port) throws IOException,
	UnknownHostException {
		SSLSocket s = (SSLSocket)ssf.createSocket(host, port);
		return performSRP(s);
	}
	
	/**
	 * Creates a new socket with the SRP protocol enabled.
	 */
	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		SSLSocket s = (SSLSocket)ssf.createSocket(host, port);
		return performSRP(s);
	}

	/**
	 * Creates a new socket with the SRP protocol enabled.
	 */
	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
	throws IOException, UnknownHostException {
		SSLSocket s = (SSLSocket)ssf.createSocket(host, port, localHost, localPort);
		return performSRP(s);
	}


	/**
	 * Creates a new socket with the SRP protocol enabled.
	 */
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
			int localPort) throws IOException {
		SSLSocket s = (SSLSocket)ssf.createSocket(address, port, localAddress, localPort);
		return performSRP(s);
	}

}
