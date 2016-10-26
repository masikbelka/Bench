(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'ProbationStatus', 'Location', 'PrimarySkill', 'Title', 'LanguageLevel', 'ProductionStatus', 'JobFunction', 'Unit', 'BenchHistory', 'ProjectHistory', 'BenchPredictions', 'OpportunityPosition'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, ProbationStatus, Location, PrimarySkill, Title, LanguageLevel, ProductionStatus, JobFunction, Unit, BenchHistory, ProjectHistory, BenchPredictions, OpportunityPosition) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
