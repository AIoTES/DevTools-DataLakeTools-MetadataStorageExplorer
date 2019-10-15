(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deploy', {
            parent: 'entity',
            url: '/deploy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Deploys'
            },
            views: {
                'content@': {
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploys.html',
                    controller: 'DeployController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('deploy-detail', {
            parent: 'deploy',
            url: '/deploy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Deploy'
            },
            views: {
                'content@': {
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploy-detail.html',
                    controller: 'DeployDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Deploy', function($stateParams, Deploy) {
                	console.log("statparams.id: "+ $stateParams.id);
                    return Deploy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'deploy',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('deploy-detail.edit', {
            parent: 'deploy-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploy-dialog.html',
                    controller: 'DeployDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deploy', function(Deploy) {
                            return Deploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deploy.new', {
            parent: 'deploy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploy-dialog.html',
                    controller: 'DeployDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                params: null,
                                created: null,
                                updated: null,
                                createdBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deploy', null, { reload: 'deploy' });
                }, function() {
                    $state.go('deploy');
                });
            }]
        })
        .state('deploy.edit', {
            parent: 'deploy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploy-dialog.html',
                    controller: 'DeployDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deploy', function(Deploy) {
                            return Deploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deploy', null, { reload: 'deploy' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deploy.delete', {
            parent: 'deploy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'metadata-explorer-app/entities/deployment/deploy-delete-dialog.html',
                    controller: 'DeployDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deploy', function(Deploy) {
                            return Deploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deploy', null, { reload: 'deploy' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
