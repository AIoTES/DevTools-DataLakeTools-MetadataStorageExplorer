(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('ModelDeleteController',ModelDeleteController);

    ModelDeleteController.$inject = ['$uibModalInstance', 'entity', 'Model', '$rootScope'];

    function ModelDeleteController($uibModalInstance, entity, Model, $rootScope) {
        var vm = this;

        vm.model = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Model.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    $rootScope.$emit('dataLakeToolApp:modelUpdateEvent');
                });
        }
    }
})();
