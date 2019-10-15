(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DatabaseDetailController', DatabaseDetailController);

    DatabaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Database', 'Table'];

    function DatabaseDetailController($scope, $rootScope, $stateParams, previousState, entity, Database, Table) {
        var vm = this;

        vm.database = entity;
        vm.previousState = previousState.name;
        vm.selectedTable = {};

        var unsubscribe = $rootScope.$on('dataLakeToolApp:databaseUpdate', function(event, result) {
            vm.database = result;
        });
        $scope.$on('$destroy', unsubscribe);



         $scope.cmOption = {
                                          lineNumbers: true,
                                          indentWithTabs: true,
                                          mode:'sql'
                                        };

         var mockData = {
                          "data": [
                            {
                              "@graph": [],
                              "@context": {
                                "ns": "http://ontology.universaal.org/PhThing.owl#",
                                "owl": "http://www.w3.org/2002/07/owl#",
                                "InterIoTMsg": "http://inter-iot.eu/message/",
                                "InterIoTInst": "http://inter-iot.eu/inst/",
                                "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                                "xsd": "http://www.w3.org/2001/XMLSchema#",
                                "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
                                "InterIoT": "http://inter-iot.eu/",
                                "ns2": "http://ontology.universaal.org/Measurement.owl#",
                                "ns1": "http://ontology.universAAL.org/SimpleHealthclient.owl#",
                                "ns4": "http://ontology.universAAL.org/Device.owl#",
                                "ns3": "http://ontology.universaal.org/HealthMeasurement.owl#"
                              }
                            }
                          ]
                        };

         document.getElementById("data_textview").innerHTML = JSON.stringify(mockData, undefined, 2);

         $scope.$on('$destroy', unsubscribe);

         $scope.cmModel = '';


          $scope.selectTable = function() {
//        	  console.log("Table Selected: " + JSON.stringify(vm.selectedTable.table));
          }

          loadTableOfTheDatabase();

          function loadTableOfTheDatabase () {
                         Table.query({
                             page: 0,
                             size: 20,
                             sort: sort(),
                             db: vm.database.db
                         }, onSuccess, onError);
                     function sort() {
                         var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                         if (vm.predicate !== 'id') {
                             result.push('id');
                         }
                         return result;
                     }
                     function onSuccess(data, headers) {
                         vm.tables = data;
                     }
                     function onError(error) {
                         AlertService.error(error.data.message);
                     }
          }
          
          
          $scope.runQuery = function() {
              
        	  //console.log(JSON.stringify(vm.database.db) + " --- " + $scope.cmModel);
        	  //console.log("Table Selected: " + JSON.stringify(vm.selectedTable.table));
        	  
        	  Table.get({id : vm.selectedTable.table, db : vm.database.db, query: $scope.cmModel}, onSuccess, onError);
        	  
        	  function onSuccess(data, headers) {
                 //console.log("Data after Querying: " + JSON.stringify(data));
                 document.getElementById("data_textview").innerHTML = JSON.stringify(data.data, undefined, 2);
              }
              function onError(error) {
                  AlertService.error(error.data.message);
              }
        	  
          };



    }
})();
