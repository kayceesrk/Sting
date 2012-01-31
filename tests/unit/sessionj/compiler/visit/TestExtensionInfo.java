package sessionj.compiler.visit;

import sessionj.ExtensionInfo;
import polyglot.frontend.*;
import polyglot.frontend.goals.Goal;

public class TestExtensionInfo extends ExtensionInfo {
    @Override
    public Scheduler createScheduler() {
        return new SJScheduler(this) {
            @Override
            public Goal currentGoal() {
                return new DummyGoal();
            }
        };
    }

    {
        initCompiler(new polyglot.frontend.Compiler(this));
    }
}
