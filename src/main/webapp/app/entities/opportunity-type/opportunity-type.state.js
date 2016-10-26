(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('opportunity-type', {
            parent: 'entity',
            url: '/opportunity-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunityType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-type/opportunity-types.html',
                    controller: 'OpportunityTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunityType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('opportunity-type-detail', {
            parent: 'entity',
            url: '/opportunity-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.opportunityType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-type/opportunity-type-detail.html',
                    controller: 'OpportunityTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('opportunityType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OpportunityType', function($stateParams, OpportunityType) {
                    return OpportunityType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'opportunity-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('opportunity-type-detail.edit', {
            parent: 'opportunity-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-type/opportunity-type-dialog.html',
                    controller: 'OpportunityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityType', function(OpportunityType) {
                            return OpportunityType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-type.new', {
            parent: 'opportunity-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-type/opportunity-type-dialog.html',
                    controller: 'OpportunityTypeDialogController',
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
                    $state.go('opportunity-type', null, { reload: 'opportunity-type' });
                }, function() {
                    $state.go('opportunity-type');
                });
            }]
        })
        .state('opportunity-type.edit', {
            parent: 'opportunity-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-type/opportunity-type-dialog.html',
                    controller: 'OpportunityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityType', function(OpportunityType) {
                            return OpportunityType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-type', null, { reload: 'opportunity-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-type.delete', {
            parent: 'opportunity-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-type/opportunity-type-delete-dialog.html',
                    controller: 'OpportunityTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OpportunityType', function(OpportunityType) {
                            return OpportunityType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-type', null, { reload: 'opportunity-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
