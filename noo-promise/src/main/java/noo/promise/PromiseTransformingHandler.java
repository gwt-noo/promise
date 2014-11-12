package noo.promise;

/**
 * @author Tal Shani
 */
public abstract class PromiseTransformingHandler<R, T> extends PromiseHandler<R, T> {

    @Override
    protected final PromiseOrValue<R> onFulfilled(T value) {
        return transform(value);
    }

    protected abstract PromiseOrValue<R> transform(T value);
}
