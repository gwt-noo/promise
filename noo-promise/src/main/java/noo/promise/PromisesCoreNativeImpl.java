package noo.promise;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


/**
 * @author Tal Shani
 */
class PromisesCoreNativeImpl extends PromisesCore {
    @Override
    <T> Promise<T> create(PromiseResolver<T> resolver) {
        return PromiseNative.createPromise(resolver);
    }

    @Override
    String getImplementationName() {
        return "native";
    }

    @Override
    native <T> Promise<T> resolve(T value) /*-{
        return Promise.resolve(value);
    }-*/;

    @Override
    Promise<ResolvedCollection> all(Object... promises) {
        ArrayOfPromises ar = JsArray.createArray().cast();
        for (Object promise : promises) {
            ar.push(promise);
        }
        return allInternal(ar);
    }

    @Override
    <T> Promise<T> race(Promise<? extends T>... promises) {
        ArrayOfPromises ar = JsArray.createArray().cast();
        for (Object promise : promises) {
            ar.push(promise);
        }
        return raceInternal(ar);
    }

    native <T> Promise<T> raceInternal(ArrayOfPromises promises) /*-{
        return Promise.race(promises);
    }-*/;

    @Override
    void setUncaughtExceptionHandler(GWT.UncaughtExceptionHandler uncaughtExceptionHandler) {
        // no exception handling
    }

    @Override
    GWT.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        // no exception handling
        return null;
    }

    native Promise<ResolvedCollection> allInternal(ArrayOfPromises promises) /*-{
        return Promise.all(promises);
    }-*/;

    private static final class ArrayOfPromises extends JavaScriptObject {

        protected ArrayOfPromises() {
        }

        public native void push(Object o) /*-{
            this.push(o);
        }-*/;
    }
}
