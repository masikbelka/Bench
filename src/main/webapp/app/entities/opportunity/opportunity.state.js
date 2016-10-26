(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('opportunity', {
            parent: 'entity',
            url: '/opportunity',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity/opportunities.html',
                    controller: 'OpportunityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunity');
                    $translatePartialLoader.addPart('opportunityStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('opportunity-detail', {
            parent: 'entity',
            url: '/opportunity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity/opportunity-detail.html',
                    controller: 'OpportunityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunity');
                    $translatePartialLoader.addPart('opportunityStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Opportunity', function($stateParams, Opportunity) {
                    return Opportunity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'opportunity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('opportunity-detail.edit', {
            parent: 'opportunity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity.new', {
            parent: 'opportunity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                staffingId: null,
                                name: null,
                                ownerUpsaId: null,
                                ownerFullName: null,
                                startDate: null,
                                endDate: null,
                                status: null,
                                description: null,
                                staffingCoordinatiorUpsaId: null,
                                staffingCoordinatiorFullName: null,
                                responsibleManagerUpsaId: null,
                                responsibleManagerFullName: null,
                                supervisorUpsaId: null,
                                supervisorFullName: null,
                                deliveryManagerUpsaId: null,
                                deliveryManagerFullName: null,
                                accountManagerUpsaId: null,
                                accountManagerFullName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('opportunity');
                });
            }]
        })
        .state('opportunity.edit', {
            parent: 'opportunity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity.delete', {
            parent: 'opportunity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-delete-dialog.html',
                    controller: 'OpportunityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
