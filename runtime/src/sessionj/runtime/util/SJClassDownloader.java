package sessionj.runtime.util;

import java.io.*;
import java.net.*;

import static sessionj.runtime.util.SJRuntimeUtils.*;

public class SJClassDownloader
{
	private URL codebase;

	public SJClassDownloader()
	{

	}

	public SJClassDownloader(URL codebase)
	{
		this.codebase = codebase;
	}

	public URL getCodebase()
	{
		return codebase;
	}

	public void setCodebase(URL codebase)
	{
		this.codebase = codebase;
	}

	public byte[] download(String name) throws IOException
	{
		if (codebase == null)
		{
			throw new IOException("Codebase not set: " + codebase);
		}

		URLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;

		try
		{
			// Unoptimised: makes a new connection for each download.
			conn = new URL(getCodebase().toString() + "/" + name + ".class").openConnection();
			is = conn.getInputStream();
			baos = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(baos);

			byte[] buffer = new byte[1024]; // Factor out constants (and above).

			for (int numRead = is.read(buffer); numRead != -1; numRead = is.read(buffer))
			{
				bos.write(buffer, 0, numRead);
			}

			bos.flush();

			return baos.toByteArray();
		}
		finally
		{
			try
			{
				closeStream(bos);
				closeStream(baos);
				closeStream(is);

				/*if (conn != null)
				{
					conn.close(); // How to close?
				}*/
			}
			catch (IOException ioe) { }
		}
	}
}
