(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Model', Model);

    Model.$inject = ['$resource', 'DateUtils'];

    function Model ($resource, DateUtils) {
    	
    	//local
    	//var resourceUrl =  'api/models/:id';
        
    	// external service
    	var resourceUrl =  'http://localhost:8081/api/models/:id';

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
