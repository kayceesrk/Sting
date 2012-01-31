package sessionj.extension;

import java.util.*;

import polyglot.ast.ExtFactory;

import sessionj.extension.noalias.*;
import sessionj.extension.sessops.*;
import sessionj.extension.sesstypes.*;
import sessionj.types.sesstypes.SJSessionType;

public interface SJExtFactory extends ExtFactory
{
	public SJNoAliasExt SJNoAliasExt(boolean isNoAlias);
	public SJNoAliasFinalExt SJNoAliasFinalExt(boolean isNoAlias, boolean isFinal);
	public SJNoAliasVariablesExt SJNoAliasVariablesExt();
	public SJNoAliasExprExt SJNoAliasExprExt(boolean isNoAlias, boolean isFinal);
	
	public SJTypeableExt SJTypeableExt(SJSessionType st);
	public SJNamedExt SJNamedExt(SJSessionType st, String sjname);
	public SJSessionOperationExt SJSessionOperationExt(SJSessionType st, List<String> sjnames);
}
