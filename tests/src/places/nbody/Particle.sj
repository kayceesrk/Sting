//$ bin/sessionjc -cp tests/classes/ tests/src/places/nbody/Particle.sj -d tests/classes/
/**
 * 
 * @author Andi
 *
 */

package places.nbody;

import java.io.Serializable;

public class Particle implements Serializable
{	
	public double x, y;
	public double m;
	
	public String toString()
	{
		return 
			 "<x=" + x + ", " 
			+ "y=" + y + ", "
			+ "m=" + m + ">";
	}
}
