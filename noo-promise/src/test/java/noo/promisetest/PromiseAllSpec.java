package noo.promisetest;

import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

import java.util.List;

import static noo.testing.jasmine.client.Jasmine.expect;
import static noo.testing.jasmine.client.Jasmine.expectFail;
import static noo.testing.jasmine.client.Jasmine.expectSuccess;

/**
 *
 * @author Tal Shani
 */
@Describe("Promise.all Spec")
public class PromiseAllSpec {
    @It("a collection of literal values will resolve to the values")
    public void test(final DoneCallback done) {
        Promises.all(true, true, false).then(new PromiseHandler<ResolvedCollection>() {
            @Override
            public void handle(ResolvedCollection value) {
                expect(value.length()).toBe(3);
                done.execute();
            }
        });
    }

    @It("should reject if any value is a rejected promise")
    public void testWithReject(final DoneCallback done) {
        Promise<Object> promise = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                callback.reject(new RuntimeException("xxx"));
            }
        });

        Promises.all(true, promise).then(new PromiseHandler<ResolvedCollection>() {
            @Override
            public void handle(ResolvedCollection value) {
                expectFail();
                done.execute();
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                expectSuccess();
                done.execute();
            }
        });
    }

    @It("should reject with the first rejected promise, by order passed to all()")
    public void testWithReject2(final DoneCallback done) {
        final ItemsAssertion itemsAssertion = new ItemsAssertion(done, "1,2", "2,1");
        final RuntimeException exception1 = new RuntimeException("exception1");
        final RuntimeException exception2 = new RuntimeException("exception2");

        final Promise<Object> p2 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                callback.reject(exception2);
            }
        });
        final Promise<Object> p1 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                callback.reject(exception1);
            }
        });

        Immediate.set(new ImmediateCommand() {
            @Override
            public void execute() {
                Promises.all(p1, p2).then(new PromiseHandler<ResolvedCollection>() {
                    @Override
                    public void handle(ResolvedCollection value) {
                        expectFail();
                    }
                }).catchIt(new PromiseHandler<Throwable>() {
                    @Override
                    public void handle(Throwable value) {
                        expect(value.getMessage()).toBe("exception1");
                        itemsAssertion.mark("1,2");
                    }
                });

                Immediate.set(new ImmediateCommand() {
                    @Override
                    public void execute() {
                        Promises.all(p2, p1).then(new PromiseHandler<ResolvedCollection>() {
                            @Override
                            public void handle(ResolvedCollection value) {
                                expectFail();
                            }
                        }).catchIt(new PromiseHandler<Throwable>() {
                            @Override
                            public void handle(Throwable value) {
                                expect(value.getMessage()).toBe("exception2");
                                itemsAssertion.mark("2,1");
                            }
                        });
                    }
                });
            }
        });
    }

//    @It("should reject with the first rejected promise, by order passed to all()")
    // For some reason we need to add immediate around the Promise.all so the promises will be resolved/rejected
    // otherwise this test won't work for FF 35.0.1
    public void testWithReject2_shouldWork(final DoneCallback done) {
        final ItemsAssertion itemsAssertion = new ItemsAssertion(done, "1,2", "2,1");
        final RuntimeException exception1 = new RuntimeException("exception1");
        final RuntimeException exception2 = new RuntimeException("exception2");

        final Promise<Object> p2 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                callback.reject(exception2);
            }
        });
        final Promise<Object> p1 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                callback.reject(exception1);
            }
        });


        Promises.all(p1, p2).then(new PromiseHandler<ResolvedCollection>() {
            @Override
            public void handle(ResolvedCollection value) {
                expectFail();
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                expect(value.getMessage()).toBe("exception1");
                itemsAssertion.mark("1,2");
            }
        });

        Promises.all(p2, p1).then(new PromiseHandler<ResolvedCollection>() {
            @Override
            public void handle(ResolvedCollection value) {
                expectFail();
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                expect(value.getMessage()).toBe("exception2");
                itemsAssertion.mark("2,1");
            }
        });
    }
}
