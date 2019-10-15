(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('SchemaSearch', SchemaSearch);

    SchemaSearch.$inject = ['$resource'];

    function SchemaSearch($resource) {
        var resourceUrl =  'api/_search/schemata/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
