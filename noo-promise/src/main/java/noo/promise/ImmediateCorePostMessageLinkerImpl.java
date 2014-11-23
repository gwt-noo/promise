package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageLinkerImpl extends ImmediateCorePostMessageBase {
    private native static JavaScriptObject getWindow() /*-{
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
