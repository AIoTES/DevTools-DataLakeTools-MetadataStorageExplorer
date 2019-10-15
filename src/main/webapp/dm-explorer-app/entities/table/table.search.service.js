(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('TableSearch', TableSearch);

    TableSearch.$inject = ['$resource'];

    function TableSearch($resource) {
        var resourceUrl =  'api/_search/tables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
