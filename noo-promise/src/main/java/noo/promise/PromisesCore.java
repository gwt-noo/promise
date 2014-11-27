package noo.promise;

/**
 * @author Tal Shani
 */
abstract class PromisesCore {
    abstract <T> Promise<T> create(PromiseResolver<T> resolver);

    abstract String getImplementationName();

    abstract <T> Promise<T> resolve(T value);
}
