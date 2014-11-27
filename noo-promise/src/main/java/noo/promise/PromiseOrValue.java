package noo.promise;

/**
 * @author Tal Shani
 */
public class PromiseOrValue<T> {
    static <R> PromiseOrValue<R> promise(Promise<R> promise) {
        return new PromiseOrValue<R>(promise, null, true);
    }

    static <R> PromiseOrValue<R> value(R value) {
        return new PromiseOrValue<R>(null, value, false);
    }
    private final boolean isPromise;
    private final Promise<T> promise;
    private final T value;

    private PromiseOrValue(Promise<T> promise, T value, boolean isPromise) {
        this.promise = promise;
        this.value = value;
        this.isPromise = isPromise;
    }

    public boolean isPromise() {return isPromise;}

    public boolean isValue() {return !isPromise;}

    public T getValue() {
        return value;
    }

    public Promise<T> getPromise() {
        return promise;
    }

    public Object getObject() {
        if(isValue()) return value;
        return promise;
    }
}
