package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
public class PromiseNative<T> extends JavaScriptObject implements Promise<T> {
    protected PromiseNative() {}


    public final native <R> Promise<R> nativeThen(HandlerWrapper<T> handler) /*-{
      var successFn = function(value) {
        return handler.@noo.promise.PromiseNative.HandlerWrapper::onFulfilled(Ljava/lang/Object;)(value);
      };
      var rejectFn = function(exception) {
        return handler.@noo.promise.PromiseNative.HandlerWrapper::onRejected(Ljava/lang/Exception;)(exception);
      };
      return this.then($entry(successFn), $entry(rejectFn));
    }-*/;

    @Override
    public final <R> Promise<R> then(PromiseHandler<R, T> handler) {
        return nativeThen(new HandlerWrapper<T>(handler));
    }

    public static native <T> Promise<T> createPromise(final PromiseResolver<T> resolver) /*-{
      return new Promise($entry(function (resolve, reject) {
        @noo.promise.PromiseNative::resolve(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lnoo/promise/PromiseResolver;)(resolve, reject, resolver);
      }));
    }-*/;

    public static <T> void resolve(JavaScriptObject resolve, JavaScriptObject reject, PromiseResolver<T> resolver) {
        resolver.resolve(new NativeCallback<T>(resolve, reject));
    }

    private final static class HandlerWrapper<T> {

        private final PromiseHandler<?, T> handler;

        private HandlerWrapper(PromiseHandler<?, T> handler) {
            this.handler = handler;
        }

        public Object onFulfilled(T value) {
            PromiseOrValue<?> ret = handler.onFulfilled(value);
            if (ret == null) return null;
            return ret.getObject();
        }

        public Object onRejected(Exception error) {
            PromiseOrValue<?> ret = handler.onRejected(error);
            if (ret == null) return null;
            return ret.getObject();
        }
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
        public void resolve(PromiseOrValue<T> value) {
            if (value == null) nativeResolve(null);
            else nativeResolve(value.getObject());
        }

        @Override
        public void resolveValue(T value) {
            nativeResolve(value);
        }

        @Override
        public void resolvePromise(Promise<T> value) {
            nativeResolve(value);
        }

        @Override
        public void reject(Exception reason) {
            nativeReject(reason);
        }
    }
}
