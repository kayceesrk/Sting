package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import sessionj.SJConstants;
import sessionj.ast.SJNodeFactory;

import java.util.List;

public class SJSend_c extends SJPass_c implements SJSend  
{	
	public SJSend_c(Position pos, SJNodeFactory nf, List arguments, List targets)
	{
		super(pos, nf, SJConstants.SJ_SOCKET_SEND, arguments, targets);
	}
}
