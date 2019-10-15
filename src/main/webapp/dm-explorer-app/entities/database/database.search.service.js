(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('DatabaseSearch', DatabaseSearch);

    DatabaseSearch.$inject = ['$resource'];

    function DatabaseSearch($resource) {
        var resourceUrl =  'api/_search/databases/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
