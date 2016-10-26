(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectRoleDetailController', ProjectRoleDetailController);

    ProjectRoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectRole'];

    function ProjectRoleDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectRole) {
        var vm = this;

        vm.projectRole = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:projectRoleUpdate', function(event, result) {
            vm.projectRole = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
