package noo.promisetest;

import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.FunctionWrapper;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 * Adapted from https://github.com/domenic/promises-unwrapping/blob/master/reference-implementation/test/simple.js
 *
 * @author Tal Shani
 */
@Describe("Simple Spec")
public class SimpleSpec {

    public Easy Easy() {return new Easy();}
    public SelfResolutionErrors SelfResultion() {return new SelfResolutionErrors();}
    public AbruptCompletion AbruptCompletion() {return new AbruptCompletion();}

    @Describe("Easy-to-debug sanity check")
    public class Easy {
        @It("a fulfilled promise calls its fulfillment handler")
        public void test(final DoneCallback done) {
            Promises.resolve(5).then(new PromiseHandler<Integer>() {
                @Override
                public void handle(Integer value) {
                    expect(value).toEqual(5);
                    done.execute();
                }
            });
        }
    }

    @Describe("Self-resolution errors")
    public class SelfResolutionErrors {
        PromiseCallback<Object> resolvePromise;

        @It("directly resolving the promise with itself")
        public void test(final DoneCallback done) {
            Promise<Object> promise = Promises.create(new PromiseResolver<Object>() {
                @Override
                public void resolve(PromiseCallback<Object> callback) {
                    resolvePromise = callback;
                }
            });
            resolvePromise.resolvePromise(promise);

            promise.then(new PromiseHandler<Object>() {
                @Override
                public void handle(Object value) {
                    expectFail();
                    done.execute();
                }
            }).catchIt(new PromiseHandler<Throwable>() {
                @Override
                public void handle(Throwable value) {
                    expectFail();
                    done.execute();
                }
            });

            Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    expectSuccess();
                    done.execute();
                    return false;
                }
            }, 100);
        }
    }

    @Describe("An abrupt completion of the executor function")
    public class AbruptCompletion {
        private boolean exceptionWillBeThrown = false;
        @It("should result in a rejected promise")
        public void test(final DoneCallback done) {
            expect(new FunctionWrapper() {
                @Override
                public void execute() throws Exception {
                    Promises.create(new PromiseResolver<Object>() {
                        @Override
                        public void resolve(PromiseCallback<Object> callback) {
                            exceptionWillBeThrown = true;
                            throw new RuntimeException("some expection");
                        }
                    });
                }
            }).not().toThrow();
            expect(exceptionWillBeThrown).toBeTruthy();
            done.execute();
        }
        @It("should resolve if the promise was resolved before the exception")
        public void test2(final DoneCallback done) {
            expect(new FunctionWrapper() {
                @Override
                public void execute() throws Exception {
                    Promise<Object> promise = Promises.create(new PromiseResolver<Object>() {
                        @Override
                        public void resolve(PromiseCallback<Object> callback) {
                            callback.resolveValue(2);
                            throw new RuntimeException("some exception2");
                        }
                    });

                    promise.then(new PromiseHandler<Object>() {
                        @Override
                        public void handle(Object value) {
                            // this should fail
                            expectSuccess();
                            done.execute();
                        }
                    }).catchIt(new PromiseHandler<Throwable>() {
                        @Override
                        public void handle(Throwable value) {
                            expectFail();
                            done.execute();
                        }
                    });
                }
            }).not().toThrow();
        }
    }
}
