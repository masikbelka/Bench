(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingTypeDeleteController',BillingTypeDeleteController);

    BillingTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'BillingType'];

    function BillingTypeDeleteController($uibModalInstance, entity, BillingType) {
        var vm = this;

        vm.billingType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BillingType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
