package noo.promise;

/**
 * @author Tal Shani
 */
class PromisesCoreNativeImpl extends PromisesCore {
    @Override
    <T> Promise<T> create(PromiseResolver<T> resolver) {
        return PromiseNative.createPromise(resolver);
    }

    @Override
    String getImplementationName() {
        return "native";
    }

    @Override
    native <T> Promise<T> resolve(T value) /*-{
        return Promise.resolve(value);
    }-*/;
}
