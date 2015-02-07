package noo.promisetest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DescribeCallback;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.Jasmine;
import noo.testing.jasmine.client.JasmineCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 * @author Tal Shani
 */
public class TestEntry implements EntryPoint {
    private Logger logger = Logger.getLogger("test");

    @Override
    public void onModuleLoad() {
        Promises.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                logger.log(Level.WARNING, "Uncaught exception", e);
            }
        });
        Jasmine.setDefaultTimeoutInterval(500);
        ((SpecRegistry) GWT.create(SpecRegistry.class)).registerTests();
    }


}
