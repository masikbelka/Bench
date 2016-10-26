(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityTypeDeleteController',OpportunityTypeDeleteController);

    OpportunityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'OpportunityType'];

    function OpportunityTypeDeleteController($uibModalInstance, entity, OpportunityType) {
        var vm = this;

        vm.opportunityType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OpportunityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
