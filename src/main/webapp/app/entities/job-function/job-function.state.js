(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-function', {
            parent: 'entity',
            url: '/job-function',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.jobFunction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-function/job-functions.html',
                    controller: 'JobFunctionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobFunction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('job-function-detail', {
            parent: 'entity',
            url: '/job-function/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.jobFunction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-function/job-function-detail.html',
                    controller: 'JobFunctionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobFunction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'JobFunction', function($stateParams, JobFunction) {
                    return JobFunction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-function',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-function-detail.edit', {
            parent: 'job-function-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-function/job-function-dialog.html',
                    controller: 'JobFunctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobFunction', function(JobFunction) {
                            return JobFunction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-function.new', {
            parent: 'job-function',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-function/job-function-dialog.html',
                    controller: 'JobFunctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                upsaId: null,
                                name: null,
                                prefix: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-function', null, { reload: 'job-function' });
                }, function() {
                    $state.go('job-function');
                });
            }]
        })
        .state('job-function.edit', {
            parent: 'job-function',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-function/job-function-dialog.html',
                    controller: 'JobFunctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobFunction', function(JobFunction) {
                            return JobFunction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-function', null, { reload: 'job-function' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-function.delete', {
            parent: 'job-function',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-function/job-function-delete-dialog.html',
                    controller: 'JobFunctionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobFunction', function(JobFunction) {
                            return JobFunction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-function', null, { reload: 'job-function' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
