//$ bin/sessionjc -cp tests/classes/ tests/src/thesis/benchmark/bmark3/Particle.sj -d tests/classes/

package thesis.benchmark.bmark3;

import java.io.Serializable;

// noalias-compatible
public class Particle implements Serializable
{	
	public double x, y;
	public double m;
	
	public String toString()
	{
		return "<x=" + x + ", " + "y=" + y + ", " + "m=" + m + ">";
	}
}
