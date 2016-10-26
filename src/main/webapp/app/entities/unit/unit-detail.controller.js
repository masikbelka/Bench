(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('UnitDetailController', UnitDetailController);

    UnitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Unit', 'Employee', 'Location', 'PrimarySkill'];

    function UnitDetailController($scope, $rootScope, $stateParams, previousState, entity, Unit, Employee, Location, PrimarySkill) {
        var vm = this;

        vm.unit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:unitUpdate', function(event, result) {
            vm.unit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
