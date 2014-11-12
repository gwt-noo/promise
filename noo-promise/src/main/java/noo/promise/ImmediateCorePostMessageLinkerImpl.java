package noo.promise;

import elemental.html.Window;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageLinkerImpl extends ImmediateCorePostMessageBase {
    private native static Window getWindow() /*-{
      return window;
    }-*/;

    ImmediateCorePostMessageLinkerImpl() {
        super(getWindow());
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":linker window";
    }
}
