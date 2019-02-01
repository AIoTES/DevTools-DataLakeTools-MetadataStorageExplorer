// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g, '..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/webapp/bower_components/jquery/dist/jquery.js',
            'src/main/webapp/bower_components/json3/lib/json3.js',
            'src/main/webapp/bower_components/messageformat/messageformat.js',
            'src/main/webapp/bower_components/bootstrap-slider/bootstrap-slider.js',
            'src/main/webapp/bower_components/datatables.net/js/jquery.dataTables.js',
            'src/main/webapp/bower_components/datatables.net-bs/js/dataTables.bootstrap.js',
            'src/main/webapp/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js',
            'src/main/webapp/bower_components/moment/moment.js',
            'src/main/webapp/bower_components/fastclick/lib/fastclick.js',
            'src/main/webapp/bower_components/Flot/jquery.flot.js',
            'src/main/webapp/bower_components/fullcalendar/dist/fullcalendar.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/inputmask.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/inputmask.extensions.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/inputmask.date.extensions.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/inputmask.numeric.extensions.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/inputmask.phone.extensions.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/jquery.inputmask.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/global/document.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/global/window.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/phone-codes/phone.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/phone-codes/phone-be.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/phone-codes/phone-nl.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/phone-codes/phone-ru.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/phone-codes/phone-uk.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/dependencyLibs/inputmask.dependencyLib.jqlite.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/dependencyLibs/inputmask.dependencyLib.jquery.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/dependencyLibs/inputmask.dependencyLib.js',
            'src/main/webapp/bower_components/inputmask/dist/inputmask/bindings/inputmask.binding.js',
            'src/main/webapp/bower_components/ion.rangeSlider/js/ion.rangeSlider.js',
            'src/main/webapp/bower_components/jquery-knob/js/jquery.knob.js',
            'src/main/webapp/bower_components/eve-raphael/eve.js',
            'src/main/webapp/bower_components/mocha/mocha.js',
            'src/main/webapp/bower_components/PACE/pace.js',
            'src/main/webapp/bower_components/select2/dist/js/select2.js',
            'src/main/webapp/bower_components/jquery-slimscroll/jquery.slimscroll.js',
            'src/main/webapp/bower_components/jquery-slimscroll/jquery.slimscroll.min.js',
            'src/main/webapp/bower_components/bootstrap-timepicker/js/bootstrap-timepicker.js',
            'src/main/webapp/bower_components/jquery-sparkline/dist/jquery.sparkline.js',
            'src/main/webapp/bower_components/jquery-ui/jquery-ui.js',
            'src/main/webapp/bower_components/codemirror/lib/codemirror.js',
            'src/main/webapp/bower_components/angular/angular.js',
            'src/main/webapp/bower_components/angular-aria/angular-aria.js',
            'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'src/main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'src/main/webapp/bower_components/ngstorage/ngStorage.js',
            'src/main/webapp/bower_components/angular-loading-bar/build/loading-bar.js',
            'src/main/webapp/bower_components/angular-resource/angular-resource.js',
            'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webapp/bower_components/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
            'src/main/webapp/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/webapp/bower_components/bootstrap-daterangepicker/daterangepicker.js',
            'src/main/webapp/bower_components/raphael/raphael.min.js',
            'src/main/webapp/bower_components/angular-ui-codemirror/ui-codemirror.js',
            'src/main/webapp/bower_components/angular-highlightjs/build/angular-highlightjs.js',
            'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
            'src/main/webapp/bower_components/morris.js/morris.js',
            // endbower
            'src/main/webapp/app/app.module.js',
            'src/main/webapp/app/app.state.js',
            'src/main/webapp/app/app.constants.js',
            'src/main/webapp/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf|protractor.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: ['src/test/javascript/e2e/**'],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout: 10000, // default 2000
        browserDisconnectTolerance: 1, // default 0
        browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
    });
};