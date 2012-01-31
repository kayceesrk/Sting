//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.ast.*;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import sessionj.ast.SJNodeFactory;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.typeobjects.*;
import sessionj.util.SJCompilerUtils;
import sessionj.visit.SJGProtocolDeclTypeBuilder;

abstract public class SJGProtocolNode_c extends SJTypeNode_c implements SJGProtocolNode
{
	private Receiver target;

	public SJGProtocolNode_c(Position pos, Receiver target)
	{
		super(pos);

		this.target = target;
	}

	public Receiver target()
	{
		return target;
	}

	public SJGProtocolNode_c target(Receiver target)
	{
		this.target = target;

		return this;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) throws SemanticException {

			SJTypeSystem ts = (SJTypeSystem) cv.typeSystem();
			SJNodeFactory nf = (SJNodeFactory) cv.nodeFactory();

            Receiver target = (Receiver) SJCompilerUtils.buildAndCheckTypes(cv, target());

            SJSessionType st;

            if (target instanceof Field)
			{
				Field f = (Field) target;

				SJFieldInstance fi = (SJFieldInstance) sjts.findField((ReferenceType) f.target().type(), f.name(), cv.context().currentClass());

				if (fi != null)
				{
					if (!(fi instanceof SJFieldGProtocolInstance)) // Similar mutually recursive lookahead routine to that of SJNoAliasTypeBuilder/SJNoAliasProcedureChecker. Also similar in that we build ahead, but cannot store the result, so have to do again later.
					{
						// FIXME: this assumes the target class must have been visited (compiled up to this stage) before the current class. But this can break for mutually dependent classes.

						//SJProtocolDeclTypeBuilder pdtb = (SJProtocolDeclTypeBuilder) new SJProtocolDeclTypeBuilder(job, ts, nf).begin(); // Doesn't seem to be enough.
						ContextVisitor pdtb = new SJGProtocolDeclTypeBuilder(cv.job(), ts, nf).context(cv.context()); // Seems to work, but does it make sense?

						SJParsedClassType pct = (SJParsedClassType) ts.typeForName(((Field) target).target().toString());
						ClassDecl cd = SJCompilerUtils.findClassDecl((SourceFile) cv.job().ast(), pct.name()); // Need to qualify type name?

						if (cd == null)
						{
							throw new SemanticException("[SJCompilerUtils.disambiguateSJTypeNode] Compiling " + ((SourceFile) cv.job().ast()).source().name() + ", class declaration not found: " + pct.name());
						}

						cd = (ClassDecl) cd.visit(pdtb); // FIXME: will cycle for mutually recursive fields.

						fi = (SJFieldInstance) sjts.findField((SJParsedClassType) cd.type(), f.name(), pdtb.context().currentClass());
					}

					st = ((SJTypeableInstance) fi).sessionType();
				}
				else // If trying to access a protocol field, target class must have been compiled using sessionjc. // FIXME: or
				{
					throw new RuntimeException("[SJCompilerUtils.disambiguateSJTypeNode] Shouldn't get in here.");
				}
			}
			else if (target instanceof Local)
			{
				String protocol = ((NamedVariable) target).name();

				st = ((SJTypeableInstance) cv.context().findLocal(protocol)).sessionType(); // No clone, immutable.
			}
			else //if (target instanceof ArrayAccess)
			{
				// FIXME: if the target class is only mention in the protocol reference, but not actually used otherwise, the above target disambiguation routine will fail. Similar for any message classes (e.g. Address) referred to in protocols, but not otherwise used.

				throw new SemanticException("[SJCompilerUtils.disambiguateSJTypeNode] GProtocol reference not yet supported for: " + target);
			}

			if (this instanceof SJGProtocolDualNode)
			{
				/*if (st instanceof SJSBeginType) // Only necessary to check this for session casts (done in SJSessionOperationTypeBuilder) and method parameters (done in SJMethodTypebuilder).
				{
					throw new SemanticException("[SJCompilerUtils.disambiguateSJTypeNode] Protocol reference to channel types not yet supported: " + target);
				}*/

				st = SJCompilerUtils.dualSessionType(st);
			}
			/*else
			{
				if (st instanceof SJCBeginType) // It's too late to check this here - base type checking will have failed (for otherwise correct programs) since the protocol reference parameter would be converted to type SJSocket.
				{
					throw new SemanticException("[SJCompilerUtils.disambiguateSJTypeNode] Protocol reference to channel types not yet supported: " + target);
				}
			}*/

			st = st.getCanonicalForm();
			
			return target(target).type(st);
    }
}
//</By MQ>