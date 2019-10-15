(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('ModelSearch', ModelSearch);

    ModelSearch.$inject = ['$resource'];

    function ModelSearch($resource) {
        var resourceUrl =  'http://localhost:8081/api/_search/devices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
