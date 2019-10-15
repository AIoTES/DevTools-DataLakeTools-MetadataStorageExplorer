(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DeployDialogController', DeployDialogController);

    DeployDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deploy', '$rootScope'];

    function DeployDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Deploy, $rootScope) {
        var vm = this;

        vm.deploy = entity;
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

            if (vm.deploy.id) {
            	console.log("Deployment id != null -- " + vm.deploy.id);
            	console.log("Deployment id != null -- " + vm.deploy.name);
//            	vm.model.deviceID = vm.model.name;
            	Deploy.update(vm.deploy, onSaveSuccess, onSaveError);
            } else {
            	vm.deploy.id = vm.deploy.name;
            	console.log("Deployment id == null");
            	Deploy.save(vm.deploy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
        	console.log("onSaveSuccess: " + JSON.stringify(result));
            $scope.$emit('dataLakeToolApp:deployUpdate', result);
            $rootScope.$emit('dataLakeToolApp:deployUpdateEvent');
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
