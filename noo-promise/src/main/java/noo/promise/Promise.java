package noo.promise;

/**
 * @author Tal Shani
 */
public interface Promise<T> {
    Promise<T> then(PromiseHandler<T> onFulfilled);
    Promise<T> catchIt(PromiseHandler<Throwable> onRejected);

    <R> Promise<R> then(PromiseTransformingHandler<R, T> onFulfilled);
    <R> Promise<R> catchIt(PromiseTransformingHandler<R, Throwable> onRejected);
}
