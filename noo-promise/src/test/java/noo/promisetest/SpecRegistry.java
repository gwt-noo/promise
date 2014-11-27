package noo.promisetest;

import noo.testing.jasmine.client.rebind.JasmineTestRegistry;
import noo.testing.jasmine.client.rebind.TestClasses;

/**
 * @author Tal Shani
 */
@TestClasses({
        ImmediateSimpleSpec.class,
        PromiseSimpleSpec.class
})
public interface SpecRegistry extends JasmineTestRegistry {
}
