(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-category', {
            parent: 'entity',
            url: '/project-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-category/project-categories.html',
                    controller: 'ProjectCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-category-detail', {
            parent: 'entity',
            url: '/project-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.projectCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-category/project-category-detail.html',
                    controller: 'ProjectCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectCategory', function($stateParams, ProjectCategory) {
                    return ProjectCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-category-detail.edit', {
            parent: 'project-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-category/project-category-dialog.html',
                    controller: 'ProjectCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectCategory', function(ProjectCategory) {
                            return ProjectCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-category.new', {
            parent: 'project-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-category/project-category-dialog.html',
                    controller: 'ProjectCategoryDialogController',
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
                    $state.go('project-category', null, { reload: 'project-category' });
                }, function() {
                    $state.go('project-category');
                });
            }]
        })
        .state('project-category.edit', {
            parent: 'project-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-category/project-category-dialog.html',
                    controller: 'ProjectCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectCategory', function(ProjectCategory) {
                            return ProjectCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-category', null, { reload: 'project-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-category.delete', {
            parent: 'project-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-category/project-category-delete-dialog.html',
                    controller: 'ProjectCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectCategory', function(ProjectCategory) {
                            return ProjectCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-category', null, { reload: 'project-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
