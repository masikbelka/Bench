(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bench-comment-history', {
            parent: 'entity',
            url: '/bench-comment-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchCommentHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-histories.html',
                    controller: 'BenchCommentHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchCommentHistory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bench-comment-history-detail', {
            parent: 'entity',
            url: '/bench-comment-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.benchCommentHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-history-detail.html',
                    controller: 'BenchCommentHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('benchCommentHistory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BenchCommentHistory', function($stateParams, BenchCommentHistory) {
                    return BenchCommentHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bench-comment-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bench-comment-history-detail.edit', {
            parent: 'bench-comment-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-history-dialog.html',
                    controller: 'BenchCommentHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchCommentHistory', function(BenchCommentHistory) {
                            return BenchCommentHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-comment-history.new', {
            parent: 'bench-comment-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-history-dialog.html',
                    controller: 'BenchCommentHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                changeTime: null,
                                oldValue: null,
                                newValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bench-comment-history', null, { reload: 'bench-comment-history' });
                }, function() {
                    $state.go('bench-comment-history');
                });
            }]
        })
        .state('bench-comment-history.edit', {
            parent: 'bench-comment-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-history-dialog.html',
                    controller: 'BenchCommentHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BenchCommentHistory', function(BenchCommentHistory) {
                            return BenchCommentHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-comment-history', null, { reload: 'bench-comment-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bench-comment-history.delete', {
            parent: 'bench-comment-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bench-comment-history/bench-comment-history-delete-dialog.html',
                    controller: 'BenchCommentHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BenchCommentHistory', function(BenchCommentHistory) {
                            return BenchCommentHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bench-comment-history', null, { reload: 'bench-comment-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
