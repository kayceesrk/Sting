package sessionj.types;

import polyglot.frontend.Source;
import polyglot.types.*;
import sessionj.types.noalias.*;
import sessionj.types.typeobjects.*;
import sessionj.types.sesstypes.*;
import sessionj.util.SJLabel;

import java.util.List;
import polyglot.ast.Id; //<By MQ>

public interface SJTypeSystem extends TypeSystem 
{
	SJCBeginType SJCBeginType();
        SJSBeginType SJSBeginType(String target); //<By MQ> Added target
        SJSendType SJSendType(String target, Type messageType) throws SemanticException; //<By MQ>
        SJReceiveType SJReceiveType(String target, Type messageType) throws SemanticException; //<By MQ>
	SJOutbranchType SJOutbranchType(); // FIXME: finish refactoring these branch types.
	SJOutbranchType SJOutbranchType(boolean isDependentlyTyped);
        SJInbranchType SJInbranchType(String target);  //<By MQ> Added target
        SJInbranchType SJInbranchType(String target, boolean isDependentlyTyped);//<By MQ> Added target
	SJOutwhileType SJOutwhileType();
        SJSessionType SJOutwhileType(SJSessionType body);
        SJInwhileType SJInwhileType(String target); //<By MQ>
        SJInwhileType SJInwhileType(SJSessionType body, String target); //<By MQ>
	SJRecursionType SJRecursionType(SJLabel lab);
	SJRecurseType SJRecurseType(SJLabel lab);
	SJUnknownType SJUnknownType();
	SJDelegatedType SJDelegatedType(SJSessionType st);
    //SJSetType SJSetType(List<SJSessionType_c> members);
	SJSetType SJSetType(List<SJSessionType> members);
	
	SJParsedClassType SJParsedClassType(LazyClassInitializer init, Source fromSource);
	SJFieldInstance SJFieldInstance(FieldInstance fi, boolean isNoAlias, boolean isFinal);
	SJConstructorInstance SJConstructorInstance(ConstructorInstance ci);
	SJMethodInstance SJMethodInstance(MethodInstance mi);
	SJLocalInstance SJLocalInstance(LocalInstance li, boolean isNoAlias, boolean isFinal);
		
	SJFieldProtocolInstance SJFieldProtocolInstance(SJFieldInstance fi, SJSessionType st, String sjname);
	SJLocalProtocolInstance SJLocalProtocolInstance(SJLocalInstance li, SJSessionType st, String sjname);
	SJLocalChannelInstance SJLocalChannelInstance(SJLocalInstance ci, SJSessionType st, String sjname);
	SJLocalSocketInstance SJLocalSocketInstance(SJLocalInstance si, SJSessionType st, String sjname);
	SJLocalServerInstance SJLocalServerInstance(SJLocalInstance ci, SJSessionType st, String sjname);
	SJLocalSelectorInstance SJLocalSelectorInstance(SJLocalInstance ci, SJSessionType st, String sjname);
	
	SJNoAliasReferenceType SJNoAliasReferenceType(ReferenceType rt);
	SJNoAliasReferenceType SJNoAliasFinalReferenceType(ReferenceType rt, boolean isFinal);
	
	boolean wellFormedRecursions(SJSessionType st);
    //<By MQ> My added definitions
        SJFieldGProtocolInstance SJFieldGProtocolInstance(SJFieldInstance fi, SJSessionType st, String sjname);
	SJLocalGProtocolInstance SJLocalGProtocolInstance(SJLocalInstance li, SJSessionType st, String sjname);
        SJParticipantsType SJParticipantsType();
	SJGMsgType SJGMsgType(Type messageType) throws SemanticException;
	SJGBranchType SJGBranchType();
	SJGBranchType SJGBranchType(boolean isDependentlyTyped);
	SJGLoopType SJGLoopType();
        SJGLoopType SJGLoopType(SJSessionType body);
	SJGBeginType SJGBeginType();
    //</By MQ>
}
