//$ bin/sessionjc tests/src/compiler/createops/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.createops.Test

package compiler.createops;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class A
{
	public static final noalias protocol p2 { cbegin.?(!(int).!(String)) }
}

public class Test 
{	
	public static void main(String[] args) throws Exception
	{						
		final noalias protocol p1 { cbegin.?(!(int).!(String)) }
		
		final noalias SJServerAddress c = SJServerAddress.create(p1, "localhost", 8888);
		
		final noalias SJSocket s1 = c.request();
		
		final noalias SJSocket s2;
		
		s2 = SJServerAddress.create(A.p2, "localhost", 8888).request();
	}
}
