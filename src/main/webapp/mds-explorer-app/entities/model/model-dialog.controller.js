(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('ModelDialogController', ModelDialogController);

    ModelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Model'];

    function ModelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Model) {
        var vm = this;

        vm.model = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            var params = JSON.parse(vm.model.params);
            vm.model.params = params;

            if (vm.model.modelID !== null) {

                Model.update(vm.model, onSaveSuccess, onSaveError);
            } else {
                Model.save(vm.model, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dataLakeToolApp:modelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
