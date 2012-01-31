package sessionj.runtime.util;

import java.text.MessageFormat;
import java.text.Format;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class MessageFormatFormatter extends Formatter {
	private static final Format messageFormat = new MessageFormat("[{0}|{5}|{2}|{3,date,HH:mm:ss}] {1}: {4}\n{6}");
	
	public String format(LogRecord record) {
		Object[] arguments = new Object[7];
		arguments[0] = record.getLoggerName();
		arguments[1] = record.getLevel();
		arguments[2] = Thread.currentThread().getName();
		arguments[3] = new Date(record.getMillis());
		arguments[4] = record.getMessage();
        arguments[5] = record.getSourceMethodName();
        Throwable t = record.getThrown();
        if (t != null) {
            StringWriter writer = new StringWriter(256);
            t.printStackTrace(new PrintWriter(writer));
            arguments[6] = writer.toString();
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        } else {
            arguments[6] = "";
        }
		return messageFormat.format(arguments);
	}	
 
}

