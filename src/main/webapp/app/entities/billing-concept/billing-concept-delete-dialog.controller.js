(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingConceptDeleteController',BillingConceptDeleteController);

    BillingConceptDeleteController.$inject = ['$uibModalInstance', 'entity', 'BillingConcept'];

    function BillingConceptDeleteController($uibModalInstance, entity, BillingConcept) {
        var vm = this;

        vm.billingConcept = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BillingConcept.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
