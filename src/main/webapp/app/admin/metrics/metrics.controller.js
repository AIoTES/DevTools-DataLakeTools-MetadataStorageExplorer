(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SimMetricsMonitoringController', SimMetricsMonitoringController);

    SimMetricsMonitoringController.$inject = ['$scope','SimMetricsService', '$uibModal'];

    function SimMetricsMonitoringController ($scope, SimMetricsService, $uibModal) {
        var vm = this;

        vm.metrics = {};
        vm.refresh = refresh;
        vm.refreshThreadDumpData = refreshThreadDumpData;
        vm.servicesStats = {};
        vm.updatingMetrics = true;

        vm.refresh();

        $scope.$watch('vm.metrics', function (newValue) {
            vm.servicesStats = {};
            angular.forEach(newValue.timers, function (value, key) {
                if (key.includes('web.rest') || key.includes('service')) {
                    vm.servicesStats[key] = value;
                }
            });

        });

        function refresh () {
            vm.updatingMetrics = true;
            SimMetricsService.getMetrics().then(function (promise) {
                vm.metrics = promise;
                vm.updatingMetrics = false;
            }, function (promise) {
                vm.metrics = promise.data;
                vm.updatingMetrics = false;
            });
        }

        function refreshThreadDumpData () {
            SimMetricsService.threadDump().then(function(data) {
                $uibModal.open({
                    templateUrl: 'app/admin/metrics/metrics.modal.html',
                    controller: 'SimMetricsMonitoringModalController',
                    controllerAs: 'vm',
                    size: 'lg',
                    resolve: {
                        threadDump: function() {
                            return data;
                        }

                    }
                });
            });
        }


    }
})();
