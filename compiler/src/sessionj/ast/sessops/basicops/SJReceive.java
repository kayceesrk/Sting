package sessionj.ast.sessops.basicops;

import polyglot.ast.StringLit;

public interface SJReceive extends SJBasicOperation
{
    SJReceive addEncodedArg(StringLit encoded);
}
