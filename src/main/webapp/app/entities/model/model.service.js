(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Model', Model);

    Model.$inject = ['$resource', 'DateUtils'];

    function Model ($resource, DateUtils) {
        var resourceUrl =  'api/models/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                        data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
