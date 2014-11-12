package noo.promise;

import elemental.client.Browser;
import elemental.html.Window;

/**
 * @author Tal Shani
 */
class ImmediateCorePostMessageHostImpl extends ImmediateCorePostMessageBase {

    private static Window getWindow() {
        return Browser.getWindow();
    }

    ImmediateCorePostMessageHostImpl() {
        super(getWindow());
    }

    @Override
    String getImplementationName() {
        return super.getImplementationName() + ":host window";
    }
}
