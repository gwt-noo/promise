package noo.promise;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tal Shani
 */
class ImmediateCorePromiseImpl extends ImmediateCore {

    private final static JavaScriptObject ids = JavaScriptObject.createArray();
    private static int id = 1;
    /**
     * The last executed id
     */
    private static int executedId = 0;

    @Override
    final native void clearImmediate(int handle) /*-{
      if(@noo.promise.ImmediateCorePromiseImpl::executedId >= handle) return;
      @noo.promise.ImmediateCorePromiseImpl::ids[handle] = true;
    }-*/;

    @Override
    final native int setImmediate(final ImmediateCommand command) /*-{
      var ar = @noo.promise.ImmediateCorePromiseImpl::ids;
      var id = @noo.promise.ImmediateCorePromiseImpl::id;
      @noo.promise.ImmediateCorePromiseImpl::id = id + 1;
      var promise = new Promise(function (resolve, reject) {
        resolve(null);
      });
      promise.then($entry(function () {
        if(!ar[id]) {
          @noo.promise.ImmediateCorePromiseImpl::executedId = Math.max(@noo.promise.ImmediateCorePromiseImpl::executedId, id);
          command.@noo.promise.ImmediateCommand::execute()();
        }
        delete ar[id];
      }));
      return id;
    }-*/;

    @Override
    String getImplementationName() {
        return "promise";
    }
}
