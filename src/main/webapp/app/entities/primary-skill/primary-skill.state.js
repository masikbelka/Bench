(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('primary-skill', {
            parent: 'entity',
            url: '/primary-skill',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.primarySkill.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/primary-skill/primary-skills.html',
                    controller: 'PrimarySkillController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('primarySkill');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('primary-skill-detail', {
            parent: 'entity',
            url: '/primary-skill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.primarySkill.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/primary-skill/primary-skill-detail.html',
                    controller: 'PrimarySkillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('primarySkill');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrimarySkill', function($stateParams, PrimarySkill) {
                    return PrimarySkill.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'primary-skill',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('primary-skill-detail.edit', {
            parent: 'primary-skill-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/primary-skill/primary-skill-dialog.html',
                    controller: 'PrimarySkillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrimarySkill', function(PrimarySkill) {
                            return PrimarySkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('primary-skill.new', {
            parent: 'primary-skill',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/primary-skill/primary-skill-dialog.html',
                    controller: 'PrimarySkillDialogController',
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
                    $state.go('primary-skill', null, { reload: 'primary-skill' });
                }, function() {
                    $state.go('primary-skill');
                });
            }]
        })
        .state('primary-skill.edit', {
            parent: 'primary-skill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/primary-skill/primary-skill-dialog.html',
                    controller: 'PrimarySkillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrimarySkill', function(PrimarySkill) {
                            return PrimarySkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('primary-skill', null, { reload: 'primary-skill' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('primary-skill.delete', {
            parent: 'primary-skill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/primary-skill/primary-skill-delete-dialog.html',
                    controller: 'PrimarySkillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrimarySkill', function(PrimarySkill) {
                            return PrimarySkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('primary-skill', null, { reload: 'primary-skill' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
