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

//        var context = JSON.stringify(vm.model.context);
//        vm.model.context = context;
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
//            var context = JSON.parse(vm.model.context);
//            vm.model.context = context;

            if (vm.model.deviceID) {
            	console.log("Device id != null -- " + vm.model.deviceID);
            	console.log("Device id != null -- " + vm.model.name);
//            	vm.model.deviceID = vm.model.name;
                Model.update(vm.model, onSaveSuccess, onSaveError);
            } else {
            	vm.model.deviceID = vm.model.name;
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
