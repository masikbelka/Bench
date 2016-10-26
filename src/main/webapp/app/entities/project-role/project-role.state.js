(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-role', {
            parent: 'entity',
            url: '/project-role',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectRole.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-role/project-roles.html',
                    controller: 'ProjectRoleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRole');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-role-detail', {
            parent: 'entity',
            url: '/project-role/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectRole.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-role/project-role-detail.html',
                    controller: 'ProjectRoleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRole');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectRole', function($stateParams, ProjectRole) {
                    return ProjectRole.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-role',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-role-detail.edit', {
            parent: 'project-role-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-role/project-role-dialog.html',
                    controller: 'ProjectRoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRole', function(ProjectRole) {
                            return ProjectRole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-role.new', {
            parent: 'project-role',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-role/project-role-dialog.html',
                    controller: 'ProjectRoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                upsaId: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-role', null, { reload: 'project-role' });
                }, function() {
                    $state.go('project-role');
                });
            }]
        })
        .state('project-role.edit', {
            parent: 'project-role',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-role/project-role-dialog.html',
                    controller: 'ProjectRoleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRole', function(ProjectRole) {
                            return ProjectRole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-role', null, { reload: 'project-role' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-role.delete', {
            parent: 'project-role',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-role/project-role-delete-dialog.html',
                    controller: 'ProjectRoleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectRole', function(ProjectRole) {
                            return ProjectRole.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-role', null, { reload: 'project-role' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
