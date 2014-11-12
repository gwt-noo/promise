package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseResolver<T> {
    void resolve(PromiseCallback<T> callback);
}
