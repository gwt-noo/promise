package noo.promisetest;

import noo.testing.jasmine.client.DoneCallback;

import java.util.HashMap;
import java.util.Map;

import static noo.testing.jasmine.client.Jasmine.expect;

/**
 * @author Tal Shani
 */
class ItemsAssertion {
    private final DoneCallback done;
    private final Map<String, Boolean> map;

    ItemsAssertion(DoneCallback done, String... items) {
        this.done = done;
        map = new HashMap<String, Boolean>(items.length);
        for (String item : items) {
            map.put(item, false);
        }

    }

    public void mark(String item) {
        expect(map.containsKey(item)).toEqual(true);
        expect(map.get(item)).toEqual(false);
        map.put(item, true);
        for (Boolean val : map.values()) {
            if (!val) return;
        }
        done.execute();
    }
}
