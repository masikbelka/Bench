(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('billing-concept', {
            parent: 'entity',
            url: '/billing-concept',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.billingConcept.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-concept/billing-concepts.html',
                    controller: 'BillingConceptController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billingConcept');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('billing-concept-detail', {
            parent: 'entity',
            url: '/billing-concept/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.billingConcept.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billing-concept/billing-concept-detail.html',
                    controller: 'BillingConceptDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billingConcept');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BillingConcept', function($stateParams, BillingConcept) {
                    return BillingConcept.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'billing-concept',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('billing-concept-detail.edit', {
            parent: 'billing-concept-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-concept/billing-concept-dialog.html',
                    controller: 'BillingConceptDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillingConcept', function(BillingConcept) {
                            return BillingConcept.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billing-concept.new', {
            parent: 'billing-concept',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-concept/billing-concept-dialog.html',
                    controller: 'BillingConceptDialogController',
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
                    $state.go('billing-concept', null, { reload: 'billing-concept' });
                }, function() {
                    $state.go('billing-concept');
                });
            }]
        })
        .state('billing-concept.edit', {
            parent: 'billing-concept',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-concept/billing-concept-dialog.html',
                    controller: 'BillingConceptDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillingConcept', function(BillingConcept) {
                            return BillingConcept.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-concept', null, { reload: 'billing-concept' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billing-concept.delete', {
            parent: 'billing-concept',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billing-concept/billing-concept-delete-dialog.html',
                    controller: 'BillingConceptDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BillingConcept', function(BillingConcept) {
                            return BillingConcept.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billing-concept', null, { reload: 'billing-concept' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
