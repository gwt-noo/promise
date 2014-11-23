package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
public class PromiseEmul<T> implements Promise<T> {

    class PromiseCallbackImpl implements PromiseCallback<T> {

        private boolean done = false;

        @Override
        public void reject(Exception reason) {
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

    private Exception reason = null;
    private STATE state = STATE.PENDING;
    private HandlersCollection<T> subscribers = HandlersCollection.create();
    private T value = null;


    public PromiseEmul(PromiseResolver<T> resolver) {
        doResolve(resolver);
    }

    public void done(final PromiseHandler<?, T> handler) {
        // when we are done, we must do it async

        Immediate.setImmediate(new ImmediateCommand() {
            @Override
            public void execute() {

                handle(handler);
            }
        });
    }

    public <R> Promise<R> then(final PromiseHandler<R, T> handler) {
        return new PromiseEmul<R>(new PromiseResolver<R>() {
            @Override
            public void resolve(final PromiseCallback<R> callback) {
                done(new PromiseHandler<Void, T>() {
                    @Override
                    protected PromiseOrValue<Void> onRejected(Exception reason) {
                        try {
                            callback.resolve(handler.onRejected(reason));
                        } catch (Exception e) {
                            callback.reject(e);
                        }
                        return null;
                    }

                    @Override
                    protected PromiseOrValue<Void> onFulfilled(T value) {
                        try {
                            callback.resolve(handler.onFulfilled(value));
                        } catch (Exception e) {
                            callback.reject(e);
                        }
                        return null;
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
                promise.then(new PromiseHandler<Object, T>() {
                    @Override
                    protected PromiseOrValue<Object> onFulfilled(T value) {
                        callback.resolveValue(value);
                        return null;
                    }

                    @Override
                    protected PromiseOrValue<Object> onRejected(Exception reason) {
                        callback.reject(reason);
                        return null;
                    }
                });
            }
        };
    }

    private void doResolve(PromiseResolver<T> fn) {
        fn.resolve(new PromiseCallbackImpl());
    }

    private void fireHandlers(STATE state) {
        if (state == STATE.PENDING || this.state != STATE.PENDING) return;
        this.state = state;
        // because we changed the state no more subscriber will join the subscribers array
        int length = subscribers.length();
        for (int i = 0; i < length; i++) {
            handle(subscribers.get(i));
        }
        subscribers = null;
    }

    private void fulfill(T value) {
        this.value = value;
        fireHandlers(STATE.FULFILLED);
    }

    private void handle(PromiseHandler<?, T> handler) {
        if (state == STATE.PENDING) {
            subscribers.push(handler);
        } else if (state == STATE.FULFILLED) {
            handler.onFulfilled(value);
        } else if (state == STATE.REJECTED) {
            handler.onRejected(reason);
        }
    }

    private void reject(Exception reason) {
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

    static final class HandlersCollection<T> extends JavaScriptObject {

        protected HandlersCollection() {
        }


        public static <T> HandlersCollection<T> create() {
            return JavaScriptObject.createArray().cast();
        }

        public native int length() /*-{
            return this.length;
        }-*/;

        public native PromiseHandler<?, T> get(int i) /*-{
            return this[i];
        }-*/;

        public native void push(PromiseHandler<?, T> handler) /*-{
            this[this.length] = handler;
        }-*/;
    }
}
