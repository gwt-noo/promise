package noo.promisetest;

import noo.testing.jasmine.client.DoneCallback;

import static noo.testing.jasmine.client.Jasmine.expect;

/**
* @author Tal Shani
*/
class PositionAssertion {
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
        if (pos == total) {
            done.execute();
        }
        if (pos > total) {
            String message = "Too many position assertions: " + currentPos + " expected " + total;
            throw new RuntimeException(message);
        }
    }
}
