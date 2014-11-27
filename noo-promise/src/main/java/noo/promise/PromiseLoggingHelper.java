package noo.promise;

import com.google.gwt.logging.client.LogConfiguration;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tal Shani
 */
class PromiseLoggingHelper {
    private static final Logger LOG = Logger.getLogger("Promise");
    private static int ID = 1;
    private static int PROMISE_HELPER_ID = 0;
    private static final boolean ENABLED = LogConfiguration.loggingIsEnabled(Level.FINER);

    private final int id = ID++;

    public PromiseLoggingHelper() {
        if (ENABLED) {
            log("Was created");
        }
    }


    public void enterThen(Object o) {
        if (ENABLED) {
            int id = getObjectId(o);
            log("'Then' called with handler: " + id);
        }
    }

    public void enterCatch(Object o) {
        if (ENABLED) {
            int id = getObjectId(o);
            log("'Catch' called with handler: " + id);
        }
    }

    private void log(String str) {
        if (ENABLED) {
            LOG.finer("Promise #" + id + " : " + str);
        }
    }

    public void callingCatchHandler(Object o) {
        if (ENABLED) {
            int id = getObjectId(o);
            log("'Catch' resolved handler: " + id);
        }
    }

    public void callingThenHandler(Object o) {
        if (ENABLED) {
            int id = getObjectId(o);
            log("'Then' resolved handler: " + id);
        }
    }

    private static native int getObjectId(Object o) /*-{
        if (typeof o.__PROMISE_HELPER_ID__ != 'number') {
            o.__PROMISE_HELPER_ID__ = @noo.promise.PromiseLoggingHelper::PROMISE_HELPER_ID++;
        }
        return o.__PROMISE_HELPER_ID__;
    }-*/;
}
