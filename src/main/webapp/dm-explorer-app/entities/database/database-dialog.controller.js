(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DatabaseDialogController', DatabaseDialogController);

    DatabaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Database'];

    function DatabaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Database) {
        var vm = this;

        vm.database = entity;
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
            if (vm.database.id !== null) {
                Database.update(vm.database, onSaveSuccess, onSaveError);
            } else {
                Database.save(vm.database, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dataLakeToolApp:databaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
