package sessionj.extension;

import java.util.*;

import polyglot.ast.*;

import sessionj.extension.noalias.*;
import sessionj.extension.sessops.*;
import sessionj.extension.sesstypes.*;

import sessionj.types.sesstypes.*;

public class SJExtFactory_c extends AbstractExtFactory_c implements
    SJExtFactory
{
    public SJNoAliasExt SJNoAliasExt(boolean isNoAlias)
	{
		return new SJNoAliasExt_c(isNoAlias);
	}
	
	public SJNoAliasFinalExt SJNoAliasFinalExt(boolean isNoAlias, boolean isFinal)
	{
		return new SJNoAliasFinalExt_c(isNoAlias, isFinal);
	}
	
	public SJNoAliasVariablesExt SJNoAliasVariablesExt()
	{
		return new SJNoAliasVariablesExt_c();
	}
	
	public SJNoAliasExprExt SJNoAliasExprExt(boolean isNoAlias, boolean isExpr)
	{
		return new SJNoAliasExprExt_c(isNoAlias, isExpr);
	}
	
	public SJTypeableExt SJTypeableExt(SJSessionType st)
	{
		return new SJTypeableExt_c(st);
	}	
	
	public SJNamedExt SJNamedExt(SJSessionType st, String sjname)
	{
		return new SJNamedExt_c(st, sjname);
	}
	
	public SJSessionOperationExt SJSessionOperationExt(SJSessionType st, List<String> sjnames)
	{
		return new SJSessionOperationExt_c(st, sjnames);
	}		
}
