(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityPositionDetailController', OpportunityPositionDetailController);

    OpportunityPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OpportunityPosition', 'Opportunity', 'ProjectRole', 'Employee'];

    function OpportunityPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, OpportunityPosition, Opportunity, ProjectRole, Employee) {
        var vm = this;

        vm.opportunityPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:opportunityPositionUpdate', function(event, result) {
            vm.opportunityPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
