package sessionj.runtime2.util;

import polyglot.main.Report;
import polyglot.types.*;
import polyglot.types.reflect.*;

import sessionj.ExtensionInfo;


/**
 * Loads class information from class files, or serialized class infomation from within class files.  It does not load from source files.
 */
public class SJClassResolver extends LoadedClassResolver
{
	private static final boolean ALLOW_RAW_CLASSES = true; // Move to SJRuntimeConstants?
	
	//private ExtensionInfo extInfo;
	//private SJRemoteClassLoader sjrcl;

	public SJClassResolver(TypeSystem ts, ExtensionInfo extInfo)
	{
		super(ts, extInfo.getOptions().constructFullClasspath(), new ClassFileLoader(extInfo), extInfo.version(), ALLOW_RAW_CLASSES); // Combine ClassFileLoader and SJRemoteClassLoader?

		//this.extInfo = extInfo;
	}

	/*// Session runtime uses a single static SJClassResolver, but the individual remote class loader for each socket instance (with their own codebase targets) should be set each time the socket is used (to receive, and if remote class loading is enabled).
	// Actually, this only needs to be set for protocol (session type) decoding: this is the only place where the SJTypeSystem and SJClassResolver are needed - the remote class loader is used directly by SJObjectInputStream to receive messages. The important point is that the remote class loader is used for both protocol decoding and message receiving - so classes loaded eagerly at initiation can be used when the actual message is received (same loader namespace).
	public /*synchronized* void setRemoteClassLoader(SJRemoteClassLoader sjrcl) 
	{
		this.sjrcl = sjrcl;
	}*/

  /**
   * Find a type by name.
   */
	public Named find(String name) throws SemanticException
	{
		if (Report.should_report(report_topics, 3))  // Code duplicated from Polyglot LoadedClassResolver.
		{
			Report.report(3, "LoadedCR.find(" + name + ")");
		}

		Named result = null;

		// First try the class file.
		ClassFile clazz = loadFile(name);

		if (clazz == null)
		{
			/*try
			{
				if (sjrcl != null /*&& sjrcl.remoteClassLoadingEnabled()*) // For sockets, remote class loading not yet enabled when the socket is first created (and the protocol argument is being decoded - all classes must be locally available). Servers do not create remote class loaders.
				{
					clazz = new ClassFile(null, sjrcl.getClassBytes(name), extInfo); // Factor out constants. // Class is not on the local classpath; getClassBytes gets the cached class binary or else tries to download it.
				}
				else
				{
					throw new ClassNotFoundException(name);
				}
			}
			catch (ClassNotFoundException cnfe)*/
			{
				throw new NoClassException(name);
			}
		}

		// Check for encoded type information.
		if (clazz.encodedClassType(version.name()) != null)
		{
			if (Report.should_report(report_topics, 4))
			{
				Report.report(4, "Using encoded class type for " + name);
			}

			result = getEncodedType(clazz, name);
		}

		if (allowRawClasses)
		{
			if (Report.should_report(report_topics, 4))
			{
				Report.report(4, "Using raw class file for " + name);
			}

			result = ts.classFileLazyClassInitializer(clazz).type();
		}

		// Verify that the type we loaded has the right name.  This prevents, for example, requesting a type through its mangled (class file) name.
		if (result != null)
		{
			if (name.equals(result.fullName()))
			{
				return result;
			}

			if (result instanceof ClassType && name.equals(ts.getTransformedClassName((ClassType) result)))
			{
				return result;
			}
		}

		// We have a raw class, but are not allowed to use it, and cannot find appropriate encoded info.
		throw new SemanticException("Unable to find a suitable definition of \"" + name + "\". A class file was found," + " but it did not contain appropriate information for this" + " language extension. If the source for this file is written" + " in the language extension, try recompiling the source code.");
	}
}
