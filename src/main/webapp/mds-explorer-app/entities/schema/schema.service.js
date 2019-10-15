(function() {
    'use strict';
    angular
        .module('dataLakeToolApp')
        .factory('Schema', Schema);

    Schema.$inject = ['$resource'];

    function Schema ($resource) {
       
//    	var resourceUrl =  'api/schemata/:id';
//
//        return $resource(resourceUrl, {}, {
//            'query': { 
//            	method: 'GET', 
//            	isArray: true,
//            	transformResponse: function (data) {
//                  if (data) {
//                  	data = angular.fromJson(data);
////                  	console.log("Schema QUERY Data Response: " + JSON.stringify(data));
//                  }
//                  return data;
//              }
//            },
//            'get': {
//                method: 'GET',
//                transformResponse: function (data) {
//                    if (data) {
//                        data = angular.fromJson(data);
//                        console.log("Schema GET Data Response: " + JSON.stringify(data));
//                    }
//                    return data;
//                }
//            },
//            'update': { method:'PUT' }
//        });
        
        
        var resourceUrl =  'http://localhost:4570/getSchema';

        return $resource(resourceUrl, {}, {
            'query': { 
            	method: 'POST', 
            	isArray: true,
            	transformRequest: {
                    body: function(data) {
                    	
//                    	console.log("Schema QUERY Data Request: " + JSON.stringify(data));
                    	
                    	data.db = "";
                    	data = angular.fromJson(data);
                    	
//                    	console.log("Schema QUERY Data Request Modified: " + JSON.stringify(data));
                    	
                    	return JSON.stringify(data);
                    }
            	},
            	transformResponse: function (data) {
                    if (data) {
//                    	console.log("Schema QUERY Data Response: " + JSON.stringify(data));
                    	
                    	data = JSON.parse(data);
                    	
                    	var schemaList = Object.keys(data);
                    	
                    	var newJson = [];
                    	
                    	var i = 0;
                    	schemaList.forEach(function(value) {
                    		
                    		var schemaId = value;
//                    		console.log("Schema: " + schemaId);
                    		
                    		var schema = new Object();
                    		schema.id = schemaId;
                    		schema.entity = schemaId;
                    		
                    		newJson[i] = schema;
                    		
//                    		var tableList = data[value];
//                    		console.log("Tables: " + JSON.stringify(tableList));
                    		i++;
                    	});
                    	
                    	
//                    	console.log("Schema QUERY Response Modified: " + JSON.stringify(newJson));
                    	
                    	
                    	newJson = angular.fromJson(newJson);
                    }
                    return newJson;
                }
            },
            'get': {
                method: 'POST',
                transformRequest: {
                    body: function(data) {
                    	
                    	console.log("Schema GET Data Request: " + JSON.stringify(data));
                    	
                    	data.db = data.id;
                    	delete data["id"];
                    	
                    	data = angular.fromJson(data);
                    	
                    	console.log("Schema GET Data Request Modified: " + JSON.stringify(data));
                    	
                    	return JSON.stringify(data);
                    }
            	},
                transformResponse: function (data) {
                    if (data) {
                    	
                    	console.log("Schema GET Data Response: " + JSON.stringify(data));
                    	
                    	data = JSON.parse(data);
                    	var schemaList = Object.keys(data);
                    	
                    	var schema = new Object();
                    	schemaList.forEach(function(value) {
                    		
                    		var schemaId = value;
                    		console.log("Schema: " + schemaId);
                    		
                    		schema.id = schemaId;
                    		schema.entity = schemaId;
                    		
                    		var tableList = data[value];
                    		console.log("Tables: " + JSON.stringify(tableList));
                    		
                    		schema.properties = tableList;
                    		
                    	});
                    	
                    	console.log("Schema GET Data Response Modified: " + JSON.stringify(schema));
                    	
                    	schema = angular.fromJson(schema);
                    }
                    return schema;
                }
            },
            'save': {
            	method:'POST',
            	url: 'http://localhost:20085/services',
            	transformRequest: {
                    body: function(data) {
                    	
//                    	console.log("Save Data: " + JSON.stringify(data));
                    	
                    	var newJson = new Object();
                    	
                    	newJson.id = data.entity;
                    	newJson.type = "independent-storage";
                    	newJson.url = "http://ids:4567/";
                    	
                    	
//                    	console.log("Save Request Modified: " + JSON.stringify(newJson));
                    	
                    	newJson = angular.fromJson(newJson);
                    	
                    	return JSON.stringify(newJson);
                    }
            	}	
            },
            'update': { method:'PUT' }
        });
        
        
    }
})();
