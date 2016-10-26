(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('probation-status', {
            parent: 'entity',
            url: '/probation-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.probationStatus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/probation-status/probation-statuses.html',
                    controller: 'ProbationStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('probationStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('probation-status-detail', {
            parent: 'entity',
            url: '/probation-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.probationStatus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/probation-status/probation-status-detail.html',
                    controller: 'ProbationStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('probationStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProbationStatus', function($stateParams, ProbationStatus) {
                    return ProbationStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'probation-status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('probation-status-detail.edit', {
            parent: 'probation-status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/probation-status/probation-status-dialog.html',
                    controller: 'ProbationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProbationStatus', function(ProbationStatus) {
                            return ProbationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('probation-status.new', {
            parent: 'probation-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/probation-status/probation-status-dialog.html',
                    controller: 'ProbationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                endDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('probation-status', null, { reload: 'probation-status' });
                }, function() {
                    $state.go('probation-status');
                });
            }]
        })
        .state('probation-status.edit', {
            parent: 'probation-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/probation-status/probation-status-dialog.html',
                    controller: 'ProbationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProbationStatus', function(ProbationStatus) {
                            return ProbationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('probation-status', null, { reload: 'probation-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('probation-status.delete', {
            parent: 'probation-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/probation-status/probation-status-delete-dialog.html',
                    controller: 'ProbationStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProbationStatus', function(ProbationStatus) {
                            return ProbationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('probation-status', null, { reload: 'probation-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
