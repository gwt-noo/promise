package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageBase extends ImmediateCoreEmulatedBase {
    private final static String MESSAGE_PREFIX = "setImmediate$" + Math.random() + "$";
    private final static int PREFIX_LENGTH = MESSAGE_PREFIX.length();
    //    private final Window window = Browser.getWindow();
    private final JavaScriptObject window;

    ImmediateCorePostMessageBase(JavaScriptObject window) {
        this.window = window;
        addEventListener(window, MESSAGE_PREFIX);
    }

    @Override
    int setImmediate(ImmediateCommand command) {
        // Installs an event handler on `global` for the `message` event: see
        // * https://developer.mozilla.org/en/DOM/window.postMessage
        // * http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#crossDocumentMessages

        int handle = addCommand(command);
        postMessage(window, MESSAGE_PREFIX + handle);
        return handle;
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":post message";
    }

    private native void addEventListener(JavaScriptObject window, String messagePrefix) /*-{
        var instance = this;
        window.addEventListener('message', $entry(function (event) {
            if (typeof event.data === "string" && event.data.indexOf(messagePrefix) === 0) {
                instance.@noo.promise.ImmediateCorePostMessageBase::runIfPresent(Ljava/lang/String;)(event.data);
            }
        }))
    }-*/;

    private native static void postMessage(JavaScriptObject window, String message) /*-{
        window.postMessage(message, "*");
    }-*/;


    private void runIfPresent(String handle) {
        runIfPresent(Integer.parseInt(handle.substring(PREFIX_LENGTH)));
    }
}
