(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityPositionDeleteController',OpportunityPositionDeleteController);

    OpportunityPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'OpportunityPosition'];

    function OpportunityPositionDeleteController($uibModalInstance, entity, OpportunityPosition) {
        var vm = this;

        vm.opportunityPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OpportunityPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
