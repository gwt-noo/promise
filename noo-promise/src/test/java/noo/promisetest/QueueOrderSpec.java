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

    @BeforeEach
    public void preparePromises() {
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
        final ItemsAssertion items = new ItemsAssertion(done, "p1", "p2");

        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                items.mark("p1");
            }
        });

        p2.catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                items.mark("p2");
            }
        });

        p2Callback.reject(null);
        p1Callback.resolve(null);
    }

    @It("should happen in the order they are queued, when added after resolution")
    public void order2(final DoneCallback done) {
        final ItemsAssertion items = new ItemsAssertion(done, "p1", "p2");
        p2Callback.reject(null);
        p1Callback.resolve(null);

        p1.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                items.mark("p1");
            }
        });

        p2.catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                items.mark("p2");
            }
        });
    }

    @It("should happen in the order they are queued, when added asynchronously after resolution")
    public void order3(final DoneCallback done) {
        final ItemsAssertion items = new ItemsAssertion(done, "p1", "p2");
        p2Callback.reject(null);
        p1Callback.resolve(null);

        Immediate.set(new ImmediateCommand() {
            @Override
            public void execute() {
                p1.then(new PromiseHandler<Object>() {
                    @Override
                    public void handle(Object value) {
                        items.mark("p1");
                    }
                });

                p2.catchIt(new PromiseHandler<Throwable>() {
                    @Override
                    public void handle(Throwable value) {
                        items.mark("p2");
                    }
                });
            }
        });
    }
}
