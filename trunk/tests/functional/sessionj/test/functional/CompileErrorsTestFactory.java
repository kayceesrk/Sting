package sessionj.test.functional;

import org.testng.annotations.Factory;

import java.io.File;
import java.util.Collection;

public class CompileErrorsTestFactory {
    @Factory
    public Object[] createInstances() {
        Collection<File> sjFiles = TestUtils.findSJSourceFiles("compilationerror/");
        Object[] result = new CompilationError[sjFiles.size()];
        int i = 0;
        for (File sjFile : sjFiles) {
            result[i] = new CompilationError(sjFile);
            i++;
        }
        return result;
    }
}
