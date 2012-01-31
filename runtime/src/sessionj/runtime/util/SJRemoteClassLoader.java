package sessionj.runtime.util;

import java.io.*;
import java.util.HashMap;

//import sessionj.runtime2.net.SJSocket;

/*
 * Responsible for downloading classes from the session partner if necessary
 * where possible. It also caches download classes.
 *
 */
public class SJRemoteClassLoader extends ClassLoader
{
	private HashMap<String, byte[]> classCache = new HashMap<String, byte[]>();

//	private SJAbstractSocket socket;
	private SJClassDownloader sjcd = new SJClassDownloader();

	public SJRemoteClassLoader(/*SJAbstractSocket socket*/)
	{
		super();

//		this.socket = socket;
	}

	public SJRemoteClassLoader(ClassLoader parent/*, SJAbstractSocket socket*/)
	{
		super(parent);

//		this.socket = socket;
	}

//	public boolean remoteClassLoadingEnabled() // To make this value available to SJClassResolver.
//	{
//		return socket.remoteClassLoadingEnabled();
//	}

	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		byte[] classBytes = getClassBytes(name);

		return defineClass(name, classBytes, 0, classBytes.length);
	}

	public byte[] getClassBytes(String name) throws ClassNotFoundException
	{
		byte[] classBytes;

		if (classCache.containsKey(name))
		{
			classBytes = classCache.get(name);
		}
		else
		{
			classBytes = downloadClass(name); // The class resolver checks if remote class loading is enabled.

			cacheClass(name, classBytes);
		}

		return classBytes;
	}

	private byte[] downloadClass(String name) throws ClassNotFoundException
	{
		// Codebase parameter can be set after the socket has been created. However, it cannot be changed after the session has been opened.
		// The remote class downloader checks that the codebase has been set properly.
		//sjcd.setCodebase(socket.getCodebase());

		try
		{
			byte[] classBytes = sjcd.download(name);

			SJRuntimeUtils.debugPrintln("Downloaded class: " + name);

			return classBytes;
		}
		catch (IOException ioe)
		{
			throw new ClassNotFoundException(ioe.getMessage());
		}
	}

	private void cacheClass(String name, byte[] classBytes)
	{
		classCache.put(name, classBytes);
	}
}
