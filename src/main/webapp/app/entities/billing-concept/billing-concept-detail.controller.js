(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingConceptDetailController', BillingConceptDetailController);

    BillingConceptDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BillingConcept'];

    function BillingConceptDetailController($scope, $rootScope, $stateParams, previousState, entity, BillingConcept) {
        var vm = this;

        vm.billingConcept = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:billingConceptUpdate', function(event, result) {
            vm.billingConcept = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
