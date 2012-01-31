//$ bin/sessionjc tests/src/compiler/noaliaz/Test2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.noaliaz.Test2

package compiler.noaliaz;

public class Test2 
{			
	class A
	{
		noalias A a;
		//A a2;
		
		void m()
		{
			a = new A();
		}
	}
	
	noalias A a;
	
	void m1()
	{
		a = new A();
		
		noalias String m = new String("");
	}
	
	A m2()
	{
		return a;
	}
}
