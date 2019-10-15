(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('TableDetailController', TableDetailController);

    TableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Table'];

    function TableDetailController($scope, $rootScope, $stateParams, previousState, entity, Table) {
        var vm = this;

        vm.table = entity;
        vm.previousState = previousState.name;

         $scope.cmOption = {
                                  lineNumbers: true,
                                  indentWithTabs: true,
                                  mode:'sql'
                                };

         
         document.getElementById("data_textview").innerHTML = JSON.stringify(vm.table.data, undefined, 2);


        var unsubscribe = $rootScope.$on('dataLakeToolApp:tableUpdate', function(event, result) {
            vm.table = result;
        });
        $scope.$on('$destroy', unsubscribe);

           // Initial code content...
           $scope.cmModel = '';
           
           $scope.runQuery = function() {
               
         	  //console.log(JSON.stringify(vm.table.db) + " --- " + $scope.cmModel);
         	  //console.log("Table Selected: " + JSON.stringify(vm.table.table));
         	  
         	  Table.get({id : vm.table.table, db : vm.table.db, query: $scope.cmModel}, onSuccess, onError);
         	  
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
