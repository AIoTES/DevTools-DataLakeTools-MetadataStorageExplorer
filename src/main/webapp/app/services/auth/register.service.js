(function () {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
