package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
final class ResolvedCollectionNativeImpl extends JavaScriptObject implements ResolvedCollection {
    protected ResolvedCollectionNativeImpl() {
    }

    @Override
    public native Object get(int i) /*-{
        return this[i];
    }-*/;

    @Override
    public native int length() /*-{
        return this.length;
    }-*/;
}
