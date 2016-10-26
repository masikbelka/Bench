(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('opportunity-position', {
            parent: 'entity',
            url: '/opportunity-position',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunityPosition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-position/opportunity-positions.html',
                    controller: 'OpportunityPositionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunityPosition');
                    $translatePartialLoader.addPart('positionStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('opportunity-position-detail', {
            parent: 'entity',
            url: '/opportunity-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunityPosition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-position/opportunity-position-detail.html',
                    controller: 'OpportunityPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunityPosition');
                    $translatePartialLoader.addPart('positionStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OpportunityPosition', function($stateParams, OpportunityPosition) {
                    return OpportunityPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'opportunity-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('opportunity-position-detail.edit', {
            parent: 'opportunity-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-position/opportunity-position-dialog.html',
                    controller: 'OpportunityPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityPosition', function(OpportunityPosition) {
                            return OpportunityPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-position.new', {
            parent: 'opportunity-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-position/opportunity-position-dialog.html',
                    controller: 'OpportunityPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdTime: null,
                                employeeUpsaId: null,
                                employeeFullName: null,
                                ownerUpsaId: null,
                                ownerFullName: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('opportunity-position', null, { reload: 'opportunity-position' });
                }, function() {
                    $state.go('opportunity-position');
                });
            }]
        })
        .state('opportunity-position.edit', {
            parent: 'opportunity-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-position/opportunity-position-dialog.html',
                    controller: 'OpportunityPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityPosition', function(OpportunityPosition) {
                            return OpportunityPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-position', null, { reload: 'opportunity-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-position.delete', {
            parent: 'opportunity-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-position/opportunity-position-delete-dialog.html',
                    controller: 'OpportunityPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OpportunityPosition', function(OpportunityPosition) {
                            return OpportunityPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-position', null, { reload: 'opportunity-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
