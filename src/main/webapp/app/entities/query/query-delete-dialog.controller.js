(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('QueryDeleteController',QueryDeleteController);

    QueryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Query'];

    function QueryDeleteController($uibModalInstance, entity, Query) {
        var vm = this;

        vm.query = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Query.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
