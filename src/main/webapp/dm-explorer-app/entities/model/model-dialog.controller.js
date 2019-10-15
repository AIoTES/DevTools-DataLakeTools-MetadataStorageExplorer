(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('ModelDialogController', ModelDialogController);

    ModelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Model', '$rootScope'];

    function ModelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Model, $rootScope) {
        var vm = this;

        vm.model = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        var params = JSON.stringify(vm.model.modelParams);
        vm.model.modelParams = params;
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            var params = JSON.parse(vm.model.modelParams);
            vm.model.modelParams = params;

            if (vm.model.modelID) {
            	console.log("model id != null -- " + vm.model.modelID);
            	console.log("model id != null -- " + vm.model.name);
                Model.update(vm.model, onSaveSuccess, onSaveError);
            } else {
            	vm.model.modelID = vm.model.name;
            	console.log("model id == null");
                Model.save(vm.model, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
        	console.log("onSaveSuccess: " + JSON.stringify(result));
            $scope.$emit('dataLakeToolApp:modelUpdate', result);
            $rootScope.$emit('dataLakeToolApp:modelUpdateEvent');
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
