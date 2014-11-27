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

    public static <T> PromiseOrValue<T> promiseAsValue(Promise<T> promise) {
        return PromiseOrValue.promise(promise);
    }

    public static <T> PromiseOrValue<T> value(Promise<T> promise) {
        return PromiseOrValue.promise(promise);
    }
}
