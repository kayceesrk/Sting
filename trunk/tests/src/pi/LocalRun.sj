//$ bin/sessionjc tests/src/pi/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pi.LocalRun 0.01 1024 

package pi;

/**
 * 
 * @author Raymond
 *
 */
public class LocalRun
{
	public static void main(String[] args) throws Exception
	{
		final String target = args[0];
		final String trials = args[1];		
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Service.main(new String[] { "4441", trials, "localhost", "4443", "localhost", "4444" } );
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}					
			}
		}.start();	
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Random.main(new String[] { "4442" } );
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();		
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Worker.main(new String[] { "4443", "localhost", "4442", "256" } );
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Worker.main(new String[] { "4444", "localhost", "4442", "512" } );
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();			

		Thread.sleep(500);
		
		new Thread()
		{
			public void run()
			{
				try
				{
					Client.main(new String[] { target, "localhost", "4441" });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}
			}
		}.start();
	}
}
