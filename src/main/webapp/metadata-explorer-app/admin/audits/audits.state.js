(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('audits', {
            parent: 'admin',
            url: '/audits',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Audits'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/admin/audits/audits.html',
                    controller: 'AuditsController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();