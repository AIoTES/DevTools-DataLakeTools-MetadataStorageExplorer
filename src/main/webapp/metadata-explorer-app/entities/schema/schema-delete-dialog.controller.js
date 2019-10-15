(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SchemaDeleteController',SchemaDeleteController);

    SchemaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Schema'];

    function SchemaDeleteController($uibModalInstance, entity, Schema) {
        var vm = this;

        vm.schema = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Schema.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
