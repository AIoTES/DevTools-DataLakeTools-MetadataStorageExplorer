(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('database', {
            parent: 'entity',
            url: '/database?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Databases'
            },
            views: {
                'content@': {
                    templateUrl: 'mds-explorer-app/entities/database/databases.html',
                    controller: 'DatabaseController',
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
        .state('database-detail', {
            parent: 'database',
            url: '/database/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Database'
            },
            views: {
                'content@': {
                    templateUrl: 'mds-explorer-app/entities/database/database-detail.html',
                    controller: 'DatabaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Database', function($stateParams, Database) {
                    return Database.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'database',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('database-detail.edit', {
            parent: 'database-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/database/database-dialog.html',
                    controller: 'DatabaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Database', function(Database) {
                            return Database.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('database.new', {
            parent: 'database',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/database/database-dialog.html',
                    controller: 'DatabaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                db: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('database', null, { reload: 'database' });
                }, function() {
                    $state.go('database');
                });
            }]
        })
        .state('database.edit', {
            parent: 'database',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/database/database-dialog.html',
                    controller: 'DatabaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Database', function(Database) {
                            return Database.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('database', null, { reload: 'database' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('database.delete', {
            parent: 'database',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'mds-explorer-app/entities/database/database-delete-dialog.html',
                    controller: 'DatabaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Database', function(Database) {
                            return Database.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('database', null, { reload: 'database' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
