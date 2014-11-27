package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseCallback<T> {
    void resolve(PromiseOrValue<T> value);

    void resolveValue(T value);

    void resolvePromise(Promise<T> value);

    void reject(Throwable reason);
}
