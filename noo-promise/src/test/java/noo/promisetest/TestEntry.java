package noo.promisetest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DescribeCallback;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.JasmineCallback;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 * @author Tal Shani
 */
public class TestEntry implements EntryPoint {
    @Override
    public void onModuleLoad() {

        describe("promises", new DescribeCallback() {

            @Override
            protected void doDescribe() {

                it("should run in a specific order", new JasmineCallback() {
                    @Override
                    public void define(final DoneCallback done) {
                        final PositionAssertion asserter = new PositionAssertion(5, done);
                        asserter.pos(0);
                        Promise<Object> promise = Promises.create(new PromiseResolver<Object>() {
                            @Override
                            public void resolve(PromiseCallback<Object> callback) {
                                asserter.pos(1);
                                callback.resolveValue(null);
                            }
                        });
                        asserter.pos(2);
                        promise.then(new PromiseHandler<Object, Object>() {
                            @Override
                            protected PromiseOrValue<Object> onFulfilled(Object value) {
                                asserter.pos(5);
                                return super.onFulfilled(value);
                            }
                        });
                        asserter.pos(3);

                        Scheduler.get().scheduleFinally(new Scheduler.RepeatingCommand() {
                            @Override
                            public boolean execute() {
                                asserter.pos(4);
                                return false;
                            }
                        });
                    }
                });
            }
        });


        describe("immediate", new DescribeCallback() {

            @Override
            protected void doDescribe() {
                it("should run in a specific order", new JasmineCallback() {
                    @Override
                    public void define(final DoneCallback done) {
                        final PositionAssertion asserter = new PositionAssertion(4, done);
                        asserter.pos(0);
                        Immediate.setImmediate(new ImmediateCommand() {
                            @Override
                            public void execute() {
                                asserter.pos(3);
                                done.execute();
                            }
                        });
                        asserter.pos(1);
                        Scheduler.get().scheduleFinally(new Scheduler.RepeatingCommand() {
                            @Override
                            public boolean execute() {
                                asserter.pos(2);
                                return false;
                            }
                        });
                    }
                });

            }
        });
    }


    static class PositionAssertion {
        private final DoneCallback done;
        private final int total;
        private int currentPos = 0;

        PositionAssertion(int total, DoneCallback done) {
            this.done = done;
            this.total = total;
        }

        public void pos(int pos) {
            expect(pos).toBe(currentPos);
            currentPos++;
            if (currentPos == total) {
                done.execute();
            }
            if (currentPos > total) {
                String message = "Too many position assertions: " + currentPos + " expected " + total;
                throw new RuntimeException(message);
            }
        }
    }
}
