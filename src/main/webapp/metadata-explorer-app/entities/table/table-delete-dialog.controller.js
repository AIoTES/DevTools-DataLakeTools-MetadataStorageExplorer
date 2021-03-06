(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('TableDeleteController',TableDeleteController);

    TableDeleteController.$inject = ['$uibModalInstance', 'entity', 'Table'];

    function TableDeleteController($uibModalInstance, entity, Table) {
        var vm = this;

        vm.table = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Table.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
