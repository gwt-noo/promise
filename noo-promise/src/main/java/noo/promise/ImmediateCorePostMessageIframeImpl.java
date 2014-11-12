package noo.promise;

import elemental.client.Browser;
import elemental.dom.Document;
import elemental.html.IFrameElement;
import elemental.html.Window;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageIframeImpl extends ImmediateCorePostMessageBase {

    private static Window getWindow() {
        Document doc = Browser.getDocument();
        IFrameElement iframe = doc.createIFrameElement();
        iframe.setSrc("javascript:void 0;");
        doc.getBody().appendChild(iframe);
        return iframe.getContentWindow();
    }

    ImmediateCorePostMessageIframeImpl() {
        super(getWindow());
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":iframe";
    }
}
