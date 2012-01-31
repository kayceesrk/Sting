package sessionj.runtime2.service;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

public interface SJCopyRetainingSerialization extends SJSerializationService
{
	public byte[] writeObjectAndRetainCopy(SJSocket s, Object o) throws SJIOException;
}
