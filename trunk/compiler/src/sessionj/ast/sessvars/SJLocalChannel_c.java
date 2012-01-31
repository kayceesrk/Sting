package sessionj.ast.sessvars;

import polyglot.ast.Id;
import polyglot.ast.Local_c;
import polyglot.util.Position;

public class SJLocalChannel_c extends Local_c implements SJLocalChannel
{
	public SJLocalChannel_c(Position pos, Id id)
	{
		super(pos, id);
	}
	
	public String sjname()
	{
		return name();
	}

    public boolean isFinal() {
        return flags().isFinal();
    }
}
