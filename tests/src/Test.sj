//$ bin/sessionjc tests/src/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ Test

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.util.*;

import thesis.benchmark.ServerMessage;
import thesis.benchmark.bmark2.NoaliasBinaryTree;

public class Test
{
	//static noalias String f;

	public static void main(String[] args) throws Exception
	{
		protocol p cbegin.!<double[]>
		
		final noalias SJService c = SJService.create(p, "", 1234);
		noalias SJSocket s;
		try (s)
		{
			s = c.request();
			s.send(new double[10]);
		}
		finally { }
		/*//x = y;
		//y = ((x = y) == x) ? null : null;
		//x = ((tmp = y) == tmp) ? (((y = null) == null) ? tmp : null) : null;
		//x = ((tmp = y) == (y = null)) ? null : tmp;

		//x = y = z;
		//x = ((tmp = (y = ((tmp = z) == (z = null)) ? null : tmp)) == (y = null)) ? null : tmp;
		//x = ((tmp = ((tmp = z) == (z = null)) ? null : tmp) == (y = null)) ? null : tmp;
		//x = ((tmp = (y = z)) == (y = z = null)) ? null : tmp;

		x = (...) ? y = z : ...;

		noalias String l = null;
		l = l;
		f = l;
		l = f;
		noalias String l2 = null;
		m(l = l2);
		noalias String l3 = null;
		l = l2 = l3;*/
	}

	/*static noalias String m(noalias String p)
	{
		return p;
	}*/
}

