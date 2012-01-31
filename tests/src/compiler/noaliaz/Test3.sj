//$ bin/sessionjc tests/src/compiler/noaliaz/Test3.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.noaliaz.Test3

package compiler.noaliaz;

public class Test3 
{			
	public static void main(String[] args)
	{
		noalias double[] ds = new double[100];
		
		noalias String[] ss = new String[100];
		
		ds[0] = 10d;
		
		double[] ds2 = new double[1];
		
		ds[1] = ds2[0]; 
		
		System.out.println(ds[0] + ds[1]);
	}
}
