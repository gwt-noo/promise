package noo.promise;

import java.util.List;

/**
 * @author Tal Shani
 */
final class ResolvedCollectionJvmImpl implements ResolvedCollection {

    private final List<Object> items;

    public ResolvedCollectionJvmImpl(List<Object> items) {
        this.items = items;
    }

    @Override
    public Object get(int i) {
        return items.get(i);
    }

    @Override
    public int length() {
        return items.size();
    }
}
