(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('TableDialogController', TableDialogController);

    TableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Table'];

    function TableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Table) {
        var vm = this;

        vm.table = entity;
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
            var data = JSON.parse(vm.table.data);
             vm.table.data = data;

            if (vm.table.id !== null) {
                Table.update(vm.table, onSaveSuccess, onSaveError);
            } else {
                Table.save(vm.table, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dataLakeToolApp:tableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
