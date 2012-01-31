//$ bin/sessionjc tests/src/compiler/noaliaz/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.noaliaz.Test

package compiler.noaliaz;

import java.util.*;

public class Test 
{			
	noalias A[] as1 = null;
	
	void m()
	{				
		noalias A[] as2 = null;	
		
		A a3 = (as1[0] = as1[1]).m(as2[0] = as1[1]);
		
		A a4 = (as1[0]).m(as1[0]);
	}
	
	public static void main(String[] args) throws Exception
	{			
		noalias A a1 = new A();
		noalias A a2 = new A();
		
		System.out.println("1: " + a1 + ", " + a2);
		
		a1 = a2;
		//a1.m(a2);
		
		System.out.println("2: " + a1 + ", " + a2);
		
		noalias String m1 = "";
		noalias String m2 = "";
		
		int x = 0;
		
		m((1 == x) ? m1 : m2);
		//m(m2);
	}
	
	static void m(noalias String m)
	{
		noalias A a = new A();
		A aa = new A();
		
		a.a3 = aa; 
	}
	
	static class A
	{
		A a3;
		
		noalias A a1;
		noalias A a2;
		
		static noalias A sa;
		
		A()
		{
			//this(sa);
			//m(this);
		}
		
		A(noalias A a)
		{
			
		}
		
		public noalias A m()
		{
			return a1.m(a1, a2);
		}

		public noalias A m(A a)
		{
			return new A();
		}
		
		public noalias A m(noalias A a1, noalias A a2)
		{
			return a1;
		}
	}
}
