noo-promise
===========

A minimal promise library for GWT that wraps the native promise if exists, or emulate promises using immediates

[![Sauce Test Status](https://saucelabs.com/browser-matrix/promise.svg)](https://saucelabs.com/u/promise)

Running the test server
=======================
You can execute a test server that will help development. In a shell execute
```Shell
gradlew runTestServer
```

This will start the SuperDevMode including the test sources.

Navigate to
```
http://localhost:9876/noo.promisetest.PromiseTest/jasmine/runner.html
```
and see the passing tests

# Building and deploying

Just run
```Shell
gradlew :noo-promise:bintrayUpload
```

# Maven / Gradle

The repository is located at
```
http://dl.bintray.com/gwt-noo/maven
```

Artifact
```
<dependency>
        <groupId>com.github.gwt-noo</groupId>
        <artifactId>noo-promise</artifactId>
        <version>VERSION</version>
        <type>jar</type>
</dependency>
```
or
```
compile(group: 'com.github.gwt-noo', name: 'noo-promise', version: 'VERSION')
```

# Change log

## 0.1.0
 - Initial version
