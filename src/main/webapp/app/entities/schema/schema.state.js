(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('schema', {
            parent: 'entity',
            url: '/schema?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Schemata'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schema/schemata.html',
                    controller: 'SchemaController',
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
        .state('schema-detail', {
            parent: 'schema',
            url: '/schema/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Schema'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schema/schema-detail.html',
                    controller: 'SchemaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Schema', function($stateParams, Schema) {
                    return Schema.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'schema',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('schema-detail.edit', {
            parent: 'schema-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schema/schema-dialog.html',
                    controller: 'SchemaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Schema', function(Schema) {
                            return Schema.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schema.new', {
            parent: 'schema',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schema/schema-dialog.html',
                    controller: 'SchemaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entity: null,
                                properties: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('schema', null, { reload: 'schema' });
                }, function() {
                    $state.go('schema');
                });
            }]
        })
        .state('schema.edit', {
            parent: 'schema',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schema/schema-dialog.html',
                    controller: 'SchemaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Schema', function(Schema) {
                            return Schema.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schema', null, { reload: 'schema' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schema.delete', {
            parent: 'schema',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schema/schema-delete-dialog.html',
                    controller: 'SchemaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Schema', function(Schema) {
                            return Schema.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schema', null, { reload: 'schema' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
