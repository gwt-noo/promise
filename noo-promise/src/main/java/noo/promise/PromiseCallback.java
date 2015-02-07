package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseCallback<T> {
    void resolveValue(T value);

    void resolvePromise(Promise<T> value);

    void reject(Throwable reason);
}
