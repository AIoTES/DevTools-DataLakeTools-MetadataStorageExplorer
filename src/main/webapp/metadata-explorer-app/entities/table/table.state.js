(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('table', {
            parent: 'entity',
            url: '/table?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tables'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/entities/table/tables.html',
                    controller: 'TableController',
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
        .state('table-detail', {
            parent: 'table',
            url: '/table/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Table'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/entities/table/table-detail.html',
                    controller: 'TableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Table', function($stateParams, Table) {
                    return Table.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'table',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('table-detail.edit', {
            parent: 'table-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/table/table-dialog.html',
                    controller: 'TableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Table', function(Table) {
                            return Table.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('table.new', {
            parent: 'table',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/table/table-dialog.html',
                    controller: 'TableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                db: null,
                                table: null,
                                data: null,
                                query: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('table', null, { reload: 'table' });
                }, function() {
                    $state.go('table');
                });
            }]
        })
        .state('table.edit', {
            parent: 'table',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/table/table-dialog.html',
                    controller: 'TableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Table', function(Table) {
                            return Table.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('table', null, { reload: 'table' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('table.delete', {
            parent: 'table',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/table/table-delete-dialog.html',
                    controller: 'TableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Table', function(Table) {
                            return Table.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('table', null, { reload: 'table' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
