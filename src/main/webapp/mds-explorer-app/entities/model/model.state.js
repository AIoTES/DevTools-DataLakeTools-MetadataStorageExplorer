(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model', {
            parent: 'entity',
            url: '/model?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Models'
            },
            views: {
                'content@': {
                    templateUrl: 'mds-explorer-app/entities/model/models.html',
                    controller: 'ModelController',
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
        .state('model-detail', {
            parent: 'model',
            url: '/model/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Model'
            },
            views: {
                'content@': {
                    templateUrl: 'mds-explorer-app/entities/model/model-detail.html',
                    controller: 'ModelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Model', function($stateParams, Model) {
                    return Model.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-detail.edit', {
            parent: 'model-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model.new', {
            parent: 'model',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
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
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('model');
                });
            }]
        })
        .state('model.edit', {
            parent: 'model',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/model/model-dialog.html',
                    controller: 'ModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model.delete', {
            parent: 'model',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/model/model-delete-dialog.html',
                    controller: 'ModelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Model', function(Model) {
                            return Model.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model', null, { reload: 'model' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
