package noo.promise;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Shani
 */
public class PromiseEmul<T> implements Promise<T> {

    class PromiseCallbackImpl implements PromiseCallback<T> {

        private boolean done = false;

        @Override
        public void reject(Throwable reason) {
            if (!done) {
                done = true;
                PromiseEmul.this.reject(reason);
            }
        }

        @Override
        public void resolve(PromiseOrValue<T> value) {
            if (!done) {
                done = true;
                PromiseEmul.this.resolve(value);
            }
        }

        @Override
        public void resolveValue(T value) {
            if (!done) {
                done = true;
                PromiseEmul.this.resolve(PromiseOrValue.value(value));
            }
        }

        @Override
        public void resolvePromise(Promise<T> promise) {
            if (!done) {
                done = true;
                PromiseEmul.this.resolve(PromiseOrValue.promise(promise));
            }
        }
    }

    private static <T> HandlersCollection<T> createHandlersCollection() {
        return GWT.isScript() ? NativeHandlersCollection.<T>create() : new JVMHandlersCollection<T>();
    }

    private Throwable reason = null;
    private STATE state = STATE.PENDING;
    private HandlersCollection<T> successSubscribers = PromiseEmul.createHandlersCollection();
    private HandlersCollection<Throwable> errorSubscribers = PromiseEmul.createHandlersCollection();
    private T value = null;
    private final PromiseLoggingHelper logger = new PromiseLoggingHelper();


    public PromiseEmul(PromiseResolver<T> resolver) {
        doResolve(resolver);
    }


    @Override
    public Promise<T> then(final PromiseHandler<T> onFulfilled) {
        logger.enterThen(onFulfilled);
        return new PromiseEmul<T>(new PromiseResolver<T>() {
            @Override
            public void resolve(final PromiseCallback<T> callback) {
                handle(new PromiseHandler<T>() {
                    @Override
                    public void handle(T value) {
                        logger.callingThenHandler(onFulfilled);
                        callback.resolveValue(value);
                        onFulfilled.handle(value);
                    }
                }, null);
            }
        });
    }

    @Override
    public Promise<T> catchIt(final PromiseHandler<Throwable> onRejected) {
        logger.enterCatch(onRejected);
        return new PromiseEmul<T>(new PromiseResolver<T>() {
            @Override
            public void resolve(final PromiseCallback<T> callback) {
                handle(null, new PromiseRejectionHandler() {
                    @Override
                    public void handle(Throwable value) {
                        logger.callingCatchHandler(onRejected);
                        onRejected.handle(value);
                        callback.reject(value);
                    }
                });
            }
        });
    }

    @Override
    public <R> Promise<R> then(final PromiseTransformingHandler<R, T> onFulfilled) {
        logger.enterThen(onFulfilled);
        return new PromiseEmul<R>(new PromiseResolver<R>() {
            @Override
            public void resolve(final PromiseCallback<R> callback) {
                handle(new PromiseHandler<T>() {
                    @Override
                    public void handle(T value) {
                        try {
                            logger.callingThenHandler(onFulfilled);
                            callback.resolve(onFulfilled.handle(value));
                        } catch (Throwable t) {
                            callback.reject(t);
                        }
                    }
                }, null);
            }
        });
    }

    @Override
    public <R> Promise<R> catchIt(final PromiseTransformingHandler<R, Throwable> onRejected) {
        logger.enterCatch(onRejected);
        return new PromiseEmul<R>(new PromiseResolver<R>() {
            @Override
            public void resolve(final PromiseCallback<R> callback) {
                handle(null, new PromiseRejectionHandler() {
                    @Override
                    public void handle(Throwable value) {
                        try {
                            logger.callingCatchHandler(onRejected);
                            callback.resolve(onRejected.handle(value));
                        } catch (Throwable t) {
                            callback.reject(t);
                        }
                    }
                });
            }
        });
    }

    /**
     * Creates a resolver that will resolvePromise it's callback one the given promise is resolved
     *
     * @param promise
     * @return
     */
    private PromiseResolver<T> createResolver(final Promise<T> promise) {
        return new PromiseResolver<T>() {
            @Override
            public void resolve(final PromiseCallback<T> callback) {
                promise.then(new PromiseHandler<T>() {
                    @Override
                    public void handle(T value) {
                        callback.resolveValue(value);
                    }
                }).catchIt(new PromiseHandler<Throwable>() {
                    @Override
                    public void handle(Throwable value) {
                        callback.reject(reason);
                    }
                });
            }
        };
    }

    private void doResolve(PromiseResolver<T> fn) {
        PromiseCallbackImpl callback = new PromiseCallbackImpl();
        try {
            fn.resolve(callback);
        } catch (Throwable throwable) {
            callback.reject(throwable);
        }
    }

    private void fireHandlers(STATE state) {

        if (state == STATE.PENDING || this.state != STATE.PENDING) return;
        this.state = state;
        // because we changed the state no more subscriber will join the subscribers array
        if (state == STATE.FULFILLED) {
            int length = successSubscribers.length();
            for (int i = 0; i < length; i++) {
                handleFulfilled(successSubscribers.get(i));
            }
        } else {
            int length = errorSubscribers.length();
            for (int i = 0; i < length; i++) {
                handleRejected(errorSubscribers.get(i));
            }
        }
        successSubscribers = null;
        errorSubscribers = null;
    }

    private void fulfill(T value) {
        this.value = value;
        fireHandlers(STATE.FULFILLED);
    }

    private void handle(final PromiseHandler<T> onFulfilled, final PromiseHandler<Throwable> onRejected) {
        if (state == STATE.PENDING) {
            if (onFulfilled != null) successSubscribers.push(onFulfilled);
            if (onRejected != null) errorSubscribers.push(onRejected);
        } else if (state == STATE.FULFILLED) {
            if (onFulfilled != null) {
                Immediate.setImmediate(new ImmediateCommand() {
                    @Override
                    public void execute() {
                        onFulfilled.handle(value);
                    }
                });
            }
        } else if (state == STATE.REJECTED) {
            if (onRejected != null) {
                Immediate.setImmediate(new ImmediateCommand() {
                    @Override
                    public void execute() {
                        onRejected.handle(reason);
                    }
                });
            }
        }
    }

    private void handleFulfilled(PromiseHandler<T> handler) {
        handler.handle(value);
    }

    private void handleRejected(PromiseHandler<Throwable> handler) {
        handler.handle(reason);
    }

    private void reject(Throwable reason) {
        this.reason = reason;
        fireHandlers(STATE.REJECTED);
    }

//    private void resolvePromise(Object value) {
//        try {
//            if (value instanceof Promise) {
//                PromiseResolver<T> then = createResolver((Promise<T>) value);
//                if (then != null) {
//                    doResolve(then);
//                    return;
//                }
//            }
//            fulfill((T) value);
//        } catch (Exception e) {
//            reject(e);
//        }
//    }


    private void resolve(PromiseOrValue<T> value) {
        try {
            if (value == null || value.getObject() == null) {
                fulfill(null);
            } else if (value.isValue()) {
                fulfill(value.getValue());
            } else {
                PromiseResolver<T> resolver = createResolver(value.getPromise());
                doResolve(resolver);
            }
        } catch (Exception e) {
            reject(e);
        }
    }

    static enum STATE {
        PENDING, FULFILLED, REJECTED;
    }

    static interface HandlersCollection<T> {
        int length();

        PromiseHandler<T> get(int i);

        void push(PromiseHandler<T> handler);
    }

    static final class NativeHandlersCollection<T> extends JavaScriptObject implements HandlersCollection<T> {

        protected NativeHandlersCollection() {
        }


        public static <T> NativeHandlersCollection<T> create() {
            return JavaScriptObject.createArray().cast();
        }

        public native int length() /*-{
            return this.length;
        }-*/;

        public native PromiseHandler<T> get(int i) /*-{
            return this[i];
        }-*/;

        public native void push(PromiseHandler<T> handler) /*-{
            this[this.length] = handler;
        }-*/;
    }

    static final class JVMHandlersCollection<T> implements HandlersCollection<T> {

        private final List<PromiseHandler<T>> ar = new ArrayList<PromiseHandler<T>>();

        public int length() {
            return ar.size();
        }

        public PromiseHandler<T> get(int i) {
            return ar.get(i);
        }

        public void push(PromiseHandler<T> handler) {
            ar.set(ar.size(), handler);
        }
    }
}
