package noo.promisetest;

import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.rebind.BeforeEach;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

import static noo.testing.jasmine.client.Jasmine.expect;

/**
 * adapted from https://github.com/domenic/promises-unwrapping/blob/master/reference-implementation/test/queue-order.js
 *
 * @author Tal Shani
 */
@Describe("Handler execution order of more than one promise")
public class QueueOrderSpec {
    public PromiseCallback<Object> p1Callback;
    public PromiseCallback<Object> p2Callback;
    Promise<Object> p1;
    Promise<Object> p2;
    double[] calls = new double[0];

    @BeforeEach
    public void preparePromises() {
        calls = new double[0];
        p1 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                p1Callback = callback;
            }
        });
        p2 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                p2Callback = callback;
            }
        });
    }

    @It("should happen in the order they are queued, when added before resolution")
    public void order1(final DoneCallback done) {

        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                calls[calls.length] = 1d;
            }
        });

        p2.catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                calls[calls.length] = 2d;
            }
        });

        // assertion and done
        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                expect(calls).toEqual(new double[]{2, 1});
                done.execute();
            }
        });

        p2Callback.reject(null);
        p1Callback.resolve(null);
    }

    @It("should happen in the order they are queued, when added after resolution")
    public void order2(final DoneCallback done) {
        p2Callback.reject(null);
        p1Callback.resolve(null);

        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                calls[calls.length] = 1d;
            }
        });

        p2.catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                calls[calls.length] = 2d;
            }
        });

        // assertion and done
        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                expect(calls.length).toEqual(2);
                expect(calls).toEqual(new double[]{1, 2});
                done.execute();
            }
        });
    }

    @It("should happen in the order they are queued, when added asynchronously after resolution")
    public void order3(final DoneCallback done) {
        p2Callback.reject(null);
        p1Callback.resolve(null);

        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                p1.then(new PromiseHandler<Object>() {
                    @Override
                    public void handle(Object value) {
                        calls[calls.length] = 1d;
                    }
                });

                p2.catchIt(new PromiseHandler<Throwable>() {
                    @Override
                    public void handle(Throwable value) {
                        calls[calls.length] = 2d;
                    }
                });

                // assertion and done
                Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        expect(calls.length).toEqual(2);
                        expect(calls).toEqual(new double[]{1, 2});
                        done.execute();
                        return false;
                    }
                }, 10);
                return false;
            }
        }, 0);
    }
}
