(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SchemaDetailController', SchemaDetailController);

    SchemaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Schema'];

    function SchemaDetailController($scope, $rootScope, $stateParams, previousState, entity, Schema) {
        var vm = this;

        vm.schema = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dataLakeToolApp:schemaUpdate', function(event, result) {
            vm.schema = result;
        });
        $scope.$on('$destroy', unsubscribe);

         $scope.cmOption = {
                                          lineNumbers: true,
                                          indentWithTabs: true,
                                          mode:'sql'
                                        };

                 document.getElementById("data_textview").innerHTML = JSON.stringify(vm.schema.properties, undefined, 2);
                 document.getElementById("properties_textview").innerHTML = JSON.stringify(vm.schema.properties, undefined, 2);




    }
})();
