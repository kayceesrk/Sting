package sessionj.compiler.visit;

import polyglot.frontend.goals.Goal;
import polyglot.frontend.*;

import java.util.Collection;
import java.util.Collections;

class DummyGoal implements Goal {
    public boolean conflictsWith(Goal goal) {
        return false;
    }

    public Pass createPass(polyglot.frontend.ExtensionInfo extInfo) {
        return null;
    }

    public Collection corequisiteGoals(Scheduler scheduler) {
        return Collections.emptyList();
    }

    public Collection prerequisiteGoals(Scheduler scheduler) {
        return Collections.emptyList();
    }

    public void addCorequisiteGoal(Goal g, Scheduler scheduler) {
    }

    public void addPrerequisiteGoal(Goal g, Scheduler scheduler) throws CyclicDependencyException {
    }

    public boolean isReachable() {
        return false;
    }

    public void setUnreachable() {
    }

    public void setUnreachableThisRun() {
    }

    public void setState(int state) {
    }

    public int state() {
        return 0;
    }

    public boolean hasBeenReached() {
        return false;
    }

    public Job job() {
        return null;
    }

    public String name() {
        return null;
    }
}
