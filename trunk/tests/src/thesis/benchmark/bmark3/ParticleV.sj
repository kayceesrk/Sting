//$ bin/sessionjc -cp tests/classes/ tests/src/thesis/benchmark/bmark3/ParticleV.sj -d tests/classes/

package thesis.benchmark.bmark3;

import java.io.Serializable;

// noalias-compatible
public class ParticleV implements Serializable
{	
	public double vi_old, vj_old;	
	public double ai_old, aj_old;
	public double ai, aj;
	
	public String toString()
	{
		return "<vi_old=" + vi_old + ", " + "vj_old=" + vj_old + ", " + "ai=" + ai + ", " + "aj=" + aj + ">";
	}
}
