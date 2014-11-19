package noo.promise;

import elemental.events.Event;
import elemental.events.EventListener;
import elemental.events.MessageEvent;
import elemental.html.Window;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageBase extends ImmediateCoreEmulatedBase {

    class Listener implements EventListener {

        @Override
        public void handleEvent(Event evt) {
            MessageEvent event = (MessageEvent) evt;
            if (event.getSource() == window && eventValid(event, messagePrefix)) {
                String data = (String) event.getData();
                runIfPresent(Integer.parseInt(data.substring(prefixLength)));
            }
        }
    }

    private final String messagePrefix = "setImmediate$" + Math.random() + "$";
    private final int prefixLength = messagePrefix.length();
    //    private final Window window = Browser.getWindow();
    private final Window window;

    ImmediateCorePostMessageBase(Window window) {
        this.window = window;
        window.addEventListener("message", new Listener(), false);
    }

    @Override
    int setImmediate(ImmediateCommand command) {
        // Installs an event handler on `global` for the `message` event: see
        // * https://developer.mozilla.org/en/DOM/window.postMessage
        // * http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#crossDocumentMessages

        int handle = addCommand(command);
        window.postMessage(messagePrefix + handle, "*");
        return handle;
    }

    private native boolean eventValid(Object event, String messagePrefix) /*-{
      return typeof event.data === "string" && event.data.indexOf(messagePrefix) === 0
    }-*/;

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":post message";
    }
}
