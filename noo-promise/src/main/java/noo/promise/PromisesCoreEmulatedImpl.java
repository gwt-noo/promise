package noo.promise;

import com.google.gwt.core.client.GWT;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Tal Shani
 */
class PromisesCoreEmulatedImpl extends PromisesCore {
    private GWT.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    <T> Promise<T> create(PromiseResolver<T> resolver) {
        return new PromiseEmul<T>(resolver);
    }

    @Override
    String getImplementationName() {
        return "emulated";
    }

    @Override
    <T> Promise<T> resolve(final T value) {
        return create(new PromiseResolver<T>() {
            @Override
            public void resolve(PromiseCallback<T> callback) {
                callback.resolveValue(value);
            }
        });
    }

    private static final Logger logger = Logger.getLogger("PromisesEmul");

    @Override
    Promise<ResolvedCollection> all(final Object... promises) {
        return create(new PromiseResolver<ResolvedCollection>() {
            @Override
            public void resolve(final PromiseCallback<ResolvedCollection> callback) {
                final int promiseCount = promises.length;
                final Map<Integer, Object> results = new HashMap<Integer, Object>(promiseCount);
                int i = 0;
                for (Object o : promises) {
                    final int index = i++;
                    if (!(o instanceof Promise)) {
                        o = Promises.resolve(o);
                    }
                    ((Promise<Object>) o).then(new PromiseHandler<Object>() {
                        @Override
                        public void handle(Object value) {
                            results.put(index, value);
                            logger.info("all() got resolved: " + index);
                            if (results.size() == promiseCount) {
                                List finalResults = new ArrayList(promiseCount);
                                for (int j = 0; j < promiseCount; j++) {
                                    finalResults.add(results.get(j));
                                }
                                callback.resolveValue(new ResolvedCollectionJvmImpl(finalResults));
                            }
                        }
                    });
                    ((Promise<Object>) o).catchIt(new PromiseHandler<Throwable>() {
                        @Override
                        public void handle(Throwable value) {
                            logger.info("all() got error: " + index);
                            callback.reject(value);
                        }
                    });
                }
            }
        });
    }

    @Override
    <T> Promise<T> race(final Promise<? extends T>... promises) {
        return create(new PromiseResolver<T>() {
            private boolean done = false;

            @Override
            public void resolve(final PromiseCallback<T> callback) {
                int i = 0;
                for (Promise<? extends T> promise : promises) {
                    final int index = i++;
                    promise.then(new PromiseHandler<T>() {
                        @Override
                        public void handle(T value) {
                            if (!done) {
                                logger.info("race() got resolved: " + index);
                                done = true;
                                callback.resolveValue(value);
                            }
                        }
                    }, new PromiseHandler<Throwable>() {
                        @Override
                        public void handle(Throwable value) {
                            if (!done) {
                                logger.info("race() got rejected: " + index);
                                done = true;
                                callback.reject(value);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    void setUncaughtExceptionHandler(GWT.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    GWT.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }
}
