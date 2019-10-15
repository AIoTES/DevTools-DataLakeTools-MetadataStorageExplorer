(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('sim-health', {
            parent: 'admin',
            url: '/health',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Health Checks'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/admin/health/health.html',
                    controller: 'SimHealthCheckController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
