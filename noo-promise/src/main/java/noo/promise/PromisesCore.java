package noo.promise;

import com.google.gwt.core.client.GWT;

/**
 * @author Tal Shani
 */
abstract class PromisesCore {
    abstract <T> Promise<T> create(PromiseResolver<T> resolver);

    abstract String getImplementationName();

    abstract <T> Promise<T> resolve(T value);

    abstract Promise<ResolvedCollection> all(Object... promises);

    abstract <T> Promise<T> race(Promise<? extends T>... promises);


    abstract void setUncaughtExceptionHandler(GWT.UncaughtExceptionHandler uncaughtExceptionHandler);

    abstract GWT.UncaughtExceptionHandler getUncaughtExceptionHandler();

}
