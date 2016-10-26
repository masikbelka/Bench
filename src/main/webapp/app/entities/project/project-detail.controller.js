(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'BillingConcept', 'BillingType', 'ProjectCategory'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, BillingConcept, BillingType, ProjectCategory) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
