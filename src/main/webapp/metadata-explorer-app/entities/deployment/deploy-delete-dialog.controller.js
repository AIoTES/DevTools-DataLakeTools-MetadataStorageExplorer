(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DeployDeleteController',DeployDeleteController);

    DeployDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deploy', '$rootScope'];

    function DeployDeleteController($uibModalInstance, entity, Deploy, $rootScope) {
        var vm = this;

        vm.deploy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	Deploy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    $rootScope.$emit('dataLakeToolApp:deployUpdateEvent');
                });
        }
    }
})();
