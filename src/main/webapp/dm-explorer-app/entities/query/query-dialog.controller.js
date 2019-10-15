(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('QueryDialogController', QueryDialogController);

    QueryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Query'];

    function QueryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Query) {
        var vm = this;

        vm.query = entity;
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
            if (vm.query.id !== null) {
                Query.update(vm.query, onSaveSuccess, onSaveError);
            } else {
                Query.save(vm.query, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dataLakeToolApp:queryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
