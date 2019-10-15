(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('QuerySearch', QuerySearch);

    QuerySearch.$inject = ['$resource'];

    function QuerySearch($resource) {
        var resourceUrl =  'api/_search/queries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
