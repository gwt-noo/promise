package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseTransformingHandler<R, T> {
    PromiseOrValue<R> handle(T value);
}