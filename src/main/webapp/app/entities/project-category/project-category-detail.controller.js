(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectCategoryDetailController', ProjectCategoryDetailController);

    ProjectCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectCategory'];

    function ProjectCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectCategory) {
        var vm = this;

        vm.projectCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:projectCategoryUpdate', function(event, result) {
            vm.projectCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
