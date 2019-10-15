(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SchemaDeleteController',SchemaDeleteController);

    SchemaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Schema', '$rootScope'];

    function SchemaDeleteController($uibModalInstance, entity, Schema, $rootScope) {
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
                    $rootScope.$emit('dataLakeToolApp:schemaUpdateEvent');
                });
        }
    }
})();
