package sessionj.test.functional;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

/**
 *
 */
public class CompilationError {
    private final File sjFile;
    private final File tempDir;

    CompilationError(File sjFile) {
        this.sjFile = sjFile;
        tempDir = new File(System.getProperty(TestConstants.TEMP_BUILD_DIR));
    }

    @Test
    public void run() {
        PrintStream out = null;
        PrintStream err = null;
        try {
            out = new PrintStream(new ByteArrayOutputStream());
            err = new PrintStream(new ByteArrayOutputStream());
            int exitCode = TestUtils.runCompiler(sjFile, tempDir, out, err);
            assert exitCode == 1;
            //assert tempDir.list().length == 0;
            // TODO: check error message
        } finally {
            if (out != null) out.close();
            if (err != null) err.close();
        }
    }

}
