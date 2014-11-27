package noo.promisetest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import noo.promise.*;
import noo.testing.jasmine.client.DescribeCallback;
import noo.testing.jasmine.client.DoneCallback;
import noo.testing.jasmine.client.Jasmine;
import noo.testing.jasmine.client.JasmineCallback;

import static noo.testing.jasmine.client.Jasmine.*;

/**
 * @author Tal Shani
 */
public class TestEntry implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Jasmine.setDefaultTimeoutInterval(500);
        ((SpecRegistry)GWT.create(SpecRegistry.class)).registerTests();
    }


}
