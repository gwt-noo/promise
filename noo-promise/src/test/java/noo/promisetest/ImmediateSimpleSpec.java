package noo.promisetest;

import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.Jasmine;
import noo.testing.jasmine.client.rebind.Describe;
import noo.testing.jasmine.client.rebind.It;

/**
 * Taken from https://github.com/domenic/promises-unwrapping/blob/master/reference-implementation/test/simple.js
 *
 * @author Tal Shani
 */
@Describe("Immediate")
public class ImmediateSimpleSpec {
    @It("should work")
    public void shouldJustWork(final DoneCallback done) {
        Immediate.setImmediate(new ImmediateCommand() {
            @Override
            public void execute() {
                Jasmine.expectSuccess();
                done.execute();
            }
        });
    }

    @It("should run in a specific order")
    public void testOrder(final DoneCallback done) {
        final PositionAssertion asserter = new PositionAssertion(3, done);
        asserter.pos(0);
        Immediate.setImmediate(new ImmediateCommand() {
            @Override
            public void execute() {
                asserter.pos(3);
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

    @It("should run in a order of registration")
    public void testSeveralImmediateOrder(final DoneCallback done) {
        final PositionAssertion asserter = new PositionAssertion(2, done);
        asserter.pos(0);
        Immediate.setImmediate(new ImmediateCommand() {
            @Override
            public void execute() {
                asserter.pos(1);
            }
        });
        Immediate.setImmediate(new ImmediateCommand() {
            @Override
            public void execute() {
                asserter.pos(2);
            }
        });
    }


}
