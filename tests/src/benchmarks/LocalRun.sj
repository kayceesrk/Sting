//$ bin/sessionjc -cp tests/classes tests/src/benchmarks/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.LocalRun 8888 1000 

package benchmarks;

/**
 * 
 * @author Raymond, Andi
 *
 */
public class LocalRun
{
	public static void main(String[] args) throws Exception
	{
		final String ports = args[0];
		final String sessLen = args[1];
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Server.main(new String[] { ports } );
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}					
			}
		}.start();		
		
		while (true)
		{
			Thread.sleep(500);
			
			new Thread()
			{
				public void run()
				{
					try
					{
						Client.main(new String[] { "localhost", ports, "1000", sessLen } );
					}
					catch (Exception x)
					{
						throw new RuntimeException(x);
					}				
				}
			}.start();
		}
	}
}
