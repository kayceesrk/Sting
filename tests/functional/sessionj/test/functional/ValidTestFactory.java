package sessionj.test.functional;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Collection;

public class ValidTestFactory {
    private final File classesDir;
    private final ClassLoader loader;

    public ValidTestFactory() throws MalformedURLException {
        classesDir = new File(System.getProperty(TestConstants.TEMP_BUILD_DIR));
        loader = new URLClassLoader(new URL[] {classesDir.toURI().toURL()});
    }

    @Factory
    public Object[] createValidTests() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Collection<File> sjFiles = TestUtils.findSJSourceFiles("valid/");
        Object[] result = new Object[sjFiles.size()];
        int i = 0;
        for (File sjFile : sjFiles) {
            result[i] = new CompileWrapper(sjFile);
            i++;
        }

        return result;
    }

    public class CompileWrapper {
        private final File sjFile;
        private BaseValidTest wrapped;

        CompileWrapper(File sjFile) {
            this.sjFile = sjFile;
        }
        @Test
        public void run() throws Exception {
            System.out.println("COMPILING: " + sjFile);
            assert TestUtils.runCompiler(sjFile, classesDir, System.out, System.err) == 0;
            wrapped = (BaseValidTest) loadCompiledClass(sjFile).newInstance();
            System.out.println("RUNNING: " + sjFile);
            wrapped.runTestcase();
        }
    }

    private Class<?> loadCompiledClass(File sjFile) throws ClassNotFoundException {
        String className = sjFile.getName();
        className = className.substring(0, className.indexOf('.'));
        return loader.loadClass
                (TestConstants.TEST_BASE_PACKAGE + '.' + className);
    }
}
