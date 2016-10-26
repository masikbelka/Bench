(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('billing-type', {
            parent: 'entity',
            url: '/billing-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.billingType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-type/billing-types.html',
                    controller: 'BillingTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billingType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('billing-type-detail', {
            parent: 'entity',
            url: '/billing-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.billingType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-type/billing-type-detail.html',
                    controller: 'BillingTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billingType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BillingType', function($stateParams, BillingType) {
                    return BillingType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'billing-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('billing-type-detail.edit', {
            parent: 'billing-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-type/billing-type-dialog.html',
                    controller: 'BillingTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillingType', function(BillingType) {
                            return BillingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billing-type.new', {
            parent: 'billing-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-type/billing-type-dialog.html',
                    controller: 'BillingTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                upsaId: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('billing-type', null, { reload: 'billing-type' });
                }, function() {
                    $state.go('billing-type');
                });
            }]
        })
        .state('billing-type.edit', {
            parent: 'billing-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-type/billing-type-dialog.html',
                    controller: 'BillingTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillingType', function(BillingType) {
                            return BillingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-type', null, { reload: 'billing-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billing-type.delete', {
            parent: 'billing-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-type/billing-type-delete-dialog.html',
                    controller: 'BillingTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BillingType', function(BillingType) {
                            return BillingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-type', null, { reload: 'billing-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
