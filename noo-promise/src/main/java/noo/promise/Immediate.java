package noo.promise;

import com.google.gwt.core.shared.GWT;

/**
 * @author Tal Shani
 */
public final class Immediate {
    private final static ImmediateCore IMPL;

    static {
        IMPL = GWT.create(ImmediateCore.class);
//        if (GWT.isScript()) {
//
//        } else {
//            IMPL = null;
//        }
    }

    public static void clearImmediate(int handle) {
        IMPL.clearImmediate(handle);
    }

    public static int setImmediate(ImmediateCommand command) {
//        GWT.debugger();
        return IMPL.setImmediate(command);
    }

    public static String getImplementationName() {
        return IMPL.getImplementationName();
    }
}
