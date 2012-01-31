//$ bin/sessionjc tests/src/compiler/protocoldecls/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.protocoldecls.Test

package compiler.protocoldecls;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class B
{	
	public static final noalias protocol pb { cbegin.!(Integer) }
}
