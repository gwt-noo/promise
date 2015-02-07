package noo.promise;

/**
 * @author Tal Shani
 */
public interface Promise<T> {
    Promise<T> then(PromiseHandler<? super T> onFulfilled);
    Promise<T> then(PromiseHandler<? super T> onFulfilled, PromiseHandler<Throwable> onRejected);
    Promise<T> catchIt(PromiseHandler<Throwable> onRejected);

    <R> Promise<R> then(PromiseTransformingHandler<R, T> onFulfilled);
    Promise<T> catchIt(PromiseTransformingHandler<T, Throwable> onRejected);
}
