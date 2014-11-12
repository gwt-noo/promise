package noo.promise;

/**
 * @author Tal Shani
 */
abstract class PromisesCore {
    abstract <T> Promise<T> create(PromiseResolver<T> resolver);

    abstract String getImplementationName();
}
