noo-promise
===========

A minimal promise library for GWT that wraps the native promise if exists, or emulate promises using immediates


Running the test server
=======================
You can execute a test server that will help development. In a shell execute
```Shell
gradlew runTestServer
```

This will start the SuperDevMode including the test sources.

Navigate to
http://localhost:9876/noo.promisetest.PromiseTest/jasmine/runner.html
and see the passing tests