(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-history', {
            parent: 'entity',
            url: '/project-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-history/project-histories.html',
                    controller: 'ProjectHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectHistory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-history-detail', {
            parent: 'entity',
            url: '/project-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-history/project-history-detail.html',
                    controller: 'ProjectHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectHistory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectHistory', function($stateParams, ProjectHistory) {
                    return ProjectHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-history-detail.edit', {
            parent: 'project-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-history/project-history-dialog.html',
                    controller: 'ProjectHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectHistory', function(ProjectHistory) {
                            return ProjectHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-history.new', {
            parent: 'project-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-history/project-history-dialog.html',
                    controller: 'ProjectHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                workload: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-history', null, { reload: 'project-history' });
                }, function() {
                    $state.go('project-history');
                });
            }]
        })
        .state('project-history.edit', {
            parent: 'project-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-history/project-history-dialog.html',
                    controller: 'ProjectHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectHistory', function(ProjectHistory) {
                            return ProjectHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-history', null, { reload: 'project-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-history.delete', {
            parent: 'project-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-history/project-history-delete-dialog.html',
                    controller: 'ProjectHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectHistory', function(ProjectHistory) {
                            return ProjectHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-history', null, { reload: 'project-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
