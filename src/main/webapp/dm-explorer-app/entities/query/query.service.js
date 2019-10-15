(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Query', Query);

    Query.$inject = ['$resource'];

    function Query ($resource) {
        var resourceUrl =  'api/queries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
