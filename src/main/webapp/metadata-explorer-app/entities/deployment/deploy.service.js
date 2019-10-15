(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Deploy', Deploy);

    Deploy.$inject = ['$resource', 'DateUtils'];

    function Deploy ($resource, DateUtils) {
        
    	var resourceUrl =  'http://localhost:8081/api/deployments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                    	console.log("Data: " + JSON.stringify(data));
                        data = angular.fromJson(JSON.parse(data));
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                        data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    }
                    return data;
                }
            },
            'update': { 
            	method:'PUT',
            	transformResponse: function (data) {
                    if (data) {
                    	data = angular.fromJson(JSON.parse(data));
                    	console.log("Update Response: " + JSON.stringify(data));
                    }
                    return data;
                }
            	}
           // 'save': { 
          //  	method:'POST', 
           // 	transformResponse: function (data) {
           // 		return data;
           // 	}
           // 	}
        });
    }
})();
