(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bench-history', {
            parent: 'entity',
            url: '/bench-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-history/bench-histories.html',
                    controller: 'BenchHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchHistory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bench-history-detail', {
            parent: 'entity',
            url: '/bench-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-history/bench-history-detail.html',
                    controller: 'BenchHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchHistory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BenchHistory', function($stateParams, BenchHistory) {
                    return BenchHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bench-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bench-history-detail.edit', {
            parent: 'bench-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-history/bench-history-dialog.html',
                    controller: 'BenchHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchHistory', function(BenchHistory) {
                            return BenchHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-history.new', {
            parent: 'bench-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-history/bench-history-dialog.html',
                    controller: 'BenchHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdTime: null,
                                bench: false,
                                managerId: null,
                                validTo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bench-history', null, { reload: 'bench-history' });
                }, function() {
                    $state.go('bench-history');
                });
            }]
        })
        .state('bench-history.edit', {
            parent: 'bench-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-history/bench-history-dialog.html',
                    controller: 'BenchHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchHistory', function(BenchHistory) {
                            return BenchHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-history', null, { reload: 'bench-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-history.delete', {
            parent: 'bench-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-history/bench-history-delete-dialog.html',
                    controller: 'BenchHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BenchHistory', function(BenchHistory) {
                            return BenchHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-history', null, { reload: 'bench-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
