(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingTypeDetailController', BillingTypeDetailController);

    BillingTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BillingType'];

    function BillingTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, BillingType) {
        var vm = this;

        vm.billingType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:billingTypeUpdate', function(event, result) {
            vm.billingType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
