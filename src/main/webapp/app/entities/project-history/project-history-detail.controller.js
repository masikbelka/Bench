(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectHistoryDetailController', ProjectHistoryDetailController);

    ProjectHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectHistory', 'Employee', 'ProjectRole', 'Project'];

    function ProjectHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectHistory, Employee, ProjectRole, Project) {
        var vm = this;

        vm.projectHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:projectHistoryUpdate', function(event, result) {
            vm.projectHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
