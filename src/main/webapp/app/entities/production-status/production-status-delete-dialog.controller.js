(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProductionStatusDeleteController',ProductionStatusDeleteController);

    ProductionStatusDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductionStatus'];

    function ProductionStatusDeleteController($uibModalInstance, entity, ProductionStatus) {
        var vm = this;

        vm.productionStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductionStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
