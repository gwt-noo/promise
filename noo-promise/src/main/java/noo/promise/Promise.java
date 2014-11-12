package noo.promise;

/**
 * @author Tal Shani
 */
public interface Promise<T> {
    <R> Promise<R> then(PromiseHandler<R, T> handler);
}
