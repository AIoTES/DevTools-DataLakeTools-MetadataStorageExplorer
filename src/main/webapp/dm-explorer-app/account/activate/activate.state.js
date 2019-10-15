(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('activate', {
            parent: 'account',
            url: '/activate?key',
            data: {
                authorities: [],
                pageTitle: 'Activation'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/account/activate/activate.html',
                    controller: 'ActivationController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
