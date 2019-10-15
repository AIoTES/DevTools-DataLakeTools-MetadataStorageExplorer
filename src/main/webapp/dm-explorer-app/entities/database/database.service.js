(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Database', Database);

    Database.$inject = ['$resource'];

    function Database ($resource) {
        var resourceUrl =  'api/databases/:id';

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
