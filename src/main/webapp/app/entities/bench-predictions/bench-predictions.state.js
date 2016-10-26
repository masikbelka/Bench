(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bench-predictions', {
            parent: 'entity',
            url: '/bench-predictions',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchPredictions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-predictions/bench-predictions.html',
                    controller: 'BenchPredictionsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchPredictions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bench-predictions-detail', {
            parent: 'entity',
            url: '/bench-predictions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchPredictions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-predictions/bench-predictions-detail.html',
                    controller: 'BenchPredictionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchPredictions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BenchPredictions', function($stateParams, BenchPredictions) {
                    return BenchPredictions.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bench-predictions',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bench-predictions-detail.edit', {
            parent: 'bench-predictions-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-predictions/bench-predictions-dialog.html',
                    controller: 'BenchPredictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchPredictions', function(BenchPredictions) {
                            return BenchPredictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-predictions.new', {
            parent: 'bench-predictions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-predictions/bench-predictions-dialog.html',
                    controller: 'BenchPredictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdTime: null,
                                ignored: false,
                                ignoredDays: null,
                                readyToBench: false,
                                readyToProduction: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bench-predictions', null, { reload: 'bench-predictions' });
                }, function() {
                    $state.go('bench-predictions');
                });
            }]
        })
        .state('bench-predictions.edit', {
            parent: 'bench-predictions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-predictions/bench-predictions-dialog.html',
                    controller: 'BenchPredictionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchPredictions', function(BenchPredictions) {
                            return BenchPredictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-predictions', null, { reload: 'bench-predictions' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-predictions.delete', {
            parent: 'bench-predictions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-predictions/bench-predictions-delete-dialog.html',
                    controller: 'BenchPredictionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BenchPredictions', function(BenchPredictions) {
                            return BenchPredictions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-predictions', null, { reload: 'bench-predictions' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
