package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
final class PromiseNative<T> extends JavaScriptObject implements Promise<T> {
    protected PromiseNative() {
    }

    public final native Promise<T> nativeThen(PromiseHandler<? super T> onFulfilled, PromiseHandler<Throwable> onRejected) /*-{
        var onFulfilledFn, onRejectedFn;
        if (onFulfilled) {
            onFulfilledFn = $entry(function (value) {
                onFulfilled.@noo.promise.PromiseHandler::handle(Ljava/lang/Object;)(value);
            });
        }
        if (onRejected) {
            onRejectedFn = $entry(function (value) {
                onRejected.@noo.promise.PromiseHandler::handle(Ljava/lang/Object;)(value);
            });
        }
        return this.then(onFulfilledFn, onRejectedFn);
    }-*/;

    public final native <R> Promise<R> nativeThen(PromiseTransformingHandler<R, T> onFulfilled, PromiseTransformingHandler<R, Throwable> onRejected) /*-{
        var onFulfilledFn, onRejectedFn;
        if (onFulfilled) {
            onFulfilledFn = $entry(function (value) {
                return onFulfilled.@noo.promise.PromiseTransformingHandler::handle(Ljava/lang/Object;)(value);
            });
        }
        if (onRejected) {
            onRejectedFn = $entry(function (value) {
                return onRejected.@noo.promise.PromiseTransformingHandler::handle(Ljava/lang/Object;)(value);
            });
        }
        return this.then(onFulfilledFn, onRejectedFn);
    }-*/;

    public static native <T> Promise<T> createPromise(final PromiseResolver<T> resolver) /*-{
        return new Promise($entry(function (resolve, reject) {
            @noo.promise.PromiseNative::resolve(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lnoo/promise/PromiseResolver;)(resolve, reject, resolver);
        }));
    }-*/;

    public static <T> void resolve(JavaScriptObject resolve, JavaScriptObject reject, PromiseResolver<T> resolver) {
        resolver.resolve(new NativeCallback<T>(resolve, reject));
    }

    @Override
    public final Promise<T> then(PromiseHandler<? super T> onFulfilled) {
        return nativeThen(onFulfilled, null);
    }

    @Override
    public Promise<T> then(PromiseHandler<? super T> onFulfilled, PromiseHandler<Throwable> onRejected) {
        return nativeThen(onFulfilled, onRejected);
    }

    @Override
    public final Promise<T> catchIt(PromiseHandler<Throwable> onRejected) {
        return nativeThen(null, onRejected);
    }

    @Override
    public final <R> Promise<R> then(PromiseTransformingHandler<R, T> onFulfilled) {
        return nativeThen(onFulfilled, null);
    }

    @Override
    public final Promise<T> catchIt(PromiseTransformingHandler<T, Throwable> onRejected) {
        return nativeThen(null, onRejected);
    }

    private static class NativeCallback<T> implements PromiseCallback<T> {

        private final JavaScriptObject resolveFn;
        private final JavaScriptObject rejectFn;

        private NativeCallback(JavaScriptObject resolveFn, JavaScriptObject rejectFn) {
            this.resolveFn = resolveFn;
            this.rejectFn = rejectFn;
        }

        private native void nativeResolve(Object o) /*-{
            var fn = this.@noo.promise.PromiseNative.NativeCallback::resolveFn;
            if (fn) fn(o);
        }-*/;

        private native void nativeReject(Object o) /*-{
            var fn = this.@noo.promise.PromiseNative.NativeCallback::rejectFn;
            if (fn) fn(o);
        }-*/;

        @Override
        public void resolveValue(T value) {
            nativeResolve(value);
        }

        @Override
        public void resolvePromise(Promise<T> value) {
            nativeResolve(value);
        }

        @Override
        public void reject(Throwable reason) {
            nativeReject(reason);
        }
    }
}
