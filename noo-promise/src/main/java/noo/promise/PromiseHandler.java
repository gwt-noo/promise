package noo.promise;

/**
 * @author Tal Shani
 */
public interface PromiseHandler<T> {
    void handle(T value);
}
