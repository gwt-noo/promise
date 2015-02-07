package noo.promise;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tal Shani
 */
class PromiseLoggingHelper {
    private static final Logger LOG = Logger.getLogger("Promise");
    private static int ID = 1;
    private static int PROMISE_HELPER_ID = 0;
    private static final boolean ENABLED = GWT.isClient() && LogConfiguration.loggingIsEnabled(Level.FINER);

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

    public void enterThen(Object fulfilled, Object reject) {
        if (ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("'Then' called with(");
            if (fulfilled != null) sb.append(" success handler: " + getObjectId(fulfilled));
            if (reject != null) sb.append(" error handler: " + getObjectId(reject));
            sb.append(" )");
            log(sb.toString());
        }
    }
}
