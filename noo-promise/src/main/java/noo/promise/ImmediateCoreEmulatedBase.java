package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
abstract class ImmediateCoreEmulatedBase extends ImmediateCore {

    private int nextHandle = 1;
    private boolean currentlyRunningATask = false;
    private JavaScriptObject tasksByHandle = JavaScriptObject.createObject();

    int addCommand(ImmediateCommand command) {
        setCommand_(tasksByHandle, nextHandle, command);
        return nextHandle++;
    }

    private static native void setCommand_(JavaScriptObject map, int key, ImmediateCommand value) /*-{
        map[key] = value;
    }-*/;

    private static native ImmediateCommand getCommand_(JavaScriptObject map, int key) /*-{
        return map[key];
    }-*/;

    void runIfPresent(final int handle) {
        // From the spec: "Wait until any invocations of this algorithm started before this one have completed."
        // So if we're currently running a task, we'll need to delay this invocation.
        if (currentlyRunningATask) {
            // Delay by doing a setTimeout. setImmediate was tried instead, but in Firefox 7 it generated a
            // "too much recursion" error.
            delayRunIfPresent(this, handle);
        } else {
            ImmediateCommand task = getCommand_(tasksByHandle, handle);
            if (task != null) {
                currentlyRunningATask = true;
                try {
                    task.execute();
                } finally {
                    clearImmediate(handle);
                    currentlyRunningATask = false;
                }
            }
        }
    }

    @Override
    void clearImmediate(int handle) {
        setCommand_(tasksByHandle, handle, null);
    }

    abstract int setImmediate(ImmediateCommand command);

    @Override
    String getImplementationName() {
        return "emulated";
    }

    private static native void delayRunIfPresent(ImmediateCoreEmulatedBase instance, int handle) /*-{
        setTimeout($entry(function () {
            instance.@noo.promise.ImmediateCoreEmulatedBase::runIfPresent(I)(handle);
        }))
    }-*/;
}
