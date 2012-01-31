/**
 * 
 */
package sessionj.runtime.session;

import sessionj.runtime.SJException;

import java.io.Serializable;

/**
 * @author Raymond
 *
 */
public abstract class SJControlSignal extends SJException implements Serializable
{
  //private static final long serialVersionUID = SJ_VERSION;
	
	public SJControlSignal()
	{
    }
}
