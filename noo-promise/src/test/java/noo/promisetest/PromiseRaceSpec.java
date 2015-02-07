package noo.promisetest;

import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.rebind.BeforeEach;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 *
 * @author Tal Shani
 */
@Describe("Promise.race Spec")
public class PromiseRaceSpec {


    private PromiseCallback<Object> cb1;
    private PromiseCallback<Object> cb2;
    private PromiseCallback<Object> cb3;
    private Promise<Object> p1;
    private Promise<Object> p2;
    private Promise<Object> p3;

    @BeforeEach
    public void createPromises() {
        p1 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                cb1 = callback;
            }
        });
        p2 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                cb2 = callback;
            }
        });
        p3 = Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                cb3 = callback;
            }
        });
    }

    @It("Should resolve to the first promise")
    public void test(final DoneCallback done) {

        Promises.race(p1, p2, p3).then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                expect(value).toBe(3);
                done.execute();
            }
        }, new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                expectFail();
            }
        });
        cb3.resolveValue(3);
        cb2.resolveValue(2);
        cb1.resolveValue(1);
    }

    @It("Should reject to the first rejection")
    public void test2(final DoneCallback done) {

        Promises.race(p1, p2, p3).then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                expectFail();
            }
        }, new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                expect(value.getMessage()).toBe("xx");
                done.execute();
            }
        });
        cb3.reject(new RuntimeException("xx"));
        cb2.resolveValue(2);
        cb1.resolveValue(1);
    }
}
