package noo.promise;

/**
 * @author Tal Shani
 */
class PromisesCoreEmulatedImpl extends PromisesCore {
    @Override
    <T> Promise<T> create(PromiseResolver<T> resolver) {
        return new PromiseEmul<T>(resolver);
    }

    @Override
    String getImplementationName() {
        return "emulated";
    }
}
