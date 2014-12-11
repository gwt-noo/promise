package noo.promise;

import com.google.gwt.core.shared.GWT;

/**
 * @author Tal Shani
 */
public final class Immediate {
    private final static ImmediateCore IMPL;

    static {
        if (GWT.isScript()) {
            IMPL = GWT.create(ImmediateCore.class);
        } else {
            IMPL = new ImmediateCoreJVM();
        }
    }

    public static void clearImmediate(int handle) {
        IMPL.clearImmediate(handle);
    }

    public static void clear(int handle) {
        IMPL.clearImmediate(handle);
    }

    public static int setImmediate(ImmediateCommand command) {
        return IMPL.setImmediate(command);
    }

    public static int set(ImmediateCommand command) {
        return IMPL.setImmediate(command);
    }

    /**
     * Used by the JVM promise emulation to call all immediate(s) that were set
     */
    public static void exit() {
        if (!GWT.isScript()) {
            ImmediateCoreJVM.exit();
        }
    }

    public static String getImplementationName() {
        return IMPL.getImplementationName();
    }
}
