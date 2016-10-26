(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityDetailController', OpportunityDetailController);

    OpportunityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Opportunity', 'OpportunityType', 'OpportunityPosition', 'Location'];

    function OpportunityDetailController($scope, $rootScope, $stateParams, previousState, entity, Opportunity, OpportunityType, OpportunityPosition, Location) {
        var vm = this;

        vm.opportunity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:opportunityUpdate', function(event, result) {
            vm.opportunity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
