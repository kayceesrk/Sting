//$ bin/sessionjc tests/src/compiler/sesstypes/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sesstypes.Test

package compiler.sesstypes;

import java.util.*;

import sessionj.types.*;
import sessionj.types.sesstypes.*;
import sessionj.util.*;

public class Test 
{			
	public static void main(String[] args) throws Exception
	{		
		SJTypeSystem sjts = new SJTypeSystem_c();
		
		SJSessionType st1 = sjts.SJRecursionType(new SJLabel("LR"))
			//.body(sjts.SJReceiveType(sjts.Int()));
			.body(sjts.SJInbranchType().branchCase(new SJLabel("L1"), sjts.SJSendType(sjts.Int())));
			//.body(sjts.SJInbranchType().branchCase(new SJLabel("L1"), sjts.SJSendType(sjts.Int())).branchCase(new SJLabel("L2"),sjts.SJSendType(sjts.Int())));
		SJSessionType st2 = sjts.SJRecursionType(new SJLabel("LS"))
			//.body(sjts.SJSendType(sjts.Int()));
			.body(sjts.SJInbranchType().branchCase(new SJLabel("L1"), sjts.SJSendType(sjts.Int())));
		
		System.out.println("st1 = " + st1);
		System.out.println("st2 = " + st2); 
		System.out.println("st1.typeEquals(st2) = " + st1.typeEquals(st2));
		System.out.println("st1.isSubtype(st2) = " + st1.isSubtype(st2));
		System.out.println("st2.isSubtype(st1) = " + st2.isSubtype(st1));
		System.out.println("st1.isDualtype(st2) = " + st1.isDualtype(st2));
	}
}
