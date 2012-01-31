package sessionj.runtime.util;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private String name;
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, name);
    }

    public void setName(String s) {
        name = s;
    }
}
