package noo.promisetest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
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

            int currentPos = 0;

            @Override
            protected void doDescribe() {

                it("should run in a specific order", new JasmineCallback() {
                    @Override
                    public void define(final DoneCallback done) {
                        assertPos(0);
                        Promise<Object> promise = Promises.create(new PromiseResolver<Object>() {
                            @Override
                            public void resolve(PromiseCallback<Object> callback) {
                                assertPos(1);
                                callback.resolveValue(null);
                            }
                        });
                        assertPos(2);
                        promise.then(new PromiseHandler<Object, Object>() {
                            @Override
                            protected PromiseOrValue<Object> onFulfilled(Object value) {
                                assertPos(5);
                                return super.onFulfilled(value);
                            }
                        });
                        assertPos(3);
                        Scheduler.get().scheduleFinally(new Command() {
                            @Override
                            public void execute() {
                                assertPos(4);
                                done.execute();
                            }
                        });
                    }
                });
            }

            void assertPos(int pos) {
                expect(pos).toBe(currentPos);
                currentPos++;
            }
        });


        describe("immediate", new DescribeCallback() {

            @Override
            protected void doDescribe() {
                it("should run in a specific order", new JasmineCallback() {
                    int currentPos = 0;

                    @Override
                    public void define(final DoneCallback done) {
                        assertPos(0);
                        Immediate.setImmediate(new ImmediateCommand() {
                            @Override
                            public void execute() {
                                assertPos(3);
                                done.execute();
                            }
                        });
                        assertPos(1);
                        Scheduler.get().scheduleFinally(new Command() {
                            @Override
                            public void execute() {
                                assertPos(2);
                            }
                        });
                    }

                    void assertPos(int pos) {
                        expect(pos).toBe(currentPos);
                        currentPos++;
                    }
                });

            }
        });
    }
}
