(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DatabaseDeleteController',DatabaseDeleteController);

    DatabaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Database'];

    function DatabaseDeleteController($uibModalInstance, entity, Database) {
        var vm = this;

        vm.database = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Database.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
