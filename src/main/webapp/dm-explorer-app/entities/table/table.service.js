(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Table', Table);

    Table.$inject = ['$resource'];

    function Table ($resource) {
        var resourceUrl =  'api/tables/:id';

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
