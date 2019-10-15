(function() {
    'use strict';

    angular
        .module('dataLakeToolApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
             'ui.codemirror',
            'hljs',
            // simlife-needle-angularjs-add-module Simlife will add new module here
            'angular-loading-bar'
        ]).config(['hljsServiceProvider',
            function (hljsServiceProvider) {
               hljsServiceProvider.setOptions({
                            // replace tab with 4 spaces
                            tabReplace: '    '
                          });
            }])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }


//.config(['$routeProvider', 'exampleRoutes',
//function ($routeProvider,   exampleRoutes) {
//
//}

})();
