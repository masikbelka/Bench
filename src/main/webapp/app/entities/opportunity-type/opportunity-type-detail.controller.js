(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityTypeDetailController', OpportunityTypeDetailController);

    OpportunityTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OpportunityType'];

    function OpportunityTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, OpportunityType) {
        var vm = this;

        vm.opportunityType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:opportunityTypeUpdate', function(event, result) {
            vm.opportunityType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
