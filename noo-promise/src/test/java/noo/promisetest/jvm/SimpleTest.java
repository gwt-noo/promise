package noo.promisetest.jvm;

import noo.promise.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Tal Shani
 */
public class SimpleTest {

    List<Object> memory;
    PromiseCallback<Object> resolvePromise;

    @Before
    public void beforeEach() {
        memory = new LinkedList<Object>();
        resolvePromise = null;
    }

    @Test
    public void easy() {
        Promises.resolve(5).then(new PromiseHandler<Integer>() {
            @Override
            public void handle(Integer value) {
                memory.add(value);
            }
        });
        Promises.flush();

        assertEquals(1, memory.size());
        assertEquals(5, memory.get(0));
    }

    //    @Test
    public void selfResolution() {
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
                memory.add(null);
                fail();
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                memory.add(null);
                fail();
            }
        });

        Promises.flush();

        assertEquals(0, memory.size());
    }

    @Test
    public void testAbruptCompletion() {
        Promises.create(new PromiseResolver<Object>() {
            @Override
            public void resolve(PromiseCallback<Object> callback) {
                memory.add(null);
                throw new RuntimeException("some exception");
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                memory.add(value);
            }
        });

        Promises.flush();

        assertEquals(2, memory.size());
    }

    @Test
    public void testAbruptCompletionAfterResolution() {
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
                memory.add(null);
            }
        }).catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {
                fail();
            }
        });

        Promises.flush();

        assertEquals(1, memory.size());
    }
}
