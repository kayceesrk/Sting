package sessionj.runtime2.util;

import java.io.*;
import java.util.*;

import polyglot.types.*;

import sessionj.types.sesstypes.*;
import sessionj.util.SJLabel;

import sessionj.runtime2.*;

public final class SJRuntimeUtils
{
	private static final boolean debugMode = true;

	//public static final int SJ_SERIALIZED_INT_LENGTH = 4;

	private SJRuntimeUtils() { }

	/*public static byte[] serializeObject(Object o) throws SJIOException
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Could optimise by reusing the byte array.
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(o);

			return baos.toByteArray();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public static Object deserializeObject(byte[] bs) throws SJIOException
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			ObjectInputStream ois = new ObjectInputStream(bais);

			return ois.readObject();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJIOException(cnfe);
		}
	}

	public static byte[] serializeInt(int i) throws SJIOException
	{
		byte[] bs = new byte[4];

		bs[0] = (byte)(i >> 24);
		bs[1] = (byte)((i << 8) >> 24);
		bs[2] = (byte)((i << 16) >> 24);
		bs[3] = (byte)((i << 24) >> 24);

		return bs;
	}

	public static int deserializeInt(byte[] bs) throws SJIOException
	{
		int i = 0;

		i += (bs[0] & 255) << 24;
		i += (bs[1] & 255) << 16;
		i += (bs[2] & 255) << 8;
		i += (bs[3] & 255);

		return i;
	}*/

	/*public static Map<String, Long> getClassSVUIDs(SJSessionType sjtype) throws SJIOException, ClassNotFoundException
	{
		HashMap<String, Long> ours = new HashMap<String, Long>();

		while (sjtype != null)
		{
			if (sjtype instanceof SJOutbranchType)
			{
				SJOutbranchType obt = (SJOutbranchType) sjtype;

				for (SJLabel lab : obt.labelSet())
				{
					ours.putAll(getClassSVUIDs(obt.branchCase(lab)));
				}
			}
			else if (sjtype instanceof SJInbranchType)
			{
				SJInbranchType ibt = (SJInbranchType) sjtype;

				for (SJLabel lab : ibt.labelSet())
				{
					ours.putAll(getClassSVUIDs(ibt.branchCase(lab)));
				}
			}
			else if (sjtype instanceof SJOutwhileType)
			{
				ours.putAll(getClassSVUIDs(((SJOutwhileType) sjtype).body()));
			}
			else if (sjtype instanceof SJInwhileType)
			{
				ours.putAll(getClassSVUIDs(((SJInwhileType) sjtype).body()));
			}
			else if (sjtype instanceof SJRecursionType)
			{
				ours.putAll(getClassSVUIDs(((SJRecursionType) sjtype).body()));
			}
			else if (sjtype instanceof SJSendType)
			{
				ours.putAll(getMessageSVUID(((SJSendType) sjtype).messageType()));
			}
			else if (sjtype instanceof SJReceiveType)
			{
				ours.putAll(getMessageSVUID(((SJReceiveType) sjtype).messageType()));
			}
			else if (sjtype instanceof SJRecurseType)
			{
				// Nothing to do.
			}

			sjtype = sjtype.child();
		}

		return ours;
	}

	private static Map<String, Long> getMessageSVUID(Type messageType) throws SJIOException, ClassNotFoundException // SJCompilerUtils has a simpler version of this routine.
	{
		HashMap<String, Long> ours = new HashMap<String, Long>();

		if (messageType instanceof SJSessionType) // Should come before ordinary class cases? (But SJSessionType shouldn't be a class type).
		{
			ours.putAll(getClassSVUIDs((SJSessionType) messageType));
		}
		else if (messageType.isPrimitive())
		{
			// No SVUID needed for primitive types.
		}
		else if (messageType.isClass()) // Duplicated from above.
		{
			String className = messageType.toClass().fullName();
			Class<?> c = Class.forName(className);

			if (c.isInterface())
			{
				// Interfaces don't have SVUIDs (so what should we do here?). // This encourages use of abstract classes rather than interfaces for message types?
			}
			else
			{
				ObjectStreamClass osc = ObjectStreamClass.lookup(c);

				if (osc == null)
				{
					throw new SJIOException("Class not serializable: " + c);
				}

				ours.put(className, osc.getSerialVersionUID()); // Not possible to find a different SVUID for the same (name) class here? // SVUIDs could be recorded in the session type objects. // Put currently problems working with these values at compilation time if the class binary is not available a priori (SVUID value not built yet?).
			}
		}
		else if (messageType.isArray())
		{
			throw new SJIOException("Array types not done yet: " + messageType);
		}

		return ours;
	}*/

	public static void closeStream(InputStream is) throws IOException
	{
		if (is != null)
		{
			is.close();
		}
	}

	public static void closeStream(OutputStream os) throws IOException
	{
		if (os != null)
		{
			os.flush(); // Is this useful?
			os.close();
		}
	}

	public static final void debugPrint(Object obj)
	{
		if (debugMode)
		{
			System.out.print(obj);
		}
	}

	public static final void debugPrintln(Object obj)
	{
		if (debugMode)
		{
			System.out.println(obj);
		}
	}
}
