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

    @Override
    <T> Promise<T> resolve(final T value) {
        return create(new PromiseResolver<T>() {
            @Override
            public void resolve(PromiseCallback<T> callback) {
                callback.resolve(PromiseOrValue.value(value));
            }
        });
    }
}
