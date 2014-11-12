package noo.promise;

import elemental.client.Browser;
import elemental.dom.TimeoutHandler;
import elemental.js.util.JsMapFromIntTo;

/**
 * @author Tal Shani
 */
abstract class ImmediateCoreEmulatedBase extends ImmediateCore {

    private int nextHandle = 1;
    private boolean currentlyRunningATask = false;
    private JsMapFromIntTo<ImmediateCommand> tasksByHandle = JsMapFromIntTo.create();

    int addCommand(ImmediateCommand command) {
        tasksByHandle.put(nextHandle, command);
        return nextHandle++;
    }

    void runIfPresent(final int handle) {
        // From the spec: "Wait until any invocations of this algorithm started before this one have completed."
        // So if we're currently running a task, we'll need to delay this invocation.
        if (currentlyRunningATask) {
            // Delay by doing a setTimeout. setImmediate was tried instead, but in Firefox 7 it generated a
            // "too much recursion" error.
            Browser.getWindow().setTimeout(new TimeoutHandler() {
                @Override
                public void onTimeoutHandler() {
                    runIfPresent(handle);
                }
            }, 0);
        } else {
            ImmediateCommand task = tasksByHandle.get(handle);
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
        tasksByHandle.put(handle, null);
    }

    abstract int setImmediate(ImmediateCommand command);

    @Override
    String getImplementationName() {
        return "emulated";
    }
}
