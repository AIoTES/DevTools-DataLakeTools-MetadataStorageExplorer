(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .factory('DeploySearch', DeploySearch);

    DeploySearch.$inject = ['$resource'];

    function DeploySearch($resource) {
        var resourceUrl =  'http://localhost:8081/api/_search/deployments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
