package noo.promise;

/**
 * @author Tal Shani
 */
public abstract class PromiseHandler<R, T> {

    protected PromiseOrValue<R> onFulfilled(T value) {
        return null;
    }

    protected PromiseOrValue<R> onRejected(Exception reason) {
        throw new RuntimeException(reason);
    }

    protected final PromiseOrValue<R> createValue(R value) {
        return PromiseOrValue.value(value);
    }

    protected final PromiseOrValue<R> createPromise(PromiseEmul<R> promise) {
        return PromiseOrValue.promise(promise);
    }
}
