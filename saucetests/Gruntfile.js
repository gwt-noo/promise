var _ = require('underscore');

module.exports = function (grunt) {
    //var request = require('request');

    var browsersSpec = {
        'firefox': {
            'XP': ['4..33', 'beta', 'dev']
        },
        'internet explorer': {
            //'XP': ['6..8'],
            'Windows 7': ['10..9'],
            'Windows 8.1': ['11']
        },
        'chrome': {
            'Windows 8.1': ['26..38', 'beta', 'dev']
        }
    };

    var browsers = [];
    _.each(browsersSpec, function (item, browser) {
        _.each(item, function (item, platform) {
            _.each(item, function (version) {
                if (version.split('..').length == 2) {
                    var a = parseInt(version.split('..')[0]);
                    var b = parseInt(version.split('..')[1]);
                    var i = Math.min(a, b);
                    for (; i <= Math.max(a, b); i++) {
                        addBroswer(browser, platform, '' + i);
                    }
                } else {
                    addBroswer(browser, platform, version);
                }
            });
        });
    });

    function addBroswer(browser, platform, version) {
        browsers.push({
            browserName: browser,
            version: version,
            platform: platform
        })
    }


    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        'saucelabs-jasmine': {
            all: {
                options: {
                    username: 'promise',
                    key: 'f208f235-2e34-449b-aeac-77f00b2d68c0',
                    urls: ['http://gwt-noo.github.io/promise/noo.promisetest.PromiseTest/jasmine/runner.html'],
                    build: '0.0.1',
                    testname: 'Sauce Unit Test on gh-pages',
                    tunneled: false,
                    browsers: browsers
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-saucelabs');

    // Default task(s).
    grunt.registerTask('default', ['saucelabs-jasmine']);

};