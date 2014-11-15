package noo.promise;

/**
 * @author Tal Shani
 */
class ImmediateCoreNativeImpl extends ImmediateCore {

    @Override
    final native void clearImmediate(int handle) /*-{
      Window.prototype.clearImmediate.call(window, handle);
    }-*/;

    @Override
    final native int setImmediate(final ImmediateCommand command) /*-{
      var cmd = command;
      var id = Window.prototype.setImmediate.call(window, $entry(function() {
        cmd.@noo.promise.ImmediateCommand::execute()();
      }));
      return id;
    }-*/;

    @Override
    String getImplementationName() {
        return "native";
    }
}
