package noo.promise;

/**
 * A collection of items that have been resolved in Promises.all
 * @author Tal Shani
 */
public interface ResolvedCollection {
    Object get(int i);
    int length();
}
