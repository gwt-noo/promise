package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageIframeImpl extends ImmediateCorePostMessageBase {

    private static native JavaScriptObject getWindow() /*-{
        var iframe = $doc.createElement('iframe');
        iframe.src = "javascript:void 0;";
        $doc.body.appendChild(iframe)
        return iframe.contentWindow;
    }-*/;

    ImmediateCorePostMessageIframeImpl() {
        super(getWindow());
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":iframe";
    }
}
