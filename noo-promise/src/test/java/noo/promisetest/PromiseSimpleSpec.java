package noo.promisetest;

import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.FunctionWrapper;
import noo.testing.jasmine.client.Jasmine;
import noo.testing.jasmine.client.rebind.BeforeEach;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 * Taken from https://github.com/domenic/promises-unwrapping/blob/master/reference-implementation/test/simple.js
 *
 * @author Tal Shani
 */
@Describe("Promises")
public class PromiseSimpleSpec {

    @It("should run in a specific order")
    public void testOrder(DoneCallback done) {
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
        promise.then(new PromiseHandler<Object>() {
            @Override
            public void handle(Object value) {
                asserter.pos(5);
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

    public QueueOrderSpec QueueOrderSpec() {return new QueueOrderSpec();}
    public SimpleSpec SimpleSpec() {return new SimpleSpec();}
}
