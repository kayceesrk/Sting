package sessionj.runtime.util;

import java.util.concurrent.CountDownLatch;

/**
 * A latch used by a group of threads competing to publish a value.
 *
 * From Java Concurrency in Practice, page 187 (listing 8.17)
 *
 * @param <T> The type of the value that the latch holds.
 */
// ThreadSafe
public class ValueLatch<T> {
    // GuardedBy("lock")
    private T value = null;
    private final Object lock = new Object();
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return done.getCount() == 0;
    }

    /**
     * Sets the value if it wasn't already set.
     * @param val The proposed value to be recorded.
     */
    public void submitValue(T val) {
        synchronized (lock) {
            if (!isSet()) {
                value = val;
                done.countDown();
            }
        }
    }

    /**
     * Waits for the value to be set, and then returns it.
     * @return The value submitted by another thread
     * @throws InterruptedException If the current thread is interrupted while waiting
     */
    public T awaitValue() throws InterruptedException {
        done.await();
        synchronized (lock) {
            return value;
        }
    }
}

