package sessionj;

import polyglot.ast.NodeFactory;
import polyglot.frontend.*;
import polyglot.frontend.goals.*;
import polyglot.lex.Lexer;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.visit.NodeVisitor;
import sessionj.ast.SJNodeFactory_c;
import sessionj.parse.Grm;
import sessionj.parse.Lexer_c;
import sessionj.types.SJTypeSystem_c;
import sessionj.visit.*;
import sessionj.visit.noalias.SJNoAliasExprBuilder;
import sessionj.visit.noalias.SJNoAliasTranslator;
import sessionj.visit.noalias.SJNoAliasTypeBuilder;
import sessionj.visit.noalias.SJNoAliasTypeChecker;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Extension information for sessionj extension.
 */
public class ExtensionInfo extends polyglot.frontend.JLExtensionInfo {
	static {
	    // force Topics to load
	    new Topics();
	}
	
	public String defaultFileExtension() {
	    return "sj";
	}
	
	public String compilerName() {
	    return "sessionjc";
	}
	
	public Parser parser(Reader reader, FileSource source, ErrorQueue eq) {
	    Lexer lexer = new Lexer_c(reader, source, eq);
	    Grm grm = new Grm(lexer, ts, nf, eq);
	    return new CupParser(grm, source, eq);
	}
	
	protected NodeFactory createNodeFactory() {
	    return new SJNodeFactory_c();
	}
	
	protected TypeSystem createTypeSystem() {
	    return new SJTypeSystem_c();
	}
	
	// Must override this method to specify the new compiler pass schedule.
	public Scheduler createScheduler()
	{
		return new SJScheduler(this);
	}
	
	public static class SJScheduler extends JLScheduler
	{
		public SJScheduler(ExtensionInfo extInfo)
		{
			super(extInfo);
		}

		// TypesInitialized phase (post Parsed, pre TypeChecked).
		/*public Goal SJThreadPreParsing(final Job job)
		{
			TypeSystem ts = job.extensionInfo().typeSystem();
			NodeFactory nf = job.extensionInfo().nodeFactory();
			Goal g = internGoal(new VisitorGoal(job, new SJThreadPreParser(job, ts, nf))
			{
				public Collection prerequisiteGoals(Scheduler scheduler)
				{
					List l = new ArrayList();
					
					l.add(scheduler.Parsed(job)); // Copied from inside polyglot.frontend.goals.TypesInitialized (i.e. the next pass).
					
					return l;
				}
			});
			return g;
		}

    public Goal TypesInitialized(Job job)
    {
			TypeSystem ts = extInfo.typeSystem();
			NodeFactory nf = extInfo.nodeFactory();
			Goal g = internGoal(new VisitorGoal(job, new TypeBuilder(job, ts, nf))
			{
				public Collection prerequisiteGoals(Scheduler scheduler)
				{
					List l = new ArrayList();
					
					l.addAll(super.prerequisiteGoals(scheduler));
					l.add(SJThreadPreParsing(job));
					
					return l;
	    	}
			});
			return g;
    }*/
		
		// ReachabilityChecked phase (post TypeChecked).
		public Goal SJCreateOperationParsing(Job job)
		{
            return createGoal(job, SJCreateOperationParser.class, TypeChecked(job), ConstantsChecked(job));
		}

        private Goal createGoal(final Job job, Class<? extends NodeVisitor> visitorClass, final Goal... prereq) {
            TypeSystem ts = job.extensionInfo().typeSystem();
            NodeFactory nf = job.extensionInfo().nodeFactory();

            return internGoal(new VisitorGoal(job, createInstance(job, visitorClass, ts, nf))
                {
                    public Collection prerequisiteGoals(Scheduler scheduler)
                    {
                        return Arrays.asList(prereq);
                    }
                }
            );
        }

        private NodeVisitor createInstance(Job job, Class<? extends NodeVisitor> visitorClass, TypeSystem ts, NodeFactory nf) {
            try {
                return visitorClass.getConstructor(Job.class, TypeSystem.class, NodeFactory.class).newInstance(job, ts, nf);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /*public Goal PolyglotToSJBarrier(final Job job)
          {
              Goal g = internGoal(new Barrier(this)
                  {
                      public Goal goalForJob(Job j) // Brings all jobs (compilation units, i.e. source files) up this barrier (i.e. the first SJ noalias type building pass is completed) before proceeding with the subsequent passes.
                      {
                          return ((SJScheduler) scheduler).SJCreateOperationParsing(j);
                      }
                  }
              );

              return g;
          }*/
		
	        public Goal SJVariableParsing(Job job)
		{
			return createGoal(job, SJVariableParser.class, SJCreateOperationParsing(job));
		}
	
	        public Goal SJSessionTryDisambiguation(Job job)
		{
			return createGoal(job, SJSessionTryDisambiguator.class, SJVariableParsing(job));
		}	
		
		public Goal SJThreadParsing(Job job)
		{
			return createGoal(job, SJThreadParser.class, SJSessionTryDisambiguation(job));
		}
		
		public Goal SJThreadParsingBarrier() 
		{
            return internGoal(new Barrier(SJScheduler.this)
                {
                    public Goal goalForJob(Job j)
                    {
                        return ((SJScheduler) scheduler).SJThreadParsing(j);
                    }
                }
            );
		}
		
		public Goal SJChannelOperationParsing(Job job)
		{
			return createGoal(job, SJChannelOperationParser.class, SJThreadParsingBarrier());
		}	
		
		public Goal SJServerOperationParsing(Job job)
		{
			return createGoal(job, SJServerOperationParser.class, SJChannelOperationParsing(job));
		}	

		public Goal SJSelectorOperationParsing(Job job)
		{
			return createGoal(job, SJSelectorOperationParser.class, SJServerOperationParsing(job));
		}			
		
		public Goal SJSessionOperationParsing(Job job)
		{
			return createGoal(job, SJSessionOperationParser.class, SJSelectorOperationParsing(job));
		}		
		
		public Goal SJNoAliasTypeBuilding(Job job)
		{
			return createGoal(job, SJNoAliasTypeBuilder.class, SJSessionOperationParsing(job));
		}
	
		public Goal SJNoAliasTypeBuildingBarrier()
		{

            return internGoal(new Barrier(SJScheduler.this)
                {
                    public Goal goalForJob(Job j) // Brings all jobs (compilation units, i.e. source files) up this barrier (i.e. the first SJ noalias type building pass is completed) before proceeding with the subsequent passes.
                    {
                        return ((SJScheduler) scheduler).SJNoAliasTypeBuilding(j);
                    }
                }
            );
		}

		public Goal SJNoAliasExprBuilding(Job job)
		{
			return createGoal(job, SJNoAliasExprBuilder.class, SJNoAliasTypeBuildingBarrier());
		}

		public Goal SJProtocolDeclTypeBuilding(Job job)
		{
			return createGoal(job, SJProtocolDeclTypeBuilder.class, SJNoAliasExprBuilding(job));
		}
		
		public Goal SJProtocolDeclTypeBuildingBarrier()
		{

            return internGoal(new Barrier(SJScheduler.this)
                {
                    public Goal goalForJob(Job j) // Brings all jobs (compilation units, i.e. source files) up this barrier (i.e. the first SJ noalias type building pass is completed) before proceeding with the subsequent passes.
                    {
                        return ((SJScheduler) scheduler).SJProtocolDeclTypeBuilding(j);
                    }
                }
            );
		}		
	
		public Goal SJSessionMethodTypeBuilding(Job job)
		{
			return createGoal(job, SJSessionMethodTypeBuilder.class, SJProtocolDeclTypeBuildingBarrier());
		}
		
		public Goal SJChannelDeclTypeBuilding(Job job)
		{
			return createGoal(job, SJChannelDeclTypeBuilder.class, SJSessionMethodTypeBuilding(job));
		}		
		
		public Goal SJServerDeclTypeBuilding(Job job)
		{
			return createGoal(job, SJServerDeclTypeBuilder.class, SJChannelDeclTypeBuilding(job));
		}				

		public Goal SJSelectorDeclTypeBuilding(Job job)
		{
			return createGoal(job, SJSelectorDeclTypeBuilder.class, SJServerDeclTypeBuilding(job));
		}
		
		/*public Goal SJChannelDeclTypeBuildingBarrier(final Job job) // Not needed because channels (and servers) can only be locals.
		{
			Goal g = internGoal(new Barrier(this)
				{
					public Goal goalForJob(Job j) // Brings all jobs (compilation units, i.e. source files) up this barrier (i.e. the first SJ noalias type building pass is completed) before proceeding with the subsequent passes. 
					{
						return ((SJScheduler) scheduler).SJChannelDeclTypeBuilding(j);
					}
				}
			);
	
			return g;
		}*/
	
		public Goal SJSocketDeclTypeBuilding(Job job)
		{
            // SJChannelDeclTypeBuildingBarrier(job) // Barrier not needed, dealing with locals only.
			return createGoal(job, SJSocketDeclTypeBuilder.class, SJSelectorDeclTypeBuilding(job));
		}		
		
		public Goal SJSessionOperationTypeBuilding(Job job)
		{
			return createGoal(job, SJSessionOperationTypeBuilder.class, SJSocketDeclTypeBuilding(job));
		}	
		
		public Goal SJTypeBuildingBarrier() // Maybe not needed?
		{
            return internGoal(new Barrier(SJScheduler.this)
                {
                    public Goal goalForJob(Job j) // Brings all jobs (compilation units, i.e. source files) up this barrier (i.e. the first SJ noalias type building pass is completed) before proceeding with the subsequent passes.
                    {
                        return ((SJScheduler) scheduler).SJSessionOperationTypeBuilding(j);
                    }
                }
            );
		}
		
		public Goal SJNoAliasTypeChecking(Job job)
		{
			return createGoal(job, SJNoAliasTypeChecker.class, SJTypeBuildingBarrier());
		}
			
		// Maybe put a barrier between these two, so that session type checking can really count on linearity.
		
		public Goal SJSessionTypeChecking(Job job) // Doing this after noalias type checking means session linearity is already checked.
		{
			return createGoal(job, SJSessionTypeChecker.class, SJNoAliasTypeChecking(job));
		}
		
		public Goal ReachabilityChecked(final Job job)
		{
			TypeSystem ts = extInfo.typeSystem();
			NodeFactory nf = extInfo.nodeFactory();

            return internGoal(new ReachabilityChecked(job, ts, nf)
                {
                    public Collection prerequisiteGoals(Scheduler scheduler)
                    {
                        List l = new ArrayList();

                        l.addAll(super.prerequisiteGoals(scheduler));
                        l.add(SJSessionTypeChecking(job));

                        return l;
                    }
                }
            );
		}
		// End of ReachabilityChecked phase.
	
		// Start of Serialized phase.
		public Goal SJSessionVisiting(Job job) // All session type has been information built, checked and recorded.
		{
			return createGoal(job, SJSessionVisitor.class,
                    TypeChecked(job), ConstantsChecked(job), ReachabilityChecked(job),
                    ExceptionsChecked(job), ExitPathsChecked(job), InitializationsChecked(job),
                    ConstructorCallsChecked(job), ForwardReferencesChecked(job));
		}		
		
		public Goal SJSendTranslation(Job job) // Needs to come before noalias translation.
		{
		    return createGoal(job, SJSendTranslator.class, SJSessionVisiting(job));
		}
		
		/*public Goal SJSessionVisiting2(final Job job) // All session type has been information built, checked and recorded.
		{
			TypeSystem ts = job.extensionInfo().typeSystem();
			NodeFactory nf = job.extensionInfo().nodeFactory();
	
			Goal g = internGoal(new VisitorGoal(job, new SJSessionVisitor(job,
			    ts, nf))
				{
					public Collection prerequisiteGoals(Scheduler scheduler)
					{
						List l = new ArrayList();
		
						// l.addAll(super.prerequisiteGoals(scheduler));
						l.add(SJSendTranslation(job));	
		
						return l;
					}
				}
			);
	
			return g;
		}*/
		
		public Goal SJNoAliasTranslation(Job job)
		{
			return createGoal(job, SJNoAliasTranslator.class, SJSendTranslation(job));
		}
	
		public Goal SJProtocolDeclTranslation(Job job) // Doing this after noalias type checking means session linearity is already checked.
		{
			return createGoal(job, SJProtocolDeclTranslator.class, SJNoAliasTranslation(job));
		}
		
		public Goal SJSessionTryTranslation(Job job) // Doing this after noalias type checking means session linearity is already checked.
		{
			return createGoal(job, SJSessionTryTranslator.class, SJProtocolDeclTranslation(job));
		}		

		public Goal SJHigherOrderTranslation(Job job) // Doing this after noalias type checking means session linearity is already checked.
		{
			return createGoal(job, SJHigherOrderTranslator.class, SJSessionTryTranslation(job));
		}
		
		public Goal SJUnicastOptimization(Job job) {
      // Currently need to do this before compound operation translation, because the latter
      // destroys the inbranch node and we don't have explicit nodes for the compound socket operations.
      return createGoal(job, SJUnicastOptimiser.class, SJHigherOrderTranslation(job));
		}		
		
		/*public Goal SJRecursiveSessionCallTranslation(Job job)
		{
			return createGoal(job, SJRecursiveSessionCallTranslator.class, SJUnicastOptimization(job));
		}*/
		
		// All SJSessionVisitor passes must come before this one: this translator destroys e.g. SJInbranch, so "automatic" session context management by the SJSessionVisitor framework won't work anymore.
    	    //<By MQ>
	    public Goal SJBatchingWithDataFlowAnalyzer(Job job) // Needs to come before noalias translation.
	    {
		return createGoal(job, SJBatchingWithDataFlow.class, SJUnicastOptimization(job));
	    }
	    //</By MQ>

		public Goal SJCompoundOperationTranslation(Job job)
		{
      //return createGoal(job, SJCompoundOperationTranslator.class, SJRecursiveSessionCallTranslation(job));
			return createGoal(job, SJCompoundOperationTranslator.class, SJBatchingWithDataFlowAnalyzer(job));
		}


    public Goal Serialized(final Job job)
		{
			/*TypeSystem ts = extInfo.typeSystem();
			NodeFactory nf = extInfo.nodeFactory();
			ErrorQueue eq = extInfo.compiler().errorQueue();
			polyglot.main.Version v = extInfo.version();*/
	
			//Goal g = internGoal(new VisitorGoal(job, new ClassSerializer(ts, nf, job.source().lastModified(), eq, v))

            return internGoal(new Serialized(job)
                {
                    public Collection prerequisiteGoals(Scheduler scheduler)
                    {
                        List l = new ArrayList();

                        l.addAll(super.prerequisiteGoals(scheduler));
                        //l.add(SJBatchingWithDataFlowAnalyzer(job));
                        l.add(SJCompoundOperationTranslation(job));
                        //l.add(SJRecursiveSessionCallTranslation(job));

                        return l;
                    }
                }
            );
		}
		// End of Serialized phase.			
	}
}
