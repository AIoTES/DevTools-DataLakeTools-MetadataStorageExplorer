(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DatabaseDeleteController',DatabaseDeleteController);

    DatabaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Database', '$rootScope'];

    function DatabaseDeleteController($uibModalInstance, entity, Database, $rootScope) {
        var vm = this;

        vm.database = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	
            Database.delete({},
                function () {
                    $uibModalInstance.close(true);
                    $rootScope.$emit('dataLakeToolApp:databaseUpdateEvent');
                });
        }
    }
})();
