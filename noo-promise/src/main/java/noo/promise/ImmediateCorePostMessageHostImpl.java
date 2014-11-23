package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageHostImpl extends ImmediateCorePostMessageBase {

    private static native JavaScriptObject getWindow() /*-{
        return $wnd;
    }-*/;

    ImmediateCorePostMessageHostImpl() {
        super(getWindow());
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":host window";
    }
}
