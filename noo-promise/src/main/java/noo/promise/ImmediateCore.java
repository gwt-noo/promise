package noo.promise;

/**
 * @author Tal Shani
 */
abstract class ImmediateCore {

    abstract void clearImmediate(int handle);

    abstract int setImmediate(ImmediateCommand command);

    abstract String getImplementationName();
}
