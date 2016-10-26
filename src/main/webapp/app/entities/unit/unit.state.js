(function() {
    'use strict';

    angular
        .module('benchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('unit', {
            parent: 'entity',
            url: '/unit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.unit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit/units.html',
                    controller: 'UnitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('unit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('unit-detail', {
            parent: 'entity',
            url: '/unit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'benchApp.unit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit/unit-detail.html',
                    controller: 'UnitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('unit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Unit', function($stateParams, Unit) {
                    return Unit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'unit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('unit-detail.edit', {
            parent: 'unit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit/unit-dialog.html',
                    controller: 'UnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unit', function(Unit) {
                            return Unit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit.new', {
            parent: 'unit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit/unit-dialog.html',
                    controller: 'UnitDialogController',
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
                    $state.go('unit', null, { reload: 'unit' });
                }, function() {
                    $state.go('unit');
                });
            }]
        })
        .state('unit.edit', {
            parent: 'unit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit/unit-dialog.html',
                    controller: 'UnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unit', function(Unit) {
                            return Unit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit', null, { reload: 'unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit.delete', {
            parent: 'unit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit/unit-delete-dialog.html',
                    controller: 'UnitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Unit', function(Unit) {
                            return Unit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit', null, { reload: 'unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
