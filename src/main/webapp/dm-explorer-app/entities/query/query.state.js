(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('query', {
            parent: 'entity',
            url: '/query?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Queries'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/entities/query/queries.html',
                    controller: 'QueryController',
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
        .state('query-detail', {
            parent: 'query',
            url: '/query/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Query'
            },
            views: {
                'content@': {
                    templateUrl: 'dm-explorer-app/entities/query/query-detail.html',
                    controller: 'QueryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Query', function($stateParams, Query) {
                    return Query.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'query',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('query-detail.edit', {
            parent: 'query-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/query/query-dialog.html',
                    controller: 'QueryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Query', function(Query) {
                            return Query.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('query.new', {
            parent: 'query',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/query/query-dialog.html',
                    controller: 'QueryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                db: null,
                                table: null,
                                query: null,
                                output: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('query', null, { reload: 'query' });
                }, function() {
                    $state.go('query');
                });
            }]
        })
        .state('query.edit', {
            parent: 'query',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/query/query-dialog.html',
                    controller: 'QueryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Query', function(Query) {
                            return Query.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('query', null, { reload: 'query' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('query.delete', {
            parent: 'query',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'dm-explorer-app/entities/query/query-delete-dialog.html',
                    controller: 'QueryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Query', function(Query) {
                            return Query.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('query', null, { reload: 'query' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
