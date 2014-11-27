package example.client;

import com.google.gwt.core.client.EntryPoint;
import noo.promise.*;

/**
 * Created by Tal Shani
 */
public class ExampleEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Promise<String> promise1 = Promises.create(new PromiseResolver<String>() {
            @Override
            public void resolve(PromiseCallback<String> callback) {
                callback.resolveValue("xx");
            }
        });

        Promise<Object> then = promise1.then(new PromiseTransformingHandler<Object, String>() {
            @Override
            public PromiseOrValue<Object> handle(String value) {
                return null;
            }
        });

        promise1.catchIt(new PromiseHandler<Throwable>() {
            @Override
            public void handle(Throwable value) {

            }
        });
    }
}
