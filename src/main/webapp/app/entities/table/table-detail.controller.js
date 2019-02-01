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
           $scope.cmModel = 'SELECT SQL_NO_CACHE DISTINCT';

    }
})();
