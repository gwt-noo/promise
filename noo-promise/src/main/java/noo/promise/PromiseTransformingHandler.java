package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseTransformingHandler<R, T> {
    Promise<R> handle(T value);
}