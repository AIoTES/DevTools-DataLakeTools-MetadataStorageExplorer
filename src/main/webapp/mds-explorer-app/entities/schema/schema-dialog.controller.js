(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SchemaDialogController', SchemaDialogController);

    SchemaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Schema', '$rootScope'];

    function SchemaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Schema, $rootScope) {
        var vm = this;

        vm.schema = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            var params = JSON.parse(vm.schema.properties);
            vm.schema.properties = params;

            if (vm.schema.id !== null) {
                Schema.update(vm.schema, onSaveSuccess, onSaveError);
            } else {
                Schema.save(vm.schema, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dataLakeToolApp:schemaUpdate', result);
            $rootScope.$emit('dataLakeToolApp:schemaUpdateEvent');
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
