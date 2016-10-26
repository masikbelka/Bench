(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prediction-details', {
            parent: 'entity',
            url: '/prediction-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.predictionDetails.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-details/prediction-details.html',
                    controller: 'PredictionDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('predictionDetails');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prediction-details-detail', {
            parent: 'entity',
            url: '/prediction-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.predictionDetails.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-details/prediction-details-detail.html',
                    controller: 'PredictionDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('predictionDetails');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PredictionDetails', function($stateParams, PredictionDetails) {
                    return PredictionDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prediction-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prediction-details-detail.edit', {
            parent: 'prediction-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-details/prediction-details-dialog.html',
                    controller: 'PredictionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionDetails', function(PredictionDetails) {
                            return PredictionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-details.new', {
            parent: 'prediction-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-details/prediction-details-dialog.html',
                    controller: 'PredictionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                assignedToProject: false,
                                active: false,
                                removedFromProject: false,
                                maternityLeave: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prediction-details', null, { reload: 'prediction-details' });
                }, function() {
                    $state.go('prediction-details');
                });
            }]
        })
        .state('prediction-details.edit', {
            parent: 'prediction-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-details/prediction-details-dialog.html',
                    controller: 'PredictionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionDetails', function(PredictionDetails) {
                            return PredictionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-details', null, { reload: 'prediction-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-details.delete', {
            parent: 'prediction-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-details/prediction-details-delete-dialog.html',
                    controller: 'PredictionDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PredictionDetails', function(PredictionDetails) {
                            return PredictionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-details', null, { reload: 'prediction-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
