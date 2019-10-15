(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SimHealthCheckController', SimHealthCheckController);

    SimHealthCheckController.$inject = ['SimHealthService', '$uibModal'];

    function SimHealthCheckController (SimHealthService, $uibModal) {
        var vm = this;

        vm.updatingHealth = true;
        vm.getLabelClass = getLabelClass;
        vm.refresh = refresh;
        vm.showHealth = showHealth;
        vm.baseName = SimHealthService.getBaseName;
        vm.subSystemName = SimHealthService.getSubSystemName;

        vm.refresh();

        function getLabelClass (statusState) {
            if (statusState === 'UP') {
                return 'label-success';
            } else {
                return 'label-danger';
            }
        }

        function refresh () {
            vm.updatingHealth = true;
            SimHealthService.checkHealth().then(function (response) {
                vm.healthData = SimHealthService.transformHealthData(response);
                vm.updatingHealth = false;
            }, function (response) {
                vm.healthData =  SimHealthService.transformHealthData(response.data);
                vm.updatingHealth = false;
            });
        }

        function showHealth (health) {
            $uibModal.open({
                templateUrl: 'dm-explorer-app/admin/health/health.modal.html',
                controller: 'HealthModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    currentHealth: function() {
                        return health;
                    },
                    baseName: function() {
                        return vm.baseName;
                    },
                    subSystemName: function() {
                        return vm.subSystemName;
                    }

                }
            });
        }

    }
})();
