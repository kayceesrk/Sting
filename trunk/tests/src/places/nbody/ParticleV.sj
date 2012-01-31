//$ bin/sessionjc -cp tests/classes/ tests/src/places/nbody/ParticleV.sj -d tests/classes/
/**
 * 
 * @author Andi
 *
 */

package places.nbody;

import java.io.Serializable;

public class ParticleV implements Serializable
{	
	public double vi_old, vj_old;	
	public double ai_old, aj_old;
	public double ai, aj;
	
	public String toString()
	{
		return 
			 "<vi_old=" + vi_old + ", " 
			+ "vj_old=" + vj_old + ", "
			+ "ai=" + ai + ", "
			+ "aj=" + aj + ">";
	}
}
