(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('production-status', {
            parent: 'entity',
            url: '/production-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.productionStatus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/production-status/production-statuses.html',
                    controller: 'ProductionStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productionStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('production-status-detail', {
            parent: 'entity',
            url: '/production-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.productionStatus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/production-status/production-status-detail.html',
                    controller: 'ProductionStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productionStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductionStatus', function($stateParams, ProductionStatus) {
                    return ProductionStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'production-status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('production-status-detail.edit', {
            parent: 'production-status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-status/production-status-dialog.html',
                    controller: 'ProductionStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductionStatus', function(ProductionStatus) {
                            return ProductionStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('production-status.new', {
            parent: 'production-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-status/production-status-dialog.html',
                    controller: 'ProductionStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('production-status', null, { reload: 'production-status' });
                }, function() {
                    $state.go('production-status');
                });
            }]
        })
        .state('production-status.edit', {
            parent: 'production-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-status/production-status-dialog.html',
                    controller: 'ProductionStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductionStatus', function(ProductionStatus) {
                            return ProductionStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('production-status', null, { reload: 'production-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('production-status.delete', {
            parent: 'production-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-status/production-status-delete-dialog.html',
                    controller: 'ProductionStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductionStatus', function(ProductionStatus) {
                            return ProductionStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('production-status', null, { reload: 'production-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
