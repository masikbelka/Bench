(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('language-level', {
            parent: 'entity',
            url: '/language-level',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.languageLevel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/language-level/language-levels.html',
                    controller: 'LanguageLevelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('languageLevel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('language-level-detail', {
            parent: 'entity',
            url: '/language-level/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.languageLevel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/language-level/language-level-detail.html',
                    controller: 'LanguageLevelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('languageLevel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LanguageLevel', function($stateParams, LanguageLevel) {
                    return LanguageLevel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'language-level',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('language-level-detail.edit', {
            parent: 'language-level-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language-level/language-level-dialog.html',
                    controller: 'LanguageLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LanguageLevel', function(LanguageLevel) {
                            return LanguageLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('language-level.new', {
            parent: 'language-level',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language-level/language-level-dialog.html',
                    controller: 'LanguageLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                language: null,
                                speaking: null,
                                writing: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('language-level', null, { reload: 'language-level' });
                }, function() {
                    $state.go('language-level');
                });
            }]
        })
        .state('language-level.edit', {
            parent: 'language-level',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language-level/language-level-dialog.html',
                    controller: 'LanguageLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LanguageLevel', function(LanguageLevel) {
                            return LanguageLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language-level', null, { reload: 'language-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('language-level.delete', {
            parent: 'language-level',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language-level/language-level-delete-dialog.html',
                    controller: 'LanguageLevelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LanguageLevel', function(LanguageLevel) {
                            return LanguageLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language-level', null, { reload: 'language-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
