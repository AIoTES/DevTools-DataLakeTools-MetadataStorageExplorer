(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .constant('errorConstants', (function() {
            var problemBaseUrl = 'http://www.simlife.tech/problem';
            return {
                EMAIL_ALREADY_USED_TYPE: problemBaseUrl + '/email-already-used',
                LOGIN_ALREADY_USED_TYPE: problemBaseUrl + '/login-already-used',
                EMAIL_NOT_FOUND_TYPE: problemBaseUrl + '/email-not-found'
            }
        })());
})();
