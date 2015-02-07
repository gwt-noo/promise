package noo.promise;

import com.google.gwt.core.client.GWT;

/**
 * @author Tal Shani
 */
public class Promises {
    private final static PromisesCore IMPL;

    static {
        if (GWT.isScript()) {
            IMPL = GWT.create(PromisesCore.class);
        } else {
            IMPL = new PromisesCoreEmulatedImpl();
        }
    }

    public static <T> Promise<T> create(PromiseResolver<T> resolver) {
        return IMPL.create(resolver);
    }

    public static String getImplementationName() {
        return IMPL.getImplementationName();
    }

    public static <T> Promise<T> resolve(final T value) {
        return IMPL.resolve(value);
    }

    public static Promise<ResolvedCollection> all(Object... promises) {
        return IMPL.all(promises);
    }

    public static <T> Promise<T> race(Promise<? extends T>... promises) {
        return IMPL.race(promises);
    }

    /**
     * Used by the JVM promise emulation to call all immediate(s) that were set
     */
    public static void flush() {
        Immediate.flush();
    }

    public static void setUncaughtExceptionHandler(GWT.UncaughtExceptionHandler exceptionHandler) {
        IMPL.setUncaughtExceptionHandler(exceptionHandler);
    }

    public static GWT.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return IMPL.getUncaughtExceptionHandler();
    }
}
