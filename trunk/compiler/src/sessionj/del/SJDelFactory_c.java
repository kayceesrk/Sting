package sessionj.del;

import polyglot.ast.*;

public class SJDelFactory_c extends AbstractDelFactory_c implements SJDelFactory
{
	public SJDelFactory_c()
	{
    }

	/*public SJReceiveDel SJReceiveDel()
	{
		return new SJReceiveDel_c();
	}*/

	public SJSpawnDel SJSpawnDel()
	{
		return new SJSpawnDel_c();
	}
}
